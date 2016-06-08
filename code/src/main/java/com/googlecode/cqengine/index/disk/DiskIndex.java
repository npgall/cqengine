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
package com.googlecode.cqengine.index.disk;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.sqlite.SimplifiedSQLiteIndex;
import com.googlecode.cqengine.index.support.indextype.DiskTypeIndex;
import com.googlecode.cqengine.persistence.disk.DiskPersistence;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.common.WrappedResultSet;

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
public class DiskIndex<A extends Comparable<A>, O, K extends Comparable<K>> extends SimplifiedSQLiteIndex<A, O, K> implements DiskTypeIndex  {

    // An integer to add or subtract to the retrieval cost returned by SimplifiedSQLiteIndex (which ranges 80-89).
    // Therefore the retrieval costs for this index will range from 90-99...
    static final int INDEX_RETRIEVAL_COST_DELTA = +10;

    DiskIndex(Class<? extends DiskPersistence<O, A>> persistenceType, Attribute<O, A> attribute, String tableNameSuffix) {
        super(persistenceType, attribute, tableNameSuffix);
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        return new WrappedResultSet<O>(super.retrieve(query, queryOptions)) {
            @Override
            public int getRetrievalCost() {
                return super.getRetrievalCost() + INDEX_RETRIEVAL_COST_DELTA;
            }
        };
    }

    // ---------- Static factory methods to create DiskIndex ----------

    /**
     * Creates a new {@link DiskIndex}. This will obtain details of the {@link DiskPersistence} to use from the
     * IndexedCollection, throwing an exception if the IndexedCollection has not been configured with a suitable
     * DiskPersistence.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param <A> The type of the attribute to be indexed.
     * @param <O> The type of the object containing the attribute.
     * @return A {@link DiskIndex} on the given attribute.
     */
    @SuppressWarnings("unchecked") // unchecked, because type K will be provided later via the init() method
    public static <A extends Comparable<A>, O> DiskIndex<A, O, ? extends Comparable<?>> onAttribute(final Attribute<O, A> attribute) {
        return new DiskIndex(DiskPersistence.class, attribute, "");
    }
}
