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
package com.googlecode.cqengine;

import com.googlecode.cqengine.engine.QueryEngine;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.metadata.MetadataEngine;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.AbstractCollection;
import java.util.Set;

/**
 * A Java collection which can maintain indexes on the objects it contains, allowing objects matching complex queries
 * to be retrieved with very low latency.
 * <p/>
 * The {@link #retrieve(com.googlecode.cqengine.query.Query)} methods accept a {@link com.googlecode.cqengine.query.Query}
 * and return a {@link com.googlecode.cqengine.resultset.ResultSet} of objects matching that query.
 * <p/>
 * The {@link #addIndex(com.googlecode.cqengine.index.Index)} methods allowing indexes to be added to the collection to
 * improve query performance.
 * <p/>
 * Several implementations of this interface exist each with different performance or transaction isolation
 * characteristics. See documentation on the implementations for further details.
 *
 * @author Niall Gallagher
 */
public interface IndexedCollection<O> extends Set<O>, QueryEngine<O> {

    /**
     * Shortcut for calling {@link #retrieve(Query, QueryOptions)} without supplying any query options.
     */
    ResultSet<O> retrieve(Query<O> query);

    /**
     * {@inheritDoc}
     */
    @Override
    ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions);

    /**
     * Removes or adds objects to/from the collection and indexes in bulk.
     * <p/>
     * Note that although this method accepts either {@code Iterable}s or {@code Collection}s for its
     * {@code objectsToRemove} and {@code objectsToAdd} parameters, there are pros and cons of each:
     * <ul>
     *     <li>
     *         If an {@code Iterable} is supplied, updates to indexes will be applied in a streaming fashion. This
     *         allows the application and CQEngine to avoid buffering many updates in memory as a batch, but requires
     *         more round trips to update indexes, which might hurt performance for indexes where making many round
     *         trips is expensive. This is typically fine for on-heap indexes, but less so for off-heap or on-disk
     *         indexes.
     *     </li>
     *     <li>
     *         If a {@code Collection} is supplied, updates to indexes will be applied as a single batch. This might
     *         improve performance for indexes where making many round trips is expensive. Ordinarily this implies that
     *         the application needs to assemble a batch into a collection before calling this method. However for
     *         applications where the source of updates is computed lazily or originates from a stream, but yet where
     *         it is desirable to apply the updates as a batch anyway, the application can wrap the stream as a lazy
     *         collection (extend {@link AbstractCollection}) so that CQEngine will behave as above.
     *         <ul><li>
     *         Note also that some off-heap and on-disk indexes support a fast "bulk import" feature which can be used
     *         in conjunction with this. For details on how to perform a bulk import, see
     *         {@link com.googlecode.cqengine.index.sqlite.support.SQLiteIndexFlags#BULK_IMPORT}.
     *         </li></ul>
     *     </li>
     * </ul>
     *
     * @param objectsToRemove The objects to remove from the collection
     * @param objectsToAdd The objects to add to the collection
     * @return True if the collection was modified as a result, false if it was not
     */
    boolean update(Iterable<O> objectsToRemove, Iterable<O> objectsToAdd);

    /**
     * Removes or adds objects to/from the collection and indexes in bulk.
     * <p/>
     * Note that although this method accepts either {@code Iterable}s or {@code Collection}s for its
     * {@code objectsToRemove} and {@code objectsToAdd} parameters, there are pros and cons of each:
     * <ul>
     *     <li>
     *         If an {@code Iterable} is supplied, updates to indexes will be applied in a streaming fashion. This
     *         allows the application and CQEngine to avoid buffering many updates in memory as a batch, but requires
     *         more round trips to update indexes, which might hurt performance for indexes where making many round
     *         trips is expensive. This is typically fine for on-heap indexes, but less so for off-heap or on-disk
     *         indexes.
     *     </li>
     *     <li>
     *         If a {@code Collection} is supplied, updates to indexes will be applied as a single batch. This might
     *         improve performance for indexes where making many round trips is expensive. Ordinarily this implies that
     *         the application needs to assemble a batch into a collection before calling this method. However for
     *         applications where the source of updates is computed lazily or originates from a stream, but yet where
     *         it is desirable to apply the updates as a batch anyway, the application can wrap the stream as a lazy
     *         collection (extend {@link AbstractCollection}) so that CQEngine will behave as above.
     *         <ul><li>
     *         Note also that some off-heap and on-disk indexes support a fast "bulk import" feature which can be used
     *         in conjunction with this. For details on how to perform a bulk import, see
     *         {@link com.googlecode.cqengine.index.sqlite.support.SQLiteIndexFlags#BULK_IMPORT}.
     *         </li></ul>
     *     </li>
     * </ul>
     *
     * @param objectsToRemove The objects to remove from the collection
     * @param objectsToAdd The objects to add to the collection
     * @param queryOptions Optional parameters for the update
     * @return True if the collection was modified as a result, false if it was not
     */
    boolean update(Iterable<O> objectsToRemove, Iterable<O> objectsToAdd, QueryOptions queryOptions);

    /**
     * @see #addIndex(Index, QueryOptions)
     */
    void addIndex(Index<O> index);

    /**
     * {@inheritDoc}
     */
    @Override
    void addIndex(Index<O> index, QueryOptions queryOptions);

    /**
     * @see #removeIndex(Index, QueryOptions)
     */
    void removeIndex(Index<O> index);

    /**
     * {@inheritDoc}
     */
    @Override
    void removeIndex(Index<O> index, QueryOptions queryOptions);

    /**
     * Returns the {@link Persistence} used by the the collection.
     *
     * @return The {@link Persistence} used by the the collection
     */
    Persistence<O, ?> getPersistence();

    /**
     * Returns the {@link MetadataEngine}, which can retrieve metadata and statistics from indexes
     * on the distribution of attribute values in the collection.
     */
    MetadataEngine<O> getMetadataEngine();
}
