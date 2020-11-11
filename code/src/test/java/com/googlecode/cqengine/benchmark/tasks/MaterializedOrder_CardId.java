package com.googlecode.cqengine.benchmark.tasks;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.benchmark.BenchmarkTask;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static com.googlecode.cqengine.query.QueryFactory.all;
import static com.googlecode.cqengine.query.QueryFactory.ascending;
import static com.googlecode.cqengine.query.QueryFactory.orderBy;
import static com.googlecode.cqengine.query.QueryFactory.queryOptions;

public class MaterializedOrder_CardId  implements BenchmarkTask {
    private Collection<Car> collection;
    private IndexedCollection<Car> indexedCollection;

    private final Comparator<Car> carIdComparator = Comparator.comparingInt(Car::getCarId);

    private final Query<Car> query = all(Car.class);
    private final QueryOptions queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)));
    @Override
    public void init(final Collection<Car> collection) {
        this.collection = collection;
        this.indexedCollection = new ConcurrentIndexedCollection<>();

        this.indexedCollection.addAll(collection);
    }

    @Override
    public int runQueryCountResults_IterationNaive() {
        final TreeSet<Car> sortedSet = new TreeSet<>(carIdComparator);
        for (final Car car : collection) {
            sortedSet.add(car);
        }
        final List<Car> result = new ArrayList<>(sortedSet);
        return result.size();
    }

    @Override
    public int runQueryCountResults_IterationOptimized() {
        final List<Car> result = new ArrayList<>(collection.size());
        for (final Car car : collection) {
            result.add(car);
        }
        result.sort(carIdComparator);
        return result.size();
    }

    @Override
    public int runQueryCountResults_CQEngine() {
        final ResultSet<Car> sortedResult = indexedCollection.retrieve(query, queryOptions);
        final List<Car> result = new ArrayList<>();
        for (final Car car : sortedResult) {
            result.add(car);
        }
        return result.size();
    }

    @Override
    public int runQueryCountResults_CQEngineStatistics() {
        final ResultSet<Car> result = indexedCollection.retrieve(query, queryOptions);
        return result.size();
    }
}
