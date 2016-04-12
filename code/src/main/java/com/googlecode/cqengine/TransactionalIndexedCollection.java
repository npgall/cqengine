/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine;

import com.googlecode.cqengine.index.support.DefaultConcurrentSetFactory;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.ArgumentValidationOption;
import com.googlecode.cqengine.query.option.FlagsEnabled;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.closeable.CloseableFilteringResultSet;
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
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;

/**
 * Extends {@link ConcurrentIndexedCollection} with support for READ_COMMITTED transaction isolation using
 * <a href="http://en.wikipedia.org/wiki/Multiversion_concurrency_control">Multiversion concurrency control</a>
 * (MVCC).
 * <p/>
 * A transaction is composed of a set of objects to be added to the collection, and a set of objects to be removed from
 * the collection. Either one of those sets can be empty, so this supports bulk <i>atomic addition</i> and <i>atomic
 * removal</i> of objects from the collection. But if both sets are non-empty then it allows bulk
 * <i>atomic replacement</i> of objects in the collection.
 * <p/>
 * <b>Atomically replacing objects</b><br/>
 * A restriction is that if you want to replace objects in the collection, then for each object to be removed,
 * {@code objectToRemove.equals(objectToAdd)} should return {@code false}. That is, the sets of objects to be removed
 * and added must be <i>disjoint</i>. You can achieve that by adding a hidden version field in your object as follows:
 * <pre>
 * <code>
 *
 * public class Car {
 *
 *     static final AtomicLong VERSION_GENERATOR = new AtomicLong();
 *
 *     final int carId;
 *     final String name;
 *     final long version = VERSION_GENERATOR.incrementAndGet();
 *
 *     public Car(int carId, String name) {
 *         this.carId = carId;
 *         this.name = name;
 *     }
 *
 *     {@literal @Override}
 *     public int hashCode() {
 *         return carId;
 *     }
 *
 *     {@literal @Override}
 *     public boolean equals(Object o) {
 *         if (this == o) return true;
 *         if (o == null || getClass() != o.getClass()) return false;
 *         Car other = (Car) o;
 *         if (this.carId != other.carId) return false;
 *         if (this.version != other.version) return false;
 *         return true;
 *     }
 * }
 * </code>
 * </pre>
 * <b>Argument validation</b><br/>
 * By default this class will <b>validate</b> that objects to be replaced adhere to the requirement above, which adds
 * some overhead to query processing. Therefore once applications are confirmed as being compliant, this validation
 * can be switched off by supplying a QueryOption. See the JavaDoc on the {@code update()} method for details.
 * @see #update(Iterable, Iterable, com.googlecode.cqengine.query.option.QueryOptions)
 *
 * @author Niall Gallagher
 */
public class TransactionalIndexedCollection<O> extends ConcurrentIndexedCollection<O> {

    /**
     * A query option flag which can be supplied to the update method to control the replacement behaviour.
     */
    public static final String STRICT_REPLACEMENT = "STRICT_REPLACEMENT";

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
     * Creates a new {@link TransactionalIndexedCollection} with default settings, using {@link OnHeapPersistence}.
     *
     * @param objectType The type of objects which will be stored in the collection
     */
    public TransactionalIndexedCollection(Class<O> objectType) {
        this(objectType, new OnHeapPersistence<O>());
    }

    /**
     * Creates a new {@link TransactionalIndexedCollection} which will use the given factory to create the backing set.
     *
     * @param objectType The type of objects which will be stored in the collection
     * @param persistence The {@link Persistence} implementation which will create a concurrent {@link java.util.Set}
     *                    in which objects added to the indexed collection will be stored, and which will provide
     *                    access to the underlying storage of indexes.
     */
    public TransactionalIndexedCollection(Class<O> objectType, Persistence<O> persistence) {
        super(persistence);
        this.objectType = objectType;
        // Set up initial version...
        incrementVersion(Collections.<O>emptySet());
    }

