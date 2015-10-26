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
package com.googlecode.cqengine.index.unique;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.googlecode.cqengine.query.QueryFactory.*;


/**
 * @author Niall Gallagher
 */
public class UniqueIndexTest {

    @Test
    public void testUniqueIndex() {
        IndexedCollection<Car> cars = CQEngine.newInstance();

        // Add some indexes...
        cars.addIndex(UniqueIndex.onAttribute(Car.CAR_ID));
        cars.addIndex(HashIndex.onAttribute(Car.CAR_ID));

        // Add some objects to the collection...
        cars.add(new Car(1, "ford focus", "great condition, low mileage", Arrays.asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "ford taurus", "dirty and unreliable, flat tyre", Arrays.asList("spare tyre", "radio")));
        cars.add(new Car(3, "honda civic", "has a flat tyre and high mileage", Arrays.asList("radio")));

        Query<Car> query = equal(Car.CAR_ID, 2);
        ResultSet<Car> rs = cars.retrieve(query);
        Assert.assertEquals("should prefer unique index over hash index", UniqueIndex.INDEX_RETRIEVAL_COST, rs.getRetrievalCost());

        Assert.assertEquals("should retrieve car 2", 2, rs.uniqueResult().carId);
    }

    @Test(expected = UniqueIndex.UniqueConstraintViolatedException.class)
    public void testDuplicateObjectDetection_SimpleAttribute() {
        IndexedCollection<Car> cars = CQEngine.newInstance();

        // Add some indexes...
        cars.addIndex(UniqueIndex.onAttribute(Car.CAR_ID));

        // Add some objects to the collection...
        cars.add(new Car(1, "ford focus", "great condition, low mileage", Arrays.asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "ford taurus", "dirty and unreliable, flat tyre", Arrays.asList("spare tyre", "radio")));
        cars.add(new Car(3, "honda civic", "has a flat tyre and high mileage", Arrays.asList("radio")));

        cars.add(new Car(2, "some other car", "foo", Arrays.asList("bar")));
    }

    @Test(expected = UniqueIndex.UniqueConstraintViolatedException.class)
    public void testDuplicateObjectDetection_MultiValueAttribute() {
        IndexedCollection<Car> cars = CQEngine.newInstance();

        // Add some indexes...
        cars.addIndex(UniqueIndex.onAttribute(Car.FEATURES));

        // Add some objects to the collection...
        cars.add(new Car(1, "ford focus", "foo", Arrays.asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "ford taurus", "bar", Arrays.asList("radio", "cd player")));

        // Try to add another car which has a cd player, when one car already has a cd player...
        cars.add(new Car(3, "honda civic", "baz", Arrays.asList("cd player", "bluetooth")));
    }
}
