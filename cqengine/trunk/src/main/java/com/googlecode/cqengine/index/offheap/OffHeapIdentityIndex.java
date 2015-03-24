package com.googlecode.cqengine.index.offheap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.common.ResourceIndex;
import com.googlecode.cqengine.index.offheap.support.ConnectionManager;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * An index which allows objects to be persisted directly in the index in serialized form, and to be accessed using a
 * primary key attribute.
 * <p/>
 * This index actually wraps an {@link OffHeapIndex}, but configures the value that it stores for each primary key, to
 * be the actual serialized bytes of the object which has that key.
 *
 * @author niall.gallagher
 */
public class OffHeapIdentityIndex<A extends Comparable<A>, O> implements AttributeIndex<A, O>, ResourceIndex {

    final OffHeapIndex<A, O, byte[]> offHeapIndex;
    final Class<O> objectType;
    final SimpleAttribute<O, A> primaryKeyAttribute;

    public OffHeapIdentityIndex(final SimpleAttribute<O, A> primaryKeyAttribute, ConnectionManager connectionManager) {
        this.offHeapIndex = OffHeapIndex.onAttribute(
                primaryKeyAttribute,
                new SerializingAttribute(primaryKeyAttribute.getObjectType(), byte[].class),
                new DeserializingAttribute(byte[].class, primaryKeyAttribute.getObjectType()),
                connectionManager
        );
        this.objectType = primaryKeyAttribute.getObjectType();
        this.primaryKeyAttribute = primaryKeyAttribute;
    }

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
     * Creates a new {@link OffHeapIdentityIndex} for the given primary key attribute and connection manager.
     *
     * @param primaryKeyAttribute The {@link SimpleAttribute} representing a primary key on which the index will be built.
     * @param connectionManager The {@link ConnectionManager}
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @return a new instance of a standalone {@link OffHeapIdentityIndex}
     */
    public static <A extends Comparable<A>, O> OffHeapIdentityIndex<A, O> onAttribute(final SimpleAttribute<O, A> primaryKeyAttribute,
                                                           final ConnectionManager connectionManager) {
        return new OffHeapIdentityIndex<A, O>(primaryKeyAttribute, connectionManager);
    }
}
