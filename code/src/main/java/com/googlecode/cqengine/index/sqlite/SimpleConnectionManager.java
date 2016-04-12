package com.googlecode.cqengine.index.sqlite;

import com.googlecode.cqengine.index.Index;

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
    public Connection getConnection(Index<?> index) {
        return connection;
    }

    @Override
    public boolean isApplyUpdateForIndexEnabled(Index<?> index) {
        return true;
    }
}
