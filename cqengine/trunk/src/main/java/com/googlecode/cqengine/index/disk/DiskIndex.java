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
package com.googlecode.cqengine.index.disk;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.sqlite.SimplifiedSQLiteIndex;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.disk.DiskPersistence;

/**
 * An index persisted in a file on disk.
 * <p/>
 * This index is similar to the on-heap {@link com.googlecode.cqengine.index.navigable.NavigableIndex} and supports
 * the same types of queries.
 * <p/>
 * The current implementation of this index is based on {@link com.googlecode.cqengine.index.sqlite.SQLiteIndex}.
 *
 * @author niall.gallagher
 */
public class DiskIndex<A extends Comparable<A>, O, K extends Comparable<K>> extends SimplifiedSQLiteIndex<A, O, K> {

    DiskIndex(DiskPersistence<O, K> persistence, Attribute<O, A> attribute, SimpleAttribute<K, O> foreignKeyAttribute) {
        super(attribute, persistence, foreignKeyAttribute);
    }

    DiskIndex(Class<? extends Persistence<O, A>> persistenceType, Attribute<O, A> attribute) {
        super(persistenceType, attribute);
    }

    // ---------- Static factory methods to create OffHeapIndex ----------

    /**
     * Creates a new {@link DiskIndex} which uses a persistence strategy configured explicitly (which may be
     * different from how the IndexedCollection is persisted).
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param persistence Specifies how and where the index should be persisted.
     * @param foreignKeyAttribute A {@link SimpleAttribute} which can resolve a foreign key read from the index back
     *                            into an object.
     * @param <A> The type of the attribute to be indexed.
     * @param <O> The type of the object containing the attribute.
     * @param <K> The type of the foreign key.
     * @return A {@link DiskIndex} on the given attribute.
     */
    public static <A extends Comparable<A>, O, K extends Comparable<K>> DiskIndex<A, O, K> onAttribute(final Attribute<O, A> attribute,
                                                                                                          final DiskPersistence<O, K> persistence,
                                                                                                          final SimpleAttribute<K, O> foreignKeyAttribute) {
        return new DiskIndex<A, O, K>(persistence, attribute, foreignKeyAttribute);
    }

    /**
     * Creates a new {@link DiskIndex} which uses the same persistence strategy as the IndexedCollection.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param <A> The type of the attribute to be indexed.
     * @param <O> The type of the object containing the attribute.
     * @return A {@link DiskIndex} on the given attribute.
     */
    @SuppressWarnings("unchecked") // unchecked, because type K will be provided by SQLitePersistentSet in the init() method
    public static <A extends Comparable<A>, O> DiskIndex<A, O, ? extends Comparable<?>> onAttribute(final Attribute<O, A> attribute) {
        return new DiskIndex(DiskPersistence.class, attribute);
    }
}
