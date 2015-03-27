package com.googlecode.cqengine.index.sqlite;

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
     * Informs if index updates should be applied.
     *
     * @param index The index.
     * @return true if updates on the index should be applied. False otherwise.
     */
    boolean isApplyUpdateForIndexEnabled(Index<?> index);
}
