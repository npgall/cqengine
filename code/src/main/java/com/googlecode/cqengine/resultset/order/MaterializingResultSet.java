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
package com.googlecode.cqengine.resultset.order;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.filter.DeduplicatingMaterializingIterator;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.Iterator;

/**
 * Wraps another {@link ResultSet} and ensures that the {@link Iterator} returned by the {@link #iterator()} method
 * never returns the same object more than once.
 * <p/>
 * The implementation delegates to {@link DeduplicatingMaterializingIterator}.
 * <p/>
 * Note that the {@link #size()} method in this implementation has O(n) time complexity, because it uses the
 * deduplicating iterator to count objects.
 *
 * @author Niall Gallagher
 */
public class MaterializingResultSet<O> extends ResultSet<O> {

    final ResultSet<O> wrappedResultSet;
    final Query<O> query;
    final QueryOptions queryOptions;

    public MaterializingResultSet(ResultSet<O> wrappedResultSet, Query<O> query, QueryOptions queryOptions) {
        this.wrappedResultSet = wrappedResultSet;
        this.query = query;
        this.queryOptions = queryOptions;
    }

    /**
     * Returns an {@link Iterator} which does not return the same object more than once.
     * <p/>
     * See class JavaDocs for more details. This implementation has <code>O(merge_cost)</code> time complexity.
     *
     * @return An {@link Iterator} which does not return the same object more than once
     */
    @Override
    public Iterator<O> iterator() {
        return new DeduplicatingMaterializingIterator<O>(wrappedResultSet.iterator());
    }

    /**
     * Delegates to the {@link ResultSet#contains(Object)} method of the wrapped {@link ResultSet}.
     *
     * @param object The object to check for containment in this {@link ResultSet}
     * @return True if this {@link ResultSet} contains the given object, false if it does not
     */
    @Override
    public boolean contains(O object) {
        return wrappedResultSet.contains(object);
    }

    @Override
    public boolean matches(O object) {
        return query.matches(object, queryOptions);
    }

    /**
     * Returns the retrieval cost of the wrapped {@link ResultSet}.
     * @return The retrieval cost of the wrapped {@link ResultSet}
     */
    @Override
    public int getRetrievalCost() {
        return wrappedResultSet.getRetrievalCost();
    }
    
    /**
     * Returns the merge cost of the wrapped {@link ResultSet}.
     * @return The merge cost of the wrapped {@link ResultSet}
     */
    @Override
    public int getMergeCost() {
        return wrappedResultSet.getMergeCost();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation has <code>O(merge_cost)</code> time complexity, because it delegates to
     * the {@link #iterator()} method and counts objects returned.
     */
    @Override
    public int size() {
        return IteratorUtil.countElements(this);
    }

    /**
     * Closes the wrapped {@code ResultSet}.
     */
    @Override
    public void close() {
        wrappedResultSet.close();
    }

    @Override
    public Query<O> getQuery() {
        return query;
    }

    @Override
    public QueryOptions getQueryOptions() {
        return queryOptions;
    }
}
