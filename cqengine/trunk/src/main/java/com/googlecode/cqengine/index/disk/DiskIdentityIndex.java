package com.googlecode.cqengine.index.disk;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.disk.support.ConnectionManager;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Set;

/**
 * An index which allows objects to be persisted directly in the index in serialized form, and to be accessed using a
 * primary key attribute.
 * <p/>
 * This index actually wraps a {@link DiskIndex}, but configures the value that it stores for each primary key, to be
 * the actual serialized bytes of the object which has that key.
 *
 * @author niall.gallagher
 */
public class DiskIdentityIndex<A, O> implements AttributeIndex<A, O> {

    final DiskIndex<A, O, byte[]> diskIndex;
    final Class<O> objectType;

    public DiskIdentityIndex(final SimpleAttribute<O, A> primaryKeyAttribute, ConnectionManager connectionManager) {
        this.diskIndex = DiskIndex.onAttribute(
                primaryKeyAttribute,
                new SerializingAttribute(),
                new DeserializingAttribute(),
                connectionManager
        );
        this.objectType = primaryKeyAttribute.getObjectType();
    }

    @Override
    public Attribute<O, A> getAttribute() {
        return diskIndex.getAttribute();
    }

    @Override
    public boolean isMutable() {
        return diskIndex.isMutable();
    }

    @Override
    public boolean supportsQuery(Query<O> query) {
        return diskIndex.supportsQuery(query);
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        return diskIndex.retrieve(query, queryOptions);
    }

    @Override
    public void notifyObjectsAdded(Collection<O> objects, QueryOptions queryOptions) {
        diskIndex.notifyObjectsAdded(objects, queryOptions);
    }

    @Override
    public void notifyObjectsRemoved(Collection<O> objects, QueryOptions queryOptions) {
        diskIndex.notifyObjectsRemoved(objects, queryOptions);
    }

    @Override
    public void notifyObjectsCleared(QueryOptions queryOptions) {
        diskIndex.notifyObjectsCleared(queryOptions);
    }

    @Override
    public void init(Set<O> collection, QueryOptions queryOptions) {
        diskIndex.init(collection, queryOptions);
    }


    final ThreadLocal<Kryo> kryoCache = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(objectType);
            return kryo;
        }
    };

    class SerializingAttribute extends SimpleAttribute<O, byte[]> {

        @Override
        public byte[] getValue(O object, QueryOptions queryOptions) {
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
     * Creates a new {@link DiskIdentityIndex} for the given primary key attribute and connection manager.
     *
     * @param primaryKeyAttribute The {@link SimpleAttribute} representing a primary key on which the index will be built.
     * @param connectionManager The {@link ConnectionManager}
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @return a new instance of a standalone {@link DiskIdentityIndex}
     */
    public static <A, O> DiskIdentityIndex<A, O> onAttribute(final SimpleAttribute<O, A> primaryKeyAttribute,
                                                           final ConnectionManager connectionManager) {
        return new DiskIdentityIndex<A, O>(primaryKeyAttribute, connectionManager);
    }
}
