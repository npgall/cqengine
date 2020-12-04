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
 * {@code ResultSet}.
 * <p/>
 * This is implemented by copying objects into an intermediate array in memory, and then performing merge-sort
 * on that array.
 * <p>
 * The time complexity for copying the objects into the intermediate array is O(n), and then the cost of sorting is
 * additionally O(n log(n)). So overall complexity is O(n) + O(n log(n)). The {@link #getMergeCost()} method computes
 * an approximation of that time complexity, in addition to accounting for an additional O(n) to iterate the final
 * results.
 *
 * @author Niall Gallagher
 */
public class MaterializedOrderedResultSet<O> extends WrappedResultSet<O> {

    final Comparator<O> comparator;

    /**
     * @param wrappedResultSet The ResultSet to be ordered.
     * @param comparator The comparator to use for ordering
     *
     */
    public MaterializedOrderedResultSet(ResultSet<O> wrappedResultSet, Comparator<O> comparator) {
        super(wrappedResultSet);
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
        return IteratorUtil.materializedSort(super.iterator(), comparator);
    }

    /**
     * Merge cost is an approximation of the cost of iterating all elements in any result set. Therefore, this
     * method attempts to calculate that approximately.
     * <p>
     * The time complexity for copying the objects into the intermediate array is O(n). The cost of sorting that array
     * is O(n log(n)). The cost to iterate that resulting array is then O(n).
     * So overall complexity is O(2n) + O(n log(n)).
     *
     * @return Where merge_cost is the {@link ResultSet#getMergeCost()} of the wrapped result set,
     * this will return ((2 * merge_cost) + (merge_cost * log(merge_cost)))
     */
    @Override
    public int getMergeCost() {
        long mergeCost = super.getMergeCost();
        mergeCost = (2 * mergeCost) + (mergeCost * (long)Math.log(mergeCost));
        mergeCost = mergeCost < 0 ? Long.MAX_VALUE : mergeCost; // in case it overflowed to a negative number
        return (int)Math.min(mergeCost, Integer.MAX_VALUE); // in case it is larger than an int
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return IteratorUtil.countElements(this);
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
