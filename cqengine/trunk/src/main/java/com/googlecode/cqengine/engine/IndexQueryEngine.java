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
package com.googlecode.cqengine.engine;

import com.googlecode.concurrenttrees.common.LazyIterator;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.CloseableQueryResources;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsIndex;
import com.googlecode.cqengine.index.support.ResourceIndex;
import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.index.compound.support.CompoundAttribute;
import com.googlecode.cqengine.index.compound.support.CompoundQuery;
import com.googlecode.cqengine.index.fallback.FallbackIndex;
import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.index.support.CloseableSet;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.LogicalQuery;
import com.googlecode.cqengine.query.logical.Not;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.query.option.*;
import com.googlecode.cqengine.query.simple.Between;
import com.googlecode.cqengine.query.simple.GreaterThan;
import com.googlecode.cqengine.query.simple.LessThan;
import com.googlecode.cqengine.query.simple.SimpleQuery;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.closeable.CloseableResultSet;
import com.googlecode.cqengine.resultset.connective.ResultSetDifference;
import com.googlecode.cqengine.resultset.connective.ResultSetIntersection;
import com.googlecode.cqengine.resultset.connective.ResultSetUnion;
import com.googlecode.cqengine.resultset.connective.ResultSetUnionAll;
import com.googlecode.cqengine.resultset.filter.FilteringResultSet;
import com.googlecode.cqengine.resultset.iterator.ConcatenatingIterable;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;
import com.googlecode.cqengine.resultset.order.AttributeOrdersComparator;
import com.googlecode.cqengine.resultset.order.MaterializingOrderedResultSet;
import com.googlecode.cqengine.resultset.order.MaterializingResultSet;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * The main component of {@code CQEngine} - maintains a set of indexes on a collection and accepts queries which
 * it performs and optimizes for those indexes.
 *
 * @author Niall Gallagher
 */
public class IndexQueryEngine<O> implements QueryEngineInternal<O> {

    private volatile Set<O> collection = Collections.emptySet();

    // Map of attributes to set of indexes on that attribute...
    private final ConcurrentMap<Attribute<O, ?>, Set<Index<O>>> attributeIndexes = new ConcurrentHashMap<Attribute<O, ?>, Set<Index<O>>>();
    // Map of CompoundAttributes to compound index on that compound attribute...
    private final ConcurrentMap<CompoundAttribute<O>, CompoundIndex<O>> compoundIndexes = new ConcurrentHashMap<CompoundAttribute<O>, CompoundIndex<O>>();
    // Map of queries to standing query index on that query...
    private final ConcurrentMap<Query<O>, StandingQueryIndex<O>> standingQueryIndexes = new ConcurrentHashMap<Query<O>, StandingQueryIndex<O>>();
    // Fallback index (handles queries which other indexes don't common)...
    private final FallbackIndex<O> fallbackIndex = new FallbackIndex<O>();
    // Initially true, updated as indexes are added in addIndex()...
    private volatile boolean allIndexesAreMutable = true;
    // This will be set to true if any index is added which requires
    // the outer ResultSet.close() method to close resources added to QueryOptions...
    private volatile boolean useCloseableResultSet = false;

    public IndexQueryEngine() {
    }

    @Override
    public void init(final Set<O> collection, final QueryOptions queryOptions) {
        this.collection = collection;
        forEachIndexDo(new IndexOperation<O>() {
            @Override
            public boolean perform(Index<O> index) {
                index.init(collection, queryOptions);
                return true;
            }
        });
    }

    // -------------------- Methods for adding indexes --------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIndex(Index<O> index) {
        addIndex(index, QueryFactory.noQueryOptions());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIndex(Index<O> index, QueryOptions queryOptions) {
        if (index instanceof StandingQueryIndex) {
            allIndexesAreMutable = allIndexesAreMutable && index.isMutable();
            @SuppressWarnings({"unchecked"})
            StandingQueryIndex<O> standingQueryIndex = (StandingQueryIndex<O>) index;
            addStandingQueryIndex(standingQueryIndex, standingQueryIndex.getStandingQuery(), queryOptions);
        }
        else if (index instanceof CompoundIndex) {
            allIndexesAreMutable = allIndexesAreMutable && index.isMutable();
            @SuppressWarnings({"unchecked"})
            CompoundIndex<O> compoundIndex = (CompoundIndex<O>) index;
            CompoundAttribute<O> compoundAttribute = compoundIndex.getAttribute();
            addCompoundIndex(compoundIndex, compoundAttribute, queryOptions);
        }
        else if (index instanceof AttributeIndex) {
            allIndexesAreMutable = allIndexesAreMutable && index.isMutable();
            useCloseableResultSet = index instanceof ResourceIndex || useCloseableResultSet;
            @SuppressWarnings({"unchecked"})
            AttributeIndex<?, O> attributeIndex = (AttributeIndex<?, O>) index;
            addAttributeIndex(attributeIndex, queryOptions);
        }
        else {
            throw new IllegalStateException("Unexpected type of index: " + (index == null ? null : index.getClass().getName()));
        }
    }

