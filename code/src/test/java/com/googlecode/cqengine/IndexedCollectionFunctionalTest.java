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

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.StandingQueryAttribute;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.compound.support.CompoundValueTuple;
import com.googlecode.cqengine.index.disk.DiskIndex;
import com.googlecode.cqengine.index.disk.PartialDiskIndex;
import com.googlecode.cqengine.index.navigable.PartialNavigableIndex;
import com.googlecode.cqengine.index.offheap.OffHeapIndex;
import com.googlecode.cqengine.index.offheap.PartialOffHeapIndex;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import com.googlecode.cqengine.index.support.AbstractMapBasedAttributeIndex;
import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.radixinverted.InvertedRadixTreeIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.index.support.indextype.DiskTypeIndex;
import com.googlecode.cqengine.index.support.indextype.OffHeapTypeIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.composite.CompositePersistence;
import com.googlecode.cqengine.persistence.disk.DiskPersistence;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.quantizer.IntegerQuantizer;
import com.googlecode.cqengine.quantizer.Quantizer;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.*;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import com.googlecode.cqengine.testutil.DiskConcurrentIndexedCollection;
import com.googlecode.cqengine.testutil.OffHeapConcurrentIndexedCollection;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.AssumptionViolatedException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.option.EngineThresholds.INDEX_ORDERING_SELECTIVITY;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

/**
 * Tests CQEngine handing a wide range of queries, with different configurations of indexes.
 *
 * @author Niall Gallagher
 */
@RunWith(DataProviderRunner.class)
public class IndexedCollectionFunctionalTest {

    static final Set<Car> REGULAR_DATASET = CarFactory.createCollectionOfCars(1000);
    static final Set<Car> SMALL_DATASET = CarFactory.createCollectionOfCars(10);

    // Note: Unfortunately ObjectLockingIndexedCollection can slow down the functional test a lot when
    // disk indexes are in use (because it splits bulk inserts into a separate transaction per object).
    // Set this true to skip the slow scenarios *during development only!*...
    static final boolean SKIP_SLOW_SCENARIOS =
            "true".equalsIgnoreCase(System.getProperty("cqengine.skip.slow.scenarios")) // system property
            || "true".equalsIgnoreCase(System.getenv("cqengine_skip_slow_scenarios")); // environment variable

    static final boolean RUN_HIGH_PRIORITY_SCENARIOS_ONLY = false;

    // Print progress of functional tests to the console at this frequncy...
    final int STATUS_UPDATE_FREQUENCY_MS = 1000;
    static long lastStatusTimestamp = 0L;

