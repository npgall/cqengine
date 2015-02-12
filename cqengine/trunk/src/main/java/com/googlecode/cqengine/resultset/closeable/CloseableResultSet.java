package com.googlecode.cqengine.resultset.closeable;

import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.io.Closeable;
import java.util.Iterator;

/**
 * A ResultSet which throws exceptions if an attempt to use it is made after its {@link #close} method has been called.
 */
public class CloseableResultSet<O> extends ResultSet<O> implements Closeable {

    final ResultSet<O> wrapped;
    final QueryOptions queryOptions;
    boolean closed = false;

    protected CloseableResultSet(ResultSet<O> wrapped, QueryOptions queryOptions) {
        this.wrapped = wrapped;
        this.queryOptions = queryOptions;
    }

    @Override
    public Iterator<O> iterator() {
        ensureNotClosed();
        return wrapped.iterator();
    }

    @Override
    public boolean contains(O object) {
        ensureNotClosed();
        return wrapped.contains(object);
    }

    @Override
    public int size() {
        ensureNotClosed();
        return wrapped.size();
    }

    @Override
    public int getRetrievalCost() {
        ensureNotClosed();
        return wrapped.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
        ensureNotClosed();
        return wrapped.getMergeCost();
    }

    @Override
    public O uniqueResult() {
        ensureNotClosed();
        return wrapped.uniqueResult();
    }

    @Override
    public boolean isEmpty() {
        ensureNotClosed();
        return wrapped.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        ensureNotClosed();
        return wrapped.isNotEmpty();
    }

    void ensureNotClosed() {
        if (closed) {
            throw new IllegalStateException("ResultSet is closed");
        }
    }

    @Override
    public void close() {
        wrapped.close();
        closed = true;
    }
}