    /**
     * Adds an {@link AttributeIndex}.
     * @param attributeIndex The index to add
     * @param <A> The type of objects indexed
     */
    <A> void addAttributeIndex(AttributeIndex<A, O> attributeIndex, QueryOptions queryOptions) {
        Attribute<O, A> attribute = attributeIndex.getAttribute();
        Set<Index<O>> indexesOnThisAttribute = attributeIndexes.get(attribute);
        if (indexesOnThisAttribute == null) {
            indexesOnThisAttribute = Collections.newSetFromMap(new ConcurrentHashMap<Index<O>, Boolean>());
            attributeIndexes.put(attribute, indexesOnThisAttribute);
        }
        indexesOnThisAttribute.add(attributeIndex);
        attributeIndex.init(collection, queryOptions);
    }

    /**
     * Adds a {@link StandingQueryIndex}.
     * @param standingQueryIndex The index to add
     * @param standingQuery The query on which the index is based
     */
    void addStandingQueryIndex(StandingQueryIndex<O> standingQueryIndex, Query<O> standingQuery, QueryOptions queryOptions) {
        StandingQueryIndex<O> existingIndex = standingQueryIndexes.putIfAbsent(standingQuery, standingQueryIndex);
        if (existingIndex != null) {
            throw new IllegalStateException("An index has already been added for standing query: " + standingQuery);
        }
        standingQueryIndex.init(collection, queryOptions);
    }

    /**
     * Adds a {@link CompoundIndex}.
     * @param compoundIndex The index to add
     * @param compoundAttribute The compound attribute on which the index is based
     */
    void addCompoundIndex(CompoundIndex<O> compoundIndex, CompoundAttribute<O> compoundAttribute, QueryOptions queryOptions) {
        CompoundIndex<O> existingIndex = compoundIndexes.putIfAbsent(compoundAttribute, compoundIndex);
        if (existingIndex != null) {
            throw new IllegalStateException("An index has already been added for compound attribute: " + compoundAttribute);
        }
        compoundIndex.init(collection, queryOptions);
    }

    // -------------------- Method for accessing indexes --------------------


    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Index<O>> getIndexes() {
        List<Index<O>> indexes = new ArrayList<Index<O>>();
        for (Set<Index<O>> attributeIndexes : this.attributeIndexes.values()) {
            indexes.addAll(attributeIndexes);
        }
        indexes.addAll(this.compoundIndexes.values());
        indexes.addAll(this.standingQueryIndexes.values());
        return indexes;
    }

    /**
     * Returns an {@link Iterable} over all indexes which have been added on the given attribute, including the
     * {@link FallbackIndex} which is implicitly available on all attributes.
     *
     * @param attribute The relevant attribute
     * @return All indexes which have been added on the given attribute, including the {@link FallbackIndex}
     */
    Iterable<Index<O>> getIndexesOnAttribute(Attribute<O, ?> attribute) {
        final Set<Index<O>> indexesOnAttribute = attributeIndexes.get(attribute);
        if (indexesOnAttribute == null || indexesOnAttribute.isEmpty()) {
            // If no index is registered for this attribute, return the fallback index...
            return Collections.<Index<O>>singleton(this.fallbackIndex);
        }
        // Return an Iterable over the registered indexes and the fallback index...
        List<Iterable<Index<O>>> iterables = new ArrayList<Iterable<Index<O>>>(2);
        iterables.add(indexesOnAttribute);
        iterables.add(Collections.<Index<O>>singleton(fallbackIndex));
        return new ConcatenatingIterable<Index<O>>(iterables);
    }

    /**
     * Returns the entire collection wrapped as a {@link ResultSet}, with retrieval cost {@link Integer#MAX_VALUE}.
     * <p/>
     * Merge cost is the size of the collection.
     *
     * @return The entire collection wrapped as a {@link ResultSet}, with retrieval cost {@link Integer#MAX_VALUE}
     */
    ResultSet<O> getEntireCollectionAsResultSet() {
        return new StoredSetBasedResultSet<O>(this.collection, Integer.MAX_VALUE);
    }

