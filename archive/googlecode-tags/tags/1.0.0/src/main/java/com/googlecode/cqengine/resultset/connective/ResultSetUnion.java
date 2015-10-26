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

import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.filter.FilteringIterator;
import com.googlecode.cqengine.resultset.iterator.ConcatenatingIterator;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.ArrayList;
import java.util.Collection;
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

    // ResultSets (not in any particular order)...
    private final Iterable<?extends ResultSet<O>> resultSets;

    public ResultSetUnion(Iterable<? extends ResultSet<O>> resultSets) {
        this.resultSets = resultSets;
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
        return new FilteringIterator<O>(unionAllIterator) {
            @Override
            public boolean isValid(O object) {
                for (ResultSet<O> resultSet : resultSetsAlreadyIterated) {
                    if (resultSet.contains(object)) {
                        return false;
                    }
                }
                return true;
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
        return anyResultSetContains(this.resultSets, object);
    }

    static <O> boolean anyResultSetContains(Iterable<? extends ResultSet<O>> resultSets, O object) {
        for (ResultSet<O> resultSet : resultSets) {
            if (resultSet.contains(object)) {
                return true;
            }
        }
        return false;
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
        int retrievalCost = 0;
        for (ResultSet<O> resultSet : this.resultSets) {
            retrievalCost = retrievalCost + resultSet.getRetrievalCost();
        }
        return retrievalCost;
    }

    /**
     * Returns the sum of the merge costs of the the underlying {@code ResultSet}s.
     * @return the sum of the merge costs of the the underlying {@code ResultSet}s
     */
    @Override
    public int getMergeCost() {
        int mergeCost = 0;
        for (ResultSet<O> resultSet : this.resultSets) {
            mergeCost = mergeCost + resultSet.getMergeCost();
        }
        return mergeCost;
    }
}
