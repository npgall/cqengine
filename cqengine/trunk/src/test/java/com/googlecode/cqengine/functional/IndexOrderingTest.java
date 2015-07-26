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
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addAll(CarFactory.createCollectionOfCars(100000));

        cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));

        QueryLog queryLog = new QueryLog(System.out);

        ResultSet<Car> resultSet = cars.retrieve(has(Car.CAR_ID), queryOptions(
                orderBy(descending(Car.CAR_ID)),
                queryLog,
                applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0))
        ));
        {
            long start = System.currentTimeMillis();
            int count = IteratorUtil.countElements(resultSet);
            long timeTaken = System.currentTimeMillis() - start;
            System.out.println("Time taken for " + count + " results: " + timeTaken);
        }
        System.out.println();
        {
            long start = System.currentTimeMillis();
            int count = IteratorUtil.countElements(resultSet);
            long timeTaken = System.currentTimeMillis() - start;
            System.out.println("Time taken for " + count + " results: " + timeTaken);
        }
    }
}
