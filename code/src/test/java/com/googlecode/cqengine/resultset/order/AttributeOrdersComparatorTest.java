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
package com.googlecode.cqengine.resultset.order;

import com.googlecode.cqengine.query.option.OrderByOption;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;

/**
 * @author Roberto Socrates
 * @author Niall Gallagher
 */
public class AttributeOrdersComparatorTest {

    @Test
    public void testSortAscending() {
        List<Car> cars = Arrays.asList(
                new Car(0, "Ford",  "Taurus", Car.Color.BLACK, 4, 7000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(1, "Ford",  "Focus",  Car.Color.BLUE,  5, 5000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(2, "BMW",   "M6",     Car.Color.RED,   2, 9000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(3, "Honda", "Civic",  Car.Color.WHITE, 5, 6000.00, Collections.<String>emptyList(), Collections.emptyList())
        );

        OrderByOption<Car> ordering = orderBy(ascending(Car.MANUFACTURER), ascending(Car.PRICE));
        Collections.sort(cars, new AttributeOrdersComparator<Car>(ordering.getAttributeOrders(), noQueryOptions()));

        List<Car> expected = Arrays.asList(
            new Car(2, "BMW",   "M6",     Car.Color.RED,   2, 9000.00, Collections.<String>emptyList(), Collections.emptyList()),
            new Car(1, "Ford",  "Focus",  Car.Color.BLUE,  5, 5000.00, Collections.<String>emptyList(), Collections.emptyList()),
            new Car(0, "Ford",  "Taurus", Car.Color.BLACK, 4, 7000.00, Collections.<String>emptyList(), Collections.emptyList()),
            new Car(3, "Honda", "Civic",  Car.Color.WHITE, 5, 6000.00, Collections.<String>emptyList(), Collections.emptyList())
        );

        Assert.assertEquals(expected, cars);
    }

    @Test
    public void testSortDescending() {
        List<Car> cars = Arrays.asList(
                new Car(0, "Ford",  "Taurus", Car.Color.BLACK, 4, 7000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(1, "Ford",  "Focus",  Car.Color.BLUE,  5, 5000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(2, "BMW",   "M6",     Car.Color.RED,   2, 9000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(3, "Honda", "Civic",  Car.Color.WHITE, 5, 6000.00, Collections.<String>emptyList(), Collections.emptyList())
        );

        OrderByOption<Car> ordering = orderBy(descending(Car.MANUFACTURER), descending(Car.PRICE));
        Collections.sort(cars, new AttributeOrdersComparator<Car>(ordering.getAttributeOrders(), noQueryOptions()));

        List<Car> expected = Arrays.asList(
                new Car(3, "Honda", "Civic",  Car.Color.WHITE, 5, 6000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(0, "Ford",  "Taurus", Car.Color.BLACK, 4, 7000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(1, "Ford",  "Focus",  Car.Color.BLUE,  5, 5000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(2, "BMW",   "M6",     Car.Color.RED,   2, 9000.00, Collections.<String>emptyList(), Collections.emptyList())
        );

        Assert.assertEquals(expected, cars);
    }

    @Test
    public void testSortMixed() {
        List<Car> cars = Arrays.asList(
                new Car(0, "Ford",  "Taurus", Car.Color.BLACK, 4, 2000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(1, "Ford",  "Taurus", Car.Color.BLACK, 4, 1000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(3, "Honda", "Civic",  Car.Color.BLACK, 4, 4000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(3, "Honda", "Civic",  Car.Color.BLACK, 4, 3000.00, Collections.<String>emptyList(), Collections.emptyList())
        );

        OrderByOption<Car> ordering = orderBy(descending(Car.MANUFACTURER), ascending(Car.PRICE));
        Collections.sort(cars, new AttributeOrdersComparator<Car>(ordering.getAttributeOrders(), noQueryOptions()));

        List<Car> expected = Arrays.asList(
                new Car(3, "Honda", "Civic",  Car.Color.BLACK, 4, 3000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(3, "Honda", "Civic",  Car.Color.BLACK, 4, 4000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(1, "Ford",  "Taurus", Car.Color.BLACK, 4, 1000.00, Collections.<String>emptyList(), Collections.emptyList()),
                new Car(0, "Ford",  "Taurus", Car.Color.BLACK, 4, 2000.00, Collections.<String>emptyList(), Collections.emptyList())
        );

        Assert.assertEquals(expected, cars);
    }
}
