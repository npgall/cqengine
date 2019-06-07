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

import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.ArgumentValidationOption;
import com.googlecode.cqengine.query.option.FlagsEnabled;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.closeable.CloseableFilteringResultSet;
import com.googlecode.cqengine.resultset.closeable.CloseableResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.option.IsolationLevel.READ_UNCOMMITTED;
import static com.googlecode.cqengine.query.option.IsolationOption.isIsolationLevel;

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
    volatile Version currentVersion;
    final Object writeMutex = new Object();

    final AtomicLong versionNumberGenerator = new AtomicLong();

    class Version {
        // New reading threads acquire read locks from the current Version.
        // Writing threads create a new Version at each step of the MVCC algorithm,
        // then acquire write locks from the previous Version before moving onto the next step.
        // Therefore reading threads always acquire read locks from an uncontended Version,
        // and writing threads wait for threads reading the previous version to finish before
        // moving onto the next step.
        final ReadWriteLock lock = new ReentrantReadWriteLock();
        final Iterable<O> objectsToExclude;

        // versionNumber is not actually used by the MVCC algorithm,
        // it is only useful when debugging and for unit tests...
        final long versionNumber = versionNumberGenerator.incrementAndGet();

        Version(Iterable<O> objectsToExclude) {
            this.objectsToExclude = objectsToExclude;
        }
    }

    /**
     * Creates a new {@link TransactionalIndexedCollection} with default settings, using {@link OnHeapPersistence}.
     *
     * @param objectType The type of objects which will be stored in the collection
     */
    @SuppressWarnings("unchecked")
    public TransactionalIndexedCollection(Class<O> objectType) {
        this(objectType, OnHeapPersistence.<O>withoutPrimaryKey());
    }

    /**
     * Creates a new {@link TransactionalIndexedCollection} which will use the given factory to create the backing set.
     *
     * @param objectType The type of objects which will be stored in the collection
     * @param persistence The {@link Persistence} implementation which will create a concurrent {@link java.util.Set}
     *                    in which objects added to the indexed collection will be stored, and which will provide
     *                    access to the underlying storage of indexes.
     */
    public <A extends Comparable<A>> TransactionalIndexedCollection(Class<O> objectType, Persistence<O, A> persistence) {
        super(persistence);
        this.objectType = objectType;
        // Set up initial version...
        this.currentVersion = new Version(Collections.<O>emptySet());
    }

    /**
     * Creates a new Version and sets it as the current version and configures that version to exclude the given
     * objects from results returned to threads which will read that version.
     * Then, acquires the write lock on the previous Version, which will cause this (writing) thread
     * to block until all threads reading the previous version have finished reading that version.
     * @param objectsToExcludeFromNextVersion Objects to exclude from the next version
     */
    void incrementVersion(Iterable<O> objectsToExcludeFromNextVersion) {
        Version previousVersion = this.currentVersion;
        this.currentVersion = new Version(objectsToExcludeFromNextVersion);
        previousVersion.lock.writeLock().lock();
    }

    /**
     * Acquires the (uncontended) read lock for the current version and returns the current Version object,
     * while handling an edge case that a writing thread might be in the middle of incrementing the version,
     * in which case this method will refresh the current version until successful.
     */
    Version acquireReadLockForCurrentVersion() {
        Version currentVersion;
        do {
            currentVersion = this.currentVersion;
        } while (!currentVersion.lock.readLock().tryLock());
        return currentVersion;
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
                    if (!objectStoreContainsAllIterable(objectStore, objectsToRemove, queryOptions)) {
                        return false;
                    }
                }
                boolean modified = false;
                if (objectsToAddIterator.hasNext()) {
                    // Configure new reading threads to exclude the objects we will add,
                    // and then wait for threads reading previous versions to finish...
                    incrementVersion(objectsToAdd);

                    // Now add the given objects...
                    modified = doAddAll(objectsToAdd, queryOptions);
                }
                if (objectsToRemoveIterator.hasNext()) {
                    // Configure (or reconfigure) new reading threads to (instead) exclude the objects we will remove,
                    // and then wait for threads reading previous versions to finish...
                    incrementVersion(objectsToRemove);

                    // Now remove the given objects...
                    modified = doRemoveAll(objectsToRemove, queryOptions) || modified;
                }

                // Finally, remove the exclusion,
                // and then wait for this to take effect across all threads...
                incrementVersion(Collections.<O>emptySet());

                return modified;
            }
            finally {
                closeRequestScopeResourcesIfNecessary(queryOptions);
            }
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
        synchronized (writeMutex) {
            QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
            try {
                // Copy objects into a new set removing nulls.
                // CQEngine does not permit nulls in queries, but the spec of {@link Collection#retainAll} does.
                Set<O> objectsToRetain = new HashSet<O>(c.size());
                for (Object object : c) {
                    if (object != null) {
                        @SuppressWarnings("unchecked") O o = (O)object;
                        objectsToRetain.add(o);
                    }
                }
                // Prepare a query which will match objects in the collection which are not contained in the given
                // collection of objects to retain and therefore which need to be removed from the collection...
                // We prepare the query to use the same QueryOptions as above.
                // Any resources opened for the query which need to be closed,
                // will be added to the QueryOptions and closed at the end of this method.
                @SuppressWarnings("unchecked")
                ResultSet<O> objectsToRemove = super.retrieve(not(in(selfAttribute(objectType), objectsToRetain)), queryOptions);

                Iterator<O> objectsToRemoveIterator = objectsToRemove.iterator();
                if (!objectsToRemoveIterator.hasNext()) {
                    return false;
                }

                // Configure new reading threads to exclude the objects we will remove,
                // then wait for this to take effect across all threads...
                incrementVersion(objectsToRemove);

                // Now remove the given objects...
                boolean modified = doRemoveAll(objectsToRemove, queryOptions);

                // Finally, remove the exclusion,
                // then wait for this to take effect across all threads...
                incrementVersion(Collections.<O>emptySet());

                return modified;
            }
            finally {
                closeRequestScopeResourcesIfNecessary(queryOptions);
            }
        }
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
            return super.retrieve(query, queryOptions);
        }
        // Otherwise apply READ_COMMITTED isolation...

        // Get the current Version and acquire an (uncontended) read lock on it...
        final Version thisVersion = acquireReadLockForCurrentVersion();
        try {

            // Return the results matching the query such that:
            // - STEP 1: When the ResultSet.close() method is called, we decrement the readers count
            //   to record that this thread is no longer reading this version.
            // - STEP 2: We filter out from the results any objects which might not be fully committed yet
            //   (as configured by writing threads for this version of the collection).
            ResultSet<O> results = super.retrieve(query, queryOptions);

            // STEP 1: Wrap the results to intercept ResultSet.close()...
            CloseableResultSet<O> versionReadingResultSet = new CloseableResultSet<O>(results, query, queryOptions) {
                @Override
                public void close() {
                    super.close();
                    // Release the read lock for this version when ResultSet.close() is called...
                    thisVersion.lock.readLock().unlock();
                }
            };
            // STEP 2: Apply filtering as necessary...
            if (thisVersion.objectsToExclude.iterator().hasNext()) {
                // Apply the filtering to omit uncommitted objects...
                return new CloseableFilteringResultSet<O>(versionReadingResultSet, query, queryOptions) {
                    @Override
                    public boolean isValid(O object, QueryOptions queryOptions) {
                        @SuppressWarnings("unchecked")
                        Iterable<O> objectsToExclude = (Iterable<O>) thisVersion.objectsToExclude;
                        return !iterableContains(objectsToExclude, object);
                    }
                };
            } else {
                // As there were no objects to exclude, then we can return the results directly without filtering...
                return versionReadingResultSet;
            }
        }
        catch (RuntimeException e) {
            // Release the read lock we acquired above, because due to throwing an exception,
            // we will not be returning a CloseableResultSet which otherwise would allow it to be released later...
            thisVersion.lock.readLock().unlock();
            throw e;
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

    static <O> boolean objectStoreContainsAllIterable(ObjectStore<O> objectStore, Iterable<O> candidates, QueryOptions queryOptions) {
        if (candidates instanceof Collection) {
            return objectStore.containsAll((Collection<?>) candidates, queryOptions);
        }
        for (O candidate : candidates) {
            if (!objectStore.contains(candidate, queryOptions)) {
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