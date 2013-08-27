package com.googlecode.cqengine.query;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.join.Garage;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Assert;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author ngallagher
 * @since 2013-08-27 12:09
 */
public class QueryToStringTest {

    @Test
    public void testQueryToString1() {
        Query<Car> query = and(
                equal(Car.MANUFACTURER, "Toyota"),
                equal(Car.COLOR, Car.Color.BLUE),
                equal(Car.DOORS, 3)
        );
        Assert.assertEquals(
                "and(equal(Car.manufacturer, Toyota), equal(Car.color, BLUE), equal(Car.doors, 3))",
                query.toString()
        );
    }

    @Test
    public void testQueryToString2() {
        IndexedCollection<Garage> garages = CQEngine.newInstance();
        Query<Car> query = and(
                in(Car.DOORS, 2, 4),
                existsIn(garages,
                        Car.MANUFACTURER,
                        Garage.BRANDS_SERVICED,
                        equal(Garage.LOCATION, "Dublin")
                )
        );
        // TODO: toString for JOINs could be improved...
        Assert.assertEquals(
                "and(or(equal(Car.doors, 2), equal(Car.doors, 4)), equal(Car.existsInForeignCollection_with_restriction, true))",
                query.toString()
        );
    }
}
