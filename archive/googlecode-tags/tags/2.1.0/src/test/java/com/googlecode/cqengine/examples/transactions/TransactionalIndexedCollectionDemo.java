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
package com.googlecode.cqengine.examples.transactions;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static java.util.Arrays.asList;

/**
 * Example usage for {@link com.googlecode.cqengine.TransactionalIndexedCollection}.
 *
 * @author Niall Gallagher
 */
public class TransactionalIndexedCollectionDemo {

    public static void main(String[] args) {
        // Create example Car objects...
        Car car1 = CarFactory.createCar(1); // "Ford Fusion"
        Car car2 = CarFactory.createCar(2); // "Ford Taurus"
        Car car3 = CarFactory.createCar(3); // "Honda Civic"
        Car car4 = CarFactory.createCar(4); // "Honda Accord"

        // We will store the cars in TransactionalIndexedCollection, which provides MVCC support...
        IndexedCollection<Car> cars = new TransactionalIndexedCollection<Car>(Car.class);

        // ===== Examples of modifying the collection using MVCC transactions... =====

        // Add 4 cars in a single transaction...
        cars.addAll(asList(car1, car2, car3, car4));

        // Remove 2 cars in a single transaction...
        cars.removeAll(asList(car3, car4));

        // Replace 1 car with 2 other cars in a single transaction...
        cars.update(asList(car2), asList(car3, car4));

        // ===== Examples of querying the collection using MVCC transactions... =====

        // Retrieve with READ_COMMITTED transaction isolation...
        ResultSet<Car> results = cars.retrieve(equal(Car.MANUFACTURER, "Ford"));
        try {
            for (Car car : results) {
                System.out.println(car); // prints car 1 ("Ford Fusion")
            }
        }
        finally {
            results.close(); // ..close the ResultSet when finished reading!
        }
    }
}
