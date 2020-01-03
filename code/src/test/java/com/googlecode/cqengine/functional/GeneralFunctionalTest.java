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
package com.googlecode.cqengine.functional;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.radixinverted.InvertedRadixTreeIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.testutil.TestUtil.setOf;
import static com.googlecode.cqengine.testutil.TestUtil.valuesOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Validates general functionality - indexes, query engine, ordering results.
 *
 * @author Niall Gallagher
 */
public class GeneralFunctionalTest {

    @Test
    public void testGeneralFunctionality() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();

        cars.addIndex(HashIndex.onAttribute(Car.MODEL));
        cars.addIndex(HashIndex.onAttribute(Car.COLOR));
        cars.addIndex(NavigableIndex.onAttribute(Car.DOORS));
        cars.addIndex(RadixTreeIndex.onAttribute(Car.MODEL));
        cars.addIndex(ReversedRadixTreeIndex.onAttribute(Car.MODEL));
        cars.addIndex(InvertedRadixTreeIndex.onAttribute(Car.MODEL));
        cars.addIndex(SuffixTreeIndex.onAttribute(Car.MODEL));

        cars.add(new Car(1, "Ford",   "Focus",  Car.Color.BLUE,  5, 9000.50, Collections.<String>emptyList(), Collections.emptyList()));
        cars.add(new Car(2, "Ford",   "Fiesta", Car.Color.BLUE,  2, 5000.00, Collections.<String>emptyList(), Collections.emptyList()));
        cars.add(new Car(3, "Ford",   "F-150",  Car.Color.RED,   2, 9500.00, Collections.<String>emptyList(), Collections.emptyList()));
        cars.add(new Car(4, "Honda",  "Civic",  Car.Color.RED,   5, 5000.00, Collections.<String>emptyList(), Collections.emptyList()));
        cars.add(new Car(5, "Toyota", "Prius",  Car.Color.BLACK, 3, 9700.00, Collections.<String>emptyList(), Collections.emptyList()));

        // Ford cars...
        assertThat(carIdsIn(cars.retrieve(equal(Car.MANUFACTURER, "Ford"))), is(setOf(1, 2, 3)));

        // 3-door cars...
        assertThat(carIdsIn(cars.retrieve(equal(Car.DOORS, 3))), is(setOf(5)));

        // 2 or 3-door cars...
        assertThat(carIdsIn(cars.retrieve(between(Car.DOORS, 2, 3))), is(setOf(2, 3, 5)));

        // 2 or 5-door cars...
        assertThat(carIdsIn(cars.retrieve(in(Car.DOORS, 2, 5))), is(setOf(1, 2, 3, 4)));

        // Blue Ford cars...
        assertThat(carIdsIn(cars.retrieve(and(equal(Car.COLOR, Car.Color.BLUE), equal(Car.MANUFACTURER, "Ford")))), is(setOf(1, 2)));

        // NOT 3-door cars...
        assertThat(carIdsIn(cars.retrieve(not(equal(Car.DOORS, 3)))), is(setOf(1, 2, 3, 4)));

        // Cars which have 5 doors and which are not red...
        assertThat(carIdsIn(cars.retrieve(and(equal(Car.DOORS, 5), not(equal(Car.COLOR, Car.Color.RED))))), is(setOf(1)));

        // Cars whose model starts with 'F'...
        assertThat(carIdsIn(cars.retrieve(startsWith(Car.MODEL, "F"))), is(setOf(1, 2, 3)));

        // Cars whose model ends with 's'...
        assertThat(carIdsIn(cars.retrieve(endsWith(Car.MODEL, "s"))), is(setOf(1, 5)));

        // Cars whose model contains 'i'...
        assertThat(carIdsIn(cars.retrieve(contains(Car.MODEL, "i"))), is(setOf(2, 4, 5)));

        // Cars whose model is contained in 'Banana, Focus, Civic, Foobar'...
        assertThat(carIdsIn(cars.retrieve(isContainedIn(Car.MODEL, "Banana, Focus, Civic, Foobar"))), is(setOf(1, 4)));

        // NOT 3-door cars, sorted by doors ascending...
        assertThat(
                carIdsIn(cars.retrieve(not(equal(Car.DOORS, 3)), queryOptions(orderBy(ascending(Car.DOORS))))).toString(),
                is(equalTo(setOf(2, 3, 1, 4).toString()))
        );

        // NOT 3-door cars, sorted by doors ascending then price descending...
        assertThat(
                carIdsIn(
                        cars.retrieve(
                                not(equal(Car.DOORS, 3)),
                                queryOptions(
                                        orderBy(ascending(Car.DOORS),
                                                descending(Car.PRICE))
                                )
                        )
                ).toString(),
                is(equalTo(setOf(3, 2, 1, 4).toString()))
        );
    }

    static Set<Integer> carIdsIn(ResultSet<Car> resultSet) {
        return valuesOf(Car.CAR_ID, resultSet);
    }

}
