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
package com.googlecode.cqengine.benchmark;

import com.googlecode.cqengine.benchmark.tasks.*;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Niall Gallagher
 */
public class BenchmarkRunner {

    // TODO: Use Google Caliper?

    static final int COLLECTION_SIZE = 100000;
    static final int WARMUP_REPETITIONS = 10000;
    static final int MEASUREMENT_REPETITIONS = 10000;

    static final List<? extends BenchmarkTask> benchmarkTasks = Arrays.asList(
            new UniqueIndex_CarId(),
            new HashIndex_CarId(),
            new HashIndex_ManufacturerFord(),
            new HashIndex_ModelFocus(),
            new NavigableIndex_PriceBetween(),
            new Quantized_HashIndex_CarId(),
            new Quantized_NavigableIndex_CarId(),
            new CompoundIndex_ManufacturerToyotaColorBlueDoorsThree(),
            new NoIndexes_ModelFocus(),
            new NonOptimalIndexes_ManufacturerToyotaColorBlueDoorsThree(),
            new StandingQueryIndex_ManufacturerToyotaColorBlueDoorsNotFive(),
            new RadixTreeIndex_ModelStartsWithP(),
            new SuffixTreeIndex_ModelContainsG(),
            new MaterializedOrder_CardId()
    );

    public static void main(String[] args) {
        Collection<Car> collection = CarFactory.createCollectionOfCars(COLLECTION_SIZE);
        printResultsHeader(System.out);
        for (BenchmarkTask task : benchmarkTasks) {
            task.init(collection);

            // Warmup...
            dummyTimingsHolder = runBenchmarkTask(task, WARMUP_REPETITIONS);

            // Run GC...
            System.gc();

            // Run the benchmark task...
            BenchmarkTaskTimings results = runBenchmarkTask(task, MEASUREMENT_REPETITIONS);

            // Print timings for this task...
            printTimings(results, System.out);
        }
    }

    static BenchmarkTaskTimings runBenchmarkTask(BenchmarkTask benchmarkTask, int repetitions) {
        dummyValueHolder = new ArrayList<Integer>();
        BenchmarkTaskTimings timings = new BenchmarkTaskTimings();
        timings.testName = benchmarkTask.getClass().getSimpleName();
        long startTimeNanos;
        int dummy = 0;

        startTimeNanos = System.nanoTime();
        for (int i = 0; i < repetitions; i++) {
            dummy = benchmarkTask.runQueryCountResults_IterationNaive();
        }
        dummyValueHolder.add(dummy);
        timings.timeTakenIterationNaive = ((System.nanoTime() - startTimeNanos) / repetitions);

        startTimeNanos = System.nanoTime();
        for (int i = 0; i < repetitions; i++) {
            dummy = benchmarkTask.runQueryCountResults_IterationOptimized();
        }
        dummyValueHolder.add(dummy);
        timings.timeTakenIterationOptimized = ((System.nanoTime() - startTimeNanos) / repetitions);

        startTimeNanos = System.nanoTime();
        for (int i = 0; i < repetitions; i++) {
            dummy = benchmarkTask.runQueryCountResults_CQEngine();
        }
        dummyValueHolder.add(dummy);
        timings.timeTakenCQEngine = ((System.nanoTime() - startTimeNanos) / repetitions);

        startTimeNanos = System.nanoTime();
        for (int i = 0; i < repetitions; i++) {
            dummy = benchmarkTask.runQueryCountResults_CQEngineStatistics();
        }
        dummyValueHolder.add(dummy);
        timings.timeTakenCQEngineStatistics = ((System.nanoTime() - startTimeNanos) / repetitions);

        return timings;
    }

    static void printResultsHeader(Appendable appendable) {
        try {
            appendable.append("TestName\tIterationNaive\tIterationOptimized\tCQEngine\tCQEngineStatistics\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void printTimings(BenchmarkTaskTimings timings, Appendable appendable) {
        try {
            appendable.append(timings.testName).append("\t");
            appendable.append(timings.timeTakenIterationNaive.toString()).append("\t");
            appendable.append(timings.timeTakenIterationOptimized.toString()).append("\t");
            appendable.append(timings.timeTakenCQEngine.toString()).append("\t");
            appendable.append(timings.timeTakenCQEngineStatistics.toString()).append("\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Store dummy values in these public variables, so JIT compiler can't eliminate "redundant" benchmark code...
    public static List<Integer> dummyValueHolder = new ArrayList<Integer>();
    public static BenchmarkTaskTimings dummyTimingsHolder = new BenchmarkTaskTimings();
}
