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
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.common.ResultSets;
import com.googlecode.cqengine.resultset.filter.FilteringIterator;
import com.googlecode.cqengine.resultset.iterator.ConcatenatingIterator;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A ResultSet which provides a view onto the union of other ResultSets, with deduplication.
 * <p/>
 * This is equivalent to UNION in SQL terminology, i.e. duplicates are eliminated.
 *
 * @author Niall Gallagher
 */
public class ResultSetUnion<O> extends ResultSet<O> {

    final Query<O> query;
    // ResultSets (not in any particular order)...
    final Iterable<?extends ResultSet<O>> resultSets;
    final QueryOptions queryOptions;
    final boolean useIndexMergeStrategy;

    public ResultSetUnion(Iterable<? extends ResultSet<O>> resultSets, Query<O> query, QueryOptions queryOptions) {
        this(resultSets, query, queryOptions, false);
    }

    public ResultSetUnion(Iterable<? extends ResultSet<O>> resultSets, Query<O> query, QueryOptions queryOptions, boolean useIndexMergeStrategy) {
        List<ResultSet<O>> costCachingResultSets = ResultSets.wrapWithCostCachingIfNecessary(resultSets);
        this.resultSets = costCachingResultSets;
        this.query = query;
        this.queryOptions = queryOptions;
        this.useIndexMergeStrategy = useIndexMergeStrategy;
    }

    @Override
    public Iterator<O> iterator() {
        // Maintain a list of ResultSets which have already been iterated, excluding the one currently being iterated...
        final List<ResultSet<O>> resultSetsAlreadyIterated = new ArrayList<ResultSet<O>>();

        // An iterator which concatenates all ResultSets together, effectively implementing UNION ALL.
        // When moving on to the next ResultSet, this iterator adds the ResultSet it has just iterated
        // to the list of resultSetsAlreadyIterated above...
        Iterator<O> unionAllIterator = new ConcatenatingIterator<O>() {
            private Iterator<? extends ResultSet<O>> resultSetsIterator = resultSets.iterator();
            private ResultSet<O> currentResultSet = null;
            @Override
            public Iterator<O> getNextIterator() {
                if (currentResultSet != null ) {
                    resultSetsAlreadyIterated.add(currentResultSet);
                }
                if (resultSetsIterator.hasNext()) {
                    currentResultSet = resultSetsIterator.next();
                    return currentResultSet.iterator();
                }
                else {
                    currentResultSet = null;
                    return null;
                }
            }
        };

        // An iterator which wraps the UNION ALL iterator, filtering out objects which are contained in ResultSets
        // iterated earlier - so effectively implementing UNION (with duplicates eliminated)...
        if (useIndexMergeStrategy) {
            return new FilteringIterator<O>(unionAllIterator, queryOptions) {
                @Override
                public boolean isValid(O object, QueryOptions queryOptions) {
                    for (ResultSet<O> resultSet : resultSetsAlreadyIterated) {
                        if (resultSet.contains(object)) {
                            return false;
                        }
                    }
                    return true;
                }
            };
        }
        else {
            return new FilteringIterator<O>(unionAllIterator, queryOptions) {
                @Override
                public boolean isValid(O object, QueryOptions queryOptions) {
                    for (ResultSet<O> resultSet : resultSetsAlreadyIterated) {
                        if (resultSet.matches(object)) {
                            return false;
                        }
                    }
                    return true;
                }
            };
        }
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
     * Returns the number of distinct objects in the the underlying {@code ResultSet}s, with duplicates eliminated.
     * @return the number of distinct objects in the the underlying {@code ResultSet}s, with duplicates eliminated
     */
    @Override
    public int size() {
        return IteratorUtil.countElements(this);
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
