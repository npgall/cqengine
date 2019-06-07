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
import com.googlecode.cqengine.engine.ModificationListener;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import com.googlecode.cqengine.persistence.support.FilteredObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.common.WrappedResultSet;

import java.util.*;

/**
 * An index which indexes and can answer queries on a subset of the collection; a kind of hybrid between a
 * {@link StandingQueryIndex} and a {@link CompoundIndex}.
 * <p/>
 * A partial index wraps a backing index and adds only a subset of objects which match a given <i>filter
 * query</i> to the backing index. As such, a partial index accelerates queries on an "interesting subset"
 * of the collection, without incurring the overhead of indexing the entire collection.
 * <p/>
 * Partial indexes require less storage space or memory than non-partial indexes, and they can yield better
 * query performance as well, because they will contain fewer irrelevant entries not pertaining to the query.
 * <p/>
 * Partial indexes are also particularly useful when used with index-accelerated ordering. They can store
 * results which match a given filter query in pre-sorted order of the given attribute, which means that
 * requesting results for that query and ordered by that attribute at runtime, can be answered quickly by
 * the partial index without requiring any post-filtering or post-sorting.
 * <p/>
 * <b>The conditions under which a partial index will be used are as follows.</b><br/>
 *
 * Two conditions must be satisfied:
 * <ol>
 *     <li>
 *         The backing index must support the branch of the query being evaluated.<br/>
 *         For example if a branch of the query is a <code>between()</code> query on a certain attribute,
 *         the backing index must support <code>between()</code> queries on that attribute.
 *     </li>
 *     <li>
 *         The root query must be compatible with the configured filter query for this partial index.<br/>
 *         Let <i>f</i> = the configured filter query, and <i>r</i> = the root query.<br/>
 *         Both of these may be any type of query (for example simple queries or complex
 *         <i>and()</i>/<i>or()</i>/<i>not()</i> queries).<br/>
 *         This partial index will indicate it supports the root query, if the root query satisfies <i>any</i> of
 *         the following conditions:
 *         <ul>
 *             <li><i>r</i> equals <i>f</i></li>
 *             <li><i>r</i> is an <code>And</code> query, which has <i>f</i> as any one of its direct children:
 *             <i>and(x, y, f, z)</i></li>
 *             <li><i>r</i> is an <code>And</code> query and <i>f</i> is an <code>And</code> query,
 *             and the direct children of <i>f</i> are also the
 *             direct children of <i>r</i>: <i>f</i> = <i>and(a, b)</i>, <i>r</i> = <i>and(x, a, y, b)</i></li>
 *         </ul>
 *     </li>
 * </ol>
 * These conditions are implemented by the {@link #supportsQuery(Query, QueryOptions)} method.
 *
 * @author niall.gallagher
 */
public abstract class PartialIndex<A, O, I extends AttributeIndex<A, O>> implements AttributeIndex<A, O> {

    // An integer to add or subtract to the retrieval cost returned by the backing index,
    // so that given a choice between a regular index and a partial index,
    // the retrieval cost from the partial index will be lower and so it will be chosen.
    // This is because partial indexes contain less data which is irrelevant to the query,
    // and so can incur less filtering...
    static final int INDEX_RETRIEVAL_COST_DELTA = -5;

    protected final Query<O> filterQuery;
    protected final Attribute<O, A> attribute;
    protected volatile I backingIndex;

    /**
     * Protected constructor, called by subclasses.
     *
     * @param attribute The attribute on which the index is built.
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     */
    protected PartialIndex(Attribute<O, A> attribute, Query<O> filterQuery) {
        this.attribute = attribute;
        this.filterQuery = filterQuery;
    }

    protected I backingIndex() {
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
     * <p/>
     * See the class JavaDoc for the conditions which must be satisfied for this method to return true.
     *
     * @param query The query supplied by the query engine, which is typically a branch of the overall query being
     *              evaluated.
     */
    @Override
    public boolean supportsQuery(Query<O> query, QueryOptions queryOptions) {
        // Extract the root query from the query options, and check if it contains the filter query...
        @SuppressWarnings("unchecked")
        Query<O> rootQuery = (Query<O>) queryOptions.get(CollectionQueryEngine.ROOT_QUERY);

        return supportsQueryInternal(backingIndex(), filterQuery, rootQuery, query, queryOptions);
    }

    static <A, O, I extends AttributeIndex<A, O>> boolean supportsQueryInternal(I backingIndex,
                                                                                Query<O> filterQuery,
                                                                                Query<O> rootQuery,
                                                                                Query<O> branchQuery,
                                                                                QueryOptions queryOptions) {
        if (!backingIndex.supportsQuery(branchQuery, queryOptions)) {
            return false;
        }

        // Check: rootQuery equals filterQuery
        if (filterQuery.equals(rootQuery)) {
            return true;
        }

        if (!(rootQuery instanceof And)) {
            return false;
        }
        // Check: rootQuery (r) is an And query, which has filterQuery (f) as any one of its direct children:
        // r = and(x, y, f, z)
        And<O> rootAndQuery = (And<O>)rootQuery;
        if (rootAndQuery.getChildQueries().contains(filterQuery)) {
            return true;
        }

        // Check: rootQuery (r) is an And query and filterQuery (f) is an And query,
        // and the direct children of f are also the direct children of r:
        // f = and(a, b), r = and(x, a, y, b)
        if (!(filterQuery instanceof And)) {
            return false;
        }
        And<O> filterAndQuery = ((And<O>) filterQuery);
        return rootAndQuery.getChildQueries().containsAll(filterAndQuery.getChildQueries());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartialIndex)) {
            return false;
        }

        PartialIndex<?, ?, ?> that = (PartialIndex<?, ?, ?>) o;

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
        backingIndex().init(new FilteredObjectStore<O>(objectStore, filterQuery), queryOptions);
    }

    /**
     * Calls {@link ModificationListener#destroy(QueryOptions)} on the backing index.
     *
     * @param queryOptions Optional parameters for the update
     */
    @Override
    public void destroy(QueryOptions queryOptions) {
        backingIndex().destroy(queryOptions);
    }

    @Override
    public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        Collection<O> matchingSubset = filter(objectSet, queryOptions);
        return backingIndex().addAll(ObjectSet.fromCollection(matchingSubset), queryOptions);
    }

    @Override
    public boolean removeAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        Collection<O> matchingSubset = filter(objectSet, queryOptions);
        return backingIndex().removeAll(ObjectSet.fromCollection(matchingSubset), queryOptions);
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        backingIndex().clear(queryOptions);
    }

    protected Collection<O> filter(ObjectSet<O> objects, QueryOptions queryOptions) {
        CloseableIterator<O> objectsIterator = objects.iterator();
        try {
            Collection<O> matchingSubset = new HashSet<O>();
            while (objectsIterator.hasNext()) {
                O candidate = objectsIterator.next();
                if (filterQuery.matches(candidate, queryOptions)) {
                    matchingSubset.add(candidate);
                }
            }
            return matchingSubset;
        }
        finally {
            objectsIterator.close();
        }
    }

    protected abstract I createBackingIndex();

}
