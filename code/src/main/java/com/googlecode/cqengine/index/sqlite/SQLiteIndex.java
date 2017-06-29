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

import com.googlecode.concurrenttrees.common.LazyIterator;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.disk.DiskIndex;
import com.googlecode.cqengine.index.offheap.OffHeapIndex;
import com.googlecode.cqengine.index.sqlite.support.DBQueries;
import com.googlecode.cqengine.index.sqlite.support.DBUtils;
import com.googlecode.cqengine.index.sqlite.support.SQLiteIndexFlags;
import com.googlecode.cqengine.index.sqlite.support.SQLiteIndexFlags.BulkImportExternallyManged;
import com.googlecode.cqengine.index.support.*;
import com.googlecode.cqengine.index.support.indextype.NonHeapTypeIndex;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.FlagsEnabled;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.*;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;
import com.googlecode.cqengine.index.support.CloseableRequestResources.CloseableResourceGroup;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;

import static com.googlecode.cqengine.index.sqlite.support.DBQueries.Row;
import static com.googlecode.cqengine.index.sqlite.support.DBUtils.sanitizeForTableName;
import static com.googlecode.cqengine.index.sqlite.support.SQLiteIndexFlags.BulkImportExternallyManged.LAST;
import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * An index backed by a table in a SQLite database.
 * <p>
 * This index is highly configurable, and forms the basis for other simpler indexes: {@link SQLiteIdentityIndex},
 * {@link OffHeapIndex} and {@link DiskIndex}.
 * </p>
 * <p>
 * Specifically, this index allows details of the database to which data should be persisted, to be supplied by the
 * application on-the-fly in request-scope via {@link QueryOptions}, as an alternative to configuring a
 * particular database statically.
 * </p>
 * <p>
 * This is useful in applications where CQEngine does not create/destroy or
 * manage the life cycle of the database itself, and so where the application requires tight control over how
 * the database is accessed.
 * </p>
 * <p>
 *     <i>In applications where CQEngine manages the SQLite database, users should probably not use this
 *     index directly, but should use one of the simpler indexes mentioned above instead.</i>
 * </p>
 * <h1>
 *     Index implementation
 * </h1>
 *      This index is persisted in a SQLite table with the following schema:
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
 *     <i>table_name</i> is the name of the table.
 *     The name of the attribute (on which the index will be built) is used to name the table. A "cqtbl_" string
 *     will be prefixed and all the non alpha-numeric chars will be stripped out.
 * </li>
 * <li>
 *     <i>objectKey</i> stores the value from the {@code primaryKeyAttribute} for each object stored
 *     (generic type {@code K}).
 *     If the primary key is configured as {@code Car.CAR_ID} then this column will hold the carId.
 *     The type of the primary key attribute will be converted to the corresponding database type.
 * </li>
 * <li>
 *     <i>value</i> is the type of the indexed value (generic type {@code A}).
 *     If the index is built on an attribute {@code Car.MODEL}, then this will hold the car model string.
 *     The type of the attribute on which the index is built will be converted to the corresponding database
 *     type.
 * </li>
 * <li>
 *     Note that this index supports {@link MultiValueAttribute}, which means there may be more than one value for each
 *     objecyKey.
 * </li>
 * </ul>
 * @author Silvano Riz
 */
public class SQLiteIndex<A extends Comparable<A>, O, K> extends AbstractAttributeIndex<A, O> implements SortedKeyStatisticsAttributeIndex<A, O>, NonHeapTypeIndex {

    static final int INDEX_RETRIEVAL_COST = 80;
    static final int INDEX_RETRIEVAL_COST_FILTERING = INDEX_RETRIEVAL_COST + 1;

    // System property to force preexisting disk/off-heap indexes to be re-initialized (re-synced with the collection)
    // at startup.
    // The recommended and default setting is false. Setting this true forces old behavior of CQEngine <= 2.10.0.
    static final boolean FORCE_REINIT_OF_PREEXISTING_INDEXES = Boolean.getBoolean("cqengine.reinit.preexisting.indexes");

    final String tableName;
    final SimpleAttribute<O, K> primaryKeyAttribute;
    final SimpleAttribute<K, O> foreignKeyAttribute;

    SQLiteConfig.SynchronousMode pragmaSynchronous;
    SQLiteConfig.JournalMode pragmaJournalMode;
    boolean canSuspendSyncAndJournaling;

