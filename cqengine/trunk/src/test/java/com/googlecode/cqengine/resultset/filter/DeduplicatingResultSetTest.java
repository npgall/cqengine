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
package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.IndexedCollectionFunctionalTest;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;


public class DeduplicatingResultSetTest {

    @Test
    public void testDeduplicatingResultSet() {
        Collection<Car> cars = CarFactory.createCollectionOfCars(10);

        DeduplicatingResultSet<Car, String> deduplicatingResultSet = new DeduplicatingResultSet<Car, String>(
                Car.MANUFACTURER,
                new StoredSetBasedResultSet<Car>(new LinkedHashSet<Car>(cars)),
                QueryFactory.all(Car.class), QueryFactory.noQueryOptions()
        );
        List<Integer> carIdsReturned = new ArrayList<Integer>();
        IndexedCollectionFunctionalTest.extractCarIds(deduplicatingResultSet, carIdsReturned);

        // Should return the first distinct Manufacturer...
        Assert.assertEquals(4, deduplicatingResultSet.size());
        Assert.assertEquals(0, deduplicatingResultSet.getRetrievalCost());
        Assert.assertEquals(10, deduplicatingResultSet.getMergeCost());
        Assert.assertEquals(4, carIdsReturned.size());
        Assert.assertEquals(0, carIdsReturned.get(0).intValue());
        Assert.assertEquals(3, carIdsReturned.get(1).intValue());
        Assert.assertEquals(6, carIdsReturned.get(2).intValue());
        Assert.assertEquals(9, carIdsReturned.get(3).intValue());

        Assert.assertTrue(deduplicatingResultSet.contains(CarFactory.createCar(0)));
        Assert.assertTrue(deduplicatingResultSet.contains(CarFactory.createCar(3)));
        Assert.assertTrue(deduplicatingResultSet.contains(CarFactory.createCar(6)));
        Assert.assertTrue(deduplicatingResultSet.contains(CarFactory.createCar(9)));
        Assert.assertFalse(deduplicatingResultSet.contains(CarFactory.createCar(1)));

        deduplicatingResultSet.close(); // No op.
    }
}