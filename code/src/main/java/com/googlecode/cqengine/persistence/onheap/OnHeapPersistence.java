package com.googlecode.cqengine.persistence.onheap;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.ResourceIndex;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.ConcurrentOnHeapObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author niall.gallagher
 */
public class OnHeapPersistence<O> implements Persistence<O> {

    final int initialCapacity;
    final float loadFactor;
    final int concurrencyLevel;

    public OnHeapPersistence() {
        this(16, 0.75F, 16);
    }

    public OnHeapPersistence(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.concurrencyLevel = concurrencyLevel;
    }

    @Override
    public boolean supportsIndex(Index<O> index) {
        return !(index instanceof ResourceIndex);
    }

    @Override
    public ObjectStore<O> createObjectStore() {
        return new ConcurrentOnHeapObjectStore<O>(initialCapacity, loadFactor, concurrencyLevel);
    }

    /**
     * Currently does nothing in this implementation of {@link Persistence}.
     */
    @Override
    public void openRequestScopeResources(QueryOptions queryOptions) {
        // No op
    }

    /**
     * Currently does nothing in this implementation of {@link Persistence}.
     */
    @Override
    public void closeRequestScopeResources(QueryOptions queryOptions) {
        // No op
    }
}
