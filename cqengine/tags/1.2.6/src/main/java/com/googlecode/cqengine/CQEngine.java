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
package com.googlecode.cqengine;

import com.googlecode.cqengine.collection.impl.*;
import com.googlecode.cqengine.engine.impl.QueryEngineImpl;
import com.googlecode.cqengine.index.common.Factory;

import java.util.Collection;
import java.util.Set;

/**
 * A static factory for creating {@link IndexedCollection}s, either as new instances to which objects can be added
 * subsequently, or by copying objects from existing collections.
 * <p/>
 * Once an {@link IndexedCollection} has been created, indexes can be added to it subsequently using
 * {@link IndexedCollection#addIndex(com.googlecode.cqengine.index.Index)}.
 * <p/>
 * An {@link IndexedCollection} is also a {@link java.util.Collection} and so can be treated as a normal collection.
 * Objects added to and removed from an {@link IndexedCollection} will automatically be added to and removed from
 * indexes as necessary.
 *
 * @author Niall Gallagher
 */
public class CQEngine {

    /**
     * Returns a new {@link IndexedCollection} to which objects can be added subsequently.
     * <p/>
     * The implementation returned supports concurrent reads in all cases, and supports concurrent modification in
     * cases where multiple threads will add/remove different objects but not the same objects to the collection
     * concurrently. See {@link com.googlecode.cqengine.collection.impl.ConcurrentIndexedCollection} for details.
     *
     * @param <O> The type of objects in the collection
     * @return A new {@link IndexedCollection} initially containing no objects
     */
    public static <O> IndexedCollection<O> newInstance() {
        return new ConcurrentIndexedCollection<O>(new DefaultConcurrentSetFactory<O>(), new QueryEngineImpl<O>());
    }

    /**
     * Returns a new {@link IndexedCollection} to which objects can be added subsequently.
     * <p/>
     * The implementation returned supports concurrent reads in all cases, and supports concurrent modification in
     * cases where multiple threads will add/remove different objects but not the same objects to the collection
     * concurrently. See {@link com.googlecode.cqengine.collection.impl.ConcurrentIndexedCollection} for details.
     *
     * @param backingSetFactory A factory which will create a concurrent {@link java.util.Set} in which objects
     * added to the indexed collection will be stored
     * @param <O> The type of objects in the collection
     * @return A new {@link IndexedCollection} initially containing no objects
     */
    public static <O> IndexedCollection<O> newInstance(Factory<Set<O>> backingSetFactory) {
        return new ConcurrentIndexedCollection<O>(backingSetFactory, new QueryEngineImpl<O>());
    }

    /**
     * Returns a new {@link IndexedCollection} containing objects from the given collection.
     * <p/>
     * The implementation returned supports concurrent reads in all cases, and supports concurrent modification in
     * cases where multiple threads will add/remove different objects but not the same objects to the collection
     * concurrently. See {@link com.googlecode.cqengine.collection.impl.ConcurrentIndexedCollection} for details.
     *
     * @param collection A collection containing initial values to be indexed
     * @param <O> The type of objects in the collection
     * @return An {@link IndexedCollection} initialized with objects from the given collection
     */
    public static <O> IndexedCollection<O> copyFrom(Collection<O> collection) {
        Factory<Set<O>> setFactory = new DefaultConcurrentSetFactory<O>(collection.size());
        IndexedCollection<O> indexedCollection = new ConcurrentIndexedCollection<O>(setFactory, new QueryEngineImpl<O>());
        indexedCollection.addAll(collection);
        return indexedCollection;
    }

    /**
     * Returns a new {@link IndexedCollection} containing objects from the given collection.
     * <p/>
     * The implementation returned supports concurrent reads in all cases, and supports concurrent modification in
     * cases where multiple threads will add/remove different objects but not the same objects to the collection
     * concurrently. See {@link com.googlecode.cqengine.collection.impl.ConcurrentIndexedCollection} for details.
     *
     * @param collection A collection containing initial values to be indexed
     * @param backingSetFactory A factory which will create a concurrent {@link java.util.Set} in which objects
     * added to the indexed collection will be stored
     * @param <O> The type of objects in the collection
     * @return An {@link IndexedCollection} initialized with objects from the given collection
     */
    public static <O> IndexedCollection<O> copyFrom(Collection<O> collection, Factory<Set<O>> backingSetFactory) {
        IndexedCollection<O> indexedCollection = new ConcurrentIndexedCollection<O>(backingSetFactory, new QueryEngineImpl<O>());
        indexedCollection.addAll(collection);
        return indexedCollection;
    }

