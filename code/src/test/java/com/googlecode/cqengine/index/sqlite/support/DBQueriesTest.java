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

import com.googlecode.cqengine.index.sqlite.ConnectionManager;
import com.googlecode.cqengine.query.simple.*;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.*;

import static com.googlecode.cqengine.index.sqlite.TemporaryDatabase.TemporaryFileDatabase;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.QueryFactory.startsWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DBQueries}
 *
 * @author Silvano Riz
 */
public class DBQueriesTest {

    private static final String NAME = "features";
    private static final String TABLE_NAME = "cqtbl_" + NAME;
    private static final String INDEX_NAME = "cqidx_" + NAME + "_value";

    @Rule
    public TemporaryFileDatabase temporaryFileDatabase = new TemporaryFileDatabase();

    @Test
    public void testCreateIndexTable() throws SQLException {

        Connection connection = null;
        Statement statement = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            connection = spy(connectionManager.getConnection(null, noQueryOptions()));
            statement = spy(connection.createStatement());
            when(connection.createStatement()).thenReturn(statement);

            DBQueries.createIndexTable(NAME, Integer.class, String.class, connection);

            assertObjectExistenceInSQLIteMasterTable(TABLE_NAME, "table", true, connectionManager);
            assertObjectExistenceInSQLIteMasterTable(INDEX_NAME, "index", false, connectionManager);
            verify(statement, times(1)).close();
        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }
    }

    @Test
    public void testCreateIndexOnTable() throws SQLException {

        Connection connection = null;
        Statement statement = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            connection = spy(connectionManager.getConnection(null, noQueryOptions()));
            statement = spy(connection.createStatement());
            when(connection.createStatement()).thenReturn(statement);

            DBQueries.createIndexTable(NAME, Integer.class, String.class, connection);
            DBQueries.createIndexOnTable(NAME, connection);

            assertObjectExistenceInSQLIteMasterTable(TABLE_NAME, "table", true, connectionManager);
            assertObjectExistenceInSQLIteMasterTable(INDEX_NAME, "index", true, connectionManager);
            verify(statement, times(2)).close();
        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }
    }

    @Test
    public void testDropIndexOnTable() throws SQLException {

        Connection connection = null;
        Statement statement = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            connection = spy(connectionManager.getConnection(null, noQueryOptions()));
            statement = spy(connection.createStatement());
            when(connection.createStatement()).thenReturn(statement);

            DBQueries.createIndexTable(NAME, Integer.class, String.class, connection);
            DBQueries.createIndexOnTable(NAME, connection);

            assertObjectExistenceInSQLIteMasterTable(TABLE_NAME, "table", true, connectionManager);
            assertObjectExistenceInSQLIteMasterTable(INDEX_NAME, "index", true, connectionManager);

            DBQueries.dropIndexOnTable(NAME, connection);
            assertObjectExistenceInSQLIteMasterTable(TABLE_NAME, "table", true, connectionManager);
            assertObjectExistenceInSQLIteMasterTable(INDEX_NAME, "index", false, connectionManager);

            verify(statement, times(3)).close();
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
            assertObjectExistenceInSQLIteMasterTable(INDEX_NAME, "index", true, connectionManager);

            connection = spy(connectionManager.getConnection(null, noQueryOptions()));
            statement = spy(connection.createStatement());
            when(connection.createStatement()).thenReturn(statement);
            DBQueries.dropIndexTable(NAME, connection);

            assertObjectExistenceInSQLIteMasterTable(TABLE_NAME, "table", false, connectionManager);
            assertObjectExistenceInSQLIteMasterTable(INDEX_NAME, "index", false, connectionManager);
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
            assertObjectExistenceInSQLIteMasterTable(INDEX_NAME, "index", true, connectionManager);

            connection = spy(connectionManager.getConnection(null, noQueryOptions()));
            statement = spy(connection.createStatement());
            when(connection.createStatement()).thenReturn(statement);
            DBQueries.clearIndexTable(NAME, connection);

            List<DBQueries.Row<Integer, String>> expectedRows = Collections.emptyList();
            assertQueryResultSet("SELECT * FROM " + TABLE_NAME, expectedRows, connectionManager);
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

            connection = connectionManager.getConnection(null, noQueryOptions());
            DBQueries.bulkAdd(rowsToAdd, NAME, connection);
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

            connection = connectionManager.getConnection(null, noQueryOptions());
            DBQueries.bulkRemove(Collections.singletonList(1), NAME, connection);
            assertQueryResultSet("SELECT * FROM " + TABLE_NAME, expectedRows, connectionManager);

        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Test
    public void testGetAllIndexEntries() throws SQLException {

        Connection connection = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            List<DBQueries.Row<Integer, String>> expectedRows = new ArrayList<DBQueries.Row<Integer, String>>(2);
            expectedRows.add(new DBQueries.Row<Integer, String>(1, "abs"));
            expectedRows.add(new DBQueries.Row<Integer, String>(1, "gps"));
            expectedRows.add(new DBQueries.Row<Integer, String>(2, "airbags"));
            expectedRows.add(new DBQueries.Row<Integer, String>(3, "abs"));

            connection = connectionManager.getConnection(null, noQueryOptions());
            ResultSet resultSet = DBQueries.getAllIndexEntries( NAME, connection);
            assertResultSetOrderAgnostic(resultSet, expectedRows);

        }finally {
            DBUtils.closeQuietly(connection);
        }

    }

    @Test
    public void testGetIndexEntryByObjectKey() throws SQLException {

        Connection connection = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            connection = connectionManager.getConnection(null, noQueryOptions());
            ResultSet resultSet = DBQueries.getIndexEntryByObjectKey(3, NAME, connection);

            List<DBQueries.Row<Integer, String>> expectedRows = new ArrayList<DBQueries.Row<Integer, String>>(2);
            expectedRows.add(new DBQueries.Row<Integer, String>(3, "abs"));

            assertResultSetOrderAgnostic(resultSet, expectedRows);

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

            connection = connectionManager.getConnection(null, noQueryOptions());
            int count = DBQueries.count(equal, NAME, connection);
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

            connection = connectionManager.getConnection(null, noQueryOptions());
            resultSet = DBQueries.search(equal, NAME, connection);
            assertResultSetObjectKeysOrderAgnostic(resultSet, Arrays.asList(1, 3));

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

            connection = connectionManager.getConnection(null, noQueryOptions());
            resultSet = DBQueries.search(lessThan, NAME, connection);
            assertResultSetObjectKeysOrderAgnostic(resultSet, Arrays.asList(1, 3));

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

            connection = connectionManager.getConnection(null, noQueryOptions());
            resultSet = DBQueries.search(greaterThan, NAME, connection);
            assertResultSetObjectKeysOrderAgnostic(resultSet, Arrays.asList(1, 2));

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

            connection = connectionManager.getConnection(null, noQueryOptions());
            resultSet = DBQueries.search(between, NAME, connection);
            assertResultSetObjectKeysOrderAgnostic(resultSet, Arrays.asList(1, 2, 3));

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

            connection = connectionManager.getConnection(null, noQueryOptions());
            resultSet = DBQueries.search(startsWith, NAME, connection);
            assertResultSetObjectKeysOrderAgnostic(resultSet, Arrays.asList(1, 3));

        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(resultSet);
        }
    }

    @Test
    public void testSearch_Has() throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            connection = connectionManager.getConnection(null, noQueryOptions());
            resultSet = DBQueries.search(has(selfAttribute(Car.class)), NAME, connection);
            assertResultSetObjectKeysOrderAgnostic(resultSet, Arrays.asList(1, 2, 3));

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

            connection = connectionManager.getConnection(null, noQueryOptions());
            Assert.assertTrue(DBQueries.contains(1, equal, NAME, connection));
            Assert.assertFalse(DBQueries.contains(4, equal, NAME, connection));

        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Test
    public void testEnsureNotNegative_ValidCase() {
        IllegalStateException unexpected = null;
        try {
            DBQueries.ensureNotNegative(0);
            DBQueries.ensureNotNegative(1);
        }
        catch (IllegalStateException e) {
            unexpected = e;
        }
        assertNull(unexpected);
    }

    @Test
    public void testEnsureNotNegative_InvalidCase() {
        IllegalStateException expected = null;
        try {
            DBQueries.ensureNotNegative(-1);
        }
        catch (IllegalStateException e) {
            expected = e;
        }
        assertNotNull(expected);
        assertEquals("Update returned error code: -1", expected.getMessage());
    }

    @Test
    public void getDistinctKeysAndCounts(){

        Connection connection = null;
        ResultSet resultSet = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            connection = connectionManager.getConnection(null, noQueryOptions());
            resultSet = DBQueries.getDistinctKeysAndCounts(false, NAME, connection);

            Map<String, Integer> resultSetToMap = resultSetToMap(resultSet);
            assertEquals(3, resultSetToMap.size());
            assertEquals(new Integer(2), resultSetToMap.get("abs"));
            assertEquals(new Integer(1), resultSetToMap.get("airbags"));
            assertEquals(new Integer(1), resultSetToMap.get("gps"));

        }finally {
            DBUtils.closeQuietly(resultSet);
            DBUtils.closeQuietly(connection);
        }

    }

    @Test
    public void getDistinctKeysAndCounts_SortByKeyDescending(){

        Connection connection = null;
        ResultSet resultSet = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            connection = connectionManager.getConnection(null, noQueryOptions());
            resultSet = DBQueries.getDistinctKeysAndCounts(true, NAME, connection);

            Map<String, Integer> resultSetToMap = resultSetToMap(resultSet);
            assertEquals(3, resultSetToMap.size());

            Iterator<Map.Entry<String, Integer>> entriesIterator = resultSetToMap.entrySet().iterator();

            Map.Entry entry = entriesIterator.next();
            assertEquals("gps", entry.getKey());
            assertEquals(1, entry.getValue());

            entry = entriesIterator.next();
            assertEquals("airbags", entry.getKey());
            assertEquals(1, entry.getValue());

            entry = entriesIterator.next();
            assertEquals("abs", entry.getKey());
            assertEquals(2, entry.getValue());

        }finally {
            DBUtils.closeQuietly(resultSet);
            DBUtils.closeQuietly(connection);
        }

    }

    @Test
    public void getCountOfDistinctKeys(){

        Connection connection = null;
        try {
            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            initWithTestData(connectionManager);

            connection = connectionManager.getConnection(null, noQueryOptions());
            int countOfDistinctKeys = DBQueries.getCountOfDistinctKeys(NAME, connection);
            assertEquals(3, countOfDistinctKeys);

        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Test
    public void suspendSyncAndJournaling() throws Exception {
        Connection connection = null;
        try {

            ConnectionManager connectionManager = temporaryFileDatabase.getConnectionManager(true);
            connection = connectionManager.getConnection(null, noQueryOptions());

            final SQLiteConfig.JournalMode journalMode = DBQueries.getPragmaJournalModeOrNull(connection);
            final SQLiteConfig.SynchronousMode synchronousMode = DBQueries.getPragmaSynchronousOrNull(connection);

            DBQueries.suspendSyncAndJournaling(connection);

            final SQLiteConfig.JournalMode journalModeDisabled = DBQueries.getPragmaJournalModeOrNull(connection);
            final SQLiteConfig.SynchronousMode synchronousModeDisabled = DBQueries.getPragmaSynchronousOrNull(connection);

            Assert.assertEquals(journalModeDisabled, SQLiteConfig.JournalMode.OFF);
            Assert.assertEquals(synchronousModeDisabled, SQLiteConfig.SynchronousMode.OFF);

            DBQueries.setSyncAndJournaling(connection, SQLiteConfig.SynchronousMode.FULL, SQLiteConfig.JournalMode.DELETE);

            final SQLiteConfig.JournalMode journalModeReset = DBQueries.getPragmaJournalModeOrNull(connection);
            final SQLiteConfig.SynchronousMode synchronousModeReset = DBQueries.getPragmaSynchronousOrNull(connection);

            Assert.assertEquals(journalModeReset, journalMode);
            Assert.assertEquals(synchronousModeReset, synchronousMode);

        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Test
    public void testIndexTableExists_ExceptionHandling() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT 1 FROM sqlite_master WHERE type='table' AND name='cqtbl_foo';"))
                .thenThrow(new SQLException("expected_exception"));

        IllegalStateException expected = null;
        try {
            DBQueries.indexTableExists("foo", connection);
        }
        catch (IllegalStateException e) {
            expected = e;
        }
        assertNotNull(expected);
        assertEquals("Unable to determine if table exists: foo", expected.getMessage());
        assertNotNull(expected.getCause());
        assertEquals("expected_exception", expected.getCause().getMessage());
    }

    void createSchema(final ConnectionManager connectionManager){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = connectionManager.getConnection(null, noQueryOptions());
            statement = connection.createStatement();
            assertEquals(statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (objectKey INTEGER, value TEXT)"), 0);
            assertEquals(statement.executeUpdate("CREATE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME + "(value)"), 0);
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
            connection = connectionManager.getConnection(null, noQueryOptions());
            statement = connection.createStatement();
            assertEquals(statement.executeUpdate("INSERT INTO " + TABLE_NAME + " values (1, 'abs')"), 1);
            assertEquals(statement.executeUpdate("INSERT INTO " + TABLE_NAME + " values (1, 'gps')"), 1);
            assertEquals(statement.executeUpdate("INSERT INTO " + TABLE_NAME + " values (2, 'airbags')"), 1);
            assertEquals(statement.executeUpdate("INSERT INTO " + TABLE_NAME + " values (3, 'abs')"), 1);
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
            connection = connectionManager.getConnection(null, noQueryOptions());
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
                Assert.assertTrue("Object '" + name + "' must exists in 'sqlite_master' but it doesn't. found: " + found + ". Objects found: " + objectsFound, found);
            else
                Assert.assertFalse("Object '" + name + "' must NOT exists in 'sqlite_master' but it does. found: " + found + " Objects found: " + objectsFound, found);

        }catch(Exception e){
            throw new IllegalStateException("Unable to verify existence of the object '" + name + "' in the 'sqlite_master' table", e);
        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }
    }

    public static <K, V> Map<K, V> resultSetToMap(final ResultSet resultSet){

        try {
            final Map<K, V> map = new LinkedHashMap<K, V>();

            while (resultSet.next()) {

                @SuppressWarnings("unchecked")
                K key = (K) resultSet.getObject(1);
                @SuppressWarnings("unchecked")
                V value = (V) resultSet.getObject(2);

                map.put(key, value);
            }

            return map;
        }catch(Exception e){
            throw new IllegalStateException("Unable to transform the resultSet into a Map", e);
        }
    }

    public void assertResultSetObjectKeysOrderAgnostic(final ResultSet resultSet, final List<Integer> objectKeys){
        try {
            List<Integer> actual = new ArrayList<Integer>(objectKeys.size());
            while (resultSet.next()) {
                actual.add(resultSet.getInt(1));
            }

            Collections.sort(actual);
            Collections.sort(objectKeys);

            Assert.assertEquals(objectKeys, actual);

        }catch(Exception e){
            throw new IllegalStateException("Unable to verify resultSet", e);
        }
    }

    public void assertResultSetOrderAgnostic(final ResultSet resultSet, final List<DBQueries.Row<Integer, String>> rows){

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
            connection = connectionManager.getConnection(null, noQueryOptions());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            assertResultSetOrderAgnostic(resultSet, rows);

        }catch(Exception e){
            throw new IllegalStateException("Unable to verify resultSet", e);
        }finally {
            DBUtils.closeQuietly(connection);
            DBUtils.closeQuietly(statement);
        }

    }
}