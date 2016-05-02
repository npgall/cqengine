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
import com.googlecode.cqengine.index.sqlite.support.DBUtils;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.composite.CompositePersistence;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.io.Closeable;
import java.sql.Connection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A ConnectionManager which create connections on-demand to the correct persistence for the indexes requesting the
 * connection, and subsequently caches the connections, for re-use within the scope of the same request into CQEngine.
 * <p/>
 * Opens only one connection to each persistence, and returns the same already-open connection for subsequent requests.
 * <p/>
 * Connections are obtained from the {@link Persistence} object provided to the constructor.
 * This can be a {@link CompositePersistence} which actually wraps more than one backing persistence.
 * In that case, the {@link CompositePersistence#getPersistenceForIndex(Index)} method will be used to locate the
 * correct persistence to use for the index requesting the connection.
 *
 * @author niall.gallagher
 */
public class RequestScopeConnectionManager implements ConnectionManager, Closeable {

    final Persistence<?, ?> persistence;

    // Map of open connections.
    final ConcurrentMap<SQLitePersistence, Connection> openConnections = new ConcurrentHashMap<SQLitePersistence, Connection>(1, 1.0F, 1);

    public RequestScopeConnectionManager(Persistence<?, ?> persistence) {
        this.persistence = persistence;
    }


    @Override
    public Connection getConnection(Index<?> index, QueryOptions queryOptions) {
        index = index.getEffectiveIndex();
        SQLitePersistence<?, ?> persistence = getPersistenceForIndex(index);
        Connection connection = openConnections.get(persistence);
        if (connection == null) {
            Connection newConnection = persistence.getConnection(index, queryOptions);
            Connection existingConnection = openConnections.putIfAbsent(persistence, newConnection);
            if (existingConnection == null) {
                connection = newConnection;
                // Disable auto-commit so that all operations will be done in a transaction we control explicitly...
                DBUtils.setAutoCommit(connection, false);
            }
            else {
                // Close the new connection and return the existing one...
                DBUtils.closeQuietly(newConnection);
                connection = existingConnection;
            }
        }
        return connection;
    }

    /**
     * Commits the transactions on the open connections to all persistences, then closes the connections.
     */
    public void close() {
        for (Iterator<Connection> iterator = openConnections.values().iterator(); iterator.hasNext(); ) {
            Connection connection = iterator.next();
            DBUtils.commit(connection);
            DBUtils.closeQuietly(connection);
            iterator.remove();
        }
    }

    @SuppressWarnings("unchecked")
    SQLitePersistence getPersistenceForIndex(Index<?> index) {
        if (persistence instanceof SQLitePersistence) {
            if (persistence.supportsIndex((Index)index)) {
                return (SQLitePersistence) persistence;
            }
        }
        else if (persistence instanceof CompositePersistence) {
            CompositePersistence compositePersistence = ((CompositePersistence) persistence);
            Persistence indexPersistence = compositePersistence.getPersistenceForIndex(index);
            if (indexPersistence instanceof SQLitePersistence) {
                return (SQLitePersistence) indexPersistence;
            }
        }
        throw new IllegalStateException("No configured Persistence implementation can support the given index: " + index);
    }

    @Override
    public boolean isApplyUpdateForIndexEnabled(Index<?> index) {
        return true;
    }
}
