package com.googlecode.cqengine.collection.impl;

import com.googlecode.cqengine.index.common.Factory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Creates a concurrent implementation of a {@link java.util.Set}, based on {@link ConcurrentHashMap}.
 *
 * @author Niall Gallagher
 */
public class DefaultConcurrentSetFactory<O> implements Factory<Set<O>> {

    final int initialSize;

    public DefaultConcurrentSetFactory() {
        this.initialSize = 16;
    }

    public DefaultConcurrentSetFactory(int initialSize) {
        this.initialSize = initialSize;
    }

    @Override
    public Set<O> create() {
        return Collections.newSetFromMap(new ConcurrentHashMap<O, Boolean>(initialSize));
    }
}
