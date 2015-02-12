package com.googlecode.cqengine.index.disk;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.disk.support.ConnectionManager;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * {@link org.junit.Rule} that allows to create and safely shut down an SQLite in-memory database.
 *
 * @author Silvano Riz
 */
public class TemporaryInMemoryDatabase implements TestRule {

    Connection connection = null;

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


    private void after() {
        try{
            if (connection != null){
                connection.close();
            }
        }catch (Exception e){
            // Ignore
        }
    }

    private void before() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:");
        }catch (Exception e){
            throw new IllegalStateException("Cannot create in-memory database connection");
        }
    }

    /**
     * Returns the {@link ConnectionManager} for the tmp database.
     *
     * @return The {@link ConnectionManager} for the tmp database.
     */
    public ConnectionManager getConnectionManager(){
        return new ConnectionManager() {
            @Override
            public Connection getConnection(Index<?> index) {
                return connection;
            }

            @Override
            public void closeConnection(Connection connection) {
                // No op. The connection will be closed in the after()
            }
        };
    }



}
