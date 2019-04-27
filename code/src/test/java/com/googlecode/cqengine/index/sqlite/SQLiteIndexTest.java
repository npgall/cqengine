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
package com.googlecode.cqengine.index.sqlite;

import com.google.common.collect.*;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.sqlite.support.DBQueries;
import com.googlecode.cqengine.index.support.CloseableIterable;
import com.googlecode.cqengine.index.support.KeyStatistics;
import com.googlecode.cqengine.index.support.KeyValue;
import com.googlecode.cqengine.persistence.support.ConcurrentOnHeapObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.FilterQuery;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static com.googlecode.cqengine.testutil.TestUtil.setOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SQLiteIndex}
 *
 * @author Silvano Riz
 */
public class SQLiteIndexTest {

    private static final String TABLE_NAME = "cqtbl_features";
    private static final String INDEX_NAME = "cqidx_features_value";

    public static final SimpleAttribute<Car, Integer> OBJECT_TO_ID = Car.CAR_ID;

    public static final SimpleAttribute<Integer, Car> ID_TO_OBJECT = new SimpleAttribute<Integer, Car>("carFromId") {
        public Car getValue(Integer carId, QueryOptions queryOptions) { return null; }
    };

    public static List<Car> data = Arrays.asList(
            new Car(1, "Ford", "Focus", Car.Color.BLUE, 5, 9000.50, Arrays.asList("abs", "gps")),
            new Car(2, "Honda", "Civic", Car.Color.RED, 5, 5000.00, Arrays.asList("airbags")),
            new Car(3, "Toyota", "Prius", Car.Color.BLACK, 3, 9700.00, Arrays.asList("abs")),
            new Car(4, "Fiat", "Panda", Car.Color.BLUE, 5, 5600.00, Collections.<String>emptyList()),
            new Car(5, "Fiat", "Punto", Car.Color.BLUE, 5, 5600.00, Arrays.asList("gps"))
    );

    @Rule
    public TemporaryDatabase.TemporaryInMemoryDatabase temporaryInMemoryDatabase = new TemporaryDatabase.TemporaryInMemoryDatabase();

    @Test
    public void testNew1() throws Exception {

        SQLiteIndex<String, Car, Integer> carFeaturesOffHeapIndex = SQLiteIndex.onAttribute(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT
        );

        assertNotNull(carFeaturesOffHeapIndex);
    }

    @Test
    public void testNew2() throws Exception {

        SQLiteIndex<String, Car, Integer> carFeaturesOffHeapIndex = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                "_tableSuffix"
        );

