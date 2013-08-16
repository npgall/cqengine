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
package com.googlecode.cqengine.index.radixinverted;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;
import com.googlecode.concurrenttrees.radixinverted.InvertedRadixTree;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.common.AbstractAttributeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationOption;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.query.simple.Equal;
import com.googlecode.cqengine.query.simple.StringIsContainedIn;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.connective.ResultSetUnion;
import com.googlecode.cqengine.resultset.connective.ResultSetUnionAll;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An index backed by a {@link ConcurrentInvertedRadixTree}.
 * <p/>
 * Supports query types:
 * <ul>
 *     <li>
 *         {@link Equal}
 *     </li>
 *     <li>
 *         {@link StringIsContainedIn}
 *     </li>
 * </ul>
 *
 * @author Niall Gallagher
 */
public class InvertedRadixTreeIndex<A extends CharSequence, O> extends AbstractAttributeIndex<A, O> {

    private static final int INDEX_RETRIEVAL_COST = 52;

    private volatile InvertedRadixTree<StoredResultSet<O>> tree = new ConcurrentInvertedRadixTree<StoredResultSet<O>>(new DefaultCharArrayNodeFactory());

    /**
     * Package-private constructor, used by static factory methods. Creates a new InvertedRadixTreeIndex initialized to
     * index the supplied attribute.
     *
     * @param attribute The attribute on which the index will be built
     */
    protected InvertedRadixTreeIndex(Attribute<O, A> attribute) {
        super(attribute, new HashSet<Class<? extends Query>>() {{
            add(Equal.class);
            add(StringIsContainedIn.class);
        }});
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, final Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        final InvertedRadixTree<StoredResultSet<O>> tree = this.tree;        
        Class<?> queryClass = query.getClass();
        if (queryClass.equals(Equal.class)) {
            final Equal<O, A> equal = (Equal<O, A>) query;
            return new ResultSet<O>() {
                @Override
                public Iterator<O> iterator() {
                    ResultSet<O> rs = tree.getValueForExactKey(equal.getValue());
                    return rs == null ? Collections.<O>emptySet().iterator() : rs.iterator();
                }
                @Override
                public boolean contains(O object) {
                    ResultSet<O> rs = tree.getValueForExactKey(equal.getValue());
                    return rs != null && rs.contains(object);
                }
                @Override
                public int size() {
                    ResultSet<O> rs = tree.getValueForExactKey(equal.getValue());
                    return rs == null ? 0 : rs.size();
                }
                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST;
                }
                @Override
                public int getMergeCost() {
                    // Return size of entire stored set as merge cost...
                    ResultSet<O> rs = tree.getValueForExactKey(equal.getValue());
                    return rs == null ? 0 : rs.size();
                }
            };
        }
        else if (queryClass.equals(StringIsContainedIn.class)) {
            final StringIsContainedIn<O, A> stringIsContainedIn = (StringIsContainedIn<O, A>) query;
            return new ResultSet<O>() {
                @Override
                public Iterator<O> iterator() {
                    Iterable<? extends ResultSet<O>> resultSets = tree.getValuesForKeysContainedIn(stringIsContainedIn.getValue());
                    ResultSet<O> rs = unionResultSets(resultSets, queryOptions);
                    return rs.iterator();
                }
                @Override
                public boolean contains(O object) {
                    Iterable<? extends ResultSet<O>> resultSets = tree.getValuesForKeysContainedIn(stringIsContainedIn.getValue());
                    ResultSet<O> rs = unionResultSets(resultSets, queryOptions);
                    return rs.contains(object);
                }
                @Override
                public int size() {
                    Iterable<? extends ResultSet<O>> resultSets = tree.getValuesForKeysContainedIn(stringIsContainedIn.getValue());
                    ResultSet<O> rs = unionResultSets(resultSets, queryOptions);
                    return rs.size();
                }
                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST;
                }
                @Override
                public int getMergeCost() {
                    Iterable<? extends ResultSet<O>> resultSets = tree.getValuesForKeysContainedIn(stringIsContainedIn.getValue());
                    ResultSet<O> rs = unionResultSets(resultSets, queryOptions);
                    return rs.getMergeCost();
                }
            };
        }
        else {
            throw new IllegalArgumentException("Unsupported query: " + query);
        }
    }

    /**
     * If a query option specifying logical deduplication was supplied, wrap the given result sets in
     * {@link ResultSetUnion}, otherwise wrap in {@link ResultSetUnionAll}.
     * <p/>
     * An exception is if the index is built on a SimpleAttribute, we can avoid deduplication and always use
     * {@link ResultSetUnionAll}, because the same object could not exist in more than one {@link StoredResultSet}.
     *
     * @param results Provides the result sets to union
     * @param queryOptions Specifies whether or not logical deduplication is required
     * @return A union view over the given result sets
     */
    ResultSet<O> unionResultSets(Iterable<? extends ResultSet<O>> results, Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        if (DeduplicationOption.isLogicalElimination(queryOptions) && !(getAttribute() instanceof SimpleAttribute)) {
            return new ResultSetUnion<O>(results) {
                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST;
                }
            };
        }
        else {
            return new ResultSetUnionAll<O>(results) {
                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST;
                }
            };
        }
    }


    /**
     * {@inheritDoc}
     * @return A {@link StoredSetBasedResultSet} based on a set backed by {@link ConcurrentHashMap}, as created via
     * {@link java.util.Collections#newSetFromMap(java.util.Map)}
     */
    public StoredResultSet<O> createValueSet() {
        return new StoredSetBasedResultSet<O>(Collections.<O>newSetFromMap(new ConcurrentHashMap<O, Boolean>()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObjectsAdded(Collection<O> objects) {
        final InvertedRadixTree<StoredResultSet<O>> tree = this.tree;
        for (O object : objects) {
            Iterable<A> attributeValues = getAttribute().getValues(object);
            for (A attributeValue : attributeValues) {

                // Look up StoredResultSet for the value...
                StoredResultSet<O> valueSet = tree.getValueForExactKey(attributeValue);
                if (valueSet == null) {
                    // No StoredResultSet, create and add one...
                    valueSet = createValueSet();
                    StoredResultSet<O> existingValueSet = tree.putIfAbsent(attributeValue, valueSet);
                    if (existingValueSet != null) {
                        // Another thread won race to add new value set, use that one...
                        valueSet = existingValueSet;
                    }
                }
                // Add the object to the StoredResultSet for this value...
                valueSet.add(object);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObjectsRemoved(Collection<O> objects) {
        final InvertedRadixTree<StoredResultSet<O>> tree = this.tree;
        for (O object : objects) {
            Iterable<A> attributeValues = getAttribute().getValues(object);
            for (A attributeValue : attributeValues) {
                StoredResultSet<O> valueSet = tree.getValueForExactKey(attributeValue);
                if (valueSet == null) {
                    continue;
                }
                valueSet.remove(object);
                if (valueSet.isEmpty()) {
                    tree.remove(attributeValue);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Set<O> collection) {
        notifyObjectsAdded(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObjectsCleared() {
        this.tree = new ConcurrentInvertedRadixTree<StoredResultSet<O>>(new DefaultCharArrayNodeFactory());
    }


    // ---------- Static factory methods to create InvertedRadixTreeIndexes ----------

    /**
     * Creates a new {@link InvertedRadixTreeIndex} on the specified attribute.
     * <p/>
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link InvertedRadixTreeIndex} on this attribute
     */
    public static <A extends CharSequence, O> InvertedRadixTreeIndex<A, O> onAttribute(Attribute<O, A> attribute) {
        return new InvertedRadixTreeIndex<A, O>(attribute);
    }
}
