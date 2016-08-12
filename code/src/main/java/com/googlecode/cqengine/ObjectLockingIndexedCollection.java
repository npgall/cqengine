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

import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.index.support.CloseableRequestResources;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Extends {@link ConcurrentIndexedCollection} with some specialized locking for applications in
 * which multiple threads might try to add or remove the <b>same object</b> to/from an indexed collection
 * <i>concurrently</i>. In this context the <i>same object</i> refers to either the same object instance, OR two object
 * instances having the same hash code and being equal according to their {@link #equals(Object)} methods.
 * <p/>
 * The {@link ConcurrentIndexedCollection} superclass is thread-safe in cases where the application will add/remove
 * <i>different</i> objects concurrently. This implementation adds safeguards around adding/removing the <i>same</i>
 * object concurrently, with some additional overhead.
 * <p/>
 * Reading threads are never blocked; reads remain lock-free as in the superclass.
 * <p/>
 * This class is currently implemented with a
 * <a href="http://code.google.com/p/guava-libraries/wiki/StripedExplained">striped lock</a> (although not the Guava
 * implementation). As such, the hash code of an object is not assigned a unique lock, but there is instead a
 * many-to-fewer association between hash codes and locks. Some different objects will therefore contend for the same
 * lock. The number of stripes (locks) is configurable, allowing this ratio and likelihood of contention to be
 * controlled, trading memory overhead of a large number of locks, against likelihood of contention. Two or more
 * modifications for the same object, will always block each other. Two or more modifications for different objects
 * will <i>usually not</i> block each other, but occasionally might.
 * <p/>
 * Although this class is currently implemented with a striped lock, this might be replaced with a
 * <a href="http://gee.cs.oswego.edu/dl/jsr166/dist/jsr166edocs/jsr166e/StampedLock.html">stamped lock</a>.
 * Applications should not rely on the locking behaviour of this class, except to guard against the concurrent
 * access scenario stated above.
 *
 * @author Niall Gallagher
 */
public class ObjectLockingIndexedCollection<O> extends ConcurrentIndexedCollection<O> {

    final StripedLock stripedLock;

    /**
     * Creates a new {@link ObjectLockingIndexedCollection} with default settings, using {@link OnHeapPersistence}
     * and a default concurrency level 64.
     */
    @SuppressWarnings("unchecked")
    public ObjectLockingIndexedCollection() {
        this(OnHeapPersistence.<O>withoutPrimaryKey(), 64);
    }

    /**
     * Creates a new {@link ObjectLockingIndexedCollection} which will use the given persistence to create the backing
     * set, and a default concurrency level 64.
     *
     * @param persistence The {@link Persistence} implementation which will create a concurrent {@link java.util.Set}
     *                    in which objects added to the indexed collection will be stored, and which will provide
     *                    access to the underlying storage of indexes.
     */
    public <A extends Comparable<A>> ObjectLockingIndexedCollection(Persistence<O, A> persistence) {
        this(persistence, 64);
    }

    /**
     * Creates a new {@link ObjectLockingIndexedCollection} using {@link OnHeapPersistence} and the given concurrency
     * level.
     *
     * @param concurrencyLevel The estimated number of concurrently updating threads
     */
    @SuppressWarnings("unchecked")
    public <A extends Comparable<A>> ObjectLockingIndexedCollection(int concurrencyLevel) {
        this(OnHeapPersistence.<O>withoutPrimaryKey(), concurrencyLevel);
    }

    /**
     * Creates a new {@link ObjectLockingIndexedCollection}, which will use the given persistence to create the backing
     * set, and the given concurrency level.
     *
     * @param persistence The {@link Persistence} implementation which will create a concurrent {@link java.util.Set}
     *                    in which objects added to the indexed collection will be stored, and which will provide
     *                    access to the underlying storage of indexes.
     * @param concurrencyLevel The estimated number of concurrently updating threads
     */
    public <A extends Comparable<A>> ObjectLockingIndexedCollection(Persistence<O, A> persistence, int concurrencyLevel) {
        super(persistence);
        this.stripedLock = new StripedLock(concurrencyLevel);
    }

    static class StripedLock {
        final int concurrencyLevel;
        final ReentrantLock[] locks;

        StripedLock(int concurrencyLevel) {
            this.concurrencyLevel = concurrencyLevel;
            this.locks = new ReentrantLock[concurrencyLevel];
            for (int i = 0; i < concurrencyLevel; i++) {
                locks[i] = new ReentrantLock();
            }
        }

        ReentrantLock getLockForObject(Object object) {
            int hashCode = object.hashCode();
            return locks[Math.abs(hashCode % concurrencyLevel)];
        }
    }

    // ----------- Collection Mutator Methods -------------


    /**
     * {@inheritDoc}
     */
    @Override
    public CloseableIterator<O> iterator() {
        return new CloseableIterator<O>() {
            final QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);

            private final CloseableIterator<O> collectionIterator = objectStore.iterator(queryOptions);
            boolean autoClosed = false;

            @Override
            public boolean hasNext() {
                boolean hasNext = collectionIterator.hasNext();
                if (!hasNext) {
                    close();
                    autoClosed = true;
                }
                return hasNext;
            }

            private O currentObject = null;
            @Override
            public O next() {
                O next = collectionIterator.next();
                currentObject = next;
                return next;
            }

            @Override
            public void remove() {
                if (currentObject == null) {
                    throw new IllegalStateException();
                }
                Lock lock = stripedLock.getLockForObject(currentObject);
                lock.lock();
                try {
                    // Handle an edge case where we might have retrieved the last object and called close() automatically,
                    // but then the application calls remove() so we have to reopen request-scope resources temporarily
                    // to remove the last object...
                    if (autoClosed) {
                        ObjectLockingIndexedCollection.this.remove(currentObject); // reopens resources temporarily
                    }
                    else {
                        doRemoveAll(Collections.singleton(currentObject), queryOptions); // uses existing resources
                    }
                    currentObject = null;
                }
                finally {
                    lock.unlock();
                }
            }

            @Override
            public void close() {
                CloseableRequestResources.closeQuietly(collectionIterator);
                closeRequestScopeResourcesIfNecessary(queryOptions);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(O o) {
        Lock lock = stripedLock.getLockForObject(o);
        lock.lock();
        try {
            return super.add(o);
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object object) {
        Lock lock = stripedLock.getLockForObject(object);
        lock.lock();
        try {
            return super.remove(object);
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends O> c) {
        boolean modified = false;
        for (O object : c) {
            modified = add(object) || modified;
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object object : c) {
            modified = remove(object) || modified;
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return super.retainAll(c); // Delegates to superclass implementation, which delegates to iterator() method above
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void clear() {
        super.clear();
    }
}
