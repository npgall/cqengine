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
package com.googlecode.cqengine.index.radixreversed;

import com.googlecode.concurrenttrees.common.LazyIterator;
import com.googlecode.concurrenttrees.radix.node.NodeFactory;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radixreversed.ConcurrentReversedRadixTree;
import com.googlecode.concurrenttrees.radixreversed.ReversedRadixTree;
import com.googlecode.cqengine.attribute.*;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.AbstractAttributeIndex;
import com.googlecode.cqengine.index.support.indextype.OnHeapTypeIndex;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationOption;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Equal;
import com.googlecode.cqengine.query.simple.In;
import com.googlecode.cqengine.query.simple.StringEndsWith;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.connective.ResultSetUnion;
import com.googlecode.cqengine.resultset.connective.ResultSetUnionAll;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.cqengine.index.support.IndexSupport.deduplicateIfNecessary;

/**
 * An index backed by a {@link ConcurrentReversedRadixTree}.
 * <p/>
 * Supports query types:
 * <ul>
 *     <li>
 *         {@link Equal}
 *     </li>
 *     <li>
 *         {@link StringEndsWith}
 *     </li>
 * </ul>
 *
 * @author Niall Gallagher
 */
public class ReversedRadixTreeIndex<A extends CharSequence, O> extends AbstractAttributeIndex<A, O> implements OnHeapTypeIndex {

    private static final int INDEX_RETRIEVAL_COST = 51;

    final NodeFactory nodeFactory;
    volatile ReversedRadixTree<StoredResultSet<O>> tree;

    /**
     * Package-private constructor, used by static factory methods.
     */
    protected ReversedRadixTreeIndex(Attribute<O, A> attribute) {
        this(attribute, new DefaultCharArrayNodeFactory());
    }

