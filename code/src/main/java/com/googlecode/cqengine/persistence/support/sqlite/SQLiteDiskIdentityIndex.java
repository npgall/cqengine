package com.googlecode.cqengine.persistence.support.sqlite;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.sqlite.SQLiteIdentityIndex;

/**
 * A subclass of {@link SQLiteIdentityIndex} intended for use with disk persistence.
 * This subclass does not override any behaviour, and exists only so that CQEngine can distinguish between
 * disk-based and off-heap configurations of the superclass index.
 *
 * @author niall.gallagher
 */
public class SQLiteDiskIdentityIndex<A extends Comparable<A>, O> extends SQLiteIdentityIndex<A, O> {

    public SQLiteDiskIdentityIndex(SimpleAttribute<O, A> primaryKeyAttribute) {
        super(primaryKeyAttribute);
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    /**
     * Creates a new {@link SQLiteDiskIdentityIndex} for the given primary key attribute.
     *
     * @param primaryKeyAttribute The {@link SimpleAttribute} representing a primary key on which the index will be built.
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @return a new instance of {@link SQLiteDiskIdentityIndex}
     */
    public static <A extends Comparable<A>, O> SQLiteDiskIdentityIndex<A, O> onAttribute(final SimpleAttribute<O, A> primaryKeyAttribute) {
        return new SQLiteDiskIdentityIndex<A, O>(primaryKeyAttribute);
    }
}
