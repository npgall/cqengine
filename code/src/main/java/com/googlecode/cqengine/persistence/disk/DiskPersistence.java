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
import com.googlecode.cqengine.index.disk.DiskIndex;
import com.googlecode.cqengine.index.sqlite.support.DBQueries;
import com.googlecode.cqengine.index.sqlite.support.DBUtils;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.sqlite.SQLitePersistentSet;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * Specifies that a collection or indexes should be persisted to a particular file on disk.
 *
 * @author niall.gallagher
 */
public class DiskPersistence<O, A extends Comparable<A>> implements Persistence<O, A> {

    final SimpleAttribute<O, A> primaryKeyAttribute;
    final File file;
    final SQLiteDataSource sqLiteDataSource;

    protected DiskPersistence(SimpleAttribute<O, A> primaryKeyAttribute, File file) {
        this.primaryKeyAttribute = primaryKeyAttribute;
        this.file = file.getAbsoluteFile();
        this.sqLiteDataSource = new SQLiteDataSource(new SQLiteConfig());
        sqLiteDataSource.setUrl("jdbc:sqlite:file:" + file);
    }

    @Override
    public SimpleAttribute<O, A> getPrimaryKeyAttribute() {
        return primaryKeyAttribute;
    }

    public File getFile() {
        return file;
    }

    @Override
    public Connection getConnection(Index<?> index) {
        try {
            return sqLiteDataSource.getConnection();
        }
        catch (SQLException e) {
            throw new IllegalStateException("Failed to open SQLite connection for file: " + file, e);
        }
    }

    /**
     * @param index The {@link Index} for which a connection is required.
     * @return True if the given index is a {@link DiskIndex}. Otherwise false.
     */
    @Override
    public boolean supportsIndex(Index<?> index) {
        return index instanceof DiskIndex;
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
    public void expand(long numBytes) {
        Connection connection = null;
        try {
            connection = getConnection(null);
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
    public Set<O> create() {
        return new SQLitePersistentSet<O, A>(this);
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
        File tempFile;
        try {
            tempFile = File.createTempFile("cqengine_", ".db");
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to create temp file for CQEngine disk persistence", e);
        }
        return new DiskPersistence<O, A>(primaryKeyAttribute, tempFile);
    }

    /**
     * Creates a {@link DiskPersistence} object which persists to a given file on disk.
     *
     * @param primaryKeyAttribute An attribute which returns the primary key of objects in the collection
     * @param file The file on disk to which data should be persisted
     * @return A {@link DiskPersistence} object which persists to the given file on disk
     */
    public static <O, A extends Comparable<A>> DiskPersistence<O, A> onPrimaryKeyInFile(SimpleAttribute<O, A> primaryKeyAttribute, File file) {
        return new DiskPersistence<O, A>(primaryKeyAttribute, file);
    }
}
