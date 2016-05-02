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
 * An implementation of {@link ConnectionManager} which wraps a single connection.
 */
public class SimpleConnectionManager implements ConnectionManager {

    final Connection connection;

    public SimpleConnectionManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection(Index<?> index, QueryOptions queryOptions) {
        return connection;
    }

    @Override
    public boolean isApplyUpdateForIndexEnabled(Index<?> index) {
        return true;
    }
}
