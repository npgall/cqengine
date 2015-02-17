package com.googlecode.cqengine.index.offheap;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.offheap.support.ConnectionManager;
import com.googlecode.cqengine.index.offheap.support.DBQueries;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OffHeapIndex}
 *
 * @author Silvano Riz
 */
public class OffHeapIndexTest {

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

    @Test
    public void testNewStandalone() throws Exception {

        OffHeapIndex<String, Car, Integer> carFeaturesOffHeapIndex = OffHeapIndex.onAttribute(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                mock(ConnectionManager.class)
        );

        assertNotNull(carFeaturesOffHeapIndex);
    }

    @Test
    public void testNewNonStandalone() throws Exception {

        OffHeapIndex<String, Car, Integer> carFeaturesOffHeapIndex = OffHeapIndex.onAttribute(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT
        );

        assertNotNull(carFeaturesOffHeapIndex);
    }

    @Test
    public void testGetConnectionManager_Standalone(){

        ConnectionManager connectionManager = mock(ConnectionManager.class);

        OffHeapIndex<String, Car, Integer> carFeaturesOffHeapIndex = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager
        );

        Assert.assertEquals(connectionManager, carFeaturesOffHeapIndex.getConnectionManager(new QueryOptions()));
    }

    @Test
    public void testGetConnectionManager_NonStandalone(){

        ConnectionManager connectionManager = mock(ConnectionManager.class);
        QueryOptions queryOptions = mock(QueryOptions.class);
        when(queryOptions.get(ConnectionManager.class)).thenReturn(connectionManager);

        OffHeapIndex<String, Car, Integer> carFeaturesOffHeapIndex = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                null
        );

        Assert.assertEquals(connectionManager, carFeaturesOffHeapIndex.getConnectionManager(queryOptions));
    }

    @Test
    public void testNotifyObjectsRemoved() throws Exception{

        // Mock
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Connection connection1 = mock(Connection.class);
        Statement statement = mock(Statement.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // Behaviour
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connection).thenReturn(connection1);
        when(connectionManager.isApplyUpdateForIndexEnabled(any(OffHeapIndex.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement);
        when(connection1.prepareStatement("DELETE FROM features WHERE objectKey = ?;")).thenReturn(preparedStatement);

        // The objects to add
        Set<Car> removedObjects = new HashSet<Car>(2);
        removedObjects.add(new Car(1, "Ford", "Focus", Car.Color.BLUE, 5, 9000.50, Arrays.asList("abs", "gps")));
        removedObjects.add(new Car(2, "Honda", "Civic", Car.Color.RED, 5, 5000.00, Arrays.asList("airbags")));

        @SuppressWarnings({"unchecked", "unused"})
        OffHeapIndex<String, Car, Integer> carFeaturesOffHeapIndex = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager
        );

        carFeaturesOffHeapIndex.notifyObjectsRemoved(removedObjects, new QueryOptions());

        // Verify
        verify(statement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS features (objectKey INTEGER, value TEXT, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;");
        verify(statement, times(1)).executeUpdate("CREATE INDEX IF NOT EXISTS idx_features_value ON features (value);");
        verify(connection, times(1)).close();

        verify(preparedStatement, times(1)).setObject(1, 1);
        verify(preparedStatement, times(1)).setObject(1, 2);
        verify(preparedStatement, times(2)).addBatch();
        verify(preparedStatement, times(1)).executeBatch();
        verify(connection1, times(1)).close();
    }

    @Test
    public void testNotifyObjectsAdded() throws Exception {

        // Mock
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Connection connection1 = mock(Connection.class);
        Statement statement = mock(Statement.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // Behaviour
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connection).thenReturn(connection1);
        when(connectionManager.isApplyUpdateForIndexEnabled(any(OffHeapIndex.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement);
        when(connection1.prepareStatement("INSERT OR REPLACE INTO features values(?, ?);")).thenReturn(preparedStatement);

        // The objects to add
        Set<Car> addedObjects = new HashSet<Car>(2);
        addedObjects.add(new Car(1, "Ford", "Focus", Car.Color.BLUE, 5, 9000.50, Arrays.asList("abs", "gps")));
        addedObjects.add(new Car(2, "Honda", "Civic", Car.Color.RED, 5, 5000.00, Arrays.asList("airbags")));

        // Create the index and cal the notifyObjectsAdded
        OffHeapIndex<String, Car, Integer> carFeaturesOffHeapIndex = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager
        );
        carFeaturesOffHeapIndex.notifyObjectsAdded(addedObjects, new QueryOptions());

        // Verify
        verify(statement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS features (objectKey INTEGER, value TEXT, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;");
        verify(statement, times(1)).executeUpdate("CREATE INDEX IF NOT EXISTS idx_features_value ON features (value);");
        verify(connection, times(1)).close();

        verify(preparedStatement, times(2)).setObject(1, 1);
        verify(preparedStatement, times(1)).setObject(1, 2);
        verify(preparedStatement, times(1)).setObject(2, "abs");
        verify(preparedStatement, times(1)).setObject(2, "gps");
        verify(preparedStatement, times(1)).setObject(2, "airbags");
        verify(preparedStatement, times(3)).addBatch();
        verify(preparedStatement, times(1)).executeBatch();
        verify(connection1, times(1)).close();
    }

    @Test
    public void testNotifyObjectsCleared() throws Exception{

        // Mock
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Connection connection1 = mock(Connection.class);
        Statement statement = mock(Statement.class);
        Statement statement1 = mock(Statement.class);

        // Behaviour
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connection).thenReturn(connection1);
        when(connectionManager.isApplyUpdateForIndexEnabled(any(OffHeapIndex.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement);
        when(connection1.createStatement()).thenReturn(statement1);

        @SuppressWarnings({"unchecked", "unused"})
        OffHeapIndex<String, Car, Integer> carFeaturesOffHeapIndex = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager
        );

        carFeaturesOffHeapIndex.notifyObjectsCleared(new QueryOptions());

        // Verify
        verify(statement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS features (objectKey INTEGER, value TEXT, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;");
        verify(statement, times(1)).executeUpdate("CREATE INDEX IF NOT EXISTS idx_features_value ON features (value);");
        verify(connection, times(1)).close();

        verify(statement1, times(1)).executeUpdate("DELETE FROM features;");
        verify(connection1, times(1)).close();
    }

    @Test
    public void testInit_EmptyCollection() throws Exception{

        // Mock
        ConnectionManager connectionManager = mock(ConnectionManager.class);

        OffHeapIndex<String, Car, Integer> carFeaturesOffHeapIndex = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager
        );

        carFeaturesOffHeapIndex.init(Collections.<Car>emptySet(), new QueryOptions());
        verify(connectionManager, times(0)).getConnection(any(OffHeapIndex.class));
    }

    @Test
    public void testInit_NonEmptyCollection() throws Exception{

        // Mock
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        Connection connection1 = mock(Connection.class);
        Statement statement = mock(Statement.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection1.prepareStatement("INSERT OR REPLACE INTO features values(?, ?);")).thenReturn(preparedStatement);
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connection).thenReturn(connection1);
        when(connectionManager.isApplyUpdateForIndexEnabled(any(OffHeapIndex.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement);

        // The objects to add
        Set<Car> initWithObjects = new HashSet<Car>(2);
        initWithObjects.add(new Car(1, "Ford", "Focus", Car.Color.BLUE, 5, 9000.50, Arrays.asList("abs", "gps")));
        initWithObjects.add(new Car(2, "Honda", "Civic", Car.Color.RED, 5, 5000.00, Arrays.asList("airbags")));

        OffHeapIndex<String, Car, Integer> carFeaturesOffHeapIndex = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager
        );

        carFeaturesOffHeapIndex.init(initWithObjects, new QueryOptions());

        // Verify
        verify(statement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS features (objectKey INTEGER, value TEXT, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;");
        verify(statement, times(1)).executeUpdate("CREATE INDEX IF NOT EXISTS idx_features_value ON features (value);");
        verify(statement, times(1)).close();
        verify(connection, times(1)).close();

        verify(preparedStatement, times(2)).setObject(1, 1);
        verify(preparedStatement, times(1)).setObject(1, 2);
        verify(preparedStatement, times(1)).setObject(2, "abs");
        verify(preparedStatement, times(1)).setObject(2, "gps");
        verify(preparedStatement, times(1)).setObject(2, "airbags");
        verify(preparedStatement, times(3)).addBatch();
        verify(preparedStatement, times(1)).executeBatch();
        verify(preparedStatement, times(1)).close();
        verify(connection1, times(1)).close();
    }

    @Test
    public void testNewResultSet_Size() throws Exception{

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);

        // Behaviour
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(objectKey) FROM features WHERE value = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        ResultSet<Car> carsWithAbs = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager)

                .retrieve(equal(Car.FEATURES, "abs"), new QueryOptions());


        Assert.assertNotNull(carsWithAbs);
        int size = carsWithAbs.size();

        Assert.assertEquals(3, size);
        verify(connection, times(1)).close();

    }

    @Test
    public void testNewResultSet_GetRetrievalCost() throws Exception{

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);

        // Iterator
        ResultSet<Car> carsWithAbs = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager)

                .retrieve(equal(Car.FEATURES, "abs"), new QueryOptions());

        Assert.assertEquals(OffHeapIndex.INDEX_RETRIEVAL_COST, carsWithAbs.getRetrievalCost());

    }

    @Test
    public void testNewResultSet_GetMergeCost() throws Exception{

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);

        // Behaviour
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connection);
        when(connection.prepareStatement("SELECT COUNT(objectKey) FROM features WHERE value = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        // Iterator
        ResultSet<Car> carsWithAbs = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager)

                .retrieve(equal(Car.FEATURES, "abs"), new QueryOptions());

        Assert.assertNotNull(carsWithAbs);
        int size = carsWithAbs.getMergeCost();

        Assert.assertEquals(3, size);
        verify(connection, times(1)).close();

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
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connectionContains).thenReturn(connectionDoNotContain);
        when(connectionContains.prepareStatement("SELECT COUNT(objectKey) FROM features WHERE value = ? AND objectKey = ?;")).thenReturn(preparedStatementContains);
        when(connectionDoNotContain.prepareStatement("SELECT COUNT(objectKey) FROM features WHERE value = ? AND objectKey = ?;")).thenReturn(preparedStatementDoNotContains);
        when(preparedStatementContains.executeQuery()).thenReturn(resultSetContains);
        when(preparedStatementDoNotContains.executeQuery()).thenReturn(resultSetDoNotContain);
        when(resultSetContains.next()).thenReturn(true).thenReturn(false);
        when(resultSetContains.getInt(1)).thenReturn(1);
        when(resultSetDoNotContain.next()).thenReturn(true).thenReturn(false);
        when(resultSetDoNotContain.getInt(1)).thenReturn(0);

        // Iterator
        ResultSet<Car> carsWithAbs = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                ID_TO_OBJECT,
                connectionManager)

                .retrieve(equal(Car.FEATURES, "abs"), new QueryOptions());

        Assert.assertNotNull(carsWithAbs);
        boolean resultContains = carsWithAbs.contains(data.get(0));
        Assert.assertTrue(resultContains);
        verify(connectionContains, times(1)).close();

        boolean resultDoNotContain = carsWithAbs.contains(data.get(1));
        Assert.assertFalse(resultDoNotContain);
        verify(connectionDoNotContain, times(1)).close();


    }

    @Test(expected = IllegalStateException.class)
    public void testNewResultSet_Iterator_Exception_Close() throws Exception{

        QueryOptions queryOptions = new QueryOptions();

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);
        @SuppressWarnings("unchecked")
        SimpleAttribute<Integer, Car> idToObject = (SimpleAttribute<Integer, Car>)mock(SimpleAttribute.class);

        // Behaviour
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connection);
        when(connection.prepareStatement("SELECT objectKey, value FROM features WHERE value = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenThrow(new SQLException("SQL exception"));
        when(idToObject.getValue(1, queryOptions)).thenReturn(data.get(0));

        // Iterator
        try {
            ResultSet<Car> carsWithAbs = new OffHeapIndex<String, Car, Integer>(
                    Car.FEATURES,
                    OBJECT_TO_ID,
                    idToObject,
                    connectionManager)

                    .retrieve(equal(Car.FEATURES, "abs"), queryOptions);

            Assert.assertNotNull(carsWithAbs);
            Iterator<Car> carsWithAbsIterator = carsWithAbs.iterator();
            Assert.assertNotNull(carsWithAbsIterator.next());
            carsWithAbsIterator.next();// Should throw exception!

        }finally {
            verify(connection, times(1)).close();
            verify(preparedStatement, times(1)).close();
            verify(resultSet, times(1)).close();
        }

    }

    @Test
    public void testNewResultSet_Iterator_Close() throws Exception{

        QueryOptions queryOptions = new QueryOptions();

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);
        @SuppressWarnings("unchecked")
        SimpleAttribute<Integer, Car> idToObject = (SimpleAttribute<Integer, Car>)mock(SimpleAttribute.class);

        // Behaviour
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connection);
        when(connection.prepareStatement("SELECT objectKey, value FROM features WHERE value = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenReturn(3);
        when(idToObject.getValue(1,queryOptions)).thenReturn(data.get(0));
        when(idToObject.getValue(3,queryOptions)).thenReturn(data.get(2));

        // Iterator
        ResultSet<Car> carsWithAbs = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                idToObject,
                connectionManager)

                .retrieve(equal(Car.FEATURES, "abs"), queryOptions);


        Assert.assertNotNull(carsWithAbs);
        Iterator carsWithAbsIterator = carsWithAbs.iterator();

        Assert.assertTrue(carsWithAbsIterator.hasNext());
        Assert.assertNotNull(carsWithAbsIterator.next());
        Assert.assertTrue(carsWithAbsIterator.hasNext());
        Assert.assertNotNull(carsWithAbsIterator.next());
        Assert.assertFalse(carsWithAbsIterator.hasNext());

        // The end of the iteration should close the resources
        verify(connection, times(1)).close();
        verify(preparedStatement, times(1)).close();
        verify(resultSet, times(1)).close();

    }

    @Test
    public void testNewResultSet_Close() throws Exception{

        QueryOptions queryOptions = new QueryOptions();

        // Mocks
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        java.sql.ResultSet resultSet = mock(java.sql.ResultSet.class);

        @SuppressWarnings("unchecked")
        SimpleAttribute<Integer, Car> idToObject = (SimpleAttribute<Integer, Car>)mock(SimpleAttribute.class);

        // Behaviour
        when(connectionManager.getConnection(any(OffHeapIndex.class))).thenReturn(connection);
        when(connection.prepareStatement("SELECT objectKey, value FROM features WHERE value = ?;")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getStatement()).thenReturn(preparedStatement);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1).thenReturn(3);
        when(idToObject.getValue(1, queryOptions)).thenReturn(data.get(0));
        when(idToObject.getValue(3, queryOptions)).thenReturn(data.get(2));

        // Iterator
        ResultSet<Car> carsWithAbs = new OffHeapIndex<String, Car, Integer>(
                Car.FEATURES,
                OBJECT_TO_ID,
                idToObject,
                connectionManager)

                .retrieve(equal(Car.FEATURES, "abs"), queryOptions);

        Assert.assertNotNull(carsWithAbs);
        Iterator carsWithAbsIterator = carsWithAbs.iterator();
        Assert.assertTrue(carsWithAbsIterator.hasNext());
        Assert.assertNotNull(carsWithAbsIterator.next());
        // Do not continue with the iteration, but close
        carsWithAbs.close();

        verify(connection, times(1)).close();
        verify(preparedStatement, times(1)).close();
        verify(resultSet, times(1)).close();

    }

    @Test
    public void testRowIterable(){

        Iterable<DBQueries.Row<Integer, String>> rows = OffHeapIndex.rowIterable(data, Car.CAR_ID, Car.FEATURES, null);
        Assert.assertNotNull(rows);

        Iterator<DBQueries.Row<Integer, String>> rowsIterator = rows.iterator();
        Assert.assertNotNull(rowsIterator);
        Assert.assertTrue(rowsIterator.hasNext());
        Assert.assertEquals(new DBQueries.Row<Integer, String>(1, "abs"),rowsIterator.next());
        Assert.assertTrue(rowsIterator.hasNext());
        Assert.assertEquals(new DBQueries.Row<Integer, String>(1, "gps"),rowsIterator.next());
        Assert.assertTrue(rowsIterator.hasNext());
        Assert.assertEquals(new DBQueries.Row<Integer, String>(2, "airbags"),rowsIterator.next());
        Assert.assertTrue(rowsIterator.hasNext());
        Assert.assertEquals(new DBQueries.Row<Integer, String>(3, "abs"),rowsIterator.next());
        Assert.assertFalse(rowsIterator.hasNext());
    }



    @Test
    public void testObjectKeyItarable(){

        Iterable<Integer> objectKeys = OffHeapIndex.objectKeyIterable(data, Car.CAR_ID, null);
        Assert.assertNotNull(objectKeys);

        Iterator<Integer> objectKeysIterator = objectKeys.iterator();
        Assert.assertNotNull(objectKeysIterator);
        Assert.assertTrue(objectKeysIterator.hasNext());
        Assert.assertEquals(new Integer(1), objectKeysIterator.next());
        Assert.assertTrue(objectKeysIterator.hasNext());
        Assert.assertEquals(new Integer(2),objectKeysIterator.next());
        Assert.assertTrue(objectKeysIterator.hasNext());
        Assert.assertEquals(new Integer(3),objectKeysIterator.next());
        Assert.assertTrue(objectKeysIterator.hasNext());
        Assert.assertEquals(new Integer(4),objectKeysIterator.next());
        Assert.assertTrue(objectKeysIterator.hasNext());
        Assert.assertEquals(new Integer(5),objectKeysIterator.next());
        Assert.assertFalse(objectKeysIterator.hasNext());
    }

}