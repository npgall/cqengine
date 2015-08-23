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
package com.googlecode.cqengine.index.hash;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.index.support.*;
import com.googlecode.cqengine.quantizer.Quantizer;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationOption;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Equal;
import com.googlecode.cqengine.query.simple.Has;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.connective.ResultSetUnion;
import com.googlecode.cqengine.resultset.connective.ResultSetUnionAll;
import com.googlecode.cqengine.resultset.filter.QuantizedResultSet;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * An index backed by a {@link ConcurrentHashMap}.
 * <p/>
 * Supports query types:
 * <ul>
 *     <li>
 *         {@link Equal}
 *     </li>
 * </ul>
 * </ul>
 * The constructor of this index accepts {@link Factory} objects, from which it will create the map and value sets it
 * uses internally. This allows the application to "tune" the construction parameters of these maps/sets,
 * by supplying custom factories.
 * For default settings, supply {@link DefaultIndexMapFactory} and {@link DefaultValueSetFactory}.
 *
 * @author Niall Gallagher
 */
public class HashIndex<A, O> extends AbstractMapBasedAttributeIndex<A, O, ConcurrentMap<A, StoredResultSet<O>>> implements KeyStatisticsAttributeIndex<A, O> {

    protected static final int INDEX_RETRIEVAL_COST = 30;

