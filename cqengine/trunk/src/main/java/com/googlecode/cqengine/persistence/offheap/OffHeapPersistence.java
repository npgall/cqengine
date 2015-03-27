package com.googlecode.cqengine.persistence.offheap;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
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

    public static <O, A extends Comparable<A>> OffHeapPersistence<O, A> onPrimaryKey(SimpleAttribute<O, A> primaryKeyAttribute) {
        return new OffHeapPersistence<O, A>(primaryKeyAttribute);
    }
}
