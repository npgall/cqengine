package com.googlecode.cqengine;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.query.option.AttributeOrder;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author wayne
 * @since 0.1.0
 */
public class CapacityLimitedIndexedCollection<O, A extends Comparable<A>> extends ConcurrentIndexedCollection<O> {

    private static final int DEF_CAPACITY_LIMIT = 1000;
    private static final int DEF_RELEASE_SIZE = 3;

    /**
     * The capacity size of collection
     */
    private final int capacityLimit;

    /**
     * The size of each release capacity
     */
    private final int releaseSize;

    /**
     * Sort field
     */
    private final AttributeOrder<O> attributeOrder;

    /**
     * Expiration strategy: LRU,LFU,FIFO,NONE
     */
    private Eviction eviction;

    public CapacityLimitedIndexedCollection() {
        this(DEF_CAPACITY_LIMIT);
    }

    public CapacityLimitedIndexedCollection(int capacityLimit) {
        this(capacityLimit, DEF_RELEASE_SIZE);
    }

    public CapacityLimitedIndexedCollection(int capacityLimit, int releaseSize) {
        this(capacityLimit, 0, Eviction.NONE, null);
    }

    public CapacityLimitedIndexedCollection(
            int capacityLimit,
            int releaseSize,
            Eviction eviction,
            Attribute<O, A> orderControlAttribute
    ) {
        this.capacityLimit = capacityLimit;
        this.releaseSize = releaseSize;
        this.eviction = eviction;
        this.attributeOrder = new AttributeOrder<>(orderControlAttribute, true);
    }

    public CapacityLimitedIndexedCollection(
            Persistence<O, ? extends Comparable> persistence,
            int capacityLimit,
            int releaseSize,
            Attribute<O, A> orderControlAttribute
    ) {
        super(persistence);
        this.capacityLimit = capacityLimit;
        this.releaseSize = releaseSize;
        this.attributeOrder = new AttributeOrder<>(orderControlAttribute, true);
    }

    @Override
    public boolean add(O o) {
        return addAll(Collections.singleton(o));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(Collection<? extends O> c) {
        if (c == null || c.isEmpty()) {
            return true;
        }
        if (isReachedCapacityLimit(c.size())) {
            if (this.eviction == Eviction.NONE) {
                throw new RuntimeException("Reached maximum capacity limit: " + capacityLimit);
            }
            int evictCount = getEvictCount(c.size());

            Class<O> clazz = (Class<O>) c.stream().findFirst().get().getClass();
            doEviction(evictCount, clazz);
        }
        return super.addAll(c);
    }

    private void doEviction(int evictCount, Class<O> clazz) {
        List<O> toEvictEntries = retrieve(all(clazz), getQueryOptionsByEviction()).stream().limit(evictCount).collect(Collectors.toList());

        if (toEvictEntries.size() > 0) {
            toEvictEntries.forEach(this::remove);
        }
    }

    private boolean isReachedCapacityLimit() {
        return size() >= capacityLimit;
    }

    private boolean isReachedCapacityLimit(int increment) {
        return (size() + increment) > capacityLimit;
    }

    private int getEvictCount(int increment) {
        int overSize = (size() + increment) - capacityLimit;
        return overSize + releaseSize;
    }

    public static class OrderedEntry<O> {
        private O o;
        private long timestamp;

        public OrderedEntry(O o) {
            this(o, System.currentTimeMillis());
        }

        public OrderedEntry(O o, long timestamp) {
            this.o = o;
            this.timestamp = timestamp;
        }

        public O getO() {
            return o;
        }

        public void setO(O o) {
            this.o = o;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public QueryOptions getQueryOptionsByEviction() {
        if (this.eviction == Eviction.NONE) {
            return null;
        }
        return queryOptions(orderBy(attributeOrder));
    }

    /**
     * Expiration strategy
     */
    public enum Eviction {
        LRU,
        LFU,
        FIFO,
        NONE
    }
}
