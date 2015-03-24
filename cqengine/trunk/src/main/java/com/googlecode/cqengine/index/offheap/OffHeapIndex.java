package com.googlecode.cqengine.index.offheap;

import com.googlecode.concurrenttrees.common.LazyIterator;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.common.AbstractAttributeIndex;
import com.googlecode.cqengine.index.common.CloseableQueryResources;
import com.googlecode.cqengine.index.common.ResourceIndex;
import com.googlecode.cqengine.index.common.SortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.offheap.support.*;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.*;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.googlecode.cqengine.index.offheap.support.DBQueries.Row;
import static com.googlecode.cqengine.query.QueryFactory.*;


/**
 * <p>
 *      {@link com.googlecode.cqengine.index.common.AbstractAttributeIndex} storing the index off-heap in a database
 *      (by default a SQLite database).
 * </p>
 *      The database schema is:
 * <pre>
 * CREATE TABLE ${table_name} (
 *      objectKey ${objectKey_type},
 *      value ${value_type},
 *      PRIMARY KEY (objectKey, value)
 * ) WITHOUT ROWID;
 *
 * CREATE INDEX idx_${table}_value ON ${table} (value);
 * </pre>
 * Where:<br>
 * <ul>
 * <li>
 *     <i>${table_name}</i> is the name of the table.
 *     The name of the Attribute (on which the index will be built) is used to name the table. Anyway, the "tbl_" string
 *     will be prefixed and all the non alpha-numeric chars will be stripped out.
 * </li>
 * <li>
 *     <i>${objectKey_type}</i> is the type of the object key defined at construction time
 *     and converted to the correspondent database type.
 * </li>
 * <li>
 *     <i>${value_type}</i> is the type of the indexed value, i.e. the value's type of the Attribute on which the index is built
 *     converted to the correspondent database type.
 * </li>
 * </ul>
 * @author Silvano Riz
 */
public class OffHeapIndex<A extends Comparable<A>, O, K> extends AbstractAttributeIndex<A, O> implements SortedKeyStatisticsAttributeIndex<A, O>, ResourceIndex {

    static final int INDEX_RETRIEVAL_COST = 60;

    final String tableName;
    final SimpleAttribute<O, K> objectToIdAttribute;
    final SimpleAttribute<K, O> idToObjectAttribute;
    final ConnectionManager connectionManager;

    // ---------- Static factory methods to create HashIndexes ----------

    /**
     * Creates a new {@link OffHeapIndex} where the {@link ConnectionManager} will always be supplied via {@link QueryOptions}
     * by the caller.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param objectKeyAttribute The {@link SimpleAttribute} used to retrieve the object key.
     * @param idToObjectAttribute The {@link SimpleAttribute} to map a query result into the domain object.
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @param <K> The type of the object key.
     * @return a new instance of the {@link OffHeapIndex}
     */
    public static <A extends Comparable<A>, O, K> OffHeapIndex<A, O, K> onAttribute(final Attribute<O, A> attribute,
                                                           final SimpleAttribute<O, K> objectKeyAttribute,
                                                           final SimpleAttribute<K, O> idToObjectAttribute) {

        return new OffHeapIndex<A,O, K>(attribute, objectKeyAttribute, idToObjectAttribute, null);
    }

    /**
     * Creates a new {@link OffHeapIndex} where the {@link ConnectionManager} is set at construction time.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param objectKeyAttribute The {@link SimpleAttribute} used to retrieve the object key.
     * @param idToObjectAttribute The {@link SimpleAttribute} to map a query result into the domain object.
     * @param connectionManager The {@link ConnectionManager}
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @param <K> The type of the object key.
     * @return a new instance of a standalone {@link OffHeapIndex}
     */
    public static <A extends Comparable<A>, O, K> OffHeapIndex<A, O, K> onAttribute(final Attribute<O, A> attribute,
                                                           final SimpleAttribute<O, K> objectKeyAttribute,
                                                           final SimpleAttribute<K, O> idToObjectAttribute,
                                                           final ConnectionManager connectionManager) {

        return new OffHeapIndex<A, O, K>(attribute, objectKeyAttribute, idToObjectAttribute, connectionManager);
    }

