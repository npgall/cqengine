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

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.PartialIndex;
import com.googlecode.cqengine.index.support.PartialSortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.indextype.NonHeapTypeIndex;
import com.googlecode.cqengine.query.Query;

import static com.googlecode.cqengine.index.sqlite.support.DBUtils.sanitizeForTableName;

/**
 * A {@link PartialIndex} backed by a {@link SQLiteIndex}.
 *
 * @author niall.gallagher
 */
public class PartialSQLiteIndex<A extends Comparable<A>, O, K> extends PartialSortedKeyStatisticsAttributeIndex<A, O> implements NonHeapTypeIndex {

    final SimpleAttribute<O, K> primaryKeyAttribute;
    final SimpleAttribute<K, O> foreignKeyAttribute;
    final String tableNameSuffix;

    /**
     * Protected constructor, called by subclasses.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param primaryKeyAttribute The {@link SimpleAttribute} used to retrieve the primary key.
     * @param foreignKeyAttribute The {@link SimpleAttribute} to map a query result into the domain object.
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     */
    protected PartialSQLiteIndex(Attribute<O, A> attribute,
                                 SimpleAttribute<O, K> primaryKeyAttribute,
                                 SimpleAttribute<K, O> foreignKeyAttribute,
                                 Query<O> filterQuery) {
        super(attribute, filterQuery);
        this.primaryKeyAttribute = primaryKeyAttribute;
        this.foreignKeyAttribute = foreignKeyAttribute;
        this.tableNameSuffix = "_partial_" + sanitizeForTableName(filterQuery.toString());
    }

    @Override
    @SuppressWarnings("unchecked") // unchecked, because type K will be provided later via the init() method
    protected SortedKeyStatisticsAttributeIndex<A, O> createBackingIndex() {
        return new SQLiteIndex(attribute, primaryKeyAttribute, foreignKeyAttribute, tableNameSuffix) {
            @Override
            public Index getEffectiveIndex() {
                return PartialSQLiteIndex.this.getEffectiveIndex();
            }
        };
    }

    // ---------- Static factory methods to create PartialSQLiteIndex ----------

    /**
     * Creates a new {@link PartialSQLiteIndex}.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param primaryKeyAttribute The {@link SimpleAttribute} used to retrieve the primary key.
     * @param foreignKeyAttribute The {@link SimpleAttribute} to map a query result into the domain object.
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @param <K> The type of the object key.
     * @return a new instance of the {@link SQLiteIndex}
     */
    public static <A extends Comparable<A>, O, K> PartialSQLiteIndex<A, O, K> onAttributeWithFilterQuery(Attribute<O, A> attribute,
                                                                                   SimpleAttribute<O, K> primaryKeyAttribute,
                                                                                   SimpleAttribute<K, O> foreignKeyAttribute,
                                                                                   Query<O> filterQuery) {
        return new PartialSQLiteIndex<A, O, K>(attribute, primaryKeyAttribute, foreignKeyAttribute, filterQuery);
    }
}
