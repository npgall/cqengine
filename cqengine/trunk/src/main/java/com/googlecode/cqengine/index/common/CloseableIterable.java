package com.googlecode.cqengine.index.common;

/**
 * @author niall.gallagher
 */
public interface CloseableIterable<T> extends Iterable<T> {

    @Override
    abstract CloseableIterator<T> iterator();
}
