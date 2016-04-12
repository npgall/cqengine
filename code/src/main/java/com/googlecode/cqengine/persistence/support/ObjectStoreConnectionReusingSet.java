package com.googlecode.cqengine.persistence.support;

import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.io.IOException;
import java.util.AbstractSet;
import java.util.Collection;

/**
 * Adapts an {@link ObjectStore} to behave like a {@link java.util.Set}, using an existing connection to the
 * persistence when methods belonging to the Set interface are called.
 */
public class ObjectStoreConnectionReusingSet<O> extends AbstractSet<O> {

    final ObjectStore<O> objectStore;
    final QueryOptions queryOptions;

    public ObjectStoreConnectionReusingSet(ObjectStore<O> objectStore, QueryOptions queryOptions) {
        this.objectStore = objectStore;
        this.queryOptions = queryOptions;
    }

    @Override
    public int size() {
        return objectStore.size(queryOptions);
    }

    @Override
    public boolean contains(Object o) {
        return objectStore.contains(o, queryOptions);
    }

    @Override
    public CloseableIterator<O> iterator() {
        return objectStore.iterator(queryOptions);
    }

    @Override
    public boolean isEmpty() {
        return objectStore.isEmpty(queryOptions);
    }

    @Override
    public boolean add(O object) {
        return objectStore.add(object, queryOptions);
    }

    @Override
    public boolean remove(Object o) {
        return objectStore.remove(o, queryOptions);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return objectStore.containsAll(c, queryOptions);
    }

    @Override
    public boolean addAll(Collection<? extends O> c) {
        return objectStore.addAll(c, queryOptions);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return objectStore.retainAll(c, queryOptions);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return objectStore.removeAll(c, queryOptions);
    }

    @Override
    public void clear() {
        objectStore.clear(queryOptions);
    }
}
