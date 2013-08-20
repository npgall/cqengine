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
package com.googlecode.cqengine.benchmark;

import com.googlecode.cqengine.testutil.Car;

import java.util.Collection;

/**
 * Defines the methods which all benchmark tasks must implement, to compare the performance of conventional iteration
 * with CQEngine. Subclasses will implement particular queries, so that overall these results can be compared
 * across various types of query.
 *
 * @author Niall Gallagher
 */
public interface BenchmarkTask {

    /**
     * Initializes the task to process the given collection of cars.
     *
     * @param collection The collection of cars to use for the benchmark
     */
    void init(Collection<Car> collection);

    /**
     * Iterates the collection and assembles a list of cars matching the query.
     * Then "processes" these results i.e. simply iterates them maintaining a count of the number of cars processed.
     * 
     * @return The number of cars matching the query
     */
    int runQueryCountResults_IterationNaive();

    /**
     * Iterates the collection and counts cars matching the query in a single pass.
     *
     * @return The number of cars matching the query
     */
    int runQueryCountResults_IterationOptimized();

    /**
     * Gets a lazy ResultSet from CQEngine for cars matching the query, which CQEngine implements to use indexes
     * during iteration.
     * Then "processes" these results i.e. simply iterates them maintaining a count of the number of cars processed.
     *
     * @return The number of cars matching the query
     */
    int runQueryCountResults_CQEngine();

    /**
     * Asks CQEngine to count cars matching the query, which it calculates from statistics stored in indexes.
     *
     * @return The number of cars matching the query
     */
    int runQueryCountResults_CQEngineStatistics();

}
