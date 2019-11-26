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
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.KeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.KeyStatisticsIndex;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsIndex;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Provides access to metadata for attributes, obtained from indexes which have been added to the collection.
 * Example metadata:
 * <ul>
 *     <li>
 *         frequency distributions (counts of the occurrences of each distinct value of an attribute in the collection)
 *     </li>
 *     <li>
 *         count the number of objects in the collection whose attribute has a specific value
 *     </li>
 *     <li>
 *         obtain all of the distinct values of an attribute
 *     </li>
 * </ul>
 * <p>
 * The {@link #getAttributeMetadata(Attribute)} method returns an {@link AttributeMetadata} accessor object,
 * which can provide access to basic metadata as discussed above for a given attribute, in unsorted order.
 * This requires in advance, an index which implements the {@link KeyStatisticsAttributeIndex} interface,
 * to be added to the collection on the given attribute. Most indexes implement that interface.
 * <p>
 * The {@link #getSortedAttributeMetadata(Attribute)} method returns a {@link SortedAttributeMetadata} accessor object,
 * which can provide access to additional metadata and in sorted order. It allows attribute values to be traversed
 * in ascending or descending order, and it supports range queries. This requires in advance, an index which implements
 * the {@link SortedKeyStatisticsAttributeIndex} interface, to be added to the collection on the given attribute.
 * That interface is implemented by many indexes.
 * <p>
 * This object can be accessed by calling {@link IndexedCollection#getMetadataEngine()}.
 */
public class MetadataEngine<O> {

    private final IndexedCollection<O> indexedCollection;
    private final Supplier<QueryOptions> openResourcesHandler;
    private final Consumer<QueryOptions> closeResourcesHandler;

    public MetadataEngine(IndexedCollection<O> indexedCollection, Supplier<QueryOptions> openResourcesHandler, Consumer<QueryOptions> closeResourcesHandler) {
        this.indexedCollection = indexedCollection;
        this.openResourcesHandler = openResourcesHandler;
        this.closeResourcesHandler = closeResourcesHandler;
    }

    /**
     * Returns an {@link AttributeMetadata} accessor object, which can provide access to basic metadata
     * for a given attribute, in unsorted order.
     * <p>
     * This requires in advance, an index which implements the {@link KeyStatisticsAttributeIndex} interface,
     * to be added to the collection on the given attribute.
     *
     * @param attribute The attribute for which metadata is required
     * @return an {@link AttributeMetadata} accessor object
     * @throws IllegalStateException if no suitable index has been added to the collection
     */
    public <A> AttributeMetadata<A, O> getAttributeMetadata(Attribute<O, A> attribute) {
        @SuppressWarnings("unchecked")
        KeyStatisticsIndex<A, O> index = getIndexOnAttribute(KeyStatisticsAttributeIndex.class, attribute);
        return new AttributeMetadata<>(index, openResourcesHandler, closeResourcesHandler);
    }

    /**
     * Returns a {@link SortedAttributeMetadata} accessor object, which can provide access to metadata in sorted order
     * for a given attribute. It allows attribute values to be traversed in ascending or descending order, and it
     * supports range queries.
     * <p>
     * This requires in advance, an index which implements the {@link SortedKeyStatisticsAttributeIndex} interface,
     * to be added to the collection on the given attribute.
     *
     * @param attribute The attribute for which metadata is required
     * @return an {@link AttributeMetadata} accessor object
     * @throws IllegalStateException if no suitable index has been added to the collection
     */
    public <A extends Comparable<A>> SortedAttributeMetadata<A, O> getSortedAttributeMetadata(Attribute<O, A> attribute) {
        @SuppressWarnings("unchecked")
        SortedKeyStatisticsIndex<A, O> index = getIndexOnAttribute(SortedKeyStatisticsAttributeIndex.class, attribute);
        return new SortedAttributeMetadata<>(index, openResourcesHandler, closeResourcesHandler);
    }

    private <A, I extends AttributeIndex<A, O>> I getIndexOnAttribute(Class<I> indexType, Attribute<O, A> attribute) {
        for (Index<O> index : indexedCollection.getIndexes()) {
            if (indexType.isAssignableFrom(index.getClass())) {
                I attributeIndex = indexType.cast(index);
                if (attributeIndex.getAttribute().equals(attribute)) {
                    return attributeIndex;
                }
            }
        }
        throw new IllegalStateException("A " + indexType.getSimpleName() + " has not been added to the collection, and must be added first, to enable metadata to be examined for attribute: " + attribute);
    }
}
