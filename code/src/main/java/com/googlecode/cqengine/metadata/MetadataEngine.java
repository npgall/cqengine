package com.googlecode.cqengine.metadata;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.KeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.KeyStatisticsIndex;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsIndex;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MetadataEngine<O> {

    private final IndexedCollection<O> indexedCollection;
    private final Supplier<QueryOptions> openResourcesHandler;
    private final Consumer<QueryOptions> closeResourcesHandler;

    public MetadataEngine(IndexedCollection<O> indexedCollection, Supplier<QueryOptions> openResourcesHandler, Consumer<QueryOptions> closeResourcesHandler) {
        this.indexedCollection = indexedCollection;
        this.openResourcesHandler = openResourcesHandler;
        this.closeResourcesHandler = closeResourcesHandler;
    }

    public <A> AttributeMetadata<A, O> getAttributeMetadata(Attribute<O, A> attribute) {
        KeyStatisticsIndex<A, O> index = getKeyStatisticsIndexForAttribute(attribute);
        return new AttributeMetadata<>(index, openResourcesHandler, closeResourcesHandler);
    }

    public <A extends Comparable<A>> NavigableAttributeMetadata<A, O> getNavigableAttributeMetadata(Attribute<O, A> attribute) {
        SortedKeyStatisticsIndex<A, O> index = getSortedKeyStatisticsIndexForAttribute(attribute);
        return new NavigableAttributeMetadata<>(index, openResourcesHandler, closeResourcesHandler);
    }

    private <A> KeyStatisticsIndex<A, O> getKeyStatisticsIndexForAttribute(Attribute<O, A> attribute) {
        for (Index<O> index : indexedCollection.getIndexes()) {
            if (index instanceof KeyStatisticsAttributeIndex) {
                if (((KeyStatisticsAttributeIndex) index).getAttribute().equals(attribute)) {
                    return (KeyStatisticsIndex<A, O>) index;
                }
            }
        }
        throw new IllegalStateException("A KeyStatisticsIndex must be added to the collection first, to enable metadata to be examined for attribute: " + attribute);
    }

    private <A extends Comparable<A>> SortedKeyStatisticsIndex<A, O> getSortedKeyStatisticsIndexForAttribute(Attribute<O, A> attribute) {
        for (Index<O> index : indexedCollection.getIndexes()) {
            if (index instanceof SortedKeyStatisticsIndex) {
                if (((KeyStatisticsAttributeIndex) index).getAttribute().equals(attribute)) {
                    return (SortedKeyStatisticsIndex<A, O>) index;
                }
            }
        }
        throw new IllegalStateException("A SortedKeyStatisticsIndex must be added to the collection first, to enable navigable metadata to be examined for attribute: " + attribute);
    }
}
