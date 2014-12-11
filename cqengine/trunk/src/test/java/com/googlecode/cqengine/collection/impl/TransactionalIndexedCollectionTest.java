package com.googlecode.cqengine.collection.impl;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.googlecode.cqengine.testutil.CarFactory.createCar;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * @author Niall Gallagher
 */
public class TransactionalIndexedCollectionTest {

    @Test
    public void testWritePath() {
        TransactionalIndexedCollection<Car> collection = CQEngine.newTransactionalInstance(Car.class);
        // Version number initially starts at 1...
        assertEquals(1, collection.currentVersion);
        assertEquals(1, collection.versions.size());
        TransactionalIndexedCollection.Version v1 = collection.versions.get(1L);
        // Adding objects to the collection should cause version number to be incremented twice...
        collection.addAll(asSet(createCar(1), createCar(2), createCar(3), createCar(4)));
        assertEquals(4, collection.size());
        assertEquals(asSet(createCar(1), createCar(2), createCar(3), createCar(4)), collection);
        assertEquals(3, collection.currentVersion);
        // Previous versions should have been removed...
        assertEquals(1, collection.versions.size());
        // Each version should have a different object to hold its state...
        TransactionalIndexedCollection.Version v3 = collection.versions.get(3L);
        assertNotSame(v1, v3);

        // Removing objects from the collection should cause version number to be incremented twice...
        collection.removeAll(asSet(createCar(2), createCar(3)));
        assertEquals(2, collection.size());
        assertEquals(asSet(createCar(1), createCar(4)), collection);
        assertEquals(5, collection.currentVersion);
        // Previous versions should have been removed...
        assertEquals(1, collection.versions.size());

        // Replacing objects in the collection should cause version number to be incremented thrice...
        collection.applyTransaction(asSet(createCar(5), createCar(6)), asSet(createCar(4)));
        assertEquals(3, collection.size());
        assertEquals(asSet(createCar(1), createCar(5), createCar(6)), collection);
        assertEquals(8, collection.currentVersion);
        // Previous versions should have been removed...
        assertEquals(1, collection.versions.size());

        // Replacing no objects should not cause version number to change...
        collection.applyTransaction(Collections.<Car>emptySet(), Collections.<Car>emptySet());
        assertEquals(8, collection.currentVersion);
    }


    static <O> Set<O> asSet(O... objects) {
        return new LinkedHashSet<O>(asList(objects));
    }
}
