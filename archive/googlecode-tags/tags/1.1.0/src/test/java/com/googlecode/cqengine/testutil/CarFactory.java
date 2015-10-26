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
package com.googlecode.cqengine.testutil;

import java.util.*;

/**
 * @author Niall Gallagher
 */
public class CarFactory {

    public static Set<Car> createCollectionOfCars(int numCars) {
        Set<Car> cars = new LinkedHashSet<Car>(numCars);
        for (int carId = 0; carId < numCars; carId++) {
            switch (carId % 10) {
                case 0: cars.add( new Car(carId, "Ford",   "Focus",   Car.Color.RED,   5, 5000.00) ); break;
                case 1: cars.add( new Car(carId, "Ford",   "Fusion",  Car.Color.RED,   4, 3999.99) ); break;
                case 2: cars.add( new Car(carId, "Ford",   "Taurus",  Car.Color.GREEN, 4, 6000.00) ); break;
                case 3: cars.add( new Car(carId, "Honda",  "Civic",   Car.Color.WHITE, 5, 4000.00) ); break;
                case 4: cars.add( new Car(carId, "Honda",  "Accord",  Car.Color.BLACK, 5, 3000.00) ); break;
                case 5: cars.add( new Car(carId, "Honda",  "Insight", Car.Color.GREEN, 3, 5000.00) ); break;
                case 6: cars.add( new Car(carId, "Toyota", "Avensis", Car.Color.GREEN, 5, 5999.95) ); break;
                case 7: cars.add( new Car(carId, "Toyota", "Prius",   Car.Color.BLUE,  3, 8500.00) ); break;
                case 8: cars.add( new Car(carId, "Toyota", "Hilux",   Car.Color.RED,   5, 7800.55) ); break;
                case 9: cars.add( new Car(carId, "BMW",    "M6",      Car.Color.BLUE,  2, 9000.23) ); break;
                default: throw new IllegalStateException();
            }
        }
        return cars;
    }
}
