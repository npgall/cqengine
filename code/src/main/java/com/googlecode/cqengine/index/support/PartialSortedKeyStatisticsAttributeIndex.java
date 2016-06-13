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

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * A {@link PartialIndex} which supports the index statistics interface {@link SortedKeyStatisticsAttributeIndex}.
 *
 * @author niall.gallagher
 */
public abstract class PartialSortedKeyStatisticsAttributeIndex<A extends Comparable<A>, O> extends PartialIndex<A, O, SortedKeyStatisticsAttributeIndex<A, O>> implements SortedKeyStatisticsAttributeIndex<A, O> {

    /**
     * Protected constructor, called by subclasses.
     *
     * @param attribute The attribute on which the index is built.
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     */
    protected PartialSortedKeyStatisticsAttributeIndex(Attribute<O, A> attribute, Query<O> filterQuery) {
        super(attribute, filterQuery);
    }

    @Override
    public CloseableIterable<A> getDistinctKeys(QueryOptions queryOptions) {
        return backingIndex().getDistinctKeys(queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeys(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return backingIndex().getDistinctKeys(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeysDescending(QueryOptions queryOptions) {
        return backingIndex().getDistinctKeysDescending(queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeysDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return backingIndex().getDistinctKeysDescending(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeysDescending(QueryOptions queryOptions) {
        return backingIndex().getStatisticsForDistinctKeysDescending(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(QueryOptions queryOptions) {
        return backingIndex().getKeysAndValues(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return backingIndex().getKeysAndValues(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(QueryOptions queryOptions) {
        return backingIndex().getKeysAndValuesDescending(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return backingIndex().getKeysAndValuesDescending(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions);
    }

    @Override
    public Integer getCountForKey(A key, QueryOptions queryOptions) {
        return backingIndex().getCountForKey(key, queryOptions);
    }

    @Override
    public Integer getCountOfDistinctKeys(QueryOptions queryOptions) {
        return backingIndex().getCountOfDistinctKeys(queryOptions);
    }

    @Override
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeys(QueryOptions queryOptions) {
        return backingIndex().getStatisticsForDistinctKeys(queryOptions);
    }
}
