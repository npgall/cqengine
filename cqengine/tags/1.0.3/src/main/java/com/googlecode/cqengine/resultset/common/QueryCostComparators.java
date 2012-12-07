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
package com.googlecode.cqengine.resultset.common;

import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Comparator;

/**
 * Stateless comparator singletons for {@link ResultSet}s based on {@link ResultSet#getRetrievalCost()} and
 * {@link com.googlecode.cqengine.resultset.ResultSet#getMergeCost()}.
 * <p/>
 *
 * @author Niall Gallagher
 */
public class QueryCostComparators {

    private static final Comparator<ResultSet> RETRIEVAL_COST_COMPARATOR = new RetrievalCostComparator();
    private static final Comparator<ResultSet> MERGE_COST_COMPARATOR = new MergeCostComparator();

    public static Comparator<ResultSet> getRetrievalCostComparator() {
        return RETRIEVAL_COST_COMPARATOR;
    }

    public static Comparator<ResultSet> getMergeCostComparator() {
        return MERGE_COST_COMPARATOR;
    }

    static class RetrievalCostComparator implements Comparator<ResultSet> {

        @Override
        public int compare(ResultSet o1, ResultSet o2) {
            if (o1.getRetrievalCost() < o2.getRetrievalCost()) {
                return -1;
            }
            else if (o1.getRetrievalCost() > o2.getRetrievalCost()) {
                return +1;
            }
            else {
                return 0;
            }
        }
    }

    static class MergeCostComparator implements Comparator<ResultSet> {

        @Override
        public int compare(ResultSet o1, ResultSet o2) {
            if (o1.getMergeCost() < o2.getMergeCost()) {
                return -1;
            }
            else if (o1.getMergeCost() > o2.getMergeCost()) {
                return +1;
            }
            else {
                return 0;
            }
        }
    }
}
