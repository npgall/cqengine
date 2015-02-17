package com.googlecode.cqengine.index.offheap.support;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.engine.ModificationListener;
import com.googlecode.cqengine.index.offheap.OffHeapIdentityIndex;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.io.IOException;
import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author Niall Gallagher
 */
public class OffHeapMutableSet<O, A> extends ResultSet<O> implements ModificationListener<O> {

    final SimpleAttribute<O, A> primaryKeyAttribute;
    final Class<O> objectType;
    final OffHeapIdentityIndex<A, O> offHeapIdentityIndex;
    final int retrievalCost;

    public OffHeapMutableSet(final SimpleAttribute<O, A> primaryKeyAttribute, ConnectionManager connectionManager, int retrievalCost) {
        this.primaryKeyAttribute = primaryKeyAttribute;
        this.retrievalCost = retrievalCost;
        this.objectType = primaryKeyAttribute.getObjectType();
        this.offHeapIdentityIndex = OffHeapIdentityIndex.onAttribute(primaryKeyAttribute, connectionManager);
    }

    @Override
    public int size() {
        return offHeapIdentityIndex.retrieve(all(objectType), noQueryOptions()).size();
    }

    @Override
    public boolean contains(O object) {
        A objectId = primaryKeyAttribute.getValue(object, noQueryOptions());
        return offHeapIdentityIndex.retrieve(equal(primaryKeyAttribute, objectId), noQueryOptions()).size() > 0;
    }

    @Override
    public CloseableIterator<O> iterator() {
        final ResultSet<O> rs = offHeapIdentityIndex.retrieve(all(objectType), noQueryOptions());
        final Iterator<O> i = rs.iterator();
        class CloseableIteratorImpl extends UnmodifiableIterator<O> implements CloseableIterator<O> {

            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public O next() {
                return i.next();
            }

            @Override
            public void close() throws IOException {
                rs.close();
            }
        };
        return new CloseableIteratorImpl();
    }

    @Override
    public void init(Set<O> collection, QueryOptions queryOptions) {
        offHeapIdentityIndex.init(collection, queryOptions);
    }

    @Override
    public void notifyObjectsAdded(Collection<O> objects, QueryOptions queryOptions) {
        offHeapIdentityIndex.notifyObjectsAdded(objects, queryOptions);
    }

    @Override
    public void notifyObjectsRemoved(Collection<O> objects, QueryOptions queryOptions) {
        offHeapIdentityIndex.notifyObjectsRemoved(objects, queryOptions);
    }

    @Override
    public void notifyObjectsCleared(QueryOptions queryOptions) {
        offHeapIdentityIndex.notifyObjectsCleared(queryOptions);
    }

    @Override
    public void close() {
        // No op
    }

    @Override
    public int getRetrievalCost() {
        return retrievalCost;
    }

    @Override
    public int getMergeCost() {
        return size();
    }
}
