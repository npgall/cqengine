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
package com.googlecode.cqengine.examples.introduction;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.query.Query;

import java.util.Arrays;

import static com.googlecode.cqengine.query.QueryFactory.*;
/**
 * An introductory example which demonstrates usage using a Car analogy.
 *
 * @author Niall Gallagher
 */
public class Introduction {

    public static void main(String[] args) {
        // Create an indexed collection (note: could alternatively use CQEngine.copyFrom() existing collection)...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();

        // Add some indexes...
        cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
        cars.addIndex(ReversedRadixTreeIndex.onAttribute(Car.NAME));
        cars.addIndex(SuffixTreeIndex.onAttribute(Car.DESCRIPTION));
        cars.addIndex(HashIndex.onAttribute(Car.FEATURES));

        // Add some objects to the collection...
        cars.add(new Car(1, "ford focus", "great condition, low mileage", Arrays.asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "ford taurus", "dirty and unreliable, flat tyre", Arrays.asList("spare tyre", "radio")));
        cars.add(new Car(3, "honda civic", "has a flat tyre and high mileage", Arrays.asList("radio")));

        // -------------------------- Run some queries --------------------------
        System.out.println("Cars whose name ends with 'vic' or whose id is less than 2:");
        Query<Car> query1 = or(endsWith(Car.NAME, "vic"), lessThan(Car.CAR_ID, 2));
        cars.retrieve(query1).forEach(System.out::println);

        System.out.println("\nCars whose flat tyre can be replaced:");
        Query<Car> query2 = and(contains(Car.DESCRIPTION, "flat tyre"), equal(Car.FEATURES, "spare tyre"));
        cars.retrieve(query2).forEach(System.out::println);


        System.out.println("\nCars which have a sunroof or a radio but are not dirty:");
        Query<Car> query3 = and(in(Car.FEATURES, "sunroof", "radio"), not(contains(Car.DESCRIPTION, "dirty")));
        cars.retrieve(query3).forEach(System.out::println);
    }
}
