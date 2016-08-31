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
package com.googlecode.cqengine.index.sqlite;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PartialSQLiteIndex}.
 *
 * @author niall.gallagher
 */
public class PartialSQLiteIndexTest {

    public static final SimpleAttribute<Car, Integer> OBJECT_TO_ID = Car.CAR_ID;

    public static final SimpleAttribute<Integer, Car> ID_TO_OBJECT = new SimpleAttribute<Integer, Car>("carFromId") {
        public Car getValue(Integer carId, QueryOptions queryOptions) { return CarFactory.createCar(carId); }
    };

    @Rule
    public TemporaryDatabase.TemporaryInMemoryDatabase temporaryInMemoryDatabase = new TemporaryDatabase.TemporaryInMemoryDatabase();

    @Test
    public void testPartialSQLiteIndex() {
        IndexedCollection<Car> indexedCollection = new ConcurrentIndexedCollection<Car>();
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.put(ConnectionManager.class, connectionManager);
        PartialSQLiteIndex<String, Car, Integer> index = PartialSQLiteIndex.onAttributeWithFilterQuery(Car.MANUFACTURER, OBJECT_TO_ID, ID_TO_OBJECT, between(Car.CAR_ID, 2, 4));
        indexedCollection.addIndex(index, queryOptions);
        indexedCollection.update(Collections.<Car>emptySet(), CarFactory.createCollectionOfCars(10), queryOptions);

        assertEquals(75,         indexedCollection.retrieve(and(equal(Car.MANUFACTURER, "Ford"), between(Car.CAR_ID, 2, 4)), queryOptions).getRetrievalCost());
        assertEquals(2147483647, indexedCollection.retrieve(and(equal(Car.MANUFACTURER, "Ford"), between(Car.CAR_ID, 2, 5)), queryOptions).getRetrievalCost());
    }
}