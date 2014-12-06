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

import static com.googlecode.cqengine.query.QueryFactory.*;

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
    final AtomicLong currentVersion = new AtomicLong();
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
            // Increment the version and configure new reading threads to exclude the objects which we will add...
            incrementVersion(objectsToAdd);

            // Wait for this to take effect across all threads...
            waitForReadersOfPreviousVersionsToDrain();

            // Now add the given objects...
            boolean modified = super.addAll(objectsToAdd);

            // Now configure new reading threads to stop excluding the objects we added, and instead to start
            // excluding the objects we will remove.
            // Effectively, this is an atomic switch on a thread-by-thread basis between two versions of the collection...
            incrementVersion(objectsToRemove);

            // Wait for this to take effect across all threads...
            waitForReadersOfPreviousVersionsToDrain();

            // Now remove the given objects...
            modified = super.removeAll(objectsToRemove) || modified;

            // Wait for this to take effect across all threads...
            waitForReadersOfPreviousVersionsToDrain();

            // Finally, remove the exclusion...
            incrementVersion(Collections.<O>emptySet());

            // Wait for this to take effect across all threads...
            waitForReadersOfPreviousVersionsToDrain();

            return modified;
        }
    }

    synchronized void incrementVersion(Collection<O> objectsToExcludeFromThisVersion) {
        // Note we add the new Version object to the map before we increment,
        // to prevent a race condition where a reader could read the incremented version
        // but the object would not yet be in the map...
        versions.put(currentVersion.get() + 1, new Version<O>(objectsToExcludeFromThisVersion));
        currentVersion.incrementAndGet();
    }

    void waitForReadersOfPreviousVersionsToDrain() {
        Collection<Version> previousVersions = versions.headMap(versions.lastKey()).values();
        for (Iterator<Version> previousVersionsIterator = previousVersions.iterator(); previousVersionsIterator.hasNext(); ) {
            Version previousVersion = previousVersionsIterator.next();
            // Wait until the last reader (if there is one) of this previous version signals that it has finished...
            if (previousVersion.readersCount.get() != 0) {
                previousVersion.writeLock.acquireUninterruptibly();
            }

            // At this point readers of this previous version have drained.
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

        final Version thisVersion = versions.get(currentVersion.get());

        thisVersion.readersCount.incrementAndGet();

        return new CloseableResultSet<O>(super.retrieve(query, queryOptions)) {
            @Override
            public boolean isValid(O object) {
                return !thisVersion.objectsToExclude.contains(object);
            }

            @Override
            public void close() {
                super.close();
                if (thisVersion.readersCount.decrementAndGet() == 0) {
                    thisVersion.writeLock.release();
                }
            }
        };
    }
}
