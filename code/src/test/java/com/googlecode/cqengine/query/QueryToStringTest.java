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
package com.googlecode.cqengine.query;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.join.Garage;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Assert;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author ngallagher
 * @since 2013-08-27 12:09
 */
public class QueryToStringTest {

    @Test
    public void testQueryToString1() {
        Query<Car> query = and(
                equal(Car.MANUFACTURER, "Toyota"),
                equal(Car.COLOR, Car.Color.BLUE),
                equal(Car.DOORS, 3)
        );
        Assert.assertEquals(
                "and(" +
                    "equal(\"manufacturer\", \"Toyota\"), " +
                    "equal(\"color\", BLUE), " +
                    "equal(\"doors\", 3)" +
                ")",
                query.toString()
        );
    }

    @Test
    public void testQueryToString2() {
        IndexedCollection<Garage> garages = new ConcurrentIndexedCollection<Garage>();
        Query<Car> query = and(
                in(Car.DOORS, 2, 4),
                existsIn(garages,
                        Car.MANUFACTURER,
                        Garage.BRANDS_SERVICED,
                        equal(Garage.LOCATION, "Dublin")
                )
        );
        // Note: QueryFactory expands 'in' queries to an 'or' of multiple 'equals' queries (logically equivalent)...
        Assert.assertEquals(
                "and(" +
                    "in(\"doors\", [2, 4]), " +
                    "existsIn(IndexedCollection<Garage>, " +
                        "\"manufacturer\", " +
                        "\"brandsServiced\", " +
                        "equal(\"location\", \"Dublin\")" +
                    ")" +
                ")",
                query.toString()
        );
    }
}
