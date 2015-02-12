package com.googlecode.cqengine.index.disk;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.disk.support.ConnectionManager;
import org.junit.Assert;
import org.junit.rules.TemporaryFolder;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;

/**
 * {@link org.junit.Rule} that allows to create and safely delete an SQLite temporary database file.
 *
 * @author Silvano Riz
 */
public class TemporaryFileDatabase extends TemporaryFolder{

    SQLiteDataSource dataSource;
    String url;
    File dbFile;

    @Override
    protected void before() throws Throwable {
        super.before();
        dbFile = newFile();
        url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        SQLiteConfig config = new SQLiteConfig();
        config.setEncoding(SQLiteConfig.Encoding.UTF8);

        dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        System.out.println("Temporary file database created: " + url);
    }

    @Override
    protected void after() {
        super.after();
        Assert.assertFalse(dbFile.exists());
        System.out.println("Temporary file purged: " + dbFile.getAbsolutePath());
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
                try {
                    return dataSource.getConnection();
                }catch(Exception e){
                    throw new IllegalStateException("Unable to create connection to: " + url);
                }
            }

            @Override
            public void closeConnection(Connection connection) {
                try{
                    if (connection != null){
                        connection.close();
                    }
                }catch (Exception e){
                    // Ingore
                }
            }
        };
    }
}
