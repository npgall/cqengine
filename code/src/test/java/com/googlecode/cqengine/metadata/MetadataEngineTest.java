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

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;
import org.mockito.Mockito;

import static com.googlecode.cqengine.metadata.AttributeMetadataTest.createIndexedCollectionOfCars;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link MetadataEngine}.
 */
public class MetadataEngineTest {

    @Test
    public void testAttemptToAccessMetadataWithoutIndex() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(5);
        @SuppressWarnings("unchecked") Attribute<Car, Integer> ATTRIBUTE = Mockito.mock(SimpleAttribute.class);
        Mockito.when(ATTRIBUTE.toString()).thenReturn("ATTRIBUTE");
        IllegalStateException expected = null;
        try {
            cars.getMetadataEngine().getAttributeMetadata(ATTRIBUTE);
        }
        catch (IllegalStateException e) {
            expected = e;
        }
        assertNotNull(expected);
        assertEquals("A KeyStatisticsAttributeIndex has not been added to the collection, and must be added first, to enable metadata to be examined for attribute: ATTRIBUTE", expected.getMessage());
    }

    @Test
    public void testAttemptToAccessMetadataWithIndexOnDifferentAttribute() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(5);
        // Add an index on a different attribute...
        cars.addIndex(HashIndex.onAttribute(Car.MODEL));
        // Create a mock (different) attribute we will query...
        @SuppressWarnings("unchecked") Attribute<Car, Integer> ATTRIBUTE = Mockito.mock(SimpleAttribute.class);
        Mockito.when(ATTRIBUTE.toString()).thenReturn("ATTRIBUTE");
        IllegalStateException expected = null;
        try {
            cars.getMetadataEngine().getAttributeMetadata(ATTRIBUTE);
        }
        catch (IllegalStateException e) {
            expected = e;
        }
        assertNotNull(expected);
        assertEquals("A KeyStatisticsAttributeIndex has not been added to the collection, and must be added first, to enable metadata to be examined for attribute: ATTRIBUTE", expected.getMessage());
    }

    @Test
    public void testAttemptToAccessMetadataWithoutSortedIndex() {
        IndexedCollection<Car> cars = createIndexedCollectionOfCars(5);
        @SuppressWarnings("unchecked") Attribute<Car, Integer> ATTRIBUTE = Mockito.mock(SimpleAttribute.class);
        Mockito.when(ATTRIBUTE.toString()).thenReturn("ATTRIBUTE");
        IllegalStateException expected = null;
        try {
            cars.getMetadataEngine().getSortedAttributeMetadata(ATTRIBUTE);
        }
        catch (IllegalStateException e) {
            expected = e;
        }
        assertNotNull(expected);
        assertEquals("A SortedKeyStatisticsAttributeIndex has not been added to the collection, and must be added first, to enable metadata to be examined for attribute: ATTRIBUTE", expected.getMessage());
    }
}