    /**
     * Returns a {@link ResultSet} from the index with the lowest retrieval cost which supports the given query.
     * <p/>
     * For a definition of retrieval cost see {@link ResultSet#getRetrievalCost()}.
     *
     * @param query The query which refers to an attribute
     * @param queryOptions Optional parameters for the query
     * @return A {@link ResultSet} from the index with the lowest retrieval cost which supports the given query
     */
    <A> ResultSet<O> getResultSetWithLowestRetrievalCost(SimpleQuery<O, A> query, QueryOptions queryOptions) {
        Iterable<Index<O>> indexesOnAttribute = getIndexesOnAttribute(query.getAttribute());

        // Choose the index with the lowest retrieval cost for this query...
        ResultSet<O> lowestCostResultSet = null;
        int lowestRetrievalCost = 0;
        for (Index<O> index : indexesOnAttribute) {
            if (index.supportsQuery(query)) {
                ResultSet<O> thisIndexResultSet = index.retrieve(query, queryOptions);
                int thisIndexRetrievalCost = thisIndexResultSet.getRetrievalCost();
                if (lowestCostResultSet == null || thisIndexRetrievalCost < lowestRetrievalCost) {
                    lowestCostResultSet = thisIndexResultSet;
                    lowestRetrievalCost = thisIndexRetrievalCost;
                }
            }
        }

        if (lowestCostResultSet == null) {
            // This should never happen (would indicate a bug);
            // the fallback index should have been selected in worst case...
            throw new IllegalStateException("Failed to locate an index supporting query: " + query);
        }
        return lowestCostResultSet;
    }

