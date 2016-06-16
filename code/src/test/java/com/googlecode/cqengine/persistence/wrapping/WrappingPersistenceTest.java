package com.googlecode.cqengine.persistence.wrapping;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.disk.DiskIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.persistence.support.CollectionWrappingObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.googlecode.cqengine.IndexedCollectionFunctionalTest.asSet;
import static com.googlecode.cqengine.IndexedCollectionFunctionalTest.extractCarIds;
import static com.googlecode.cqengine.query.QueryFactory.greaterThan;
import static org.junit.Assert.*;

/**
 * Tests for {@link WrappingPersistence}.
 *
 * @author npgall
 */
public class WrappingPersistenceTest {

    @Test
    public void testWrappingPersistence() {
        Collection<Car> backingCollection = new LinkedHashSet<Car>();
        backingCollection.addAll(CarFactory.createCollectionOfCars(3)); // CarIds 0, 1, 2

        IndexedCollection<Car> indexedCollection = new ConcurrentIndexedCollection<Car>(
                WrappingPersistence.aroundCollection(backingCollection)
        );

        indexedCollection.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));

        ResultSet<Car> results = indexedCollection.retrieve(greaterThan(Car.CAR_ID, 0));

        // Assert that the index will be used...
        assertNotEquals(Integer.MAX_VALUE, results.getRetrievalCost());

        // Assert correct results are returned...
        Set<Integer> expectedCarIds, actualCarIds;
        expectedCarIds = asSet(1, 2);
        actualCarIds = extractCarIds(results, new HashSet<Integer>());
        assertEquals(expectedCarIds, actualCarIds);

        // Add that a new object added to the IndexedCollection...
        indexedCollection.add(CarFactory.createCar(3));

        // Assert the new object was added to the backing collection...
        expectedCarIds = asSet(0, 1, 2, 3);
        actualCarIds = extractCarIds(backingCollection, new HashSet<Integer>());
        assertEquals(expectedCarIds, actualCarIds);
    }

    @Test
    public void testGetPrimaryKeyAttribute() throws Exception {
        WrappingPersistence<Car, Integer> wrappingPersistence =
                WrappingPersistence.aroundCollectionWithPrimaryKey(new HashSet<Car>(), Car.CAR_ID);

        assertEquals(Car.CAR_ID, wrappingPersistence.getPrimaryKeyAttribute());
    }


    @Test
    public void testSupportsIndex() throws Exception {
        WrappingPersistence<Car, Integer> wrappingPersistence =
                WrappingPersistence.aroundCollectionWithPrimaryKey(new HashSet<Car>(), Car.CAR_ID);

        assertTrue(wrappingPersistence.supportsIndex(NavigableIndex.onAttribute(Car.MANUFACTURER)));
        assertFalse(wrappingPersistence.supportsIndex(DiskIndex.onAttribute(Car.MANUFACTURER)));
    }

    @Test
    public void testCreateObjectStore() throws Exception {
        HashSet<Car> backingCollection = new HashSet<Car>();

        WrappingPersistence<Car, Integer> wrappingPersistence =
                WrappingPersistence.aroundCollectionWithPrimaryKey(backingCollection, Car.CAR_ID);

        ObjectStore<Car> objectStore = wrappingPersistence.createObjectStore();

        assertTrue(objectStore instanceof CollectionWrappingObjectStore);
        assertEquals(backingCollection, ((CollectionWrappingObjectStore)objectStore).getBackingCollection());
    }
}