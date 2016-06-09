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

import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.filter.FilteringIterator;

import java.util.Collection;

/**
 * Wraps an {@link ObjectStore} and filters objects returned by its iterator to ensure they match a given query.
 * <p/>
 * Note this wrapper does not support any method except {@link #iterator(QueryOptions)}!
 * All other methods will throw {@link UnsupportedOperationException}.
 *
 * @author niall.gallagher
 */
public class FilteredObjectStore<O> implements ObjectStore<O> {

    final ObjectStore<O> backingObjectStore;
    final Query<O> filterQuery;

    public FilteredObjectStore(ObjectStore<O> backingObjectStore, Query<O> filterQuery) {
        this.backingObjectStore = backingObjectStore;
        this.filterQuery = filterQuery;
    }

    /**
     * Returns the subset of objects from the backing {@link ObjectStore} which match the filter query supplied to the
     * constructor.
     *
     * @return the subset of objects from the backing {@link ObjectStore} which match the filter query supplied to the
     * constructor.
     */
    @Override
    public CloseableIterator<O> iterator(QueryOptions queryOptions) {
        final CloseableIterator<O> backingIterator = backingObjectStore.iterator(queryOptions);
        final FilteringIterator<O> filteringIterator = new FilteringIterator<O>(backingIterator, queryOptions) {
            @Override
            public boolean isValid(O object, QueryOptions queryOptions) {
                return filterQuery.matches(object, queryOptions);
            }
        };
        return new CloseableIterator<O>() {
            @Override
            public void close() {
                backingIterator.close();
            }

            @Override
            public boolean hasNext() {
                return filteringIterator.hasNext();
            }

            @Override
            public O next() {
                return filteringIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Modification not supported");
            }
        };
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public int size(QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public boolean contains(Object o, QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public boolean isEmpty(QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public boolean add(O object, QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public boolean remove(Object o, QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public boolean containsAll(Collection<?> c, QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public boolean addAll(Collection<? extends O> c, QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public boolean retainAll(Collection<?> c, QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public boolean removeAll(Collection<?> c, QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public void clear(QueryOptions queryOptions) {
        throw new UnsupportedOperationException();
    }
}
