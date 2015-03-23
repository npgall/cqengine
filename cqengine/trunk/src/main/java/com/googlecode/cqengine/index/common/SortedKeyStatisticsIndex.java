package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.index.offheap.support.CloseableIterable;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * An index which allows the set of distinct keys to be queried in sorted order, and which can return statistics on the
 * number of objects stored for each key.
 *
 * Created by niall.gallagher on 09/01/2015.
 */
public interface SortedKeyStatisticsIndex<A extends Comparable<A>, O> extends KeyStatisticsIndex<A, O> {

    /**
     * Returns the distinct keys in the index, in ascending order.
     * @return The distinct keys in the index, in ascending order
     * @param queryOptions Optional parameters for the query
     */
    @Override
    public CloseableIterable<A> getDistinctKeys(QueryOptions queryOptions);

    /**
     * Returns distinct keys within an optional range from the index, in ascending order.
     *
     * @param lowerBound The lower bound for the keys returned, or null if no lower bound should be applied
     * @param lowerInclusive true if the lowerBound is inclusive, false if exclusive
     * @param upperBound The upper bound for the keys returned, or null if no upper bound should be applied
     * @param upperInclusive true if the lowerBound is inclusive, false if exclusive
     * @param queryOptions Optional parameters for the query
     * @return The distinct keys in the index within the given bounds, in ascending order
     */
    public CloseableIterable<A> getDistinctKeys(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions);

    /**
     * Returns the distinct keys in the index, in descending order.
     * @return The distinct keys in the index, in descending order
     * @param queryOptions Optional parameters for the query
     */
    public CloseableIterable<A> getDistinctKeysDescending(QueryOptions queryOptions);

    /**
     * Returns distinct keys within an optional range from the index, in descending order.
     *
     * @param lowerBound The lower bound for the keys returned, or null if no lower bound should be applied
     * @param lowerInclusive true if the lowerBound is inclusive, false if exclusive
     * @param upperBound The upper bound for the keys returned, or null if no upper bound should be applied
     * @param upperInclusive true if the lowerBound is inclusive, false if exclusive
     * @param queryOptions Optional parameters for the query
     * @return The distinct keys in the index within the given bounds, in descending order
     */
    public CloseableIterable<A> getDistinctKeysDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions);
}
