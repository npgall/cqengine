package com.googlecode.cqengine.index.sqlite;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PartialSQLiteIndex}.
 *
 * @author niall.gallagher
 */
public class PartialSQLiteIndexTest {

    public static final SimpleAttribute<Car, Integer> OBJECT_TO_ID = Car.CAR_ID;

    public static final SimpleAttribute<Integer, Car> ID_TO_OBJECT = new SimpleAttribute<Integer, Car>("carFromId") {
        public Car getValue(Integer carId, QueryOptions queryOptions) { return CarFactory.createCar(carId); }
    };

    @Rule
    public TemporaryDatabase.TemporaryInMemoryDatabase temporaryInMemoryDatabase = new TemporaryDatabase.TemporaryInMemoryDatabase();

    @Test
    public void testPartialSQLiteIndex() {
        IndexedCollection<Car> indexedCollection = new ConcurrentIndexedCollection<Car>();
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.put(ConnectionManager.class, connectionManager);
        PartialSQLiteIndex<String, Car, Integer> sqLiteIndex = PartialSQLiteIndex.onAttribute(Car.MANUFACTURER, OBJECT_TO_ID, ID_TO_OBJECT, QueryFactory.between(Car.CAR_ID, 2, 4));
        indexedCollection.addIndex(sqLiteIndex, queryOptions);
        indexedCollection.update(Collections.<Car>emptySet(), CarFactory.createCollectionOfCars(10), queryOptions);

        assertEquals(75,         indexedCollection.retrieve(and(equal(Car.MANUFACTURER, "Ford"), between(Car.CAR_ID, 2, 4)), queryOptions).getRetrievalCost());
        assertEquals(2147483647, indexedCollection.retrieve(and(equal(Car.MANUFACTURER, "Ford"), between(Car.CAR_ID, 2, 5)), queryOptions).getRetrievalCost());
    }
}