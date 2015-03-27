package com.googlecode.cqengine.testutil;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.persistence.disk.DiskPersistence;

/**
 * For testing purposes - a {@link ConcurrentIndexedCollection} hard-wired to use disk persistence.
 *
 * @author niall.gallagher
 */
public class DiskConcurrentIndexedCollection extends ConcurrentIndexedCollection<Car> {

    public DiskConcurrentIndexedCollection() {
        super(DiskPersistence.onPrimaryKey(Car.CAR_ID));
    }
}
