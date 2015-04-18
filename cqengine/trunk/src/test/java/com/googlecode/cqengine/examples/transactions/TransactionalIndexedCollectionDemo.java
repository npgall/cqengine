package com.googlecode.cqengine.examples.transactions;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import java.util.Arrays;

import static com.googlecode.cqengine.query.QueryFactory.all;
import static java.util.Arrays.asList;

/**
 * @author Niall Gallagher
 */
public class TransactionalIndexedCollectionDemo {

    public static void main(String[] args) {
// Create example Car objects...
Car car1 = CarFactory.createCar(1);
Car car2 = CarFactory.createCar(2);
Car car3 = CarFactory.createCar(3);
Car car4 = CarFactory.createCar(4);

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

// Read all cars with READ_COMMITTED transaction isolation...
ResultSet<Car> allCars = cars.retrieve(all(Car.class));
try {
    for (Car car : allCars) {
        System.out.println(car); // prints cars 1, 3 & 4
    }
}
finally {
    allCars.close(); // ..close the ResultSet when finished reading!
}
    }
}
