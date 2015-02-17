package com.googlecode.cqengine.index.disk.support;

import com.googlecode.cqengine.query.simple.*;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.sql.*;
import java.util.*;

import static com.googlecode.cqengine.index.disk.TemporaryDatabase.TemporaryFileDatabase;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.QueryFactory.startsWith;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DBQueries}
 *
 * @author Silvano Riz
 */
public class DBQueriesTest {

    private static final String TABLE_NAME = "features";

    @Rule
    public TemporaryFileDatabase temporaryFileDatabase = new TemporaryFileDatabase();

    @Test
    public void testCreateIndexTable() throws SQLException {

        Connection connection = null;
        Statement statement = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            connection = spy(connectionManager.getConnection(null));
            statement = spy(connection.createStatement());
            when(connection.createStatement()).thenReturn(statement);

            DBQueries.createIndexTable(TABLE_NAME, Integer.class, String.class, connection);

            assertObjectExistenceInSQLIteMasterTable(TABLE_NAME, "table", true, connectionManager);
            assertObjectExistenceInSQLIteMasterTable("idx_" + TABLE_NAME + "_value", "index", true, connectionManager);
            verify(statement, times(1)).close();
        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }
    }

    @Test
    public void testDropIndexTable() throws SQLException {

        Connection connection = null;
        Statement statement = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            createSchema(connectionManager);

            assertObjectExistenceInSQLIteMasterTable(TABLE_NAME, "table", true, connectionManager);
            assertObjectExistenceInSQLIteMasterTable("idx_" + TABLE_NAME + "_value", "index", true, connectionManager);

            connection = spy(connectionManager.getConnection(null));
            statement = spy(connection.createStatement());
            when(connection.createStatement()).thenReturn(statement);
            DBQueries.dropIndexTable(TABLE_NAME, connection);

            assertObjectExistenceInSQLIteMasterTable(TABLE_NAME, "table", false, connectionManager);
            assertObjectExistenceInSQLIteMasterTable("idx_" + TABLE_NAME + "_value", "index", false, connectionManager);
            verify(statement, times(1)).close();
        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }
    }

    @Test
    public void testClearIndexTable() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            createSchema(connectionManager);
            assertObjectExistenceInSQLIteMasterTable(TABLE_NAME, "table", true, connectionManager);
            assertObjectExistenceInSQLIteMasterTable("idx_" + TABLE_NAME + "_value", "index", true, connectionManager);

            connection = spy(connectionManager.getConnection(null));
            statement = spy(connection.createStatement());
            when(connection.createStatement()).thenReturn(statement);
            DBQueries.clearIndexTable(TABLE_NAME, connection);

            List<DBQueries.Row<Integer, String>> expectedRows = Collections.emptyList();
            assertQueryResultSet("SELECT * FROM " +TABLE_NAME, expectedRows, connectionManager);
            verify(statement, times(1)).close();
        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }
    }

    @Test
    public void testBulkAdd() throws SQLException {
        Connection connection = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            createSchema(connectionManager);

            List<DBQueries.Row<Integer, String>> rowsToAdd = new ArrayList<DBQueries.Row<Integer, String>>(4);
            rowsToAdd.add(new DBQueries.Row<Integer, String>(1, "abs"));
            rowsToAdd.add(new DBQueries.Row<Integer, String>(1, "gps"));
            rowsToAdd.add(new DBQueries.Row<Integer, String>(2, "airbags"));
            rowsToAdd.add(new DBQueries.Row<Integer, String>(2, "abs"));

            connection = connectionManager.getConnection(null);
            DBQueries.bulkAdd(rowsToAdd, TABLE_NAME, connection);
            assertQueryResultSet("SELECT * FROM " + TABLE_NAME, rowsToAdd, connectionManager);

        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Test
    public void testBulkRemove() throws SQLException {

        Connection connection = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            List<DBQueries.Row<Integer, String>> expectedRows = new ArrayList<DBQueries.Row<Integer, String>>(2);
            expectedRows.add(new DBQueries.Row<Integer, String>(2, "airbags"));
            expectedRows.add(new DBQueries.Row<Integer, String>(3, "abs"));

            connection = connectionManager.getConnection(null);
            DBQueries.bulkRemove(Arrays.asList(1), TABLE_NAME, connection);
            assertQueryResultSet("SELECT * FROM " + TABLE_NAME, expectedRows, connectionManager);

        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Test
    public void testCount_Equal() throws SQLException {

        Connection connection = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            Equal<Car, String> equal = equal(Car.FEATURES, "abs");

            connection = connectionManager.getConnection(null);
            int count = DBQueries.count(equal, TABLE_NAME, connection);
            Assert.assertEquals(2, count);

        }finally {
            DBUtils.closeQuietly(connection);
        }

    }

    @Test
    public void testSearch_Equal() throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            Equal<Car, String> equal = equal(Car.FEATURES, "abs");

            List<DBQueries.Row<Integer, String>> expectedRows = new ArrayList<DBQueries.Row<Integer, String>>(2);
            expectedRows.add(new DBQueries.Row<Integer, String>(1, "abs"));
            expectedRows.add(new DBQueries.Row<Integer, String>(3, "abs"));

            connection = connectionManager.getConnection(null);
            resultSet = DBQueries.search(equal, TABLE_NAME, connection);
            assertResultSet(resultSet, expectedRows);

        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(resultSet);
        }
    }

    @Test
    public void testSearch_LessThan() throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            LessThan<Car, String> lessThan = lessThan(Car.FEATURES, "abz");

            List<DBQueries.Row<Integer, String>> expectedRows = new ArrayList<DBQueries.Row<Integer, String>>(2);
            expectedRows.add(new DBQueries.Row<Integer, String>(1, "abs"));
            expectedRows.add(new DBQueries.Row<Integer, String>(3, "abs"));

            connection = connectionManager.getConnection(null);
            resultSet = DBQueries.search(lessThan, TABLE_NAME, connection);
            assertResultSet(resultSet, expectedRows);

        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(resultSet);
        }
    }

    @Test
    public void testSearch_GreaterThan() throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            GreaterThan<Car, String> greaterThan = greaterThan(Car.FEATURES, "abz");

            List<DBQueries.Row<Integer, String>> expectedRows = new ArrayList<DBQueries.Row<Integer, String>>(2);
            expectedRows.add(new DBQueries.Row<Integer, String>(1, "gps"));
            expectedRows.add(new DBQueries.Row<Integer, String>(2, "airbags"));

            connection = connectionManager.getConnection(null);
            resultSet = DBQueries.search(greaterThan, TABLE_NAME, connection);
            assertResultSet(resultSet, expectedRows);

        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(resultSet);
        }
    }

    @Test
    public void testSearch_Between() throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            Between<Car, String> between = between(Car.FEATURES, "a", "b");

            List<DBQueries.Row<Integer, String>> expectedRows = new ArrayList<DBQueries.Row<Integer, String>>(2);
            expectedRows.add(new DBQueries.Row<Integer, String>(1, "abs"));
            expectedRows.add(new DBQueries.Row<Integer, String>(2, "airbags"));
            expectedRows.add(new DBQueries.Row<Integer, String>(3, "abs"));

            connection = connectionManager.getConnection(null);
            resultSet = DBQueries.search(between, TABLE_NAME, connection);
            assertResultSet(resultSet, expectedRows);

        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(resultSet);
        }
    }

    @Test
    public void testSearch_StringStartsWith() throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            StringStartsWith<Car, String> startsWith = startsWith(Car.FEATURES, "ab");

            List<DBQueries.Row<Integer, String>> expectedRows = new ArrayList<DBQueries.Row<Integer, String>>(2);
            expectedRows.add(new DBQueries.Row<Integer, String>(1, "abs"));
            expectedRows.add(new DBQueries.Row<Integer, String>(3, "abs"));

            connection = connectionManager.getConnection(null);
            resultSet = DBQueries.search(startsWith, TABLE_NAME, connection);
            assertResultSet(resultSet, expectedRows);

        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(resultSet);
        }
    }

    @Test
    public void testSearch_All() throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            All<Car> all = (All<Car>)all(Car.class);

            List<DBQueries.Row<Integer, String>> expectedRows = new ArrayList<DBQueries.Row<Integer, String>>(2);
            expectedRows.add(new DBQueries.Row<Integer, String>(1, "abs"));
            expectedRows.add(new DBQueries.Row<Integer, String>(1, "gps"));
            expectedRows.add(new DBQueries.Row<Integer, String>(2, "airbags"));
            expectedRows.add(new DBQueries.Row<Integer, String>(3, "abs"));

            connection = connectionManager.getConnection(null);
            resultSet = DBQueries.search(all, TABLE_NAME, connection);
            assertResultSet(resultSet, expectedRows);

        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(resultSet);
        }
    }

    @Test
    public void testContains() throws SQLException {
        Connection connection = null;

        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            Equal<Car, String> equal = equal(Car.FEATURES, "abs");

            connection = connectionManager.getConnection(null);
            Assert.assertTrue(DBQueries.contains(1, equal, TABLE_NAME, connection));
            Assert.assertFalse(DBQueries.contains(4, equal, TABLE_NAME, connection));

        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    void createSchema(final ConnectionManager connectionManager){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = connectionManager.getConnection(null);
            statement = connection.createStatement();
            assertEquals(statement.executeUpdate("CREATE TABLE IF NOT EXISTS features (objectKey INTEGER, value TEXT)"), 0);
            assertEquals(statement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_features_value ON features(value)"), 0);
        }catch(Exception e){
            throw new IllegalStateException("Unable to create test database schema", e);
        }finally{
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }
    }

    void initWithTestData(final ConnectionManager connectionManager){

        createSchema(connectionManager);

        Connection connection = null;
        Statement statement = null;
        try {
            connection = connectionManager.getConnection(null);
            statement = connection.createStatement();
            assertEquals(statement.executeUpdate("INSERT INTO features values (1, 'abs')"), 1);
            assertEquals(statement.executeUpdate("INSERT INTO features values (1, 'gps')"), 1);
            assertEquals(statement.executeUpdate("INSERT INTO features values (2, 'airbags')"), 1);
            assertEquals(statement.executeUpdate("INSERT INTO features values (3, 'abs')"), 1);
        }catch(Exception e){
            throw new IllegalStateException("Unable to initialize test database",e);
        }finally{
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }
    }

    public void assertObjectExistenceInSQLIteMasterTable(final String name, final String type, boolean exists, final ConnectionManager connectionManager){
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection = connectionManager.getConnection(null);
            statement = connection.prepareStatement("SELECT name FROM sqlite_master WHERE type=?");
            statement.setString(1, type);
            java.sql.ResultSet indices = statement.executeQuery();

            boolean found = false;
            StringBuilder objectsFound = new StringBuilder();
            String next;
            while(indices.next()){
                next = indices.getString(1);
                objectsFound.append("'").append(next).append("' ");
                if (name.equals(next)){
                    found = true;
                }
            }

            if (exists)
                Assert.assertTrue("Object '" + name + "' must exists in 'sqlite_master' but it doesn't. found:" + found + ". Objects found: " + objectsFound, found);
            else
                Assert.assertFalse("Object '" + name + "' must NOT exists in 'sqlite_master' but it does. found:" + found + "Objects found: " + objectsFound, found);

        }catch(Exception e){
            throw new IllegalStateException("Unable to verify existence of the object '" + name + "' in the 'sqlite_master' table", e);
        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }
    }

    public void assertResultSet(final ResultSet resultSet, final List<DBQueries.Row<Integer, String>> rows){

        try {
            List<DBQueries.Row<Integer, String>> actual = new ArrayList<DBQueries.Row<Integer, String>>(rows.size());
            while (resultSet.next()) {
                actual.add(new DBQueries.Row<Integer, String>(resultSet.getInt(1), resultSet.getString(2)));
            }

            Comparator<DBQueries.Row<Integer, String>> comparator = new Comparator<DBQueries.Row<Integer, String>>() {
                @Override
                public int compare(DBQueries.Row<Integer, String> o1, DBQueries.Row<Integer, String> o2) {
                    int objectKeyComparison = o1.getObjectKey().compareTo(o2.getObjectKey());
                    if (objectKeyComparison != 0){
                        return objectKeyComparison;
                    }
                    return o1.getValue().compareTo(o2.getValue());
                }
            };

            Collections.sort(actual, comparator);
            Collections.sort(rows, comparator);

            Assert.assertEquals(rows, actual);
        }catch(Exception e){
            throw new IllegalStateException("Unable to verify resultSet", e);
        }
    }

    public void assertQueryResultSet(final String query, final List<DBQueries.Row<Integer, String>> rows, final ConnectionManager connectionManager) throws SQLException {

        Connection connection = null;
        Statement statement = null;

        try{
            connection = connectionManager.getConnection(null);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            assertResultSet(resultSet, rows);

        }catch(Exception e){
            throw new IllegalStateException("Unable to verify resultSet", e);
        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }

    }
}