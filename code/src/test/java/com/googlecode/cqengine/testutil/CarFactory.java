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
package com.googlecode.cqengine.testutil;

import com.googlecode.concurrenttrees.common.LazyIterator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;

/**
 * @author Niall Gallagher
 */
public class CarFactory {

    public static Set<Car> createCollectionOfCars(int numCars) {
        Set<Car> cars = new LinkedHashSet<Car>(numCars);
        for (int carId = 0; carId < numCars; carId++) {
            cars.add(createCar(carId));
        }
        return cars;
    }

    public static Iterable<Car> createIterableOfCars(final int numCars) {
        final AtomicInteger count = new AtomicInteger();
        return new Iterable<Car>() {
            @Override
            public Iterator<Car> iterator() {
                return new LazyIterator<Car>() {
                    @Override
                    protected Car computeNext() {
                        int carId = count.getAndIncrement();
                        return carId < numCars ? createCar(carId) : endOfData();
                    }
                };
            }
        };
    }

    public static Car createCar(int carId) {
        switch (carId % 10) {
            case 0: return new Car(carId, "Ford",   "Focus",   Car.Color.RED,   5, 5000.00, noFeatures(), noKeywords());
            case 1: return new Car(carId, "Ford",   "Fusion",  Car.Color.RED,   4, 3999.99, asList("hybrid"), asList("zulu"));
            case 2: return new Car(carId, "Ford",   "Taurus",  Car.Color.GREEN, 4, 6000.00, asList("grade a"), asList("alpha"));
            case 3: return new Car(carId, "Honda",  "Civic",   Car.Color.WHITE, 5, 4000.00, asList("grade b"), asList("bravo"));
            case 4: return new Car(carId, "Honda",  "Accord",  Car.Color.BLACK, 5, 3000.00, asList("grade c"), asList("very-good"));
            case 5: return new Car(carId, "Honda",  "Insight", Car.Color.GREEN, 3, 5000.00, noFeatures(), asList("alpha"));
            case 6: return new Car(carId, "Toyota", "Avensis", Car.Color.GREEN, 5, 5999.95, noFeatures(), asList("very-good-car"));
            case 7: return new Car(carId, "Toyota", "Prius",   Car.Color.BLUE,  3, 8500.00, asList("sunroof", "hybrid"), noKeywords());
            case 8: return new Car(carId, "Toyota", "Hilux",   Car.Color.RED,   5, 7800.55, noFeatures(), asList("very-good-car"));
            case 9: return new Car(carId, "BMW",    "M6",      Car.Color.BLUE,  2, 9000.23, asList("coupe"), asList("zulu"));
            default: throw new IllegalStateException();
        }
    }

    static List<String> noFeatures() {
        return Collections.<String>emptyList();
    }

    static List<String> noKeywords() {
        return Collections.<String>emptyList();
    }
}
