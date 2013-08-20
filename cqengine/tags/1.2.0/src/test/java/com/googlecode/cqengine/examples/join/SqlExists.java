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
package com.googlecode.cqengine.examples.join;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.query.Query;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;

/**
 * Demonstrates a query between two indexed collections. Given a collection of Cars, and a collections of Garages,
 * find cars which are convertible or which have a sunroof, which can be serviced by garages in Dublin.
 *
 * @author Niall Gallagher
 */
public class SqlExists {

    public static void main(String[] args) {
        // Create an indexed collection of cars...
        IndexedCollection<Car> cars = CQEngine.newInstance();
        cars.add(new Car(1, "Ford Focus", "great condition, low mileage", asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "Ford Taurus", "dirty and unreliable, flat tyre", asList("spare tyre", "radio")));
        cars.add(new Car(3, "Honda Civic", "has a flat tyre and high mileage", asList("radio")));
        cars.add(new Car(4, "BMW M3", "2013 model", asList("radio", "convertible")));

        // Create an indexed collection of garages...
        final IndexedCollection<Garage> garages = CQEngine.newInstance();
        garages.add(new Garage(1, "Joe's garage", "London", asList("Ford Focus", "Honda Civic")));
        garages.add(new Garage(2, "Jane's garage", "Dublin", asList("BMW M3")));
        garages.add(new Garage(3, "John's garage", "Dublin", asList("Ford Focus", "Ford Taurus")));
        garages.add(new Garage(4, "Jill's garage", "Dublin", asList("Ford Focus")));

        // Query: Cars which are convertible or which have a sunroof, which can be serviced in Dublin...
        Query<Car> carsQuery = and(
                in(Car.FEATURES, "sunroof", "convertible"),
                existsIn(garages,
                        Car.NAME,
                        Garage.BRANDS_SERVICED,
                        equal(Garage.LOCATION, "Dublin")
                )
        );

        for (Car car : cars.retrieve(carsQuery)) {
            System.out.println(car.name + " has a sunroof or is convertible, and can be serviced in Dublin");
        }
        /* ..prints:

            BMW M3 has a sunroof or is convertible, and can be serviced in Dublin
            Ford Focus has a sunroof or is convertible, and can be serviced in Dublin
         */
    }
}