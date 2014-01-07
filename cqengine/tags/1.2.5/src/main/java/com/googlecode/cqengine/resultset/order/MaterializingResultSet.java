/**
 * Copyright 2012 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.resultset.order;

import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Wraps another {@link ResultSet} and ensures that the {@link Iterator} returned by the {@link #iterator()} method
 * never returns the same object more than once.
 * <p/>
 * This {@link ResultSet} does not materialize all objects up-front, rather it starts returning objects from the
 * wrapped {@link ResultSet} immediately, and records during iteration the objects issued so far in a temporary
 * collection. If the wrapped {@link ResultSet} returns the same object more than once, this {@link ResultSet} will
 * transparently skip it and move to the next object.
 * <p/>
 * Note that the {@link #size()} method in this implementation has O(n) time complexity, because it uses the
 * deduplicating iterator to count objects.
 *
 * @author Niall Gallagher
 */
public class MaterializingResultSet<O> extends ResultSet<O> {

    private final ResultSet<O> wrappedResultSet;

    public MaterializingResultSet(ResultSet<O> wrappedResultSet) {
        this.wrappedResultSet = wrappedResultSet;
    }

    /**
     * Returns an {@link Iterator} which does not return the same object more than once.
     * <p/>
     * See class JavaDocs for more details. This implementation has <code>O(merge_cost)</code> time complexity.
     *
     * @return An {@link Iterator} which does not return the same object more than once
     */
    @Override
    public Iterator<O> iterator() {
        return new UnmodifiableIterator<O>() {

            Iterator<O> wrappedIterator = wrappedResultSet.iterator();
            Set<O> materializedSet = new HashSet<O>();

            O nextObject = null;

            @Override
            public boolean hasNext() {
                while(wrappedIterator.hasNext()) {
                    O next = wrappedIterator.next();
                    if (next == null) {
                        throw new IllegalStateException("Unexpectedly received null from the wrapped ResultSet's iterator.next()");
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
        };
    }

    /**
     * Delegates to the {@link ResultSet#contains(Object)} method of the wrapped {@link ResultSet}.
     *
     * @param object The object to check for containment in this {@link ResultSet}
     * @return True if this {@link ResultSet} contains the given object, false if it does not
     */
    @Override
    public boolean contains(O object) {
        return wrappedResultSet.contains(object);
    }

    /**
     * Returns the retrieval cost of the wrapped {@link ResultSet}.
     * @return The retrieval cost of the wrapped {@link ResultSet}
     */
    @Override
    public int getRetrievalCost() {
        return wrappedResultSet.getRetrievalCost();
    }
    
    /**
     * Returns the merge cost of the wrapped {@link ResultSet}.
     * @return The merge cost of the wrapped {@link ResultSet}
     */
    @Override
    public int getMergeCost() {
        return wrappedResultSet.getMergeCost();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation has <code>O(merge_cost)</code> time complexity, because it delegates to
     * the {@link #iterator()} method and counts objects returned.
     */
    @Override
    public int size() {
        return IteratorUtil.countElements(this);
    }
}
