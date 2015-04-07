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

import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class CollectionQueryEngineTest {

    @Test(expected = IllegalStateException.class)
    public void testAddIndex_ArgumentValidation1() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(Collections.<Car>emptySet(), QueryFactory.noQueryOptions());

        queryEngine.addIndex(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddIndex_ArgumentValidation2() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(Collections.<Car>emptySet(), QueryFactory.noQueryOptions());

        queryEngine.retrieveRecursive(new Query<Car>() {
            @Override
            public boolean matches(Car object, QueryOptions queryOptions) {
                return false;
            }
        }, QueryFactory.noQueryOptions());
    }

    @Test
    public void testGetClassName() throws Exception {
        Assert.assertEquals(CollectionQueryEngineTest.class.getName(), CollectionQueryEngine.getClassNameNullSafe(this));
        Assert.assertNull(CollectionQueryEngine.getClassNameNullSafe(null));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateStandingQueryIndex() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(Collections.<Car>emptySet(), QueryFactory.noQueryOptions());

        queryEngine.addIndex(StandingQueryIndex.onQuery(QueryFactory.has(Car.CAR_ID)));
        queryEngine.addIndex(StandingQueryIndex.onQuery(QueryFactory.has(Car.CAR_ID)));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateCompoundIndex() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(Collections.<Car>emptySet(), QueryFactory.noQueryOptions());

        queryEngine.addIndex(CompoundIndex.onAttributes(Car.MANUFACTURER, Car.MODEL));
        queryEngine.addIndex(CompoundIndex.onAttributes(Car.MANUFACTURER, Car.MODEL));
    }

    @Test
    public void testIsMutable() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(Collections.<Car>emptySet(), QueryFactory.noQueryOptions());

        Assert.assertTrue(queryEngine.isMutable());
        queryEngine.addIndex(createImmutableIndex());
        Assert.assertFalse(queryEngine.isMutable());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureMutable() throws Exception {
        CollectionQueryEngine<Car> queryEngine = new CollectionQueryEngine<Car>();
        queryEngine.init(Collections.<Car>emptySet(), QueryFactory.noQueryOptions());

        queryEngine.addIndex(createImmutableIndex());
        queryEngine.addAll(Collections.singleton(CarFactory.createCar(1)), QueryFactory.noQueryOptions());
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