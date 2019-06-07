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
package com.googlecode.cqengine.index.standingquery;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.indextype.OnHeapTypeIndex;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An index which stores objects matching any type of query, and which can answer arbitrarily complex
 * queries in constant time complexity.
 * <p/>
 * A standing query index must be added ahead of time for the exact query which is to be accelerated.
 * <p/>
 * Note that it is possible to add a standing query index on any <i>fragment</i> of a query, as well as whole queries.
 * CQEngine will accelerate evaluation of query fragments (branches, or nested sub-trees within queries) using any
 * standing query indexes which match those fragments.
 * <p/>
 *
 * @author Niall Gallagher
 */
public class StandingQueryIndex<O> implements Index<O>, OnHeapTypeIndex {

    private static final int INDEX_RETRIEVAL_COST = 10;

    private final Query<O> standingQuery;

    private final StoredResultSet<O> storedResultSet = new StoredSetBasedResultSet<O>(
            Collections.<O>newSetFromMap(new ConcurrentHashMap<O, Boolean>()),
            INDEX_RETRIEVAL_COST
    );

    public StandingQueryIndex(Query<O> standingQuery) {
        this.standingQuery = standingQuery;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This index is mutable.
     *
     * @return true
     */
    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    public Query<O> getStandingQuery() {
        return standingQuery;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <b>This implementation always returns true, as this index supports all types of query.</b>
     *
     * @return true, this index supports all types of query
     */
    @Override
    public boolean supportsQuery(Query<O> query, QueryOptions queryOptions) {
        return standingQuery.equals(query);
    }


    @Override
    public boolean isQuantized() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(final Query<O> query, QueryOptions queryOptions) {
        if (!supportsQuery(query, queryOptions)) {
            // TODO: check necessary?
            throw new IllegalArgumentException("Unsupported query: " + query);
        }
        return storedResultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ObjectStore<O> objectStore, QueryOptions queryOptions) {
        storedResultSet.clear();
        addAll(ObjectSet.fromObjectStore(objectStore, queryOptions), queryOptions);
    }

    /**
     * This is a no-op for this type of index.
     * @param queryOptions Optional parameters for the update
     */
    @Override
    public void destroy(QueryOptions queryOptions) {
        // No-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        try {
            boolean modified = false;
            for (O object : objectSet) {
                if (standingQuery.matches(object, queryOptions)) {
                    modified |= storedResultSet.add(object);
                }
            }
            return modified;
        }
        finally {
            objectSet.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        try {
            boolean modified = false;
            for (O object : objectSet) {
                if (standingQuery.matches(object, queryOptions)) {
                    modified |= storedResultSet.remove(object);
                }
            }
            return modified;
        }
        finally {
            objectSet.close();
        }
    }

    /**
     * {@inheritDoc}
     * @param queryOptions
     */
    @Override
    public void clear(QueryOptions queryOptions) {
        storedResultSet.clear();
    }

    @Override
    public String toString() {
        return "StandingQueryIndex{" +
                "standingQuery=" + standingQuery +
                '}';
    }

    /**
     * A static factory method for convenience.
     * <p/>
     * Equivalent to {@code new StandingQueryIndex&lt;Query&lt;O&gt;, O&gt;(standingQuery)}.
     * <p/>
     * @param standingQuery The standing query on which the index will be built
     * @param <O> The type of the objects in the collection being indexed
     * @return A new StandingQueryIndex which will build an index on this standing query
     */
    public static <O> StandingQueryIndex<O> onQuery(Query<O> standingQuery) {
        return new StandingQueryIndex<O>(standingQuery);
    }
}
