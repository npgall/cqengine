package com.googlecode.cqengine.resultset.common;

import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Iterator;

/**
 * A ResultSet which wraps another. Subclasses may extend this, to override some methods.
 *
 * @author niall.gallagher
 */
public class WrappedResultSet<O> extends ResultSet<O> {

    final ResultSet<O> wrappedResultSet;
    final QueryOptions queryOptions;

    public WrappedResultSet(ResultSet<O> wrappedResultSet, QueryOptions queryOptions) {
        this.wrappedResultSet = wrappedResultSet;
        this.queryOptions = queryOptions;
    }

    @Override
    public Iterator<O> iterator() {
        return wrappedResultSet.iterator();
    }

    @Override
    public boolean contains(O object) {
        return wrappedResultSet.contains(object);
    }

    @Override
    public int size() {
        return wrappedResultSet.size();
    }

    @Override
    public int getRetrievalCost() {
        return wrappedResultSet.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
        return wrappedResultSet.getMergeCost();
    }

    @Override
    public void close() {
        wrappedResultSet.close();
    }
}
