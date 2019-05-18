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
package com.googlecode.cqengine.persistence.disk;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.sqlite.ConnectionManager;
import com.googlecode.cqengine.index.sqlite.RequestScopeConnectionManager;
import com.googlecode.cqengine.index.sqlite.SQLitePersistence;
import com.googlecode.cqengine.index.sqlite.support.DBQueries;
import com.googlecode.cqengine.index.sqlite.support.DBUtils;
import com.googlecode.cqengine.index.support.indextype.DiskTypeIndex;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.persistence.support.sqlite.LockReleasingConnection;
import com.googlecode.cqengine.persistence.support.sqlite.SQLiteDiskIdentityIndex;
import com.googlecode.cqengine.persistence.support.sqlite.SQLiteObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.Closeable;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.googlecode.cqengine.persistence.support.PersistenceFlags.READ_REQUEST;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static com.googlecode.cqengine.query.option.FlagsEnabled.isFlagEnabled;

/**
 * Specifies that a collection or indexes should be persisted to a particular file on disk.
 * <p/>
 * <b>Note on Concurrency</b><br/>
 * Note this disk persistence implementation supports fully concurrent reads and writes by default. This is because
 * it enables <i><a href="https://www.sqlite.org/wal.html">Write-Ahead Logging</a></i> ("WAL") journal mode in the
 * underlying SQLite database file by default (see that link for more details).
 * <p/>
 * Optionally, this class allows the application to override the journal mode or other settings in SQLite by
 * supplying <i>"override properties"</i> to the {@link #onPrimaryKeyInFileWithProperties(SimpleAttribute, File, Properties)}
 * method. As WAL mode is suitable for most applications, most applications should work best with the default settings;
 * the override support is intended for advanced or custom use cases.
 * <p>
 * Two other CQEngine-specific properties are also supported:
 * <ul>
 *     <li>
 *         {@code shared_cache} = true|false (default is false)<br/>
 *         This enables <a href="https://www.sqlite.org/sharedcache.html">SQLite Shared-Cache Mode</a>,
 *         which can improve transaction throughput and reduce IO,
 *         at the expense of supporting less write concurrency.
 *     </li>
 *     <li>
 *         {@code persistent_connection} = true|false (default is false)<br/>
 *         This causes the DiskPersistence to keep open an otherwise unused persistent connection
 *         to the database file on disk. This prevents the file from being closed between
 *         transactions, which can improve performance.<br/>
 *         This will be enabled automatically if {@code shared_cache} is enabled
 *         because it is a requirement for that feature to work.
 *         This might be beneficial to enable on its own even without {@code shared_cache} in some
 *         applications, but the benefit without {@code shared_cache} could to be quite marginal.<br/>
 *         When {@code persistent_connection} = true, it is recommended (although not mandatory)
 *         to call {@link #close()} when the application is finished using the collection;
 *         in order to close the persistent connection. Otherwise the persistent connection will only
 *         be closed when this object is garbage collected.
 *     </li>
 *     <li>
 *         {@code use_read_write_lock} = true|false (default is true)<br/>
 *         This is enabled by default but used only when {@code shared_cache} is enabled.
 *         This causes a read-write lock to be used to guard access to the shared cache.
 *         The shared-cache mode supports less concurrency than the non-shared cache mode,
 *         and leaving this enabled can prevent exceptions being thrown due to attempts to
 *         write concurrently.
 *     </li>
 * </ul>
 * </p>
 *
 * @author niall.gallagher
 */
public class DiskPersistence<O, A extends Comparable<A>> implements SQLitePersistence<O, A>, Closeable {

    final SimpleAttribute<O, A> primaryKeyAttribute;
    final File file;
    final SQLiteDataSource sqLiteDataSource;
    final boolean useReadWriteLock;

    // Read-write lock is only used in shared-cache mode...
    final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    static final Properties DEFAULT_PROPERTIES = new Properties();
    static {
        DEFAULT_PROPERTIES.setProperty("busy_timeout", String.valueOf(Integer.MAX_VALUE)); // Wait indefinitely to acquire locks (technically 68 years)
        DEFAULT_PROPERTIES.setProperty("journal_mode", "WAL"); // Use Write-Ahead-Logging which supports concurrent reads and writes
        DEFAULT_PROPERTIES.setProperty("synchronous", "NORMAL"); // Setting synchronous to normal is safe and faster when using WAL

        DEFAULT_PROPERTIES.setProperty("shared_cache", "false"); // Improves transaction throughput and reduces IO, at the expense of supporting less write concurrency
        DEFAULT_PROPERTIES.setProperty("persistent_connection", "false"); // Prevents the database file from being closed between transactions
    }