    /**
     * This is the same as calling without any query options:
     * {@link #update(Iterable, Iterable, com.googlecode.cqengine.query.option.QueryOptions)}.
     * <p/>
     * @see #update(Iterable, Iterable, com.googlecode.cqengine.query.option.QueryOptions)
     */
    @Override
    public boolean update(Iterable<O> objectsToRemove, Iterable<O> objectsToAdd) {
        return update(objectsToRemove, objectsToAdd, noQueryOptions());
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This method applies multi-version concurrency control by default such that the update is seen to occur
     * <i>atomically</i> with {@code READ_COMMITTED} transaction isolation by reading threads.
     * <p/>
     * For performance reasons, transaction isolation may (optionally) be overridden on a case-by-case basis by
     * supplying an {@link com.googlecode.cqengine.query.option.IsolationOption} query option to this method requesting
     * {@link com.googlecode.cqengine.query.option.IsolationLevel#READ_UNCOMMITTED} transaction isolation instead.
     * In that case the modifications will be made directly to the collection, bypassing multi-version concurrency
     * control. This might be useful when making some modifications to the collection which do not need to be viewed
     * atomically.
     * <p/>
     * <b>Atomically replacing objects and argument validation</b><br/>
     * As discussed in this class' JavaDoc, the sets of objects to be removed and objects to be added supplied to this
     * method as arguments, must be <i>disjoint</i> and <i>this method will validate that this is the case by
     * default</i>.
     * <p/>
     * To disable this validation for performance reasons, supply QueryOption: <code>argumentValidation(SKIP)</code>.
     * <p/>
     * Note that if the application disables this validation and proceeds to call this method with non-compliant
     * arguments anyway, then indexes may become inconsistent. Validation should only be skipped when it is certain that
     * the application will be compliant.
     * <p/>
     * <b>Atomically replacing objects with STRICT_REPLACEMENT</b><br/>
     * By default, this method will not check if the objects to be removed are actually contained in the collection.
     * If any objects to be removed are not actually contained, then the objects to be added will be added anyway.
     * <p/>
     * Applications requiring "strict" object replacement, can supply QueryOption:
     * <code>enableFlags(TransactionalIndexedCollection.STRICT_REPLACEMENT))</code>.
     * If this query option is supplied, then a check will be performed to ensure that all of the objects to be removed
     * are actually contained in the collection. If any objects to be removed are not contained, then the collection
     * will not be modified and this method will return false.
     */
    @Override
    public boolean update(final Iterable<O> objectsToRemove, final Iterable<O> objectsToAdd, QueryOptions queryOptions) {
        if (isIsolationLevel(queryOptions, READ_UNCOMMITTED)) {
            // Write directly to the collection with no MVCC overhead...
            return super.update(objectsToRemove, objectsToAdd, queryOptions);
        }
        // By default, validate that the sets of objectsToRemove and objectsToAdd are disjoint...
        if (!ArgumentValidationOption.isSkip(queryOptions)) {
            ensureUpdateSetsAreDisjoint(objectsToRemove, objectsToAdd);
        }

        // Otherwise apply MVCC to support READ_COMMITTED isolation...
        synchronized (writeMutex) {
            queryOptions = openRequestScopeResourcesIfNecessary(queryOptions);
            try {
                Iterator<O> objectsToRemoveIterator = objectsToRemove.iterator();
                Iterator<O> objectsToAddIterator = objectsToAdd.iterator();
                if (!objectsToRemoveIterator.hasNext() && !objectsToAddIterator.hasNext()) {
                    return false;
                }
                if (FlagsEnabled.isFlagEnabled(queryOptions, STRICT_REPLACEMENT)) {
                    if (!collectionContainsAllIterable(getObjectStoreAsSet(queryOptions), objectsToRemove)) {
                        return false;
                    }
                }
                boolean modified = false;
                if (objectsToAddIterator.hasNext()) {
                    // Configure new reading threads to exclude the objects we will add...
                    incrementVersion(objectsToAdd);

                    // Wait for this to take effect across all threads...
                    waitForReadersOfPreviousVersionsToFinish();

                    // Now add the given objects...
                    modified = doAddAll(objectsToAdd, queryOptions);
                }
                if (objectsToRemoveIterator.hasNext()) {
                    // Configure (or reconfigure) new reading threads to (instead) exclude the objects we will remove...
                    incrementVersion(objectsToRemove);

                    // Wait for this to take effect across all threads...
                    waitForReadersOfPreviousVersionsToFinish();

                    // Now remove the given objects...
                    modified = doRemoveAll(objectsToRemove, queryOptions) || modified;
                }

                // Finally, remove the exclusion...
                incrementVersion(Collections.<O>emptySet());

                // Wait for this to take effect across all threads...
                waitForReadersOfPreviousVersionsToFinish();

                return modified;
            }
            finally {
                closeRequestScopeResourcesIfNecessary(queryOptions);
            }
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
        Query<O> query = all(objectType);
        FilteringResultSet<O> subsetToRemove = new FilteringResultSet<O>( // TODO: should this be closed?
                retrieve(query, queryOptions(isolationLevel(READ_UNCOMMITTED))), query, noQueryOptions()) {
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
            return new CloseableFilteringResultSet<O>(super.retrieve(query, queryOptions), query, queryOptions) {
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
        return new CloseableFilteringResultSet<O>(super.retrieve(query, queryOptions), query, queryOptions) {
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

    static <O> boolean collectionContainsAllIterable(Collection<O> collection, Iterable<O> candidates) {
        if (candidates instanceof Collection) {
            return collection.containsAll((Collection<?>) candidates);
        }
        for (O candidate : candidates) {
            if (!collection.contains(candidate)) {
                return false;
            }
        }
        return true;
    }

    static <O> void ensureUpdateSetsAreDisjoint(final Iterable<O> objectsToRemove, final Iterable<O> objectsToAdd) {
        for (O objectToRemove : objectsToRemove) {
            if (iterableContains(objectsToAdd, objectToRemove)) {
                throw new IllegalArgumentException("The sets of objectsToRemove and objectsToAdd are not disjoint [for all objectsToRemove, objectToRemove.equals(objectToAdd) must return false].");
            }
        }
    }
}