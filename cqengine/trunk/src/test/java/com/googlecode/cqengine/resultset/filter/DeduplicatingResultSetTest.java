package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.IndexedCollectionFunctionalTest;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;


public class DeduplicatingResultSetTest {

    @Test
    public void testDeduplicatingResultSet() {
        Collection<Car> cars = CarFactory.createCollectionOfCars(10);

        DeduplicatingResultSet<Car, String> deduplicatingResultSet = new DeduplicatingResultSet<Car, String>(
                Car.MANUFACTURER,
                new StoredSetBasedResultSet<Car>(new LinkedHashSet<Car>(cars)),
                QueryOptions.noQueryOptions()
        );
        List<Integer> carIdsReturned = new ArrayList<Integer>();
        IndexedCollectionFunctionalTest.extractCarIds(deduplicatingResultSet, carIdsReturned);

        // Should return the first distinct Manufacturer...
        Assert.assertEquals(4, deduplicatingResultSet.size());
        Assert.assertEquals(0, deduplicatingResultSet.getRetrievalCost());
        Assert.assertEquals(10, deduplicatingResultSet.getMergeCost());
        Assert.assertEquals(4, carIdsReturned.size());
        Assert.assertEquals(0, carIdsReturned.get(0).intValue());
        Assert.assertEquals(3, carIdsReturned.get(1).intValue());
        Assert.assertEquals(6, carIdsReturned.get(2).intValue());
        Assert.assertEquals(9, carIdsReturned.get(3).intValue());

        Assert.assertTrue(deduplicatingResultSet.contains(CarFactory.createCar(0)));
        Assert.assertTrue(deduplicatingResultSet.contains(CarFactory.createCar(3)));
        Assert.assertTrue(deduplicatingResultSet.contains(CarFactory.createCar(6)));
        Assert.assertTrue(deduplicatingResultSet.contains(CarFactory.createCar(9)));
        Assert.assertFalse(deduplicatingResultSet.contains(CarFactory.createCar(1)));

        deduplicatingResultSet.close(); // No op.
    }
}