    /**
     * Package-private constructor, used by static factory methods.
     */
    protected ReversedRadixTreeIndex(Attribute<O, A> attribute, NodeFactory nodeFactory) {
        super(attribute, new HashSet<Class<? extends Query>>() {{
            add(Equal.class);
            add(In.class);
            add(StringEndsWith.class);
        }});
        this.nodeFactory = nodeFactory;
        this.tree = new ConcurrentReversedRadixTree<StoredResultSet<O>>(nodeFactory);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean isQuantized() {
        return false;
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    @Override
    public ResultSet<O> retrieve(final Query<O> query, final QueryOptions queryOptions) {
        final ReversedRadixTree<StoredResultSet<O>> tree = this.tree;
        Class<?> queryClass = query.getClass();
        if (queryClass.equals(Equal.class)) {
            @SuppressWarnings("unchecked")
            Equal<O, A> equal = (Equal<O, A>) query;
            return retrieveEqual(equal, queryOptions, tree);
        }
        else if (queryClass.equals(In.class)){
            @SuppressWarnings("unchecked")
            In<O, A> in = (In<O, A>) query;
            return retrieveIn(in, queryOptions, tree);
        }
        else if (queryClass.equals(StringEndsWith.class)) {
            @SuppressWarnings("unchecked")
            final StringEndsWith<O, A> stringEndsWith = (StringEndsWith<O, A>) query;
            return new ResultSet<O>() {
                @Override
                public Iterator<O> iterator() {
                    Iterable<? extends ResultSet<O>> resultSets = tree.getValuesForKeysEndingWith(stringEndsWith.getValue());
                    ResultSet<O> rs = unionResultSets(resultSets, query, queryOptions);
                    return rs.iterator();
                }
                @Override
                public boolean contains(O object) {
                    Iterable<? extends ResultSet<O>> resultSets = tree.getValuesForKeysEndingWith(stringEndsWith.getValue());
                    ResultSet<O> rs = unionResultSets(resultSets, query, queryOptions);
                    return rs.contains(object);
                }
                @Override
                public boolean matches(O object) {
                    return query.matches(object, queryOptions);
                }
                @Override
                public int size() {
                    Iterable<? extends ResultSet<O>> resultSets = tree.getValuesForKeysEndingWith(stringEndsWith.getValue());
                    ResultSet<O> rs = unionResultSets(resultSets, query, queryOptions);
                    return rs.size();
                }
                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST;
                }
                @Override
                public int getMergeCost() {
                    Iterable<? extends ResultSet<O>> resultSets = tree.getValuesForKeysEndingWith(stringEndsWith.getValue());
                    ResultSet<O> rs = unionResultSets(resultSets, query, queryOptions);
                    return rs.getMergeCost();
                }
                @Override
                public void close() {
                    // No op.
                }
                @Override
                public Query<O> getQuery() {
                    return query;
                }
                @Override
                public QueryOptions getQueryOptions() {
                    return queryOptions;
                }
            };
        }
        else {
            throw new IllegalArgumentException("Unsupported query: " + query);
        }
    }

    protected ResultSet<O> retrieveIn(final In<O, A> in, final QueryOptions queryOptions, final ReversedRadixTree<StoredResultSet<O>> tree) {
        // Process the IN query as the union of the EQUAL queries for the values specified by the IN query.
        final Iterable<? extends ResultSet<O>> results = new Iterable<ResultSet<O>>() {
            @Override
            public Iterator<ResultSet<O>> iterator() {
                return new LazyIterator<ResultSet<O>>() {
                    final Iterator<A> values = in.getValues().iterator();
                    @Override
                    protected ResultSet<O> computeNext() {
                        if (values.hasNext()){
                            return retrieveEqual(new Equal<O, A>(in.getAttribute(), values.next()), queryOptions, tree);
                        }else{
                            return endOfData();
                        }
                    }
                };
            }
        };
        return deduplicateIfNecessary(results, in, getAttribute(), queryOptions, INDEX_RETRIEVAL_COST);
    }

    protected ResultSet<O> retrieveEqual(final Equal<O, A> equal, final QueryOptions queryOptions, final ReversedRadixTree<StoredResultSet<O>> tree) {
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
            public boolean matches(O object) {
                return equal.matches(object, queryOptions);
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
            @Override
            public void close() {
                // No op.
            }
            @Override
            public Query<O> getQuery() {
                return equal;
            }
            @Override
            public QueryOptions getQueryOptions() {
                return queryOptions;
            }
        };
    }

    /**
     * If a query option specifying logical deduplication was supplied, wrap the given result sets in
     * {@link ResultSetUnion}, otherwise wrap in {@link ResultSetUnionAll}.
     * <p/>
     * An exception is if the index is built on a SimpleAttribute, we can avoid deduplication and always use
     * {@link ResultSetUnionAll}, because the same object could not exist in more than one {@link StoredResultSet}.
     *
     * @param results Provides the result sets to union
     * @param query The query for which the union is being constructed
     * @param queryOptions Specifies whether or not logical deduplication is required
     * @return A union view over the given result sets
     */
    ResultSet<O> unionResultSets(Iterable<? extends ResultSet<O>> results, Query<O> query, QueryOptions queryOptions) {
        if (DeduplicationOption.isLogicalElimination(queryOptions)
                && !(getAttribute() instanceof ISimpleAttribute || getAttribute() instanceof ISimpleNullableAttribute)) {
            return new ResultSetUnion<O>(results, query, queryOptions) {
                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST;
                }
            };
        }
        else {
            return new ResultSetUnionAll<O>(results, query, queryOptions) {
                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST;
                }
            };
        }
    }


    /**
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
    public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        try {
            boolean modified = false;
            final ReversedRadixTree<StoredResultSet<O>> tree = this.tree;
            for (O object : objectSet) {
                Iterable<A> attributeValues = getAttribute().getValues(object, queryOptions);
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
                    modified |= valueSet.add(object);
                }
            }
            return modified;
        }
        finally {
            objectSet.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        try {
            boolean modified = false;
            final ReversedRadixTree<StoredResultSet<O>> tree = this.tree;
            for (O object : objectSet) {
                Iterable<A> attributeValues = getAttribute().getValues(object, queryOptions);
                for (A attributeValue : attributeValues) {
                    StoredResultSet<O> valueSet = tree.getValueForExactKey(attributeValue);
                    if (valueSet == null) {
                        continue;
                    }
                    modified |= valueSet.remove(object);
                    if (valueSet.isEmpty()) {
                        tree.remove(attributeValue);
                    }
                }
            }
            return modified;
        }
        finally {
            objectSet.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ObjectStore<O> objectStore, QueryOptions queryOptions) {
        addAll(ObjectSet.fromObjectStore(objectStore, queryOptions), queryOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear(QueryOptions queryOptions) {
        this.tree = new ConcurrentReversedRadixTree<StoredResultSet<O>>(new DefaultCharArrayNodeFactory());
    }


    // ---------- Static factory methods to create ReversedRadixTreeIndexes ----------

    /**
     * Creates a new {@link ReversedRadixTreeIndex} on the specified attribute.
     * <p/>
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link ReversedRadixTreeIndex} on this attribute
     */
    public static <A extends CharSequence, O> ReversedRadixTreeIndex<A, O> onAttribute(Attribute<O, A> attribute) {
        return new ReversedRadixTreeIndex<A, O>(attribute);
    }

    /**
     * Creates a new {@link ReversedRadixTreeIndex} on the specified attribute.
     * <p/>
     * @param attribute The attribute on which the index will be built
     * @param nodeFactory The NodeFactory to be used by the tree
     * @param <O> The type of the object containing the attribute
     * @return A {@link ReversedRadixTreeIndex} on this attribute
     */
    public static <A extends CharSequence, O> ReversedRadixTreeIndex<A, O> onAttributeUsingNodeFactory(Attribute<O, A> attribute, NodeFactory nodeFactory) {
        return new ReversedRadixTreeIndex<A, O>(attribute, nodeFactory);
    }
}
