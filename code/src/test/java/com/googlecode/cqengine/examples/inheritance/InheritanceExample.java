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
package com.googlecode.cqengine.examples.inheritance;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

import static com.googlecode.cqengine.query.QueryFactory.*;

import java.util.Arrays;

/**
 * @author Niall Gallagher
 */
public class InheritanceExample {

    static final Attribute<Car, Class<? extends Car>> CAR_TYPE = new SimpleAttribute<Car, Class<? extends Car>>() {
        public Class<? extends Car> getValue(Car car, QueryOptions queryOptions) { return car.getClass(); }
    };

    public static void main(String[] args) {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();

        // Add some indexes...
        cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
        cars.addIndex(SuffixTreeIndex.onAttribute(Car.NAME));
        cars.addIndex(SuffixTreeIndex.onAttribute(Car.DESCRIPTION));
        cars.addIndex(HashIndex.onAttribute(Car.FEATURES));
        // Index which applies only to SportsCar subclass...
        cars.addIndex(NavigableIndex.onAttribute(SportsCar.HORSEPOWER));

        // Add some objects to the collection...
        cars.add(new Car(1, "mazda 6", "great condition, low mileage", Arrays.asList("nitro boost", "sunroof")));
        cars.add(new Car(2, "honda civic", "has a flat tyre and high mileage", Arrays.asList("radio")));
        cars.add(new SportsCar(3, "mazda mx-5", "red and sporty", Arrays.asList("radio"), 9000));
        cars.add(new SportsCar(4, "porsche 911", "really fast", Arrays.asList("cd player"), 10000));

        System.out.println("Cars which have nitro boost or have a horsepower attribute:- ");
        Query<Car> carsWithNitroBoostOrAHorsepowerAttribute = or(
                equal(Car.FEATURES, "nitro boost"),
                has(SportsCar.HORSEPOWER)
        );
        for (Car car : cars.retrieve(carsWithNitroBoostOrAHorsepowerAttribute)) {
            System.out.println(car); // prints mazda 6, mazda mx-5, porsche 911
        }

        System.out.println();
        System.out.println("Cars which are SportsCars but are not a Mazda:- ");
        Query<Car> sportsCarsButNotMazda = and(
                equal(CAR_TYPE, SportsCar.class),
                not(contains(Car.NAME, "mazda"))
        );
        for (Car car : cars.retrieve(sportsCarsButNotMazda)) {
            System.out.println(car); // prints porsche 911
        }

        System.out.println();
        System.out.println("Cars which are a Honda or have horsepower above 9000:- ");
        Query<Car> hondaOrHorsepowerAbove9000 = or(
                contains(Car.NAME, "honda"),
                greaterThan(SportsCar.HORSEPOWER, 9000)
        );
        for (Car car : cars.retrieve(hondaOrHorsepowerAbove9000)) {
            System.out.println(car); // prints honda civic, porsche 911
        }
    }
}
