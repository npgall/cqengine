/**
 * Copyright 2012-2015 Niall Gallagher
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
import com.googlecode.cqengine.resultset.common.WrappedResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.*;

/**
 * A {@code ResultSet} which wraps another {@code ResultSet}, providing an {@link #iterator()} method which returns
 * objects from the wrapped ResultSet in sorted order according to a comparator supplied to the constructor of this
 * {@code ResultSet}, AND which implicitly deduplicates the objects returned as well.
 * <p/>
 * Note that the {@link #size()} method does not necessarily reflect the outcome of that deduplication.
 * As deduplication is expensive, the constructor accepts a boolean flag to specify if the size() method should perform
 * deduplication.
 * <p/>
 * Deduplication is implemented by copying objects from the wrapped {@link ResultSet} into a sorted set on-demand when
 * the {@link #iterator()} method is called, and then returning an iterator over that set.
 * <p/>
 * Insertion-sorting, and <u>then iterating</u> objects in this manner has
 * <b><code>O(merge_cost^2 * log(merge_cost))</code></b> time complexity.
 * See the {@link #iterator()} and {@link #getMergeCost()} methods for details.
 *
 * @author Niall Gallagher
 */
public class MaterializedOrderedResultSet<O> extends WrappedResultSet<O> {

    final Comparator<O> comparator;
    final boolean deduplicateSize;

    /**
     * @param wrappedResultSet The ResultSet to be ordered.
     * @param comparator The comparator to use for ordering
     * @param deduplicateSize If true, the {@link #size()} method will deduplicate results such that the count it
     *                        returns will match the number of objects returned by the {@link #iterator()} method (but
     *                        this deduplication is expensive); if false, the size() method will not deduplicate, and so
     *                        the size reported might sometimes be greater then the number of objects which the
     *                        {@link #iterator()} method returns (but this computation is inexpensive).
     *
     */
    public MaterializedOrderedResultSet(ResultSet<O> wrappedResultSet, Comparator<O> comparator, boolean deduplicateSize) {
        super(wrappedResultSet);
        this.comparator = comparator;
        this.deduplicateSize = deduplicateSize;
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
        return IteratorUtil.materializedSort(super.iterator(), comparator);
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
        long mergeCost = super.getMergeCost();
        return (int)Math.min(mergeCost * mergeCost, Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Note that whether or not this method deduplicates results is specified by the parameter supplied to the
     * constructor of this ResultSet.
     */
    @Override
    public int size() {
        return deduplicateSize ? IteratorUtil.countElements(this) : super.size();
    }

    /**
     * @return the result of calling this method on the wrapped ResultSet
     */
    @Override
    public boolean isEmpty() {
        return wrappedResultSet.isEmpty();
    }

    /**
     * @return the result of calling this method on the wrapped ResultSet
     */
    @Override
    public boolean isNotEmpty() {
        return wrappedResultSet.isNotEmpty();
    }
}
