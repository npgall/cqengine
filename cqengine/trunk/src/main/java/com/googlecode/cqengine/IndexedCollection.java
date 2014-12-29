/**
 * Copyright 2012 Niall Gallagher
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
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Set;

/**
 * @author Niall Gallagher
 */
public interface IndexedCollection<O> extends Set<O>, QueryEngine<O> {

    /**
     * {@inheritDoc}
     */
    @Override
    ResultSet<O> retrieve(Query<O> query);

    /**
     * {@inheritDoc}
     */
    @Override
    ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions);

    /**
     * Removes or adds objects to/from the collection and indexes in bulk.
     *
     * @param objectsToRemove The objects to remove from the collection
     * @param objectsToAdd The objects to add to the collection
     * @return True if the collection was modified as a result, false if it was not
     */
    public boolean update(Iterable<O> objectsToRemove, Iterable<O> objectsToAdd);

    /**
     * Removes or adds objects to/from the collection and indexes in bulk.
     *
     * @param objectsToRemove The objects to remove from the collection
     * @param objectsToAdd The objects to add to the collection
     * @param queryOptions Optional parameters for the update
     * @return True if the collection was modified as a result, false if it was not
     */
    public boolean update(Iterable<O> objectsToRemove, Iterable<O> objectsToAdd, QueryOptions queryOptions);

    /**
     * {@inheritDoc}
     */
    @Override
    void addIndex(Index<O> index);

    /**
     * {@inheritDoc}
     */
    @Override
    void addIndex(Index<O> index, QueryOptions queryOptions);
}
