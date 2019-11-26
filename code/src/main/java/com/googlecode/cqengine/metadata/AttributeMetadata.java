package com.googlecode.cqengine.metadata;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.support.*;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Provides access to basic metadata for a given attribute, in unsorted order.
 * <p>
 * This requires in advance, an index which implements the {@link KeyStatisticsAttributeIndex} interface,
 * to be added to the collection on the given attribute. See {@link MetadataEngine} for more details.
 * <p>
 * This object can be accessed first by calling {@link IndexedCollection#getMetadataEngine()} to access the
 * {@link MetadataEngine}, and then by calling {@link MetadataEngine#getAttributeMetadata(Attribute)} for a given
 * attribute.
 */
public class AttributeMetadata<A, O> {

    private final KeyStatisticsIndex<A, O> index;
    private final Supplier<QueryOptions> openResourcesHandler;
    private final Consumer<QueryOptions> closeResourcesHandler;

    AttributeMetadata(KeyStatisticsIndex<A, O> index, Supplier<QueryOptions> openResourcesHandler, Consumer<QueryOptions> closeResourcesHandler) {
        this.index = index;
        this.openResourcesHandler = openResourcesHandler;
        this.closeResourcesHandler = closeResourcesHandler;
    }

    /**
     * Returns the frequencies of distinct keys (a.k.a. attribute values) in the index.
     * <p>
     * The {@link KeyFrequency} objects encapsulate a key (a.k.a. attribute value), and the frequency (or count)
     * of how many objects in the collection match that key.
     */
    public Stream<KeyFrequency<A>> getFrequencyDistribution() {
        QueryOptions queryOptions = openResources();
        return asKeyFrequencyStream(queryOptions, index.getStatisticsForDistinctKeys(queryOptions));
    }

    /**
     * Returns the distinct keys in the index.
     */
    public Stream<A> getDistinctKeys() {
        QueryOptions queryOptions = openResources();
        return asStream(queryOptions, index.getDistinctKeys(queryOptions));
    }

    /**
     * Returns the count of distinct keys in the index.
     */
    public Integer getCountOfDistinctKeys() {
        QueryOptions queryOptions = openResources();
        try {
            return index.getCountOfDistinctKeys(queryOptions);
        }
        finally {
            closeResources(queryOptions);
        }
    }

    /**
     * Returns the number of objects in the index which match the given key.
     */
    public Integer getCountForKey(A key) {
        QueryOptions queryOptions = openResources();
        try {
            return index.getCountForKey(key, queryOptions);
        }
        finally {
            closeResources(queryOptions);
        }
    }

    /**
     * Returns the keys (a.k.a. attribute values) and the objects they match in the index.
     * <p>
     * Note the same key will be returned multiple times if more than one object has the same key. Also the same object
     * might be returned multiple times, each time for a different key, if the index is built on a multi-value attribute.
     *
     * @return The keys and objects they match in the index
     */
    public Stream<KeyValue<A, O>> getKeysAndValues() {
        QueryOptions queryOptions = openResources();
        return asStream(queryOptions, index.getKeysAndValues(queryOptions));
    }

    @SuppressWarnings("unchecked")
    protected Stream<KeyFrequency<A>> asKeyFrequencyStream(QueryOptions queryOptions, CloseableIterable<KeyStatistics<A>> iterable) {
        Stream<? extends KeyFrequency<A>> keyStatisticsStream = asStream(queryOptions, iterable);
        return (Stream<KeyFrequency<A>>) keyStatisticsStream;
    }

    protected  <T> Stream<T> asStream(QueryOptions queryOptions, CloseableIterable<T> iterable) {
        CloseableIterator<T> iterator = iterable.iterator();
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .onClose(iterator::close) // ..when the stream is closed, first close the iterator
                .onClose(() -> closeResources(queryOptions)); // ..then close any other resources which were acquired
    }

    protected QueryOptions openResources() {
        return openResourcesHandler.get();
    }

    protected void closeResources(QueryOptions queryOptions) {
        closeResourcesHandler.accept(queryOptions);
    }
}
