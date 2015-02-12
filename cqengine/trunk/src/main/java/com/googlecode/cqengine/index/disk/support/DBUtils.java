package com.googlecode.cqengine.index.disk.support;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A bunch of useful database utilities.
 *
 * @author Silvano Riz
 */
public class DBUtils {

    public static Closeable wrapConnectionInCloseable(final Connection connection, final ConnectionManager connectionManagerForClosing){
        return new Closeable() {
            @Override
            public void close() throws IOException {
                connectionManagerForClosing.closeConnection(connection);
            }
        };
    }

    public static Closeable wrapResultSetInCloseable(final ResultSet resultSet){
        return new Closeable() {
            @Override
            public void close() throws IOException {
                DBUtils.closeQuietly(resultSet);
            }
        };
    }

    public static boolean setAutoCommit(final Connection connection, final boolean value){
        try {
            boolean previousValue = connection.getAutoCommit();
            connection.setAutoCommit(value);
            return previousValue;
        }catch (Exception e){
            throw new IllegalStateException("Unable to set the Connection autoCommit to " + value, e);
        }
    }

    public static void commit(final Connection connection){
        try {
            connection.commit();
        }catch (Exception e){
            throw new IllegalStateException("Commit failed", e);
        }
    }

    public static boolean rollback(final Connection connection){
        try {
            connection.rollback();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void closeQuietly(java.sql.ResultSet resultSet){
        if (resultSet == null)
            return;

        try {
            Statement statement = resultSet.getStatement();
            if (statement != null){
                statement.close();
            }
        }catch(Exception e){
            // Ignore
        }

        try{
            resultSet.close();
        }catch (Exception e){
            // Ignore
        }
    }

    public static void closeQuietly(Statement statement){
        if (statement == null)
            return;
        try{
            statement.close();
        }catch (Exception e){
            // Ignore
        }
    }

    public static void closeQuietly(Connection connection){
        if (connection == null)
            return;
        try{
            connection.close();
        }catch (Exception e){
            // Ignore
        }
    }

    public static String getDBTypeForClass(Class valueType){
        if (valueType == String.class) {
            return "TEXT";
        }else if (valueType == Long.class || valueType == Integer.class || valueType == Short.class){
            return "INTEGER";
        }else{
            return "";
        }
    }

}
