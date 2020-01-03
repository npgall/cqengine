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
import com.googlecode.cqengine.resultset.filter.FilteringIterator;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.common.QueryCostComparators;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.*;

/**
 * A ResultSet which provides a view onto the intersection of other ResultSets.
 *
 * @author Niall Gallagher
 */
public class ResultSetIntersection<O> extends ResultSet<O> {

    final Query<O> query;
    // ResultSets sorted in ascending order of merge cost...
    final List<ResultSet<O>> resultSets;
    final QueryOptions queryOptions;
    final boolean useIndexMergeStrategy;

    public ResultSetIntersection(Iterable<ResultSet<O>> resultSets, Query<O> query, QueryOptions queryOptions, boolean useIndexMergeStrategy) {
        this.query = query;
        this.queryOptions = queryOptions;
        // Sort the supplied result sets in ascending order of merge cost...
        List<ResultSet<O>> sortedResultSets = ResultSets.wrapWithCostCachingIfNecessary(resultSets);
        Collections.sort(sortedResultSets, QueryCostComparators.getMergeCostComparator());
        this.resultSets = sortedResultSets;
        this.useIndexMergeStrategy = useIndexMergeStrategy;
    }

    @Override
    public Iterator<O> iterator() {
        if (resultSets.isEmpty()) {
            return Collections.<O>emptySet().iterator();
        }
        else if (resultSets.size() == 1) {
            return resultSets.get(0).iterator();
        }
        ResultSet<O> lowestMergeCostResultSet = resultSets.get(0);
        final List<ResultSet<O>> moreExpensiveResultSets = resultSets.subList(1, resultSets.size());
        if (useIndexMergeStrategy) {
            return new FilteringIterator<O>(lowestMergeCostResultSet.iterator(), queryOptions) {
                @Override
                public boolean isValid(O object, QueryOptions queryOptions) {
                    for (ResultSet<O> resultSet : moreExpensiveResultSets) {
                        if (!resultSet.contains(object)) {
                            return false;
                        }
                    }
                    return true;
                }
            };
        }
        else {
            return new FilteringIterator<O>(lowestMergeCostResultSet.iterator(), queryOptions) {
                @Override
                public boolean isValid(O object, QueryOptions queryOptions) {
                    for (ResultSet<O> resultSet : moreExpensiveResultSets) {
                        if (!resultSet.matches(object)) {
                            return false;
                        }
                    }
                    return true;
                }
            };
        }
    }

    /**
     * Returns true if the given object is contained in <b><u>all</u></b> underlying ResultSets.
     * @param object An object to check if contained
     * @return true if the given object is contained in <b><u>all</u></b> underlying ResultSets, false if it is not
     * contained in one or more ResultSets or if there are no underlying result sets
     */
    @Override
    public boolean contains(O object) {
        if (this.resultSets.isEmpty()) {
            return false;
        }
        for (ResultSet<O> resultSet : this.resultSets) {
            if (!resultSet.contains(object)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean matches(O object) {
        return query.matches(object, queryOptions);
    }

    @Override
    public int size() {
        return IteratorUtil.countElements(this);
    }

    /**
     * Returns the retrieval cost from the underlying {@code ResultSet} which has the lowest merge cost.
     * @return the retrieval cost from the underlying {@code ResultSet} which has the lowest merge cost
     */
    @Override
    public int getRetrievalCost() {
        if (resultSets.isEmpty()) {
            return 0;
        }
        else {
            ResultSet<O> lowestMergeCostResultSet = resultSets.get(0);
            return lowestMergeCostResultSet.getRetrievalCost();
        }
    }

    /**
     * Returns the merge cost from the underlying {@code ResultSet} with the lowest merge cost.
     * @return the merge cost from the underlying {@code ResultSet} with the lowest merge cost
     */
    @Override
    public int getMergeCost() {
        if (resultSets.isEmpty()) {
            return 0;
        }
        else {
            ResultSet<O> lowestMergeCostResultSet = resultSets.get(0);
            return lowestMergeCostResultSet.getMergeCost();
        }
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
