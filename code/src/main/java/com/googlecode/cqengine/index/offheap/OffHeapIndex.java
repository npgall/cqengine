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
package com.googlecode.cqengine.index.offheap;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.sqlite.SimplifiedSQLiteIndex;
import com.googlecode.cqengine.index.support.indextype.OffHeapTypeIndex;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;

/**
 * An index persisted in native memory within the JVM process but outside the Java heap.
 * <p/>
 * This index is similar to the on-heap {@link com.googlecode.cqengine.index.navigable.NavigableIndex} and supports
 * the same types of queries.
 * <p/>
 * The current implementation of this index is based on {@link com.googlecode.cqengine.index.sqlite.SQLiteIndex}.
 *
 * @author niall.gallagher
 */
public class OffHeapIndex<A extends Comparable<A>, O, K extends Comparable<K>> extends SimplifiedSQLiteIndex<A, O, K> implements OffHeapTypeIndex {

    OffHeapIndex(Class<? extends OffHeapPersistence<O, A>> persistenceType, Attribute<O, A> attribute) {
        super(persistenceType, attribute);
    }

    // ---------- Static factory methods to create OffHeapIndex ----------

    /**
     * Creates a new {@link OffHeapIndex}.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param <A> The type of the attribute to be indexed.
     * @param <O> The type of the object containing the attribute.
     * @return An {@link OffHeapIndex} on the given attribute.
     */
    @SuppressWarnings("unchecked") // unchecked, because type K will be provided later via the init() method
    public static <A extends Comparable<A>, O> OffHeapIndex<A, O, ? extends Comparable<?>> onAttribute(final Attribute<O, A> attribute) {
        return new OffHeapIndex(OffHeapPersistence.class, attribute);
    }
}
