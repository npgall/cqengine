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

import java.util.*;

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
     * concurrently. See {@link IndexedCollectionImpl} for details.
     *
     * @param <O> The type of objects in the collection
     * @return A new {@link IndexedCollection} initially containing no objects
     */
    public static <O> IndexedCollection<O> newInstance() {
        return new IndexedCollectionImpl<O>(16, new QueryEngineImpl<O>());
    }

    /**
     * Returns a new {@link IndexedCollection} containing objects from the given collection.
     * <p/>
     * The implementation returned supports concurrent reads in all cases, and supports concurrent modification in
     * cases where multiple threads will add/remove different objects but not the same objects to the collection
     * concurrently. See {@link IndexedCollectionImpl} for details.
     *
     * @param collection A collection containing initial values to be indexed
     * @param <O> The type of objects in the collection
     * @return An {@link IndexedCollection} initialized with objects from the given collection
     */
    public static <O> IndexedCollection<O> copyFrom(Collection<O> collection) {
        IndexedCollection<O> indexedCollection = new IndexedCollectionImpl<O>(collection.size(), new QueryEngineImpl<O>());
        indexedCollection.addAll(collection);
        return indexedCollection;
    }

    /**
     * Returns a new {@link IndexedCollection} to which objects can be added subsequently.
     * <p/>
     * The implementation returned uses a stripped lock to support applications in which multiple threads might try to
     * add/remove the <i>same</i> object to/from the collection concurrently. See {@link IndexedCollectionStripedImpl}
     * for details.
     *
     * @param <O> The type of objects in the collection
     * @param concurrentWritesNumStripes The number of stripes to use for concurrent writes, more stripes reduces the
     * likelihood of lock contention on writes, but uses more memory to hold locks. 512 could be a sensible default
     * @return A new {@link IndexedCollection} initially containing no objects
     */
    public static <O> IndexedCollection<O> newInstanceStriped(int concurrentWritesNumStripes) {
        return new IndexedCollectionStripedImpl<O>(16, concurrentWritesNumStripes, new QueryEngineImpl<O>());
    }

    /**
     * Returns a new {@link IndexedCollection} containing objects from the given collection.
     * <p/>
     * The implementation returned uses a stripped lock to support applications in which multiple threads might try to
     * add/remove the <i>same</i> object to/from the collection concurrently. See {@link IndexedCollectionStripedImpl}
     * for details.
     *
     * @param collection A collection containing initial values to be indexed
     * @param concurrentWritesNumStripes The number of stripes to use for concurrent writes, more stripes reduces the
     * likelihood of lock contention on writes, but uses more memory to hold locks. 512 could be a sensible default
     * @param <O> The type of objects in the collection
     * @return An {@link IndexedCollection} initialized with objects from the given collection
     */
    public static <O> IndexedCollection<O> copyFromStriped(Collection<O> collection, int concurrentWritesNumStripes) {
        IndexedCollection<O> indexedCollection = new IndexedCollectionStripedImpl<O>(collection.size(), concurrentWritesNumStripes, new QueryEngineImpl<O>());
        indexedCollection.addAll(collection);
        return indexedCollection;
    }

    /**
     * Private constructor, not used.
     */
    CQEngine() {
    }
}
