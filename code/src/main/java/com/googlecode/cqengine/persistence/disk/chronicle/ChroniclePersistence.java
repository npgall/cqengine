package com.googlecode.cqengine.persistence.disk.chronicle;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;
import io.protostuff.*;
import io.protostuff.runtime.*;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;

public class ChroniclePersistence<O, A extends Comparable<A>> implements ObjectStore<O>, Persistence<O, A> {

    private final File dbFile;

    final SimpleAttribute<O, A> primaryKeyAttribute;

    Schema<O> objectSchema;
    Schema<A> indexSchema;

    int objectMaxSize;
    int indexMaxSize;


    private ChronicleMap<A, O> chronicleMap;


    /**
     * Create's a chronicle persistence object, this will save the state to disk with a filesize proportional to the settings
     *
     * Note that the maximums below presume the worst case, (eg if your actual object size average is lower than what is set then you will be able to store more entries than indicated)
     * @param primaryKeyAttribute The primary attribute
     * @param dbFile The file to store in
     * @param indexClass The class of the indexing object
     * @param objectClass The class of the stored object
     * @param indexMaxSize The maximum expected size of an indexing object
     * @param objectMaxSize The maximum expected size of a stored object
     * @param maxEntries The maximum number of entries expected for this store
     * @throws IOException
     */
    public ChroniclePersistence(SimpleAttribute<O, A> primaryKeyAttribute, File dbFile, Class<A> indexClass, Class<O> objectClass, int indexMaxSize, int objectMaxSize, long maxEntries) throws
                                                                                                                                                                   IOException {
        // Timestamps aren't correctly decoded without this delegate..
        TimestampDelegate                       timestampDelegate = new TimestampDelegate();
        io.protostuff.runtime.DefaultIdStrategy sessionIdStrategy = new DefaultIdStrategy();
        sessionIdStrategy.registerDelegate(timestampDelegate);

        this.indexMaxSize  = indexMaxSize;
        this.objectMaxSize = objectMaxSize;

        this.indexSchema  = RuntimeSchema.getSchema(indexClass, sessionIdStrategy);
        this.objectSchema = RuntimeSchema.getSchema(objectClass, sessionIdStrategy);

        this.primaryKeyAttribute = primaryKeyAttribute;
        this.dbFile              = dbFile;

        ChronicleMapBuilder<A, O> mapBuilder = ChronicleMapBuilder.of(indexClass, objectClass)
                                                                  .name(dbFile.getName())
                                                                  .averageKeySize(indexMaxSize)
                                                                  .averageValueSize(objectMaxSize)
                                                                  .entries(maxEntries); // Adjust the expected number of entries as needed
        try {
            chronicleMap = mapBuilder.createPersistedTo(dbFile);
        } catch (Exception e) {
            chronicleMap = mapBuilder.createOrRecoverPersistedTo(dbFile);
        }
    }


    @Override
    public ObjectStore<O> createObjectStore() {
        return this;
    }

    @Override
    public boolean supportsIndex(Index<O> index) {
        return true;
    }

    @Override
    public void openRequestScopeResources(QueryOptions queryOptions) {
        // No resources need to be opened for this implementation
    }

    @Override
    public void closeRequestScopeResources(QueryOptions queryOptions) {
        // No resources need to be closed for this implementation
    }

    @Override
    public SimpleAttribute<O, A> getPrimaryKeyAttribute() {
        return primaryKeyAttribute;
    }

    @Override
    public int size(QueryOptions queryOptions) {
        return (int) chronicleMap.longSize();
    }

    @Override
    public boolean contains(Object o, QueryOptions queryOptions) {
        A key = primaryKeyAttribute.getValue((O) o, queryOptions);
        return chronicleMap.containsKey(key);
    }

    @Override
    public boolean add(O o, QueryOptions queryOptions) {
        A key           = primaryKeyAttribute.getValue(o, queryOptions);
        O existingValue = chronicleMap.putIfAbsent(key, o);
        return existingValue == null;
    }

    @Override
    public boolean remove(Object o, QueryOptions queryOptions) {
        A key          = primaryKeyAttribute.getValue((O) o, queryOptions);
        O removedValue = chronicleMap.remove(key);
        return removedValue != null;
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        chronicleMap.clear();
    }

    @Override
    public CloseableIterator<O> iterator(QueryOptions queryOptions) {
        return new ChronicleMapIterator<>(chronicleMap);
    }

    @Override
    public boolean isEmpty(QueryOptions queryOptions) {
        return size(queryOptions) == 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection, QueryOptions queryOptions) {
        for (Object o : collection) {
            if (!contains(o, queryOptions)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends O> collection, QueryOptions queryOptions) {
        boolean modified = false;
        for (O o : collection) {
            if (add(o, queryOptions)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> collection, QueryOptions queryOptions) {
        boolean modified = false;
        try (CloseableIterator<O> iterator = iterator(queryOptions)) {
            while (iterator.hasNext()) {
                O o = iterator.next();
                if (!collection.contains(o)) {
                    iterator.remove();
                    modified = true;
                }
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> collection, QueryOptions queryOptions) {
        boolean modified = false;
        for (Object o : collection) {
            if (remove(o, queryOptions)) {
                modified = true;
            }
        }
        return modified;
    }


    // Helper methods to convert keys and values to ByteBuffers

    private ByteBuffer serializeKey(A key) {
        LinkedBuffer buffer = LinkedBuffer.allocate(indexMaxSize);
        byte[]       bytes  = ProtostuffIOUtil.toByteArray(key, indexSchema, buffer);
        return ByteBuffer.wrap(bytes);
    }

    private ByteBuffer serializeValue(O value) {
        LinkedBuffer buffer = LinkedBuffer.allocate(objectMaxSize);
        byte[]       bytes  = ProtostuffIOUtil.toByteArray(value, objectSchema, buffer);
        return ByteBuffer.wrap(bytes);
    }

    private A deserializeKey(ByteBuffer keyBuffer) {
        A key = indexSchema.newMessage();
        ProtostuffIOUtil.mergeFrom(keyBuffer.array(), key, indexSchema);
        return key;
    }

    private O deserializeValue(ByteBuffer valueBuffer) {
        O value = objectSchema.newMessage();
        ProtostuffIOUtil.mergeFrom(valueBuffer.array(), value, objectSchema);
        return value;
    }

    public File getDbFile() {
        return dbFile;
    }
}
