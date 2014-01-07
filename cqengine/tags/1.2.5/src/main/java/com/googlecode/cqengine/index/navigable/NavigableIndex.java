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
package com.googlecode.cqengine.index.navigable;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.common.Factory;
import com.googlecode.cqengine.quantizer.Quantizer;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationOption;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.query.simple.*;
import com.googlecode.cqengine.index.common.AbstractMapBasedAttributeIndex;
import com.googlecode.cqengine.resultset.connective.ResultSetUnion;
import com.googlecode.cqengine.resultset.connective.ResultSetUnionAll;
import com.googlecode.cqengine.resultset.filter.QuantizedResultSet;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;

import java.util.*;
import java.util.concurrent.*;

/**
 * An index backed by a {@link ConcurrentSkipListMap}.
 * <p/>
 * Supports query types:
 * <ul>
 *     <li>
 *         {@link Equal}
 *     </li>
 *     <li>
 *         {@link LessThan}
 *     </li>
 *     <li>
 *         {@link GreaterThan}
 *     </li>
 *     <li>
 *         {@link Between}
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
public class NavigableIndex<A extends Comparable<A>, O> extends AbstractMapBasedAttributeIndex<A, O, ConcurrentNavigableMap<A, StoredResultSet<O>>> {

    protected static final int INDEX_RETRIEVAL_COST = 40;

    /**
     * Package-private constructor, used by static factory methods. Creates a new NavigableIndex initialized to index
     * the supplied attribute.
     *
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param valueSetFactory A factory used to create sets to store values in the index
     * @param attribute The attribute on which the index will be built
     */
    protected NavigableIndex(Factory<ConcurrentNavigableMap<A, StoredResultSet<O>>> indexMapFactory, Factory<StoredResultSet<O>> valueSetFactory, Attribute<O, A> attribute) {
        super(indexMapFactory, valueSetFactory,  attribute, new HashSet<Class<? extends Query>>() {{
                add(Equal.class);
                add(LessThan.class);
                add(GreaterThan.class);
                add(Between.class);
        }});
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This index is mutable.
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
        // Process Equal queries in the same was as HashIndex...
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
        // Process LessThan, GreaterThan and Between queries as follows...
        final IndexRangeLookupFunction<O> lookupFunction;
        if (queryClass.equals(LessThan.class)) {
            final LessThan<O, A> lessThan = (LessThan<O, A>) query;
            lookupFunction = new IndexRangeLookupFunction<O>(query, false, true) {
                @Override
                public Iterable<StoredResultSet<O>> perform() {
                    return indexMap.headMap(
                            getQuantizedValue(lessThan.getValue()),
                            lessThan.isValueInclusive()
                    ).values();
                }
            };
        }
        else if (queryClass.equals(GreaterThan.class)) {
            final GreaterThan<O, A> greaterThan = (GreaterThan<O, A>) query;
            lookupFunction = new IndexRangeLookupFunction<O>(query, true, false) {
                @Override
                public Iterable<StoredResultSet<O>> perform() {
                    return indexMap.tailMap(
                            getQuantizedValue(greaterThan.getValue()),
                            greaterThan.isValueInclusive()
                    ).values();
                }
            };
        }
        else if (queryClass.equals(Between.class)) {
            final Between<O, A> between = (Between<O, A>) query;
            lookupFunction = new IndexRangeLookupFunction<O>(query, true, true) {
                @Override
                public Iterable<StoredResultSet<O>> perform() {
                    return indexMap.subMap(
                            getQuantizedValue(between.getLowerValue()),
                            between.isLowerInclusive(),
                            getQuantizedValue(between.getUpperValue()),
                            between.isUpperInclusive()
                    ).values();
                }
            };
        }
        else {
            throw new IllegalStateException("Unsupported query: " + query);
        }

        // Fetch results using the supplied function.
        // This should return a Collection which is actually just a view onto the index
        // which presents a collection of sets containing values selected by the function...
        @SuppressWarnings({"unchecked"})
        Iterable<ResultSet<O>> results = (Iterable<ResultSet<O>>) lookupFunction.perform();

        // Add filtering for quantization (implemented by subclass supporting a Quantizer, a no-op in this class)...
        results = addFilteringForQuantization(results, lookupFunction);

        // If a query option specifying logical deduplication is supplied return ResultSetUnion,
        // otherwise return ResultSetUnionAll.
        // We can avoid deduplication if the index is built on a SimpleAttribute however,
        // because the same object could not exist in more than one StoredResultSet...
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
     * An interface which when implemented encapsulates the logic to retrieve zero or more {@link ResultSet}s from an
     * index, where the logic for selecting these {@link ResultSet}s is implemented by the {@link #perform()} method.
     *
     * @param <O> The type of object stored in the result sets
     */
    protected abstract class IndexRangeLookupFunction<O> {
        protected final boolean filterFirstResultSet;
        protected final boolean filterLastResultSet;
        protected final Query<O> query;

        /**
         * The following arguments are useful when the index uses a {@link com.googlecode.cqengine.quantizer.Quantizer},
         * and so the stored sets of objects might need to be filtered on retrieval.
         *
         * @param query The query against which objects should be filtered
         * @param filterFirstResultSet True if the first {@link StoredResultSet} returned by the function should be
         * filtered to return only objects which actually match the query - typically true for {@link GreaterThan} and
         * {@link Between} queries and false for {@link LessThan} queries
         * @param filterLastResultSet  True if the last {@link StoredResultSet} returned by the function should be
         * filtered to return only objects which actually match the query - typically true for {@link LessThan} and
         * {@link Between} queries and false for {@link GreaterThan} queries
         */
        protected IndexRangeLookupFunction(Query<O> query, boolean filterFirstResultSet, boolean filterLastResultSet) {
            this.query = query;
            this.filterFirstResultSet = filterFirstResultSet;
            this.filterLastResultSet = filterLastResultSet;
        }

        protected abstract Iterable<? extends ResultSet<O>> perform();
    }

    // ---------- Hook methods which can be overridden by subclasses using a Quantizer ----------

    /**
     * A no-op method which may be overridden by subclasses which use a
     * {@link com.googlecode.cqengine.quantizer.Quantizer}.
     * <p/>
     * <b>This default implementation simply returns the given attribute value unmodified.</b>
     * <p/>
     * Returns an {@link Iterable} which is similar to the one supplied, but which transparently wraps the first
     * {@link ResultSet} and/or the last {@link ResultSet} returned by the supplied {@link Iterable} in a
     * {@link QuantizedResultSet}.
     * <p/>
     * A {@link QuantizedResultSet} transparently filters objects in the wrapped {@link ResultSet} to ensure that
     * they match the given {@link Query}. This is necessary when the index uses a
     * {@link com.googlecode.cqengine.quantizer.Quantizer}, where objects having several adjacent attribute values
     * will be stored together in the same {@link StoredResultSet} in the index.
     * <p/>
     * For <i>range queries</i> ({@link LessThan}, {@link GreaterThan}, {@link Between}), the {@link StoredResultSet}s
     * in an index using a quantizer at the keys in the index referenced by the query, are not guaranteed to only
     * contain objects matching the values in the query, due to objects being mixed with their adjacent counterparts.
     * Therefore it is necessary to filter the {@link ResultSet} at <i>either end</i> of the range.
     *
     * @param resultSets {@link ResultSet}s stored in the index which were found to match the range query, in ascending
     * order of their associated keys
     * @param lookupFunction Contains parameters specifying whether the first or last {@link ResultSet}s should be
     * wrapped in a {@link QuantizedResultSet}, and also encapsulates the {@link Query} against which objects should
     * be filtered
     *
     * @return An {@link Iterable} optionally with the first and/or last {@link ResultSet}s wrapped in
     * {@link QuantizedResultSet}s
     */
    protected Iterable<ResultSet<O>> addFilteringForQuantization(final Iterable<ResultSet<O>> resultSets, final IndexRangeLookupFunction<O> lookupFunction) {
        return resultSets;
    }

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


    // ---------- Static factory methods to create NavigableIndexes ----------

    /**
     * Creates a new {@code NavigableIndex} on the given attribute. The attribute can be a {@link SimpleAttribute} or a
     * {@link com.googlecode.cqengine.attribute.MultiValueAttribute}, as long as the type of the attribute referenced
     * implements {@link Comparable}.
     * <p/>
     * @param attribute The attribute on which the index will be built, a {@link SimpleAttribute} or a
     * {@link com.googlecode.cqengine.attribute.MultiValueAttribute} where the type of the attribute referenced
     * implements {@link Comparable}
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A new HashIndex which will build an index on this attribute
     */
    public static <A extends Comparable<A>, O> NavigableIndex<A, O> onAttribute(Attribute<O, A> attribute) {
        return onAttribute(new DefaultIndexMapFactory<A, O>(), new DefaultValueSetFactory<O>(), attribute);
    }

    /**
     * Creates a new {@code NavigableIndex} on the given attribute. The attribute can be a {@link SimpleAttribute} or a
     * {@link com.googlecode.cqengine.attribute.MultiValueAttribute}, as long as the type of the attribute referenced
     * implements {@link Comparable}.
     * <p/>
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param valueSetFactory A factory used to create sets to store values in the index
     * @param attribute The attribute on which the index will be built, a {@link SimpleAttribute} or a
     * {@link com.googlecode.cqengine.attribute.MultiValueAttribute} where the type of the attribute referenced
     * implements {@link Comparable}
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A new HashIndex which will build an index on this attribute
     */
    public static <A extends Comparable<A>, O> NavigableIndex<A, O> onAttribute(Factory<ConcurrentNavigableMap<A, StoredResultSet<O>>> indexMapFactory, Factory<StoredResultSet<O>> valueSetFactory, Attribute<O, A> attribute) {
        return new NavigableIndex<A, O>(indexMapFactory, valueSetFactory, attribute);
    }

    /**
     * Creates a {@link NavigableIndex} on the given attribute using the given {@link Quantizer}.
     * <p/>
     * @param quantizer A {@link Quantizer} to use in this index
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link NavigableIndex} on the given attribute using the given {@link Quantizer}
     */
    public static <A extends Comparable<A>, O> NavigableIndex<A, O> withQuantizerOnAttribute(final Quantizer<A> quantizer, Attribute<O, A> attribute) {
        return withQuantizerOnAttribute(new DefaultIndexMapFactory<A, O>(), new DefaultValueSetFactory<O>(), quantizer, attribute);
    }

    /**
     * Creates a {@link NavigableIndex} on the given attribute using the given {@link Quantizer}.
     * <p/>
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param valueSetFactory A factory used to create sets to store values in the index
     * @param quantizer A {@link Quantizer} to use in this index
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link NavigableIndex} on the given attribute using the given {@link Quantizer}
     */
    public static <A extends Comparable<A>, O> NavigableIndex<A, O> withQuantizerOnAttribute(Factory<ConcurrentNavigableMap<A, StoredResultSet<O>>> indexMapFactory, Factory<StoredResultSet<O>> valueSetFactory, final Quantizer<A> quantizer, Attribute<O, A> attribute) {
        return new NavigableIndex<A, O>(indexMapFactory, valueSetFactory, attribute) {

            // ---------- Override the hook methods related to Quantizer ----------

            @Override
            protected Iterable<ResultSet<O>> addFilteringForQuantization(final Iterable<ResultSet<O>> resultSets, final IndexRangeLookupFunction<O> lookupFunction) {
                if (!lookupFunction.filterFirstResultSet && !lookupFunction.filterLastResultSet) {
                    // No filtering required, return the same iterable...
                    return resultSets;
                }
                return new Iterable<ResultSet<O>>() {
                    @Override
                    public Iterator<ResultSet<O>> iterator() {
                        return new UnmodifiableIterator<ResultSet<O>>() {
                            Iterator<? extends ResultSet<O>> resultSetsIterator = resultSets.iterator();
                            boolean firstResultSet = true;

                            @Override
                            public boolean hasNext() {
                                return resultSetsIterator.hasNext();
                            }

                            @Override
                            public ResultSet<O> next() {
                                ResultSet<O> rs = resultSetsIterator.next();
                                if (lookupFunction.filterFirstResultSet && firstResultSet) {
                                    // Filtering is enabled for first ResultSet, and we are processing first ResultSet.
                                    // Wrap it in QuantizedResultSet...
                                    firstResultSet = false;
                                    return new QuantizedResultSet<O>(rs, lookupFunction.query);
                                }
                                else if (!lookupFunction.filterLastResultSet || resultSetsIterator.hasNext()) {
                                    // Filtering is disabled for last ResultSet,
                                    // or we are processing a ResultSet which is neither first nor last.
                                    // Don't wrap it...
                                    return rs;
                                }
                                else {
                                    // Filtering is enabled for last ResultSet and we are processing last ResultSet.
                                    // Wrap it in QuantizedResultSet...
                                    return new QuantizedResultSet<O>(rs, lookupFunction.query);
                                }
                            }
                        };
                    }
                };
            }

            @Override
            protected A getQuantizedValue(A attributeValue) {
                return quantizer.getQuantizedValue(attributeValue);
            }

            @Override
            protected ResultSet<O> filterForQuantization(ResultSet<O> storedResultSet, Query<O> query) {
                return new QuantizedResultSet<O>(storedResultSet, query);
            }
        };
    }

    /**
     * Creates an index map using default settings.
     */
    public static class DefaultIndexMapFactory<A, O> implements Factory<ConcurrentNavigableMap<A, StoredResultSet<O>>> {
        @Override
        public ConcurrentNavigableMap<A, StoredResultSet<O>> create() {
            return new ConcurrentSkipListMap<A, StoredResultSet<O>>();
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
