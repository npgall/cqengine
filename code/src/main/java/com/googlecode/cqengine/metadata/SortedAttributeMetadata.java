/**
 * Copyright 2012-2019 Niall Gallagher
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
package com.googlecode.cqengine.metadata;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.support.KeyValue;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsIndex;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Provides access to metadata in sorted order for a given attribute. Allows attribute values to be traversed
 * in ascending or descending order, and supports range queries. See {@link MetadataEngine} for more details.
 * <p>
 * This requires in advance, an index which implements the {@link SortedKeyStatisticsAttributeIndex} interface,
 * to be added to the collection on the given attribute.
 * <p>
 * This object can be accessed first by calling {@link IndexedCollection#getMetadataEngine()} to access the
 * {@link MetadataEngine}, and then by calling {@link MetadataEngine#getSortedAttributeMetadata(Attribute)} for a given
 * attribute.
 */
public class SortedAttributeMetadata<A extends Comparable<A>, O> extends AttributeMetadata<A, O> {

    private final SortedKeyStatisticsIndex<A, O> index;

    SortedAttributeMetadata(SortedKeyStatisticsIndex<A, O> index, Supplier<QueryOptions> openResourcesHandler, Consumer<QueryOptions> closeResourcesHandler) {
        super(index, openResourcesHandler, closeResourcesHandler);
        this.index = index;
    }


    /**
     * Returns the frequencies of distinct keys (a.k.a. attribute values) in the index, in ascending order.
     * <p>
     * The {@link KeyFrequency} objects encapsulate a key (a.k.a. attribute value), and the frequency (or count)
     * of how many objects in the collection match that key.
     */
    @Override
    public Stream<KeyFrequency<A>> getFrequencyDistribution() {
        return super.getFrequencyDistribution();
    }

    /**
     * Returns the frequencies of distinct keys (a.k.a. attribute values) in the index, in descending order.
     * <p>
     * The {@link KeyFrequency} objects encapsulate a key (a.k.a. attribute value), and the frequency (or count)
     * of how many objects in the collection match that key.
     */
    public Stream<KeyFrequency<A>> getFrequencyDistributionDescending() {
        QueryOptions queryOptions = openResources();
        return asKeyFrequencyStream(queryOptions, index.getStatisticsForDistinctKeysDescending(queryOptions));
    }

    /**
     * Returns the distinct keys in the index, in ascending order.
     */
    @Override
    public Stream<A> getDistinctKeys() {
        return super.getDistinctKeys();
    }

    /**
     * Returns the distinct keys in the index, in descending order.
     */
    public Stream<A> getDistinctKeysDescending() {
        QueryOptions queryOptions = openResources();
        return asStream(queryOptions, index.getDistinctKeysDescending(queryOptions));
    }

    /**
     * Returns the distinct keys in the index within an optional range, in ascending order.
     *
     * @param lowerBound The lower bound for the keys returned, or null if no lower bound should be applied
     * @param lowerInclusive true if the lowerBound is inclusive, false if exclusive
     * @param upperBound The upper bound for the keys returned, or null if no upper bound should be applied
     * @param upperInclusive true if the lowerBound is inclusive, false if exclusive
     * @return The distinct keys in the index within an optional range, in ascending order
     */
    public Stream<A> getDistinctKeys(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive) {
        QueryOptions queryOptions = openResources();
        return asStream(queryOptions, index.getDistinctKeys(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions));
    }

    /**
     * Returns the distinct keys in the index within an optional range, in descending order.
     *
     * @param lowerBound The lower bound for the keys returned, or null if no lower bound should be applied
     * @param lowerInclusive true if the lowerBound is inclusive, false if exclusive
     * @param upperBound The upper bound for the keys returned, or null if no upper bound should be applied
     * @param upperInclusive true if the lowerBound is inclusive, false if exclusive
     * @return The distinct keys in the index within an optional range, in descending order
     */
    public Stream<A> getDistinctKeysDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive) {
        QueryOptions queryOptions = openResources();
        return asStream(queryOptions, index.getDistinctKeysDescending(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions));
    }

    /**
     * Returns the count of distinct keys in the index.
     */
    @Override
    public Integer getCountOfDistinctKeys() {
        return super.getCountOfDistinctKeys();
    }

    /**
     * Returns the number of objects in the index which match the given key.
     */
    @Override
    public Integer getCountForKey(A key) {
        return super.getCountForKey(key);
    }

    /**
     * Returns the keys (a.k.a. attribute values) and the objects they match in the index, in ascending order of key.
     * <p>
     * Note the same key will be returned multiple times if more than one object has the same key. Also the same object
     * might be returned multiple times, each time for a different key, if the index is built on a multi-value attribute.
     *
     * @return The keys and objects they match in the index, in ascending order of key
     */
    @Override
    public Stream<KeyValue<A, O>> getKeysAndValues() {
        return super.getKeysAndValues();
    }

    /**
     * Returns the keys (a.k.a. attribute values) and the objects they match in the index, in descending order of key.
     * <p>
     * Note the same key will be returned multiple times if more than one object has the same key. Also the same object
     * might be returned multiple times, each time for a different key, if the index is built on a multi-value attribute.
     *
     * @return The keys and objects they match in the index, in descending order of key
     */
    public Stream<KeyValue<A, O>> getKeysAndValuesDescending() {
        QueryOptions queryOptions = openResources();
        return asStream(queryOptions, index.getKeysAndValuesDescending(queryOptions));
    }


    /**
     * Returns the keys (a.k.a. attribute values) within an optional range, and the objects they match in the index,
     * in ascending order of key.
     * <p>
     * Note the same key will be returned multiple times if more than one object has the same key. Also the same object
     * might be returned multiple times, each time for a different key, if the index is built on a multi-value attribute.
     *
     * @param lowerBound The lower bound for the keys returned, or null if no lower bound should be applied
     * @param lowerInclusive true if the lowerBound is inclusive, false if exclusive
     * @param upperBound The upper bound for the keys returned, or null if no upper bound should be applied
     * @param upperInclusive true if the lowerBound is inclusive, false if exclusive
     *
     * @return The keys and objects they match in the index, in ascending order of key
     */
    public Stream<KeyValue<A, O>> getKeysAndValues(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive) {
        QueryOptions queryOptions = openResources();
        return asStream(queryOptions, index.getKeysAndValues(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions));
    }

    /**
     * Returns the keys (a.k.a. attribute values) within an optional range, and the objects they match in the index,
     * in descending order of key.
     * <p>
     * Note the same key will be returned multiple times if more than one object has the same key. Also the same object
     * might be returned multiple times, each time for a different key, if the index is built on a multi-value attribute.
     *
     * @param lowerBound The lower bound for the keys returned, or null if no lower bound should be applied
     * @param lowerInclusive true if the lowerBound is inclusive, false if exclusive
     * @param upperBound The upper bound for the keys returned, or null if no upper bound should be applied
     * @param upperInclusive true if the lowerBound is inclusive, false if exclusive
     *
     * @return The keys and objects they match in the index, in descending order of key
     */
    public Stream<KeyValue<A, O>> getKeysAndValuesDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive) {
        QueryOptions queryOptions = openResources();
        return asStream(queryOptions, index.getKeysAndValuesDescending(lowerBound, lowerInclusive, upperBound, upperInclusive, queryOptions));
    }


}
