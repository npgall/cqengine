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
        ResultSet<Car> resultSet = cars.retrieve(equal(Car.MANUFACTURER, "Ford"));
        try {
            for (Car car : resultSet) {
                System.out.println(car); // prints car 1 ("Ford Fusion")
            }
        }
        finally {
            resultSet.close(); // ..close the ResultSet when finished reading!
        }
    }
}
