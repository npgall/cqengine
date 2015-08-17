package com.googlecode.cqengine.functional;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.option.EngineThresholds;
import com.googlecode.cqengine.query.option.QueryLog;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.option.EngineThresholds.*;

/**
 * TODO - remove this temporary test (functionality is tested in IndexedCollectionFunctionalTest).
 *
 * Created by npgall on 27/07/2015.
 */
public class IndexOrderingTest {

    public static void main(String[] args) {
        final int NUM_ITERATIONS = 1000;
        final int[] numObjects = {10000, 10000, 100000};
        final double[] selectivityThreshold = {0.0, 0.5, 1.0};

        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addAll(CarFactory.createCollectionOfCars(1000000));

        cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
        cars.addIndex(NavigableIndex.onAttribute(Car.COLOR));

        for (int n : numObjects) {
            for (double s : selectivityThreshold) {
                long start = System.currentTimeMillis();
                long count = 0;
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    count = countRetrievedResults(cars, n, s);
                }
                long timeTaken = System.currentTimeMillis() - start;
                System.out.println("Number: " + n + ", selectivity threshold: " + s + ", time taken per iteration: " + (timeTaken / (double)NUM_ITERATIONS) + " (count=" + count + ")");
            }
        }
    }

    static long countRetrievedResults(IndexedCollection<Car> cars, int numObjects, double indexOrderingSelectivityThreshold) {
        ResultSet<Car> resultSet = cars.retrieve(and(lessThan(Car.CAR_ID, numObjects), equal(Car.COLOR, Car.Color.BLUE)), queryOptions(
                orderBy(descending(Car.CAR_ID)),
                applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, indexOrderingSelectivityThreshold))
        ));
        int count = 0;
        for (Car c : resultSet) {
            count++;
            if (count>= 10) {
                break;
            }
        }
        return count;
    }
}
