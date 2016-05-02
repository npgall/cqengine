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
package com.googlecode.cqengine.index.sqlite;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.sql.Connection;

/**
 * Connection Manager
 *
 * @author Silvano Riz
 */
public interface ConnectionManager {

    /**
     * Returns a {@link Connection}.
     *
     * @param index The {@link Index} requesting the connection.
     * @param queryOptions The query options for the request
     * @return The {@link Connection}
     */
    Connection getConnection(Index<?> index, QueryOptions queryOptions);

    /**
     * Informs if index updates should be applied.
     * This is for advanced use cases only, where the application supplies a custom ConnectionManager to CQEngine.
     *
     * @param index The index.
     * @return true if updates on the index should be applied. False otherwise.
     */
    boolean isApplyUpdateForIndexEnabled(Index<?> index);
}
