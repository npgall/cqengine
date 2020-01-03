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
package com.googlecode.cqengine.functional;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationOption;
import com.googlecode.cqengine.query.option.DeduplicationStrategy;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.Collections;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.testutil.Car.COLOR;
import static com.googlecode.cqengine.testutil.Car.Color.BLUE;
import static com.googlecode.cqengine.testutil.Car.MANUFACTURER;
import static org.junit.Assert.assertEquals;
/**
 * @author Niall Gallagher
 */
public class DeduplicationTest {

    @Test
    public void testDeduplication_Materialize() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.add(new Car(1, "Ford", "Focus", BLUE, 5, 1000.0, Collections.<String>emptyList(), Collections.emptyList()));
        cars.addIndex(HashIndex.onAttribute(Car.COLOR));
        cars.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));

        Query<Car> query = or(
                equal(COLOR, BLUE),
                equal(MANUFACTURER, "Ford")
        );
        ResultSet<Car> results;
        results = cars.retrieve(query);
        assertEquals(2, results.size());

        DeduplicationOption deduplicate = deduplicate(DeduplicationStrategy.MATERIALIZE);
        results = cars.retrieve(query, queryOptions(deduplicate));
        assertEquals(1, results.size());
    }

    @Test
    public void testDeduplication_Logical() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.add(new Car(1, "Ford", "Focus", BLUE, 5, 1000.0, Collections.<String>emptyList(), Collections.emptyList()));
        cars.addIndex(HashIndex.onAttribute(Car.COLOR));
        cars.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));

        Query<Car> query = or(
                equal(COLOR, BLUE),
                equal(MANUFACTURER, "Ford")
        );
        ResultSet<Car> results;
        results = cars.retrieve(query);
        assertEquals(2, results.size());

        DeduplicationOption deduplicate = deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION);
        results = cars.retrieve(query, queryOptions(deduplicate));
        assertEquals(1, results.size());
    }
}
