package com.googlecode.cqengine.index.support;

/**
 * @author niall.gallagher
 */
public interface CloseableIterable<T> extends Iterable<T> {

    @Override
    abstract CloseableIterator<T> iterator();
}
