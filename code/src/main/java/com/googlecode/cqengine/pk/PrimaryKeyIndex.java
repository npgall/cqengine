package com.googlecode.cqengine.pk;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.index.unique.UniqueIndex;

import java.util.concurrent.ConcurrentMap;

/**
 * An index that reuses map supplied in constructor
 */
public class PrimaryKeyIndex<A, O> extends UniqueIndex<A, O> {
    public PrimaryKeyIndex(final ConcurrentMap<A, O> indexMap, Attribute attribute) {
        super(new Factory<ConcurrentMap<A, O>>() {
            @Override
            public ConcurrentMap<A, O> create() {
                return indexMap;
            }
        }, attribute);
    }
}