    /**
     * Package-private constructor. The index should be created via the static factory methods instead.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param objectToIdAttribute The {@link SimpleAttribute} with which the index will retrieve the object key.
     * @param idToObjectAttribute The {@link SimpleAttribute} to map a query result into the domain object.
     * @param connectionManager The {@link ConnectionManager} or null if it will be provided via QueryOptions.
     */
    OffHeapIndex(final Attribute<O, A> attribute,
                 final SimpleAttribute<O, K> objectToIdAttribute,
                 final SimpleAttribute<K, O> idToObjectAttribute,
                 final ConnectionManager connectionManager) {

        super(attribute, new HashSet<Class<? extends Query>>() {{
            add(Equal.class);
            add(LessThan.class);
            add(GreaterThan.class);
            add(Between.class);
            add(StringStartsWith.class);
            add(All.class);
        }});

        this.tableName = attribute.getAttributeName().replaceAll("[^A-Za-z0-9\\s]", "");
        this.objectToIdAttribute = objectToIdAttribute;
        this.idToObjectAttribute = idToObjectAttribute;
        this.connectionManager = connectionManager;

    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public ResultSet<O> retrieve(final Query<O> query, final QueryOptions queryOptions) {
        final ConnectionManager connectionManager = getConnectionManager(queryOptions);

        final CloseableQueryResources closeableQueryResources = CloseableQueryResources.from(queryOptions);
        final CloseableSet resultSetResourcesToClose = new CloseableSet();
        closeableQueryResources.add(resultSetResourcesToClose);

        return new ResultSet<O>(){

            @Override
            public Iterator<O> iterator() {

                final Connection searchConnection = connectionManager.getConnection(OffHeapIndex.this);
                resultSetResourcesToClose.add(DBUtils.wrapConnectionInCloseable(searchConnection));

                final java.sql.ResultSet searchResultSet = DBQueries.search(query, tableName, searchConnection);
                resultSetResourcesToClose.add(DBUtils.wrapResultSetInCloseable(searchResultSet));

                return new LazyIterator<O>() {
                    @Override
                    protected O computeNext() {
                        try {
                            if (!searchResultSet.next()) {
                                close();
                                return endOfData();
                            }
                            final K objectKey = DBUtils.getValueFromResultSet(1, searchResultSet, objectToIdAttribute.getAttributeType());
                            return idToObjectAttribute.getValue(objectKey, queryOptions);
                        }catch (Exception e){
                            endOfData();
                            close();
                            throw new IllegalStateException("Unable to retrieve the ResultSet item.", e);
                        }
                    }
                };
            }

            @Override
            public int getRetrievalCost() {
                return INDEX_RETRIEVAL_COST;
            }

            @Override
            public int getMergeCost() {
                return size();
            }

            @Override
            public boolean contains(O object) {
                final K objectKey = objectToIdAttribute.getValue(object, queryOptions);
                final Connection connection = connectionManager.getConnection(OffHeapIndex.this);
                try{
                    return DBQueries.contains(objectKey, query, tableName, connection);
                }finally {
                    DBUtils.closeQuietly(connection);
                }
            }

            @Override
            public int size() {
                final Connection connection = connectionManager.getConnection(OffHeapIndex.this);
                try {
                    return DBQueries.count(query, tableName, connection);
                } finally {
                    DBUtils.closeQuietly(connection);
                }
            }

            @Override
            public void close() {
                closeableQueryResources.closeAndRemove(resultSetResourcesToClose);
            }
        };

    }

    @Override
    public boolean addAll(final Collection<O> objects, final QueryOptions queryOptions) {

        ConnectionManager connectionManager = getConnectionManager(queryOptions);
        if (!connectionManager.isApplyUpdateForIndexEnabled(this)) {
            return false;
        }
        createTableIndexIfNeeded(connectionManager);

        Connection connection = null;
        try {
            connection = connectionManager.getConnection(this);
            Iterable<Row<K, A>> rows = rowIterable(objects, objectToIdAttribute, getAttribute(), queryOptions);
            int rowsModified = DBQueries.bulkAdd(rows, tableName, connection);
            return rowsModified > 0;
        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    /**
     * Utility method that transforms an {@link Iterable} of domain objects into {@link Row}s composed by object id and
     * respective value.
     *
     * @param objects {@link Iterable} of domain objects.
     * @param objectToIdAttribute {@link SimpleAttribute} used to retrieve the object id.
     * @param indexAttribute {@link Attribute} used to retrieve the value(s).
     * @param queryOptions The {@link QueryOptions}.
     * @return {@link Iterable} of {@link Row}s.
     */
    static <O, K, A> Iterable<Row< K, A>> rowIterable(final Iterable<O> objects,
                                                      final SimpleAttribute<O, K> objectToIdAttribute,
                                                      final Attribute<O, A> indexAttribute,
                                                      final QueryOptions queryOptions){
        return new Iterable<Row<K, A>>() {
            @Override
            public Iterator<Row<K, A>> iterator() {

                return new LazyIterator<Row<K, A>>() {

                    final Iterator<O> objectIterator = objects.iterator();
                    Iterator<A> valuesIterator = null;
                    K currentObjectKey;
                    Row<K, A> next;
                    @Override
                    protected Row<K, A> computeNext() {

                        while(computeNextOrNull()){
                            if (next!=null)
                                return next;
                        }
                        return endOfData();
                    }

                    boolean computeNextOrNull(){
                        if (valuesIterator == null || !valuesIterator.hasNext()){
                            if (objectIterator.hasNext()){
                                O next = objectIterator.next();
                                currentObjectKey = objectToIdAttribute.getValue(next, queryOptions);
                                valuesIterator = indexAttribute.getValues(next, queryOptions).iterator();
                            }else{
                                return false;
                            }
                        }

                        if (valuesIterator.hasNext()){
                            next = new Row<K, A>(currentObjectKey, valuesIterator.next());
                            return true;
                        }else{
                            next = null;
                            return true;
                        }
                    }

                };
            }
        };
    }


    @Override
    public boolean removeAll(final Collection<O> objects, final QueryOptions queryOptions) {
        ConnectionManager connectionManager = getConnectionManager(queryOptions);
        if (!connectionManager.isApplyUpdateForIndexEnabled(this)) {
            return false;
        }
        createTableIndexIfNeeded(connectionManager);

        Connection connection = null;
        try {
            connection = connectionManager.getConnection(this);
            Iterable<K> objectKeys = objectKeyIterable(objects, objectToIdAttribute, queryOptions);

            int rowsModified = DBQueries.bulkRemove(objectKeys, tableName, connection);
            return rowsModified > 0;
        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    /**
     * Utility method that transforms an {@link Iterable} of domain objects into an {@link Iterable} over the objects ids.
     *
     * @param objects {@link Iterable} of domain objects.
     * @param objectToIdAttribute {@link SimpleAttribute} used to retrieve the object id.
     * @param queryOptions The {@link QueryOptions}.
     * @return {@link Iterable} over the objects ids.
     */
    static <O, K> Iterable<K> objectKeyIterable(final Iterable<O> objects,
                                                final SimpleAttribute<O, K> objectToIdAttribute,
                                                final QueryOptions queryOptions){
        return new Iterable<K>() {

            @Override
            public Iterator<K> iterator() {
                return new UnmodifiableIterator<K>() {

                    final Iterator<O> iterator = objects.iterator();

                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public K next() {
                        O next = iterator.next();
                        return objectToIdAttribute.getValue(next, queryOptions);
                    }
                };
            }

        };

    }

    @Override
    public void clear(QueryOptions queryOptions) {

        ConnectionManager connectionManager = getConnectionManager(queryOptions);
        if (!connectionManager.isApplyUpdateForIndexEnabled(this)) {
            return;
        }
        createTableIndexIfNeeded(connectionManager);

        Connection connection = null;
        try {
            connection = connectionManager.getConnection(this);
            DBQueries.clearIndexTable(tableName, connection);
        }finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Override
    public void init(Set<O> collection, QueryOptions queryOptions) {
        addAll(collection, queryOptions);
    }

    /**
     * Utility method to create the index table if needed.
     *
     * @param connectionManager The {@link ConnectionManager}.
     */
    void createTableIndexIfNeeded(final ConnectionManager connectionManager){
        Connection connection = null;
        try {
            connection = connectionManager.getConnection(this);
            DBQueries.createIndexTable(tableName, objectToIdAttribute.getAttributeType(), getAttribute().getAttributeType(), connection);
        } finally {
            DBUtils.closeQuietly(connection);
        }
    }

    /**
     * Utility method to get the {@link ConnectionManager}.<br>
     * It will return the instance variable if initialized at construction time or retrieve it from the QueryOptions otherwise.
     *
     * @param queryOptions The {@link QueryOptions}.
     * @return The {@link ConnectionManager}
     *
     * @throws IllegalStateException if the Connection manager is not found.
     */
    ConnectionManager getConnectionManager(final QueryOptions queryOptions){
        if (connectionManager != null){
            return connectionManager;
        }else{
            ConnectionManager connectionManagerFromQueryOptions = queryOptions.get(ConnectionManager.class);
            if (connectionManagerFromQueryOptions == null)
                throw new IllegalStateException("Cannot find the ConnectionManager in the QueryOptions.");
            return connectionManagerFromQueryOptions;
        }
    }

    @Override
    public CloseableIterable<A> getDistinctKeys(final QueryOptions queryOptions) {
        return getDistinctKeys(null, true, null, true, queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeys(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, final QueryOptions queryOptions) {
        return getDistinctKeysInRange(lowerBound, lowerInclusive, upperBound, upperInclusive, false, queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeysDescending(QueryOptions queryOptions) {
        return getDistinctKeysDescending(null, true, null, true, queryOptions);
    }

    @Override
    public CloseableIterable<A> getDistinctKeysDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return getDistinctKeysInRange(lowerBound, lowerInclusive, upperBound, upperInclusive, true, queryOptions);
    }

    public CloseableIterable<A> getDistinctKeysInRange(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, final boolean descending, final QueryOptions queryOptions) {
        final Query<O> query;
        if (lowerBound != null && upperBound != null) {
            query = between(attribute, lowerBound, lowerInclusive, upperBound, upperInclusive);
        }
        else if (lowerBound != null) {
            query = lowerInclusive ? greaterThanOrEqualTo(attribute, lowerBound) : greaterThan(attribute, lowerBound);
        }
        else if (upperBound != null) {
            query = upperInclusive ? lessThanOrEqualTo(attribute, upperBound) : lessThan(attribute, upperBound);
        }
        else {
            query = all(attribute.getObjectType());
        }

        final CloseableQueryResources closeableQueryResources = CloseableQueryResources.from(queryOptions);
        final CloseableSet resultSetResourcesToClose = new CloseableSet();
        closeableQueryResources.add(resultSetResourcesToClose);

        return new CloseableIterable<A>() {
            @Override
            public CloseableIterator<A> iterator() {
                final ConnectionManager connectionManager = getConnectionManager(queryOptions);
                final Connection searchConnection = connectionManager.getConnection(OffHeapIndex.this);

                resultSetResourcesToClose.add(DBUtils.wrapConnectionInCloseable(searchConnection));


                final java.sql.ResultSet searchResultSet = DBQueries.getDistinctKeys(query, descending, tableName, searchConnection);
                resultSetResourcesToClose.add(DBUtils.wrapResultSetInCloseable(searchResultSet));

                return new LazyCloseableIterator<A>() {
                    @Override
                    protected A computeNext() {
                        try {
                            if (!searchResultSet.next()) {
                                close();
                                return endOfData();
                            }
                            return DBUtils.getValueFromResultSet(1, searchResultSet, attribute.getAttributeType());
                        }
                        catch (Exception e) {
                            endOfData();
                            close();
                            throw new IllegalStateException("Unable to retrieve the ResultSet item.", e);
                        }
                    }

                    @Override
                    public void close() {
                        closeableQueryResources.closeAndRemove(resultSetResourcesToClose);
                    }
                };
            }
        };
    }

    @Override
    public Integer getCountForKey(A key, QueryOptions queryOptions) {
        return retrieve(equal(attribute, key), queryOptions).size();
    }
}