    /**
     * Returns a new {@link IndexedCollection} to which objects can be added subsequently.
     * <p/>
     * The implementation returned supports concurrent reads and writes in general, but employs some locking
     * in the write path for applications in which multiple threads might try to add/remove the <i>same</i>
     * object to/from the collection concurrently.
     * See {@link com.googlecode.cqengine.collection.impl.ObjectLockingIndexedCollection} for details.
     *
     * @param concurrencyLevel The estimated number of concurrently updating threads. 64 could be a sensible default
     * @param <O> The type of objects in the collection
     * @return A new {@link IndexedCollection} initially containing no objects
     */
    public static <O> IndexedCollection<O> newObjectLockingInstance(int concurrencyLevel) {
        return new ObjectLockingIndexedCollection<O>(new DefaultConcurrentSetFactory<O>(), concurrencyLevel, new QueryEngineImpl<O>());
    }

    /**
     * Returns a new {@link IndexedCollection} to which objects can be added subsequently.
     * <p/>
     * The implementation returned supports concurrent reads and writes in general, but employs some locking
     * in the write path for applications in which multiple threads might try to add/remove the <i>same</i>
     * object to/from the collection concurrently.
     * See {@link com.googlecode.cqengine.collection.impl.ObjectLockingIndexedCollection} for details.
     *
     * @param concurrencyLevel The estimated number of concurrently updating threads. 64 could be a sensible default
     * @param backingSetFactory A factory which will create a concurrent {@link java.util.Set} in which objects
     * added to the indexed collection will be stored
     * @param <O> The type of objects in the collection
     * @return A new {@link IndexedCollection} initially containing no objects
     */
    public static <O> IndexedCollection<O> newObjectLockingInstance(int concurrencyLevel, Factory<Set<O>> backingSetFactory) {
        return new ObjectLockingIndexedCollection<O>(backingSetFactory, concurrencyLevel, new QueryEngineImpl<O>());
    }

    /**
     * Returns a new {@link IndexedCollection} containing objects from the given collection.
     * <p/>
     * The implementation returned supports concurrent reads and writes in general, but employs some locking
     * in the write path for applications in which multiple threads might try to add/remove the <i>same</i>
     * object to/from the collection concurrently.
     * See {@link com.googlecode.cqengine.collection.impl.ObjectLockingIndexedCollection} for details.
     *
     * @param collection A collection containing initial values to be indexed
     * @param concurrencyLevel The estimated number of concurrently updating threads. 64 could be a sensible default
     * @param <O> The type of objects in the collection
     * @return An {@link IndexedCollection} initialized with objects from the given collection
     */
    public static <O> IndexedCollection<O> copyFromObjectLocking(Collection<O> collection, int concurrencyLevel) {
        Factory<Set<O>> setFactory = new DefaultConcurrentSetFactory<O>(collection.size());
        IndexedCollection<O> indexedCollection = new ObjectLockingIndexedCollection<O>(setFactory, concurrencyLevel, new QueryEngineImpl<O>());
        indexedCollection.addAll(collection);
        return indexedCollection;
    }

    /**
     * Returns a new {@link IndexedCollection} containing objects from the given collection.
     * <p/>
     * <p/>
     * The implementation returned supports concurrent reads and writes in general, but employs some locking
     * in the write path for applications in which multiple threads might try to add/remove the <i>same</i>
     * object to/from the collection concurrently.
     * See {@link com.googlecode.cqengine.collection.impl.ObjectLockingIndexedCollection} for details.
     *
     * @param collection A collection containing initial values to be indexed
     * @param concurrencyLevel The estimated number of concurrently updating threads. 64 could be a sensible default
     * @param backingSetFactory A factory which will create a concurrent {@link java.util.Set} in which objects
     * added to the indexed collection will be stored
     * @param <O> The type of objects in the collection
     * @return An {@link IndexedCollection} initialized with objects from the given collection
     */
    public static <O> IndexedCollection<O> copyFromObjectLocking(Collection<O> collection, int concurrencyLevel, Factory<Set<O>> backingSetFactory) {
        IndexedCollection<O> indexedCollection = new ObjectLockingIndexedCollection<O>(backingSetFactory, concurrencyLevel, new QueryEngineImpl<O>());
        indexedCollection.addAll(collection);
        return indexedCollection;
    }

    /**
     * Private constructor, not used.
     */
    CQEngine() {
    }
}
