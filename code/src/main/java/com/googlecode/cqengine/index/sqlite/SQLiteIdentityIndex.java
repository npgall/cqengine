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
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.persistence.support.serialization.PersistenceConfig;
import com.googlecode.cqengine.persistence.support.serialization.PojoSerializer;
import com.googlecode.cqengine.index.support.CloseableIterable;
import com.googlecode.cqengine.index.support.KeyStatistics;
import com.googlecode.cqengine.index.support.KeyValue;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.indextype.NonHeapTypeIndex;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.lang.reflect.Constructor;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;

/**
 * An index which allows objects to be persisted directly in the index in serialized form, and to be accessed using a
 * primary key attribute (as obtained from a foreign key held elsewhere).
 * <p/>
 * This index actually wraps an {@link SQLiteIndex}, but configures the value that it stores for each primary key, to
 * be the actual serialized bytes of the object which has that key.
 *
 * @author niall.gallagher
 */
public class SQLiteIdentityIndex<A extends Comparable<A>, O> implements IdentityAttributeIndex<A, O>, SortedKeyStatisticsAttributeIndex<A, O>, NonHeapTypeIndex {

    final SQLiteIndex<A, O, byte[]> sqLiteIndex;
    final Class<O> objectType;
    final SimpleAttribute<O, A> primaryKeyAttribute;
    final SimpleAttribute<A, O> foreignKeyAttribute;
    final PojoSerializer<O> pojoSerializer;

    public SQLiteIdentityIndex(final SimpleAttribute<O, A> primaryKeyAttribute) {
        this.sqLiteIndex = new SQLiteIndex<A, O, byte[]>(
                primaryKeyAttribute,
                new SerializingAttribute(primaryKeyAttribute.getObjectType(), byte[].class),
                new DeserializingAttribute(byte[].class, primaryKeyAttribute.getObjectType()),
                "") {
            // Override getEffectiveIndex() in the SQLiteIndex to return a reference to this index...
            @Override
            public Index<O> getEffectiveIndex() {
                return SQLiteIdentityIndex.this.getEffectiveIndex();
            }
        };
        this.objectType = primaryKeyAttribute.getObjectType();
        this.primaryKeyAttribute = primaryKeyAttribute;
        this.foreignKeyAttribute = new ForeignKeyAttribute();
        this.pojoSerializer = createSerializer(objectType);
    }

    public SimpleAttribute<A, O> getForeignKeyAttribute() {
        return foreignKeyAttribute;
    }

    /**
     * Returns the attribute which given an object can read its primary key.
     */
    @Override
    public Attribute<O, A> getAttribute() {
        return sqLiteIndex.getAttribute();
    }

    @Override
    public boolean isMutable() {
        return sqLiteIndex.isMutable();
    }

    @Override
    public boolean supportsQuery(Query<O> query, QueryOptions queryOptions) {
        return sqLiteIndex.supportsQuery(query, queryOptions);
    }

    @Override
    public boolean isQuantized() {
        return sqLiteIndex.isQuantized();
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        return sqLiteIndex.retrieve(query, queryOptions);
    }