    // If persistent_connection=true, this will be a connection which we keep open to prevent SQLite
    // from closing the database file until this object is garbage-collected,
    // or close() is called explicitly on this object...
    volatile Connection persistentConnection;
    volatile boolean closed = false;

    protected DiskPersistence(SimpleAttribute<O, A> primaryKeyAttribute, File file, Properties overrideProperties) {
        Properties effectiveProperties = new Properties();
        effectiveProperties.putAll(DEFAULT_PROPERTIES);
        effectiveProperties.putAll(overrideProperties);
        SQLiteConfig sqLiteConfig = new SQLiteConfig(effectiveProperties);
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource(sqLiteConfig);
        sqLiteDataSource.setUrl("jdbc:sqlite:file:" + file);

        this.primaryKeyAttribute = primaryKeyAttribute;
        this.file = file.getAbsoluteFile();
        this.sqLiteDataSource = sqLiteDataSource;

        boolean openPersistentConnection = "true".equals(effectiveProperties.getProperty("persistent_connection")); //default false
        boolean useSharedCache = "true".equals(effectiveProperties.getProperty("shared_cache")); // default false
        boolean useReadWriteLock = !"false".equals(effectiveProperties.getProperty("use_read_write_lock")); // default true
        if (useSharedCache) {
            // If shared_cache mode is enabled, by default we also use a read-write lock,
            // unless using the read-write lock has been explicitly disabled...
            sqLiteDataSource.setUrl("jdbc:sqlite:file:" + file + "?cache=shared");
            this.useReadWriteLock = useReadWriteLock;
        }
        else {
            // If we are not using shared_cache mode, we never use a read-write lock...
            sqLiteDataSource.setUrl("jdbc:sqlite:file:" + file);
            this.useReadWriteLock = false;
        }
        if (useSharedCache || openPersistentConnection) {
            // If shared_cache is enabled, we always open a persistent connection regardless...
            this.persistentConnection = getConnectionWithoutRWLock(null, noQueryOptions());
        }
    }

    @Override
    public SimpleAttribute<O, A> getPrimaryKeyAttribute() {
        return primaryKeyAttribute;
    }

    public File getFile() {
        return file;
    }

    @Override
    public Connection getConnection(Index<?> index, QueryOptions queryOptions) {
        return useReadWriteLock
                ? getConnectionWithRWLock(index, queryOptions)
                : getConnectionWithoutRWLock(index, queryOptions);
    }

    protected Connection getConnectionWithRWLock(Index<?> index, QueryOptions queryOptions) {
        // Acquire a read lock IFF the READ_REQUEST flag has been set, otherwise acquire a write lock by default...
        final Lock connectionLock = isFlagEnabled(queryOptions, READ_REQUEST)
                                        ? readWriteLock.readLock() : readWriteLock.writeLock();

        connectionLock.lock();
        Connection connection;
        try {
            connection = getConnectionWithoutRWLock(index, queryOptions);
        }
        catch (RuntimeException e) {
            connectionLock.unlock();
            throw e;
        }
        return LockReleasingConnection.wrap(connection, connectionLock);
    }

    protected Connection getConnectionWithoutRWLock(Index<?> index, QueryOptions queryOptions) {
        if (closed) {
            throw new IllegalStateException("DiskPersistence has been closed: " + this.toString());
        }
        try {
            return sqLiteDataSource.getConnection();
        }
        catch (SQLException e) {
            throw new IllegalStateException("Failed to open SQLite connection for file: " + file, e);
        }
    }

    /**
     * @param index The {@link Index} for which a connection is required.
     * @return True if the given index is a {@link DiskTypeIndex}. Otherwise false.
     */
    @Override
    public boolean supportsIndex(Index<O> index) {
        return index instanceof DiskTypeIndex;
    }

    /**
     * Closes the persistent connection, if there is an open persistent connection.
     * After calling this, the DiskPersistence can no longer be used, and attempts to do
     * so will result in {@link IllegalStateException}s being thrown.
     */
    @Override
    public void close() {
        DBUtils.closeQuietly(persistentConnection);
        this.persistentConnection = null;
        this.closed = true;
    }

