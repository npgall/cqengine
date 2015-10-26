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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.support.ResourceIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

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
public class SQLiteIdentityIndex<A extends Comparable<A>, O> implements IdentityAttributeIndex<A, O>, ResourceIndex {

    final SQLiteIndex<A, O, byte[]> offHeapIndex;
    final Class<O> objectType;
    final SimpleAttribute<O, A> primaryKeyAttribute;
    final SimpleAttribute<A, O> foreignKeyAttribute;

    public SQLiteIdentityIndex(final SimpleAttribute<O, A> primaryKeyAttribute, ConnectionManager connectionManager) {
        this.offHeapIndex = SQLiteIndex.onAttribute(
                primaryKeyAttribute,
                new SerializingAttribute(primaryKeyAttribute.getObjectType(), byte[].class),
                new DeserializingAttribute(byte[].class, primaryKeyAttribute.getObjectType()),
                connectionManager
        );
        this.objectType = primaryKeyAttribute.getObjectType();
        this.primaryKeyAttribute = primaryKeyAttribute;
        this.foreignKeyAttribute = new ForeignKeyAttribute();
    }

    public SimpleAttribute<A, O> getForeignKeyAttribute() {
        return foreignKeyAttribute;
    }

    /**
     * Returns the attribute which given an object can read its primary key.
     */
    @Override
    public Attribute<O, A> getAttribute() {
        return offHeapIndex.getAttribute();
    }

    @Override
    public boolean isMutable() {
        return offHeapIndex.isMutable();
    }

    @Override
    public boolean supportsQuery(Query<O> query) {
        return offHeapIndex.supportsQuery(query);
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        return offHeapIndex.retrieve(query, queryOptions);
    }

    @Override
    public boolean addAll(Collection<O> objects, QueryOptions queryOptions) {
        return offHeapIndex.addAll(objects, queryOptions);
    }

    @Override
    public boolean removeAll(Collection<O> objects, QueryOptions queryOptions) {
        return offHeapIndex.removeAll(objects, queryOptions);
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        offHeapIndex.clear(queryOptions);
    }

    @Override
    public void init(Set<O> collection, QueryOptions queryOptions) {
        offHeapIndex.init(collection, queryOptions);
    }


    final ThreadLocal<Kryo> kryoCache = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            // Register the object which this index will persist...
            kryo.register(objectType);
            // Register additional serializers which are not built-in to Kryo 3.0...
            kryo.register(Arrays.asList().getClass(), new ArraysAsListSerializer());
            UnmodifiableCollectionsSerializer.registerSerializers(kryo);
            SynchronizedCollectionsSerializer.registerSerializers(kryo);
            return kryo;
        }
    };

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
                Kryo kryo = kryoCache.get();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Output output = new Output(baos);
                kryo.writeObject(output, object);
                output.close();
                return baos.toByteArray();
            }
            catch (Exception e) {
                throw new IllegalStateException("Failed to serialize object for saving in index, object type: " + objectType, e);
            }
        }
    }

    class DeserializingAttribute extends SimpleAttribute<byte[], O> {

        public DeserializingAttribute(Class<byte[]> objectType, Class<O> attributeType) {
            super(objectType, attributeType);
        }

        @Override
        public O getValue(byte[] bytes, QueryOptions queryOptions) {
            try {
                Kryo kryo = kryoCache.get();
                Input input = new Input(new ByteArrayInputStream(bytes));
                O object = kryo.readObject(input, objectType);
                input.close();
                return object;
            }
            catch (Exception e) {
                throw new IllegalStateException("Failed to deserialize object from index, object type: " + objectType, e);
            }
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
     * Creates a new {@link SQLiteIdentityIndex} for the given primary key attribute and connection manager.
     *
     * @param primaryKeyAttribute The {@link SimpleAttribute} representing a primary key on which the index will be built.
     * @param connectionManager The {@link ConnectionManager}
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @return a new instance of a standalone {@link SQLiteIdentityIndex}
     */
    public static <A extends Comparable<A>, O> SQLiteIdentityIndex<A, O> onAttribute(final SimpleAttribute<O, A> primaryKeyAttribute,
                                                           final ConnectionManager connectionManager) {
        return new SQLiteIdentityIndex<A, O>(primaryKeyAttribute, connectionManager);
    }
}
