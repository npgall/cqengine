package com.googlecode.cqengine.examples.parser;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.codegen.AttributeBytecodeGenerator;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;

/**
 * Demonstrates creating a collection and running an SQL query on it.
 * <p/>
 * Attribute are generated automatically using {@link AttributeBytecodeGenerator}.
 *
 * @author niall.gallagher
 */
public class SQLQueryDemo {

    public static void main(String[] args) {
        SQLParser<Car> parser = SQLParser.forPojoWithAttributes(Car.class, createAttributes(Car.class));
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addAll(CarFactory.createCollectionOfCars(10));

        ResultSet<Car> results = parser.retrieve(cars, "SELECT * FROM cars WHERE (" +
                                        "(manufacturer = 'Ford' OR manufacturer = 'Honda') " +
                                        "AND price <= 5000.0 " +
                                        "AND color NOT IN ('GREEN', 'WHITE')) " +
                                        "ORDER BY manufacturer DESC, price ASC");

        for (Car car : results) {
            System.out.println(car); // Prints: Honda Accord, Ford Fusion, Ford Focus
        }
    }
}
