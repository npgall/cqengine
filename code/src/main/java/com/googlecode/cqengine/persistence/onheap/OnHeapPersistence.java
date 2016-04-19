package com.googlecode.cqengine.persistence.onheap;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.ResourceIndex;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.ConcurrentOnHeapObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * @author niall.gallagher
 */
public class OnHeapPersistence<O, A extends Comparable<A>> implements Persistence<O, A> {

    final SimpleAttribute<O, A> primaryKeyAttribute;
    final int initialCapacity;
    final float loadFactor;
    final int concurrencyLevel;

    public OnHeapPersistence() {
        this(null, 16, 0.75F, 16);
    }

    public OnHeapPersistence(SimpleAttribute<O, A> primaryKeyAttribute) {
        this(primaryKeyAttribute, 16, 0.75F, 16);
    }

    public OnHeapPersistence(SimpleAttribute<O, A> primaryKeyAttribute, int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.primaryKeyAttribute = primaryKeyAttribute;
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

    @Override
    public SimpleAttribute<O, A> getPrimaryKeyAttribute() {
        return primaryKeyAttribute;
    }

    /**
     * Creates an {@link OnHeapPersistence} object which persists to the Java heap.
     *
     * @param primaryKeyAttribute An attribute which returns the primary key of objects in the collection
     * @return An {@link OnHeapPersistence} object which persists to the Java heap.
     */
    public static <O, A extends Comparable<A>> OnHeapPersistence<O, A> onPrimaryKey(SimpleAttribute<O, A> primaryKeyAttribute) {
        return new OnHeapPersistence<O, A>(primaryKeyAttribute);
    }

    /**
     * Creates an {@link OnHeapPersistence} object which persists to the Java heap, without specifying a primary key.
     * As such, this persistence implementation will be compatible with on-heap indexes only.
     * <p/>
     * This persistence will not work with composite persistence configurations, where some indexes are located on heap,
     * and some off-heap etc. To use this persistence in those configurations, it is necessary to specify a primary
     * key - see: {@link #onPrimaryKey(SimpleAttribute)}.
     *
     * @return An {@link OnHeapPersistence} object which persists to the Java heap, and which is not configured with
     * a primary key.
     */
    public static <O> OnHeapPersistence<O, ? extends Comparable> withoutPrimaryKey() {
        return withoutPrimaryKey_Internal();
    }

    static <O, A extends Comparable<A>> OnHeapPersistence<O, A> withoutPrimaryKey_Internal() {
        return new OnHeapPersistence<O, A>();
    }
}
