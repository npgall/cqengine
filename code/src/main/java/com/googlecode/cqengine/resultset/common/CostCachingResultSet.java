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
package com.googlecode.cqengine.resultset.common;

import com.googlecode.cqengine.resultset.ResultSet;

/**
 * Caches the merge cost and retrieval costs of a wrapped result set.
 * <p>
 * Specifically this is to prevent
 * <code>java.lang.IllegalArgumentException: Comparison method violates its general contract!</code>
 * on Java 7 and later, when the collection is modified concurrently (causing merge costs to change)
 * while ResultSets are being sorted.
 * </p>
 * @author niall.gallagher
 */
public class CostCachingResultSet<O> extends WrappedResultSet<O> {

    volatile int cachedMergeCost = -1;
    volatile int cachedRetrievalCost = -1;

    public CostCachingResultSet(ResultSet<O> wrappedResultSet) {
        super(wrappedResultSet);
    }

    @Override
    public int getRetrievalCost() {
        return cachedRetrievalCost != -1 ? cachedRetrievalCost : (cachedRetrievalCost = super.getRetrievalCost());
    }

    @Override
    public int getMergeCost() {
        return cachedMergeCost != -1 ? cachedMergeCost : (cachedMergeCost = super.getMergeCost());
    }
}
