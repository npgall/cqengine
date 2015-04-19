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
package com.googlecode.cqengine.examples.ordering;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.option.OrderingStrategy.INDEX;

/**
 * @author Niall Gallagher
 */
public class IndexOrderingDemo {

    public static void main(String[] args) {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
        cars.addAll(CarFactory.createCollectionOfCars(100));

        ResultSet<Car> results = cars.retrieve(
                between(Car.CAR_ID, 40, 50),
                queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX)
        ));
        for (Car car : results) {
            System.out.println(car); // prints cars 50 -> 40, using the index for ordering
        }
    }
}
