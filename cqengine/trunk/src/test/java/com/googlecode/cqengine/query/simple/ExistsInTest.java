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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.examples.join.Garage;
import com.googlecode.cqengine.query.Query;
import org.junit.Assert;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.existsIn;

public class ExistsInTest {

    @Test
    public void testToString1() throws Exception {
        IndexedCollection<Garage> garages = new ConcurrentIndexedCollection<Garage>();

        Query<Car> existsIn = existsIn(garages, Car.NAME, Garage.BRANDS_SERVICED);
        Assert.assertEquals("existsIn(IndexedCollection<Garage>, \"name\", \"brandsServiced\")", existsIn.toString());
    }

    @Test
    public void testToString2() throws Exception {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        IndexedCollection<Garage> garages = new ConcurrentIndexedCollection<Garage>();

        Query<Car> existsIn = existsIn(garages, Car.NAME, Garage.BRANDS_SERVICED, equal(Garage.LOCATION, "Dublin"));
        Assert.assertEquals("existsIn(IndexedCollection<Garage>, \"name\", \"brandsServiced\", equal(\"location\", \"Dublin\"))", existsIn.toString());
    }
}