    /**
     * Constructor. Note the index should normally be created via the static factory methods instead.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param primaryKeyAttribute The {@link SimpleAttribute} with which the index will retrieve the object key.
     * @param foreignKeyAttribute The {@link SimpleAttribute} to map a query result into the domain object.
     * @param tableNameSuffix An optional string to append the end of the table name used by this index;
     *                        This can be an empty string, but cannot be null; If not an empty string, the string
     *                        should only contain characters suitable for use in a SQLite table name; therefore see
     *                        {@link DBUtils#sanitizeForTableName(String)}
     */
    public SQLiteIndex(final Attribute<O, A> attribute,
                       final SimpleAttribute<O, K> primaryKeyAttribute,
                       final SimpleAttribute<K, O> foreignKeyAttribute,
                       final String tableNameSuffix) {

        super(attribute, new HashSet<Class<? extends Query>>() {{
            add(Equal.class);
            add(In.class);
            add(LessThan.class);
            add(GreaterThan.class);
            add(Between.class);
            add(StringStartsWith.class);
            add(Has.class);
        }});

        this.tableName = sanitizeForTableName(attribute.getAttributeName()) + tableNameSuffix;
        this.primaryKeyAttribute = primaryKeyAttribute;
        this.foreignKeyAttribute = foreignKeyAttribute;
    }

    @Override
    public boolean supportsQuery(Query<O> query, QueryOptions queryOptions) {
        return query instanceof FilterQuery || super.supportsQuery(query, queryOptions);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean isQuantized() {
        return false;
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    @Override
    public ResultSet<O> retrieve(final Query<O> query, final QueryOptions queryOptions) {
        final ConnectionManager connectionManager = getConnectionManager(queryOptions);

        final CloseableResourceGroup closeableResourceGroup = CloseableRequestResources.forQueryOptions(queryOptions).addGroup();

        if (query instanceof FilterQuery){
            @SuppressWarnings("unchecked")
            final FilterQuery<O, A> filterQuery =  (FilterQuery<O, A>)query;
            return new ResultSet<O>() {
                @Override
                public Iterator<O> iterator() {
                    final Connection searchConnection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);

                    final java.sql.ResultSet searchResultSet = DBQueries.getAllIndexEntries(tableName, searchConnection);
                    closeableResourceGroup.add(DBUtils.wrapAsCloseable(searchResultSet));

                    return new LazyIterator<O>() {
                        @Override
                        protected O computeNext() {
                            try {
                                while (true) {

                                    if (!searchResultSet.next()) {
                                        close();
                                        return endOfData();
                                    }

                                    final K objectKey = DBUtils.getValueFromResultSet(1, searchResultSet, primaryKeyAttribute.getAttributeType());
                                    final A objectValue = DBUtils.getValueFromResultSet(2, searchResultSet, attribute.getAttributeType());
                                    if (filterQuery.matchesValue(objectValue, queryOptions)) {
                                        return foreignKeyAttribute.getValue(objectKey, queryOptions);
                                    }
                                }

                            } catch (Exception e) {
                                endOfData();
                                close();
                                throw new IllegalStateException("Unable to retrieve the ResultSet item.", e);
                            }
                        }
                    };

                }

                @Override
                public boolean contains(O object) {

                    Connection connection = null;
                    java.sql.ResultSet searchResultSet = null;
                    try {
                        connection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);
                        searchResultSet = DBQueries.getIndexEntryByObjectKey(primaryKeyAttribute.getValue(object, queryOptions), tableName, connection);

                        return lazyMatchingValuesIterable(searchResultSet).iterator().hasNext();

                    }finally {
                        DBUtils.closeQuietly(searchResultSet);
                    }

                }

                @Override
                public boolean matches(O object) {
                    return query.matches(object, queryOptions);
                }

                @Override
                public Query<O> getQuery() {
                    return query;
                }

                @Override
                public QueryOptions getQueryOptions() {
                    return queryOptions;
                }

                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST_FILTERING;// choose between indexes
                }

                @Override
                public int getMergeCost() {
                    //choose between branches.
                    final Connection connection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);
                    return DBQueries.count(has(primaryKeyAttribute), tableName, connection); // no need to eliminate duplicates
                }

                @Override
                public int size() {
                    Connection connection = null;
                    java.sql.ResultSet searchResultSet = null;
                    try {
                        connection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);
                        searchResultSet = DBQueries.getAllIndexEntries(tableName, connection);

                        final Iterable<K> iterator = lazyMatchingValuesIterable(searchResultSet);
                        return IteratorUtil.countElements(iterator);
                    }finally {
                        DBUtils.closeQuietly(searchResultSet);
                    }
                }

