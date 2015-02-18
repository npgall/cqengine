package com.googlecode.cqengine.index.offheap.support;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.offheap.OffHeapIdentityIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.io.IOException;
import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author Niall Gallagher
 */
public class OffHeapMutableSet<O, A extends Comparable<A>> implements Set<O> {

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
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        O object = (O) o;
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
        }
        ;
        return new CloseableIteratorImpl();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean add(O object) {
        offHeapIdentityIndex.notifyObjectsAdded(Collections.singleton(object), noQueryOptions());
        return true;
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        O object = (O) o;
        offHeapIdentityIndex.notifyObjectsRemoved(Collections.singleton(object), noQueryOptions());
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends O> c) {
        @SuppressWarnings("unchecked")
        Collection<O> objects = (Collection<O>) c;
        offHeapIdentityIndex.notifyObjectsAdded(objects, noQueryOptions());
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Note: this could be optimized...
        Collection<O> objectsToRemove = new ArrayList<O>();
        ResultSet<O> allObjects = offHeapIdentityIndex.retrieve(all(objectType), noQueryOptions());
        try {
            for (O object : allObjects) {
                if (!c.contains(object)) {
                    objectsToRemove.add(object);
                }
            }
        }
        finally {
            allObjects.close();
        }
        offHeapIdentityIndex.notifyObjectsRemoved(objectsToRemove, noQueryOptions());
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        @SuppressWarnings("unchecked")
        Collection<O> objects = (Collection<O>) c;
        offHeapIdentityIndex.notifyObjectsRemoved(objects, noQueryOptions());
        return true;
    }

    @Override
    public void clear() {
        offHeapIdentityIndex.notifyObjectsCleared(noQueryOptions());
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
