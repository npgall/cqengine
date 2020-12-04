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
import com.googlecode.cqengine.resultset.filter.MaterializedDeduplicatedIterator;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.Iterator;

/**
 * Wraps another {@link ResultSet} and ensures that the {@link Iterator} returned by the {@link #iterator()} method
 * never returns the same object more than once.
 * <p/>
 * The implementation delegates to {@link MaterializedDeduplicatedIterator}.
 * <p/>
 * Note that the {@link #size()} method in this implementation has O(n) time complexity, because it uses the
 * deduplicating iterator to count objects.
 *
 * @author Niall Gallagher
 */
public class MaterializedDeduplicatedResultSet<O> extends WrappedResultSet<O> {

    public MaterializedDeduplicatedResultSet(ResultSet<O> wrappedResultSet) {
        super(wrappedResultSet);
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
        return IteratorUtil.materializedDeduplicate(super.iterator());
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
