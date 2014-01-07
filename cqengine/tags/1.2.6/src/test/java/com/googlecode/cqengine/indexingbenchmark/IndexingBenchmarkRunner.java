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

import com.googlecode.cqengine.indexingbenchmark.task.*;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Niall Gallagher
 */
public class IndexingBenchmarkRunner {

    // TODO: Use Google Caliper?

    static final int COLLECTION_SIZE = 100000;
    static final int WARMUP_REPETITIONS = 50;
    static final int MEASUREMENT_REPETITIONS = 50;

    static final List<? extends IndexingTask> benchmarkTasks = Arrays.asList(
            new HashIndex_CarId(),
            new UniqueIndex_CarId(),
            new Quantized_HashIndex_CarId(),
            new HashIndex_Manufacturer(),
            new HashIndex_Model(),
            new CompoundIndex_ManufacturerColorDoors(),
            new NavigableIndex_Price(),
            new RadixTreeIndex_Model(),
            new SuffixTreeIndex_Model()
    );

    public static void main(String[] args) {
        Collection<Car> collection = CarFactory.createCollectionOfCars(COLLECTION_SIZE);

        printResultsHeader(System.out);
        for (IndexingTask task : benchmarkTasks) {
            // Warmup...
            dummyTimingsHolder = runBenchmarkTask(task, WARMUP_REPETITIONS, collection);

            // Run GC...
            System.gc();

            // Run the benchmark task...
            IndexingTaskTimings results = runBenchmarkTask(task, MEASUREMENT_REPETITIONS, collection);

            // Print timings for this task...
            printTimings(results, System.out);
        }
    }

    static IndexingTaskTimings runBenchmarkTask(IndexingTask indexingTask, int repetitions, Collection<Car> collection) {
        IndexingTaskTimings timings = new IndexingTaskTimings();
        timings.testName = indexingTask.getClass().getSimpleName();
        timings.collectionSize = collection.size();

        long startTimeNanos, timeTakenNanos = 0;
        for (int i = 0; i < repetitions; i++) {
            indexingTask.init(collection);
            startTimeNanos = System.nanoTime();
            indexingTask.buildIndex();
            timeTakenNanos += (System.nanoTime() - startTimeNanos);
        }
        timings.timeTakenPerCollection = (timeTakenNanos / repetitions);
        timings.timeTakenPerObject = (timeTakenNanos / repetitions / collection.size());

        return timings;
    }

    static void printResultsHeader(Appendable appendable) {
        try {
            appendable.append("Index\tNumObjects\tPerCollection\tPerObject\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void printTimings(IndexingTaskTimings timings, Appendable appendable) {
        try {
            appendable.append(timings.testName).append("\t");
            appendable.append(timings.collectionSize.toString()).append("\t");
            appendable.append(timings.timeTakenPerCollection.toString()).append("\t");
            appendable.append(timings.timeTakenPerObject.toString()).append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Store dummy values in these public variables, so JIT compiler can't eliminate "redundant" benchmark code...
    public static IndexingTaskTimings dummyTimingsHolder = new IndexingTaskTimings();

}