    /**
     * Finalizer which automatically calls {@link #close()} when this object is garbage collected.
]     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    @Override
    public long getBytesUsed() {
        Connection connection = null;
        try {
            connection = getConnection(null, noQueryOptions());
            return DBQueries.getDatabaseSize(connection);
        }
        finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Override
    public void compact() {
        Connection connection = null;
        try {
            connection = getConnection(null, noQueryOptions());
            DBQueries.compactDatabase(connection);
        }
        finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Override
    public void expand(long numBytes) {
        Connection connection = null;
        try {
            connection = getConnection(null, noQueryOptions());
            DBQueries.expandDatabase(connection, numBytes);
        }
        finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiskPersistence)) {
            return false;
        }
        DiskPersistence<?, ?> that = (DiskPersistence<?, ?>) o;
        return primaryKeyAttribute.equals(that.primaryKeyAttribute) && file.equals(that.file);
    }

    @Override
    public int hashCode() {
        int result = primaryKeyAttribute.hashCode();
        result = 31 * result + file.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DiskPersistence{" +
                "primaryKeyAttribute=" + primaryKeyAttribute +
                ", file=" + file +
                '}';
    }

    @Override
    public ObjectStore<O> createObjectStore() {
        return new SQLiteObjectStore<O, A>(this);
    }

    @Override
    public SQLiteDiskIdentityIndex<A, O> createIdentityIndex() {
        return SQLiteDiskIdentityIndex.onAttribute(primaryKeyAttribute);
    }

    /**
     * Creates a new {@link RequestScopeConnectionManager} and adds it to the given query options with key
     * {@link ConnectionManager}, if an only if no object with that key is already in the query options.
     * This allows the application to supply its own implementation of {@link ConnectionManager} to override the default
     * if necessary.
     *
     * @param queryOptions The query options supplied with the request into CQEngine.
     */
    @Override
    public void openRequestScopeResources(QueryOptions queryOptions) {
        if (queryOptions.get(ConnectionManager.class) == null) {
            queryOptions.put(ConnectionManager.class, new RequestScopeConnectionManager(this));
        }
    }

    /**
     * Closes a {@link RequestScopeConnectionManager} if it is present in the given query options with key
     * {@link ConnectionManager}.
     *
     * @param queryOptions The query options supplied with the request into CQEngine.
     */
    @Override
    public void closeRequestScopeResources(QueryOptions queryOptions) {
        ConnectionManager connectionManager = queryOptions.get(ConnectionManager.class);
        if (connectionManager instanceof RequestScopeConnectionManager) {
            ((RequestScopeConnectionManager) connectionManager).close();
            queryOptions.remove(ConnectionManager.class);
        }
    }

    /**
     * Creates a new unique temp file in the JVM temp directory which can be used for persistence.
     * @return a new unique temp file in the JVM temp directory which can be used for persistence.
     */
    public static File createTempFile() {
        File tempFile;
        try {
            tempFile = File.createTempFile("cqengine_", ".db");
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to create temp file for CQEngine disk persistence", e);
        }
        return tempFile;
    }

    /**
     * Creates a {@link DiskPersistence} object which persists to a temp file on disk. The exact temp file used can
     * be determined by calling the {@link #getFile()} method.
     *
     * @param primaryKeyAttribute An attribute which returns the primary key of objects in the collection
     * @return A {@link DiskPersistence} object which persists to a temp file on disk
     * @see #onPrimaryKeyInFile(SimpleAttribute, File)
     */
    public static <O, A extends Comparable<A>> DiskPersistence<O, A> onPrimaryKey(SimpleAttribute<O, A> primaryKeyAttribute) {
        return DiskPersistence.onPrimaryKeyInFile(primaryKeyAttribute, createTempFile());
    }

    /**
     * Creates a {@link DiskPersistence} object which persists to a given file on disk.
     *
     * @param primaryKeyAttribute An attribute which returns the primary key of objects in the collection
     * @param file The file on disk to which data should be persisted
     * @return A {@link DiskPersistence} object which persists to the given file on disk
     */
    public static <O, A extends Comparable<A>> DiskPersistence<O, A> onPrimaryKeyInFile(SimpleAttribute<O, A> primaryKeyAttribute, File file) {
        return DiskPersistence.onPrimaryKeyInFileWithProperties(primaryKeyAttribute, file, new Properties());
    }

    /**
     * Creates a {@link DiskPersistence} object which persists to a given file on disk.
     *
     * @param primaryKeyAttribute An attribute which returns the primary key of objects in the collection
     * @param file The file on disk to which data should be persisted
     * @param overrideProperties Optional properties to override default settings (can be empty to use all default
     *                           settings, but cannot be null)
     * @return A {@link DiskPersistence} object which persists to the given file on disk
     */
    public static <O, A extends Comparable<A>> DiskPersistence<O, A> onPrimaryKeyInFileWithProperties(SimpleAttribute<O, A> primaryKeyAttribute, File file, Properties overrideProperties) {
        return new DiskPersistence<O, A>(primaryKeyAttribute, file, overrideProperties);
    }
}
