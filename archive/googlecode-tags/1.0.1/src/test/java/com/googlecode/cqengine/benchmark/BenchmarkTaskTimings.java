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

/**
 * Time taken to perform each test *per-repetition*. I.e. if many iterations were performed, this is total time / reps.
 *
 * @author Niall Gallagher
 */
public class BenchmarkTaskTimings {

    String testName;
    Long timeTakenIterationNaive;
    Long timeTakenIterationOptimized;
    Long timeTakenCQEngine;
    Long timeTakenCQEngineStatistics;

    @Override
    public String toString() {
        return "BenchmarkTaskTimings{" +
                "testName='" + testName + '\'' +
                ", runQueryCountResults_IterationNaive=" + timeTakenIterationNaive +
                ", timeTakenIterationOptimized=" + timeTakenIterationOptimized +
                ", timeTakenCQEngine=" + timeTakenCQEngine +
                ", timeTakenCQEngineStatistics=" + timeTakenCQEngineStatistics +
                '}';
    }
}
