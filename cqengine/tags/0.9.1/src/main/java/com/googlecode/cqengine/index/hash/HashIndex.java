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
package com.googlecode.cqengine.index.hash;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.common.AbstractMapBasedAttributeIndex;
import com.googlecode.cqengine.quantizer.Quantizer;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.query.simple.Equal;
import com.googlecode.cqengine.resultset.ResultSet;
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
 *
 * @author Niall Gallagher
 */
public class HashIndex<A, O> extends AbstractMapBasedAttributeIndex<A, O> {

    private static final int INDEX_RETRIEVAL_COST = 30;

    private final ConcurrentMap<A, StoredResultSet<O>> indexMap = new ConcurrentHashMap<A, StoredResultSet<O>>();

    /**
     * Package-private constructor, used by static factory methods. Creates a new HashIndex initialized to index the
     * supplied attribute.
     *
     * @param attribute The attribute on which the index will be built
     */
    HashIndex(Attribute<O, A> attribute) {
        super(attribute, new HashSet<Class<? extends Query>>() {{
            add(Equal.class);
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
    public ResultSet<O> retrieve(Query<O> query, Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        Class<?> queryClass = query.getClass();
        if (queryClass.equals(Equal.class)) {
            final Equal<O, A> equal = (Equal<O, A>) query;
            return new ResultSet<O>() {
                @Override
                public Iterator<O> iterator() {
                    ResultSet<O> rs = indexMap.get(getQuantizedValue(equal.getValue()));
                    return rs == null ? Collections.<O>emptySet().iterator() : filterForQuantization(rs, equal).iterator();
                }
                @Override
                public boolean contains(O object) {
                    ResultSet<O> rs = indexMap.get(getQuantizedValue(equal.getValue()));
                    return rs != null && filterForQuantization(rs, equal).contains(object);
                }
                @Override
                public int size() {
                    ResultSet<O> rs = indexMap.get(getQuantizedValue(equal.getValue()));
                    return rs == null ? 0 : filterForQuantization(rs, equal).size();
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
            };
        }
        else {
            throw new IllegalArgumentException("Unsupported query: " + query);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConcurrentMap<A, StoredResultSet<O>> getIndexMap() {
        return indexMap;
    }

    /**
     * {@inheritDoc}
     * @return A {@link StoredSetBasedResultSet} based on a set backed by {@link ConcurrentHashMap}, as created via
     * {@link Collections#newSetFromMap(java.util.Map)}
     */
    public StoredResultSet<O> createValueSet() {
        return new StoredSetBasedResultSet<O>(Collections.<O>newSetFromMap(new ConcurrentHashMap<O, Boolean>()));
    }

    // ---------- Hook methods which can be overridden by subclasses using a Quantizer ----------

    /**
     * A no-op method which can be overridden by a subclass to return a {@link ResultSet} which filters objects from the
     * given {@link ResultSet}, to return only those objects matching the query, for the case that the index is using a
     * {@link com.googlecode.cqengine.quantizer.Quantizer}.
     * <p/>
     * <b>This default implementation simply returns the given {@link ResultSet} unmodified.</b>
     *
     * @param storedResultSet A {@link ResultSet} stored against a quantized key in the index
     * @param query The query against which results should be matched
     * @return A {@link ResultSet} which filters objects from the given {@link ResultSet},
     * to return only those objects matching the query
     */
    protected ResultSet<O> filterForQuantization(ResultSet<O> storedResultSet, Query<O> query) {
        return storedResultSet;
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
        return new HashIndex<A, O>(attribute);
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
        return new HashIndex<A, O>(attribute) {

            // ---------- Override the hook methods related to Quantizer ----------

            final HashIndex<A, O> thisRef = this;
            @Override
            protected ResultSet<O> filterForQuantization(ResultSet<O> resultSet, final Query<O> query) {
                return new QuantizedResultSet<O>(resultSet, query);
            }

            @Override
            protected A getQuantizedValue(A attributeValue) {
                return quantizer.getQuantizedValue(attributeValue);
            }
        };
    }
}
