package com.googlecode.cqengine.functional;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationStrategy;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.testutil.Car.COLOR;
import static com.googlecode.cqengine.testutil.Car.Color.BLUE;
import static com.googlecode.cqengine.testutil.Car.MANUFACTURER;
import static org.junit.Assert.assertEquals;
/**
 * @author Niall Gallagher
 */
public class DeduplicationTest {

    @Test
    public void testDeduplication_Materialize() {
        IndexedCollection<Car> cars = CQEngine.newInstance();
        cars.add(new Car(1, "Ford", "Focus", BLUE, 5, 1000.0));

        Query<Car> query = or(
                equal(COLOR, BLUE),
                equal(MANUFACTURER, "Ford")
        );
        ResultSet<Car> results;
        results = cars.retrieve(query);
        assertEquals(2, results.size());

        QueryOption<Car> deduplicate = deduplicate(DeduplicationStrategy.MATERIALIZE);
        results = cars.retrieve(query, queryOptions(deduplicate));
        assertEquals(1, results.size());
    }

    @Test
    public void testDeduplication_Logical() {
        IndexedCollection<Car> cars = CQEngine.newInstance();
        cars.add(new Car(1, "Ford", "Focus", BLUE, 5, 1000.0));

        Query<Car> query = or(
                equal(COLOR, BLUE),
                equal(MANUFACTURER, "Ford")
        );
        ResultSet<Car> results;
        results = cars.retrieve(query);
        assertEquals(2, results.size());

        QueryOption<Car> deduplicate = deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION);
        results = cars.retrieve(query, queryOptions(deduplicate));
        assertEquals(1, results.size());
    }
}
