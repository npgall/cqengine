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
package com.googlecode.cqengine.indexingbenchmark;

import com.googlecode.cqengine.testutil.Car;

import java.util.Collection;

/**
 * Defines the methods which all indexing tasks must implement,
 * to measure the overhead of building indexes of various types.
 *
 * @author Niall Gallagher
 */
public interface IndexingTask {

    /**
     * Initializes the task with the given collection.
     * Typically this will copy the collection into a new instance of CQEngine.
     * <p/>
     * Note that time taken by this method is NOT factored into the overhead to build the index.
     * Applications should store objects in IndexedCollection in the first place.
     *
     * @param collection The collection of cars to be indexed
     */
    void init(Collection<Car> collection);

    /**
     * Builds an in index on the collection, the type of index  determined by the task implementation.
     */
    void buildIndex();
}
