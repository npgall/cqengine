package com.googlecode.cqengine.index.levenstein;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.levensteinDistance;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class LevensteinTest {

    @Test(expected = IllegalStateException.class)
    public void testImmutable() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addIndex(LevensteinDistanceIndex.onAttribute(Car.MODEL));
        collection.addAll(CarFactory.createCollectionOfCars(10));
    }

    @Test
    public void testQuery() {
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>();
        collection.addAll(CarFactory.createCollectionOfCars(10));
        collection.addIndex(LevensteinDistanceIndex.onAttribute(Car.MANUFACTURER));
        assertEquals(3, collection.retrieve(levensteinDistance(Car.MANUFACTURER, "Frd", 1)).size());
    }
}
