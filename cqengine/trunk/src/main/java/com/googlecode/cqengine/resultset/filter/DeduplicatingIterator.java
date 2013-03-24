package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.attribute.Attribute;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An iterator which wraps another, to suppress duplicate objects, where a duplicate object is one which has
 * the same value(s) for the given attribute.
 * <p/>
 * Say an object had an attribute {@code COLOR}. This iterator would return only one object of each color from the
 * wrapped iterator.
 * <p/>
 * Only one of the potentially duplicate objects will be returned, but which one is unspecified (the query engine is
 * usually free for performance reasons to return objects in any order).
 *
 * @author Niall Gallagher
 */
public class DeduplicatingIterator<O, A> extends FilteringIterator<O> {

    private final Attribute<O, A> uniqueAttribute;

    private final Set<A> attributeValuesProcessed = new HashSet<A>();

    public DeduplicatingIterator(Attribute<O, A> uniqueAttribute, Iterator<O> wrappedIterator) {
        super(wrappedIterator);
        this.uniqueAttribute = uniqueAttribute;
    }

    @Override
    public boolean isValid(O object) {
        return attributeValuesProcessed.addAll(uniqueAttribute.getValues(object));
    }
}
