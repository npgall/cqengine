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
package com.googlecode.cqengine.index.unique;

import com.googlecode.concurrenttrees.common.LazyIterator;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.support.AbstractAttributeIndex;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.index.support.indextype.OnHeapTypeIndex;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Equal;
import com.googlecode.cqengine.query.simple.In;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.googlecode.cqengine.index.support.IndexSupport.deduplicateIfNecessary;

/**
 * An index backed by a {@link ConcurrentHashMap}, which can be more efficient than {@link HashIndex} when used with
 * (and only with) attributes which uniquely identify objects (primary key-type attributes).
 * <p/>
 * This type of index does not store a set of objects matching each attribute value, but instead stores only a
 * single object for each value. This results in faster query performance, and often lower memory usage, but has some
 * trade-offs.
 * <p/>
 * This index will throw an exception if a duplicate object is detected for an existing attribute value. That condition
 * means however that inconsistencies might already have arisen between this and other indexes as a result of the
 * application's misuse of this index.
 * <p/>
 * <b>Trade-offs: {@code UniqueIndex} versus {@code HashIndex}</b>
 * <ul>
 *     <li>
 *         {@code UniqueIndex} will always use less memory than a <i>non-quantized</i> {@code HashIndex}
 *     </li>
 *     <li>
 *         {@code UniqueIndex} will not necessarily use less memory than a <i>quantized</i> {@code HashIndex}, i.e.
 *         configured with a {@link com.googlecode.cqengine.quantizer.Quantizer}
 *     </li>
 *     <li>
 *         In all cases, {@code UniqueIndex} will answer queries faster than a {@code HashIndex}
 *     </li>
 *     <li>
 *         It is important that {@code UniqueIndex} only be used with attributes which uniquely identify objects
 *     </li>
 *     <li>
 *         <b>A {@code UniqueIndex} on a primary key-type attribute might not be compatible with the MVCC algorithm
 *         implemented by {@link TransactionalIndexedCollection}.</b>
 *         <ul><li>
 *             However, as an alternative option to reduce memory overhead in those situations see:
 *             {@link HashIndex#onSemiUniqueAttribute(Attribute)}
 *         </li></ul>
 *     </li>
 * </ul>
 * <p/>
 * Supports query types:
 * <ul>
 *     <li>
 *         {@link Equal}
 *     </li>
 * </ul>
 *
 * @author Kinz Liu
 * @author Niall Gallagher
 */
public class UniqueIndex<A,O> extends AbstractAttributeIndex<A,O> implements OnHeapTypeIndex {

    protected static final int INDEX_RETRIEVAL_COST = 25;

    protected final Factory<ConcurrentMap<A,O>> indexMapFactory;

    protected final ConcurrentMap<A,O> indexMap;

    /**
     * Package-private constructor, used by static factory methods. Creates a new UniqueIndex initialized to index the
     * supplied attribute.
     *
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param attribute The attribute on which the index will be built
     */
    protected UniqueIndex(Factory<ConcurrentMap<A,O>> indexMapFactory, Attribute<O, A> attribute)	{
        super(attribute, new HashSet<Class<? extends Query>>() {{
            add(Equal.class);
            add(In.class);
        }});
        this.indexMapFactory = indexMapFactory;
        this.indexMap = indexMapFactory.create();
    }

    @Override
    public boolean supportsQuery(Query<O> query, QueryOptions queryOptions) {
        Class<?> queryClass = query.getClass();
        return queryClass.equals(Equal.class) || queryClass.equals(In.class);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This index is mutable.
     *
     * @return true
     */
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
        Class<?> queryClass = query.getClass();
        if (queryClass.equals(Equal.class))
        {
            final ConcurrentMap<A, O> indexMap = this.indexMap;
            @SuppressWarnings("unchecked")
            Equal<O, A> equal = (Equal<O, A>) query;
            return retrieveEqual(equal, queryOptions, indexMap);
        }
        else if(queryClass.equals(In.class)){
            @SuppressWarnings("unchecked")
            In<O, A> in = (In<O, A>) query;
            return retrieveIn(in, queryOptions, indexMap);
        }
        throw new IllegalArgumentException("Unsupported query: " + query);
    }

