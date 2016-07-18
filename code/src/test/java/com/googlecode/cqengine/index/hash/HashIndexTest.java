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
package com.googlecode.cqengine.index.hash;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.support.AbstractMapBasedAttributeIndex;
import com.googlecode.cqengine.index.support.KeyStatistics;
import com.googlecode.cqengine.index.support.KeyStatisticsIndex;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static com.googlecode.cqengine.testutil.TestUtil.setOf;

/**
 * Created by niall.gallagher on 09/01/2015.
 */
public class HashIndexTest {

    @Test
    public void testGetDistinctKeysAndCounts() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        KeyStatisticsIndex<String, Car> MODEL_INDEX = HashIndex.onAttribute(Car.MODEL);
        collection.addIndex(MODEL_INDEX);

        collection.addAll(CarFactory.createCollectionOfCars(20));

        Set<String> distinctModels = setOf(MODEL_INDEX.getDistinctKeys(noQueryOptions()));
        Assert.assertEquals(setOf("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus"), distinctModels);
        for (String model : distinctModels) {
            Assert.assertEquals(Integer.valueOf(2), MODEL_INDEX.getCountForKey(model, noQueryOptions()));
        }
    }

    @Test
    public void testGetCountOfDistinctKeys(){
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        KeyStatisticsIndex<String, Car> MANUFACTURER_INDEX = HashIndex.onAttribute(Car.MANUFACTURER);
        collection.addIndex(MANUFACTURER_INDEX);

        collection.addAll(CarFactory.createCollectionOfCars(20));

        Assert.assertEquals(Integer.valueOf(4), MANUFACTURER_INDEX.getCountOfDistinctKeys(noQueryOptions()));
    }

    @Test
    public void testGetStatisticsForDistinctKeys(){
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        KeyStatisticsIndex<String, Car> MANUFACTURER_INDEX = HashIndex.onAttribute(Car.MANUFACTURER);
        collection.addIndex(MANUFACTURER_INDEX);

        collection.addAll(CarFactory.createCollectionOfCars(20));

        Set<KeyStatistics<String>> keyStatistics = setOf(MANUFACTURER_INDEX.getStatisticsForDistinctKeys(noQueryOptions()));
        Assert.assertEquals(setOf(
                        new KeyStatistics<String>("Ford", 6),
                        new KeyStatistics<String>("Honda", 6),
                        new KeyStatistics<String>("Toyota", 6),
                        new KeyStatistics<String>("BMW", 2)

                ),
                keyStatistics);
    }

    @Test
    public void testOnSemiUniqueAttribute() throws Exception{
        HashIndex<Integer, Car> hashIndex = HashIndex.onSemiUniqueAttribute(Car.CAR_ID);
        // Validate that the HashIndex was configured with CompactValueSetFactory.
        // We have to use reflection to do this
        // because the valueSetFactory has protected access in AbstractMapBasedAttributeIndex.
        // This is a bit hacky, but OTOH we should not break encapsulation of AbstractMapBasedAttributeIndex...
        Field valueSetFactoryField = AbstractMapBasedAttributeIndex.class.getDeclaredField("valueSetFactory");
        valueSetFactoryField.setAccessible(true);
        Assert.assertTrue("HashIndex should be configured with CompactValueSetFactory",
                valueSetFactoryField.get(hashIndex) instanceof HashIndex.CompactValueSetFactory);
    }
}
