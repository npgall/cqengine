package com.googlecode.cqengine.index.levenshtein;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.levenshteinDistance;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class LevenshteinTest {

    @Test(expected = IllegalStateException.class)
    public void testImmutable() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addIndex(LevenshteinDistanceIndex.onAttribute(Car.MODEL));
        collection.addAll(CarFactory.createCollectionOfCars(10));
    }

    @Test
    public void testQuery() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addAll(CarFactory.createCollectionOfCars(10));
        collection.addIndex(LevenshteinDistanceIndex.onAttribute(Car.MANUFACTURER));
        assertEquals(3, collection.retrieve(levenshteinDistance(Car.MANUFACTURER, "Frd", 1)).size());
    }
}