    protected ResultSet<O> retrieveIn(final In<O, A> in, final QueryOptions queryOptions, final ConcurrentMap<A, O> indexMap) {
        // Process the IN query as the union of the EQUAL queries for the values specified by the IN query.
        final Iterable<? extends ResultSet<O>> results = new Iterable<ResultSet<O>>() {
            @Override
            public Iterator<ResultSet<O>> iterator() {
                return new LazyIterator<ResultSet<O>>() {
                    final Iterator<A> values = in.getValues().iterator();
                    @Override
                    protected ResultSet<O> computeNext() {
                        if (values.hasNext()){
                            return retrieveEqual(new Equal<O, A>(in.getAttribute(), values.next()), queryOptions, indexMap);
                        }else{
                            return endOfData();
                        }
                    }
                };
            }
        };
        return deduplicateIfNecessary(results, in, getAttribute(), queryOptions, INDEX_RETRIEVAL_COST);
    }

    protected ResultSet<O> retrieveEqual(final Equal<O, A> equal, final QueryOptions queryOptions, final ConcurrentMap<A, O> indexMap) {

        final O obj = indexMap.get(equal.getValue());

        return new ResultSet<O>() {
            @Override
            public Iterator<O> iterator() {
                return new UnmodifiableIterator<O>() {
                    boolean hasNext = (obj != null);
                    @Override
                    public boolean hasNext() {
                        return this.hasNext;
                    }
                    @Override
                    public O next() {
                        this.hasNext=false;
                        return obj;
                    }
                };
            }
            @Override
            public boolean contains(O object) {
                return (object != null && obj != null && object.equals(obj));
            }
            @Override
            public boolean matches(O object) {
                return equal.matches(object, queryOptions);
            }
            @Override
            public int size() {
                return obj == null ? 0 : 1;
            }
            @Override
            public int getRetrievalCost() {
                return INDEX_RETRIEVAL_COST;
            }
            @Override
            public int getMergeCost() {
                return obj == null ? 0 : 1;
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
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        try {
            boolean modified = false;
            ConcurrentMap<A, O> indexMap = this.indexMap;
            for (O object : objectSet) {
                Iterable<A> attributeValues = getAttribute().getValues(object, queryOptions);
                for (A attributeValue : attributeValues) {
                    O existingValue = indexMap.put(attributeValue, object);
                    if (existingValue != null && !existingValue.equals(object)) {
                        throw new UniqueConstraintViolatedException(
                                "The application has attempted to add a duplicate object to the UniqueIndex on attribute '"
                                        + attribute.getAttributeName() +
                                        "', potentially causing inconsistencies between indexes. " +
                                        "UniqueIndex should not be used with attributes which do not uniquely identify objects. " +
                                        "Problematic attribute value: '" + attributeValue + "', " +
                                        "problematic duplicate object: " + object);
                    }
                    modified = true;

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
            ConcurrentMap<A, O> indexMap = this.indexMap;
            for (O object : objectSet) {
                Iterable<A> attributeValues = getAttribute().getValues(object, queryOptions);
                for (A attributeValue : attributeValues) {
                    modified |= (indexMap.remove(attributeValue) != null);
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
     * This is a no-op for this type of index.
     * @param queryOptions Optional parameters for the update
     */
    @Override
    public void destroy(QueryOptions queryOptions) {
        // No-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear(QueryOptions queryOptions) {
        this.indexMap.clear();
    }

    public static class UniqueConstraintViolatedException extends RuntimeException {
        public UniqueConstraintViolatedException(String message) {
            super(message);
        }
    }

    /**
     * Creates an index map using default settings.
     */
    public static class DefaultIndexMapFactory<A, O> implements Factory<ConcurrentMap<A, O>> {
        @Override
        public ConcurrentMap<A, O> create() {
            return new ConcurrentHashMap<A, O>();
        }
    }

    // ---------- Static factory methods to create UniqueIndexes ----------

    /**
     * Creates a new {@link UniqueIndex} on the specified attribute.
     * <p/>
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link UniqueIndex} on this attribute
     */
    public static <A, O> UniqueIndex<A, O> onAttribute(Attribute<O, A> attribute) {
        return onAttribute(new DefaultIndexMapFactory<A, O>(), attribute);
    }

    /**
     * Creates a new {@link UniqueIndex} on the specified attribute.
     * <p/>
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link UniqueIndex} on this attribute
     */
    public static <A, O> UniqueIndex<A, O> onAttribute(Factory<ConcurrentMap<A, O>> indexMapFactory, Attribute<O, A> attribute) {
        return new UniqueIndex<A, O>(indexMapFactory, attribute);
    }
}
