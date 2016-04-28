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
import com.googlecode.cqengine.resultset.common.ResultSets;
import com.googlecode.cqengine.resultset.iterator.ConcatenatingIterator;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.*;

/**
 * A ResultSet which provides a view onto the union of other ResultSets, <b>without</b> deduplication.
 * <p/>
 * This is equivalent to UNION ALL in SQL terminology, i.e. duplicates are <b>not</b> eliminated.
 *
 * @author Niall Gallagher
 */
public class ResultSetUnionAll<O> extends ResultSet<O> {

    final Query<O> query;
    final QueryOptions queryOptions;
    // ResultSets (not in any particular order)...
    private final Iterable<? extends ResultSet<O>> resultSets;

    public ResultSetUnionAll(Iterable<? extends ResultSet<O>> resultSets, Query<O> query, QueryOptions queryOptions) {
        this.resultSets = ResultSets.wrapWithCostCachingIfNecessary(resultSets);
        this.query = query;
        this.queryOptions = queryOptions;
    }

    @Override
    public Iterator<O> iterator() {
        return new ConcatenatingIterator<O>() {
            Iterator<? extends ResultSet<O>> resultSetsIterator = resultSets.iterator();
            @Override
            public Iterator<O> getNextIterator() {
                return resultSetsIterator.hasNext() ? resultSetsIterator.next().iterator() : null;
            }
        };
    }

    /**
     * Returns true if the given object is contained in <b><u>any</u></b> underlying ResultSets.
     * @param object An object to check if contained
     * @return true if the given object is contained in <b><u>any</u></b> underlying ResultSets, false if it is not
     * contained in any ResultSets or if there are no underlying result sets
     */
    @Override
    public boolean contains(O object) {
        for (ResultSet<O> resultSet : this.resultSets) {
            if (resultSet.contains(object)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matches(O object) {
        return query.matches(object, queryOptions);
    }

    /**
     * Returns the sum of the sizes of the the underlying {@code ResultSet}s.
     * @return the sum of the sizes of the the underlying {@code ResultSet}s
     */
    @Override
    public int size() {
        int size = 0;
        for (ResultSet<O> resultSet : this.resultSets) {
            size = size + resultSet.size();
        }
        return size;
    }

    /**
     * Returns the sum of the retrieval costs of the the underlying {@code ResultSet}s.
     * @return the sum of the retrieval costs of the the underlying {@code ResultSet}s
     */
    @Override
    public int getRetrievalCost() {
        long retrievalCost = 0;
        for (ResultSet<O> resultSet : this.resultSets) {
            retrievalCost = retrievalCost + resultSet.getRetrievalCost();
        }
        return (int)Math.min(retrievalCost, Integer.MAX_VALUE);
    }

    /**
     * Returns the sum of the merge costs of the the underlying {@code ResultSet}s.
     * @return the sum of the merge costs of the the underlying {@code ResultSet}s
     */
    @Override
    public int getMergeCost() {
        long mergeCost = 0;
        for (ResultSet<O> resultSet : this.resultSets) {
            mergeCost = mergeCost + resultSet.getMergeCost();
        }
        return (int)Math.min(mergeCost, Integer.MAX_VALUE);
    }

    /**
     * Closes all of the underlying {@code ResultSet}s.
     */
    @Override
    public void close() {
        for (ResultSet<O> resultSet : this.resultSets) {
            resultSet.close();
        }
    }

    @Override
    public Query<O> getQuery() {
        return query;
    }

    @Override
    public QueryOptions getQueryOptions() {
        return queryOptions;
    }
}
