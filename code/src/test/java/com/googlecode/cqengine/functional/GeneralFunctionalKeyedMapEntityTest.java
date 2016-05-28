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
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.entity.KeyedMapEntity;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.radixinverted.InvertedRadixTreeIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.testutil.TestUtil.setOf;
import static com.googlecode.cqengine.testutil.TestUtil.valuesOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Validates general functionality using MapEntity as collection element - indexes, query engine, ordering results.
 *
 * @author Niall Gallagher
 */
public class GeneralFunctionalKeyedMapEntityTest {

    private static final Attribute<KeyedMapEntity, String> MODEL_ATTR = keyedMapEntityAttribute("MODEL", String.class);
    private static final Attribute<KeyedMapEntity, Integer> DOORS_ATTR = keyedMapEntityAttribute("DOORS", Integer.class);
    private static final Attribute<KeyedMapEntity, Car.Color> COLOR_ATTR = keyedMapEntityAttribute("COLOR",Car.Color.class);
    private static final Attribute<KeyedMapEntity, String> MANUFACTURER_ATTR = keyedMapEntityAttribute("MANUFACTURER", String.class);
    private static final Attribute<KeyedMapEntity, Double> PRICE_ATTR = keyedMapEntityAttribute("PRICE", Double.class);
    private static final Attribute<KeyedMapEntity, Integer> CAR_ID_ATTR = keyedMapEntityAttribute("CAR_ID", Integer.class);

    @Test
    public void testGeneralFunctionality() {
        IndexedCollection<KeyedMapEntity> cars = new ConcurrentIndexedCollection<KeyedMapEntity>();

        cars.addIndex(HashIndex.onAttribute(COLOR_ATTR));
        cars.addIndex(NavigableIndex.onAttribute(DOORS_ATTR));
        cars.addIndex(RadixTreeIndex.onAttribute(MODEL_ATTR));
        cars.addIndex(ReversedRadixTreeIndex.onAttribute(MODEL_ATTR));
        cars.addIndex(InvertedRadixTreeIndex.onAttribute(MODEL_ATTR));
        cars.addIndex(SuffixTreeIndex.onAttribute(MODEL_ATTR));

        cars.add(buildNewCar(1, "Ford",   "Focus",  Car.Color.BLUE,  5, 9000.50, Collections.<String>emptyList()));
        cars.add(buildNewCar(2, "Ford",   "Fiesta", Car.Color.BLUE,  2, 5000.00, Collections.<String>emptyList()));
        cars.add(buildNewCar(3, "Ford",   "F-150",  Car.Color.RED,   2, 9500.00, Collections.<String>emptyList()));
        cars.add(buildNewCar(4, "Honda",  "Civic",  Car.Color.RED,   5, 5000.00, Collections.<String>emptyList()));
        cars.add(buildNewCar(5, "Toyota", "Prius",  Car.Color.BLACK, 3, 9700.00, Collections.<String>emptyList()));

        // Ford cars...
        assertThat(carIdsIn(cars.retrieve(equal(MANUFACTURER_ATTR, "Ford"))), is(setOf(1, 2, 3)));

        // 3-door cars...
        assertThat(carIdsIn(cars.retrieve(equal(DOORS_ATTR, 3))), is(setOf(5)));

        // 2 or 3-door cars...
        assertThat(carIdsIn(cars.retrieve(between(DOORS_ATTR, 2, 3))), is(setOf(2, 3, 5)));

        // 2 or 5-door cars...
        assertThat(carIdsIn(cars.retrieve(in(DOORS_ATTR, 2, 5))), is(setOf(1, 2, 3, 4)));

        // Blue Ford cars...
        assertThat(carIdsIn(cars.retrieve(and(equal(COLOR_ATTR, Car.Color.BLUE),
                equal(MANUFACTURER_ATTR, "Ford")))), is(setOf(1, 2)));

        // NOT 3-door cars...
        assertThat(carIdsIn(cars.retrieve(not(equal(DOORS_ATTR, 3)))),
                is(setOf(1, 2, 3, 4)));

        // Cars which have 5 doors and which are not red...
        assertThat(carIdsIn(cars.retrieve(and(equal(DOORS_ATTR, 5), not(equal(COLOR_ATTR, Car.Color.RED))))), is(setOf(1)));

        // Cars whose model starts with 'F'...
        assertThat(carIdsIn(cars.retrieve(startsWith(MODEL_ATTR, "F"))), is(setOf(1, 2, 3)));

        // Cars whose model ends with 's'...
        assertThat(carIdsIn(cars.retrieve(endsWith(MODEL_ATTR, "s"))), is(setOf(1, 5)));

        // Cars whose model contains 'i'...
        assertThat(carIdsIn(cars.retrieve(contains(MODEL_ATTR, "i"))), is(setOf(2, 4, 5)));

        // Cars whose model is contained in 'Banana, Focus, Civic, Foobar'...
        assertThat(carIdsIn(cars.retrieve(isContainedIn(MODEL_ATTR, "Banana, Focus, Civic, Foobar"))), is(setOf(1, 4)));

        // NOT 3-door cars, sorted by doors ascending...
        assertThat(
                carIdsIn(cars.retrieve(not(equal(DOORS_ATTR, 3)), queryOptions(orderBy(ascending(DOORS_ATTR), ascending(MODEL_ATTR))))).toString(),
                is(equalTo(setOf(3, 2, 4, 1).toString()))
        );

        // NOT 3-door cars, sorted by doors ascending then price descending...
        assertThat(
                carIdsIn(
                        cars.retrieve(
                                not(equal(DOORS_ATTR, 3)),
                                queryOptions(
                                        orderBy(ascending(DOORS_ATTR),
                                                descending(PRICE_ATTR))
                                )
                        )
                ).toString(),
                is(equalTo(setOf(3, 2, 1, 4).toString()))
        );
    }

    static Set<Integer> carIdsIn(ResultSet<KeyedMapEntity> resultSet) {
        return valuesOf(CAR_ID_ATTR, resultSet);
    }

    KeyedMapEntity buildNewCar(int carId, String manufacturer, String model, Car.Color color, int doors, double price, List<String> features) {
        Map map = new HashMap();
        map.put("CAR_ID", carId);
        map.put("MANUFACTURER", manufacturer);
        map.put("MODEL", model);
        map.put("COLOR", color);
        map.put("DOORS", doors);
        map.put("PRICE", price);
        map.put("FEATURES", features);
        return keyedMapEntity(map, "CAR_ID");
    }


}