    @Override
    public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        return sqLiteIndex.addAll(objectSet, queryOptions);
    }

    @Override
    public boolean removeAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        return sqLiteIndex.removeAll(objectSet, queryOptions);
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        sqLiteIndex.clear(queryOptions);
    }

    @Override
    public void init(ObjectStore<O> objectStore, QueryOptions queryOptions) {
        sqLiteIndex.init(objectStore, queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeys(QueryOptions queryOptions) {
        return sqLiteIndex.getDistinctKeys(queryOptions);
    }

    @Override
    public Integer getCountForKey(A key, QueryOptions queryOptions) {
        return sqLiteIndex.getCountForKey(key, queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeys(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return sqLiteIndex.getDistinctKeys(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeysDescending(QueryOptions queryOptions) {
        return sqLiteIndex.getDistinctKeysDescending(queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeysDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return sqLiteIndex.getDistinctKeysDescending(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeysDescending(QueryOptions queryOptions) {
        return sqLiteIndex.getStatisticsForDistinctKeysDescending(queryOptions);
    }

    @Override
    public Integer getCountOfDistinctKeys(QueryOptions queryOptions) {
        return sqLiteIndex.getCountOfDistinctKeys(queryOptions);
    }

    @Override
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeys(QueryOptions queryOptions) {
        return sqLiteIndex.getStatisticsForDistinctKeys(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(QueryOptions queryOptions) {
        return sqLiteIndex.getKeysAndValues(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return sqLiteIndex.getKeysAndValues(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(QueryOptions queryOptions) {
        return sqLiteIndex.getKeysAndValuesDescending(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return sqLiteIndex.getKeysAndValuesDescending(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLiteIdentityIndex that = (SQLiteIdentityIndex) o;

        if (!primaryKeyAttribute.equals(that.primaryKeyAttribute)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result = 31 * result + primaryKeyAttribute.hashCode();
        return result;
    }

    @SuppressWarnings("unchecked")
    static <O> PojoSerializer<O> createSerializer(Class<O> objectType) {
        Class<? extends PojoSerializer> serializerClass = null;
        try {
            // Read the configured serializer from the @PersistenceConfig annotation...
            PersistenceConfig persistenceConfig = objectType.getAnnotation(PersistenceConfig.class);
            if (persistenceConfig == null) {
                persistenceConfig = PersistenceConfig.DEFAULT_CONFIG;
            }
            serializerClass = persistenceConfig.serializer();

            // Instantiate the serializer, supplying the parameters to its (objectType, persistenceConfig) constructor...
            Constructor constructor = serializerClass.getConstructor(Class.class, PersistenceConfig.class);
            Object serializerInstance = constructor.newInstance(objectType, persistenceConfig);
            return (PojoSerializer<O>) serializerInstance;
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate PojoSerializer: " + serializerClass, e);
        }
    }

    class SerializingAttribute extends SimpleAttribute<O, byte[]> {

        public SerializingAttribute(Class<O> objectType, Class<byte[]> attributeType) {
            super(objectType, attributeType);
        }

        @Override
        public byte[] getValue(O object, QueryOptions queryOptions) {
            if (object == null) {
                throw new NullPointerException("Object was null");
            }
            try {
                return pojoSerializer.serialize(object);
            }
            catch (Exception e) {
                throw new IllegalStateException("Failed to serialize object, object type: " + objectType, e);
            }
        }
    }

    class DeserializingAttribute extends SimpleAttribute<byte[], O> {

        public DeserializingAttribute(Class<byte[]> objectType, Class<O> attributeType) {
            super(objectType, attributeType);
        }

        @Override
        public O getValue(byte[] bytes, QueryOptions queryOptions) {
            return pojoSerializer.deserialize(bytes);
        }
    }

    /**
     * An attribute which given a primary key (or a foreign key to it) can read the corresponding
     * object from this index.
     */
    class ForeignKeyAttribute extends SimpleAttribute<A, O> {

        public ForeignKeyAttribute() {
            super(primaryKeyAttribute.getAttributeType(), objectType, ForeignKeyAttribute.class.getSimpleName() + "_" + primaryKeyAttribute.getAttributeName());
        }

        @Override
        public O getValue(A foreignKey, QueryOptions queryOptions) {
            return SQLiteIdentityIndex.this.retrieve(equal(primaryKeyAttribute, foreignKey), noQueryOptions()).uniqueResult();
        }
    }

    /**
     * Creates a new {@link SQLiteIdentityIndex} for the given primary key attribute.
     *
     * @param primaryKeyAttribute The {@link SimpleAttribute} representing a primary key on which the index will be built.
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @return a new instance of a standalone {@link SQLiteIdentityIndex}
     */
    public static <A extends Comparable<A>, O> SQLiteIdentityIndex<A, O> onAttribute(final SimpleAttribute<O, A> primaryKeyAttribute) {
        return new SQLiteIdentityIndex<A, O>(primaryKeyAttribute);
    }
}
