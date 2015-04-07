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
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.examples.join.Garage;
import com.googlecode.cqengine.query.Query;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Niall Gallagher
 */
public class JoinTest {

    // Create an indexed collection of cars...
    IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
    Car car1 = new Car(1, "Ford Focus", "great condition, low mileage", asList("spare tyre", "sunroof"));
    Car car2 = new Car(2, "Ford Taurus", "dirty and unreliable, flat tyre", asList("spare tyre", "radio"));
    Car car3 = new Car(3, "Honda Civic", "has a flat tyre and high mileage", asList("radio"));
    Car car4 = new Car(4, "BMW M3", "2013 model", asList("radio", "convertible"));
    Car car5 = new Car(5, "BMW M6", "2014 model", asList("radio", "convertible"));
    { cars.addAll(asList(car1, car2, car3, car4, car5)); }

    // Create an indexed collection of garages...
    final IndexedCollection<Garage> garages = new ConcurrentIndexedCollection<Garage>();
    Garage garage1 = new Garage(1, "Joe's garage", "London", asList("Ford Focus", "Honda Civic"));
    Garage garage2 = new Garage(2, "Jane's garage", "Dublin", asList("BMW M3"));
    Garage garage3 = new Garage(3, "John's garage", "Dublin", asList("Ford Focus", "Ford Taurus"));
    Garage garage4 = new Garage(4, "Jill's garage", "Dublin", asList("Ford Focus"));
    Garage garage5 = new Garage(5, "Sam's garage", "Dubai", asList("BMW M3", "BMW M6"));
    Garage garage6 = new Garage(6, "Jen's garage", "Galway", asList("Bat Mobile", "Golf Cart"));

    { garages.addAll(asList(garage1, garage2, garage3, garage4, garage5, garage6)); }


    /**
     * Find cars which are convertible or which have a sunroof, which can be serviced in Dublin.
     */
    @Test
    public void testSqlExistsWithForeignRestriction() {
        Query<Car> carsQuery = and(
                in(Car.FEATURES, "sunroof", "convertible"),
                existsIn(garages,
                        Car.NAME,
                        Garage.BRANDS_SERVICED,
                        equal(Garage.LOCATION, "Dublin")
                )
        );

        Set<Car> results = asSet(cars.retrieve(carsQuery));

        assertEquals("should have 2 results", 2, results.size());
        assertTrue("results should contain car1", results.contains(car1));
        assertTrue("results should contain car4", results.contains(car4));
    }

    /**
     * Find cars which are convertible or which have a sunroof,
     * which can be serviced by any garage which we have on file.
     */
    @Test
    public void testSqlExistsNoForeignRestriction() {
        Query<Car> carsQuery = and(
                in(Car.FEATURES, "sunroof", "convertible"),
                existsIn(garages,
                        Car.NAME,
                        Garage.BRANDS_SERVICED
                )
        );

        Set<Car> results = asSet(cars.retrieve(carsQuery));

        assertEquals("should have 3 results", 3, results.size());
        assertTrue("results should contain car1", results.contains(car1));
        assertTrue("results should contain car4", results.contains(car4));
        assertTrue("results should contain car5", results.contains(car5));
    }

    /**
     * Find garages which can service any type of convertible car.
     */
    @Test
    public void testSqlExistsMultiValuedWithForeignRestriction() {
        Query<Garage> garagesQuery = existsIn(cars,
                Garage.BRANDS_SERVICED,
                Car.NAME,
                equal(Car.FEATURES, "convertible")
        );

        Set<Garage> results = asSet(garages.retrieve(garagesQuery));

        assertEquals("should have 2 results", 2, results.size());
        assertTrue("results should contain garage2", results.contains(garage2));
        assertTrue("results should contain garage5", results.contains(garage5));
    }

    /**
     * Find garages which can service cars which we do not have on file.
     */
    @Test
    public void testSqlExistsMultiValuedNoForeignRestriction() {
        Query<Garage> garagesQuery = not(
                existsIn(cars,
                        Garage.BRANDS_SERVICED,
                        Car.NAME
                )
        );

        Set<Garage> results = asSet(garages.retrieve(garagesQuery));

        assertEquals("should have 1 result", 1, results.size());
        assertTrue("results should contain garage6", results.contains(garage6));
    }

    /**
     * Find cars which are convertible or which have a sunroof,
     * which can be serviced in Dublin, and find those Garages which can service them.
     */
    @Test
    public void testSqlExistsBasedJoin() {
        Query<Car> carsQuery = and(
                in(Car.FEATURES, "sunroof", "convertible"),
                existsIn(garages,
                        Car.NAME,
                        Garage.BRANDS_SERVICED,
                        equal(Garage.LOCATION, "Dublin")
                )
        );

        Map<Car, Set<Garage>> results = new LinkedHashMap<Car, Set<Garage>>();
        for (Car car : cars.retrieve(carsQuery)) {
            Query<Garage> garagesWhichServiceThisCarInDublin
                    = and(equal(Garage.BRANDS_SERVICED, car.name), equal(Garage.LOCATION, "Dublin"));
            for (Garage garage : garages.retrieve(garagesWhichServiceThisCarInDublin)) {
                Set<Garage> garagesWhichCanServiceThisCar = results.get(car);
                if (garagesWhichCanServiceThisCar == null) {
                    garagesWhichCanServiceThisCar = new LinkedHashSet<Garage>();
                    results.put(car, garagesWhichCanServiceThisCar);
                }
                garagesWhichCanServiceThisCar.add(garage);
            }
        }

        assertEquals("join results should contain 2 cars", 2, results.size());
        Assert.assertTrue("join results should contain car1", results.containsKey(car1));
        Assert.assertTrue("join results should contain car4", results.containsKey(car4));

        assertEquals("join results for car1", asSet(garage3, garage4), results.get(car1));
        assertEquals("join results for car4", asSet(garage2), results.get(car4));
    }


    static <T> Set<T> asSet(T... objects) {
        return asSet(asList(objects));
    }

    static <T> Set<T> asSet(Iterable<T> objects) {
        Set<T> results = new LinkedHashSet<T>();
        for (T object : objects) {
            results.add(object);
        }
        return results;
    }
}
