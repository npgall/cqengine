package com.googlecode.cqengine.examples.introduction;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.query.Query;

import static com.googlecode.cqengine.query.QueryFactory.*;
/**
 * An introductory example which demonstrates usage using a Car analogy.
 *
 * @author Niall Gallagher
 */
public class Introduction {

    public static void main(String[] args) {
        // -------------------------- Set up the collection --------------------------
        // Create an indexed collection (note: could alternatively use CQEngine.copyFrom() existing collection)...
        IndexedCollection<Car> cars = CQEngine.newInstance();

        // Add some indexes...
        cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
        cars.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
        cars.addIndex(ReversedRadixTreeIndex.onAttribute(Car.MODEL));

        // Add some objects to the collection...
        cars.add(new Car(1, "Ford", "Focus"));
        cars.add(new Car(2, "Ford", "Taurus"));
        cars.add(new Car(3, "Honda", "Civic"));

        // -------------------------- Run some queries --------------------------
        System.out.println("Cars manufactured by Ford:");
        for (Car car : cars.retrieve(equal(Car.MANUFACTURER, "Ford"))) {
            System.out.println(car);
        }

        System.out.println("\nCars whose model is Focus OR whose manufacturer is Honda:");
        Query<Car> query1 = or(equal(Car.MODEL, "Focus"), equal(Car.MANUFACTURER, "Honda"));
        for (Car car : cars.retrieve(query1)) {
            System.out.println(car);
        }

        System.out.println("\nCars whose model ends with 's' AND whose id is greater than 1:");
        Query<Car> query2 = and(endsWith(Car.MODEL, "s"), greaterThan(Car.CAR_ID, 1));
        for (Car car : cars.retrieve(query2)) {
            System.out.println(car);
        }
    }
}
