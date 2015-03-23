package com.googlecode.cqengine.index.offheap.support;

/**
 * @author niall.gallagher
 */
public interface CloseableIterable<T> extends Iterable<T> {

    @Override
    abstract CloseableIterator<T> iterator();
}
