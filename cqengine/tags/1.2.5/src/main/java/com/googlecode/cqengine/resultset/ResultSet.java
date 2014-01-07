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
package com.googlecode.cqengine.resultset;

import java.util.Iterator;
import com.googlecode.cqengine.resultset.common.*;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

/**
 * @author Niall Gallagher
 */
public abstract class ResultSet<O> implements Iterable<O> {

    /**
     * Returns an {@link Iterator} over objects matching the query for which this {@link ResultSet} was constructed.
     *
     * @return An {@link Iterator} over objects matching the query for which this {@link ResultSet} was constructed
     */
    public abstract Iterator<O> iterator();

    /**
     * Returns true if this {@link ResultSet} contains the given object, false if it does not.
     * <p/>
     * Note that the cost of calling this method depends on the query for which it was constructed, but will be
     * significantly cheaper than iterating all results to check if an object is contained.
     * 
     * @param object The object to check for containment in this {@link ResultSet}
     * @return True if this {@link ResultSet} contains the given object, false if it does not
     */
    public abstract boolean contains(O object);

    /**
     * Returns the first object returned by the iterator of this {@link ResultSet}, and throws an exception if
     * the iterator does not provide exactly one object.
     *
     * @return The first object returned by the iterator of this {@link ResultSet}
     * @throws NoSuchObjectException If the iterator indicates no object is available
     * @throws NonUniqueObjectException If the iterator indicates more than one object is available
     */
    public O uniqueResult() {
        Iterator<O> iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchObjectException("ResultSet is empty");
        }
        O result = iterator.next();
        if (iterator.hasNext()) {
            throw new NonUniqueObjectException("ResultSet contains more than one object");
        }
        return result;
    }

    /**
     * Returns an estimate of the cost of looking up objects matching the query
     * underlying this {@code ResultSet} in the index.
     * <p/>
     * The query engine will use this to select the index with the lowest cost,
     * when more than one index supporting the query exists for the same attribute.
     * <p/>
     * An example: a single-level hash index will typically have a lower retrieval cost than a tree-based index. Of
     * course a hash index only supports equality-based retrieval whereas a sorted tree-based index might common
     * equality/less than/greater than or range based retrieval. But for an equality-based query, supported by
     * both indexes, retrieval cost allows the query engine to <i>prefer</i> the hash index.
     *
     * @return An estimate of the cost of looking up a particular query in the index
     */
    public abstract int getRetrievalCost();

    /**
     * Returns an estimate of the cost of merging (or otherwise processing) objects matching the query.
     * <p/>
     * This will typically be based on the number of objects matching the query.
     * <ul>
     *     <li>
     *         If the query specifies a simple retrieval from an index, this might be the number of objects matching
     *         the query
     *     </li>
     *     <li>
     *         If the query specifies a union between multiple other sub-queries, this might be the sum of their merge
     *         costs
     *     </li>
     *     <li>
     *         If the query specifies an intersection, this might be the based on the merge cost of the sub-query with
     *         the lowest merge cost
     *     </li>
     * </ul>
     * The query engine will use this to optimize the order of intersections and unions, and to decide between merging
     * versus filtering strategies.
     *
     * @return An estimate of the cost of merging (or otherwise processing) objects matching a query
     */
    public abstract int getMergeCost();

    /**
     * Returns the number of objects which would be returned by this {@code ResultSet} if iterated.
     * <p/>
     * Note that the cost of calling this method depends on the query for which it was constructed.
     * <p/>
     * For simple queries where a single query is supplied and a matching index exists, or where several such simple
     * queries are supplied and are connected using a simple {@link com.googlecode.cqengine.query.logical.Or}
     * query, calculating the size via this method will be cheaper than iterating through the ResultSet and counting
     * the number of objects individually.
     * <p/>
     * For more complex queries, where intersections must be performed or where no suitable indexes exist, calling this
     * method can be non-trivial, but it will always be at least as cheap as iterating through the ResultSet and
     * counting the number of objects individually.
     *
     * @return The number of objects which would be returned by this {@code ResultSet} if iterated
     */
    public abstract int size();

    /**
     * Checks if this {@code ResultSet} if iterated would not return any objects (i.e. the query does not match any
     * objects).
     * <p/>
     * This method can be more efficient than calling {@code #size()} to check simply if no objects would be
     * returned.
     *
     * @return True if this {@code ResultSet} if iterated would not return any objects; false if the {@code ResultSet}
     * would return objects
     */
    public boolean isEmpty() {
        return !iterator().hasNext();
    }

    /**
     * Checks if this {@code ResultSet} if iterated would return some objects (i.e. the query matches some
     * objects).
     * <p/>
     * This method can be more efficient than calling {@code #size()} to check simply if some objects would be
     * returned.
     *
     * @return True if this {@code ResultSet} if iterated would return some objects; false if the {@code ResultSet}
     * would not return any objects
     */
    public boolean isNotEmpty() {
        return iterator().hasNext();
    }
}
