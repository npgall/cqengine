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
package com.googlecode.cqengine.index.support;

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

    /**
     * Returns the statistics {@link KeyStatistics} for all distinct keys in the index,  in descending order
     *
     * @param queryOptions Optional parameters for the query
     * @return The statistics {@link KeyStatistics} for all distinct keys in the index,  in descending order
     */
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeysDescending(QueryOptions queryOptions);

    /**
     * Returns the keys and corresponding values for those keys in the index. Note the same key
     * will be returned multiple times if more than one object has the same key. Also the same value might be returned
     * multiple times, each time for a different key, if the index is built on a multi-value attribute.
     *
     * @return The keys and corresponding values for those keys in the index, in ascending order of key
     *
     * @param queryOptions Optional parameters for the query
     */
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(QueryOptions queryOptions);

    /**
     * Returns the keys within an optional range and corresponding values for those keys in the index. Note the same key
     * will be returned multiple times if more than one object has the same key. Also the same value might be returned
     * multiple times, each time for a different key, if the index is built on a multi-value attribute.
     *
     * @param lowerBound The lower bound for the keys returned, or null if no lower bound should be applied
     * @param lowerInclusive true if the lowerBound is inclusive, false if exclusive
     * @param upperBound The upper bound for the keys returned, or null if no upper bound should be applied
     * @param upperInclusive true if the lowerBound is inclusive, false if exclusive
     * @param queryOptions Optional parameters for the query
     *
     * @return The keys and corresponding values for those keys in the index, in ascending order of key
     */
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions);

    /**
     * Returns the keys and corresponding values for those keys in the index. Note the same key
     * will be returned multiple times if more than one object has the same key. Also the same value might be returned
     * multiple times, each time for a different key, if the index is built on a multi-value attribute.
     *
     * @return The keys and corresponding values for those keys in the index, in ascending order of key
     *
     * @param queryOptions Optional parameters for the query
     */
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(QueryOptions queryOptions);

    /**
     * Returns the keys within an optional range and corresponding values for those keys in the index. Note the same key
     * will be returned multiple times if more than one object has the same key. Also the same value might be returned
     * multiple times, each time for a different key, if the index is built on a multi-value attribute.
     *
     * @param lowerBound The lower bound for the keys returned, or null if no lower bound should be applied
     * @param lowerInclusive true if the lowerBound is inclusive, false if exclusive
     * @param upperBound The upper bound for the keys returned, or null if no upper bound should be applied
     * @param upperInclusive true if the lowerBound is inclusive, false if exclusive
     * @param queryOptions Optional parameters for the query
     *
     * @return The keys and corresponding values for those keys in the index, in descending order of key
     */
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions);


}
