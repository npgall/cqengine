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
package com.googlecode.cqengine.index.sqlite;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.support.*;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.PersistentSet;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Collection;
import java.util.Set;

/**
 * An abstract class which wraps a {@link SQLiteIndex}, and simplifies some of its configuration options to make it
 * easier to use with IndexedCollections which themselves are persistent.
 * <p/>
 * The primary key to be used, and the database to connect to, is obtained from the IndexedCollection.
 *
 * @author niall.gallagher
 */
public abstract class SimplifiedSQLiteIndex<A extends Comparable<A>, O, K extends Comparable<K>> implements SortedKeyStatisticsAttributeIndex<A, O>, ResourceIndex {

    final Class<? extends Persistence> persistenceType;
    volatile Attribute<O, A> attribute;
    volatile SQLiteIndex<A, O, K> backingIndex;

    protected SimplifiedSQLiteIndex(final Attribute<O, A> attribute,
                                    final Persistence<O, K> persistence,
                                    final SimpleAttribute<K, O> foreignKeyAttribute) {
        this.persistenceType = persistence.getClass();
        this.attribute = attribute;
        this.backingIndex = new SQLiteIndex<A, O, K>(
                attribute,
                persistence.getPrimaryKeyAttribute(),
                foreignKeyAttribute,
                persistence);
    }

    protected SimplifiedSQLiteIndex(Class<? extends Persistence<O, A>> persistenceType, Attribute<O, A> attribute) {
        this.persistenceType = persistenceType;
        this.attribute = attribute;
    }

    @Override
    public void init(Set<O> collection, QueryOptions queryOptions) {
        if (backingIndex == null) {
            if (!(collection instanceof PersistentSet)) {
                throw new IllegalStateException("No persistence strategy was configured for " + getClass().getSimpleName() + " on attribute '" + attribute.getAttributeName() + "', neither at index-level nor at IndexedCollection-level");
            }
            final PersistentSet<O, K> persistentSet = (PersistentSet<O, K>)collection;
            final Persistence<O, K> persistence = persistentSet.getPersistence();
            if (!(persistenceType.isAssignableFrom(persistence.getClass()))) {
                throw new IllegalStateException("No persistence strategy was configured explicitly for " + getClass().getSimpleName() + " on attribute '" + attribute.getAttributeName() + "', and the persistence strategy at IndexedCollection-level is not compatible with this index: " + persistence);
            }
            final IdentityAttributeIndex<K, O> identityIndex = persistentSet.getBackingIndex();
            final SimpleAttribute<O, K> primaryKeyAttribute = persistence.getPrimaryKeyAttribute();
            final SimpleAttribute<K, O> foreignKeyAttribute = identityIndex.getForeignKeyAttribute();
            backingIndex = new SQLiteIndex<A, O, K>(
                    attribute,
                    primaryKeyAttribute,
                    foreignKeyAttribute,
                    persistence);
        }
        backingIndex.init(collection, queryOptions);
    }

    SQLiteIndex<A, O, K> backingIndex() {
        SQLiteIndex<A, O, K> backingIndex = this.backingIndex;
        if (backingIndex == null) {
            throw new IllegalStateException("This index can only be used after it has been added to an IndexedCollection");
        }
        return backingIndex;
    }

    @Override
    public Attribute<O, A> getAttribute() {
        return attribute;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    // The following methods were mostly auto-generated using IntelliJ: Code -> Generate -> Delegate Methods...

    @Override
    public boolean supportsQuery(Query<O> query) {
        return backingIndex().supportsQuery(query);
    }

    @Override
    public boolean isQuantized() {
        return backingIndex().isQuantized();
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        return backingIndex().retrieve(query, queryOptions);
    }

    @Override
    public boolean addAll(Collection<O> objects, QueryOptions queryOptions) {
        return backingIndex().addAll(objects, queryOptions);
    }

    @Override
    public boolean removeAll(Collection<O> objects, QueryOptions queryOptions) {
        return backingIndex().removeAll(objects, queryOptions);
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        backingIndex().clear(queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeys(QueryOptions queryOptions) {
        return backingIndex().getDistinctKeys(queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeys(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return backingIndex().getDistinctKeys(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeysDescending(QueryOptions queryOptions) {
        return backingIndex().getDistinctKeysDescending(queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeysDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return backingIndex().getDistinctKeysDescending(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public Integer getCountForKey(A key, QueryOptions queryOptions) {
        return backingIndex().getCountForKey(key, queryOptions);
    }

    @Override
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeysDescending(QueryOptions queryOptions) {
        return backingIndex().getStatisticsForDistinctKeysDescending(queryOptions);
    }

    @Override
    public Integer getCountOfDistinctKeys(QueryOptions queryOptions) {
        return backingIndex().getCountOfDistinctKeys(queryOptions);
    }

    @Override
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeys(QueryOptions queryOptions) {
        return backingIndex().getStatisticsForDistinctKeys(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(QueryOptions queryOptions) {
        return backingIndex().getKeysAndValues(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return backingIndex().getKeysAndValues(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(QueryOptions queryOptions) {
        return backingIndex().getKeysAndValuesDescending(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return backingIndex().getKeysAndValuesDescending(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplifiedSQLiteIndex that = (SimplifiedSQLiteIndex) o;

        if (!attribute.equals(that.attribute)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result = 31 * result + attribute.hashCode();
        return result;
    }
}
