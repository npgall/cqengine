package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An iterator which wraps another and prevents duplicate objects from being returned.
 * <p/>
 * The implementation starts returning objects from the wrapped iterator immediately, and records during iteration the
 * objects issued so far in a temporary collection. If the wrapped iterator returns the same object more than once,
 * this iterator will transparently skip it and move to the next object.
 *
 * @author niall.gallagher
 */
public class DeduplicatingMaterializingIterator<O> extends UnmodifiableIterator<O> {

    final Iterator<O> backingIterator;

    final Set<O> materializedSet = new HashSet<O>();

    O nextObject = null;

    public DeduplicatingMaterializingIterator(Iterator<O> backingIterator) {
        this.backingIterator = backingIterator;
    }

    @Override
    public boolean hasNext() {
        if (nextObject != null) {
            return true;
        }
        while(backingIterator.hasNext()) {
            O next = backingIterator.next();
            if (next == null) {
                throw new IllegalStateException("Unexpectedly received null from the backing iterator's iterator.next()");
            }
            if (!materializedSet.add(next)) {
                continue;
            }
            nextObject = next;
            return true;
        }
        nextObject = null;
        materializedSet.clear(); // ..help GC
        return false;
    }

    @Override
    public O next() {
        O next = nextObject;
        if (next == null) {
            throw new IllegalStateException("Detected an attempt to call iterator.next() without calling iterator.hasNext() immediately beforehand");
        }
        nextObject = null;
        return next;
    }
}
