package com.googlecode.cqengine.index.sqlite;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.sqlite.TemporaryDatabase.TemporaryInMemoryDatabase;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.*;

public class SQLiteIdentityIndexTest {

    @Rule
    public TemporaryInMemoryDatabase temporaryDatabase = new TemporaryInMemoryDatabase();

    @Test
    public void testSerialization() {
        SQLiteIdentityIndex<Integer, Car> index = new SQLiteIdentityIndex<Integer, Car>(
                Car.CAR_ID,
                temporaryDatabase.getConnectionManager(true)
        );

        SimpleAttribute<Car, byte[]> serializingAttribute = index.new SerializingAttribute(Car.class, byte[].class);
        SimpleAttribute<byte[], Car> deserializingAttribute = index.new DeserializingAttribute(byte[].class, Car.class);

        Car c1 = CarFactory.createCar(1);
        byte[] s1 = serializingAttribute.getValue(c1, noQueryOptions());
        Car c2 = deserializingAttribute.getValue(s1, noQueryOptions());
        byte[] s2 = serializingAttribute.getValue(c2, noQueryOptions());
        Assert.assertEquals(c1, c2);
        Assert.assertArrayEquals(s1, s2);
    }

}