        assertNotNull(carFeaturesOffHeapIndex);
        assertEquals("features_tableSuffix", carFeaturesOffHeapIndex.tableName);
    }

    @Test
    public void testGetConnectionManager(){

        ConnectionManager connectionManager = mock(ConnectionManager.class);
        QueryOptions queryOptions = mock(QueryOptions.class);
        when(queryOptions.get(ConnectionManager.class)).thenReturn(connectionManager);

        SQLiteIndex<String, Car, Integer> carFeaturesOffHeapIndex = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                ""
        );

        assertEquals(connectionManager, carFeaturesOffHeapIndex.getConnectionManager(queryOptions));
    }

    @Test
    public void testNotifyObjectsRemoved() throws Exception{

        // Mock
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection).thenReturn(connection);
        when(connectionManager.isApplyUpdateForIndexEnabled(any(SQLiteIndex.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE objectKey = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeBatch()).thenReturn(new int[] {1});

        // The objects to add
        Set<Car> removedObjects = new HashSet<Car>(2);
        removedObjects.add(new Car(1, "Ford", "Focus", Car.Color.BLUE, 5, 9000.50, Arrays.asList("abs", "gps")));
        removedObjects.add(new Car(2, "Honda", "Civic", Car.Color.RED, 5, 5000.00, Arrays.asList("airbags")));

        @SuppressWarnings({"unchecked", "unused"})
        SQLiteIndex<String, Car, Integer> carFeaturesOffHeapIndex = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                ""
        );

        carFeaturesOffHeapIndex.removeAll(objectSet(removedObjects), createQueryOptions(connectionManager));

        // Verify
        verify(statement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (objectKey INTEGER, value TEXT, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;");
        verify(statement, times(1)).executeUpdate("CREATE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME + " (value);");

        verify(preparedStatement, times(1)).setObject(1, 1);
        verify(preparedStatement, times(1)).setObject(1, 2);
        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(1)).executeBatch();

        verify(connection, times(0)).close();
    }

    @Test
    public void testNotifyObjectsAdded() throws Exception {

        // Mock
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection).thenReturn(connection);
        when(connectionManager.isApplyUpdateForIndexEnabled(any(SQLiteIndex.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.prepareStatement("INSERT OR IGNORE INTO " + TABLE_NAME + " values(?, ?);")).thenReturn(preparedStatement);
        when(preparedStatement.executeBatch()).thenReturn(new int[] {2});
        // The objects to add
        Set<Car> addedObjects = new HashSet<Car>(2);
        addedObjects.add(new Car(1, "Ford", "Focus", Car.Color.BLUE, 5, 9000.50, Arrays.asList("abs", "gps")));
        addedObjects.add(new Car(2, "Honda", "Civic", Car.Color.RED, 5, 5000.00, Arrays.asList("airbags")));

        // Create the index and cal the addAll
        SQLiteIndex<String, Car, Integer> carFeaturesOffHeapIndex = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                ""
        );
        carFeaturesOffHeapIndex.addAll(objectSet(addedObjects), createQueryOptions(connectionManager));

        // Verify
        verify(statement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (objectKey INTEGER, value TEXT, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;");
        verify(statement, times(1)).executeUpdate("CREATE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME + " (value);");

        verify(preparedStatement, times(2)).setObject(1, 1);
        verify(preparedStatement, times(1)).setObject(1, 2);
        verify(preparedStatement, times(1)).setObject(2, "abs");
        verify(preparedStatement, times(1)).setObject(2, "gps");
        verify(preparedStatement, times(1)).setObject(2, "airbags");
        verify(preparedStatement, times(3)).addBatch();
        verify(preparedStatement, times(1)).executeBatch();

        verify(connection, times(0)).close();
    }

    @Test
    public void testNotifyObjectsCleared() throws Exception{

        // Mock
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection).thenReturn(connection);
        when(connectionManager.isApplyUpdateForIndexEnabled(any(SQLiteIndex.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement).thenReturn(statement).thenReturn(statement);

        @SuppressWarnings({"unchecked", "unused"})
        SQLiteIndex<String, Car, Integer> carFeaturesOffHeapIndex = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                ""
        );

        carFeaturesOffHeapIndex.clear(createQueryOptions(connectionManager));

        // Verify
        verify(statement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (objectKey INTEGER, value TEXT, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;");
        verify(statement, times(1)).executeUpdate("CREATE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME + " (value);");
        verify(statement, times(1)).executeUpdate("DELETE FROM " + TABLE_NAME + ";");

        verify(connection, times(0)).close();
    }

    /**
     * Verifies that if connectionManager.isApplyUpdateForIndexEnabled() returns false,
     * init() will do nothing.
     */
    @Test
    public void testInit_ApplyUpdateForIndexIsFalse() throws Exception{
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        // Simulate isApplyUpdateForIndexEnabled == false...
        when(connectionManager.isApplyUpdateForIndexEnabled(any(SQLiteIndex.class))).thenReturn(false);

        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);

        java.sql.ResultSet journalModeRs = mock(java.sql.ResultSet.class);
        java.sql.ResultSet synchronousRs = mock(java.sql.ResultSet.class);
        when(journalModeRs.next()).thenReturn(true).thenReturn(false);
        when(synchronousRs.next()).thenReturn(true).thenReturn(false);

        when(journalModeRs.getString(1)).thenReturn("DELETE");
        when(synchronousRs.getInt(1)).thenReturn(2);

        when(statement.executeQuery("PRAGMA journal_mode;")).thenReturn(journalModeRs);
        when(statement.executeQuery("PRAGMA synchronous;")).thenReturn(synchronousRs);

        SQLiteIndex<String, Car, Integer> carFeaturesOffHeapIndex = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                ""
        );

        carFeaturesOffHeapIndex.init(emptyObjectStore(), createQueryOptions(connectionManager));

        verify(statement, times(1)).executeQuery("PRAGMA journal_mode;");
        verify(statement, times(1)).executeQuery("PRAGMA synchronous;");
        verify(statement, times(2)).close();

        Assert.assertEquals(carFeaturesOffHeapIndex.pragmaSynchronous, SQLiteConfig.SynchronousMode.FULL);
        Assert.assertEquals(carFeaturesOffHeapIndex.pragmaJournalMode, SQLiteConfig.JournalMode.DELETE);
        Assert.assertTrue(carFeaturesOffHeapIndex.canModifySyncAndJournaling);

    }

    /**
     * Verifies that if connectionManager.isApplyUpdateForIndexEnabled() returns true,
     * and the index table already exists, init() will do nothing.
     */
    @Test
    public void testInit_IndexTableExists() throws Exception{
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        java.sql.ResultSet tableCheckRs = mock(java.sql.ResultSet.class);
        java.sql.ResultSet journalModeRs = mock(java.sql.ResultSet.class);
        java.sql.ResultSet synchronousRs = mock(java.sql.ResultSet.class);
        when(tableCheckRs.next()).thenReturn(true); // <- simulates a preexisting table
        when(journalModeRs.next()).thenReturn(true).thenReturn(false);
        when(synchronousRs.next()).thenReturn(true).thenReturn(false);

        when(journalModeRs.getString(1)).thenReturn("DELETE");
        when(synchronousRs.getInt(1)).thenReturn(2);

        when(statement.executeQuery("SELECT 1 FROM sqlite_master WHERE type='table' AND name='cqtbl_features';")).thenReturn(tableCheckRs);
        when(statement.executeQuery("PRAGMA journal_mode;")).thenReturn(journalModeRs);
        when(statement.executeQuery("PRAGMA synchronous;")).thenReturn(synchronousRs);

        when(connection.prepareStatement("INSERT OR IGNORE INTO " + TABLE_NAME + " values(?, ?);")).thenReturn(preparedStatement);
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connectionManager.isApplyUpdateForIndexEnabled(any(SQLiteIndex.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement);
        when(preparedStatement.executeBatch()).thenReturn(new int[] {2});

        // The objects to add
        Set<Car> initWithObjects = new HashSet<Car>(2);
        initWithObjects.add(new Car(1, "Ford", "Focus", Car.Color.BLUE, 5, 9000.50, Arrays.asList("abs", "gps")));
        initWithObjects.add(new Car(2, "Honda", "Civic", Car.Color.RED, 5, 5000.00, Arrays.asList("airbags")));

        SQLiteIndex<String, Car, Integer> carFeaturesOffHeapIndex = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                ""
        );

        carFeaturesOffHeapIndex.init(wrappingObjectStore(initWithObjects), createQueryOptions(connectionManager));

        // Verify
        verify(statement, times(1)).executeQuery("PRAGMA journal_mode;");
        verify(statement, times(1)).executeQuery("PRAGMA synchronous;");

        // Verify we should not proceed to recreate the table...
        verify(statement, times(0)).executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (objectKey INTEGER, value TEXT, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;");
    }

    /**
     * Verifies that if connectionManager.isApplyUpdateForIndexEnabled() returns true,
     * and the index table does not exist, init() will create and populate the index table.
     */
    @Test
    public void testInit_IndexTableDoesNotExist() throws Exception{
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        java.sql.ResultSet tableCheckRs = mock(java.sql.ResultSet.class);
        java.sql.ResultSet journalModeRs = mock(java.sql.ResultSet.class);
        java.sql.ResultSet synchronousRs = mock(java.sql.ResultSet.class);
        when(tableCheckRs.next()).thenReturn(false); // <- simulates table does not already exist
        when(journalModeRs.next()).thenReturn(true).thenReturn(false);
        when(synchronousRs.next()).thenReturn(true).thenReturn(false);

        when(journalModeRs.getString(1)).thenReturn("DELETE");
        when(synchronousRs.getInt(1)).thenReturn(2);

        when(statement.executeQuery("SELECT 1 FROM sqlite_master WHERE type='table' AND name='cqtbl_features';")).thenReturn(tableCheckRs);
        when(statement.executeQuery("PRAGMA journal_mode;")).thenReturn(journalModeRs);
        when(statement.executeQuery("PRAGMA synchronous;")).thenReturn(synchronousRs);

        when(connection.prepareStatement("INSERT OR IGNORE INTO " + TABLE_NAME + " values(?, ?);")).thenReturn(preparedStatement);
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connectionManager.isApplyUpdateForIndexEnabled(any(SQLiteIndex.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement);
        when(preparedStatement.executeBatch()).thenReturn(new int[] {2});

        // The objects to add
        Set<Car> initWithObjects = new HashSet<Car>(2);
        initWithObjects.add(new Car(1, "Ford", "Focus", Car.Color.BLUE, 5, 9000.50, Arrays.asList("abs", "gps")));
        initWithObjects.add(new Car(2, "Honda", "Civic", Car.Color.RED, 5, 5000.00, Arrays.asList("airbags")));

        SQLiteIndex<String, Car, Integer> carFeaturesOffHeapIndex = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                ""
        );

        carFeaturesOffHeapIndex.init(wrappingObjectStore(initWithObjects), createQueryOptions(connectionManager));

        // Verify
        verify(statement, times(1)).executeQuery("PRAGMA journal_mode;");
        verify(statement, times(1)).executeQuery("PRAGMA synchronous;");
        verify(statement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (objectKey INTEGER, value TEXT, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;");
        verify(statement, times(1)).executeUpdate("CREATE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME + " (value);");
        verify(statement, times(6)).close();

        verify(preparedStatement, times(2)).setObject(1, 1);
        verify(preparedStatement, times(1)).setObject(1, 2);
        verify(preparedStatement, times(1)).setObject(2, "abs");
        verify(preparedStatement, times(1)).setObject(2, "gps");
        verify(preparedStatement, times(1)).setObject(2, "airbags");
        verify(preparedStatement, times(3)).addBatch();
        verify(preparedStatement, times(1)).executeBatch();
        verify(preparedStatement, times(1)).close();

        verify(connection, times(0)).close();

        Assert.assertEquals(carFeaturesOffHeapIndex.pragmaSynchronous, SQLiteConfig.SynchronousMode.FULL);
        Assert.assertEquals(carFeaturesOffHeapIndex.pragmaJournalMode, SQLiteConfig.JournalMode.DELETE);
        Assert.assertTrue(carFeaturesOffHeapIndex.canModifySyncAndJournaling);
    }

    @Test
    public void testNewResultSet_Size() throws Exception{

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(1) AS countDistinct FROM (SELECT objectKey FROM " + TABLE_NAME + " WHERE value = ? GROUP BY objectKey);")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                "")

                .retrieve(equal(Car.FEATURES, "abs"), createQueryOptions(connectionManager));


        assertNotNull(carsWithAbs);
        int size = carsWithAbs.size();

        assertEquals(3, size);
        verify(connection, times(0)).close();

    }

    @Test
    public void testNewResultSet_GetRetrievalCost() throws Exception{

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);

        // Iterator
        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                "")

                .retrieve(equal(Car.FEATURES, "abs"), createQueryOptions(connectionManager));

        assertEquals(SQLiteIndex.INDEX_RETRIEVAL_COST, carsWithAbs.getRetrievalCost());

    }

    @Test
    public void testNewResultSet_GetMergeCost() throws Exception{

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(objectKey) FROM " + TABLE_NAME + " WHERE value = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        // Iterator
        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                "")

                .retrieve(equal(Car.FEATURES, "abs"), createQueryOptions(connectionManager));

        assertNotNull(carsWithAbs);
        int size = carsWithAbs.getMergeCost();

        assertEquals(3, size);
        verify(connection, times(0)).close();

    }

    @Test
    public void testNewResultSet_Contains() throws Exception{

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connectionContains = mock(Connection.class);
        Connection connectionDoNotContain = mock(Connection.class);
        PreparedStatement preparedStatementContains = mock(PreparedStatement.class);
        PreparedStatement preparedStatementDoNotContains = mock(PreparedStatement.class);
        java.sql.ResultSet resultSetContains = mock(java.sql.ResultSet.class);
        java.sql.ResultSet resultSetDoNotContain = mock(java.sql.ResultSet.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connectionContains).thenReturn(connectionDoNotContain);
        when(connectionContains.prepareStatement("SELECT objectKey FROM " + TABLE_NAME + " WHERE value = ? AND objectKey = ? LIMIT 1;")).thenReturn(preparedStatementContains);
        when(connectionDoNotContain.prepareStatement("SELECT objectKey FROM " + TABLE_NAME + " WHERE value = ? AND objectKey = ? LIMIT 1;")).thenReturn(preparedStatementDoNotContains);
        when(preparedStatementContains.executeQuery()).thenReturn(resultSetContains);
        when(preparedStatementDoNotContains.executeQuery()).thenReturn(resultSetDoNotContain);
        when(resultSetContains.next()).thenReturn(true).thenReturn(false);
        when(resultSetDoNotContain.next()).thenReturn(false);

        // Iterator
        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                "")

                .retrieve(equal(Car.FEATURES, "abs"), createQueryOptions(connectionManager));

        assertNotNull(carsWithAbs);
        boolean resultContains = carsWithAbs.contains(data.get(0));
        assertTrue(resultContains);
        verify(connectionContains, times(0)).close();

        boolean resultDoNotContain = carsWithAbs.contains(data.get(1));
        assertFalse(resultDoNotContain);
        verify(connectionDoNotContain, times(0)).close();


    }

    @Test(expected = IllegalStateException.class)
    public void testNewResultSet_Iterator_Exception_Close() throws Exception{

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);
        @SuppressWarnings("unchecked")
        SimpleAttribute<Integer, Car> idToObject = (SimpleAttribute<Integer, Car>)mock(SimpleAttribute.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.prepareStatement("SELECT DISTINCT objectKey FROM " + TABLE_NAME + " WHERE value = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenThrow(new SQLException("SQL exception"));
        when(idToObject.getValue(eq(1), anyQueryOptions())).thenReturn(data.get(0));

        // Iterator
        try {
            ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                    Car.FEATURES,
                    OBJECT_TO_ID,
                    idToObject,
                    "")

                    .retrieve(equal(Car.FEATURES, "abs"), createQueryOptions(connectionManager));

            assertNotNull(carsWithAbs);
            Iterator<Car> carsWithAbsIterator = carsWithAbs.iterator();
            assertNotNull(carsWithAbsIterator.next());
            carsWithAbsIterator.next();// Should throw exception!

        }finally {
            verify(connection, times(0)).close(); // Connection should be left open
            verify(preparedStatement, times(1)).close();
            verify(resultSet, times(1)).close();
        }

    }

    @Test
    public void testNewResultSet_Iterator_Close() throws Exception{


        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);
        @SuppressWarnings("unchecked")
        SimpleAttribute<Integer, Car> idToObject = (SimpleAttribute<Integer, Car>)mock(SimpleAttribute.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.prepareStatement("SELECT DISTINCT objectKey FROM " + TABLE_NAME + " WHERE value = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenReturn(3);
        when(idToObject.getValue(eq(1), anyQueryOptions())).thenReturn(data.get(0));
        when(idToObject.getValue(eq(3), anyQueryOptions())).thenReturn(data.get(2));

        // Iterator
        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                idToObject,
                "")

                .retrieve(equal(Car.FEATURES, "abs"), createQueryOptions(connectionManager));


        assertNotNull(carsWithAbs);
        Iterator carsWithAbsIterator = carsWithAbs.iterator();

        assertTrue(carsWithAbsIterator.hasNext());
        assertNotNull(carsWithAbsIterator.next());
        assertTrue(carsWithAbsIterator.hasNext());
        assertNotNull(carsWithAbsIterator.next());
        assertFalse(carsWithAbsIterator.hasNext());

        // The end of the iteration should close the resources
        verify(connection, times(0)).close(); // Connection should be left open
        verify(preparedStatement, times(1)).close();
        verify(resultSet, times(1)).close();

    }

    @Test
    public void testNewResultSet_Close() throws Exception{

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);

        @SuppressWarnings("unchecked")
        SimpleAttribute<Integer, Car> idToObject = (SimpleAttribute<Integer, Car>)mock(SimpleAttribute.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.prepareStatement("SELECT DISTINCT objectKey FROM " + TABLE_NAME + " WHERE value = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenReturn(3);
        when(idToObject.getValue(eq(1), anyQueryOptions())).thenReturn(data.get(0));
        when(idToObject.getValue(eq(3), anyQueryOptions())).thenReturn(data.get(2));

        // Iterator
        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                idToObject,
                "")

                .retrieve(equal(Car.FEATURES, "abs"), createQueryOptions(connectionManager));

        assertNotNull(carsWithAbs);
        Iterator carsWithAbsIterator = carsWithAbs.iterator();
        assertTrue(carsWithAbsIterator.hasNext());
        assertNotNull(carsWithAbsIterator.next());
        // Do not continue with the iteration, but close
        carsWithAbs.close();

        verify(connection, times(0)).close(); // Connection should be left open
        verify(preparedStatement, times(1)).close();
        verify(resultSet, times(1)).close();

    }

    @Test
    public void testRowIterable(){

        Iterable<DBQueries.Row<Integer, String>> rows = SQLiteIndex.rowIterable(data, Car.CAR_ID, Car.FEATURES, null);
        assertNotNull(rows);

        Iterator<DBQueries.Row<Integer, String>> rowsIterator = rows.iterator();
        assertNotNull(rowsIterator);
        assertTrue(rowsIterator.hasNext());
        assertEquals(new DBQueries.Row<Integer, String>(1, "abs"), rowsIterator.next());
        assertTrue(rowsIterator.hasNext());
        assertEquals(new DBQueries.Row<Integer, String>(1, "gps"), rowsIterator.next());
        assertTrue(rowsIterator.hasNext());
        assertEquals(new DBQueries.Row<Integer, String>(2, "airbags"), rowsIterator.next());
        assertTrue(rowsIterator.hasNext());
        assertEquals(new DBQueries.Row<Integer, String>(3, "abs"), rowsIterator.next());
        assertTrue(rowsIterator.hasNext());
        assertEquals(new DBQueries.Row<Integer, String>(5, "gps"), rowsIterator.next());
        assertFalse(rowsIterator.hasNext());
    }

    @Test
    public void testObjectKeyIterable(){

        Iterable<Integer> objectKeys = SQLiteIndex.objectKeyIterable(data, Car.CAR_ID, null);
        assertNotNull(objectKeys);

        Iterator<Integer> objectKeysIterator = objectKeys.iterator();
        assertNotNull(objectKeysIterator);
        assertTrue(objectKeysIterator.hasNext());
        assertEquals(new Integer(1), objectKeysIterator.next());
        assertTrue(objectKeysIterator.hasNext());
        assertEquals(new Integer(2), objectKeysIterator.next());
        assertTrue(objectKeysIterator.hasNext());
        assertEquals(new Integer(3), objectKeysIterator.next());
        assertTrue(objectKeysIterator.hasNext());
        assertEquals(new Integer(4), objectKeysIterator.next());
        assertTrue(objectKeysIterator.hasNext());
        assertEquals(new Integer(5), objectKeysIterator.next());
        assertFalse(objectKeysIterator.hasNext());
    }

    @Test
    public void testGetDistinctKeys_AllAscending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MODEL,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));

        List<String> expected = Arrays.asList("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus");
        List<String> actual = Lists.newArrayList(offHeapIndex.getDistinctKeys(createQueryOptions(connectionManager)));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDistinctKeys_AllDescending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MODEL,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));

        List<String> expected = Arrays.asList("Taurus", "Prius", "M6", "Insight", "Hilux", "Fusion", "Focus", "Civic", "Avensis", "Accord");
        List<String> actual = Lists.newArrayList(offHeapIndex.getDistinctKeysDescending(createQueryOptions(connectionManager)));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDistinctKeys_GreaterThanExclusiveAscending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MODEL,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));
        List<String> expected, actual;

        expected = Arrays.asList("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys("", false, null, true, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);

        expected = Arrays.asList("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys("A", false, null, true, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);

        expected = Arrays.asList("Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys("Accord", false, null, true, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDistinctKeys_GreaterThanInclusiveAscending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MODEL,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));
        List<String> expected, actual;

        expected = Arrays.asList("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys("Accord", true, null, true, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDistinctKeys_LessThanExclusiveAscending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MODEL,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));
        List<String> expected, actual;

        expected = Arrays.asList();
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys(null, true, "", false, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);

        expected = Arrays.asList("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius", "Taurus");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys(null, true, "Z", false, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);

        expected = Arrays.asList("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys(null, true, "Prius", false, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDistinctKeys_LessThanInclusiveAscending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MODEL,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));
        List<String> expected, actual;

        expected = Arrays.asList("Accord", "Avensis", "Civic", "Focus", "Fusion", "Hilux", "Insight", "M6", "Prius");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys(null, true, "Prius", true, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDistinctKeys_BetweenExclusiveAscending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MODEL,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));
        List<String> expected, actual;

        expected = Arrays.asList("Focus", "Fusion", "Hilux");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys("Civic", false, "Insight", false, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDistinctKeys_BetweenInclusiveAscending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MODEL,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));
        List<String> expected, actual;

        expected = Arrays.asList("Civic", "Focus", "Fusion", "Hilux", "Insight");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeys("Civic", true, "Insight", true, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDistinctKeys_BetweenInclusiveDescending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MODEL,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));
        List<String> expected, actual;

        expected = Arrays.asList("Insight", "Hilux", "Fusion", "Focus", "Civic");
        actual = Lists.newArrayList(offHeapIndex.getDistinctKeysDescending("Civic", true, "Insight", true, createQueryOptions(connectionManager)));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetKeysAndValues(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MANUFACTURER,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));

        Multimap<String, Car> expected = MultimapBuilder.SetMultimapBuilder.linkedHashKeys().hashSetValues().build();
        expected.put("BMW", CarFactory.createCar(9));
        expected.put("Ford", CarFactory.createCar(0));
        expected.put("Ford", CarFactory.createCar(1));
        expected.put("Ford", CarFactory.createCar(2));
        expected.put("Honda", CarFactory.createCar(3));
        expected.put("Honda", CarFactory.createCar(4));
        expected.put("Honda", CarFactory.createCar(5));
        expected.put("Toyota", CarFactory.createCar(6));
        expected.put("Toyota", CarFactory.createCar(7));
        expected.put("Toyota", CarFactory.createCar(8));


        Multimap<String, Car> actual = MultimapBuilder.SetMultimapBuilder.linkedHashKeys().hashSetValues().build();

        CloseableIterable<KeyValue<String, Car>> keysAndValues = offHeapIndex.getKeysAndValues(createQueryOptions(connectionManager));

        for (KeyValue<String, Car> keyValue : keysAndValues) {
            actual.put(keyValue.getKey(), keyValue.getValue());
        }

        assertEquals("keys and values", expected, actual);

        List<String> expectedKeysOrder = Lists.newArrayList(expected.keySet());
        List<String> actualKeysOrder = Lists.newArrayList(actual.keySet());
        assertEquals("key order", expectedKeysOrder, actualKeysOrder);
    }

    @Test
    public void testGetKeysAndValuesDescending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MANUFACTURER,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(10), createQueryOptions(connectionManager));

        Multimap<String, Car> expected = MultimapBuilder.SetMultimapBuilder.linkedHashKeys().hashSetValues().build();
        expected.put("Toyota", CarFactory.createCar(6));
        expected.put("Toyota", CarFactory.createCar(7));
        expected.put("Toyota", CarFactory.createCar(8));
        expected.put("Honda", CarFactory.createCar(3));
        expected.put("Honda", CarFactory.createCar(4));
        expected.put("Honda", CarFactory.createCar(5));
        expected.put("Ford", CarFactory.createCar(0));
        expected.put("Ford", CarFactory.createCar(1));
        expected.put("Ford", CarFactory.createCar(2));
        expected.put("BMW", CarFactory.createCar(9));

        Multimap<String, Car> actual = MultimapBuilder.SetMultimapBuilder.linkedHashKeys().hashSetValues().build();

        CloseableIterable<KeyValue<String, Car>> keysAndValues = offHeapIndex.getKeysAndValuesDescending(createQueryOptions(connectionManager));

        for (KeyValue<String, Car> keyValue : keysAndValues) {
            actual.put(keyValue.getKey(), keyValue.getValue());
        }

        assertEquals("keys and values", expected, actual);

        List<String> expectedKeysOrder = Lists.newArrayList(expected.keySet());
        List<String> actualKeysOrder = Lists.newArrayList(actual.keySet());
        assertEquals("key order", expectedKeysOrder, actualKeysOrder);
    }

    @Test
    public void testGetCountOfDistinctKeys(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MANUFACTURER,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(20), createQueryOptions(connectionManager));

        Assert.assertEquals(Integer.valueOf(4), offHeapIndex.getCountOfDistinctKeys(createQueryOptions(connectionManager)));
    }

    @Test
    public void testGetStatisticsForDistinctKeys(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MANUFACTURER,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(20), createQueryOptions(connectionManager));

        Set<KeyStatistics<String>> keyStatistics = setOf(offHeapIndex.getStatisticsForDistinctKeys(createQueryOptions(connectionManager)));
        Assert.assertEquals(setOf(
                        new KeyStatistics<String>("Ford", 6),
                        new KeyStatistics<String>("Honda", 6),
                        new KeyStatistics<String>("Toyota", 6),
                        new KeyStatistics<String>("BMW", 2)

                ),
                keyStatistics);
    }

    @Test
    public void testGetStatisticsForDistinctKeysDescending(){
        ConnectionManager connectionManager = temporaryInMemoryDatabase.getConnectionManager(true);
        SQLiteIndex<String, Car, Integer> offHeapIndex = SQLiteIndex.onAttribute(
                Car.MANUFACTURER,
                Car.CAR_ID,
                new SimpleAttribute<Integer, Car>() {
                    @Override
                    public Car getValue(Integer carId, QueryOptions queryOptions) {
                        return CarFactory.createCar(carId);
                    }
                }
        );
        offHeapIndex.addAll(createObjectSetOfCars(20), createQueryOptions(connectionManager));

        Set<KeyStatistics<String>> keyStatistics = setOf(offHeapIndex.getStatisticsForDistinctKeysDescending(createQueryOptions(connectionManager)));
        Assert.assertEquals(setOf(
                        new KeyStatistics<String>("Toyota", 6),
                        new KeyStatistics<String>("Honda", 6),
                        new KeyStatistics<String>("Ford", 6),
                        new KeyStatistics<String>("BMW", 2)

                ),
                keyStatistics);
    }

    @Test(expected = IllegalStateException.class)
    public void testNewResultSet_FilterQuery_Iterator_Exception_Close() throws Exception{

        // Mocks
        FilterQuery<Car, String> filterQuery = mockFilterQuery();
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);
        @SuppressWarnings("unchecked")
        SimpleAttribute<Integer, Car> idToObject = (SimpleAttribute<Integer, Car>)mock(SimpleAttribute.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT objectKey, value FROM " + TABLE_NAME + " ORDER BY objectKey;")).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(statement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenThrow(new SQLException("SQL exception"));
        when(idToObject.getValue(eq(1), anyQueryOptions())).thenReturn(data.get(0));

        // Iterator
        try {
            ResultSet<Car> cars = new SQLiteIndex<String, Car, Integer>(
                    Car.FEATURES,
                    OBJECT_TO_ID,
                    idToObject,
                    "")

                    .retrieve(filterQuery, createQueryOptions(connectionManager));

            assertNotNull(cars);
            Iterator<Car> carsWithAbsIterator = cars.iterator();
            assertNotNull(carsWithAbsIterator.next());
            carsWithAbsIterator.next();// Should throw exception!

        }finally {
            verify(connection, times(0)).close(); // Connection should be left open
            verify(statement, times(1)).close();
            verify(resultSet, times(1)).close();
        }

    }

    @Test
    public void testNewResultSet_FilterQuery_Iterator_Close() throws Exception{

        // Mocks
        FilterQuery<Car, String> filterQuery = mockFilterQuery();
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);
        @SuppressWarnings("unchecked")
        SimpleAttribute<Integer, Car> idToObject = (SimpleAttribute<Integer, Car>)mock(SimpleAttribute.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT objectKey, value FROM " + TABLE_NAME + " ORDER BY objectKey;")).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(statement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        when(resultSet.getString(2)).thenReturn("abs").thenReturn("gps").thenReturn("airbags").thenReturn("abs").thenReturn("").thenReturn("gps");
        when(idToObject.getValue(eq(1), anyQueryOptions())).thenReturn(data.get(0));
        when(idToObject.getValue(eq(3), anyQueryOptions())).thenReturn(data.get(2));
        when(idToObject.getValue(eq(5), anyQueryOptions())).thenReturn(data.get(4));

        // Iterator
        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                idToObject,
                "")

                .retrieve(filterQuery, createQueryOptions(connectionManager));


        assertNotNull(carsWithAbs);
        Iterator carsWithAbsIterator = carsWithAbs.iterator();

        assertTrue(carsWithAbsIterator.hasNext());
        assertNotNull(carsWithAbsIterator.next());
        assertTrue(carsWithAbsIterator.hasNext());
        assertNotNull(carsWithAbsIterator.next());
        assertTrue(carsWithAbsIterator.hasNext());
        assertNotNull(carsWithAbsIterator.next());
        assertTrue(carsWithAbsIterator.hasNext());
        assertNotNull(carsWithAbsIterator.next());
        assertFalse(carsWithAbsIterator.hasNext());

        // The end of the iteration should close the resources
        verify(connection, times(0)).close(); // Connection should be left open
        verify(statement, times(1)).close();
        verify(resultSet, times(1)).close();

    }

    @Test
    public void testNewResultSet_FilterQuery_Close() throws Exception{

        // Mocks
        FilterQuery<Car, String> filterQuery = mockFilterQuery();
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);

        @SuppressWarnings("unchecked")
        SimpleAttribute<Integer, Car> idToObject = (SimpleAttribute<Integer, Car>)mock(SimpleAttribute.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT objectKey, value FROM " + TABLE_NAME + " ORDER BY objectKey;")).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(statement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        when(resultSet.getString(2)).thenReturn("abs").thenReturn("gps").thenReturn("airbags").thenReturn("abs").thenReturn("").thenReturn("gps");
        when(idToObject.getValue(eq(1), anyQueryOptions())).thenReturn(data.get(0));
        when(idToObject.getValue(eq(3), anyQueryOptions())).thenReturn(data.get(2));
        when(idToObject.getValue(eq(5), anyQueryOptions())).thenReturn(data.get(4));

        // Iterator
        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                idToObject,
                "")

                .retrieve(filterQuery, createQueryOptions(connectionManager));

        assertNotNull(carsWithAbs);
        Iterator carsWithAbsIterator = carsWithAbs.iterator();
        assertTrue(carsWithAbsIterator.hasNext());
        assertNotNull(carsWithAbsIterator.next());
        // Do not continue with the iteration, but close
        carsWithAbs.close();

        verify(connection, times(0)).close(); // Connection should be left open
        verify(statement, times(1)).close();
        verify(resultSet, times(1)).close();

    }

    @Test
    public void testNewResultSet_FilterQuery_GetMergeCost() throws Exception{

        // Mocks
        FilterQuery<Car, String> filterQuery = mockFilterQuery();
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);

        // Behaviour
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(objectKey) FROM " + TABLE_NAME + " ;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        // Iterator
        ResultSet<Car> cars = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                "")

                .retrieve(filterQuery, createQueryOptions(connectionManager));

        assertNotNull(cars);
        int mergeCost = cars.getMergeCost();

        assertEquals(3, mergeCost);
        verify(connection, times(0)).close();
    }

    @Test
    public void testNewResultSet_FilterQuery_GetRetrievalCost(){

        // Mocks
        FilterQuery<Car, String> filterQuery = mockFilterQuery();
        ConnectionManager connectionManager = mock(ConnectionManager.class);

        // Iterator
        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                "")

                .retrieve(filterQuery, createQueryOptions(connectionManager));

        assertEquals(carsWithAbs.getRetrievalCost(), SQLiteIndex.INDEX_RETRIEVAL_COST_FILTERING);

    }

    @Test
    public void testNewResultSet_FilterQuery_Contains() throws Exception{

        // Mocks
        FilterQuery<Car, String> filterQuery = mockFilterQuery();
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connectionContains = mock(Connection.class);
        Connection connectionDoNotContain = mock(Connection.class);
        Connection connectionNoRows = mock(Connection.class);
        PreparedStatement preparedStatementContains = mock(PreparedStatement.class);
        PreparedStatement preparedStatementDoNotContains = mock(PreparedStatement.class);
        PreparedStatement preparedStatementNoRows = mock(PreparedStatement.class);
        java.sql.ResultSet resultSetContains = mock(java.sql.ResultSet.class);
        java.sql.ResultSet resultSetDoNotContain = mock(java.sql.ResultSet.class);
        java.sql.ResultSet resultSetNoRows = mock(java.sql.ResultSet.class);

        // Behaviour
        //SELECT objectKey, value FROM cqtbl_%s WHERE objectKey=?
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connectionContains).thenReturn(connectionDoNotContain).thenReturn(connectionNoRows);
        when(connectionContains.prepareStatement("SELECT objectKey, value FROM " + TABLE_NAME + " WHERE objectKey = ?")).thenReturn(preparedStatementContains);
        when(connectionDoNotContain.prepareStatement("SELECT objectKey, value FROM " + TABLE_NAME + " WHERE objectKey = ?")).thenReturn(preparedStatementDoNotContains);
        when(connectionNoRows.prepareStatement("SELECT objectKey, value FROM " + TABLE_NAME + " WHERE objectKey = ?")).thenReturn(preparedStatementNoRows);
        when(preparedStatementContains.executeQuery()).thenReturn(resultSetContains);
        when(preparedStatementDoNotContains.executeQuery()).thenReturn(resultSetDoNotContain);
        when(preparedStatementNoRows.executeQuery()).thenReturn(resultSetNoRows);

        when(resultSetContains.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetContains.getInt(1)).thenReturn(1).thenReturn(1);
        when(resultSetContains.getString(2)).thenReturn("abs").thenReturn("gps");

        when(resultSetDoNotContain.next()).thenReturn(true).thenReturn(false);
        when(resultSetDoNotContain.getInt(1)).thenReturn(2);
        when(resultSetDoNotContain.getString(2)).thenReturn("airbags");

        when(resultSetNoRows.next()).thenReturn(false);

        // Iterator
        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                "")

                .retrieve(filterQuery, createQueryOptions(connectionManager));

        assertNotNull(carsWithAbs);
        boolean resultContains = carsWithAbs.contains(data.get(0));
        assertTrue(resultContains);
        verify(connectionContains, times(0)).close();

        boolean resultDoNotContain = carsWithAbs.contains(data.get(1));
        assertFalse(resultDoNotContain);
        verify(connectionDoNotContain, times(0)).close();

        boolean resultNoRows = carsWithAbs.contains(CarFactory.createCar(100));
        assertFalse(resultNoRows);
        verify(connectionNoRows, times(0)).close();

    }

    @Test
    public void testNewResultSet_FilterQuery_Size() throws Exception{

        // Mocks
        FilterQuery<Car, String> filterQuery = mockFilterQuery();
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);

        // Behaviour//
        when(connectionManager.getConnection(any(SQLiteIndex.class), anyQueryOptions())).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT objectKey, value FROM " + TABLE_NAME + " ORDER BY objectKey;")).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(statement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        when(resultSet.getString(2)).thenReturn("abs").thenReturn("gps").thenReturn("airbags").thenReturn("abs").thenReturn("").thenReturn("gps");

        ResultSet<Car> carsWithAbs = new SQLiteIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                "")

                .retrieve(filterQuery, createQueryOptions(connectionManager));


        assertNotNull(carsWithAbs);
        int size = carsWithAbs.size();

        assertEquals(3, size);
        verify(connection, times(0)).close();

    }

    static FilterQuery<Car, String> mockFilterQuery(){
        @SuppressWarnings("unchecked")
        FilterQuery<Car, String> filterQuery = (FilterQuery<Car, String>)mock(FilterQuery.class);
        when(filterQuery.matchesValue(Mockito.anyString(), any(QueryOptions.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                if (args != null && args.length == 2 && args[0] instanceof String) {
                    String value = (String) args[0];
                    return "abs".equals(value) || "gps".equals(value);
                }
                throw new IllegalStateException("matchesValue invocation not expected. Args " + Arrays.toString(args));
            }
        });
        return filterQuery;
    }

    static QueryOptions createQueryOptions(ConnectionManager connectionManager) {
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.put(ConnectionManager.class, connectionManager);
        return queryOptions;
    }

    static ObjectStore<Car> emptyObjectStore() {
        return new ConcurrentOnHeapObjectStore<Car>();
    }

    static ObjectStore<Car> wrappingObjectStore(Collection<Car> objects) {
        ConcurrentOnHeapObjectStore<Car> objectStore = new ConcurrentOnHeapObjectStore<Car>();
        objectStore.addAll(objects, noQueryOptions());
        return objectStore;
    }

    static QueryOptions anyQueryOptions() {
        return Mockito.any();
    }

    static ObjectSet<Car> createObjectSetOfCars(int numCars) {
        return ObjectSet.fromCollection(CarFactory.createCollectionOfCars(numCars));
    }

    static ObjectSet<Car> objectSet(Collection<Car> collection) {
        return ObjectSet.fromCollection(collection);
    }

}