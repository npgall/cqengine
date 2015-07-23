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
package com.googlecode.cqengine.index.navigable;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.support.KeyStatistics;
import com.googlecode.cqengine.index.support.KeyStatisticsIndex;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsIndex;
import com.googlecode.cqengine.quantizer.IntegerQuantizer;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.testutil.TestUtil.setOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class NavigableIndexTest {

    @Test
    public void testGetDistinctKeysAndCounts() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        SortedKeyStatisticsIndex<String, Car> MODEL_INDEX = NavigableIndex.onAttribute(Car.MODEL);
        collection.addIndex(MODEL_INDEX);

        collection.addAll(CarFactory.createCollectionOfCars(20));

        Set<String> distinctModels = setOf(MODEL_INDEX.getDistinctKeys(noQueryOptions()));
        assertEquals(asList("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus"), new ArrayList<String>(distinctModels));
        for (String model : distinctModels) {
            assertEquals(Integer.valueOf(2), MODEL_INDEX.getCountForKey(model, noQueryOptions()));
        }

        Set<String> distinctModelsDescending = setOf(MODEL_INDEX.getDistinctKeysDescending(noQueryOptions()));
        assertEquals(asList("Taurus", "Prius", "M6", "Insight", "Hilux", "Fusion", "Focus", "Civic", "Avensis", "Accord"), new ArrayList<String>(distinctModelsDescending));
    }

    @Test
    public void testGetCountOfDistinctKeys(){
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        KeyStatisticsIndex<String, Car> MANUFACTURER_INDEX = NavigableIndex.onAttribute(Car.MANUFACTURER);
        collection.addIndex(MANUFACTURER_INDEX);

        collection.addAll(CarFactory.createCollectionOfCars(20));

        Assert.assertEquals(Integer.valueOf(4), MANUFACTURER_INDEX.getCountOfDistinctKeys(noQueryOptions()));
    }

    @Test
    public void testGetStatisticsForDistinctKeys(){
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        KeyStatisticsIndex<String, Car> MANUFACTURER_INDEX = NavigableIndex.onAttribute(Car.MANUFACTURER);
        collection.addIndex(MANUFACTURER_INDEX);

        collection.addAll(CarFactory.createCollectionOfCars(20));

        Set<KeyStatistics<String>> keyStatistics = setOf(MANUFACTURER_INDEX.getStatisticsForDistinctKeys(noQueryOptions()));
        Assert.assertEquals(setOf(
                        new KeyStatistics<String>("Ford", 6),
                        new KeyStatistics<String>("Honda", 6),
                        new KeyStatistics<String>("Toyota", 6),
                        new KeyStatistics<String>("BMW", 2)

                ),
                keyStatistics);
    }

    @Test
    public void testGetStatisticsForDistinctKeysDescending(){
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        SortedKeyStatisticsIndex<String, Car> MANUFACTURER_INDEX = NavigableIndex.onAttribute(Car.MANUFACTURER);
        collection.addIndex(MANUFACTURER_INDEX);

        collection.addAll(CarFactory.createCollectionOfCars(20));

        Set<KeyStatistics<String>> keyStatistics = setOf(MANUFACTURER_INDEX.getStatisticsForDistinctKeysDescending(noQueryOptions()));
        Assert.assertEquals(setOf(
                        new KeyStatistics<String>("Toyota", 6),
                        new KeyStatistics<String>("Honda", 6),
                        new KeyStatistics<String>("Ford", 6),
                        new KeyStatistics<String>("BMW", 2)

                ),
                keyStatistics);
    }

    @Test
    public void testIndexQuantization_SpanningTwoBucketsMidRange() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addIndex(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(10), Car.CAR_ID));
        collection.addAll(CarFactory.createCollectionOfCars(100));

        // Merge cost should be 20 because this query spans 2 buckets (each containing 10 objects)...
        assertEquals(20, collection.retrieve(between(Car.CAR_ID, 47, 53)).getMergeCost());

        // 7 objects match the query (between is inclusive)...
        assertEquals(7, collection.retrieve(between(Car.CAR_ID, 47, 53)).size());

        // The matching objects are...
        List<Integer> carIdsFound = retrieveCarIds(collection, between(Car.CAR_ID, 47, 53));
        assertEquals(asList(47, 48, 49, 50, 51, 52, 53), carIdsFound);
    }

    @Test
    public void testIndexQuantization_FirstBucket() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addIndex(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(10), Car.CAR_ID));
        collection.addAll(CarFactory.createCollectionOfCars(100));

        // Merge cost should be 10, because objects matching this query are in a single bucket...
        assertEquals(10, collection.retrieve(between(Car.CAR_ID, 2, 4)).getMergeCost());

        // 3 objects match the query...
        assertEquals(3, collection.retrieve(between(Car.CAR_ID, 2, 4)).size());

        List<Integer> carIdsFound = retrieveCarIds(collection, between(Car.CAR_ID, 2, 4));
        assertEquals(asList(2, 3, 4), carIdsFound);
    }

    @Test
    public void testIndexQuantization_LastBucket() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addIndex(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(10), Car.CAR_ID));
        collection.addAll(CarFactory.createCollectionOfCars(100));

        // Merge cost should be 10, because objects matching this query are in a single bucket...
        assertEquals(10, collection.retrieve(between(Car.CAR_ID, 96, 98)).getMergeCost());

        // 3 objects match the query...
        assertEquals(3, collection.retrieve(between(Car.CAR_ID, 96, 98)).size());

        List<Integer> carIdsFound = retrieveCarIds(collection, between(Car.CAR_ID, 96, 98));
        assertEquals(asList(96, 97, 98), carIdsFound);
    }


    @Test
    public void testIndexQuantization_LessThan() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addIndex(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(10), Car.CAR_ID));
        collection.addAll(CarFactory.createCollectionOfCars(100));

        assertEquals(5, collection.retrieve(lessThan(Car.CAR_ID, 5)).size());
        assertEquals(15, collection.retrieve(lessThan(Car.CAR_ID, 15)).size());
        assertEquals(6, collection.retrieve(lessThanOrEqualTo(Car.CAR_ID, 5)).size());
        assertEquals(16, collection.retrieve(lessThanOrEqualTo(Car.CAR_ID, 15)).size());
    }

    @Test
    public void testIndexQuantization_GreaterThan() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addIndex(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(10), Car.CAR_ID));
        collection.addAll(CarFactory.createCollectionOfCars(100));

        assertEquals(4, collection.retrieve(greaterThan(Car.CAR_ID, 95)).size());
        assertEquals(14, collection.retrieve(greaterThan(Car.CAR_ID, 85)).size());
        assertEquals(5, collection.retrieve(greaterThanOrEqualTo(Car.CAR_ID, 95)).size());
        assertEquals(15, collection.retrieve(greaterThanOrEqualTo(Car.CAR_ID, 85)).size());
    }

    @Test
    public void testIndexQuantization_Between() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addIndex(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(10), Car.CAR_ID));
        collection.addAll(CarFactory.createCollectionOfCars(100));

        Query<Car> query = between(Car.CAR_ID, 88, 92);
        assertEquals(5, collection.retrieve(query).size());
        assertEquals(asList(88, 89, 90, 91, 92), retrieveCarIds(collection, query));

        query = between(Car.CAR_ID, 88, true, 92, true);
        assertEquals(5, collection.retrieve(query).size());
        assertEquals(asList(88, 89, 90, 91, 92), retrieveCarIds(collection, query));

        query = between(Car.CAR_ID, 88, false, 92, true);
        assertEquals(4, collection.retrieve(query).size());
        assertEquals(asList(89, 90, 91, 92), retrieveCarIds(collection, query));

        query = between(Car.CAR_ID, 88, true, 92, false);
        assertEquals(4, collection.retrieve(query).size());
        assertEquals(asList(88, 89, 90, 91), retrieveCarIds(collection, query));

        query = between(Car.CAR_ID, 88, false, 92, false);
        assertEquals(3, collection.retrieve(query).size());
        assertEquals(asList(89, 90, 91), retrieveCarIds(collection, query));
    }

    @Test
    public void testIndexQuantization_ComplexQuery() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addIndex(NavigableIndex.withQuantizerOnAttribute(IntegerQuantizer.withCompressionFactor(10), Car.CAR_ID));
        collection.addAll(CarFactory.createCollectionOfCars(100));
        Query<Car> query = and(between(Car.CAR_ID, 96, 98), greaterThan(Car.CAR_ID, 95));

        // Merge cost should be 10, because objects matching this query are in a single bucket...
        assertEquals(10, collection.retrieve(query).getMergeCost());

        // 3 objects match the query...
        assertEquals(3, collection.retrieve(query).size());

        List<Integer> carIdsFound = retrieveCarIds(collection, query);
        assertEquals(asList(96, 97, 98), carIdsFound);
    }

    static List<Integer> retrieveCarIds(IndexedCollection<Car> collection, Query<Car> query) {
        ResultSet<Car> cars = collection.retrieve(query, queryOptions(orderBy(ascending(Car.CAR_ID))));
        List<Integer> carIds = new ArrayList<Integer>();
        for (Car car : cars) {
            carIds.add(car.getCarId());
        }
        return carIds;
    }
}
