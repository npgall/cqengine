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

import java.util.*;

/**
 * A {@code ResultSet} which wraps another {@code ResultSet}, providing an {@link #iterator()} method which returns
 * objects in sorted order according to a comparator supplied to the constructor of this {@code ResultSet}, and which
 * never returns the same object more than once.
 * <p/>
 * This is implemented by copying objects from the wrapped {@link ResultSet} into a sorted set on-demand when
 * the {@link #iterator()} method is called, and then returning an iterator over that set.
 * <p/>
 * Insertion-sorting, and <u>then iterating</u> objects in this manner has
 * <b><code>O(merge_cost^2 * log(merge_cost))</code></b> time complexity.
 * See the {@link #iterator()} and {@link #getMergeCost()} methods for details.
 *
 * @author Niall Gallagher
 */
public class MaterializingOrderedResultSet<O> extends ResultSet<O> {

    private final ResultSet<O> wrappedResultSet;
    private final Comparator<O> comparator;

    public MaterializingOrderedResultSet(ResultSet<O> wrappedResultSet, Comparator<O> comparator) {
        this.wrappedResultSet = wrappedResultSet;
        this.comparator = comparator;
    }

    /**
     * Builds an insertion-sorted set from the wrapped {@link ResultSet}, and returns an iterator over the sorted set.
     * <p/>
     * Time complexity for building an insertion sorted set is <code>O(merge_cost * log(merge_cost))</code>, and then
     * iterating it makes this <b><code>O(merge_cost^2 * log(merge_cost))</code></b>. (Merge cost is an approximation
     * of the cost of iterating all elements in any result set.)
     *
     * @return An iterator over a new sorted set built from the wrapped {@link ResultSet}
     */
    @Override
    public Iterator<O> iterator() {
        Set<O> materializedSet = new TreeSet<O>(comparator);
        for (O object : wrappedResultSet) {
            materializedSet.add(object);
        }
        return materializedSet.iterator();
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
     * Time complexity for building an insertion sorted set is <code>O(merge_cost * log(merge_cost))</code>, and then
     * iterating it makes this <b><code>O(merge_cost^2 * log(merge_cost))</code></b>. (Merge cost is an approximation
     * of the cost of iterating all elements in any result set.)
     * <p/>
     * Returns the merge cost of the wrapped {@link ResultSet} <b>squared</b> (as a simplification).
     *
     * @return The merge cost of the wrapped {@link ResultSet} <b>squared</b> (as a simplification)
     */
    @Override
    public int getMergeCost() {
        int mergeCost = wrappedResultSet.getMergeCost();
        return mergeCost * mergeCost;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation has <code>O(merge_cost^2 * log(merge_cost))</code> time complexity, because it delegates to
     * the {@link #iterator()} method and counts objects returned.
     */
    @Override
    public int size() {
        return IteratorUtil.countElements(this);
    }
}
