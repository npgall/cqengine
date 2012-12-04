package com.googlecode.cqengine.indexingbenchmark.task;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.indexingbenchmark.IndexingTask;
import com.googlecode.cqengine.testutil.Car;

import java.util.Collection;

/**
 * @author Niall Gallagher
 */
public class HashIndex_Manufacturer implements IndexingTask {

    private IndexedCollection<Car> indexedCollection;

    @Override
    public void init(Collection<Car> collection) {
        indexedCollection = CQEngine.copyFrom(collection);
    }

    @Override
    public void buildIndex() {
        indexedCollection.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
    }
}
