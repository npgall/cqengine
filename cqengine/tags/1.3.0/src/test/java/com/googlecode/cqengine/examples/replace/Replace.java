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
package com.googlecode.cqengine.examples.replace;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.filter.DeduplicatingResultSet;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * Demonstrates how to concurrently replace a Car in a collection, using multi-version concurrency control (MVCC).
 * <p/>
 * This is an alternative to simply removing the old Car object and adding a new Car object with the same carId,
 * which is susceptible to a race condition where reading threads might momentarily observe the collection not
 * containing any car with the carId.
 * <p/>
 * In this approach, a new <i>version</i> of the car is added to the collection <i>before removing the old
 * version</i>. Filtering on read is applied, such that reading threads will find either the old version or
 * the new version of the car (but always exactly one version of it), and will never observe the collection
 * <i>not</i> containing the car.
 * <p/>
 * See also the modified {@link Car} class for this example, which has automatic versioning.
 *
 * @author Niall Gallagher
 */
public class Replace {

    /**
     * Demonstrates how to concurrently replace a Car in a collection, using multi-version concurrency control.
     * <p/>
     * Prints:
     * <pre>
     * The only car in the collection, before the replacement: Car{carId=1, name='Ford Focus', version=1}
     * Collection contains 2 cars, but we filtered the duplicate: Car{carId=1, name='New Ford Focus', version=2}
     * Collection contains 1 car again: Car{carId=1, name='New Ford Focus', version=2}
     * </pre>
     *
     * @param args Not used
     */
    public static void main(String[] args) {
        IndexedCollection<Car> cars = CQEngine.newInstance();

        // Add a car with carId 1...
        cars.add(new Car(1, "Ford Focus"));

        // Test the retrieval...
        Car carFound = retrieveOnlyOneVersion(cars, 1);
        System.out.println("The only car in the collection, before the replacement: " + carFound);

        // Update the name of the Car with carId 1, by replacing it using MVCC...
        Car oldVersion = cars.retrieve(equal(Car.CAR_ID, 1)).uniqueResult(); // Retrieve the existing version
        Car newVersion = new Car(1, "New Ford Focus"); // Create a new car, same carId, different version
        cars.add(newVersion); // Collection now contains two versions of the same car

        // Test the retrieval (collection contains both versions, should retrieve only one of them)...
        carFound = retrieveOnlyOneVersion(cars, 1);
        System.out.println("Collection contains " + cars.size() + " cars, but we filtered the duplicate: " + carFound);

        cars.remove(oldVersion); // Remove the old version, collection now only contains new version

        // Test the retrieval...
        carFound = retrieveOnlyOneVersion(cars, 1);
        System.out.println("Collection contains " + cars.size() + " car again: " + carFound);
    }

    static Car retrieveOnlyOneVersion(IndexedCollection<Car> cars, int carId) {
        ResultSet<Car> multipleCarVersions = cars.retrieve(equal(Car.CAR_ID, carId));
        // Wrap in a result set which will return only one car per version number...
        ResultSet<Car> deduplicatedCars = new DeduplicatingResultSet<Car, Integer>(Car.CAR_ID, multipleCarVersions);

        return deduplicatedCars.uniqueResult();
    }
}
