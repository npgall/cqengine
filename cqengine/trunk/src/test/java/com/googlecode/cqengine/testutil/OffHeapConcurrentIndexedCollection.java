package com.googlecode.cqengine.testutil;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;

/**
 * For testing purposes - a {@link ConcurrentIndexedCollection} hard-wired to use off-heap persistence.
 *
 * @author niall.gallagher
 */
public class OffHeapConcurrentIndexedCollection extends ConcurrentIndexedCollection<Car> {

    public OffHeapConcurrentIndexedCollection() {
        super(OffHeapPersistence.onPrimaryKey(Car.CAR_ID));
    }
}
