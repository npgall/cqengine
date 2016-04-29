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

import org.junit.Assert;
import org.junit.Test;

import java.io.Closeable;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DBUtils}
 *
 * @author Silvano Riz
 */
public class DBUtilsTest {

    @Test
    public void testWrapConnectionInCloseable() throws Exception {

        ResultSet resultSet = mock(ResultSet.class);

        Closeable closeable = DBUtils.wrapAsCloseable(resultSet);
        closeable.close();

        verify(resultSet, times(1)).close();

    }

    @Test
    public void testCloseResultSetQuietly() throws Exception {

        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getStatement()).thenReturn(statement);

        DBUtils.closeQuietly(resultSet);

        verify(resultSet, times(1)).close();
        verify(statement, times(1)).close();
    }

    @Test
    public void testCloseResultSetQuietly_NullStatement() throws Exception {

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getStatement()).thenReturn(null);

        DBUtils.closeQuietly(resultSet);

        verify(resultSet, times(1)).close();
    }

    @Test
    public void testCloseResultSetQuietly_Null() throws Exception {
        ResultSet resultSet = null;
        DBUtils.closeQuietly(resultSet);
    }

    @Test
    public void testCloseResultSetQuietly_ResultSetCloseException() throws Exception {

        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getStatement()).thenReturn(statement);
        doThrow(new SQLException("SQL Exception")).when(resultSet).close();

        DBUtils.closeQuietly(resultSet);

        verify(resultSet, times(1)).close();
        verify(statement, times(1)).close();

    }

    @Test
    public void testCloseResultSetQuietly_StatementCloseException() throws Exception {

        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getStatement()).thenReturn(statement);
        doThrow(new SQLException("SQL Exception")).when(statement).close();

        DBUtils.closeQuietly(resultSet);

        verify(resultSet, times(1)).close();
        verify(statement, times(1)).close();

    }

    @Test
    public void testCloseStatementQuietly() throws Exception {
        Statement statement = mock(Statement.class);
        DBUtils.closeQuietly(statement);
        verify(statement, times(1)).close();
    }

    @Test
    public void testCloseStatementQuietly_Null() throws Exception {
        Statement statement = null;
        DBUtils.closeQuietly(statement);
    }

    @Test
    public void testCloseStatementQuietly_Exception() throws Exception {
        Statement statement = mock(Statement.class);
        doThrow(new SQLException("SQL Exception")).when(statement).close();
        DBUtils.closeQuietly(statement);
        verify(statement, times(1)).close();
    }

    @Test
    public void testCloseConnectionQuietly() throws Exception {
        Connection connection = mock(Connection.class);
        DBUtils.closeQuietly(connection);
        verify(connection, times(1)).close();
    }

    @Test
    public void testCloseConnectionQuietly_Null() throws Exception {
        Connection connection = null;
        DBUtils.closeQuietly(connection);
    }

    @Test
    public void testCloseConnectionQuietly_Exception() throws Exception {
        Connection connection = mock(Connection.class);
        doThrow(new SQLException("SQL Exception")).when(connection).close();
        DBUtils.closeQuietly(connection);
        verify(connection, times(1)).close();
    }

    @Test
    public void testGetDBTypeForClass() throws Exception {

        Assert.assertEquals("INTEGER", DBUtils.getDBTypeForClass(Integer.class));
        Assert.assertEquals("INTEGER", DBUtils.getDBTypeForClass(Long.class));
        Assert.assertEquals("INTEGER", DBUtils.getDBTypeForClass(Short.class));
        Assert.assertEquals("INTEGER", DBUtils.getDBTypeForClass(Boolean.class));

        Assert.assertEquals("REAL", DBUtils.getDBTypeForClass(Float.class));
        Assert.assertEquals("REAL", DBUtils.getDBTypeForClass(Double.class));

        Assert.assertEquals("TEXT", DBUtils.getDBTypeForClass(String.class));
        Assert.assertEquals("TEXT", DBUtils.getDBTypeForClass(CharSequence.class));
        Assert.assertEquals("TEXT", DBUtils.getDBTypeForClass(BigDecimal.class));

        Assert.assertEquals("INTEGER", DBUtils.getDBTypeForClass(java.util.Date.class));
        Assert.assertEquals("INTEGER", DBUtils.getDBTypeForClass(java.sql.Date.class));
        Assert.assertEquals("INTEGER", DBUtils.getDBTypeForClass(Time.class));
        Assert.assertEquals("INTEGER", DBUtils.getDBTypeForClass(Timestamp.class));

        Assert.assertEquals("BLOB", DBUtils.getDBTypeForClass(byte[].class));

    }

    @Test(expected = IllegalStateException.class)
    public void testGetDBTypeForClass_UnsupportedType() throws Exception {
        DBUtils.getDBTypeForClass(Calendar.class);
    }

    @Test
    public void testSetValueToPreparedStatement() throws Exception {
        PreparedStatement statement = mock(PreparedStatement.class);

        // Date / Time
        final long now = System.currentTimeMillis();

        DBUtils.setValueToPreparedStatement(1,statement, new java.util.Date(now));
        verify(statement).setLong(1, now);

        DBUtils.setValueToPreparedStatement(2,statement, new java.sql.Date(now));
        verify(statement).setLong(2, now);

        DBUtils.setValueToPreparedStatement(3,statement, new Time(now));
        verify(statement).setLong(3, now);

        DBUtils.setValueToPreparedStatement(4,statement, new Timestamp(now));
        verify(statement).setLong(4, now);

        // String and CharSequence
        final String str = "This is a string";
        final StringBuilder stringBuilder = new StringBuilder("This is a CharSequence");
        DBUtils.setValueToPreparedStatement(5,statement, str);
        verify(statement).setString(5, "This is a string");


        DBUtils.setValueToPreparedStatement(6,statement, stringBuilder);
        verify(statement).setString(6, "This is a CharSequence");

        // Others...use setObject
        DBUtils.setValueToPreparedStatement(7,statement, Boolean.FALSE);
        verify(statement).setObject(7, Boolean.FALSE);

        DBUtils.setValueToPreparedStatement(7,statement, Long.MAX_VALUE);
        verify(statement).setObject(7, Long.MAX_VALUE);

        DBUtils.setValueToPreparedStatement(8,statement, "bytes".getBytes());
        verify(statement).setObject(8, "bytes".getBytes());
    }

    @Test
    public void testGetValueFromResultSet() throws Exception{

        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt(1)).thenReturn(Integer.MAX_VALUE);
        DBUtils.getValueFromResultSet(1, resultSet, Integer.class);
        verify(resultSet, times(1)).getInt(1);

        when(resultSet.getLong(2)).thenReturn(Long.MAX_VALUE);
        DBUtils.getValueFromResultSet(2, resultSet, Long.class);
        verify(resultSet, times(1)).getLong(2);

        when(resultSet.getShort(3)).thenReturn(Short.MAX_VALUE);
        DBUtils.getValueFromResultSet(3, resultSet, Short.class);
        verify(resultSet, times(1)).getShort(3);

        when(resultSet.getBoolean(4)).thenReturn(Boolean.FALSE);
        DBUtils.getValueFromResultSet(4, resultSet, Boolean.class);
        verify(resultSet, times(1)).getBoolean(4);

        when(resultSet.getFloat(5)).thenReturn(Float.MAX_VALUE);
        DBUtils.getValueFromResultSet(5, resultSet, Float.class);
        verify(resultSet, times(1)).getFloat(5);

        when(resultSet.getDouble(6)).thenReturn(Double.MAX_VALUE);
        DBUtils.getValueFromResultSet(6, resultSet, Double.class);
        verify(resultSet, times(1)).getDouble(6);

        when(resultSet.getString(7)).thenReturn("7 string");
        DBUtils.getValueFromResultSet(7, resultSet, String.class);
        verify(resultSet, times(1)).getString(7);

        when(resultSet.getString(8)).thenReturn("8 CharSequence");
        DBUtils.getValueFromResultSet(8, resultSet, CharSequence.class);
        verify(resultSet, times(1)).getString(8);

        when(resultSet.getBigDecimal(9)).thenReturn(new BigDecimal("1"));
        DBUtils.getValueFromResultSet(9, resultSet, BigDecimal.class);
        verify(resultSet, times(1)).getBigDecimal(9);

        final long now = System.currentTimeMillis();

        when(resultSet.getLong(10)).thenReturn(now);
        DBUtils.getValueFromResultSet(10, resultSet, java.util.Date.class);
        verify(resultSet, times(1)).getLong(10);

        when(resultSet.getLong(11)).thenReturn(now);
        DBUtils.getValueFromResultSet(11, resultSet, java.sql.Date.class);
        verify(resultSet, times(1)).getLong(11);

        when(resultSet.getLong(12)).thenReturn(now);
        DBUtils.getValueFromResultSet(12, resultSet, Time.class);
        verify(resultSet, times(1)).getLong(12);

        when(resultSet.getLong(13)).thenReturn(now);
        DBUtils.getValueFromResultSet(13, resultSet, Timestamp.class);
        verify(resultSet, times(1)).getLong(13);

        when(resultSet.getBytes(14)).thenReturn("bytes".getBytes());
        DBUtils.getValueFromResultSet(14, resultSet, byte[].class);
        verify(resultSet, times(1)).getBytes(14);
    }
}