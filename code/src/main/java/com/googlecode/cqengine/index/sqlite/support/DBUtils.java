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
package com.googlecode.cqengine.index.sqlite.support;

import com.googlecode.concurrenttrees.common.CharSequences;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;

/**
 * A bunch of useful database utilities.
 *
 * @author Silvano Riz
 */
public class DBUtils {

    public static Closeable wrapAsCloseable(final ResultSet resultSet){
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

    public static String getDBTypeForClass(final Class<?> valueType){

        if ( CharSequence.class.isAssignableFrom(valueType) || BigDecimal.class.isAssignableFrom(valueType)) {
            return "TEXT";

        }else if (Long.class.isAssignableFrom(valueType) || Integer.class.isAssignableFrom(valueType) || Short.class.isAssignableFrom(valueType) || Boolean.class.isAssignableFrom(valueType) || Date.class.isAssignableFrom(valueType)) {
            return "INTEGER";

        }else if (Float.class.isAssignableFrom(valueType) || Double.class.isAssignableFrom(valueType)){
            return "REAL";

        }else if (valueType == byte[].class){
            return "BLOB";

        }else{
            throw new IllegalStateException("Type " + valueType + " not supported.");
        }
    }

    public static void setValueToPreparedStatement(int index, final PreparedStatement preparedStatement, Object value) throws SQLException {

        if (value instanceof Date) {
            preparedStatement.setLong(index, ((Date) value).getTime());

        }else if(value instanceof CharSequence){
            preparedStatement.setString(index, CharSequences.toString((CharSequence)value));

        }else{
            preparedStatement.setObject(index, value);
        }
    }

    /**
     * <p> Binds a set of values to the statement.
     *
     * @param startIndex parameter index from where to start the binding.
     * @param preparedStatement the prepared statement.
     * @param values The values to bind
     * @return The new start index.
     * @throws SQLException if the binding fails.
     */
    public static int setValuesToPreparedStatement(final int startIndex, final PreparedStatement preparedStatement, final Iterable values) throws SQLException {
        int index = startIndex;
        for (Object value : values){
            setValueToPreparedStatement(index++, preparedStatement, value);
        }
        return index;
    }

    @SuppressWarnings("unchecked")
    public static <T>T getValueFromResultSet(int index, final ResultSet resultSet, final Class<T> type){

        try {
            if (java.sql.Date.class.isAssignableFrom(type)) {
                final long time = resultSet.getLong(index);
                return (T)new java.sql.Date(time);

            } else if (Time.class.isAssignableFrom(type)) {
                final long time = resultSet.getLong(index);
                return (T)new java.sql.Time(time);

            } else if (Timestamp.class.isAssignableFrom(type)) {
                final long time = resultSet.getLong(index);
                return (T)new java.sql.Timestamp(time);

            }else if (Date.class.isAssignableFrom(type)) {
                final long time = resultSet.getLong(index);
                return (T)new Date(time);

            } else if (Long.class.isAssignableFrom(type)) {
                return (T) Long.valueOf(resultSet.getLong(index));

            } else if (Integer.class.isAssignableFrom(type)) {
                return (T) Integer.valueOf(resultSet.getInt(index));

            } else if (Short.class.isAssignableFrom(type)) {
                return (T) Short.valueOf(resultSet.getShort(index));

            } else if (Float.class.isAssignableFrom(type)) {
                return (T) Float.valueOf(resultSet.getFloat(index));

            } else if (Double.class.isAssignableFrom(type)) {
                return (T) Double.valueOf(resultSet.getDouble(index));

            } else if (Boolean.class.isAssignableFrom(type)) {
                return (T) Boolean.valueOf(resultSet.getBoolean(index));

            } else if (BigDecimal.class.isAssignableFrom(type)) {
                return (T) resultSet.getBigDecimal(index);

            } else if (CharSequence.class.isAssignableFrom(type)) {
                return (T) resultSet.getString(index);

            } else if (byte[].class.isAssignableFrom(type)) {
                return (T) resultSet.getBytes(index);

            } else {
                throw new IllegalStateException("Type " + type + " not supported.");
            }
        }catch (Exception e){
            throw new IllegalStateException("Unable to read the value from the resultSet. Index:" + index + ", type: " + type, e);
        }
    }

    /**
     * Strips illegal characters so that the given string can be used as a SQLite table name.
     */
    public static String sanitizeForTableName(String input) {
        return input.replaceAll("[^A-Za-z0-9]", "");
    }

}
