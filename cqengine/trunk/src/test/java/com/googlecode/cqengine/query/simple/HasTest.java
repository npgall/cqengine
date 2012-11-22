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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import org.junit.Assert;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.*;
import java.util.Arrays;

/**
 * @author Niall Gallagher
 */
public class HasTest {

    @Test
    public void testExists() {
        // Create an indexed collection (note: could alternatively use CQEngine.copyFrom() existing collection)...
        IndexedCollection<Car> cars = CQEngine.newInstance();

        Attribute<Car, String> NAME = new SimpleNullableAttribute<Car, String>("name") {
            public String getValue(Car car) { return car.name; }
        };
        // Add some indexes...
        cars.addIndex(StandingQueryIndex.onQuery(has(NAME)));
        cars.addIndex(StandingQueryIndex.onQuery(not(has(NAME))));

        // Add some objects to the collection...
        cars.add(new Car(1, "ford focus", "great condition, low mileage", Arrays.asList("spare tyre", "sunroof")));
        cars.add(new Car(2, null, "dirty and unreliable, flat tyre", Arrays.asList("spare tyre", "radio")));
        cars.add(new Car(3, "honda civic", "has a flat tyre and high mileage", Arrays.asList("radio")));

        Assert.assertEquals(cars.retrieve(has(NAME)).size(), 2);
        Assert.assertEquals(cars.retrieve(not(has(NAME))).size(), 1);
    }
}
