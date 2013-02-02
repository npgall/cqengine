/**
 * Copyright 2012 Niall Gallagher
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
package com.googlecode.cqengine.collection.impl;

import com.googlecode.cqengine.engine.QueryEngineInternal;
import com.googlecode.cqengine.index.common.Factory;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Extends {@link DefaultIndexedCollection} with specialized concurrency support, for applications in which multiple
 * threads might try to add or remove the <b>same object</b> to/from an indexed collection <i>concurrently</i>.
 * In this context the <i>same object</i> refers to either the same object instance, OR two object instances having the
 * same hash code and being equal according to their {@link #equals(Object)} methods.
 * <p/>
 * The {@link DefaultIndexedCollection} superclass is thread-safe in cases where the application will add/remove
 * <i>different</i> objects concurrently. This implementation adds safeguards around adding/removing the <i>same</i>
 * object concurrently, with some additional overhead.
 * <p/>
 * This class is implemented with a <a href="http://code.google.com/p/guava-libraries/wiki/StripedExplained">striped lock</a>
 * (but not the Guava implementation). As such, the hash code of an object is not assigned a unique lock, but there is
 * instead a many-to-fewer association between hash codes and locks. Some different objects will therefore contend for
 * the same lock. The number of stripes (locks) is configurable, allowing this ratio and likelihood of contention to be
 * controlled, trading memory overhead of a large number of locks, against likelihood of contention. Two or more
 * modifications for the same object, will always block each other. Two or more modifications for different objects
 * will <i>usually not</i> block each other, but occasionally might.
 * <p/>
 * Reading threads are never blocked; reads remain lock-free as in the superclass.
 *
 * @author Niall Gallagher
 */
public class StripeLockedIndexedCollection<O> extends DefaultIndexedCollection<O> {

    final StripedLock stripedLock;

    /**
     * Constructor.
     *
     * @param backingSetFactory A factory which will create a concurrent {@link java.util.Set} in which objects
     * added to the indexed collection will be stored
     * @param numStripes The number of stripes to use for concurrent writes, more stripes reduces the
     * likelihood of lock contention on writes, but uses more memory to hold locks. 512 could be a sensible default
     * @param queryEngine The query engine
     */
    public StripeLockedIndexedCollection(Factory<Set<O>> backingSetFactory, int numStripes, QueryEngineInternal<O> queryEngine) {
        super(backingSetFactory, queryEngine);
        this.stripedLock = new StripedLock(numStripes);
    }

    static class StripedLock {
        final int numStripes;
        final ReentrantLock[] locks;

        StripedLock(int numStripes) {
            this.numStripes = numStripes;
            this.locks = new ReentrantLock[numStripes];
            for (int i = 0; i < numStripes; i++) {
                locks[i] = new ReentrantLock();
            }
        }

        ReentrantLock getLockForObject(Object object) {
            int hashCode = object.hashCode();
            return locks[hashCode % numStripes];
        }
    }

    // ----------- Collection Mutator Methods -------------


    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<O> iterator() {
        return new Iterator<O>() {
            private final Iterator<O> collectionIterator = collection.iterator();
            @Override
            public boolean hasNext() {
                return collectionIterator.hasNext();
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
                Lock lock = stripedLock.getLockForObject(currentObject);
                lock.lock();
                try {
                    collectionIterator.remove();
                    indexEngine.notifyObjectsRemoved(Collections.singleton(currentObject));
                }
                finally {
                    lock.unlock();
                }
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
