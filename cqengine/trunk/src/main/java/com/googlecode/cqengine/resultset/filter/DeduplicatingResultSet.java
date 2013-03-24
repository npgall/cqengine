package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.Iterator;

/**
 * A {@link ResultSet} which wraps another, to suppress duplicate objects, where a duplicate object is one which has
 * the same value(s) for the given attribute.
 * <p/>
 * Say an object had an attribute {@code COLOR}. This result set would return only one object of each color from the
 * wrapped result set.
 * <p/>
 * Only one of the potentially duplicate objects will be returned, but which one is unspecified (the query engine is
 * usually free for performance reasons to return objects in any order).
 *
 * @author Niall Gallagher
 */
public class DeduplicatingResultSet<O, A> extends ResultSet<O> {

    private final ResultSet<O> wrappedResultSet;

    private final Attribute<O, A> uniqueAttribute;

    public DeduplicatingResultSet(Attribute<O, A> uniqueAttribute, ResultSet<O> wrappedResultSet) {
        this.uniqueAttribute = uniqueAttribute;
        this.wrappedResultSet = wrappedResultSet;
    }

    @Override
    public Iterator<O> iterator() {
        return new DeduplicatingIterator<O, A>(uniqueAttribute, wrappedResultSet.iterator());
    }

    @Override
    public final boolean contains(O object) {
        // Check if this ResultSet contains the given object by iterating the ResultSet...
        return IteratorUtil.iterableContains(this, object);
    }

    @Override
    public int size() {
        return IteratorUtil.countElements(this);
    }

    @Override
    public int getRetrievalCost() {
        return wrappedResultSet.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
        return wrappedResultSet.getMergeCost();
    }
}
