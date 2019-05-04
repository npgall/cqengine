/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.engine;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.persistence.support.ConcurrentOnHeapObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.persistence.wrapping.WrappingPersistence;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import com.googlecode.cqengine.testutil.IterationCountingSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.resultset.iterator.IteratorUtil.countElements;

public class CollectionQueryEngineTest {

    @Test(expected = IllegalStateException.class)
    public void testAddIndex_ArgumentValidation1() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        queryEngine.addIndex(null, noQueryOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnexpectedQueryTye() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        queryEngine.retrieveRecursive(new Query<Car>() {
            @Override
            public boolean matches(Car object, QueryOptions queryOptions) {
                return false;
            }
        }, noQueryOptions());
    }

    @Test
    public void testGetClassName() throws Exception {
        Assert.assertEquals(CollectionQueryEngineTest.class.getName(), CollectionQueryEngine.getClassNameNullSafe(this));
        Assert.assertNull(CollectionQueryEngine.getClassNameNullSafe(null));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateStandingQueryIndex() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        queryEngine.addIndex(StandingQueryIndex.onQuery(QueryFactory.has(Car.CAR_ID)), noQueryOptions());
        queryEngine.addIndex(StandingQueryIndex.onQuery(QueryFactory.has(Car.CAR_ID)), noQueryOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateCompoundIndex() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        queryEngine.addIndex(CompoundIndex.onAttributes(Car.MANUFACTURER, Car.MODEL), noQueryOptions());
        queryEngine.addIndex(CompoundIndex.onAttributes(Car.MANUFACTURER, Car.MODEL), noQueryOptions());
    }

    @Test
    public void testIsMutable() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        Assert.assertTrue(queryEngine.isMutable());
        queryEngine.addIndex(createImmutableIndex(), noQueryOptions());
        Assert.assertFalse(queryEngine.isMutable());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureMutable() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        queryEngine.addIndex(createImmutableIndex(), noQueryOptions());
        queryEngine.addAll(ObjectSet.fromCollection(Collections.singleton(CarFactory.createCar(1))), noQueryOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateIndex() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        queryEngine.addIndex(HashIndex.onAttribute(Car.MANUFACTURER), noQueryOptions());
        queryEngine.addIndex(HashIndex.onAttribute(Car.MANUFACTURER), noQueryOptions());
    }

    @Test
    public void testAddNonDuplicateIndex() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        queryEngine.addIndex(HashIndex.onAttribute(Car.MANUFACTURER), noQueryOptions());
        queryEngine.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER), noQueryOptions());
    }

    @Test
    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "StatementWithEmptyBody"})
    public void testOrQueryCollectionScan() {
        IterationCountingSet<Car> iterationCountingSet = new IterationCountingSet<Car>(CarFactory.createCollectionOfCars(10));
        IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>(WrappingPersistence.aroundCollection(iterationCountingSet));

        Query<Car> query = or(equal(Car.COLOR, Car.Color.BLUE), equal(Car.MANUFACTURER, "Honda"));
        ResultSet<Car> resultSet = collection.retrieve(query);

        for (Car car : resultSet) {
            // No op
        }

        // The two-branch or() query should have been evaluated by scanning the collection only once...
        Assert.assertEquals(iterationCountingSet.size(), iterationCountingSet.getItemsIteratedCount());
    }

    @Test
    public void testRemoveIndex() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        HashIndex<String, Car> index1 = HashIndex.onAttribute(Car.MANUFACTURER);
        queryEngine.addIndex(index1, noQueryOptions());

        UniqueIndex<Integer, Car> index2 = UniqueIndex.onAttribute(Car.CAR_ID);
        queryEngine.addIndex(index2, noQueryOptions());

        StandingQueryIndex<Car> index3 = StandingQueryIndex.onQuery(equal(Car.MODEL, "Focus"));
        queryEngine.addIndex(index3, noQueryOptions());

        CompoundIndex<Car> index4 = CompoundIndex.onAttributes(Car.MANUFACTURER, Car.MODEL);
        queryEngine.addIndex(index4, noQueryOptions());

        HashIndex<Boolean, Car> index5 = HashIndex.onAttribute(forStandingQuery(equal(Car.MANUFACTURER, "Ford")));
        queryEngine.addIndex(index5, noQueryOptions());

        Assert.assertEquals(5, countElements(queryEngine.getIndexes()));

        queryEngine.removeIndex(index1, noQueryOptions());
        Assert.assertEquals(4, countElements(queryEngine.getIndexes()));

        queryEngine.removeIndex(index2, noQueryOptions());
        Assert.assertEquals(3, countElements(queryEngine.getIndexes()));

        queryEngine.removeIndex(index3, noQueryOptions());
        Assert.assertEquals(2, countElements(queryEngine.getIndexes()));

        queryEngine.removeIndex(index4, noQueryOptions());
        Assert.assertEquals(1, countElements(queryEngine.getIndexes()));

        queryEngine.removeIndex(index5, noQueryOptions());
        Assert.assertEquals(0, countElements(queryEngine.getIndexes()));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveIndex_ArgumentValidation1() {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), queryOptionsWithOnHeapPersistence());

        queryEngine.removeIndex(null, noQueryOptions());
    }

    static QueryOptions queryOptionsWithOnHeapPersistence() {
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.put(Persistence.class, OnHeapPersistence.withoutPrimaryKey());
        return queryOptions;
    }

    static ObjectStore<Car> emptyObjectStore() {
        return new ConcurrentOnHeapObjectStore<Car>();
    }

    static HashIndex<Integer, Car> createImmutableIndex() {
        HashIndex.DefaultIndexMapFactory<Integer, Car> defaultIndexMapFactory = new HashIndex.DefaultIndexMapFactory<Integer, Car>();
        HashIndex.DefaultValueSetFactory<Car> defaultValueSetFactory = new HashIndex.DefaultValueSetFactory<Car>();
        return new HashIndex<Integer, Car>(defaultIndexMapFactory, defaultValueSetFactory, Car.CAR_ID ) {
            @Override
            public boolean isMutable() {
                return false;
            }
        };
    }
}