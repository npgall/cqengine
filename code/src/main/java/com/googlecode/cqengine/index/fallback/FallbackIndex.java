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
package com.googlecode.cqengine.index.fallback;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.All;
import com.googlecode.cqengine.query.simple.None;
import com.googlecode.cqengine.resultset.filter.FilteringIterator;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;

import java.util.*;

/**
 * A special index which when asked to retrieve data simply scans the underlying collection for matching objects.
 * This index does not maintain any data structure of its own.
 * <p/>
 * This index supports <b>all</b> query types, because it it relies on the supplied query object itself
 * to determine if objects in the collection match the query, by calling
 * {@link Query#matches(Object, com.googlecode.cqengine.query.option.QueryOptions)}.
 * <p/>
 * The query engine automatically uses this <i>fallback</i> index when an attribute is referenced by a query,
 * and no other index has been added for that attribute that supports the query.
 * <p/>
 * The time complexity of retrievals from this fallback index is usually O(n) - linear, proportional to the number of
 * objects in the collection.
 *
 * @author Niall Gallagher
 */
public class FallbackIndex<O> implements Index<O> {

    private static final int INDEX_RETRIEVAL_COST = Integer.MAX_VALUE;
    private static final int INDEX_MERGE_COST = Integer.MAX_VALUE;

    volatile ObjectStore<O> objectStore = null;

    public FallbackIndex() {
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

    /**
     * {@inheritDoc}
     * <p/>
     * <b>This implementation always returns true, as this index supports all types of query.</b>
     *
     * @return true, this index supports all types of query
     */
    @Override
    public boolean supportsQuery(Query<O> query, QueryOptions queryOptions) {
        return true;
    }

    @Override
    public boolean isQuantized() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(final Query<O> query, final QueryOptions queryOptions) {
        final ObjectSet<O> objectSet = ObjectSet.fromObjectStore(objectStore, queryOptions);
        return new ResultSet<O>() {
            @Override
            public Iterator<O> iterator() {
                if (query instanceof All) {
                    return IteratorUtil.wrapAsUnmodifiable(objectSet.iterator());
                }
                else if (query instanceof None) {
                    return Collections.<O>emptyList().iterator();
                }
                else {
                    return new FilteringIterator<O>(objectSet.iterator(), queryOptions) {
                        @Override
                        public boolean isValid(O object, QueryOptions queryOptions) {
                            return query.matches(object, queryOptions);
                        }
                    };
                }
            }
            @Override
            public boolean contains(O object) {
                // Contains is based on objects contained in this *filtered* ResultSet, so delegate to iterator...
                return IteratorUtil.iterableContains(this, object);
            }
            @Override
            public int size() {
                // Size is based on objects contained in this *filtered* ResultSet, so delegate to iterator...
                return IteratorUtil.countElements(this);
            }
            @Override
            public boolean matches(O object) {
                return query.matches(object, queryOptions);
            }
            @Override
            public int getRetrievalCost() {
                // None is a special case where we know it can't match any objects, and therefore retrieval cost is 0...
                return query instanceof None ? 0 : INDEX_RETRIEVAL_COST;
            }
            @Override
            public int getMergeCost() {
                // None is a special case where we know it can't match any objects, and therefore merge cost is 0...
                return query instanceof None ? 0 : INDEX_MERGE_COST;
            }
            @Override
            public void close() {
                objectSet.close();
            }
            @Override
            public Query<O> getQuery() {
                return query;
            }
            @Override
            public QueryOptions getQueryOptions() {
                return queryOptions;
            }
        };
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <b>In this implementation, does nothing.</b>
     */
    @Override
    public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        // No need to take any action
        return false;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <b>In this implementation, does nothing.</b>
     */
    @Override
    public boolean removeAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        // No need to take any action
        return false;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <b>In this implementation, stores a reference to the supplied collection, which the
     * {@link Index#retrieve(com.googlecode.cqengine.query.Query, com.googlecode.cqengine.query.option.QueryOptions)} method can subsequently iterate.</b>
     */
    @Override
    public void init(ObjectStore<O> objectStore, QueryOptions queryOptions) {
        // Store the collection...
        this.objectStore = objectStore;
    }

    /**
     * {@inheritDoc}
     * @param queryOptions
     */
    @Override
    public void clear(QueryOptions queryOptions) {
        objectStore.clear(queryOptions);
    }
}
