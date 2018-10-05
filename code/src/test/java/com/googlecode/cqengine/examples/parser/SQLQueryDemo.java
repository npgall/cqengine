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

        results.forEach(System.out::println); // Prints: Honda Accord, Ford Fusion, Ford Focus
    }
}
