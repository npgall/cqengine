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
package com.googlecode.cqengine.persistence.support;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Iterator;

/**
 * Created by npgall on 01/03/2016.
 */
public class ObjectStoreResultSet<O> extends ResultSet<O> {

    final ObjectStore<O> objectStore;
    final Query<O> query;
    final QueryOptions queryOptions;
    final int retrievalCost;
    final ObjectSet<O> objectSet;

    public ObjectStoreResultSet(ObjectStore<O> objectStore, Query<O> query, QueryOptions queryOptions, int retrievalCost) {
        this.objectStore = objectStore;
        this.query = query;
        this.queryOptions = queryOptions;
        this.retrievalCost = retrievalCost;
        this.objectSet = ObjectSet.fromObjectStore(objectStore, queryOptions);
    }

    @Override
    public Iterator<O> iterator() {
        return objectSet.iterator();
    }

    @Override
    public boolean contains(O object) {
        return objectStore.contains(object, queryOptions);
    }

    @Override
    public boolean matches(O object) {
        return query.matches(object, queryOptions);
    }

    @Override
    public Query<O> getQuery() {
        return query;
    }

    @Override
    public QueryOptions getQueryOptions() {
        return queryOptions;
    }

    @Override
    public int getRetrievalCost() {
        return retrievalCost;
    }

    @Override
    public int getMergeCost() {
        return size();
    }

    @Override
    public int size() {
        return objectStore.size(queryOptions);
    }

    @Override
    public void close() {
        objectSet.close();
    }
}
