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

    protected final Query<O> filterQuery;
    protected final Attribute<O, A> attribute;
    protected final AttributeIndex<A, O> backingIndex;

    /**
     * Protected constructor, called by subclasses.
     *
     * @param attribute
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     */
    protected PartialIndex(Attribute<O, A> attribute, Query<O> filterQuery) {
        this.attribute = attribute;
        this.filterQuery = filterQuery;
        this.backingIndex = createBackingIndex();
    }

    public Attribute<O, A> getAttribute() {
        return backingIndex.getAttribute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsQuery(Query<O> query) {
        SimpleQuery<O, A> backingIndexQuery = getBackingIndexQueryOrNull(query);
        return backingIndexQuery != null
                && backingIndex.supportsQuery(backingIndexQuery)
                && backingIndex.getAttribute().equals(backingIndexQuery.getAttribute());
    }

    /**
     * Extracts the filter query and the query referring to the backing index from the given query.
     * <p/>
     * The partial index only supports 2-branch And queries where one branch is the filterQuery (either a SimpleQuery
     * or LogicalQuery)
     * and the other branch is a SimpleQuery on the attribute on which the backingIndex is based.
     *
     * @param query The query from which to extract the backing index query.
     * @return The query referring to the backing index, or null if the given query does not conform as above.
     */
    protected SimpleQuery<O, A> getBackingIndexQueryOrNull(Query<O> query) {
        if (!(query instanceof And)) {
            return null;
        }
        And<O> and = (And<O>)query;

        if (and.size() != 2) {
            return null;
        }

        boolean filterQueryFound = false;
        SimpleQuery<O, A> backingIndexQuery = null;
        for (Query<O> child : and.getChildQueries()) {
            if (filterQuery.equals(child)) {
                filterQueryFound = true;
            }
            else if (child instanceof SimpleQuery) {
                backingIndexQuery = (SimpleQuery<O, A>) child;
            }
        }
        if (!filterQueryFound) {
            return null;
        }
        return backingIndexQuery; // might still be null
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
        return backingIndex.equals(that.backingIndex);

    }

    @Override
    public int hashCode() {
        int result = filterQuery.hashCode();
        result = 31 * result + backingIndex.hashCode();
        return result;
    }

    @Override
    public boolean isMutable() {
        return backingIndex.isMutable();
    }

    @Override
    public boolean isQuantized() {
        return backingIndex.isQuantized();
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        SimpleQuery<O, A> backingIndexQuery = getBackingIndexQueryOrNull(query);
        // ..note we don't need to validate the backingIndexQuery,
        // as the supportsQuery method should have been invoked earlier to do that already.
        return backingIndex.retrieve(backingIndexQuery, queryOptions);
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
        backingIndex.init(objectStore, queryOptions);
    }

    @Override
    public boolean addAll(Collection<O> objects, QueryOptions queryOptions) {
        Collection<O> matchingSubset = filter(objects, queryOptions);
        return backingIndex.addAll(matchingSubset, queryOptions);
    }

    @Override
    public boolean removeAll(Collection<O> objects, QueryOptions queryOptions) {
        Collection<O> matchingSubset = filter(objects, queryOptions);
        return backingIndex.removeAll(matchingSubset, queryOptions);
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        backingIndex.clear(queryOptions);
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
