/**
 * Copyright 2012-2019 Niall Gallagher
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
package com.googlecode.cqengine.metadata;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.support.KeyStatistics;
import com.googlecode.cqengine.index.support.KeyValue;
import com.googlecode.cqengine.index.support.KeyValueMaterialized;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link AttributeMetadata}.
 */
public class AttributeMetadataTest {

    @Test
    public void testGetFrequencyDistribution() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20);

        // Add an unsorted index on Car.MANUFACTURER (a HashIndex)...
        cars.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Access metadata for Car.MANUFACTURER attribute.
        // Because we call getAttributeMetadata() (and not getSortedAttributeMetadata()),
        // values will be returned in no particular order...
        AttributeMetadata<String, Car> sortedAttributeMetadata = metadataEngine.getAttributeMetadata(Car.MANUFACTURER);

        // Call AttributeMetadata.getFrequencyDistribution() to retrieve distinct keys and counts in no particular order.
        // We retrieve into a Set (not a List), to assert the expected values were returned, without asserting any order...
        assertEquals(
                asSet(frequency("Ford", 6), frequency("BMW", 2), frequency("Toyota", 6), frequency("Honda", 6)),
                sortedAttributeMetadata.getFrequencyDistribution().collect(toSet())
        );
    }

    @Test
    public void testGetDistinctKeys() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20); // the 20 cars will contain 10 distinct models

        // Add an unsorted index on Car.MODEL (a HashIndex)...
        cars.addIndex(HashIndex.onAttribute(Car.MODEL));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Access metadata for Car.MODEL attribute.
        // Because we call getAttributeMetadata() (and not getSortedAttributeMetadata()),
        // values will be returned in no particular order...
        AttributeMetadata<String, Car> attributeMetadata = metadataEngine.getAttributeMetadata(Car.MODEL);

        // Call AttributeMetadata.getDistinctKeys() to retrieve distinct keys in no particular order.
        // We retrieve into a Set (not a List), to assert the expected values were returned, without asserting any order...
        assertEquals(
                asSet("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus"),
                attributeMetadata.getDistinctKeys().collect(toSet())
        );
    }

    @Test
    public void testGetCountOfDistinctKeys() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20); // the 20 cars will contain 4 distinct manufacturers

        // Add an unsorted index on Car.MANUFACTURER (a HashIndex)...
        cars.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Count the distinct manufacturers...
        AttributeMetadata<String, Car> attributeMetadata = metadataEngine.getAttributeMetadata(Car.MANUFACTURER);
        assertEquals(Integer.valueOf(4), attributeMetadata.getCountOfDistinctKeys());
    }

    @Test
    public void testGetCountForKey() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20);

        // Add an unsorted index on Car.MANUFACTURER (a HashIndex)...
        cars.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Count the number of cars manufactured by BMW...
        AttributeMetadata<String, Car> attributeMetadata = metadataEngine.getAttributeMetadata(Car.MANUFACTURER);
        assertEquals(Integer.valueOf(2), attributeMetadata.getCountForKey("BMW"));
    }

    @Test
    public void testGetKeysAndValues() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<>();
        Car car1 = new Car(1, "Ford", "Taurus", Car.Color.GREEN, 4, 1000.0, emptyList(), Collections.emptyList());
        Car car2 = new Car(2, "Honda", "Civic", Car.Color.BLUE, 4, 2000.0, emptyList(), Collections.emptyList());
        Car car3 = new Car(3, "Honda", "Accord", Car.Color.RED, 4, 3000.0, emptyList(), Collections.emptyList());
        cars.addAll(asList(car1, car2, car3));

        // Add an unsorted index on Car.MANUFACTURER (a HashIndex)...
        cars.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Access metadata for Car.MODEL attribute.
        // Because we call getAttributeMetadata() (and not getSortedAttributeMetadata()),
        // values will be returned in no particular order...
        AttributeMetadata<String, Car> attributeMetadata = metadataEngine.getAttributeMetadata(Car.MANUFACTURER);

        // Call AttributeMetadata.getDistinctKeys() to retrieve distinct keys in no particular order.
        // We retrieve into a Set (not a List), to assert the expected values were returned, without asserting any order...
        assertEquals(
                asSet(keyValue("Ford", car1), keyValue("Honda", car2), keyValue("Honda", car3)),
                attributeMetadata.getKeysAndValues().collect(toSet())
        );
    }

    // ==============================
    // === Test helper methods... ===
    // ==============================

    static IndexedCollection<Car> createIndexedCollectionOfCars(int numCars) {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<>();
        collection.addAll(CarFactory.createCollectionOfCars(numCars));
        return collection;
    }

    static <T> Set<T> asSet(T... elements) {
        return new LinkedHashSet<>(asList(elements));
    }

    static <A> KeyFrequency<A> frequency(A value, int count) {
        return new KeyStatistics<>(value, count);
    }

    static <A, O> KeyValue<A, O> keyValue(A key, O value) {
        return new KeyValueMaterialized<>(key, value);
    }
}