    // Macro scenarios specify sets of IndexedCollection implementations,
    // sets of index combinations, and sets of queries. Macro scenarios will be expanded
    // to many individual scenarios comprised of the distinct combinations of these sets.
    // Each individual scenario resulting from the expansion will be run as a separate test.
    // This allows to run the same set of queries with different combinations of indexes etc.
    static List<MacroScenario> getMacroScenarios() {
        return Arrays.asList(
                new MacroScenario() {{
                    name = "typical queries";
                    dataSet = REGULAR_DATASET;
                    alsoEvaluateWithIndexMergeStrategy = true; // runs each of these scenarios twice to test with both the default merge strategy and with the index merge strategy
                    collectionImplementations = SKIP_SLOW_SCENARIOS
                            ? classes(ConcurrentIndexedCollection.class, TransactionalIndexedCollection.class, OffHeapConcurrentIndexedCollection.class)
                            : classes(ConcurrentIndexedCollection.class, TransactionalIndexedCollection.class, OffHeapConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class);
                    queriesToEvaluate = asList(
                            new QueryToEvaluate() {{
                                query = equal(Car.CAR_ID, 500);
                                expectedResults = new ExpectedResults() {{
                                    size = 1;
                                    carIdsAnyOrder = asSet(500);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 500, 500);
                                expectedResults = new ExpectedResults() {{
                                    size = 1;
                                    carIdsAnyOrder = asSet(500);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 998, 2000);
                                expectedResults = new ExpectedResults() {{
                                    size = 2;
                                    carIdsAnyOrder = asSet(998, 999);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, -10, 2);
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 499, 501);
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsAnyOrder = asSet(499, 500, 501);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 407, 683);
                                expectedResults = new ExpectedResults() {{
                                    size = 277;
                                    carIdsAnyOrder = integersBetween(407, 683);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 407, true, 683, true);
                                expectedResults = new ExpectedResults() {{
                                    size = 277;
                                    carIdsAnyOrder = integersBetween(407, 683);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 407, false, 683, true);
                                expectedResults = new ExpectedResults() {{
                                    size = 276;
                                    carIdsAnyOrder = integersBetween(408, 683);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 407, true, 683, false);
                                expectedResults = new ExpectedResults() {{
                                    size = 276;
                                    carIdsAnyOrder = integersBetween(407, 682);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 407, false, 683, false);
                                expectedResults = new ExpectedResults() {{
                                    size = 275;
                                    carIdsAnyOrder = integersBetween(408, 682);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = lessThan(Car.CAR_ID, 5);
                                expectedResults = new ExpectedResults() {{
                                    size = 5;
                                    carIdsAnyOrder = integersBetween(0, 4);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = lessThanOrEqualTo(Car.CAR_ID, 5);
                                expectedResults = new ExpectedResults() {{
                                    size = 6;
                                    carIdsAnyOrder = integersBetween(0, 5);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = greaterThan(Car.CAR_ID, 995);
                                expectedResults = new ExpectedResults() {{
                                    size = 4;
                                    carIdsAnyOrder = integersBetween(996, 999);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = greaterThanOrEqualTo(Car.CAR_ID, 995);
                                expectedResults = new ExpectedResults() {{
                                    size = 5;
                                    carIdsAnyOrder = integersBetween(995, 999);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = in(Car.CAR_ID, 5, 463, 999);
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsAnyOrder = asSet(5, 463, 999);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = in(Car.CAR_ID, -10, 5, 463, 999, 1500);
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsAnyOrder = asSet(5, 463, 999);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = all(Car.class);
                                expectedResults = new ExpectedResults() {{
                                    size = 1000;
                                    carIdsAnyOrder = integersBetween(0, 999);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = none(Car.class);
                                expectedResults = new ExpectedResults() {{
                                    size = 0;
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(between(Car.CAR_ID, 400, 500), lessThan(Car.CAR_ID, 450));
                                expectedResults = new ExpectedResults() {{
                                    containsCarIds = asSet(400, 449);
                                    doesNotContainCarIds = asSet(399, 450, 501);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(between(Car.CAR_ID, 400, 500), not(greaterThan(Car.CAR_ID, 450)));
                                expectedResults = new ExpectedResults() {{
                                    containsCarIds = asSet(400, 450, 449);
                                    doesNotContainCarIds = asSet(11, 399, 451, 501);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = or(between(Car.CAR_ID, 400, 500), between(Car.CAR_ID, 450, 550));
                                expectedResults = new ExpectedResults() {{
                                    containsCarIds = integersBetween(400, 550);
                                    doesNotContainCarIds = asSet(399, 551);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = not(greaterThan(Car.CAR_ID, 450));
                                expectedResults = new ExpectedResults() {{
                                    containsCarIds = asSet(1, 449, 450);
                                    doesNotContainCarIds = asSet(451);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = equal(Car.FEATURES, "hybrid");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 200;
                                    containsCarIds = asSet(1, 7);
                                    doesNotContainCarIds = asSet(0, 2, 3, 4, 5, 6, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = in(Car.FEATURES, "hybrid", "coupe");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 300;
                                    containsCarIds = asSet(1, 7, 9);
                                    doesNotContainCarIds = asSet(0, 2, 3, 4, 5, 6, 8);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.FEATURES, "grade a", "grade c"); // note: lexicographically between
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 300;
                                    containsCarIds = asSet(2, 3, 4);
                                    doesNotContainCarIds = asSet(0, 1, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.FEATURES, "grade a", false, "grade c", true); // note: lexicographically between
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 200;
                                    containsCarIds = asSet(3, 4);
                                    doesNotContainCarIds = asSet(0, 1, 2, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.FEATURES, "grade a", true, "grade c", false); // note: lexicographically between
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 200;
                                    containsCarIds = asSet(2, 3);
                                    doesNotContainCarIds = asSet(0, 1, 4, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.FEATURES, "grade a", false, "grade c", false); // note: lexicographically between
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 100;
                                    containsCarIds = asSet(3);
                                    doesNotContainCarIds = asSet(0, 1, 2, 4, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(greaterThan(Car.FEATURES, "grade a"), lessThan(Car.FEATURES, "grade c"));
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 100;
                                    containsCarIds = asSet(3);
                                    doesNotContainCarIds = asSet(0, 1, 2, 4, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(greaterThanOrEqualTo(Car.FEATURES, "grade a"), lessThanOrEqualTo(Car.FEATURES, "grade c"));
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 300;
                                    containsCarIds = asSet(2, 3, 4);
                                    doesNotContainCarIds = asSet(0, 1, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = startsWith(Car.FEATURES, "grade");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 300;
                                    containsCarIds = asSet(2, 3, 4);
                                    doesNotContainCarIds = asSet(0, 1, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = startsWith(Car.MANUFACTURER, "Hon");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 300;
                                    containsCarIds = asSet(3, 4, 5);
                                    doesNotContainCarIds = asSet(0, 1, 2, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = contains(Car.FEATURES, "ade");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 300;
                                    containsCarIds = asSet(2, 3, 4);
                                    doesNotContainCarIds = asSet(0, 1, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = contains(Car.MANUFACTURER, "on");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 300;
                                    containsCarIds = asSet(3, 4, 5);
                                    doesNotContainCarIds = asSet(0, 1, 2, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = isContainedIn(Car.MANUFACTURER, "I would like to buy a Honda car");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 300;
                                    containsCarIds = asSet(3, 4, 5);
                                    doesNotContainCarIds = asSet(0, 1, 2, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(lessThan(Car.CAR_ID, 100), or(none(Car.class), isContainedIn(Car.MANUFACTURER, "I would like to buy a Honda car")));
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 30;
                                    containsCarIds = asSet(3, 4, 5);
                                    doesNotContainCarIds = asSet(0, 1, 2, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = isContainedIn(Car.FEATURES, "I would like a coupe or a sunroof please");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 200;
                                    containsCarIds = asSet(7, 9);
                                    doesNotContainCarIds = asSet(0, 1, 2, 3, 4, 5, 6, 8);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = matchesRegex(Car.MODEL, "F.+n");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 100;
                                    containsCarIds = asSet(1);
                                    doesNotContainCarIds = asSet(0, 2, 3, 4, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = matchesRegex(Car.FEATURES, ".*ade.*");
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 300;
                                    containsCarIds = asSet(2, 3, 4);
                                    doesNotContainCarIds = asSet(0, 1, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = or(between(Car.CAR_ID, 400, 500), between(Car.CAR_ID, 450, 550));
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.MATERIALIZE));
                                expectedResults = new ExpectedResults() {{
                                    size = 151;
                                    containsCarIds = integersBetween(400, 550);
                                    doesNotContainCarIds = asSet(399, 551);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = or(between(Car.CAR_ID, 400, 500), between(Car.CAR_ID, 450, 550));
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 151;
                                    containsCarIds = integersBetween(400, 550);
                                    doesNotContainCarIds = asSet(399, 551);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = or(between(Car.CAR_ID, 400, 450), or(between(Car.CAR_ID, 451, 499), between(Car.CAR_ID, 500, 550)));
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 151;
                                    containsCarIds = integersBetween(400, 550);
                                    doesNotContainCarIds = asSet(399, 551);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = endsWith(Car.FEATURES, "roof");
                                expectedResults = new ExpectedResults() {{
                                    size = 100;
                                    containsCarIds = asSet(7);
                                    doesNotContainCarIds = asSet(0, 1, 2, 3, 4, 5, 6, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = endsWith(Car.MODEL, "sight");
                                expectedResults = new ExpectedResults() {{
                                    size = 100;
                                    containsCarIds = asSet(5);
                                    doesNotContainCarIds = asSet(0, 1, 2, 3, 4, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Fusion"));
                                expectedResults = new ExpectedResults() {{
                                    size = 100;
                                    containsCarIds = asSet(1);
                                    doesNotContainCarIds = asSet(0, 2, 3, 4, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "XXX"));
                                expectedResults = new ExpectedResults() {{
                                    size = 0;
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = has(Car.FEATURES);
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 600;
                                    containsCarIds = asSet(1, 2, 3, 4, 7, 9);
                                    doesNotContainCarIds = asSet(0, 5, 6, 8);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = has(Car.FEATURES);
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.MATERIALIZE));
                                expectedResults = new ExpectedResults() {{
                                    size = 600;
                                    containsCarIds = asSet(1, 2, 3, 4, 7, 9);
                                    doesNotContainCarIds = asSet(0, 5, 6, 8);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = has(Car.MANUFACTURER);
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION));
                                expectedResults = new ExpectedResults() {{
                                    size = 1000;
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = has(Car.MANUFACTURER);
                                queryOptions = queryOptions(deduplicate(DeduplicationStrategy.MATERIALIZE));
                                expectedResults = new ExpectedResults() {{
                                    size = 1000;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            noIndexes(),
                            indexCombination(UniqueIndex.onAttribute(Car.CAR_ID)),
                            indexCombination(HashIndex.onAttribute(Car.CAR_ID)),
                            indexCombination(HashIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(10), Car.CAR_ID)),
                            indexCombination(NavigableIndex.onAttribute(Car.CAR_ID)),
                            indexCombination(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(10), Car.CAR_ID)),
                            indexCombination(NavigableIndex.onAttribute(Car.FEATURES)),
                            indexCombination(HashIndex.onAttribute(Car.FEATURES)),
                            indexCombination(RadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(InvertedRadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(ReversedRadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(CompoundIndex.onAttributes(Car.MANUFACTURER, Car.MODEL)),
                            indexCombination(CompoundIndex.withQuantizerOnAttributes(new Quantizer<CompoundValueTuple<Car>>() {
                                        @Override
                                        public CompoundValueTuple<Car> getQuantizedValue(CompoundValueTuple<Car> tuple) {
                                            Iterator<Object> tupleValues = tuple.getAttributeValues().iterator();
                                            String manufacturer = (String) tupleValues.next();
                                            String model = (String) tupleValues.next();
                                            String quantizedModel = "Focus".equals(model) ? "Focus" : "Other";
                                            return new CompoundValueTuple<Car>(Arrays.asList(manufacturer, quantizedModel));
                                        }
                                    }, Car.MANUFACTURER, Car.MODEL)
                            ),
                            indexCombination(OffHeapIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(DiskIndex.onAttribute(Car.FEATURES)),
                            indexCombination(DiskIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(OffHeapIndex.onAttribute(Car.MANUFACTURER))
                    );
                }},
                new MacroScenario() {{
                    name = "off-heap collection";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(OffHeapConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = in(Car.CAR_ID, 3, 4, 5);
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsAnyOrder = asSet(3, 4, 5);
                                    indexUsed = true; // An index should be used, because OffHeapPersistence creates an index on primary key
                                    mergeCost = 3;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            noIndexes()
                    );
                }},
                new MacroScenario() {{
                    name = "off-heap index";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(OffHeapConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                    indexUsed = true;
                                    mergeCost = 3;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(OffHeapIndex.onAttribute(Car.MANUFACTURER))
                    );
                }},
                new MacroScenario() {{
                    name = "disk index";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(DiskConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                    indexUsed = true;
                                    mergeCost = 3;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(DiskIndex.onAttribute(Car.MANUFACTURER))
                    );
                }},
                new MacroScenario() {{
                    name = "merge cost without indexes";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                    indexUsed = false;
                                    mergeCost = Integer.MAX_VALUE;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(noIndexes());
                }},
                new MacroScenario() {{
                    name = "merge costs with indexes";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                    indexUsed = true;
                                    mergeCost = 3;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(HashIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(NavigableIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(RadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(ReversedRadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(InvertedRadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(DiskIndex.onAttribute(Car.MANUFACTURER))
                    );
                }},
                new MacroScenario() {{
                    name = "retrieval cost without indexes";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = Integer.MAX_VALUE;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(noIndexes());
                }},
                new MacroScenario() {{
                    name = "retrieval cost with HashIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 30;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(HashIndex.onAttribute(Car.MANUFACTURER)));
                }},
                new MacroScenario() {{
                    name = "retrieval cost with NavigableIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 40;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(NavigableIndex.onAttribute(Car.MANUFACTURER)));
                }},
                new MacroScenario() {{
                    name = "retrieval cost with RadixTreeIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 50;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(RadixTreeIndex.onAttribute(Car.MANUFACTURER)));
                }},
                new MacroScenario() {{
                    name = "retrieval cost with ReversedRadixTreeIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 51;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(ReversedRadixTreeIndex.onAttribute(Car.MANUFACTURER)));
                }},
                new MacroScenario() {{
                    name = "retrieval cost with InvertedRadixTreeIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 52;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(InvertedRadixTreeIndex.onAttribute(Car.MANUFACTURER)));
                }},
                new MacroScenario() {{
                    name = "retrieval cost with SuffixTreeIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 53;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER)));
                }},
                new MacroScenario() {{
                    name = "retrieval cost with CompoundIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = and(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Fusion"));
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 20;
                                    mergeCost = 1;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(CompoundIndex.onAttributes(Car.MANUFACTURER, Car.MODEL)));
                }},
                new MacroScenario() {{
                    name = "retrieval cost with PartialNavigableIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = and(greaterThan(Car.PRICE, 4000.0), equal(Car.MANUFACTURER, "Ford"));
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 35; // 40 from NavigableIndex, -5 from PartialIndex
                                    mergeCost = 2;
                                    size = 2;
                                    carIdsAnyOrder = asSet(0, 2);
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(
                            PartialNavigableIndex.onAttributeWithFilterQuery(Car.PRICE, equal(Car.MANUFACTURER, "Ford"))
                    ));
                }},
                new MacroScenario() {{
                    name = "retrieval cost with PartialOffHeapIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = and(greaterThan(Car.PRICE, 4000.0), equal(Car.MANUFACTURER, "Ford"));
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 65; // 80 from SQLiteIndex, -10 from OffHeapIndex, -5 from PartialIndex
                                    mergeCost = 2;
                                    size = 2;
                                    carIdsAnyOrder = asSet(0, 2);
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(
                            PartialOffHeapIndex.onAttributeWithFilterQuery(Car.PRICE, equal(Car.MANUFACTURER, "Ford"))
                    ));
                }},
                new MacroScenario() {{
                    name = "retrieval cost with PartialDiskIndex";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = and(greaterThan(Car.PRICE, 4000.0), equal(Car.MANUFACTURER, "Ford"));
                                expectedResults = new ExpectedResults() {{
                                    retrievalCost = 85; // 80 from SQLiteIndex, +10 from DiskIndex, -5 from PartialIndex
                                    mergeCost = 2;
                                    size = 2;
                                    carIdsAnyOrder = asSet(0, 2);
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(indexCombination(
                            PartialDiskIndex.onAttributeWithFilterQuery(Car.PRICE, equal(Car.MANUFACTURER, "Ford"))
                    ));
                }},
                new MacroScenario() {{
                    name = "string queries 1";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = asList(
                            new QueryToEvaluate() {{
                                query = startsWith(Car.MANUFACTURER, "For");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    mergeCost = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                    indexUsed = true;
                                    containsCarIds = asSet(1);
                                    doesNotContainCarIds = asSet(5, 8);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    mergeCost = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                    indexUsed = true;
                                    containsCarIds = asSet(1);
                                    doesNotContainCarIds = asSet(5, 8);
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(RadixTreeIndex.onAttribute(Car.MANUFACTURER))
                    );
                }},
                new MacroScenario() {{
                    name = "string queries 2";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = asList(
                            new QueryToEvaluate() {{
                                query = endsWith(Car.MANUFACTURER, "ord");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    mergeCost = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                    indexUsed = true;
                                    containsCarIds = asSet(1);
                                    doesNotContainCarIds = asSet(5, 8);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    mergeCost = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                    indexUsed = true;
                                    containsCarIds = asSet(1);
                                    doesNotContainCarIds = asSet(5, 8);
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(ReversedRadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER))
                    );
                }},
                new MacroScenario() {{
                    name = "string queries 3";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = asList(
                            new QueryToEvaluate() {{
                                query = contains(Car.MANUFACTURER, "on");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    mergeCost = 3;
                                    carIdsAnyOrder = asSet(3, 4, 5);
                                    indexUsed = true;
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    mergeCost = 3;
                                    carIdsAnyOrder = asSet(0, 1, 2);
                                    indexUsed = true;
                                    containsCarIds = asSet(1);
                                    doesNotContainCarIds = asSet(5, 8);
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER))
                    );
                }},
                new MacroScenario() {{
                    name = "ordering";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = asList(
                            new QueryToEvaluate() {{
                                query = in(Car.MANUFACTURER, "Ford", "Toyota");
                                queryOptions = queryOptions(orderBy(descending(Car.MANUFACTURER), ascending(Car.CAR_ID)));
                                expectedResults = new ExpectedResults() {{
                                    size = 6;
                                    carIdsInOrder = asList(6, 7, 8, 0, 1, 2);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = all(Car.class);
                                // Ascending regular order: <cars with no features> <cars with features in ascending feature order>
                                queryOptions = queryOptions(orderBy(ascending(Car.FEATURES), descending(Car.CAR_ID)));
                                expectedResults = new ExpectedResults() {{
                                    size = 10;
                                    carIdsInOrder = asList(8, 6, 5, 0, 9, 2, 3, 4, 1, 7);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = all(Car.class);
                                // Ascending missingLast order: <cars with features in ascending feature order> <cars with no features>
                                queryOptions = queryOptions(orderBy(ascending(missingLast(Car.FEATURES)), descending(Car.CAR_ID)));
                                expectedResults = new ExpectedResults() {{
                                    size = 10;
                                    carIdsInOrder = asList(9, 2, 3, 4, 1, 7, 8, 6, 5, 0);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = all(Car.class);
                                // Descending regular order: <cars with features in descending feature order> <cars with no features>
                                queryOptions = queryOptions(orderBy(descending(Car.FEATURES), descending(Car.CAR_ID)));
                                expectedResults = new ExpectedResults() {{
                                    size = 10;
                                    carIdsInOrder = asList(7, 1, 4, 3, 2, 9, 8, 6, 5, 0);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = all(Car.class);
                                // Descending missingFirst order: <cars with no features> <cars with features in descending feature order>
                                queryOptions = queryOptions(orderBy(descending(missingFirst(Car.FEATURES)), descending(Car.CAR_ID)));
                                expectedResults = new ExpectedResults() {{
                                    size = 10;
                                    carIdsInOrder = asList(8, 6, 5, 0, 7, 1, 4, 3, 2, 9);
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            noIndexes(),
                            indexCombination(
                                    NavigableIndex.onAttribute(Car.CAR_ID),
                                    NavigableIndex.onAttribute(Car.FEATURES),
                                    NavigableIndex.onAttribute(forObjectsMissing(Car.FEATURES))
                            )
                    );
                }},
                new MacroScenario() {{
                    name = "index ordering";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = asList(
                            // Force use of the index ordering strategy (set selectivity threshold = 1.0)...
                            new QueryToEvaluate() {{
                                query = all(Car.class);
                                queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 10;
                                    carIdsInOrder = asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 4, 6);
                                queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsInOrder = asList(4, 5, 6);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = lessThan(Car.CAR_ID, 6);
                                queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 6;
                                    carIdsInOrder = asList(0, 1, 2, 3, 4, 5);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = lessThanOrEqualTo(Car.CAR_ID, 6);
                                queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 7;
                                    carIdsInOrder = asList(0, 1, 2, 3, 4, 5, 6);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = greaterThan(Car.CAR_ID, 3);
                                queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 6;
                                    carIdsInOrder = asList(4, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = greaterThanOrEqualTo(Car.CAR_ID, 3);
                                queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 7;
                                    carIdsInOrder = asList(3, 4, 5, 6, 7, 8, 9);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = all(Car.class);
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 10;
                                    carIdsInOrder = asList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 4, 6);
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsInOrder = asList(6, 5, 4);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = lessThan(Car.CAR_ID, 6);
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 6;
                                    carIdsInOrder = asList(5, 4 ,3 ,2 ,1, 0);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = lessThanOrEqualTo(Car.CAR_ID, 6);
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 7;
                                    carIdsInOrder = asList(6, 5, 4 ,3 ,2 ,1, 0);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = greaterThan(Car.CAR_ID, 3);
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 6;
                                    carIdsInOrder = asList(9, 8, 7, 6, 5, 4);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = greaterThanOrEqualTo(Car.CAR_ID, 3);
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 7;
                                    carIdsInOrder = asList(9, 8, 7, 6, 5, 4, 3);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(greaterThanOrEqualTo(Car.CAR_ID, 3), lessThanOrEqualTo(Car.CAR_ID, 6));
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 4;
                                    carIdsInOrder = asList(6, 5, 4, 3);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = or(greaterThanOrEqualTo(Car.CAR_ID, 7), none(Car.class));
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsInOrder = asList(9, 8, 7);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(or(greaterThanOrEqualTo(Car.CAR_ID, 7), none(Car.class)), or(all(Car.class), none(Car.class)));
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsInOrder = asList(9, 8, 7);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = and(equal(Car.CAR_ID, 8), all(Car.class));
                                queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 1;
                                    carIdsInOrder = singletonList(8);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = all(Car.class);
                                // Should order cars without any features first, followed by cars with features
                                // in ascending alphabetical order of feature string...
                                queryOptions = queryOptions(orderBy(ascending(Car.FEATURES), ascending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 10;
                                    carIdsInOrder = asList(0, 5, 6, 8, 9, 2, 3, 4, 1, 7);
                                }};
                            }},
                            new QueryToEvaluate() {{
                                query = all(Car.class);
                                // Should order cars without any features last, preceded by cars with features
                                // in descending alphabetical order of feature string...
                                queryOptions = queryOptions(orderBy(descending(Car.FEATURES), ascending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 10;
                                    carIdsInOrder = asList(7, 1, 4, 3, 2, 9, 0, 5, 6, 8);
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(
                                    NavigableIndex.onAttribute(Car.CAR_ID),
                                    NavigableIndex.onAttribute(Car.FEATURES),
                                    NavigableIndex.onAttribute(forObjectsMissing(Car.FEATURES))
                            ),
                            indexCombination(OffHeapIndex.onAttribute(Car.CAR_ID))
                    );
                }},
                new MacroScenario() {{
                        name = "index ordering strategy selection";
                        dataSet = SMALL_DATASET;
                        collectionImplementations = classes(ConcurrentIndexedCollection.class);
                        queriesToEvaluate = asList(
                                new QueryToEvaluate() {{
                                    query = between(Car.CAR_ID, 4, 6); // querySelectivity = 1.0 - 3/10 = 0.7
                                    queryOptions = queryOptions(orderBy(ascending(Car.MANUFACTURER), descending(Car.PRICE)),
                                            applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 0.8))); // selectivityThreshold = 0.8 -> use index ordering
                                    expectedResults = new ExpectedResults() {{
                                        size = 3;
                                        carIdsInOrder = asList(5, 4, 6);
                                        containsQueryLogMessages = asList("querySelectivity: 0.7", "orderingStrategy: index");
                                    }};
                                }},
                                new QueryToEvaluate() {{
                                    query = between(Car.CAR_ID, 4, 6); // querySelectivity = 1.0 - 3/10 = 0.7
                                    queryOptions = queryOptions(orderBy(ascending(Car.MANUFACTURER), descending(Car.PRICE)),
                                            applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 0.6))); // selectivityThreshold = 0.6 -> use materialize ordering
                                    expectedResults = new ExpectedResults() {{
                                        size = 3;
                                        carIdsInOrder = asList(5, 4, 6);
                                        containsQueryLogMessages = asList("querySelectivity: 0.7", "orderingStrategy: materialize");
                                    }};
                                }}//, TODO: the following test is disabled until index ordering support is enabled by default...
//                                new QueryToEvaluate() {{
//                                    query = between(Car.CAR_ID, 4, 6); // querySelectivity = 1.0 - 3/10 = 0.7
//                                    queryOptions = queryOptions(orderBy(ascending(Car.MANUFACTURER), descending(Car.PRICE))); // selectivityThreshold = default of 0.5 -> use materialize ordering
//                                    expectedResults = new ExpectedResults() {{
//                                        size = 3;
//                                        carIdsInOrder = asList(5, 4, 6);
//                                        containsQueryLogMessages = asList("querySelectivity: 0.7", "orderingStrategy: materialize");
//                                    }};
//                                }}
                        );
                        indexCombinations = indexCombinations(
                                indexCombination(
                                        NavigableIndex.onAttribute(Car.CAR_ID),
                                        NavigableIndex.onAttribute(Car.MANUFACTURER),
                                        NavigableIndex.onAttribute(Car.PRICE)
                                )
                        );
                    }},
                new MacroScenario() {{
                    name = "index ordering strategy selection - negative";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = asList(
                            // Try to force use of the index ordering strategy (set selectivity threshold = 1.0),
                            // however it will not actually be used...
                            new QueryToEvaluate() {{
                                query = between(Car.CAR_ID, 4, 6); // querySelectivity = 1.0 - 3/10 = 0.7
                                queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)));
                                expectedResults = new ExpectedResults() {{
                                    size = 3;
                                    carIdsInOrder = asList(6, 5, 4);
                                    // The materialize strategy will be used instead because the index is quantized...
                                    containsQueryLogMessages = singletonList("orderingStrategy: materialize");
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(5), Car.CAR_ID))
                    );
                }},
                new MacroScenario() {{
                    name = "remove objects";
                    dataSet = REGULAR_DATASET;
                    removeDataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    size = 297;
                                    doesNotContainCarIds = asSet(0, 1, 2);
                                    containsCarIds = asSet(10, 11, 12);
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            noIndexes(),
                            indexCombination(UniqueIndex.onAttribute(Car.CAR_ID)),
                            indexCombination(HashIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(NavigableIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(RadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(ReversedRadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(InvertedRadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(OffHeapIndex.onAttribute(Car.MANUFACTURER))
                    );
                }},
                new MacroScenario() {{
                    name = "clear objects";
                    dataSet = REGULAR_DATASET;
                    clearDataSet = true;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = equal(Car.MANUFACTURER, "Ford");
                                expectedResults = new ExpectedResults() {{
                                    size = 0;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            noIndexes(),
                            indexCombination(UniqueIndex.onAttribute(Car.CAR_ID)),
                            indexCombination(HashIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(NavigableIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(RadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(ReversedRadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(InvertedRadixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER)),
                            indexCombination(OffHeapIndex.onAttribute(Car.MANUFACTURER))
                    );
                }},
                new MacroScenario() {{
                    name = "standing query index on entire query";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = or(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Car.Color.BLUE));
                                expectedResults = new ExpectedResults() {{
                                    size = 5;
                                    retrievalCost = 10;
                                    mergeCost = 5;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(StandingQueryIndex.onQuery(or(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Car.Color.BLUE))))
                    );
                }},
                new MacroScenario() {{
                    name = "standing query index on nested logical query";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = and(all(Car.class), or(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Car.Color.BLUE)));
                                expectedResults = new ExpectedResults() {{
                                    size = 5;
                                    retrievalCost = 10;
                                    mergeCost = 5;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(StandingQueryIndex.onQuery(or(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Car.Color.BLUE))))
                    );
                }},
                new MacroScenario() {{
                    name = "standing query index on nested simple query";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = and(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Car.Color.RED));
                                expectedResults = new ExpectedResults() {{
                                    size = 2;
                                    retrievalCost = 10;
                                    mergeCost = 3; // there are 3 RED cars in total (although only 2 of them are Ford)
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(StandingQueryIndex.onQuery(equal(Car.COLOR, Car.Color.RED)))
                    );
                }},
                new MacroScenario() {{
                    name = "HashIndex on standing query";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = or(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Car.Color.BLUE));
                                expectedResults = new ExpectedResults() {{
                                    size = 5;
                                    retrievalCost = 30;
                                    mergeCost = 5;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(HashIndex.onAttribute(forStandingQuery(or(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Car.Color.BLUE)))))
                    );
                }},
                new MacroScenario() {{
                    name = "OffHeapIndex on standing query";
                    dataSet = SMALL_DATASET;
                    collectionImplementations = classes(ConcurrentIndexedCollection.class);
                    queriesToEvaluate = singletonList(
                            new QueryToEvaluate() {{
                                query = or(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Car.Color.BLUE));
                                expectedResults = new ExpectedResults() {{
                                    size = 5;
                                    retrievalCost = 70; // 80 from SQLiteIndex, -10 from OffHeapIndex
                                    mergeCost = 5;
                                }};
                            }}
                    );
                    indexCombinations = indexCombinations(
                            indexCombination(OffHeapIndex.onAttribute(forStandingQuery(
                                    or(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Car.Color.BLUE))
                                ))
                            )
                    );
                }}
        );
    }

    private static SimpleAttribute<Integer, Car> createForeignKeyAttribute() {
        return new SimpleAttribute<Integer, Car>() {
            @Override
            public Car getValue(final Integer carId, final QueryOptions queryOptions) {
                return CarFactory.createCar(carId);
            }
        };
    }


    static class MacroScenario {
        String name = "<unnamed>";
        Collection<Car> dataSet = Collections.emptySet();
        Collection<Car> removeDataSet = Collections.emptySet();
        Boolean clearDataSet = false;
        Boolean alsoEvaluateWithIndexMergeStrategy = false;
        Iterable<? extends QueryToEvaluate> queriesToEvaluate = Collections.emptyList();
        Iterable<Class> collectionImplementations;
        Iterable<Iterable<Index>> indexCombinations;
    }

    public static class Scenario {
        String name = "<unnamed>";
        Integer lineNumber;
        Long startTime;
        Integer scenarioNumber;
        AtomicInteger totalScenarioCount;
        Collection<Car> dataSet = Collections.emptySet();
        Collection<Car> removeDataSet = Collections.emptySet();
        Boolean clearDataSet = false;
        Query<Car> query = none(Car.class);
        QueryOptions queryOptions = noQueryOptions();
        ExpectedResults expectedResults = null;
        Class collectionImplementation;
        Iterable<Index> indexCombination;
        boolean useIndexMergeStrategy = false;
        boolean highPriority = false;

        @Override
        public String toString() {
            return "[" +
                    "name='" + name + '\'' +
                    ", line=" + lineNumber +
                    ", collection=" + collectionImplementation.getSimpleName().replace("IndexedCollection", "").toLowerCase() +
                    ", indexes=" + getIndexDescriptions(indexCombination) +
                    ", mergeStrategy=" + (useIndexMergeStrategy ? "index" : "default") +
                    ", query=" + query +
                    ", queryOptions=" + queryOptions +
                    ", dataSet=<" + dataSet.size() + " items>" +
                    ", removeDataSet=<" + removeDataSet.size() + " items>" +
                    ", clearDataSet=" + clearDataSet +
                    ", expectedResults=" + expectedResults +
                    ']';
        }
    }

    @DataProvider
    public static List<List<Object>> expandMacroScenarios() {
        List<MacroScenario> macroScenarios = getMacroScenarios();
        List<List<Object>> scenarios = new ArrayList<List<Object>>();
        final long finalStartTime = System.currentTimeMillis();
        final AtomicInteger scenarioCount = new AtomicInteger();
        for (int i = 0; i < macroScenarios.size(); i++) {
            final MacroScenario macroScenario = macroScenarios.get(i);
            try {
                for (final Class currentCollectionImplementation : macroScenario.collectionImplementations) {
                    for (final Iterable<Index> currentIndexCombination : macroScenario.indexCombinations) {
                        for (final QueryToEvaluate currentQueryToEvaluate : macroScenario.queriesToEvaluate) {
                            Scenario scenario = new Scenario() {{
                                name = macroScenario.name;
                                lineNumber = currentQueryToEvaluate.lineNumber;
                                startTime = finalStartTime;
                                scenarioNumber = scenarioCount.incrementAndGet();
                                totalScenarioCount = scenarioCount;
                                dataSet = macroScenario.dataSet;
                                removeDataSet = macroScenario.removeDataSet;
                                clearDataSet = macroScenario.clearDataSet;
                                query = currentQueryToEvaluate.query;
                                queryOptions = currentQueryToEvaluate.queryOptions;
                                expectedResults = currentQueryToEvaluate.expectedResults;
                                collectionImplementation = currentCollectionImplementation;
                                indexCombination = currentIndexCombination;
                                highPriority = currentQueryToEvaluate.highPriority;
                            }};
                            scenarios.add(Collections.<Object>singletonList(scenario));
                            if (macroScenario.alsoEvaluateWithIndexMergeStrategy) {
                                Scenario scenarioWithIndexMergeStrategy = new Scenario() {{
                                    name = macroScenario.name;
                                    lineNumber = currentQueryToEvaluate.lineNumber;
                                    startTime = finalStartTime;
                                    scenarioNumber = scenarioCount.incrementAndGet();
                                    totalScenarioCount = scenarioCount;
                                    dataSet = macroScenario.dataSet;
                                    removeDataSet = macroScenario.removeDataSet;
                                    clearDataSet = macroScenario.clearDataSet;
                                    query = currentQueryToEvaluate.query;
                                    queryOptions = currentQueryToEvaluate.queryOptions;
                                    expectedResults = currentQueryToEvaluate.expectedResults;
                                    collectionImplementation = currentCollectionImplementation;
                                    indexCombination = currentIndexCombination;
                                    highPriority = currentQueryToEvaluate.highPriority;
                                    useIndexMergeStrategy = true;
                                }};
                                scenarios.add(Collections.<Object>singletonList(scenarioWithIndexMergeStrategy));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException("Configuration issue for MacroScenario " + i + " in the list: " + macroScenario.name, e);
            }
        }
        if (SKIP_SLOW_SCENARIOS) {
            System.out.println("    === Note: slow scenarios in the functional test are disabled ===");
        } else {
            System.out.println("    === Note: all scenarios in the functional test are enabled ===\n    To skip slow scenarios, run with: -Dcqengine.skip.slow.scenarios=true");
        }
        return scenarios;
    }

    @Test
    @UseDataProvider(value = "expandMacroScenarios")
    @SuppressWarnings("unchecked")
    public void testScenario(Scenario scenario) {
        if (RUN_HIGH_PRIORITY_SCENARIOS_ONLY && !scenario.highPriority) {
            throw new AssumptionViolatedException("Skipping non-high priority scenario");
        }

        if (!IndexedCollection.class.isAssignableFrom(scenario.collectionImplementation)) {
            throw new IllegalStateException("Invalid IndexedCollection class: " + scenario.collectionImplementation);
        }
        boolean hasDiskIndex = false, hasOffHeapIndex = false;
        for (Index<Car> index : scenario.indexCombination) {
            if (index instanceof DiskTypeIndex) {
                hasDiskIndex = true;
            }
            else if (index instanceof OffHeapTypeIndex) {
                hasOffHeapIndex = true;
            }
        }
        Persistence<Car, Integer> persistence;
        if (hasDiskIndex && hasOffHeapIndex) {
            persistence = CompositePersistence.of(OffHeapPersistence.onPrimaryKey(Car.CAR_ID), DiskPersistence.onPrimaryKey(Car.CAR_ID));
        }
        else if (hasDiskIndex) {
            persistence = DiskPersistence.onPrimaryKey(Car.CAR_ID);
        }
        else if (hasOffHeapIndex) {
            persistence = OffHeapPersistence.onPrimaryKey(Car.CAR_ID);
        }
        else {
            persistence = OnHeapPersistence.onPrimaryKey(Car.CAR_ID);
        }
        IndexedCollection<Car> indexedCollection;
        try {
            if (TransactionalIndexedCollection.class.isAssignableFrom(scenario.collectionImplementation)) {
                indexedCollection = (IndexedCollection<Car>) scenario.collectionImplementation.getConstructor(Class.class, Persistence.class).newInstance(Car.class, persistence);
            }
            else if (OffHeapConcurrentIndexedCollection.class.isAssignableFrom(scenario.collectionImplementation)) {
                if (!(persistence instanceof OffHeapTypeIndex)) {
                    persistence =  CompositePersistence.of(OffHeapPersistence.onPrimaryKey(Car.CAR_ID), persistence);
                }
                indexedCollection = (IndexedCollection<Car>) scenario.collectionImplementation.getConstructor(Persistence.class).newInstance(persistence);
            }
            else {
                indexedCollection = (IndexedCollection<Car>) scenario.collectionImplementation.getConstructor(Persistence.class).newInstance(persistence);
            }
        }
        catch (Exception e) {
            throw new IllegalStateException("Could not instantiate IndexedCollection: " + scenario.collectionImplementation, e);
        }
        for (Index<Car> index : scenario.indexCombination) {
            if (!persistenceProvidesEquivalentIndexAlready(persistence, index)) {
                try {
                    indexedCollection.addIndex(index);
                }
                catch (Exception e) {
                    throw new IllegalStateException("Could not add " + getIndexDescription(index) + " with persistence type: " + persistence, e);
                }
            }
        }
        indexedCollection.addAll(scenario.dataSet);

        indexedCollection.removeAll(scenario.removeDataSet);

        if (scenario.clearDataSet) {
            indexedCollection.clear();
        }

        FlagsEnabled flagsEnabled = scenario.queryOptions.get(FlagsEnabled.class);
        if (flagsEnabled == null) {
            flagsEnabled = new FlagsEnabled();
        }
        if (scenario.useIndexMergeStrategy) {
            flagsEnabled.add(EngineFlags.PREFER_INDEX_MERGE_STRATEGY);
        }
        else {
            flagsEnabled.remove(EngineFlags.PREFER_INDEX_MERGE_STRATEGY);
        }
        scenario.queryOptions.put(FlagsEnabled.class, flagsEnabled);

        try {
            evaluateQuery(indexedCollection, scenario.query, scenario.queryOptions, scenario.expectedResults);
        }
        catch (RuntimeException e) {
            System.err.println("Failed scenario: " + scenario);
            throw e;
        }
        catch (AssertionError e) {
            System.err.println("Failed scenario: " + scenario);
            throw e;
        }
        finally {
            int scenarioNumber = scenario.scenarioNumber;
            int totalScenarioCount = scenario.totalScenarioCount.get();
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastStatusTimestamp) >= STATUS_UPDATE_FREQUENCY_MS || scenarioNumber == totalScenarioCount || scenarioNumber == 1) {
                double fractionComplete = ((double) scenarioNumber) / totalScenarioCount;
                long elapsedTime = currentTime - scenario.startTime;
                System.out.format("    Scenario %d of %d :: %d seconds elapsed :: %d%% complete", scenarioNumber, totalScenarioCount, elapsedTime / 1000, (int)(fractionComplete * 100));
                System.out.print(scenarioNumber == totalScenarioCount ? "\n" : "\r");
                lastStatusTimestamp = currentTime;
            }
            if (persistence instanceof CompositePersistence) {
                CompositePersistence compositePersistence = (CompositePersistence) persistence;
                closePersistenceIfNecessary(compositePersistence.getPrimaryPersistence());
                closePersistenceIfNecessary(compositePersistence.getSecondaryPersistence());
                List<? extends Persistence<Car, Integer>> additionalPersistences = compositePersistence.getAdditionalPersistences();
                for (Persistence<Car, Integer> additionalPersistence : additionalPersistences) {
                    closePersistenceIfNecessary(additionalPersistence);
                }
            }
            else {
                closePersistenceIfNecessary(persistence);
            }
        }
    }

    static void closePersistenceIfNecessary(Persistence<Car, Integer> persistence) {
        if (persistence instanceof DiskPersistence) {
            DiskPersistence diskPersistence = (DiskPersistence) persistence;
            diskPersistence.close();
            File diskPersistenceFile = diskPersistence.getFile();
            if (!diskPersistenceFile.delete()) {
                throw new IllegalStateException("Failed to delete temporary disk persistence file: " + diskPersistenceFile);
            }
        }
        else if (persistence instanceof OffHeapPersistence) {
            ((OffHeapPersistence) persistence).close();
        }
    }

    static boolean persistenceProvidesEquivalentIndexAlready(Persistence<Car, Integer> persistence, Index<Car> indexToBeAdded) {
        if (persistence != null && !(persistence instanceof OnHeapPersistence)) {
            // Persistence is non-heap therefore has a primary key index already.
            if (indexToBeAdded instanceof AttributeIndex) {
                if (Car.CAR_ID.equals(((AttributeIndex) indexToBeAdded).getAttribute())) {
                    // Collection will already have an identity index on this attribute...
                    return true;
                }
            }
        }
        return false;
    }

    static void evaluateQuery(IndexedCollection<Car> indexedCollection, Query<Car> query, QueryOptions queryOptions, ExpectedResults expectedResults) {
        if (expectedResults == null) {
            throw new IllegalStateException("No expectedResults configured for query: " + query);
        }
        try {
            if (expectedResults.containsQueryLogMessages != null) {
                queryOptions.put(QueryLog.class, new QueryLog(new AppendableCollection()));
            }
            ResultSet<Car> results = indexedCollection.retrieve(query, queryOptions);
            try {
                if (expectedResults.size != null) {
                    assertEquals("size mismatch for query: " + query, expectedResults.size.intValue(), results.size());
                }
                if (expectedResults.mergeCost != null) {
                    assertEquals("mergeCost mismatch for query: " + query, expectedResults.mergeCost.intValue(), results.getMergeCost());
                }
                if (expectedResults.retrievalCost != null) {
                    assertEquals("retrievalCost mismatch for query: " + query, expectedResults.retrievalCost.intValue(), results.getRetrievalCost());
                }
                if (expectedResults.indexUsed != null) {
                    assertEquals("indexUsed mismatch for query: " + query, expectedResults.indexUsed, results.getRetrievalCost() < Integer.MAX_VALUE);
                }
                if (expectedResults.carIdsAnyOrder != null) {
                    assertEquals("carIdsAnyOrder mismatch for query: " + query, expectedResults.carIdsAnyOrder, extractCarIds(results, new HashSet<Integer>()));
                }
                if (expectedResults.carIdsInOrder != null) {
                    assertEquals("carIdsInOrder mismatch for query: " + query, expectedResults.carIdsInOrder, extractCarIds(results, new ArrayList<Integer>()));
                }
                if (expectedResults.containsCarIds != null) {
                    // Validate ResultSet.contains()...
                    for (Integer carId : expectedResults.containsCarIds) {
                        assertTrue("containsCarIds mismatch, results do not contain carId " + carId + " for query: " + query, results.contains(CarFactory.createCar(carId)));
                    }
                    // Validate actual results returned by ResultSet.iterator()...
                    Set<Integer> expectedCarIds = new HashSet<Integer>(expectedResults.containsCarIds);
                    for (Car car : results) {
                        if (expectedCarIds.contains(car.getCarId())) {
                            expectedCarIds.remove(car.getCarId());
                        }
                        if (expectedCarIds.isEmpty()) {
                            break;
                        }
                    }
                    assertTrue("containsCarIds mismatch, iterated results do not include carIds " + expectedCarIds + " for query: " + query, expectedCarIds.isEmpty());
                }
                if (expectedResults.doesNotContainCarIds != null) {
                    // Validate ResultSet.contains()...
                    for (Integer carId : expectedResults.doesNotContainCarIds) {
                        assertFalse("doesNotContainCarIds mismatch, results contain carId " + carId + " for query: " + query, results.contains(CarFactory.createCar(carId)));
                    }
                    // Validate actual results returned by ResultSet.iterator()...
                    for (Car car : results) {
                        assertFalse("doesNotContainCarIds mismatch, results contain carId " + car.getCarId() + " for query: " + query, expectedResults.doesNotContainCarIds.contains(car.getCarId()));
                    }
                }
                if (expectedResults.containsQueryLogMessages != null) {
                    QueryLog queryLog = queryOptions.get(QueryLog.class);
                    AppendableCollection messages = (AppendableCollection)queryLog.getSink();
                    for (String expectedMessage : expectedResults.containsQueryLogMessages) {
                        assertTrue("QueryLog does not contain message '" + expectedMessage + "' for query: " + query + ", messages contained: " + messages, messages.contains(expectedMessage));
                    }
                }
            }
            finally {
                results.close();
            }
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to retrieve results for query: " + query, e);
        }
    }

    static class QueryToEvaluate {
        final int lineNumber = getLineNumber();
        Query<Car> query = none(Car.class);
        QueryOptions queryOptions = noQueryOptions();
        ExpectedResults expectedResults = null;
        boolean highPriority = false;

        static int getLineNumber() {
            StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
            for (int i=1; i<stElements.length; i++) {
                StackTraceElement ste = stElements[i];
                if (ste.getClassName() != null
                        && ste.getClassName().startsWith(IndexedCollectionFunctionalTest.class.getName())
                        && !ste.getClassName().endsWith(QueryToEvaluate.class.getName())) {
                    return ste.getLineNumber();
                }
            }
            return -1;
        }
    }

    static class ExpectedResults {
        Integer size;
        Integer retrievalCost;
        Integer mergeCost;
        Boolean indexUsed;
        Collection<String> containsQueryLogMessages;
        List<Integer> carIdsInOrder;
        Set<Integer> carIdsAnyOrder;
        Set<Integer> containsCarIds;
        Set<Integer> doesNotContainCarIds;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            if (size != null) {
                sb.append("size=").append(size);
                first = false;
            }
            if (retrievalCost != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("retrievalCost=").append(retrievalCost);
                first = false;
            }
            if (mergeCost != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("mergeCost=").append(mergeCost);
                first = false;
            }
            if (indexUsed != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("indexUsed=").append(indexUsed);
                first = false;
            }
            if (carIdsInOrder != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("carIdsInOrder=").append(carIdsInOrder);
                first = false;
            }
            if (carIdsAnyOrder != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("carIdsAnyOrder=").append(carIdsAnyOrder);
                first = false;
            }
            if (containsCarIds != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("containsCarIds=").append(containsCarIds);
                first = false;
            }
            if (doesNotContainCarIds != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("doesNotContainCarIds=").append(doesNotContainCarIds);
            }
            if (containsQueryLogMessages != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append("containsQueryLogMessages=").append(containsQueryLogMessages);
            }
            sb.append("]");
            return sb.toString();
        }
    }

    @SuppressWarnings("unchecked")
    static List<Class> classes(Class<?>... indexedCollectionClasses) {
        return Arrays.<Class>asList(indexedCollectionClasses);

    }

    static Iterable<? extends Index> noIndexes() {
        return indexCombination();
    }
    static Iterable<Index> indexCombination(Index... indexes) {
        return Arrays.asList(indexes);
    }

    @SuppressWarnings("unchecked")
    static Iterable<Iterable<Index>> indexCombinations(Iterable... indexSets) {
        return Arrays.<Iterable<Index>>asList(indexSets);
    }

    static String getIndexDescriptions(Iterable<Index> indexes) {
        StringBuilder sb = new StringBuilder("[");
        for (Iterator<Index> iterator = indexes.iterator(); iterator.hasNext(); ) {
            Index index = iterator.next();
            sb.append(getIndexDescription(index));
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    static String getIndexDescription(Index index) {
        String description = index.getClass().getSimpleName();
        if (description.isEmpty()) {
            // Anonymous inner class, use enclosing class name...
            description = index.getClass().getEnclosingClass().getSimpleName();
            if (description.isEmpty()) {
                throw new IllegalStateException("Failed to determine name for index: " + index.getClass());
            }
        }
        if (index instanceof CompoundIndex) {
            description += ".onAttribute(<CompoundAttribute>)";
        }
        else if (index instanceof AttributeIndex) {
            Attribute attribute = ((AttributeIndex) index).getAttribute();
            if (attribute instanceof StandingQueryAttribute) {
                description += ".onAttribute(" + attribute.getAttributeName() + ")";
            }
            else {
                description += ".onAttribute(" + attribute.getObjectType().getSimpleName() + "." + attribute.getAttributeName() + ")";

            }
        }
        if (index instanceof AbstractMapBasedAttributeIndex && index.isQuantized()) {
            description += " (quantized)";
        }
        return description;
    }

    public static <C extends Collection<Integer>> C extractCarIds(Iterable<Car> resultSet, C output) {
        for (Car car : resultSet) {
            output.add(car.getCarId());
        }
        return output;
    }

    public static Set<Integer> asSet(Integer... integers) {
        return new HashSet<Integer>(asList(integers));
    }

    static SortedSet<Integer> integersBetween(int lower, int upper) {
        SortedSet<Integer> treeSet = new TreeSet<Integer>();
        for (int i = lower; i <= upper; i++) {
            treeSet.add(i);
        }
        return treeSet;
    }

    static class AppendableCollection extends LinkedList<String> implements Appendable {

        final String lineSeparator = System.getProperty("line.separator");
        @Override
        public Appendable append(CharSequence csq) throws IOException {
            if (!lineSeparator.equals(csq)) {
                super.add(String.valueOf(csq));
            }
            return this;
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Appendable append(char c) throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}
