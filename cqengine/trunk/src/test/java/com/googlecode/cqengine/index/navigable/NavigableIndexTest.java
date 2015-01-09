package com.googlecode.cqengine.index.navigable;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.common.NavigableMapBasedIndex;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import junit.framework.Assert;
import org.junit.Test;

import java.util.*;

import static com.googlecode.cqengine.testutil.TestUtil.setOf;

/**
 * Created by niall.gallagher on 09/01/2015.
 */
public class NavigableIndexTest {

    @Test
    public void testGetDistinctKeysAndCounts() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        NavigableMapBasedIndex<String, Car> MODEL_INDEX = NavigableIndex.onAttribute(Car.MODEL);
        collection.addIndex(MODEL_INDEX);

        collection.addAll(CarFactory.createCollectionOfCars(20));

        SortedSet<String> distinctModels = MODEL_INDEX.getDistinctKeys();
        Assert.assertEquals(setOf("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus"), distinctModels);
        for (String model : distinctModels) {
            Assert.assertEquals(Integer.valueOf(2), MODEL_INDEX.getCountForKey(model));
        }

        SortedSet<String> distinctModelsDescending = MODEL_INDEX.getDistinctKeysDescending();
        Assert.assertEquals(Arrays.asList("Taurus", "Prius", "M6", "Insight", "Hilux", "Fusion", "Focus", "Civic", "Avensis", "Accord"), new ArrayList<String>(distinctModelsDescending));
    }
}
