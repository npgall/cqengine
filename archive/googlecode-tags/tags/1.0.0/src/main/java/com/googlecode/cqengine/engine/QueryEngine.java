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
package com.googlecode.cqengine.engine;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Collection;
import java.util.Map;

/**
 * The main component of {@code CQEngine} - maintains a set of indexes on a collection and accepts queries which
 * it performs and optimizes for those indexes.
 *
 * @author Niall Gallagher
 */
public interface QueryEngine<O> {

    /**
     * Retrieves a {@link ResultSet} which provides objects matching the given query.
     *
     * @param query A query representing some assertions which sought objects must match
     * @return A {@link ResultSet} which provides objects matching the given query
     */
    public ResultSet<O> retrieve(Query<O> query);

    /**
     * Retrieves a {@link ResultSet} which provides objects matching the given query, additionally accepting
     * {@link QueryOption} objects which can specify ordering of results, deduplication strategy etc.
     *
     * @param query A query representing some assertions which sought objects must match
     * @param queryOptions A map of {@link QueryOption} class to {@link QueryOption} object containing optional
     * parameters for the query
     * @return A {@link ResultSet} which provides objects matching the given query
     */
    public ResultSet<O> retrieve(Query<O> query, Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions);

    /**
     * Adds the given index to the collection.
     * <p/>
     * Subsequently queries passed to the {@link #retrieve(com.googlecode.cqengine.query.Query)} methods will use these
     * indexes if suitable for the particular queries, to speed up retrievals.
     *
     * @param index The index to add
     */
    public void addIndex(Index<O> index);
}
