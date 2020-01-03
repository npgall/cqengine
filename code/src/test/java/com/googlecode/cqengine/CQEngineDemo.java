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
package com.googlecode.cqengine;

import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.radixinverted.InvertedRadixTreeIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.Car.Color;

import java.util.Collections;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * Demonstrates creating an indexed collection, adding various indexes to it, adding various objects to the collection,
 * and performing some queries on the collection to retrieve matching objects.
 * 
 * @author Niall Gallagher
 */
public class CQEngineDemo {

    public static void main(String[] args) {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();

        cars.addIndex(HashIndex.onAttribute(Car.MODEL));
        cars.addIndex(HashIndex.onAttribute(Car.COLOR));
        cars.addIndex(NavigableIndex.onAttribute(Car.DOORS));
        cars.addIndex(RadixTreeIndex.onAttribute(Car.MODEL));
        cars.addIndex(ReversedRadixTreeIndex.onAttribute(Car.MODEL));
        cars.addIndex(InvertedRadixTreeIndex.onAttribute(Car.MODEL));
        cars.addIndex(SuffixTreeIndex.onAttribute(Car.MODEL));

        cars.add(new Car(1, "Ford",   "Focus",  Color.BLUE,  5, 9000.50, Collections.<String>emptyList(), Collections.emptyList()));
        cars.add(new Car(2, "Ford",   "Fiesta", Color.BLUE,  2, 5000.00, Collections.<String>emptyList(), Collections.emptyList()));
        cars.add(new Car(3, "Ford",   "F-150",  Color.RED,   2, 9500.00, Collections.<String>emptyList(), Collections.emptyList()));
        cars.add(new Car(4, "Honda",  "Civic",  Color.RED,   5, 5000.00, Collections.<String>emptyList(), Collections.emptyList()));
        cars.add(new Car(5, "Toyota", "Prius",  Color.BLACK, 3, 9700.00, Collections.<String>emptyList(), Collections.emptyList()));

        Query<Car> query;

        System.out.println("\nAll cars, ordered by carId:");
        query = all(Car.class);
        for (Car car : cars.retrieve(query, queryOptions(orderBy(ascending(Car.CAR_ID))))) {
            System.out.println(car);
        }

        System.out.println("\nFord cars:");
        query = equal(Car.MANUFACTURER, "Ford");
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\n3-door cars:");
        query = equal(Car.DOORS, 3);
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\n2 or 3-door cars:");
        query = between(Car.DOORS, 2, 3);
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\n2 or 5-door cars:");
        query = in(Car.DOORS, 2, 5);
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\nBlue Ford cars:");
        query = and(equal(Car.COLOR, Color.BLUE), equal(Car.MANUFACTURER, "Ford"));
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\nNOT 3-door cars:");
        query = not(equal(Car.DOORS, 3));
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\nCars which have 5 doors and which are not red:");
        query = and(equal(Car.DOORS, 5), not(equal(Car.COLOR, Color.RED)));
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\nNOT 3-door cars, sorted by doors ascending:");
        query = not(equal(Car.DOORS, 3));
        for (Car car : cars.retrieve(query, queryOptions(orderBy(ascending(Car.DOORS))))) {
            System.out.println(car);
        }

        System.out.println("\nNOT 3-door cars, sorted by doors ascending then price descending:");
        query = not(equal(Car.DOORS, 3));
        for (Car car : cars.retrieve(query, queryOptions(orderBy(ascending(Car.DOORS), descending(Car.PRICE))))) {
            System.out.println(car);
        }

        System.out.println("\nCars whose model starts with 'F':");
        query = startsWith(Car.MODEL, "F");
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\nCars whose model ends with 's':");
        query = endsWith(Car.MODEL, "s");
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\nCars whose model contains 'i':");
        query = contains(Car.MODEL, "i");
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }

        System.out.println("\nCars whose model is contained in 'Banana, Focus, Civic, Foobar':");
        query = isContainedIn(Car.MODEL, "Banana, Focus, Civic, Foobar");
        for (Car car : cars.retrieve(query)) {
            System.out.println(car);
        }
    }
}
