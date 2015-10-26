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
package com.googlecode.cqengine.resultset.connective;

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

    // ResultSets sorted in ascending order of merge cost...
    private final List<ResultSet<O>> resultSets;

    public ResultSetIntersection(Iterable<ResultSet<O>> resultSets) {
        // Sort the supplied result sets in ascending order of merge cost...
        List<ResultSet<O>> sortedResultSets = new ArrayList<ResultSet<O>>();
        for (ResultSet<O> resultSet : resultSets){
            sortedResultSets.add(resultSet);
        }
        Collections.sort(sortedResultSets, QueryCostComparators.getMergeCostComparator());
        this.resultSets = sortedResultSets;
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
        return new FilteringIterator<O>(lowestMergeCostResultSet.iterator()) {
            @Override
            public boolean isValid(O object) {
                return allResultSetsContain(moreExpensiveResultSets, object);
            }
        };
    }

    /**
     * Returns true if the given object is contained in <b><u>all</u></b> underlying ResultSets.
     * @param object An object to check if contained
     * @return true if the given object is contained in <b><u>all</u></b> underlying ResultSets, false if it is not
     * contained in one or more ResultSets or if there are no underlying result sets
     */
    @Override
    public boolean contains(O object) {
        return allResultSetsContain(this.resultSets, object);
    }

    static <O> boolean allResultSetsContain(Collection<ResultSet<O>> resultSets, O object) {
        if (resultSets.isEmpty()) {
            return false;
        }
        for (ResultSet<O> resultSet : resultSets) {
            if (!resultSet.contains(object)) {
                return false;
            }
        }
        return true;
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
}
