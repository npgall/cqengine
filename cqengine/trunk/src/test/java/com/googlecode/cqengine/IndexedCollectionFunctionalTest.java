package com.googlecode.cqengine;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.common.AbstractMapBasedAttributeIndex;
import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.index.compound.support.CompoundValueTuple;
import com.googlecode.cqengine.index.offheap.OffHeapIndex;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.radixinverted.InvertedRadixTreeIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.quantizer.IntegerQuantizer;
import com.googlecode.cqengine.quantizer.Quantizer;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationStrategy;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static com.googlecode.cqengine.index.offheap.TemporaryDatabase.TemporaryFileDatabase;
import static com.googlecode.cqengine.index.offheap.TemporaryDatabase.TemporaryInMemoryDatabase;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.option.OrderingStrategy.INDEX;
import static java.util.Arrays.asList;
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

    @ClassRule
    public static TemporaryInMemoryDatabase temporaryInMemoryDatabase = new TemporaryInMemoryDatabase();

    @ClassRule
    public static TemporaryFileDatabase temporaryFileDatabase = new TemporaryFileDatabase();

    // Macro scenarios specify sets of IndexedCollection implementations,
    // sets of index combinations, and sets of queries. Macro scenarios will be expanded
    // to many individual scenarios comprised of the distinct combinations of these sets.
    // Each individual scenario resulting from the expansion will be run as a separate test.
    // This allows to run the same set of queries with different combinations of indexes etc.
    static List<MacroScenario> getMacroScenarios() {
        return Arrays.asList(
            new MacroScenario() {{
                name = "typical queries and deduplication";
                dataSet = REGULAR_DATASET;
                collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                queriesToEvaluate = asList(
                        new QueryToEvaluate() {{
                            query = equal(Car.CAR_ID, 500);
                            expectedResults = new ExpectedResults() {{
                                size = 1;
                                carIdsAnyOrder = asSet(500);
                            }};
                        }}
                        ,
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
                                size = 202;
                                containsCarIds = integersBetween(400, 550);
                                doesNotContainCarIds = asSet(399, 551);
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
                        indexCombination(OffHeapIndex.onAttribute(
                                        Car.MANUFACTURER,
                                        Car.CAR_ID,
                                        new SimpleAttribute<Integer, Car>() {
                                            @Override
                                            public Car getValue(final Integer carId, final QueryOptions queryOptions) {
                                                return CarFactory.createCar(carId);
                                            }
                                        },
                                        temporaryInMemoryDatabase.getConnectionManager(true)
                                )

                        ),
                        indexCombination(OffHeapIndex.onAttribute(
                                        Car.FEATURES,
                                        Car.CAR_ID,
                                        new SimpleAttribute<Integer, Car>() {
                                            @Override
                                            public Car getValue(final Integer carId, final QueryOptions queryOptions) {
                                                return CarFactory.createCar(carId);
                                            }
                                        },
                                        temporaryInMemoryDatabase.getConnectionManager(true)
                                )

                        ),
                        indexCombination(OffHeapIndex.onAttribute(
                                        Car.MANUFACTURER,
                                        Car.CAR_ID,
                                        new SimpleAttribute<Integer, Car>() {
                                            @Override
                                            public Car getValue(final Integer carId, final QueryOptions queryOptions) {
                                                return CarFactory.createCar(carId);
                                            }
                                        },
                                        temporaryFileDatabase.getConnectionManager(true)
                                )

                        )
                );
            }},
            new MacroScenario() {{
                name = "merge cost without indexes";
                dataSet = SMALL_DATASET;
                collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                queriesToEvaluate = asList(
                        new QueryToEvaluate() {{
                            query = equal(Car.MANUFACTURER, "Ford");
                            expectedResults = new ExpectedResults() {{
                                size = 3;
                                carIdsAnyOrder = asSet(0, 1, 2);
                                indexUsed = false;
                                mergeCost = 10;
                            }};
                        }}
                );
                indexCombinations = indexCombinations(noIndexes());
            }},
            new MacroScenario() {{
                name = "merge costs with indexes";
                dataSet = SMALL_DATASET;
                collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                queriesToEvaluate = asList(
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
                        indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER))
                );
            }},
            new MacroScenario() {{
                name = "retrieval cost without indexes";
                dataSet = SMALL_DATASET;
                collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                queriesToEvaluate = asList(
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
                queriesToEvaluate = asList(
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
                queriesToEvaluate = asList(
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
                queriesToEvaluate = asList(
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
                queriesToEvaluate = asList(
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
                queriesToEvaluate = asList(
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
                queriesToEvaluate = asList(
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
                queriesToEvaluate = asList(
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
                collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
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
                            // Should order cars without any features first, followed by cars with features
                            // in ascending alphabetical order of feature string...
                            queryOptions = queryOptions(orderBy(ascending(Car.FEATURES), ascending(Car.CAR_ID)));
                            expectedResults = new ExpectedResults() {{
                                size = 10;
                                carIdsInOrder = asList(0, 5, 6, 8, 9, 2, 3, 4, 1, 7);
                            }};
                        }}
                );
                indexCombinations = indexCombinations(noIndexes());
            }},
            new MacroScenario() {{
                name = "index ordering";
                dataSet = SMALL_DATASET;
                collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                queriesToEvaluate = asList(
                        new QueryToEvaluate() {{
                            query = all(Car.class);
                            queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 10;
                                carIdsInOrder = asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = between(Car.CAR_ID, 4, 6);
                            queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 3;
                                carIdsInOrder = asList(4, 5, 6);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = lessThan(Car.CAR_ID, 6);
                            queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 6;
                                carIdsInOrder = asList(0, 1, 2, 3, 4, 5);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = lessThanOrEqualTo(Car.CAR_ID, 6);
                            queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 7;
                                carIdsInOrder = asList(0, 1, 2, 3, 4, 5, 6);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = greaterThan(Car.CAR_ID, 3);
                            queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 6;
                                carIdsInOrder = asList(4, 5, 6, 7, 8, 9);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = greaterThanOrEqualTo(Car.CAR_ID, 3);
                            queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 7;
                                carIdsInOrder = asList(3, 4, 5, 6, 7, 8, 9);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = all(Car.class);
                            queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 10;
                                carIdsInOrder = asList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = between(Car.CAR_ID, 4, 6);
                            queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 3;
                                carIdsInOrder = asList(6, 5, 4);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = lessThan(Car.CAR_ID, 6);
                            queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 6;
                                carIdsInOrder = asList(5, 4 ,3 ,2 ,1, 0);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = lessThanOrEqualTo(Car.CAR_ID, 6);
                            queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 7;
                                carIdsInOrder = asList(6, 5, 4 ,3 ,2 ,1, 0);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = greaterThan(Car.CAR_ID, 3);
                            queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 6;
                                carIdsInOrder = asList(9, 8, 7, 6, 5, 4);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = greaterThanOrEqualTo(Car.CAR_ID, 3);
                            queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 7;
                                carIdsInOrder = asList(9, 8, 7, 6, 5, 4, 3);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = and(greaterThanOrEqualTo(Car.CAR_ID, 3), lessThanOrEqualTo(Car.CAR_ID, 6));
                            queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 4;
                                carIdsInOrder = asList(6, 5, 4, 3);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = or(greaterThanOrEqualTo(Car.CAR_ID, 7), none(Car.class));
                            queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 3;
                                carIdsInOrder = asList(9, 8, 7);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = and(or(greaterThanOrEqualTo(Car.CAR_ID, 7), none(Car.class)), or(all(Car.class), none(Car.class)));
                            queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 3;
                                carIdsInOrder = asList(9, 8, 7);
                            }};
                        }},
                        new QueryToEvaluate() {{
                            query = and(equal(Car.CAR_ID, 8), all(Car.class));
                            queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)), orderingStrategy(INDEX));
                            expectedResults = new ExpectedResults() {{
                                size = 1;
                                carIdsInOrder = asList(8);
                            }};
                        }}
                );
                indexCombinations = indexCombinations(
                        indexCombination(NavigableIndex.onAttribute(Car.CAR_ID)),
                        indexCombination(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(5), Car.CAR_ID))
                );
            }},
            new MacroScenario() {{
                name = "remove objects";
                dataSet = REGULAR_DATASET;
                removeDataSet = SMALL_DATASET;
                collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                queriesToEvaluate = asList(
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
                        indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER))
                );
            }},
            new MacroScenario() {{
                name = "clear objects";
                dataSet = REGULAR_DATASET;
                clearDataSet = true;
                collectionImplementations = classes(ConcurrentIndexedCollection.class, ObjectLockingIndexedCollection.class, TransactionalIndexedCollection.class);
                queriesToEvaluate = asList(
                        new QueryToEvaluate() {{
                            query = equal(Car.MANUFACTURER, "Ford");
                            expectedResults = new ExpectedResults() {{ size = 0; }};
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
                        indexCombination(SuffixTreeIndex.onAttribute(Car.MANUFACTURER))
                );
            }}
        );
    }


    static class MacroScenario {
        String name = "<unnamed>";
        Collection<Car> dataSet = Collections.emptySet();
        Collection<Car> removeDataSet = Collections.emptySet();
        Boolean clearDataSet = false;
        Iterable<? extends QueryToEvaluate> queriesToEvaluate = Collections.emptyList();
        Iterable<Class> collectionImplementations;
        Iterable<Iterable<Index>> indexCombinations;
    }

    public static class Scenario {
        String name = "<unnamed>";
        Collection<Car> dataSet = Collections.emptySet();
        Collection<Car> removeDataSet = Collections.emptySet();
        Boolean clearDataSet = false;
        Query<Car> query = none(Car.class);
        QueryOptions queryOptions = noQueryOptions();
        ExpectedResults expectedResults = null;
        Class collectionImplementation;
        Iterable<Index> indexCombination;

        @Override
        public String toString() {
            return "[" +
                    "name='" + name + '\'' +
                    ", dataSet=<" + dataSet.size() + " items>" +
                    ", removeDataSet=<" + removeDataSet.size() + " items>" +
                    ", clearDataSet=" + clearDataSet +
                    ", query=" + query +
                    ", queryOptions=" + queryOptions +
                    ", expectedResults=" + expectedResults +
                    ", collectionImplementation=" + collectionImplementation.getSimpleName() +
                    ", indexCombination=" + getIndexDescriptions(indexCombination) +
                    ']';
        }
    }

    @DataProvider
    public static List<List<Object>> expandMacroScenarios() {
        List<MacroScenario> macroScenarios = getMacroScenarios();
        List<List<Object>> scenarios = new ArrayList<List<Object>>();
        for (int i = 0; i < macroScenarios.size(); i++) {
            final MacroScenario macroScenario = macroScenarios.get(i);
            try {
                for (final Class currentCollectionImplementation : macroScenario.collectionImplementations) {
                    for (final Iterable<Index> currentIndexCombination : macroScenario.indexCombinations) {
                        for (final QueryToEvaluate currentQueryToEvaluate : macroScenario.queriesToEvaluate) {
                            Scenario scenario = new Scenario() {{
                                name = macroScenario.name;
                                dataSet = macroScenario.dataSet;
                                removeDataSet = macroScenario.removeDataSet;
                                clearDataSet = macroScenario.clearDataSet;
                                query = currentQueryToEvaluate.query;
                                queryOptions = currentQueryToEvaluate.queryOptions;
                                expectedResults = currentQueryToEvaluate.expectedResults;
                                collectionImplementation = currentCollectionImplementation;
                                indexCombination = currentIndexCombination;
                            }};
                            scenarios.add(Collections.<Object>singletonList(scenario));
                        }
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException("Configuration issue for MacroScenario " + i + " in the list: " + macroScenario.name, e);
            }
        }
        return scenarios;
    }

    @Test
    @UseDataProvider(value = "expandMacroScenarios")
    @SuppressWarnings("unchecked")
    public void testScenario(Scenario scenario) {
        if (!IndexedCollection.class.isAssignableFrom(scenario.collectionImplementation)) {
            throw new IllegalStateException("Invalid IndexedCollection class: " + scenario.collectionImplementation);
        }
        IndexedCollection<Car> indexedCollection;
        try {
            if (TransactionalIndexedCollection.class.isAssignableFrom(scenario.collectionImplementation)) {
                indexedCollection = (IndexedCollection<Car>) scenario.collectionImplementation.getConstructor(Class.class).newInstance(Car.class);
            }
            else {
                indexedCollection = (IndexedCollection<Car>) scenario.collectionImplementation.newInstance();
            }
        }
        catch (Exception e) {
            throw new IllegalStateException("Could not instantiate IndexedCollection: " + scenario.collectionImplementation, e);
        }
        for (Index<Car> index : scenario.indexCombination) {
            try {
                indexedCollection.addIndex(index);
            }
            catch (Exception e) {
                throw new IllegalStateException("Could not add " + getIndexDescription(index), e);
            }
        }
        indexedCollection.addAll(scenario.dataSet);

        indexedCollection.removeAll(scenario.removeDataSet);

        if (scenario.clearDataSet) {
            indexedCollection.clear();
        }

        evaluateQuery(indexedCollection, scenario.query, scenario.queryOptions, scenario.expectedResults);
    }

    static void evaluateQuery(IndexedCollection<Car> indexedCollection, Query<Car> query, QueryOptions queryOptions, ExpectedResults expectedResults) {
        if (expectedResults == null) {
            throw new IllegalStateException("No expectedResults configured for query: " + query);
        }
        try {
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
                    for (Integer carId : expectedResults.containsCarIds) {
                        assertTrue("containsCarIds mismatch, results do not contain carId " + carId + " for query: " + query, results.contains(CarFactory.createCar(carId)));
                    }
                }
                if (expectedResults.doesNotContainCarIds != null) {
                    for (Integer carId : expectedResults.doesNotContainCarIds) {
                        assertFalse("doesNotContainCarIds mismatch, results contain carId " + carId + " for query: " + query, results.contains(CarFactory.createCar(carId)));
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
        Query<Car> query = none(Car.class);
        QueryOptions queryOptions = noQueryOptions();
        ExpectedResults expectedResults = null;
    }

    static class ExpectedResults {
        Integer size;
        Integer retrievalCost;
        Integer mergeCost;
        Boolean indexUsed;
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
        if (index instanceof CompoundIndex) {
            description += ".onAttribute(<CompoundAttribute>)";
        }
        else if (index instanceof AttributeIndex) {
            Attribute attribute = ((AttributeIndex) index).getAttribute();
            description += ".onAttribute(" + attribute.getObjectType().getSimpleName() + "." + attribute.getAttributeName() + ")";
        }
        if (index instanceof AbstractMapBasedAttributeIndex && ((AbstractMapBasedAttributeIndex) index).isIndexQuantized()) {
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

    static Set<Integer> asSet(Integer... integers) {
        return new HashSet<Integer>(asList(integers));
    }

    static SortedSet<Integer> integersBetween(int lower, int upper) {
        SortedSet<Integer> treeSet = new TreeSet<Integer>();
        for (int i = lower; i <= upper; i++) {
            treeSet.add(i);
        }
        return treeSet;
    }
}
