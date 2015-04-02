package com.googlecode.cqengine.persistence;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.index.sqlite.ConnectionManager;

import java.util.Set;

/**
 * An interface with multiple implementations, which provide details on how a collection or indexes should be persisted
 * off-heap (for example to off-heap memory, or to disk).
 *
 * @author niall.gallagher
 */
public interface Persistence<O, A extends Comparable<A>> extends ConnectionManager, Factory<Set<O>> {

    SimpleAttribute<O, A> getPrimaryKeyAttribute();

    /**
     * @return The number of bytes used to persist the collection and/or indexes
     */
    long getBytesUsed();

    /**
     * Compacts the underlying persistence, which returns unused memory or disk space to the operating system.
     */
    void compact();
}
