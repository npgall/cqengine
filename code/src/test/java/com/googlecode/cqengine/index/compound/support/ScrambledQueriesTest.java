package com.googlecode.cqengine.index.compound.support;

import com.google.common.collect.ImmutableList;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * @author Eduardo Barrios
 */
public class ScrambledQueriesTest {

    private final IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();

    @Before
    public void setUp() {
        // add the index we want to test
        cars.addIndex(CompoundIndex.onAttributes(Car.CAR_ID, Car.NAME, Car.DESCRIPTION, Car.FEATURES));

        // Add some objects to the collection...
        cars.add(new Car(1, "ford", "focus", ImmutableList.of("fwd", "hatchback", "black")));
        cars.add(new Car(2, "honda", "civic", ImmutableList.of("fwd", "sedan", "silver")));
        cars.add(new Car(3, "toyota", "prius", ImmutableList.of("hybrid", "fwd", "blue")));
        cars.add(new Car(4, "ford", "explorer", ImmutableList.of("awd", "green")));
        cars.add(new Car(5, "honda", "crv", ImmutableList.of("awd", "black")));
        cars.add(new Car(6, "toyota", "tundra", ImmutableList.of("4x4", "red")));
    }

    @Test
    public void testUnscrambleQuery() {

        Query<Car> scrambledQuery = and(
                equal(Car.NAME, "ford"),
                equal(Car.FEATURES, "black"),
                equal(Car.CAR_ID, 1),
                equal(Car.DESCRIPTION, "focus")
        );
        ResultSet<Car> flatQueryResult = cars.retrieve(scrambledQuery);
        Assert.assertEquals(1, flatQueryResult.size());
        Assert.assertEquals(20, flatQueryResult.getRetrievalCost());
    }

    @Test
    public void testFlattenQuery() {
        And<Car> nestedQuery = and(
                equal(Car.CAR_ID, 1),
                and(
                        equal(Car.NAME, "ford"),
                        and(
                                equal(Car.DESCRIPTION, "focus"),
                                equal(Car.FEATURES, "black")
                        )
                )
        );

        ResultSet<Car> nestedQueryResult = cars.retrieve(nestedQuery);
        Assert.assertEquals(1, nestedQueryResult.size());
        Assert.assertEquals(20, nestedQueryResult.getRetrievalCost());

    }
}