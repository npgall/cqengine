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
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Tests for the benchmark itself.
 *
 * @author Niall Gallagher
 */
public class BenchmarkUnitTest {

    private final Collection<Car> collection = CarFactory.createCollectionOfCars(1000);

    @Test
    public void testHashIndex_ModelFocus() {
        BenchmarkTask task = new HashIndex_ModelFocus();
        task.init(collection);

        assertEquals(100, task.runQueryCountResults_IterationNaive());
        assertEquals(100, task.runQueryCountResults_IterationOptimized());
        assertEquals(100, task.runQueryCountResults_CQEngine());
        assertEquals(100, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testHashIndex_ManufacturerFord() {
        BenchmarkTask task = new HashIndex_ManufacturerFord();
        task.init(collection);

        assertEquals(300, task.runQueryCountResults_IterationNaive());
        assertEquals(300, task.runQueryCountResults_IterationOptimized());
        assertEquals(300, task.runQueryCountResults_CQEngine());
        assertEquals(300, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testNavigableIndex_PriceBetween() {
        BenchmarkTask task = new NavigableIndex_PriceBetween();
        task.init(collection);

        assertEquals(200, task.runQueryCountResults_IterationNaive());
        assertEquals(200, task.runQueryCountResults_IterationOptimized());
        assertEquals(200, task.runQueryCountResults_CQEngine());
        assertEquals(200, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testCompoundIndex_ManufacturerToyotaColorBlueDoorsThree() {
        BenchmarkTask task = new CompoundIndex_ManufacturerToyotaColorBlueDoorsThree();
        task.init(collection);

        assertEquals(100, task.runQueryCountResults_IterationNaive());
        assertEquals(100, task.runQueryCountResults_IterationOptimized());
        assertEquals(100, task.runQueryCountResults_CQEngine());
        assertEquals(100, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testStandingQueryIndex_ManufacturerToyotaColorBlueDoorsNotFive() {
        BenchmarkTask task = new StandingQueryIndex_ManufacturerToyotaColorBlueDoorsNotFive();
        task.init(collection);

        assertEquals(100, task.runQueryCountResults_IterationNaive());
        assertEquals(100, task.runQueryCountResults_IterationOptimized());
        assertEquals(100, task.runQueryCountResults_CQEngine());
        assertEquals(100, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testRadixTreeIndex_ModelStartsWithF() {
        BenchmarkTask task = new RadixTreeIndex_ModelStartsWithP();
        task.init(collection);

        assertEquals(100, task.runQueryCountResults_IterationNaive());
        assertEquals(100, task.runQueryCountResults_IterationOptimized());
        assertEquals(100, task.runQueryCountResults_CQEngine());
        assertEquals(100, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testSuffixTreeIndex_ModelContainsI() {
        BenchmarkTask task = new SuffixTreeIndex_ModelContainsG();
        task.init(collection);

        assertEquals(100, task.runQueryCountResults_IterationNaive());
        assertEquals(100, task.runQueryCountResults_IterationOptimized());
        assertEquals(100, task.runQueryCountResults_CQEngine());
        assertEquals(100, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testHashIndex_CarId() {
        BenchmarkTask task = new HashIndex_CarId();
        task.init(collection);

        assertEquals(1, task.runQueryCountResults_IterationNaive());
        assertEquals(1, task.runQueryCountResults_IterationOptimized());
        assertEquals(1, task.runQueryCountResults_CQEngine());
        assertEquals(1, task.runQueryCountResults_CQEngineStatistics());
    }
    
    @Test
    public void testQuantized_HashIndex_CarId() {
        BenchmarkTask task = new Quantized_HashIndex_CarId();
        task.init(collection);

        assertEquals(1, task.runQueryCountResults_IterationNaive());
        assertEquals(1, task.runQueryCountResults_IterationOptimized());
        assertEquals(1, task.runQueryCountResults_CQEngine());
        assertEquals(1, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testQuantized_NavigableIndex_CarId() {
        BenchmarkTask task = new Quantized_NavigableIndex_CarId();
        task.init(collection);

        assertEquals(3, task.runQueryCountResults_IterationNaive());
        assertEquals(3, task.runQueryCountResults_IterationOptimized());
        assertEquals(3, task.runQueryCountResults_CQEngine());
        assertEquals(3, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testNonOptimalIndexes() {
        BenchmarkTask task = new NonOptimalIndexes_ManufacturerToyotaColorBlueDoorsThree();
        task.init(collection);

        assertEquals(100, task.runQueryCountResults_IterationNaive());
        assertEquals(100, task.runQueryCountResults_IterationOptimized());
        assertEquals(100, task.runQueryCountResults_CQEngine());
        assertEquals(100, task.runQueryCountResults_CQEngineStatistics());
    }

    @Test
    public void testMaterializedOrder_CardId() {
        BenchmarkTask task = new MaterializedOrder_CardId();
        task.init(collection);

        assertEquals(100, task.runQueryCountResults_IterationNaive());
        assertEquals(100, task.runQueryCountResults_IterationOptimized());
        assertEquals(100, task.runQueryCountResults_CQEngine());
        assertEquals(100, task.runQueryCountResults_CQEngineStatistics());
    }
}
