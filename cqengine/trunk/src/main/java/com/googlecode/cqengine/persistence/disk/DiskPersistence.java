package com.googlecode.cqengine.persistence.disk;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.sqlite.SQLitePersistentSet;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
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

    @Override
    public boolean isApplyUpdateForIndexEnabled(Index<?> index) {
        return true;
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

    public static <O, A extends Comparable<A>> DiskPersistence<O, A> onPrimaryKeyInFile(SimpleAttribute<O, A> primaryKeyAttribute, File file) {
        return new DiskPersistence<O, A>(primaryKeyAttribute, file);
    }
}