                @Override
                public void close() {
                    closeableResourceGroup.close();
                }

                // Method to retrieve all the distinct keys for the matching values from the index. Used in count and contains
                Iterable<K> lazyMatchingValuesIterable(final java.sql.ResultSet searchResultSet) {
                    return new Iterable<K>() {
                        @Override
                        public Iterator<K> iterator() {
                            return new LazyIterator<K>() {

                                K currentKey = null;

                                @Override
                                protected K computeNext() {
                                    try {
                                        while (true) {

                                            if (!searchResultSet.next()) {
                                                close();
                                                return endOfData();
                                            }

                                            final K objectKey = DBUtils.getValueFromResultSet(1, searchResultSet, primaryKeyAttribute.getAttributeType());
                                            if (currentKey == null || !currentKey.equals(objectKey)) {
                                                final A objectValue = DBUtils.getValueFromResultSet(2, searchResultSet, attribute.getAttributeType());
                                                if (filterQuery.matchesValue(objectValue, queryOptions)) {
                                                    currentKey = objectKey;
                                                    return objectKey;
                                                }
                                            }
                                        }

                                    } catch (Exception e) {
                                        endOfData();
                                        close();
                                        throw new IllegalStateException("Unable to retrieve the ResultSet item.", e);
                                    }
                                }
                            };
                        }
                    };
                }

            };
        }else {

            return new ResultSet<O>() {

                @Override
                public Iterator<O> iterator() {

                    final Connection searchConnection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);

                    final java.sql.ResultSet searchResultSet = DBQueries.search(query, tableName, searchConnection); // eliminates duplicates
                    closeableResourceGroup.add(DBUtils.wrapAsCloseable(searchResultSet));

                    return new LazyIterator<O>() {
                        @Override
                        protected O computeNext() {
                            try {
                                if (!searchResultSet.next()) {
                                    close();
                                    return endOfData();
                                }
                                final K objectKey = DBUtils.getValueFromResultSet(1, searchResultSet, primaryKeyAttribute.getAttributeType());
                                return foreignKeyAttribute.getValue(objectKey, queryOptions);
                            } catch (Exception e) {
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
                    final Connection connection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);
                    return DBQueries.count(query, tableName, connection); // no need to eliminate duplicates
                }

                @Override
                public boolean contains(O object) {
                    final K objectKey = primaryKeyAttribute.getValue(object, queryOptions);
                    final Connection connection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);
                    return DBQueries.contains(objectKey, query, tableName, connection);
                }

                @Override
                public boolean matches(O object) {
                    return query.matches(object, queryOptions);
                }

                @Override
                public int size() {
                    final Connection connection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);

                    boolean attributeHasAtMostOneValue = (attribute instanceof SimpleAttribute || attribute instanceof SimpleNullableAttribute);
                    boolean queryIsADisjointInQuery = query instanceof In && ((In) query).isDisjoint();

                    if (queryIsADisjointInQuery || attributeHasAtMostOneValue) {
                        return DBQueries.count(query, tableName, connection); // No need to eliminates duplicates
                    }else{
                        return DBQueries.countDistinct(query, tableName, connection); // eliminates duplicates
                    }
                }

                @Override
                public void close() {
                    closeableResourceGroup.close();
                }

                @Override
                public Query<O> getQuery() {
                    return query;
                }

                @Override
                public QueryOptions getQueryOptions() {
                    return queryOptions;
                }
            };
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Note objects can be imported into this index rapidly via this method,
     * by setting flag {@link SQLiteIndexFlags#BULK_IMPORT}. See documentation on that flag for details and caveats.
     */
    @Override
    public boolean addAll(final ObjectSet<O> objectSet, final QueryOptions queryOptions) {
        return doAddAll(objectSet, queryOptions, false);
    }

    boolean doAddAll(final ObjectSet<O> objectSet, final QueryOptions queryOptions, boolean isInit) {
        try {
            ConnectionManager connectionManager = getConnectionManager(queryOptions);
            if (!connectionManager.isApplyUpdateForIndexEnabled(this)) {
                return false;
            }

            final Connection connection = connectionManager.getConnection(this, queryOptions);

            if (!FORCE_REINIT_OF_PREEXISTING_INDEXES) {
                if (isInit && DBQueries.indexTableExists(tableName, connection)) {
                    // init() was called, but index table already exists. Skip initializing it...
                    return false;
                }
            }

            // Create table if it doesn't exists...
            DBQueries.createIndexTable(tableName, primaryKeyAttribute.getAttributeType(), getAttribute().getAttributeType(), connection);

            // Handle the SQLite indexes on the table
            final BulkImportExternallyManged bulkImportExternallyManged = queryOptions.get(BulkImportExternallyManged.class);
            final boolean isBulkImport = bulkImportExternallyManged == null && FlagsEnabled.isFlagEnabled(queryOptions, SQLiteIndexFlags.BULK_IMPORT);
            final boolean isSuspendSyncAndJournaling = FlagsEnabled.isFlagEnabled(queryOptions, SQLiteIndexFlags.BULK_IMPORT_SUSPEND_SYNC_AND_JOURNALING);
            if ((bulkImportExternallyManged != null || isBulkImport) && !objectSet.isEmpty()) {
                // Drop the SQLite index temporarily (if any) to speed up bulk import...
                DBQueries.dropIndexOnTable(tableName, connection);

                if (isSuspendSyncAndJournaling) {
                    if (!canSuspendSyncAndJournaling) {
                        throw new IllegalStateException("Cannot suspend sync and journaling because it was not possible to read the original 'synchronous' and 'journal_mode' pragmas during the index initialization.");
                    }
                    DBQueries.suspendSyncAndJournaling(connection);
                }

            } else {
                // Not a bulk import, create indexes...
                DBQueries.createIndexOnTable(tableName, connection);
            }

            Iterable<Row<K, A>> rows = rowIterable(objectSet, primaryKeyAttribute, getAttribute(), queryOptions);
            final int rowsModified = DBQueries.bulkAdd(rows, tableName, connection);

            if (isBulkImport || (bulkImportExternallyManged != null && LAST.equals(bulkImportExternallyManged))) {
                // Bulk import finished, recreate the SQLite index...
                DBQueries.createIndexOnTable(tableName, connection);

                if (isSuspendSyncAndJournaling) {
                    DBQueries.setSyncAndJournaling(connection, pragmaSynchronous, pragmaJournalMode);
                }

            }

            return rowsModified > 0;
        }
        finally {
            objectSet.close();
        }
    }

    /**
     * Utility method that transforms an {@link Iterable} of domain objects into {@link Row}s composed by object id and
     * respective value.
     *
     * @param objects {@link Iterable} of domain objects.
     * @param primaryKeyAttribute {@link SimpleAttribute} used to retrieve the object id.
     * @param indexAttribute {@link Attribute} used to retrieve the value(s).
     * @param queryOptions The {@link QueryOptions}.
     * @return {@link Iterable} of {@link Row}s.
     */
    static <O, K, A> Iterable<Row< K, A>> rowIterable(final Iterable<O> objects,
                                                      final SimpleAttribute<O, K> primaryKeyAttribute,
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
                                currentObjectKey = primaryKeyAttribute.getValue(next, queryOptions);
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
    public boolean removeAll(final ObjectSet<O> objectSet, final QueryOptions queryOptions) {
        try {
            ConnectionManager connectionManager = getConnectionManager(queryOptions);
            if (!connectionManager.isApplyUpdateForIndexEnabled(this)) {
                return false;
            }

            final Connection connection = connectionManager.getConnection(this, queryOptions);
            final boolean isBulkImport = queryOptions.get(BulkImportExternallyManged.class) != null || FlagsEnabled.isFlagEnabled(queryOptions, SQLiteIndexFlags.BULK_IMPORT);
            if (isBulkImport) {
                // It's a bulk import, avoid creating the index on the SQLite table...
                DBQueries.createIndexTable(tableName, primaryKeyAttribute.getAttributeType(), getAttribute().getAttributeType(), connection);
            }
            else {
                // It's NOT a bulk import, create both table and index on the table...
                createTableIndexIfNeeded(connection);
            }

            Iterable<K> objectKeys = objectKeyIterable(objectSet, primaryKeyAttribute, queryOptions);

            int rowsModified = DBQueries.bulkRemove(objectKeys, tableName, connection);
            return rowsModified > 0;
        }
        finally {
            objectSet.close();
        }
    }

    /**
     * Utility method that transforms an {@link Iterable} of domain objects into an {@link Iterable} over the objects ids.
     *
     * @param objects {@link Iterable} of domain objects.
     * @param primaryKeyAttribute {@link SimpleAttribute} used to retrieve the object id.
     * @param queryOptions The {@link QueryOptions}.
     * @return {@link Iterable} over the objects ids.
     */
    static <O, K> Iterable<K> objectKeyIterable(final Iterable<O> objects,
                                                final SimpleAttribute<O, K> primaryKeyAttribute,
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
                        return primaryKeyAttribute.getValue(next, queryOptions);
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

        final Connection connection = connectionManager.getConnection(this, queryOptions);
        createTableIndexIfNeeded(connection);
        DBQueries.clearIndexTable(tableName, connection);
    }

    @Override
    public void init(ObjectStore<O> objectStore, QueryOptions queryOptions) {

        final ConnectionManager connectionManager = getConnectionManager(queryOptions);
        final Connection connection = connectionManager.getConnection(this, queryOptions);
        pragmaJournalMode = DBQueries.getPragmaJournalModeOrNull(connection);
        pragmaSynchronous = DBQueries.getPragmaSynchronousOrNull(connection);
        canSuspendSyncAndJournaling = pragmaJournalMode != null && pragmaSynchronous != null;

        doAddAll(ObjectSet.fromObjectStore(objectStore, queryOptions), queryOptions, true);
    }

    /**
     * Utility method to create the index table if needed.
     *
     * @param connection The {@link Connection}.
     */
    void createTableIndexIfNeeded(final Connection connection){
        DBQueries.createIndexTable(tableName, primaryKeyAttribute.getAttributeType(), getAttribute().getAttributeType(), connection);
        DBQueries.createIndexOnTable(tableName, connection);
    }

    /**
     * Utility method to extract the {@link ConnectionManager} from QueryOptions.
     *
     * @param queryOptions The {@link QueryOptions}.
     * @return The {@link ConnectionManager}
     *
     * @throws IllegalStateException if the ConnectionManager is not found.
     */
    ConnectionManager getConnectionManager(final QueryOptions queryOptions){
        ConnectionManager connectionManager = queryOptions.get(ConnectionManager.class);
        if (connectionManager == null)
            throw new IllegalStateException("A ConnectionManager is required but was not provided in the QueryOptions.");
        return connectionManager;
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

    @Override
    public Integer getCountOfDistinctKeys(QueryOptions queryOptions) {
        final ConnectionManager connectionManager = getConnectionManager(queryOptions);
        Connection connection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);
        return DBQueries.getCountOfDistinctKeys(tableName, connection);
    }

    @Override
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeysDescending(QueryOptions queryOptions) {
        return getStatisticsForDistinctKeys(queryOptions, true);
    }

    @Override
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeys(QueryOptions queryOptions) {
        return getStatisticsForDistinctKeys(queryOptions, false);
    }

    CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeys(final QueryOptions queryOptions, final boolean sortByKeyDescending){

        final CloseableResourceGroup closeableResourceGroup = CloseableRequestResources.forQueryOptions(queryOptions).addGroup();

        return new CloseableIterable<KeyStatistics<A>>() {
            @Override
            public CloseableIterator<KeyStatistics<A>> iterator() {
                final ConnectionManager connectionManager = getConnectionManager(queryOptions);
                final Connection connection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);

                final java.sql.ResultSet resultSet = DBQueries.getDistinctKeysAndCounts(sortByKeyDescending, tableName, connection);
                closeableResourceGroup.add(DBUtils.wrapAsCloseable(resultSet));

                return new LazyCloseableIterator<KeyStatistics<A>>() {
                    @Override
                    protected KeyStatistics<A> computeNext() {
                        try {
                            if (!resultSet.next()) {
                                close();
                                return endOfData();
                            }
                            A key = DBUtils.getValueFromResultSet(1, resultSet, attribute.getAttributeType());
                            Integer count = DBUtils.getValueFromResultSet(2, resultSet, Integer.class);
                            return new KeyStatistics<A>(key, count);
                        }
                        catch (Exception e) {
                            endOfData();
                            close();
                            throw new IllegalStateException("Unable to retrieve the ResultSet item.", e);
                        }
                    }

                    @Override
                    public void close() {
                        closeableResourceGroup.close();
                    }
                };
            }
        };

    }

    CloseableIterable<A> getDistinctKeysInRange(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, final boolean descending, final QueryOptions queryOptions) {
        final Query<O> query = getKeyRangeRestriction(lowerBound, lowerInclusive, upperBound, upperInclusive);

        final CloseableResourceGroup closeableResourceGroup = CloseableRequestResources.forQueryOptions(queryOptions).addGroup();

        return new CloseableIterable<A>() {
            @Override
            public CloseableIterator<A> iterator() {
                final ConnectionManager connectionManager = getConnectionManager(queryOptions);
                final Connection searchConnection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);

                final java.sql.ResultSet searchResultSet = DBQueries.getDistinctKeys(query, descending, tableName, searchConnection);
                closeableResourceGroup.add(DBUtils.wrapAsCloseable(searchResultSet));

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
                        closeableResourceGroup.close();
                    }
                };
            }
        };
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(final QueryOptions queryOptions) {
        return getKeysAndValues(null, true, null, true, queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, final QueryOptions queryOptions) {
        return getKeysAndValuesInRange(lowerBound, lowerInclusive, upperBound, upperInclusive, false, queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(QueryOptions queryOptions) {
        return getKeysAndValuesDescending(null, true, null, true, queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValuesDescending(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, QueryOptions queryOptions) {
        return getKeysAndValuesInRange(lowerBound, lowerInclusive, upperBound, upperInclusive, true, queryOptions);
    }

    CloseableIterable<KeyValue<A, O>> getKeysAndValuesInRange(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive, final boolean descending, final QueryOptions queryOptions) {
        final Query<O> query = getKeyRangeRestriction(lowerBound, lowerInclusive, upperBound, upperInclusive);

        final CloseableResourceGroup closeableResourceGroup = CloseableRequestResources.forQueryOptions(queryOptions).addGroup();

        return new CloseableIterable<KeyValue<A, O>>() {
            @Override
            public CloseableIterator<KeyValue<A, O>> iterator() {
                final ConnectionManager connectionManager = getConnectionManager(queryOptions);
                final Connection searchConnection = connectionManager.getConnection(SQLiteIndex.this, queryOptions);

                final java.sql.ResultSet searchResultSet = DBQueries.getKeysAndValues(query, descending, tableName, searchConnection);
                closeableResourceGroup.add(DBUtils.wrapAsCloseable(searchResultSet));

                return new LazyCloseableIterator<KeyValue<A, O>>() {
                    @Override
                    protected KeyValue<A, O> computeNext() {
                        try {
                            if (!searchResultSet.next()) {
                                close();
                                return endOfData();
                            }
                            final K objectKey = DBUtils.getValueFromResultSet(1, searchResultSet, primaryKeyAttribute.getAttributeType());
                            final A objectValue = DBUtils.getValueFromResultSet(2, searchResultSet, attribute.getAttributeType());
                            final O object = foreignKeyAttribute.getValue(objectKey, queryOptions);
                            return new KeyValueMaterialized<A, O>(objectValue, object);
                        }
                        catch (Exception e) {
                            endOfData();
                            close();
                            throw new IllegalStateException("Unable to retrieve the ResultSet item.", e);
                        }
                    }

                    @Override
                    public void close() {
                        closeableResourceGroup.close();
                    }
                };
            }
        };
    }

    Query<O> getKeyRangeRestriction(A lowerBound, boolean lowerInclusive, A upperBound, boolean upperInclusive) {
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
            query = has(attribute);
        }
        return query;
    }

    @Override
    public Integer getCountForKey(A key, QueryOptions queryOptions) {
        return retrieve(equal(attribute, key), queryOptions).size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLiteIndex that = (SQLiteIndex) o;

        if (!attribute.equals(that.attribute)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        result = 31 * result + attribute.hashCode();
        return result;
    }

    // ---------- Static factory methods to create SQLiteIndex ----------

    /**
     * Creates a new {@link SQLiteIndex}.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param objectKeyAttribute The {@link SimpleAttribute} used to retrieve the object key.
     * @param foreignKeyAttribute The {@link SimpleAttribute} to map a query result into the domain object.
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @param <K> The type of the object key.
     * @return a new instance of the {@link SQLiteIndex}
     */
    public static <A extends Comparable<A>, O, K> SQLiteIndex<A, O, K> onAttribute(final Attribute<O, A> attribute,
                                                                                   final SimpleAttribute<O, K> objectKeyAttribute,
                                                                                   final SimpleAttribute<K, O> foreignKeyAttribute) {
        return new SQLiteIndex<A, O, K>(attribute, objectKeyAttribute, foreignKeyAttribute, "");
    }
}

