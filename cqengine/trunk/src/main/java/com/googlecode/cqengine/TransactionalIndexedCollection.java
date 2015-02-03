package com.googlecode.cqengine;

import com.googlecode.cqengine.index.common.DefaultConcurrentSetFactory;
import com.googlecode.cqengine.index.common.Factory;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.closeable.ValidatingCloseableResultSet;
import com.googlecode.cqengine.resultset.filter.FilteringResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import static com.googlecode.cqengine.query.QueryFactory.all;
import static com.googlecode.cqengine.query.QueryFactory.isolationLevel;
import static com.googlecode.cqengine.query.QueryFactory.queryOptions;
import static com.googlecode.cqengine.query.option.IsolationLevel.READ_UNCOMMITTED;
import static com.googlecode.cqengine.query.option.IsolationOption.isIsolationLevel;
import static com.googlecode.cqengine.query.option.QueryOptions.noQueryOptions;

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
        final Iterable<O> objectsToExclude;

        Version(Iterable<O> objectsToExclude) {
            this.objectsToExclude = objectsToExclude;
        }
    }

    /**
     * Creates a new {@link TransactionalIndexedCollection} with default settings.
     *
     * Uses {@link DefaultConcurrentSetFactory} to create the backing set.
     *
     * @param objectType The type of objects which will be stored in the collection
     */
    public TransactionalIndexedCollection(Class<O> objectType) {
        super(new DefaultConcurrentSetFactory<O>());
        this.objectType = objectType;
        // Set up initial version...
        incrementVersion(Collections.<O>emptySet());
    }

    /**
     * Creates a new {@link TransactionalIndexedCollection} which will use the given factory to create the backing set.
     *
     * @param objectType The type of objects which will be stored in the collection
     * @param backingSetFactory A factory which will create a concurrent {@link java.util.Set} in which objects
     * added to the indexed collection will be stored
     */
    public TransactionalIndexedCollection(Class<O> objectType, Factory<Set<O>> backingSetFactory) {
        super(backingSetFactory);
        this.objectType = objectType;
        // Set up initial version...
        incrementVersion(Collections.<O>emptySet());
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This method applies multi-version concurrency control by default such that update is seen to occur with
     * {@code READ_COMMITTED} transaction isolation by reading threads.
     * <p/>
     * However optionally for performance reasons, this may be overridden on a case-by-case basis, by supplying an
     * {@link com.googlecode.cqengine.query.option.IsolationOption} query option to this method requesting
     * {@link com.googlecode.cqengine.query.option.IsolationLevel#READ_UNCOMMITTED} transaction isolation instead.
     * In that case the modifications will be made directly to the collection, bypassing multi-version concurrency
     * control. This might be useful when making some modifications to the collection which do not need to be viewed
     * atomically.
     */
    @Override
    public boolean update(Iterable<O> objectsToRemove, Iterable<O> objectsToAdd) {
        return update(objectsToRemove, objectsToAdd, noQueryOptions());
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This method applies multi-version concurrency control by default such that update is seen to occur with
     * {@code READ_COMMITTED} transaction isolation by reading threads.
     * <p/>
     * However optionally for performance reasons, this may be overridden on a case-by-case basis, by supplying an
     * {@link com.googlecode.cqengine.query.option.IsolationOption} query option to this method requesting
     * {@link com.googlecode.cqengine.query.option.IsolationLevel#READ_UNCOMMITTED} transaction isolation instead.
     * In that case the modifications will be made directly to the collection, bypassing multi-version concurrency
     * control. This might be useful when making some modifications to the collection which do not need to be viewed
     * atomically.
     */
    @Override
    public boolean update(final Iterable<O> objectsToRemove, final Iterable<O> objectsToAdd, QueryOptions queryOptions) {
        if (isIsolationLevel(queryOptions, READ_UNCOMMITTED)) {
            // Write directly to the collection with no MVCC overhead...
            return super.update(objectsToRemove, objectsToAdd, queryOptions);
        }
        // Otherwise apply MVCC to support READ_COMMITTED isolation...
        synchronized (writeMutex) {
            Iterator<O> objectsToRemoveIterator = objectsToRemove.iterator();
            Iterator<O> objectsToAddIterator = objectsToAdd.iterator();
            if (!objectsToRemoveIterator.hasNext() && !objectsToAddIterator.hasNext()) {
                return false;
            }
            boolean modified = false;
            if (objectsToAddIterator.hasNext()) {
                // Configure new reading threads to exclude the objects we will add...
                incrementVersion(objectsToAdd);

                // Wait for this to take effect across all threads...
                waitForReadersOfPreviousVersionsToFinish();

                // Now add the given objects...
                modified = doAddAll(objectsToAdd);
            }
            if (objectsToRemoveIterator.hasNext()) {
                // Configure (or reconfigure) new reading threads to (instead) exclude the objects we will remove...
                incrementVersion(objectsToRemove);

                // Wait for this to take effect across all threads...
                waitForReadersOfPreviousVersionsToFinish();

                // Now remove the given objects...
                modified = doRemoveAll(objectsToRemove) || modified;
            }

            // Finally, remove the exclusion...
            incrementVersion(Collections.<O>emptySet());

            // Wait for this to take effect across all threads...
            waitForReadersOfPreviousVersionsToFinish();

            return modified;
        }
    }

    void incrementVersion(Iterable<O> objectsToExcludeFromThisVersion) {
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
        return update(Collections.<O>emptySet(), Collections.singleton(o));
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean remove(Object object) {
        return update(Collections.singleton((O) object), Collections.<O>emptySet());
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean addAll(Collection<? extends O> c) {
        return update(Collections.<O>emptySet(), (Collection<O>) c);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean removeAll(Collection<?> c) {
        return update((Collection<O>) c, Collections.<O>emptySet());
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        // Prepare to lazily read all objects from the collection *without using MVCC* (..READ_UNCOMMITTED),
        // and lazily filter it to return only the subsetToRemove objects which are not in the given collection...
        FilteringResultSet<O> subsetToRemove = new FilteringResultSet<O>(
                retrieve(all(objectType), queryOptions(isolationLevel(READ_UNCOMMITTED))), noQueryOptions()) {
            @Override
            public boolean isValid(O object, QueryOptions queryOptions) {
                return !c.contains(object);
            }
        };
        // We don't need to use MVCC above, because subsetToRemove will be iterated inside the synchronized update()
        // method...
        return update(subsetToRemove, Collections.<O>emptySet());
    }

    @Override
    public synchronized void clear() {
        retainAll(Collections.emptySet());
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query) {
        return retrieve(query, noQueryOptions());
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        if (isIsolationLevel(queryOptions, READ_UNCOMMITTED)) {
            // Allow the query to read directly from the collection with no filtering overhead...
            return new ValidatingCloseableResultSet<O>(super.retrieve(query, queryOptions), queryOptions) {
                @Override
                public boolean isValid(O object, QueryOptions queryOptions) {
                    return true;
                }

                @Override
                public void close() {
                    super.close();
                }
            };
        }
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
        return new ValidatingCloseableResultSet<O>(super.retrieve(query, queryOptions), queryOptions) {
            @Override
            public boolean isValid(O object, QueryOptions queryOptions) {
                return !iterableContains(thisVersion.objectsToExclude, object);
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

    boolean doAddAll(Iterable<O> objects) {
        if (objects instanceof Collection) {
            return super.addAll((Collection<O>) objects);
        }
        else {
            boolean modified = false;
            for (O object : objects) {
                modified = super.add(object) || modified;
            }
            return modified;
        }
    }

    boolean doRemoveAll(Iterable<O> objects) {
        if (objects instanceof Collection) {
            return super.removeAll((Collection<O>) objects);
        } else {
            boolean modified = false;
            for (O object : objects) {
                modified = super.remove(object) || modified;
            }
            return modified;
        }
    }

    static <O> boolean iterableContains(Iterable<O> objects, O o) {
        if (objects instanceof Collection) {
            return ((Collection<?>)objects).contains(o);
        }
        else if (objects instanceof ResultSet) {
            return ((ResultSet<O>)objects).contains(o);
        }
        else {
            return IteratorUtil.iterableContains(objects, o);
        }
    }
}