    // -------------------- Methods for query processing --------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(Query<O> query) {
        return retrieveRecursive(query, QueryFactory.noQueryOptions());
    }

    /**
     * {@inheritDoc}
     */
    // Implementation note: this methods actually just pre-processes QueryOption arguments and then delegates
    // to the #retrieveRecursive() method.
    @Override
    public ResultSet<O> retrieve(final Query<O> query, final QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        OrderByOption<O> orderByOption = (OrderByOption<O>) queryOptions.get(OrderByOption.class);

        ResultSet<O> resultSet;
        // Check if we can use an index to drive the sort...
        if (OrderingStrategyOption.isIndex(queryOptions)) {
            resultSet = retrieveWithIndexOrdering(query, queryOptions, orderByOption);
        }
        else {
            // Retrieve results...
            resultSet = retrieveRecursive(query, queryOptions);

            // Check if we need to wrap ResultSet to order and/or deduplicate results (deduplicate using MATERIAIZE rather
            // than LOGICAL_ELIMINATION strategy)...
            if (orderByOption != null) {
                // An OrderByOption was specified, wrap the results in an MaterializingOrderedResultSet,
                // which will both deduplicate and sort results. O(n^2 log(n)) time complexity to subsequently iterate...
                Comparator<O> comparator = new AttributeOrdersComparator<O>(orderByOption.getAttributeOrders(), queryOptions);
                resultSet = new MaterializingOrderedResultSet<O>(resultSet, comparator);
            } else if (DeduplicationOption.isMaterialize(queryOptions)) {
                // A DeduplicationOption was specified, wrap the results in an MaterializingResultSet,
                // which will deduplicate (but not sort) results. O(n) time complexity to subsequently iterate...
                resultSet = new MaterializingResultSet<O>(resultSet);
            }
        }

        // Return the results...
        if (useCloseableResultSet) {
            return new CloseableResultSet<O>(resultSet, queryOptions) {
                @Override
                public void close() {
                    super.close();
                    CloseableQueryResources closeableQueryResources = queryOptions.get(CloseableQueryResources.class);
                    if (closeableQueryResources != null) {
                        closeableQueryResources.closeAll();
                    }
                }
            };
        }
        else {
            return resultSet;
        }
    }

    /**
     * Use an index to order results.
     */
    ResultSet<O> retrieveWithIndexOrdering(final Query<O> query, final QueryOptions queryOptions, final OrderByOption<O> orderByOption) {
        final List<AttributeOrder<O>> attributeOrders = orderByOption.getAttributeOrders();

        if (attributeOrders.size() != 1) {
            throw new IllegalStateException("Ordering strategy '" + queryOptions.get(OrderingStrategyOption.class) + "' is not compatible with compound orderBy clause '" + orderByOption + "'");
        }
        AttributeOrder<O> order = attributeOrders.iterator().next();
        final Attribute<O, ? extends Comparable> attribute = order.getAttribute();
        if (!(attribute instanceof SimpleAttribute)) {
            throw new IllegalStateException("Ordering strategy '" + queryOptions.get(OrderingStrategyOption.class) + "' is not compatible with non-simple attribute in orderBy clause '" + orderByOption + "'");
        }
        @SuppressWarnings("unchecked")
        final SimpleAttribute<O, Comparable> simpleAttribute = (SimpleAttribute<O, Comparable>)attribute;
        SortedKeyStatisticsIndex<?, O> keyStatisticsIndex = null;
        for (Index<O> index : this.getIndexesOnAttribute(simpleAttribute)) {
            if (index instanceof SortedKeyStatisticsIndex) {
                keyStatisticsIndex = (SortedKeyStatisticsIndex<?, O>)index;
                break;
            }
        }
        if (keyStatisticsIndex == null) {
            throw new IllegalStateException("Ordering strategy '" + queryOptions.get(OrderingStrategyOption.class) + "' requires an implementation of SortedKeyStatisticsIndex, on the attribute in orderBy clause '" + orderByOption + "'");
        }
        final SortedKeyStatisticsIndex<? extends Comparable, O> keyStatisticsIndexRef = keyStatisticsIndex;

        final RangeBounds<?> rangeBoundsFromQuery = getBoundsFromQuery(query, simpleAttribute);
        final boolean descending = order.isDescending();

        // Ensure that at the end of processing the request, that we close any resources we opened...
        final CloseableQueryResources closeableQueryResources = CloseableQueryResources.from(queryOptions);
        final CloseableSet resultSetResourcesToClose = new CloseableSet();
        closeableQueryResources.add(resultSetResourcesToClose);

        return new ResultSetUnionAll<O>(new Iterable<ResultSet<O>>() {
            @Override
            public Iterator<ResultSet<O>> iterator() {
                return new LazyIterator<ResultSet<O>>() {
                    Comparable previousKey = null;
                    boolean lastKeyProcessed = false;

                    final CloseableIterator<? extends Comparable> keysInRange = getKeysInRange(keyStatisticsIndexRef, rangeBoundsFromQuery, descending, queryOptions);
                    // Ensure this CloseableIterator gets closed...
                    {resultSetResourcesToClose.add(keysInRange);}

                    @Override
                    protected ResultSet<O> computeNext() {
                        Comparable currentKey = null;
                        if (!keysInRange.hasNext()) {
                            if (lastKeyProcessed) {
                                CloseableQueryResources.closeQuietly(keysInRange);
                                return endOfData();
                            }
                            lastKeyProcessed = true;
                        }
                        else {
                            currentKey = keysInRange.next();
                        }
                        Query<O> rangeRestriction = getRangeRestriction(descending, simpleAttribute, currentKey, previousKey);

                        Query<O> restrictedQuery = and(query, rangeRestriction);

                        ResultSet<O> resultsForThisKey = retrieveRecursive(restrictedQuery, queryOptions);

                        // We must also sort results within each bucket, in case the index is quantized,
                        // or in case there are additional sort orders after the first one...
                        Comparator<O> comparator = new AttributeOrdersComparator<O>(attributeOrders, queryOptions);
                        resultsForThisKey = new MaterializingOrderedResultSet<O>(resultsForThisKey, comparator);

                        previousKey = currentKey;
                        return resultsForThisKey;
                    }
                };
            }
        });
    }

    static <O> Query<O> getRangeRestriction(boolean descending, SimpleAttribute<O, Comparable> attribute, Comparable currentKey, Comparable previousKey) {
        if (!descending) {
            return (previousKey == null)
                    ? lessThanOrEqualTo(attribute, currentKey)
                    : (currentKey == null)
                        ? greaterThan(attribute, previousKey)
                        : between(attribute, previousKey, false, currentKey, true);
        } else {
            return (previousKey == null)
                    ? greaterThan(attribute, currentKey)
                    : (currentKey == null)
                        ? lessThanOrEqualTo(attribute, previousKey)
                        : between(attribute, currentKey, false, previousKey, true);
        }
    }

    static <A extends Comparable<A>, O> CloseableIterator<A> getKeysInRange(SortedKeyStatisticsIndex<A, O> index, RangeBounds<?> queryBounds, boolean descending, QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        RangeBounds<A> typedBounds = (RangeBounds<A>) queryBounds;
        if (!descending) {
            return index.getDistinctKeys(
                    typedBounds.lowerBound, typedBounds.lowerInclusive,
                    typedBounds.upperBound, typedBounds.upperInclusive,
                    queryOptions
            ).iterator();
        }
        else {
            return index.getDistinctKeysDescending(
                    typedBounds.lowerBound, typedBounds.lowerInclusive,
                    typedBounds.upperBound, typedBounds.upperInclusive,
                    queryOptions
            ).iterator();
        }
    }

    static class RangeBounds<A extends Comparable<A>> {
        final A lowerBound;
        final boolean lowerInclusive;
        final A upperBound;
        final Boolean upperInclusive;

        public RangeBounds(A lowerBound, boolean lowerInclusive, A upperBound, Boolean upperInclusive) {
            this.lowerBound = lowerBound;
            this.lowerInclusive = lowerInclusive;
            this.upperBound = upperBound;
            this.upperInclusive = upperInclusive;
        }
    }

    static <A extends Comparable<A>, O> RangeBounds getBoundsFromQuery(Query<O> query, SimpleAttribute<O, A> attribute) {
        A lowerBound = null, upperBound = null;
        boolean lowerInclusive = false, upperInclusive = false;
        List<SimpleQuery<O, ?>> candidateRangeQueries = Collections.emptyList();
        if (query instanceof SimpleQuery) {
            candidateRangeQueries = Collections.<SimpleQuery<O, ?>>singletonList((SimpleQuery<O, ?>) query);
        }
        else if (query instanceof And) {
            And<O> and = (And<O>)query;
            if (and.hasSimpleQueries()) {
                candidateRangeQueries = and.getSimpleQueries();
            }
        }
        for (SimpleQuery<O, ?> candidate : candidateRangeQueries) {
            if (attribute.equals(candidate.getAttribute())) {
                if (candidate instanceof GreaterThan) {
                    @SuppressWarnings("unchecked")
                    GreaterThan<O, A> bound = (GreaterThan<O, A>) candidate;
                    lowerBound = bound.getValue();
                    lowerInclusive = bound.isValueInclusive();
                }
                else if (candidate instanceof LessThan) {
                    @SuppressWarnings("unchecked")
                    LessThan<O, A> bound = (LessThan<O, A>) candidate;
                    upperBound = bound.getValue();
                    upperInclusive = bound.isValueInclusive();
                }
                else if (candidate instanceof Between) {
                    @SuppressWarnings("unchecked")
                    Between<O, A> bound = (Between<O, A>) candidate;
                    lowerBound = bound.getLowerValue();
                    lowerInclusive = bound.isLowerInclusive();
                    upperBound = bound.getUpperValue();
                    upperInclusive = bound.isUpperInclusive();
                }
            }
        }
        return new RangeBounds<A>(lowerBound, lowerInclusive, upperBound, upperInclusive);
    }

    /**
     * Implements the bulk of query processing.
     * <p/>
     * This method is recursive.
     * <p/>
     * When processing a {@link SimpleQuery}, the method will simply delegate to the helper methods
     * {@link #retrieveIntersection(Collection, QueryOptions)} and {@link #retrieveUnion(Collection, QueryOptions)}
     * and will return their results.
     * <p/>
     * When processing a descendant of {@link CompoundQuery} ({@link And}, {@link Or}, {@link Not}), the method
     * will extract separately from those objects the child queries which are {@link SimpleQuery}s and the child
     * queries which are {@link CompoundQuery}s. It will call the helper methods above to process the child
     * {@link SimpleQuery}s, and the method will call itself recursively to process the child {@link CompoundQuery}s.
     * Once the method has results for both the child {@link SimpleQuery}s and the child {@link CompoundQuery}s, it
     * will return them in a {@link ResultSetIntersection}, {@link ResultSetUnion} or {@link ResultSetDifference}
     * object as appropriate for {@link And}, {@link Or}, {@link Not} respectively. These {@link ResultSet} objects
     * will take care of performing intersections or unions etc. on the child {@link ResultSet}s.
     *
     * @param query A query representing some assertions which sought objects must match
     * @param queryOptions Optional parameters for the query
     * supplied specifying strategy {@link DeduplicationStrategy#LOGICAL_ELIMINATION}
     * @return A {@link ResultSet} which provides objects matching the given query
     */
    ResultSet<O> retrieveRecursive(Query<O> query, final QueryOptions queryOptions) {

        // Check if we can process this query from a standing query index...
        StandingQueryIndex<O> standingQueryIndex = standingQueryIndexes.get(query);
        if (standingQueryIndex != null) {
            // No deduplication required for standing queries.
            return standingQueryIndex.retrieve(query, queryOptions);
        } // else no suitable standing query index exists, process the query normally...

        if (query instanceof SimpleQuery) {
            // No deduplication required for a single SimpleQuery.
            // Return the ResultSet from the index with the lowest retrieval cost which supports
            // this query and the attribute on which it is based...
            return getResultSetWithLowestRetrievalCost((SimpleQuery<O, ?>) query, queryOptions);
        }
        else if (query instanceof And) {
            final And<O> and = (And<O>) query;

            // Check if we can process this And query from a compound index...
            if (!compoundIndexes.isEmpty()) {
                // Compound indexes exist. Check if any can be used for this And query...
                CompoundQuery<O> compoundQuery = CompoundQuery.fromAndQueryIfSuitable(and);
                if (compoundQuery != null) {
                    CompoundIndex<O> compoundIndex = compoundIndexes.get(compoundQuery.getCompoundAttribute());
                    if (compoundIndex != null && compoundIndex.supportsQuery(compoundQuery)) {
                        // No deduplication required for retrievals from compound indexes.
                        return compoundIndex.retrieve(compoundQuery, queryOptions);
                    }
                }
            } // else no suitable compound index exists, process the And query normally...

            // No deduplication required for intersections.
            return new ResultSetIntersection<O>(new Iterable<ResultSet<O>>() {
                @Override
                public Iterator<ResultSet<O>> iterator() {
                    return new UnmodifiableIterator<ResultSet<O>>() {

                        boolean needToProcessSimpleQueries = and.hasSimpleQueries();
                        Iterator<LogicalQuery<O>> logicalQueriesIterator = and.getLogicalQueries().iterator();

                        @Override
                        public boolean hasNext() {
                            return needToProcessSimpleQueries || logicalQueriesIterator.hasNext();
                        }

                        @Override
                        public ResultSet<O> next() {
                            if (needToProcessSimpleQueries) {
                                needToProcessSimpleQueries = false;
                                // Retrieve results for simple queries from indexes...
                                return retrieveIntersection(and.getSimpleQueries(), queryOptions);
                            }
                            // Recursively call this method for logical queries...
                            return retrieveRecursive(logicalQueriesIterator.next(), queryOptions);
                        }
                    };
                }
            }, queryOptions);
        }
        else if (query instanceof Or) {
            final Or<O> or = (Or<O>) query;
            // If the Or query indicates child queries are disjoint,
            // ignore any instruction to perform deduplication in the queryOptions supplied...
            final QueryOptions queryOptionsForOrUnion;
            if (or.isDisjoint()) {
                // The Or query is disjoint, so there is no need to perform deduplication on its results.
                // Wrap the QueryOptions object in another which omits the DeduplicationOption if it is requested
                // when evaluating this Or statement...
                queryOptionsForOrUnion = new QueryOptions(queryOptions.getOptions()) {
                    @Override
                    public Object get(Object key) {
                        return DeduplicationOption.class.equals(key) ? null : super.get(key);
                    }
                };
            }
            else {
                // Use the supplied queryOptions...
                queryOptionsForOrUnion = queryOptions;
            }
            Iterable<ResultSet<O>> resultSetsToUnion = new Iterable<ResultSet<O>>() {
                @Override
                public Iterator<ResultSet<O>> iterator() {
                    return new UnmodifiableIterator<ResultSet<O>>() {

                        boolean needToProcessSimpleQueries = or.hasSimpleQueries();
                        Iterator<LogicalQuery<O>> logicalQueriesIterator = or.getLogicalQueries().iterator();

                        @Override
                        public boolean hasNext() {
                            return needToProcessSimpleQueries || logicalQueriesIterator.hasNext();
                        }

                        @Override
                        public ResultSet<O> next() {
                            if (needToProcessSimpleQueries) {
                                needToProcessSimpleQueries = false;
                                // Retrieve results for simple queries from indexes...
                                return retrieveUnion(or.getSimpleQueries(), queryOptionsForOrUnion);
                            }
                            // Recursively call this method for logical queries.
                            // Note we supply the original queryOptions for recursive calls...
                            return retrieveRecursive(logicalQueriesIterator.next(), queryOptions);
                        }
                    };
                }
            };
            // *** Deduplication can be required for unions... ***
            if (DeduplicationOption.isLogicalElimination(queryOptionsForOrUnion)) {
                return new ResultSetUnion<O>(resultSetsToUnion, queryOptions);
            }
            else {
                return new ResultSetUnionAll<O>(resultSetsToUnion);
            }
        }
        else if (query instanceof Not) {
            final Not<O> not = (Not<O>) query;
            // No deduplication required for negation (the entire collection is a Set, contains no duplicates).
            // Retrieve the ResultSet for the negated query, by calling this method recursively...
            ResultSet<O> resultSetToNegate = retrieveRecursive(not.getNegatedQuery(), queryOptions);
            // Return the negation of this result set, by subtracting it from the entire collection of objects...
            return new ResultSetDifference<O>(getEntireCollectionAsResultSet(), resultSetToNegate, queryOptions);
        }
        else {
            throw new IllegalStateException("Unexpected type of query object: " + getClassNameNullSafe(query));
        }
    }

    /**
     * Retrieves an intersection of the objects matching {@link SimpleQuery}s.
     * <p/>
     * <i>Definitions:
     * For a definition of <u>retrieval cost</u> see {@link ResultSet#getRetrievalCost()}.
     * For a definition of <u>merge cost</u> see {@link ResultSet#getMergeCost()}.
     * </i>
     * <p/>
     * The algorithm employed by this method is as follows.
     * <p/>
     * For each {@link SimpleQuery} supplied, retrieves a {@link ResultSet} for that {@link SimpleQuery}
     * from the index with the lowest <u>retrieval cost</u> which supports that {@link SimpleQuery}.
     * <p/>
     * The algorithm then determines the {@link ResultSet} with the <i>lowest</i> <u>merge cost</u>, and the
     * {@link SimpleQuery} which was associated with that {@link ResultSet}. It also assembles a list of the
     * <i>other</i> {@link SimpleQuery}s which had <i>more expensive</i> <u>merge costs</u>.
     * <p/>
     * The algorithm then returns a {@link FilteringResultSet} which iterates the {@link ResultSet} with the
     * <i>lowest</i> <u>merge cost</u>. During iteration, this {@link FilteringResultSet} calls a
     * {@link FilteringResultSet#isValid(Object, com.googlecode.cqengine.query.option.QueryOptions)} method for each object. This algorithm implements that method to
     * return true if the object matches all of the {@link SimpleQuery}s which had the <i>more expensive</i>
     * <u>merge costs</u>.
     * <p/>
     * As such the {@link ResultSet} which had the lowest merge cost drives the iteration.  Note therefore that this
     * method <i>does <u>not</u> perform set intersections in the conventional sense</i> (i.e. using
     * {@link Set#contains(Object)}). It has been tested empirically that it is usually cheaper to invoke
     * {@link Query#matches(Object, com.googlecode.cqengine.query.option.QueryOptions)} to test each object in the smallest set against queries which would match the
     * more expensive sets, rather than perform several hash lookups and equality tests between multiple sets.
     *
     * @param queries A collection of {@link SimpleQuery} objects to be retrieved and intersected
     * @param queryOptions Optional parameters for the query
     * @return A {@link ResultSet} which provides objects matching the intersection of results for each of the
     * {@link SimpleQuery}s
     */
    <A> ResultSet<O> retrieveIntersection(Collection<SimpleQuery<O, ?>> queries, QueryOptions queryOptions) {
        // Iterate through the query objects, store the ResultSet from the index with the lowest merge cost
        // in the following variables, and put the rest of the query in the moreExpensiveQueries list...
        SimpleQuery<O, A> lowestMergeCostQuery = null;
        ResultSet<O> lowestMergeCostResultSet = null;
        int lowestMergeCost = 0;
        final List<SimpleQuery<O, A>> moreExpensiveQueries = new ArrayList<SimpleQuery<O, A>>(queries.size() - 1);
        for (SimpleQuery query : queries) {
            // Work around type erasure...
            @SuppressWarnings({"unchecked"})
            SimpleQuery<O, A> queryTyped = (SimpleQuery<O, A>) query;

            ResultSet<O> resultSet = getResultSetWithLowestRetrievalCost(queryTyped, queryOptions);
            if (lowestMergeCostResultSet == null) {
                // First query/index evaluated. Store it as the lowest cost encountered so far...
                lowestMergeCostQuery = queryTyped;
                lowestMergeCostResultSet = resultSet;
                lowestMergeCost = resultSet.getMergeCost();
            }
            else {
                int mergeCost = resultSet.getMergeCost();
                if (mergeCost < lowestMergeCost) {
                    // We just found a merge cost lower than the one encountered previously.
                    // Add the previous lowest cost to to the set of more expensive query...
                    moreExpensiveQueries.add(lowestMergeCostQuery);
                    // Set our lowest merge cost to this new one...
                    lowestMergeCostQuery = queryTyped;
                    lowestMergeCostResultSet = resultSet;
                    lowestMergeCost = mergeCost;
                }
                else {
                    // This merge cost is greater than the one already stored.
                    // Add it to the more expensive set...
                    moreExpensiveQueries.add(queryTyped);
                }
            }
        }
        // At this point, we have identified the query with the lowest merge cost and stored a corresponding
        // ResultSet from its index, and we have put all of the other queries into a set of more expensive
        // queries.

        if (lowestMergeCostResultSet == null) {
            throw new IllegalStateException("The set of queries supplied was empty");
        }

        // If we had only one item of query, results from the one index are sufficient.
        // Return these results, no need for additional evaluation...
        if (moreExpensiveQueries.isEmpty()) {
            return lowestMergeCostResultSet;

        }

        // Return an iterator which will iterate these results (which match the query with the lowest merge
        // cost), and which will test each of these objects on-the-fly to determine if it matches the other more
        // expensive items of query...
        final ResultSet<O> lowestCostResultSetRef = lowestMergeCostResultSet;
        return new FilteringResultSet<O>(lowestCostResultSetRef, queryOptions) {
            @Override
            public boolean isValid(O object, QueryOptions queryOptions) {
                for (SimpleQuery<O, A> query : moreExpensiveQueries) {
                    if (!(query.matches(object, queryOptions))) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    /**
     * Retrieves a union of the objects matching {@link SimpleQuery}s.
     * <p/>
     * <i>Definitions:
     * For a definition of <u>retrieval cost</u> see {@link ResultSet#getRetrievalCost()}.
     * For a definition of <u>merge cost</u> see {@link ResultSet#getMergeCost()}.
     * </i>
     * <p/>
     * The algorithm employed by this method is as follows.
     * <p/>
     * For each {@link SimpleQuery} supplied, retrieves a {@link ResultSet} for that {@link SimpleQuery}
     * from the index with the lowest <u>retrieval cost</u> which supports that {@link SimpleQuery}.
     * <p/>
     * The method then returns these {@link ResultSet}s in either a {@link ResultSetUnion} or a
     * {@link ResultSetUnionAll} object, depending on whether {@code logicalDuplicateElimination} was specified
     * or not. These concatenate the wrapped {@link ResultSet}s when iterated. In the case of {@link ResultSetUnion},
     * this also ensures that duplicate objects are not returned more than once, by means of logical elimination via
     * set theory rather than maintaining a record of all objects iterated.
     *
     * @param queries A collection of {@link SimpleQuery} objects to be retrieved and unioned
     * @param queryOptions Optional parameters for the query
     * supplied specifying strategy {@link DeduplicationStrategy#LOGICAL_ELIMINATION}
     * @return A {@link ResultSet} which provides objects matching the union of results for each of the
     * {@link SimpleQuery}s
     */
    ResultSet<O> retrieveUnion(final Collection<SimpleQuery<O, ?>> queries, final QueryOptions queryOptions) {
        Iterable<ResultSet<O>> resultSetsToUnion = new Iterable<ResultSet<O>>() {
            @Override
            public Iterator<ResultSet<O>> iterator() {
                return new UnmodifiableIterator<ResultSet<O>>() {

                    Iterator<SimpleQuery<O, ?>> queriesIterator = queries.iterator();
                    @Override
                    public boolean hasNext() {
                        return queriesIterator.hasNext();
                    }

                    @Override
                    public ResultSet<O> next() {
                        return getResultSetWithLowestRetrievalCost(queriesIterator.next(), queryOptions);
                    }
                };
            }
        };
        // Perform deduplication as necessary...
        if (DeduplicationOption.isLogicalElimination(queryOptions)) {
            return new ResultSetUnion<O>(resultSetsToUnion, queryOptions);
        }
        else {
            return new ResultSetUnionAll<O>(resultSetsToUnion);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(final Collection<O> objects, final QueryOptions queryOptions) {
        ensureMutable();
        final FlagHolder modified = new FlagHolder();
        forEachIndexDo(new IndexOperation<O>() {
            @Override
            public boolean perform(Index<O> index) {
                modified.value |= index.addAll(objects, queryOptions);
                return true;
            }
        });
        return modified.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(final Collection<O> objects, final QueryOptions queryOptions) {
        ensureMutable();
        final FlagHolder modified = new FlagHolder();
        forEachIndexDo(new IndexOperation<O>() {
            @Override
            public boolean perform(Index<O> index) {
                modified.value |= index.removeAll(objects, queryOptions);
                return true;
            }
        });
        return modified.value;
    }

    /**
     * {@inheritDoc}
     * @param queryOptions
     */
    @Override
    public void clear(final QueryOptions queryOptions) {
        ensureMutable();
        forEachIndexDo(new IndexOperation<O>() {
            @Override
            public boolean perform(Index<O> index) {
                index.clear(queryOptions);
                return true;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMutable() {
        return allIndexesAreMutable;
    }

    /**
     * Throws an {@link IllegalStateException} if all indexes are not mutable.
     */
    void ensureMutable() {
        if (!allIndexesAreMutable) {
            throw new IllegalStateException("Cannot modify indexes, an immutable index has been added.");
        }
    }

    /**
     * A closure/callback object invoked for each index in turn by method
     * {@link IndexQueryEngine#forEachIndexDo(IndexOperation)}.
     */
    interface IndexOperation<O> {
        /**
         * @param index The index to be processed
         * @return Operation can return true to continue iterating through all indexes, false to stop iterating
         */
        boolean perform(Index<O> index);
    }

    /**
     * Iterates through all indexes and for each index invokes the given index operation. If the operation returns
     * false for any index, stops iterating and returns false. If the operation returns true for every index,
     * returns true after all indexes have been iterated.
     * @param indexOperation The operation to perform on each index.
     * @return true if the operation returned true for all indexes and so all indexes were iterated, false if the
     * operation returned false for any index and so iteration was stopped
     */
    boolean forEachIndexDo(IndexOperation<O> indexOperation) {
        // Perform the operation on attribute indexes...
        Iterable<Index<O>> attributeIndexes = new ConcatenatingIterable<Index<O>>(this.attributeIndexes.values());
        for (Index<O> index : attributeIndexes) {
            boolean continueIterating = indexOperation.perform(index);
            if (!continueIterating) {
                return false;
            }
        }
        // Perform the operation on compound indexes...
        Iterable<? extends Index<O>> compoundIndexes = this.compoundIndexes.values();
        for (Index<O> index : compoundIndexes) {
            boolean continueIterating = indexOperation.perform(index);
            if (!continueIterating) {
                return false;
            }
        }
        // Perform the operation on standing query indexes...
        Iterable<? extends Index<O>> standingQueryIndexes = this.standingQueryIndexes.values();
        for (Index<O> index : standingQueryIndexes) {
            boolean continueIterating = indexOperation.perform(index);
            if (!continueIterating) {
                return false;
            }
        }
        // Perform the operation on the fallback index...
        return indexOperation.perform(fallbackIndex);
    }

    static class FlagHolder {
        boolean value = false;
    }

    static String getClassNameNullSafe(Object object) {
        return object == null ? null : object.getClass().getName();
    }
}
