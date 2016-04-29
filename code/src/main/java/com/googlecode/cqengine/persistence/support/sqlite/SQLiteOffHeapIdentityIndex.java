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
package com.googlecode.cqengine.persistence.support.sqlite;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.sqlite.SQLiteIdentityIndex;
import com.googlecode.cqengine.index.support.indextype.OffHeapTypeIndex;

/**
 * A subclass of {@link SQLiteIdentityIndex} intended for use with off-heap persistence.
 * This subclass does not override any behaviour, and exists only so that CQEngine can distinguish between
 * disk-based and off-heap configurations of the superclass index.
 *
 * @author niall.gallagher
 */
public class SQLiteOffHeapIdentityIndex<A extends Comparable<A>, O> extends SQLiteIdentityIndex<A, O> implements OffHeapTypeIndex {

    public SQLiteOffHeapIdentityIndex(SimpleAttribute<O, A> primaryKeyAttribute) {
        super(primaryKeyAttribute);
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    /**
     * Creates a new {@link SQLiteOffHeapIdentityIndex} for the given primary key attribute.
     *
     * @param primaryKeyAttribute The {@link SimpleAttribute} representing a primary key on which the index will be built.
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @return a new instance of {@link SQLiteOffHeapIdentityIndex}
     */
    public static <A extends Comparable<A>, O> SQLiteOffHeapIdentityIndex<A, O> onAttribute(final SimpleAttribute<O, A> primaryKeyAttribute) {
        return new SQLiteOffHeapIdentityIndex<A, O>(primaryKeyAttribute);
    }
}
