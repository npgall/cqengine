package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.examples.join.Garage;
import com.googlecode.cqengine.query.Query;
import org.junit.Assert;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.existsIn;

public class ExistsInTest {

    @Test
    public void testToString1() throws Exception {
        IndexedCollection<Garage> garages = new ConcurrentIndexedCollection<Garage>();

        Query<Car> existsIn = existsIn(garages, Car.NAME, Garage.BRANDS_SERVICED);
        Assert.assertEquals("existsIn(IndexedCollection<Garage>, Car.name, Garage.brandsServiced)", existsIn.toString());
    }

    @Test
    public void testToString2() throws Exception {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        IndexedCollection<Garage> garages = new ConcurrentIndexedCollection<Garage>();

        Query<Car> existsIn = existsIn(garages, Car.NAME, Garage.BRANDS_SERVICED, equal(Garage.LOCATION, "Dublin"));
        Assert.assertEquals("existsIn(IndexedCollection<Garage>, Car.name, Garage.brandsServiced, equal(Garage.location, Dublin))", existsIn.toString());
    }
}