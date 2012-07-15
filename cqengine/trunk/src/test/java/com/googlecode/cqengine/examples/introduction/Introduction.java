package com.googlecode.cqengine.examples.introduction;

import com.googlecode.concurrenttrees.radixreversed.ReversedRadixTree;
import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;

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
        IndexedCollection<Car> cars = CQEngine.newInstance();

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
        for (Car car : cars.retrieve(query1)) {
            System.out.println(car);
        }

        System.out.println("\nCars whose flat tyre can be replaced:");
        Query<Car> query2 = and(contains(Car.DESCRIPTION, "flat tyre"), equal(Car.FEATURES, "spare tyre"));
        for (Car car : cars.retrieve(query2)) {
            System.out.println(car);
        }

        System.out.println("\nCars which have a sunroof or a radio but are not dirty:");
        Query<Car> query3 = and(in(Car.FEATURES, "sunroof", "radio"), not(contains(Car.DESCRIPTION, "dirty")));
        for (Car car : cars.retrieve(query3)) {
            System.out.println(car);
        }
    }
}
