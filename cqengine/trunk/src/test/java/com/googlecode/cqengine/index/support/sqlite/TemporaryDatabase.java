package com.googlecode.cqengine.index.support.sqlite;

import com.googlecode.cqengine.index.Index;
import org.junit.Assert;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Set of utility classes and {@link org.junit.Rule} to test Disk index against a temporary SQLite database.
 *
 * @author Silvano Riz
 */
public class TemporaryDatabase {

    // ----------------------
    // Helper classes & methods
    // ----------------------

    public static interface ConnectionProxy extends Connection {
        Connection getTargetConnection();
    }

    static Connection createConnectionProxy(final Connection connection) throws SQLException {

        return (Connection) Proxy.newProxyInstance(
                ConnectionProxy.class.getClassLoader(),
                new Class<?>[]{ConnectionProxy.class},
                new SuppressCloseInvocationHandler(connection));
    }

    static class SuppressCloseInvocationHandler implements InvocationHandler {

        private final Connection target;

        public SuppressCloseInvocationHandler(Connection target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Invocation on ConnectionProxy interface coming in...
            final String methodName = method.getName();

            if ("close".equals(methodName)) {
                // Close method does nothing.
                return null;

            } else if ("getTargetConnection".equals(methodName)) {
                // Handle getTargetConnection method: return underlying Connection.
                return this.target;

            }

            // Invoke method on target Connection.
            try {
                return method.invoke(this.target, args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

    static void closeQuietly(final Connection connection) {
        try {
            if (connection != null) {
                if (connection instanceof ConnectionProxy) {
                    ((ConnectionProxy) connection).getTargetConnection().close();
                } else {
                    connection.close();
                }
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    /**
     * {@link org.junit.Rule} that allows to create and safely delete an SQLite temporary database file.
     *
     * @author Silvano Riz
     */
    public static class TemporaryFileDatabase extends TemporaryFolder {

        SQLiteDataSource dataSource;
        SQLiteConfig config;
        String url;
        File dbFile;
        Set<Connection> singleConnections = new HashSet<Connection>();

        public TemporaryFileDatabase() {
            this.config = new SQLiteConfig();
        }

        public TemporaryFileDatabase(SQLiteConfig config) {
            this.config = config;
        }

        @Override
        public void before() {
            try {
                super.before();
                dbFile = newFile();
                url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
                dataSource = new SQLiteDataSource(config);
                dataSource.setUrl(url);
            }
            catch (Throwable throwable) {
                throw new IllegalStateException(throwable);
            }
        }

        @Override
        public void after() {
            super.after();
            Assert.assertFalse(dbFile.exists());

            for (Connection connection : singleConnections) {
                closeQuietly(connection);
            }

        }

        /**
         * Returns the {@link ConnectionManager} for the tmp database.
         *
         * @return The {@link ConnectionManager} for the tmp database.
         */
        public ConnectionManager getConnectionManager(final boolean applyUpdateForIndexEnabled) {
            return new ConnectionManager() {
                @Override
                public Connection getConnection(Index<?> index) {
                    try {
                        return dataSource.getConnection();
                    } catch (Exception e) {
                        throw new IllegalStateException("Unable to create connection to: " + url);
                    }
                }

                @Override
                public boolean isApplyUpdateForIndexEnabled(Index<?> index) {
                    return applyUpdateForIndexEnabled;
                }
            };
        }

        /**
         * Returns the {@link ConnectionManager} for the tmp database.
         *
         * @return The {@link ConnectionManager} for the tmp database.
         */
        public ConnectionManager getSingleConnectionManager(final boolean applyUpdateForIndexEnabled) {
            return new ConnectionManager() {

                Connection singleConnection;

                @Override
                public Connection getConnection(Index<?> index) {
                    try {
                        if (singleConnection != null)
                            return singleConnection;

                        synchronized (this) {
                            if (singleConnection != null)
                                return singleConnection;
                            else {
                                singleConnection = createConnectionProxy(dataSource.getConnection());
                                singleConnections.add(singleConnection);
                            }
                            return singleConnection;

                        }
                    } catch (Exception e) {
                        throw new IllegalStateException("Unable to create connection to: " + url);
                    }
                }

                @Override
                public boolean isApplyUpdateForIndexEnabled(Index<?> index) {
                    return applyUpdateForIndexEnabled;
                }
            };
        }
    }

    /**
     * {@link org.junit.Rule} that allows to create and safely shut down an SQLite in-memory database.
     *
     * @author Silvano Riz
     */
    public static class TemporaryInMemoryDatabase implements TestRule {

        Connection connection = null;
        SQLiteConfig config = null;

        public TemporaryInMemoryDatabase(){}

        public TemporaryInMemoryDatabase(final SQLiteConfig config){
            this.config = config;
        }

        @Override
        public Statement apply(final Statement base, final Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    before();
                    try {
                        base.evaluate();
                    } finally {
                        after();
                    }
                }
            };
        }


        public void after() {
            closeQuietly(connection);
        }

        public void before() {
            try {
                if (config != null) {
                    connection = createConnectionProxy(DriverManager.getConnection("jdbc:sqlite:", config.toProperties()));
                }else{
                    connection = createConnectionProxy(DriverManager.getConnection("jdbc:sqlite:"));
                }
            } catch (Exception e) {
                throw new IllegalStateException("Cannot create in-memory database connection");
            }
        }

        /**
         * Returns the {@link ConnectionManager} for the tmp database.
         *
         * @return The {@link ConnectionManager} for the tmp database.
         */
        public ConnectionManager getConnectionManager(final boolean applyUpdateForIndexEnabled) {
            return new ConnectionManager() {
                @Override
                public Connection getConnection(Index<?> index) {
                    return connection;
                }

                @Override
                public boolean isApplyUpdateForIndexEnabled(Index<?> index) {
                    return applyUpdateForIndexEnabled;
                }
            };
        }

    }
}
