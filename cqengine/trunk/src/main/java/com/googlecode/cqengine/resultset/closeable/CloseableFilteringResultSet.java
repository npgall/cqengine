package com.googlecode.cqengine.resultset.closeable;

import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.filter.FilteringResultSet;

import java.io.Closeable;
import java.util.Iterator;

/**
 * A FilteringResultSet which throws exceptions if an attempt to use it is made after its {@link #close} method has
 * been called.
 */
public abstract class CloseableFilteringResultSet<O> extends FilteringResultSet<O> implements Closeable {

    boolean closed = false;

    public CloseableFilteringResultSet(ResultSet<O> wrappedResultSet, QueryOptions queryOptions) {
        super(wrappedResultSet, queryOptions);
    }

    @Override
    public Iterator<O> iterator() {
        ensureNotClosed();
        return super.iterator();
    }

    @Override
    public int size() {
        ensureNotClosed();
        return super.size();
    }

    @Override
    public int getRetrievalCost() {
        ensureNotClosed();
        return super.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
        ensureNotClosed();
        return super.getMergeCost();
    }

    @Override
    public O uniqueResult() {
        ensureNotClosed();
        return super.uniqueResult();
    }

    @Override
    public boolean isEmpty() {
        ensureNotClosed();
        return super.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        ensureNotClosed();
        return super.isNotEmpty();
    }

    void ensureNotClosed() {
        if (closed) {
            throw new IllegalStateException("ResultSet is closed");
        }
    }

    @Override
    public void close() {
        super.close();
        closed = true;
    }
}
