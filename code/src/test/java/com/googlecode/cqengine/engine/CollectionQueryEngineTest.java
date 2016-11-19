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

public class CollectionQueryEngineTest {

    @Test(expected = IllegalStateException.class)
    public void testAddIndex_ArgumentValidation1() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), noQueryOptions());

        queryEngine.addIndex(null, noQueryOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddIndex_ArgumentValidation2() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), noQueryOptions());

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
    public void testAddDuplicateStandingQueryIndex() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), noQueryOptions());

        queryEngine.addIndex(StandingQueryIndex.onQuery(QueryFactory.has(Car.CAR_ID)), noQueryOptions());
        queryEngine.addIndex(StandingQueryIndex.onQuery(QueryFactory.has(Car.CAR_ID)), noQueryOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateCompoundIndex() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), noQueryOptions());

        queryEngine.addIndex(CompoundIndex.onAttributes(Car.MANUFACTURER, Car.MODEL), noQueryOptions());
        queryEngine.addIndex(CompoundIndex.onAttributes(Car.MANUFACTURER, Car.MODEL), noQueryOptions());
    }

    @Test
    public void testIsMutable() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.put(Persistence.class, OnHeapPersistence.withoutPrimaryKey());
        queryEngine.init(emptyObjectStore(), queryOptions);

        Assert.assertTrue(queryEngine.isMutable());
        queryEngine.addIndex(createImmutableIndex(), noQueryOptions());
        Assert.assertFalse(queryEngine.isMutable());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureMutable() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), noQueryOptions());

        queryEngine.addIndex(createImmutableIndex(), noQueryOptions());
        queryEngine.addAll(ObjectSet.fromCollection(Collections.singleton(CarFactory.createCar(1))), noQueryOptions());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateIndex() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(emptyObjectStore(), noQueryOptions());

        queryEngine.addIndex(HashIndex.onAttribute(Car.MANUFACTURER), noQueryOptions());
        queryEngine.addIndex(HashIndex.onAttribute(Car.MANUFACTURER), noQueryOptions());
    }

    @Test
    public void testAddNonDuplicateIndex() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.put(Persistence.class, OnHeapPersistence.withoutPrimaryKey());
        queryEngine.init(emptyObjectStore(), queryOptions);

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