    /**
     * Package-private constructor, used by static factory methods. Creates a new HashIndex initialized to index the
     * supplied attribute.
     *
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param valueSetFactory A factory used to create sets to store values in the index
     * @param attribute The attribute on which the index will be built
     */
    protected HashIndex(Factory<ConcurrentMap<A, StoredResultSet<O>>> indexMapFactory, Factory<StoredResultSet<O>> valueSetFactory, Attribute<O, A> attribute) {
        super(indexMapFactory, valueSetFactory, attribute, new HashSet<Class<? extends Query>>() {{
            add(Equal.class);
            add(Has.class);
        }});
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(final Query<O> query, final QueryOptions queryOptions) {
        Class<?> queryClass = query.getClass();
        if (queryClass.equals(Equal.class)) {
            final Equal<O, A> equal = (Equal<O, A>) query;
            return new ResultSet<O>() {
                @Override
                public Iterator<O> iterator() {
                    ResultSet<O> rs = indexMap.get(getQuantizedValue(equal.getValue()));
                    return rs == null ? Collections.<O>emptySet().iterator() : filterForQuantization(rs, equal, queryOptions).iterator();
                }
                @Override
                public boolean contains(O object) {
                    ResultSet<O> rs = indexMap.get(getQuantizedValue(equal.getValue()));
                    return rs != null && filterForQuantization(rs, equal, queryOptions).contains(object);
                }
                @Override
                public boolean matches(O object) {
                    return query.matches(object, queryOptions);
                }
                @Override
                public int size() {
                    ResultSet<O> rs = indexMap.get(getQuantizedValue(equal.getValue()));
                    return rs == null ? 0 : filterForQuantization(rs, equal, queryOptions).size();
                }
                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST;
                }
                @Override
                public int getMergeCost() {
                    // Return size of entire stored set as merge cost...
                    ResultSet<O> rs = indexMap.get(getQuantizedValue(equal.getValue()));
                    return rs == null ? 0 : rs.size();
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
        else if (queryClass.equals(Has.class)) {
            // If a query option specifying logical deduplication is supplied return ResultSetUnion,
            // otherwise return ResultSetUnionAll.
            // We can avoid deduplication if the index is built on a SimpleAttribute however,
            // because the same object could not exist in more than one StoredResultSet...
            if (DeduplicationOption.isLogicalElimination(queryOptions) && !(getAttribute() instanceof SimpleAttribute || getAttribute() instanceof SimpleNullableAttribute)) {
                return new ResultSetUnion<O>(indexMap.values(), query, queryOptions) {
                    @Override
                    public int getRetrievalCost() {
                        return INDEX_RETRIEVAL_COST;
                    }
                };
            }
            else {
                return new ResultSetUnionAll<O>(indexMap.values(), query, queryOptions) {
                    @Override
                    public int getRetrievalCost() {
                        return INDEX_RETRIEVAL_COST;
                    }
                };
            }
        }
        else {
            throw new IllegalArgumentException("Unsupported query: " + query);
        }
    }

    // ---------- Hook methods which can be overridden by subclasses using a Quantizer ----------

    /**
     * A no-op method which can be overridden by a subclass to return a {@link ResultSet} which filters objects from the
     * given {@link ResultSet}, to return only those objects matching the query, for the case that the index is using a
     * {@link com.googlecode.cqengine.quantizer.Quantizer}.
     * <p/>
     * <b>This default implementation simply returns the given {@link ResultSet} unmodified.</b>
     *
     * @param storedResultSet A {@link com.googlecode.cqengine.resultset.ResultSet} stored against a quantized key in the index
     * @param query The query against which results should be matched
     * @param queryOptions Optional parameters for the query
     * @return A {@link ResultSet} which filters objects from the given {@link ResultSet},
     * to return only those objects matching the query
     */
    protected ResultSet<O> filterForQuantization(ResultSet<O> storedResultSet, Query<O> query, QueryOptions queryOptions) {
        return storedResultSet;
    }

    @Override
    public Integer getCountForKey(A key, QueryOptions queryOptions) {
        return super.getCountForKey(key);
    }

    @Override
    public CloseableIterable<A> getDistinctKeys(QueryOptions queryOptions) {
        return super.getDistinctKeys();
    }

    @Override
    public Integer getCountOfDistinctKeys(QueryOptions queryOptions) {
        return super.getCountOfDistinctKeys(queryOptions);
    }

    @Override
    public CloseableIterable<KeyStatistics<A>> getStatisticsForDistinctKeys(QueryOptions queryOptions) {
        return super.getStatisticsForDistinctKeys(queryOptions);
    }

    @Override
    public CloseableIterable<KeyValue<A, O>> getKeysAndValues(QueryOptions queryOptions) {
        return super.getKeysAndValues();
    }

    // ---------- Static factory methods to create HashIndexes ----------

    /**
     * Creates a new {@link HashIndex} on the specified attribute.
     * <p/>
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link HashIndex} on this attribute
     */
    public static <A, O> HashIndex<A, O> onAttribute(Attribute<O, A> attribute) {
        return onAttribute(new DefaultIndexMapFactory<A, O>(), new DefaultValueSetFactory<O>(), attribute);
    }

    /**
     * Creates a new {@link HashIndex} on the specified attribute.
     * <p/>
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param valueSetFactory A factory used to create sets to store values in the index
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link HashIndex} on this attribute
     */
    public static <A, O> HashIndex<A, O> onAttribute(Factory<ConcurrentMap<A, StoredResultSet<O>>> indexMapFactory, Factory<StoredResultSet<O>> valueSetFactory, Attribute<O, A> attribute) {
        return new HashIndex<A, O>(indexMapFactory, valueSetFactory, attribute);
    }

    /**
     * Creates a {@link HashIndex} on the given attribute using the given {@link Quantizer}.
     * <p/>
     * @param quantizer A {@link Quantizer} to use in this index
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link HashIndex} on the given attribute using the given {@link Quantizer}
     */
    public static <A, O> HashIndex<A, O> withQuantizerOnAttribute(final Quantizer<A> quantizer, Attribute<O, A> attribute) {
        return withQuantizerOnAttribute(new DefaultIndexMapFactory<A, O>(), new DefaultValueSetFactory<O>(), quantizer, attribute);
    }

    /**
     * Creates a {@link HashIndex} on the given attribute using the given {@link Quantizer}.
     * <p/>
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param valueSetFactory A factory used to create sets to store values in the index
     * @param quantizer A {@link Quantizer} to use in this index
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link HashIndex} on the given attribute using the given {@link Quantizer}
     */
    public static <A, O> HashIndex<A, O> withQuantizerOnAttribute(Factory<ConcurrentMap<A, StoredResultSet<O>>> indexMapFactory, Factory<StoredResultSet<O>> valueSetFactory, final Quantizer<A> quantizer, Attribute<O, A> attribute) {
        return new HashIndex<A, O>(indexMapFactory, valueSetFactory, attribute) {

            // ---------- Override the hook methods related to Quantizer ----------

            @Override
            protected ResultSet<O> filterForQuantization(ResultSet<O> resultSet, final Query<O> query, QueryOptions queryOptions) {
                return new QuantizedResultSet<O>(resultSet, query, queryOptions);
            }

            @Override
            protected A getQuantizedValue(A attributeValue) {
                return quantizer.getQuantizedValue(attributeValue);
            }

            @Override
            public boolean isQuantized() {
                return true;
            }
        };
    }

    /**
     * Creates an index map using default settings.
     */
    public static class DefaultIndexMapFactory<A, O> implements Factory<ConcurrentMap<A, StoredResultSet<O>>> {
        @Override
        public ConcurrentMap<A, StoredResultSet<O>> create() {
            return new ConcurrentHashMap<A, StoredResultSet<O>>();
        }
    }

    /**
     * Creates a value set using default settings.
     */
    public static class DefaultValueSetFactory<O> implements Factory<StoredResultSet<O>> {
        @Override
        public StoredResultSet<O> create() {
            return new StoredSetBasedResultSet<O>(Collections.<O>newSetFromMap(new ConcurrentHashMap<O, Boolean>()));
        }
    }
}
