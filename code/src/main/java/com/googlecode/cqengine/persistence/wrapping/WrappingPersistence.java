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
package com.googlecode.cqengine.persistence.wrapping;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.indextype.OnHeapTypeIndex;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.persistence.support.CollectionWrappingObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Collection;

/**
 * Specifies to wrap and use a given collection for persistence.
 * <p/>
 * Note that, as the implementation of the given collection is outside of CQEngine's control,
 * the following points should be considered with respect to performance and query processing.
 * <p/>
 * <b>Collection.contains() method</b><br/>
 * As CQEngine evaluates queries using set theory, it relies heavily on the performance of the
 * {@link Collection#contains(Object)} method.
 * If the implementation of this method in the given collection is slow, then it may slow down some queries on the
 * {@link IndexedCollection}.
 * <p/>
 * As such, it is recommended, although not required, that the wrapped collection is a {@link java.util.Set}.
 * The time complexity of the {@link java.util.Set#contains(Object)} method is usually <i>O(1)</i>.
 * <p/>
 * <b>Duplicate objects</b><br/>
 * CQEngine does not expect the given collection to contain duplicate objects. If the given collection does
 * contain duplicates, then it is possible that duplicate objects may be returned in {@link ResultSet}s
 * even if deduplication was requested.
 * <p/>
 * <b>Thread-safety</b><br/>
 * CQEngine will depend on the wrapped collection to be thread-safe, if an {@link IndexedCollection} is
 * configured with this persistence, and it will be accessed concurrently.
 * <p/>
 * If the application needs to access the the {@link IndexedCollection} concurrently but it cannot supply
 * a thread-safe collection to wrap, then it is recommended to use the {@link OnHeapPersistence} instead.
 *
 * @author niall.gallagher
 */
public class WrappingPersistence<O, A extends Comparable<A>> implements Persistence<O, A> {

    final Collection<O> backingCollection;
    final SimpleAttribute<O, A> primaryKeyAttribute;

    public WrappingPersistence(Collection<O> backingCollection) {
        this(backingCollection, null);
    }

    public WrappingPersistence(Collection<O> backingCollection, SimpleAttribute<O, A> primaryKeyAttribute) {
        this.backingCollection = backingCollection;
        this.primaryKeyAttribute = primaryKeyAttribute;
    }

    /**
     * Returns true if the given index implements the {@link OnHeapTypeIndex} marker interface.
     */
    @Override
    public boolean supportsIndex(Index<O> index) {
        return index instanceof OnHeapTypeIndex;
    }

    @Override
    public ObjectStore<O> createObjectStore() {
        return new CollectionWrappingObjectStore<O>(backingCollection);
    }

    /**
     * Currently does nothing in this implementation of {@link Persistence}.
     */
    @Override
    public void openRequestScopeResources(QueryOptions queryOptions) {
        // No op
    }

    /**
     * Currently does nothing in this implementation of {@link Persistence}.
     */
    @Override
    public void closeRequestScopeResources(QueryOptions queryOptions) {
        // No op
    }

    @Override
    public SimpleAttribute<O, A> getPrimaryKeyAttribute() {
        return primaryKeyAttribute;
    }

    /**
     * Creates a {@link WrappingPersistence} object which persists to the given collection.
     *
     * @param primaryKeyAttribute An attribute which returns the primary key of objects in the collection
     * @return A {@link WrappingPersistence} object which persists to the given collection.
     */
    public static <O, A extends Comparable<A>> WrappingPersistence<O, A> aroundCollectionOnPrimaryKey(Collection<O> collection, SimpleAttribute<O, A> primaryKeyAttribute) {
        return new WrappingPersistence<O, A>(collection, primaryKeyAttribute);
    }

    /**
     * Creates a {@link WrappingPersistence} object which persists to the given collection, without specifying a primary
     * key. As such, this persistence implementation will be compatible with on-heap indexes only.
     * <p/>
     * This persistence will not work with composite persistence configurations, where some indexes are located on heap,
     * and some off-heap etc. To use this persistence in those configurations, it is necessary to specify a primary
     * key - see: {@link #aroundCollectionOnPrimaryKey(Collection, SimpleAttribute)}.
     *
     * @return A {@link WrappingPersistence} object which persists to the given collection, and which is not configured
     * with a primary key.
     */
    @SuppressWarnings("unchecked")
    public static <O> WrappingPersistence<O, ? extends Comparable> aroundCollection(Collection<O> collection) {
        return withoutPrimaryKey_Internal(collection);
    }

    static <O, A extends Comparable<A>> WrappingPersistence<O, A> withoutPrimaryKey_Internal(Collection<O> collection) {
        return new WrappingPersistence<O, A>(collection);
    }
}
