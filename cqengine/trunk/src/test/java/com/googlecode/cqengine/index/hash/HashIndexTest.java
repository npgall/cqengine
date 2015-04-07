/**
 * Copyright 2012 Niall Gallagher
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
import com.googlecode.cqengine.index.support.KeyStatisticsIndex;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Test;

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
}
