package com.googlecode.cqengine.benchmark.tasks;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.benchmark.BenchmarkTask;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.ascending;
import static com.googlecode.cqengine.query.QueryFactory.orderBy;
import static com.googlecode.cqengine.query.QueryFactory.queryOptions;

public class MaterializedOrder_CardId  implements BenchmarkTask {
    private Collection<Car> collection;
    private IndexedCollection<Car> indexedCollection;

    private final Comparator<Car> carIdComparator = Comparator.comparingInt(Car::getCarId);

    private final Query<Car> query = equal(Car.MODEL, "Focus");

    private final QueryOptions queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)));
    @Override
    public void init(final Collection<Car> collection) {
        this.collection = collection;
        IndexedCollection<Car> indexedCollection = new ConcurrentIndexedCollection<Car>();
        indexedCollection.addAll(collection);
        this.indexedCollection = indexedCollection;
        this.indexedCollection.addIndex(HashIndex.onAttribute(Car.MODEL));
    }

    /**
     * Uses iteration with insertion sort.
     */
    @Override
    public int runQueryCountResults_IterationNaive() {
        final TreeSet<Car> result = new TreeSet<>(carIdComparator);
        for (final Car car : collection) {
            if (car.getModel().equals("Focus")) {
                result.add(car);
            }
        }
        return result.size();
    }

    /**
     * Uses iteration with merge sort.
     */
    @Override
    public int runQueryCountResults_IterationOptimized() {
        final List<Car> result = new ArrayList<>();
        for (final Car car : collection) {
            if (car.getModel().equals("Focus")) {
                result.add(car);
            }
        }
        result.sort(carIdComparator);
        return result.size();
    }

    @Override
    public int runQueryCountResults_CQEngine() {
        final ResultSet<Car> sortedResult = indexedCollection.retrieve(query, queryOptions);
        return BenchmarkTaskUtil.countResultsViaIteration(sortedResult);
    }

    @Override
    public int runQueryCountResults_CQEngineStatistics() {
        final ResultSet<Car> result = indexedCollection.retrieve(query, queryOptions);
        return result.size();
    }
}
