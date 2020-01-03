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
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.Collections;

import static com.googlecode.cqengine.metadata.AttributeMetadataTest.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link SortedAttributeMetadata}.
 */
public class SortedAttributeMetadataTest {

    @Test
    public void testGetFrequencyDistribution() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20);

        // Add a sorted index on Car.MANUFACTURER (a NavigableIndex)...
        cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Access metadata for Car.MANUFACTURER attribute.
        // Because we call getSortedAttributeMetadata() values will be returned in ascending order...
        SortedAttributeMetadata<String, Car> sortedAttributeMetadata = metadataEngine.getSortedAttributeMetadata(Car.MANUFACTURER);

        // Call AttributeMetadata.getFrequencyDistribution() to retrieve distinct keys and counts in ascending order.
        // We retrieve into a List in order to assert the ordering of values returned...
        assertEquals(
                asList(frequency("BMW", 2), frequency("Ford", 6), frequency("Honda", 6), frequency("Toyota", 6)),
                sortedAttributeMetadata.getFrequencyDistribution().collect(toList())
        );
    }

    @Test
    public void testGetFrequencyDistributionDescending() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20);

        // Add a sorted index on Car.MANUFACTURER (a NavigableIndex)...
        cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Access metadata for Car.MANUFACTURER attribute.
        // Because we call getSortedAttributeMetadata() values will be returned in ascending order...
        SortedAttributeMetadata<String, Car> sortedAttributeMetadata = metadataEngine.getSortedAttributeMetadata(Car.MANUFACTURER);

        // Call AttributeMetadata.getFrequencyDistribution() to retrieve distinct keys and counts in ascending order.
        // We retrieve into a List in order to assert the ordering of values returned...
        assertEquals(
                asList(frequency("Toyota", 6), frequency("Honda", 6), frequency("Ford", 6), frequency("BMW", 2)),
                sortedAttributeMetadata.getFrequencyDistributionDescending().collect(toList())
        );
    }

    @Test
    public void testGetDistinctKeys() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20); // the 20 cars will contain 10 distinct models

        // Add a sorted index on Car.MODEL (a NavigableIndex)...
        cars.addIndex(NavigableIndex.onAttribute(Car.MODEL));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Access metadata for Car.MODEL attribute.
        // Because we call getSortedAttributeMetadata(), values will be returned in sorted order...
        SortedAttributeMetadata<String, Car> sortedAttributeMetadata = metadataEngine.getSortedAttributeMetadata(Car.MODEL);

        // Call SortedAttributeMetadata.getDistinctKeys() to retrieve distinct keys in ascending order.
        // We retrieve into a List in order to assert the ordering of values returned...
        assertEquals(
                asList("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus"),
                sortedAttributeMetadata.getDistinctKeys().collect(toList())
        );

        // Test specifying range explicitly...
        assertEquals(
                asList("Civic", "Focus", "Fusion", "Hilux", "Insight"),
                sortedAttributeMetadata.getDistinctKeys("Civic", true, "Insight", true).collect(toList())
        );
        assertEquals(
                asList("Focus", "Fusion", "Hilux"),
                sortedAttributeMetadata.getDistinctKeys("Civic", false, "Insight", false).collect(toList())
        );
        assertEquals(
                asList("Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius"),
                sortedAttributeMetadata.getDistinctKeys("Alpha", false, "Tango", false).collect(toList())
        );
    }

    @Test
    public void testGetDistinctKeysDescending() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20); // the 20 cars will contain 10 distinct models

        // Add a sorted index on Car.MODEL (a NavigableIndex)...
        cars.addIndex(NavigableIndex.onAttribute(Car.MODEL));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Access metadata for Car.MODEL attribute.
        // Because we call getSortedAttributeMetadata(), values will be returned in sorted order...
        SortedAttributeMetadata<String, Car> sortedAttributeMetadata = metadataEngine.getSortedAttributeMetadata(Car.MODEL);

        // Call SortedAttributeMetadata.getDistinctKeysDescending() to retrieve distinct keys in descending order.
        // We retrieve into a List in order to assert the ordering of values returned...
        assertEquals(
                asList("Taurus", "Prius", "M6", "Insight", "Hilux", "Fusion", "Focus", "Civic", "Avensis", "Accord"),
                sortedAttributeMetadata.getDistinctKeysDescending().collect(toList())
        );

        // Test specifying range explicitly...
        assertEquals(
                asList("Hilux", "Fusion", "Focus"),
                sortedAttributeMetadata.getDistinctKeysDescending("Civic", false, "Insight", false).collect(toList())
        );
        assertEquals(
                asList("Prius", "M6", "Insight", "Hilux", "Fusion", "Focus", "Civic", "Avensis"),
                sortedAttributeMetadata.getDistinctKeysDescending("Alpha", false, "Tango", false).collect(toList())
        );
    }

    @Test
    public void testGetCountOfDistinctKeys() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20); // the 20 cars will contain 4 distinct manufacturers

        // Add a sorted index on Car.MANUFACTURER (a NavigableIndex)...
        cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Count the distinct manufacturers...
        SortedAttributeMetadata<String, Car> sortedAttributeMetadata = metadataEngine.getSortedAttributeMetadata(Car.MANUFACTURER);
        assertEquals(Integer.valueOf(4), sortedAttributeMetadata.getCountOfDistinctKeys());
    }

    @Test
    public void testGetCountForKey() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(20);

        // Add a sorted index on Car.MANUFACTURER (a NavigableIndex)...
        cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Count the number of cars manufactured by BMW...
        SortedAttributeMetadata<String, Car> attributeMetadata = metadataEngine.getSortedAttributeMetadata(Car.MANUFACTURER);
        assertEquals(Integer.valueOf(2), attributeMetadata.getCountForKey("BMW"));
    }

    @Test
    public void testGetKeysAndValues() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<>();
        Car car1 = new Car(1, "Ford", "Taurus", Car.Color.GREEN, 4, 1000.0, emptyList(), Collections.emptyList());
        Car car2 = new Car(2, "Toyota", "Prius", Car.Color.BLUE, 4, 2000.0, emptyList(), Collections.emptyList());
        Car car3 = new Car(3, "Honda", "Civic", Car.Color.BLUE, 4, 2000.0, emptyList(), Collections.emptyList());
        cars.addAll(asList(car1, car2, car3));

        // Add a sorted index on Car.MANUFACTURER (a NavigableIndex)...
        cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Access metadata for Car.MANUFACTURER attribute.
        SortedAttributeMetadata<String, Car> attributeMetadata = metadataEngine.getSortedAttributeMetadata(Car.MANUFACTURER);

        // Call SortedAttributeMetadata.getKeysAndValues() to retrieve keys and values in ascending order.
        // We retrieve into a List in order to assert the ordering of values returned...
        assertEquals(
                asList(keyValue("Ford", car1), keyValue("Honda", car3), keyValue("Toyota", car2)),
                attributeMetadata.getKeysAndValues().collect(toList())
        );

        // Test specifying range explicitly...
        assertEquals(
                asList(keyValue("Ford", car1), keyValue("Honda", car3)),
                attributeMetadata.getKeysAndValues("Alpha", true, "Toyota", false).collect(toList())
        );
    }



    @Test
    public void testGetKeysAndValuesDescending() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<>();
        Car car1 = new Car(1, "Ford", "Taurus", Car.Color.GREEN, 4, 1000.0, emptyList(), Collections.emptyList());
        Car car2 = new Car(2, "Toyota", "Prius", Car.Color.BLUE, 4, 2000.0, emptyList(), Collections.emptyList());
        Car car3 = new Car(3, "Honda", "Civic", Car.Color.BLUE, 4, 2000.0, emptyList(), Collections.emptyList());
        cars.addAll(asList(car1, car2, car3));

        // Add a sorted index on Car.MANUFACTURER (a NavigableIndex)...
        cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));
        MetadataEngine<Car> metadataEngine = cars.getMetadataEngine();

        // Access metadata for Car.MANUFACTURER attribute.
        SortedAttributeMetadata<String, Car> attributeMetadata = metadataEngine.getSortedAttributeMetadata(Car.MANUFACTURER);

        // Call SortedAttributeMetadata.getKeysAndValuesDescending() to retrieve keys and values in descending order.
        // We retrieve into a List in order to assert the ordering of values returned...
        assertEquals(
                asList(keyValue("Toyota", car2), keyValue("Honda", car3), keyValue("Ford", car1)),
                attributeMetadata.getKeysAndValuesDescending().collect(toList())
        );

        // Test specifying range explicitly...
        assertEquals(
                asList(keyValue("Honda", car3), keyValue("Ford", car1)),
                attributeMetadata.getKeysAndValuesDescending("Alpha", true, "Toyota", false).collect(toList())
        );
    }
}