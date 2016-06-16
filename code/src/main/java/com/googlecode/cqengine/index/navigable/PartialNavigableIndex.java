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
package com.googlecode.cqengine.index.navigable;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.index.support.PartialIndex;
import com.googlecode.cqengine.index.support.PartialSortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.indextype.OnHeapTypeIndex;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;

import java.util.concurrent.ConcurrentNavigableMap;

/**
 * A {@link PartialIndex} which wraps a {@link NavigableIndex} and uses {@link OnHeapPersistence}.
 *
 * @author niall.gallagher
 */
public class PartialNavigableIndex<A extends Comparable<A>, O> extends PartialSortedKeyStatisticsAttributeIndex<A, O> implements OnHeapTypeIndex {

    final Factory<ConcurrentNavigableMap<A, StoredResultSet<O>>> indexMapFactory;
    final Factory<StoredResultSet<O>> valueSetFactory;

    /**
     * Protected constructor, called by subclasses.
     *
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     */
    protected PartialNavigableIndex(Factory<ConcurrentNavigableMap<A, StoredResultSet<O>>> indexMapFactory, Factory<StoredResultSet<O>> valueSetFactory, Attribute<O, A> attribute, Query<O> filterQuery) {
        super(attribute, filterQuery);
        this.indexMapFactory = indexMapFactory;
        this.valueSetFactory = valueSetFactory;
    }

    @Override
    @SuppressWarnings("unchecked") // unchecked, because type K will be provided later via the init() method
    protected SortedKeyStatisticsAttributeIndex<A, O> createBackingIndex() {
        return new NavigableIndex<A, O>(indexMapFactory, valueSetFactory, attribute) {
            @Override
            public Index getEffectiveIndex() {
                return PartialNavigableIndex.this.getEffectiveIndex();
            }
        };
    }

    // ---------- Static factory methods to create PartialNavigableIndex ----------

    /**
     * Creates a new {@link PartialNavigableIndex}.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     * @param <A> The type of the attribute to be indexed.
     * @param <O> The type of the object containing the attribute.
     * @return A {@link PartialNavigableIndex} on the given attribute.
     */
    public static <A extends Comparable<A>, O> PartialNavigableIndex<A, O> onAttributeWithFilterQuery(Attribute<O, A> attribute, Query<O> filterQuery) {
        return onAttributeWithFilterQuery(new NavigableIndex.DefaultIndexMapFactory<A, O>(), new NavigableIndex.DefaultValueSetFactory<O>(), attribute, filterQuery);
    }

    /**
     * Creates a new {@link PartialNavigableIndex}.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     * @param indexMapFactory The index map factory to supply to the {@link NavigableIndex}.
     * @param valueSetFactory The value set factory to supply to the {@link NavigableIndex}.
     * @param <A> The type of the attribute to be indexed.
     * @param <O> The type of the object containing the attribute.
     * @return A {@link PartialNavigableIndex} on the given attribute.
     */
    public static <A extends Comparable<A>, O> PartialNavigableIndex<A, O> onAttributeWithFilterQuery(Factory<ConcurrentNavigableMap<A, StoredResultSet<O>>> indexMapFactory, Factory<StoredResultSet<O>> valueSetFactory, Attribute<O, A> attribute, Query<O> filterQuery) {
        return new PartialNavigableIndex<A, O>(indexMapFactory, valueSetFactory, attribute, filterQuery);
    }
}
