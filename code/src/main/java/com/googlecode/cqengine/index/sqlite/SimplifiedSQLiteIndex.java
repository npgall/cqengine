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
import com.googlecode.cqengine.engine.QueryEngine;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.*;
import com.googlecode.cqengine.index.support.indextype.NonHeapTypeIndex;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

/**
 * An abstract class which wraps a {@link SQLiteIndex}, and simplifies some of its configuration options to make it
 * easier to use with IndexedCollections which themselves are persistent.
 * <p/>
 * The primary key to be used, and the database to connect to, is obtained from the IndexedCollection.
 *
 * @author niall.gallagher
 */
public abstract class SimplifiedSQLiteIndex<A extends Comparable<A>, O, K extends Comparable<K>> implements SortedKeyStatisticsAttributeIndex<A, O>, NonHeapTypeIndex {

    final Class<? extends SQLitePersistence> persistenceType;
    final Attribute<O, A> attribute;
    final String tableNameSuffix;
    volatile SQLiteIndex<A, O, K> backingIndex;

    protected SimplifiedSQLiteIndex(Class<? extends SQLitePersistence<O, A>> persistenceType, Attribute<O, A> attribute, String tableNameSuffix) {
        this.persistenceType = persistenceType;
        this.attribute = attribute;
        this.tableNameSuffix = tableNameSuffix;
    }

    @Override
    public void init(ObjectStore<O> objectStore, QueryOptions queryOptions) {
        Persistence<O, K> persistence = SimplifiedSQLiteIndex.<O, K>getPersistenceFromQueryOptions(queryOptions);
        QueryEngine<O> queryEngine = getQueryEngineFromQueryOptions(queryOptions);

        final SimpleAttribute<O, K> primaryKeyAttribute = getPrimaryKeyFromPersistence(persistence);
        final AttributeIndex<K, O> primaryKeyIndex = getPrimaryKeyIndexFromQueryEngine(primaryKeyAttribute, queryEngine, queryOptions);
        final SimpleAttribute<K, O> foreignKeyAttribute = new SimpleAttribute<K, O>(primaryKeyAttribute.getAttributeType(), primaryKeyAttribute.getObjectType()) {
            @Override
            public O getValue(K primaryKeyValue, QueryOptions queryOptions) {
                return primaryKeyIndex.retrieve(QueryFactory.equal(primaryKeyAttribute, primaryKeyValue), queryOptions).uniqueResult();
            }
        };
        backingIndex = new SQLiteIndex<A, O, K>(this.attribute, primaryKeyAttribute, foreignKeyAttribute, tableNameSuffix) {
            // Override getEffectiveIndex() in the backing index to return a reference to this index...
            @Override
            public Index<O> getEffectiveIndex() {
                return SimplifiedSQLiteIndex.this.getEffectiveIndex();
            }
        };
        backingIndex.init(objectStore, queryOptions);
    }

    /**
     * Calls {@link SQLiteIndex#destroy(QueryOptions)} on the wrapped index.
     *
     * @param queryOptions Optional parameters for the update
     */
    @Override
    public void destroy(QueryOptions queryOptions) {
        backingIndex().destroy(queryOptions);
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    static <O, K extends Comparable<K>> Persistence<O, K> getPersistenceFromQueryOptions(QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        Persistence<O, K> persistence = (Persistence<O, K>) queryOptions.get(Persistence.class);
        if (persistence == null) {
            throw new IllegalStateException("A required Persistence object was not supplied in query options");
        }
        return persistence;
    }

    static <O> QueryEngine<O> getQueryEngineFromQueryOptions(QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        QueryEngine<O> queryEngine = (QueryEngine<O>) queryOptions.get(QueryEngine.class);
        if (queryEngine == null) {
            throw new IllegalStateException("The QueryEngine was not supplied in query options");
        }
        return queryEngine;
    }

    SimpleAttribute<O, K> getPrimaryKeyFromPersistence(Persistence<O, K> persistence) {
        SimpleAttribute<O, K> primaryKey = persistence.getPrimaryKeyAttribute();
        if (primaryKey == null) {
            throw new IllegalStateException("This index " + getClass().getSimpleName() + " on attribute '" + attribute.getAttributeName() + "' cannot be added to the IndexedCollection, because the configured persistence was not configured with a primary key attribute.");
        }
        return primaryKey;
    }

    AttributeIndex<K, O> getPrimaryKeyIndexFromQueryEngine(SimpleAttribute<O, K> primaryKeyAttribute, QueryEngine<O> queryEngine, QueryOptions queryOptions) {
        for (Index<O> index : queryEngine.getIndexes()) {
            if (index instanceof AttributeIndex) {
                @SuppressWarnings("unchecked")
                AttributeIndex<K, O> attributeIndex = (AttributeIndex<K, O>) index;
                if (primaryKeyAttribute.equals(attributeIndex.getAttribute())) {
                    return attributeIndex;
                }
            }
        }
        throw new IllegalStateException("This index " + getClass().getSimpleName() + " on attribute '" + attribute.getAttributeName() + "' cannot be added to the IndexedCollection yet, because it requires that an index on the primary key to be added first.");
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
    public boolean supportsQuery(Query<O> query, QueryOptions queryOptions) {
        return backingIndex().supportsQuery(query, queryOptions);
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
    public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        return backingIndex().addAll(objectSet, queryOptions);
    }

    @Override
    public boolean removeAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        return backingIndex().removeAll(objectSet, queryOptions);
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
