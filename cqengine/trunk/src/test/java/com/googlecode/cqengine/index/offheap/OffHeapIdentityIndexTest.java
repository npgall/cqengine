package com.googlecode.cqengine.index.offheap;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.offheap.TemporaryDatabase.TemporaryInMemoryDatabase;
import com.googlecode.cqengine.index.offheap.support.ConnectionManager;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static org.junit.Assert.*;

public class OffHeapIdentityIndexTest {

    @Rule
    public TemporaryInMemoryDatabase temporaryDatabase = new TemporaryInMemoryDatabase();

    @Test
    public void testSerialization() {
        OffHeapIdentityIndex<Integer, Car> index = new OffHeapIdentityIndex<Integer, Car>(
                Car.CAR_ID,
                temporaryDatabase.getConnectionManager(true)
        );

        SimpleAttribute<Car, byte[]> serializingAttribute = index.new SerializingAttribute(Car.class, byte[].class);
        SimpleAttribute<byte[], Car> deserializingAttribute = index.new DeserializingAttribute(byte[].class, Car.class);

        Car input = CarFactory.createCar(1);
        byte[] serialized = serializingAttribute.getValue(input, noQueryOptions());
        Car output = deserializingAttribute.getValue(serialized, noQueryOptions());
        Assert.assertEquals(input, output);
    }

    @Test
    public void testNestedContains() {
        ConnectionManager connectionManager = temporaryDatabase.getConnectionManager(true);
        OffHeapIdentityIndex<Integer, Car> index = new OffHeapIdentityIndex<Integer, Car>(
                Car.CAR_ID,
                connectionManager
        );
        index.notifyObjectsAdded(CarFactory.createCollectionOfCars(10), noQueryOptions());
        ResultSet<Car> rs1 = index.retrieve(between(Car.CAR_ID, 3, 5), noQueryOptions());
        ResultSet<Car> rs2 = index.retrieve(between(Car.CAR_ID, 4, 6), noQueryOptions());


        assertTrue(rs1.contains(CarFactory.createCar(3)));
        assertFalse(rs2.contains(CarFactory.createCar(3)));

        assertTrue(rs1.contains(CarFactory.createCar(4)));
        assertTrue(rs2.contains(CarFactory.createCar(4)));

        assertTrue(rs1.contains(CarFactory.createCar(5)));
        assertTrue(rs2.contains(CarFactory.createCar(5)));

        assertFalse(rs1.contains(CarFactory.createCar(6)));
        assertTrue(rs2.contains(CarFactory.createCar(6)));

        try {
            for (Car car : rs1) {
                if (car.getCarId() == 4) {
                    assertTrue("RS1 should contain " + car, rs1.contains(car));
                    assertTrue("RS2 should contain " + car, rs2.contains(car));
                }
            }
        }
        finally {
            rs1.close();
        }
    }

}