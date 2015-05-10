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
package com.googlecode.cqengine.resultset.connective;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.filter.FilteringIterator;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.*;

/**
 * A ResultSet which provides a view onto the set difference of two ResultSets.
 * <p/>
 * The set difference is elements contained in the first ResultSet which are NOT contained in the second ResultSet.
 *
 * @author Niall Gallagher
 */
public class ResultSetDifference<O> extends ResultSet<O> {

    final ResultSet<O> firstResultSet;
    final ResultSet<O> secondResultSet;
    final Query<O> query;
    final QueryOptions queryOptions;

    public ResultSetDifference(ResultSet<O> firstResultSet, ResultSet<O> secondResultSet, Query<O> query, QueryOptions queryOptions) {
        this.firstResultSet = firstResultSet;
        this.secondResultSet = secondResultSet;
        this.query = query;
        this.queryOptions = queryOptions;
    }

    @Override
    public Iterator<O> iterator() {
        return new FilteringIterator<O>(firstResultSet.iterator(), queryOptions) {
            @Override
            public boolean isValid(O object, QueryOptions queryOptions) {
                return !secondResultSet.contains(object);
            }
        };
    }

    /**
     * Returns true if the given object is contained in the first ResultSet,
     * but is NOT contained in the second ResultSet.
     *
     * @param object An object to check if contained
     * @return true if the given object is contained in the first ResultSet,
     * but is NOT contained in the second ResultSet, otherwise false
     */
    @Override
    public boolean contains(O object) {
        return firstResultSet.contains(object) && !secondResultSet.contains(object);
    }

    @Override
    public int size() {
        return IteratorUtil.countElements(this);
    }

    /**
     * Returns the retrieval cost from the first ResultSet.
     * @return the retrieval cost from the first ResultSet
     */
    @Override
    public int getRetrievalCost() {
        return firstResultSet.getRetrievalCost();
    }

    /**
     * Returns the merge cost from the first ResultSet.
     * @return the merge cost from the first ResultSet
     */
    @Override
    public int getMergeCost() {
        return firstResultSet.getMergeCost();
    }

    @Override
    public void close() {
        firstResultSet.close();
        secondResultSet.close();
    }

    @Override
    public Query<O> getQuery() {
        return query;
    }
}
