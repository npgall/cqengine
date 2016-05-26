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
package com.googlecode.cqengine.index.support;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.engine.CollectionQueryEngine;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.SimpleQuery;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.common.WrappedResultSet;

import java.util.*;

/**
 * An index which indexes and answers queries only on a subset of the collection; a kind of hybrid between a
 * {@link StandingQueryIndex} and a {@link CompoundIndex}.
 * <p/>
 * A partial index wraps a backing index and adds only a subset of objects which match a given <i>filter
 * query</i> to the backing index.
 * <p/>
 * When the partial index is asked if it supports a given query, it will report true only if the query is an {@link And}
 * query with two branches, where one branch is the filter query it was configured with, and the other branch can be
 * handled by the backing index.
 * <p/>
 * As such, a partial index accelerates queries on an "interesting subset" of the collection, without incurring the
 * overhead of indexing the entire collection.
 *
 * @author niall.gallagher
 */
public abstract class PartialIndex<A, O> implements AttributeIndex<A, O> {

    // An integer to add or subtract to the retrieval cost returned by the backing index,
    // so that given a choice between a regular index and a partial index,
    // the retrieval cost from the partial index will be lower and so it will be chosen.
    // This is because partial indexes contain less data which is irrelevant to the query,
    // and so can incur less filtering...
    static final int INDEX_RETRIEVAL_COST_DELTA = -5;

    protected final Query<O> filterQuery;
    protected final Attribute<O, A> attribute;
    protected volatile AttributeIndex<A, O> backingIndex;

    /**
     * Protected constructor, called by subclasses.
     *
     * @param attribute
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     */
    protected PartialIndex(Attribute<O, A> attribute, Query<O> filterQuery) {
        this.attribute = attribute;
        this.filterQuery = filterQuery;
    }

    protected AttributeIndex<A, O> backingIndex() {
        if (backingIndex == null) {
            synchronized (this) { // Double-checked locking to prevent duplicate index creation
                if (backingIndex == null) {
                    backingIndex = createBackingIndex();
                }
            }
        }
        return backingIndex;
    }


    public Attribute<O, A> getAttribute() {
        return backingIndex().getAttribute();
    }

    public Query<O> getFilterQuery() {
        return filterQuery;
    }

    public AttributeIndex<A, O> getBackingIndex() {
        return backingIndex;
    }

    /**
     * Returns true if this partial index can answer this branch of the query.
     * Two conditions must be satisfied:
     * <ol>
     *     <li>The backing index must support the given (branch) query.</li>
     *     <li>The root query (as extracted from query options) must be an And query, and one of its direct children
     *         must be equal to the filter query with which this partial index was configured.</li>
     * </ol>
     * @param query The query supplied by the query engine, which is typically a branch of the overall query being
     *              evaluated.
     */
    @Override
    public boolean supportsQuery(Query<O> query, QueryOptions queryOptions) {
        // Extract the root query from the query options, and check if it contains the filter query...
        @SuppressWarnings("unchecked")
        Query<O> rootQuery = (Query<O>) queryOptions.get(CollectionQueryEngine.ROOT_QUERY);
        if (!(rootQuery instanceof And)) {
            return false;
        }
        And<O> rootAndQuery = (And<O>)rootQuery;
        for (Query<O> childOfRoot : rootAndQuery.getChildQueries()) {
            if (filterQuery.equals(childOfRoot)) {
                // The filter query is a direct child of the root query.
                // Now check if the backing index supports the branch query...
                return backingIndex.supportsQuery(query, queryOptions);
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartialIndex)) {
            return false;
        }

        PartialIndex<?, ?> that = (PartialIndex<?, ?>) o;

        if (!filterQuery.equals(that.filterQuery)) {
            return false;
        }
        return backingIndex().equals(that.backingIndex());

    }

    @Override
    public int hashCode() {
        int result = filterQuery.hashCode();
        result = 31 * result + backingIndex().hashCode();
        return result;
    }

    @Override
    public boolean isMutable() {
        return backingIndex().isMutable();
    }

    @Override
    public boolean isQuantized() {
        return backingIndex().isQuantized();
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        return new WrappedResultSet<O>(backingIndex().retrieve(query, queryOptions)) {
            @Override
            public int getRetrievalCost() {
                return super.getRetrievalCost() + INDEX_RETRIEVAL_COST_DELTA;
            }
        };
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    @Override
    public void init(ObjectStore<O> objectStore, QueryOptions queryOptions) {
        if (!objectStore.isEmpty(queryOptions)) {
            // TODO implement filtering here. In the meantime, throw exception...
            throw new UnsupportedOperationException("Initializing this index with a non-empty collection is not yet supported.");
        }
        backingIndex().init(objectStore, queryOptions);
    }

    @Override
    public boolean addAll(Collection<O> objects, QueryOptions queryOptions) {
        Collection<O> matchingSubset = filter(objects, queryOptions);
        return backingIndex().addAll(matchingSubset, queryOptions);
    }

    @Override
    public boolean removeAll(Collection<O> objects, QueryOptions queryOptions) {
        Collection<O> matchingSubset = filter(objects, queryOptions);
        return backingIndex().removeAll(matchingSubset, queryOptions);
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        backingIndex().clear(queryOptions);
    }

    protected Collection<O> filter(Collection<O> objects, QueryOptions queryOptions) {
        Collection<O> matchingSubset = new HashSet<O>(objects.size());
        for (O candidate : objects) {
            if (filterQuery.matches(candidate, queryOptions)) {
                matchingSubset.add(candidate);
            }
        }
        return matchingSubset;
    }

    protected abstract AttributeIndex<A, O> createBackingIndex();

}
