package com.googlecode.cqengine.index.disk.support;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DBUtils}
 *
 * @author Silvano Riz
 */
public class DBUtilsTest {

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

        Map<Class, String> expectedMappings = new HashMap<Class, String>();
        expectedMappings.put(Integer.class, "INTEGER");
        expectedMappings.put(Long.class, "INTEGER");
        expectedMappings.put(Short.class, "INTEGER");
        expectedMappings.put(String.class, "TEXT");

        for (Map.Entry<Class, String> entry: expectedMappings.entrySet()){
            Assert.assertEquals(entry.getValue(), DBUtils.getDBTypeForClass(entry.getKey()));
        }

    }
}