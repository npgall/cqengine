package com.googlecode.cqengine.index.offheap.support;

import java.io.Closeable;
import java.util.Iterator;

/**
 * @author niall.gallagher
 */
public interface CloseableIterator<T> extends Iterator<T>, Closeable {
}
