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
package com.googlecode.cqengine.persistence.composite;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.disk.DiskIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.offheap.OffHeapIndex;
import com.googlecode.cqengine.index.support.CloseableRequestResources;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.disk.DiskPersistence;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.persistence.support.ConcurrentOnHeapObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author niall.gallagher
 */
public class CompositePersistenceTest {

    /**
     * Tests a configuration where the collection is stored off-heap, one index is on-disk, and one index is on-heap.
     */
    @Test
    public void testCompositePersistence_EndToEnd() {
        OffHeapPersistence<Car, Integer> offHeapPersistence = OffHeapPersistence.onPrimaryKey(Car.CAR_ID);
        DiskPersistence<Car, Integer> diskPersistence = DiskPersistence.onPrimaryKey(Car.CAR_ID);
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>(CompositePersistence.of(
                offHeapPersistence,
                diskPersistence,
                singletonList(OnHeapPersistence.onPrimaryKey(Car.CAR_ID))
        ));

        collection.addIndex(DiskIndex.onAttribute(Car.MANUFACTURER));
        collection.addIndex(OffHeapIndex.onAttribute(Car.MODEL));
        collection.addIndex(NavigableIndex.onAttribute(Car.PRICE));

        collection.addAll(CarFactory.createCollectionOfCars(1000));

        ResultSet<Car> results = null;
        try {
            results = collection.retrieve(
                    and(
                            or(
                                    equal(Car.MANUFACTURER, "Ford"),
                                    equal(Car.MODEL, "Avensis")
                            ),
                            lessThan(Car.PRICE, 6000.0)
                    )
            );
            Assert.assertEquals(300, results.size());

            Assert.assertTrue(offHeapPersistence.getBytesUsed() > 4096); // example: 163840
            Assert.assertTrue(diskPersistence.getBytesUsed() > 4096); // example: 30720
        }
        finally {
            CloseableRequestResources.closeQuietly(results);
            collection.clear();
            offHeapPersistence.close();
            diskPersistence.getFile().delete();
        }
    }

    @Test
    public void testGetPrimaryKeyAttribute() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, noAdditionalPersistences());
        assertEquals(Car.CAR_ID, compositePersistence.getPrimaryKeyAttribute());
    }


    @Test
    public void testSupportsIndex() throws Exception {
        Index<Car> index1 = mockIndex("index1");
        Index<Car> index2 = mockIndex("index2");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence1.supportsIndex(index1)).thenReturn(true);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, noAdditionalPersistences());
        assertTrue(compositePersistence.supportsIndex(index1));
        assertFalse(compositePersistence.supportsIndex(index2));
    }


    @Test
    public void testCreateObjectStore() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        ObjectStore<Car> objectStore = new ConcurrentOnHeapObjectStore<Car>();
        when(persistence1.createObjectStore()).thenReturn(objectStore);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, singletonList(persistence3));
        ObjectStore<Car> result = compositePersistence.createObjectStore();
        assertEquals(objectStore, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPersistenceForIndex_NoPersistence() throws Exception {
        Index<Car> index = mockIndex("index1");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);

        CompositePersistence<Car, Integer> compositePersistence = CompositePersistence.of(persistence1, persistence2, singletonList(persistence3));
        compositePersistence.getPersistenceForIndex(index);
    }

    @Test
    public void testGetPersistenceForIndex_PrimaryPersistence() throws Exception {
        Index<Car> index = mockIndex("index1");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence1.supportsIndex(index)).thenReturn(true);

        CompositePersistence<Car, Integer> compositePersistence = CompositePersistence.of(persistence1, persistence2, singletonList(persistence3));
        Persistence<Car, Integer> result = compositePersistence.getPersistenceForIndex(index);
        assertEquals(persistence1, result);
    }

    @Test
    public void testGetPersistenceForIndex_SecondaryPersistence() throws Exception {
        Index<Car> index = mockIndex("index1");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.supportsIndex(index)).thenReturn(true);

        CompositePersistence<Car, Integer> compositePersistence = CompositePersistence.of(persistence1, persistence2, singletonList(persistence3));
        Persistence<Car, Integer> result = compositePersistence.getPersistenceForIndex(index);
        assertEquals(persistence2, result);
    }

    @Test
    public void testGetPersistenceForIndex_AdditionalPersistence() throws Exception {
        Index<Car> index = mockIndex("index1");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.supportsIndex(index)).thenReturn(true);

        CompositePersistence<Car, Integer> compositePersistence = CompositePersistence.of(persistence1, persistence2, singletonList(persistence3));
        Persistence<Car, Integer> result = compositePersistence.getPersistenceForIndex(index);
        assertEquals(persistence3, result);
    }

    @Test
    public void testValidateBackingPersistences_Success() throws Exception {
        @SuppressWarnings("unchecked")
        Persistence<Car, Integer> persistence1 = mock(Persistence.class);
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);

        CompositePersistence.validatePersistenceArguments(persistence1, persistence1, singletonList(persistence1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateBackingPersistences_NoPrimaryKey() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);

        CompositePersistence.validatePersistenceArguments(persistence1, persistence2, noAdditionalPersistences());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateBackingPersistences_DifferentPrimaryKeys1() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.DOORS);

        CompositePersistence.validatePersistenceArguments(persistence1, persistence2, noAdditionalPersistences());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateBackingPersistences_DifferentPrimaryKeys2() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.DOORS);

        CompositePersistence.validatePersistenceArguments(persistence1, persistence1, singletonList(persistence2));
    }

    @SuppressWarnings("unchecked")
    static Persistence<Car, Integer> mockPersistence(String name) {
        return mock(Persistence.class, name);
    }

    @SuppressWarnings("unchecked")
    static Index<Car> mockIndex(String name) {
        return mock(Index.class, name);
    }

    static List<Persistence<Car, Integer>> noAdditionalPersistences() {
        return Collections.emptyList();
    }
}