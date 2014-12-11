package com.googlecode.cqengine.collection.impl;

import com.googlecode.cqengine.engine.QueryEngineInternal;
import com.googlecode.cqengine.index.common.Factory;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.resultset.closeable.CloseableResultSet;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import static com.googlecode.cqengine.query.QueryFactory.all;
import static com.googlecode.cqengine.query.option.IsolationLevel.READ_UNCOMMITTED;
import static com.googlecode.cqengine.query.option.IsolationOption.isIsolationLevel;

/**
 * Extends {@link ConcurrentIndexedCollection} with support for READ_COMMITTED transaction isolation using
 * <a href="http://en.wikipedia.org/wiki/Multiversion_concurrency_control">Multiversion concurrency control</a>
 * (MVCC).
 * <p/>
 * <b>This class is feature complete but needs further testing!</b>
 *
 * @author Niall Gallagher
 */
public class TransactionalIndexedCollection<O> extends ConcurrentIndexedCollection<O> {

    final Class<O> objectType;
    final AtomicLong versionGenerator = new AtomicLong();
    volatile long currentVersion = 0;
    final ConcurrentNavigableMap<Long, Version> versions = new ConcurrentSkipListMap<Long, Version>();
    final Object writeMutex = new Object();

    static class Version<O> {
        final AtomicLong readersCount = new AtomicLong();
        final Semaphore writeLock = new Semaphore(0, true);
        final Collection<O> objectsToExclude;

        Version(Collection<O> objectsToExclude) {
            this.objectsToExclude = objectsToExclude;
        }
    }

    /**
     * {@inheritDoc}
     */
    public TransactionalIndexedCollection(Class<O> objectType, Factory<Set<O>> backingSetFactory, QueryEngineInternal<O> queryEngine) {
        super(backingSetFactory, queryEngine);
        this.objectType = objectType;
        // Set up initial version...
        incrementVersion(Collections.<O>emptySet());
    }

    public boolean applyTransaction(final Collection<O> objectsToAdd, final Collection<O> objectsToRemove) {
        synchronized (writeMutex) {
            if (objectsToAdd.isEmpty() && objectsToRemove.isEmpty()) {
                return false;
            }
            boolean modified = false;
            if (!objectsToAdd.isEmpty()) {
                // Configure new reading threads to exclude the objects we will add...
                incrementVersion(objectsToAdd);

                // Wait for this to take effect across all threads...
                waitForReadersOfPreviousVersionsToFinish();

                // Now add the given objects...
                modified = super.addAll(objectsToAdd);
            }
            if (!objectsToRemove.isEmpty()) {
                // Configure (or reconfigure) new reading threads to (instead) exclude the objects we will remove...
                incrementVersion(objectsToRemove);

                // Wait for this to take effect across all threads...
                waitForReadersOfPreviousVersionsToFinish();

                // Now remove the given objects...
                modified = super.removeAll(objectsToRemove) || modified;
            }

            // Finally, remove the exclusion...
            incrementVersion(Collections.<O>emptySet());

            // Wait for this to take effect across all threads...
            waitForReadersOfPreviousVersionsToFinish();

            return modified;
        }
    }

    void incrementVersion(Collection<O> objectsToExcludeFromThisVersion) {
        // Note we add the new Version object to the map before we update currentVersion,
        // to prevent a race condition where a reader could read the incremented version
        // but the object would not yet be in the map...
        long nextVersion = versionGenerator.incrementAndGet();
        versions.put(nextVersion, new Version<O>(objectsToExcludeFromThisVersion));
        currentVersion = nextVersion;
    }

    void waitForReadersOfPreviousVersionsToFinish() {
        Collection<Version> previousVersions = versions.headMap(versions.lastKey()).values();
        for (Iterator<Version> previousVersionsIterator = previousVersions.iterator(); previousVersionsIterator.hasNext(); ) {
            Version previousVersion = previousVersionsIterator.next();
            // Wait until the last reader (if there is one) of this previous version signals that it has finished...
            if (previousVersion.readersCount.get() != 0) {
                previousVersion.writeLock.acquireUninterruptibly();
            }

            // At this point readers of this previous version have finished.
            // Remove this version from memory...
            previousVersionsIterator.remove();
        }
    }

    @Override
    public boolean add(O o) {
        return applyTransaction(Collections.singleton(o), Collections.<O>emptySet());
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean remove(Object object) {
        return applyTransaction(Collections.<O>emptySet(), Collections.singleton((O)object));
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean addAll(Collection<? extends O> c) {
        return applyTransaction((Collection<O>)c, Collections.<O>emptySet());
    }


    @Override
    @SuppressWarnings({"unchecked"})
    public boolean removeAll(Collection<?> c) {
        return applyTransaction(Collections.<O>emptySet(), (Collection<O>)c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Set<O> objectsToRemove = new HashSet<O>();
        for (O object : retrieve(all(objectType))) {
            if (!c.contains(object)) {
                objectsToRemove.add(object);
            }
        }
        return applyTransaction(Collections.<O>emptySet(), objectsToRemove);
    }

    @Override
    public synchronized void clear() {
        retainAll(Collections.<Object>emptySet());
    }

    @Override
    public CloseableResultSet<O> retrieve(Query<O> query) {
        return retrieve(query, Collections.<Class<? extends QueryOption>, QueryOption<O>>emptyMap());
    }

    @Override
    public CloseableResultSet<O> retrieve(Query<O> query, Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        if (isIsolationLevel(queryOptions, READ_UNCOMMITTED)) {
            // Allow the query to read directly from the collection with no filtering overhead...
            return new CloseableResultSet<O>(super.retrieve(query, queryOptions)) {
                @Override
                public boolean isValid(O object) {
                    return true;
                }

                @Override
                public void close() {
                    super.close();
                }
            };
        }
        else {
            // Otherwise apply READ_COMMITTED isolation...

            // Get the Version object for the current version of the collection...
            final long thisVersionNumber = currentVersion;
            final Version thisVersion = versions.get(thisVersionNumber);
            // Increment the readers count to record that this thread is reading this version...
            thisVersion.readersCount.incrementAndGet();
            // Return the results matching the query such that:
            // - We filter out from the results any objects which might not be fully committed yet
            //   (as configured by writing threads for this version of the collection).
            // - When the ResultSet.close() method is called, we decrement the readers count
            //   to record that this thread is no longer reading this version.
            return new CloseableResultSet<O>(super.retrieve(query, queryOptions)) {
                @Override
                public boolean isValid(O object) {
                    return !thisVersion.objectsToExclude.contains(object);
                }

                @Override
                public void close() {
                    super.close();
                    // Decrement the readers count for this version...
                    long decrementedReadersCountForThisVersion = thisVersion.readersCount.decrementAndGet();
                    if (decrementedReadersCountForThisVersion == 0) {
                        // Readers count is zero so there are no other concurrent readers of this version *right now*.
                        // If the version of the collection *has not changed* since this thread started reading,
                        // then this is still the latest version of the collection, and it is possible that
                        // another thread will start reading from this version and increment the count above zero again
                        // after this thread finishes.
                        // OTOH, if the version of the collection *has changed* since this thread started reading,
                        // then no new threads can ever start reading from this version again, AND this is
                        // the last thread to finish reading this version.

                        if (thisVersionNumber != currentVersion) {
                            // This is the last thread to finish reading this version.
                            // Release the write lock, which notifies writing threads that this last reading thread has
                            // finished reading this version...
                            thisVersion.writeLock.release();
                        }
                    }
                }
            };
        }
    }
}