package com.googlecode.cqengine.examples.parser;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.codegen.AttributeBytecodeGenerator;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.cqn.CQNParser;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;

/**
 * Demonstrates creating a collection and running an CQN query on it.
 * <p/>
 * Attribute are generated automatically using {@link AttributeBytecodeGenerator}.
 *
 * @author niall.gallagher
 */
public class CQNQueryDemo {

    public static void main(String[] args) {
        CQNParser<Car> parser = CQNParser.forPojoWithAttributes(Car.class, createAttributes(Car.class));
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addAll(CarFactory.createCollectionOfCars(10));

        Query<Car> query = parser.parse("and(equal(\"manufacturer\", \"Ford\"), greaterThan(\"price\", 5000.0))");

        for (Car car : cars.retrieve(query)) {
            System.out.println(car); // Prints: Ford Taurus
        }
    }
}
