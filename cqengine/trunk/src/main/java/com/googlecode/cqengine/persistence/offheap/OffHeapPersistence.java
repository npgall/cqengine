/**
 * Copyright 2012 Niall Gallagher
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
package com.googlecode.cqengine.persistence.offheap;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.sqlite.support.DBQueries;
import com.googlecode.cqengine.index.sqlite.support.DBUtils;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.sqlite.SQLitePersistentSet;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Specifies that a collection or indexes should be persisted in native memory, within the JVM process but outside the
 * Java heap.
 * <p/>
 * Each instance of this object specifies persistence to a different area of memory. So for example, to have an
 * {@link com.googlecode.cqengine.IndexedCollection} and multiple indexes all persist to the same area of memory,
 * configure them all to persist via the same instance of this object.
 * <p/>
 * <b>Garbage collection</b><br/>
 * The memory allocated off-heap will be freed automatically when this object is garbage collected. So using off-heap
 * memory does not require any special handling per-se. This object will be garbage collected when any
 * {@link com.googlecode.cqengine.IndexedCollection} or indexes using this object for persistence (which hold a
 * reference to this object internally) are garbage collected, and the application also releases any direct reference to
 * this object which it might be holding.
 * <p/>
 * <b>Garbage collection - implementation details</b><br/>
 * Internally this persistence strategy will open a connection to an in-memory SQLite database, and it will hold open
 * a connection to that database until either this object is garbage collected, or the {@link  #close()} method is
 * called explicitly. (The {@link  #finalize()} method of this object calls {@link #close()} automatically.)
 * <p/>
 * This object provides additional connections to the database on-demand; which the {@link IndexedCollection}
 * and indexes will request on-the-fly as necessary whenever the collection or indexes need to be read or updated.
 * SQLite automatically frees the memory used by an in-memory database when the last connection to the
 * database is closed. So by holding open a connection, this object keeps the in-memory database alive between requests.
 * <p/>
 * In terms of memory usage, the application can treat this as a very large object. The memory will be freed when this
 * object is garbage collected, but the application can also free memory sooner by calling {@link #close()}, but
 * this is optional.
 *
 * @author niall.gallagher
 */
public class OffHeapPersistence<O, A extends Comparable<A>> implements Persistence<O, A>, Closeable {

    static final AtomicInteger INSTANCE_ID_GENERATOR = new AtomicInteger();

    final SimpleAttribute<O, A> primaryKeyAttribute;
    final String instanceName;
    final SQLiteDataSource sqLiteDataSource;

    // A connection which we keep open to prevent SQLite from freeing the in-memory database
    // until this object is garbage-collected, or close() is called explicitly on this object...
    volatile Connection persistentConnection;
    volatile boolean closed = false;

    protected OffHeapPersistence(SimpleAttribute<O, A> primaryKeyAttribute) {
        this.primaryKeyAttribute = primaryKeyAttribute;
        this.sqLiteDataSource = new SQLiteDataSource(new SQLiteConfig());
        this.instanceName = "cqengine_" + INSTANCE_ID_GENERATOR.incrementAndGet();
        sqLiteDataSource.setUrl("jdbc:sqlite:file:" + instanceName + "?mode=memory&cache=shared");
        this.persistentConnection = getConnection(null);
    }

    @Override
    public SimpleAttribute<O, A> getPrimaryKeyAttribute() {
        return primaryKeyAttribute;
    }

    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public Connection getConnection(Index<?> index) {
        if (closed) {
            throw new IllegalStateException("OffHeapPersistence has been closed: " + this.toString());
        }
        try {
            return sqLiteDataSource.getConnection();
        }
        catch (SQLException e) {
            throw new IllegalStateException("Failed to open SQLite connection for memory instance: " + instanceName, e);
        }
    }

    @Override
    public void close() {
        DBUtils.closeQuietly(persistentConnection);
        this.persistentConnection = null;
        this.closed = true;
    }

    @Override
    public boolean isApplyUpdateForIndexEnabled(Index<?> index) {
        return true;
    }

    @Override
    public long getBytesUsed() {
        Connection connection = null;
        try {
            connection = getConnection(null);
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
            connection = getConnection(null);
            DBQueries.compactDatabase(connection);
        }
        finally {
            DBUtils.closeQuietly(connection);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OffHeapPersistence)) {
            return false;
        }
        OffHeapPersistence<?, ?> that = (OffHeapPersistence<?, ?>) o;
        return primaryKeyAttribute.equals(that.primaryKeyAttribute) && instanceName.equals(that.instanceName);
    }

    @Override
    public int hashCode() {
        int result = primaryKeyAttribute.hashCode();
        result = 31 * result + instanceName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OffHeapPersistence{" +
                "primaryKeyAttribute=" + primaryKeyAttribute +
                ", instanceName='" + instanceName + '\'' +
                '}';
    }

    @Override
    public Set<O> create() {
        return new SQLitePersistentSet<O, A>(this);
    }

    /**
     * Creates an {@link OffHeapPersistence} object which persists to native memory, within the JVM process but outside
     * the Java heap.
     *
     * @param primaryKeyAttribute An attribute which returns the primary key of objects in the collection
     * @return An {@link OffHeapPersistence} object which persists to native memory
     */
    public static <O, A extends Comparable<A>> OffHeapPersistence<O, A> onPrimaryKey(SimpleAttribute<O, A> primaryKeyAttribute) {
        return new OffHeapPersistence<O, A>(primaryKeyAttribute);
    }
}
