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

    // ResultSets (not in any particular order)...
    private final Iterable<? extends ResultSet<O>> resultSets;

    public ResultSetUnionAll(Iterable<? extends ResultSet<O>> resultSets) {
        this.resultSets = resultSets;
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
