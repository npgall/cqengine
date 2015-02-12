package com.googlecode.cqengine.index.disk.support;

import com.googlecode.cqengine.index.Index;

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
     * @return The {@link Connection}
     */
    Connection getConnection(Index<?> index);

    /**
     * Closes a {@link Connection}.
     *
     * @param connection The {@link Connection} to close
     */
    void closeConnection(Connection connection);
}
