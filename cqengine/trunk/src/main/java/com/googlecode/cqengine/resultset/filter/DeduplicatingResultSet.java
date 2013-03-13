package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.HashSet;
import java.util.Set;

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
public class DeduplicatingResultSet<O, A> extends FilteringResultSet<O> {

    private final Attribute<O, A> uniqueAttribute;

    private final Set<A> attributeValuesProcessed = new HashSet<A>();

    public DeduplicatingResultSet(Attribute<O, A> uniqueAttribute, ResultSet<O> wrappedResultSet) {
        super(wrappedResultSet);
        this.uniqueAttribute = uniqueAttribute;
    }

    @Override
    public boolean isValid(O object) {
        return attributeValuesProcessed.addAll(uniqueAttribute.getValues(object));
    }
}
