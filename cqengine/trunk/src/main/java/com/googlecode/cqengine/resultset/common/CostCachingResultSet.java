package com.googlecode.cqengine.resultset.common;

import com.googlecode.cqengine.query.option.QueryOptions;
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

    public CostCachingResultSet(ResultSet<O> wrappedResultSet, QueryOptions queryOptions) {
        super(wrappedResultSet, queryOptions);
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
