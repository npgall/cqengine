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
package com.googlecode.cqengine.engine;

import com.googlecode.concurrenttrees.common.LazyIterator;
import com.googlecode.cqengine.attribute.*;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.sqlite.IdentityAttributeIndex;
import com.googlecode.cqengine.index.sqlite.SQLiteIdentityIndex;
import com.googlecode.cqengine.index.sqlite.SimplifiedSQLiteIndex;
import com.googlecode.cqengine.index.support.*;
import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.index.compound.support.CompoundAttribute;
import com.googlecode.cqengine.index.compound.support.CompoundQuery;
import com.googlecode.cqengine.index.fallback.FallbackIndex;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectStoreResultSet;
import com.googlecode.cqengine.persistence.support.sqlite.SQLiteObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.LogicalQuery;
import com.googlecode.cqengine.query.logical.Not;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.query.option.*;
import com.googlecode.cqengine.query.simple.*;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.closeable.CloseableResultSet;
import com.googlecode.cqengine.resultset.common.CostCachingResultSet;
import com.googlecode.cqengine.resultset.connective.ResultSetDifference;
import com.googlecode.cqengine.resultset.connective.ResultSetIntersection;
import com.googlecode.cqengine.resultset.connective.ResultSetUnion;
import com.googlecode.cqengine.resultset.connective.ResultSetUnionAll;
import com.googlecode.cqengine.resultset.filter.MaterializedDeduplicatedIterator;
import com.googlecode.cqengine.resultset.filter.FilteringIterator;
import com.googlecode.cqengine.resultset.filter.FilteringResultSet;
import com.googlecode.cqengine.resultset.iterator.ConcatenatingIterable;
import com.googlecode.cqengine.resultset.iterator.ConcatenatingIterator;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;
import com.googlecode.cqengine.resultset.order.AttributeOrdersComparator;
import com.googlecode.cqengine.resultset.order.MaterializedDeduplicatedResultSet;
import com.googlecode.cqengine.resultset.order.MaterializedOrderedResultSet;
import com.googlecode.cqengine.index.support.CloseableRequestResources.CloseableResourceGroup;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.option.EngineFlags.INDEX_ORDERING_ALLOW_FAST_ORDERING_OF_MULTI_VALUED_ATTRIBUTES;
import static com.googlecode.cqengine.query.option.EngineFlags.PREFER_INDEX_MERGE_STRATEGY;
import static com.googlecode.cqengine.query.option.FlagsEnabled.isFlagEnabled;
import static com.googlecode.cqengine.resultset.iterator.IteratorUtil.concatenate;
import static com.googlecode.cqengine.resultset.iterator.IteratorUtil.groupAndSort;

/**
 * The main component of {@code CQEngine} - maintains a set of indexes on a collection and accepts queries which
 * it performs and optimizes for those indexes.
 *
 * @author Niall Gallagher
 */
public class CollectionQueryEngine<O> implements QueryEngineInternal<O> {

    // A key used to store the root query in the QueryOptions, so it may be accessed by partial indexes...
    public static final String ROOT_QUERY = "ROOT_QUERY";

    private volatile Persistence<O, ? extends Comparable> persistence;
    private volatile ObjectStore<O> objectStore;

    // Map of attributes to set of indexes on that attribute...
    private final ConcurrentMap<Attribute<O, ?>, Set<Index<O>>> attributeIndexes = new ConcurrentHashMap<Attribute<O, ?>, Set<Index<O>>>();
    private final ConcurrentMap<Attribute<O, ?>, Index<O>> uniqueIndexes = new ConcurrentHashMap<Attribute<O, ?>, Index<O>>();
    // Map of CompoundAttributes to compound index on that compound attribute...
    private final ConcurrentMap<CompoundAttribute<O>, CompoundIndex<O>> compoundIndexes = new ConcurrentHashMap<CompoundAttribute<O>, CompoundIndex<O>>();
    // Map of queries to standing query index on that query...
    private final ConcurrentMap<Query<O>, Index<O>> standingQueryIndexes = new ConcurrentHashMap<Query<O>, Index<O>>();
    // Fallback index (handles queries which other indexes don't support)...
    private final FallbackIndex<O> fallbackIndex = new FallbackIndex<O>();
    // Updated as indexes are added or removed, this is used by the isMutable() method...
    private final Set<Index<O>> immutableIndexes = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public CollectionQueryEngine() {
    }

    @Override
    public void init(final ObjectStore<O> objectStore, final QueryOptions queryOptions) {
        this.objectStore = objectStore;
        @SuppressWarnings("unchecked")
        Persistence<O, ? extends Comparable> persistenceFromQueryOptions = getPersistenceFromQueryOptions(queryOptions);
        this.persistence = persistenceFromQueryOptions;
        if (objectStore instanceof SQLiteObjectStore) {
            // If the collection is backed by a SQLiteObjectStore, add the backing index of the SQLiteObjectStore
            // so that it can also be used as a regular index to accelerate queries...
            SQLiteObjectStore<O, ? extends Comparable<?>> sqLiteObjectStore = (SQLiteObjectStore<O, ? extends Comparable<?>>)objectStore;
            SQLiteIdentityIndex<? extends Comparable<?>, O> backingIndex = sqLiteObjectStore.getBackingIndex();
            addIndex(backingIndex, queryOptions);
        }

        forEachIndexDo(new IndexOperation<O>() {
            @Override
            public boolean perform(Index<O> index) {
                queryOptions.put(QueryEngine.class, this);
                queryOptions.put(Persistence.class, persistence);
                index.init(objectStore, queryOptions);
                return true;
            }
        });
    }

    // -------------------- Methods for adding indexes --------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIndex(Index<O> index, QueryOptions queryOptions) {
        if (index instanceof StandingQueryIndex) {
            @SuppressWarnings({"unchecked"})
            StandingQueryIndex<O> standingQueryIndex = (StandingQueryIndex<O>) index;
            addStandingQueryIndex(standingQueryIndex, standingQueryIndex.getStandingQuery(), queryOptions);
        }
        else if (index instanceof CompoundIndex) {
            @SuppressWarnings({"unchecked"})
            CompoundIndex<O> compoundIndex = (CompoundIndex<O>) index;
            CompoundAttribute<O> compoundAttribute = compoundIndex.getAttribute();
            addCompoundIndex(compoundIndex, compoundAttribute, queryOptions);
        }
        else if (index instanceof AttributeIndex) {
            @SuppressWarnings({"unchecked"})
            AttributeIndex<?, O> attributeIndex = (AttributeIndex<?, O>) index;
            Attribute<O, ?> indexedAttribute = attributeIndex.getAttribute();
            if (indexedAttribute instanceof StandingQueryAttribute) {
                @SuppressWarnings("unchecked")
                StandingQueryAttribute<O> standingQueryAttribute = (StandingQueryAttribute<O>) indexedAttribute;
                Query<O> standingQuery = standingQueryAttribute.getQuery();
                addStandingQueryIndex(index, standingQuery, queryOptions);
            }
            else {
                addAttributeIndex(attributeIndex, queryOptions);
            }
        }
        else {
            throw new IllegalStateException("Unexpected type of index: " + (index == null ? null : index.getClass().getName()));
        }
        if (!index.isMutable()) {
            immutableIndexes.add(index);
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
        if (attributeIndex instanceof SimplifiedSQLiteIndex) {
            // Ensure there is not already an identity index added for this attribute...
            for (Index<O> existingIndex : indexesOnThisAttribute) {
                if (existingIndex instanceof IdentityAttributeIndex) {
                    throw new IllegalStateException("An identity index for persistence has already been added, and no additional non-heap indexes are allowed, on attribute: " + attribute);
                }
            }
        }
        // Add the index...
        if (!indexesOnThisAttribute.add(attributeIndex)) {
            throw new IllegalStateException("An equivalent index has already been added for attribute: " + attribute);
        }
        if (attributeIndex instanceof UniqueIndex) {
            // We put UniqueIndexes in a separate map too, to access directly...
            uniqueIndexes.put(attribute, attributeIndex);
        }
        queryOptions.put(QueryEngine.class, this);
        queryOptions.put(Persistence.class, persistence);
        attributeIndex.init(objectStore, queryOptions);
    }



    /**
     * Adds either a {@link StandingQueryIndex} or a regular index build on a {@link StandingQueryAttribute}.
     * @param standingQueryIndex The index to add
     * @param standingQuery The query on which the index is based
     */
    void addStandingQueryIndex(Index<O> standingQueryIndex, Query<O> standingQuery, QueryOptions queryOptions) {
        Index<O> existingIndex = standingQueryIndexes.putIfAbsent(standingQuery, standingQueryIndex);
        if (existingIndex != null) {
            throw new IllegalStateException("An index has already been added for standing query: " + standingQuery);
        }
        queryOptions.put(QueryEngine.class, this);
        queryOptions.put(Persistence.class, persistence);
        standingQueryIndex.init(objectStore, queryOptions);
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
        queryOptions.put(QueryEngine.class, this);
        queryOptions.put(Persistence.class, persistence);
        compoundIndex.init(objectStore, queryOptions);
    }

    // -------------------- Methods for removing indexes --------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeIndex(Index<O> index, QueryOptions queryOptions) {
        boolean removed;
        if (index instanceof StandingQueryIndex) {
            @SuppressWarnings({"unchecked"})
            StandingQueryIndex<O> standingQueryIndex = (StandingQueryIndex<O>) index;

            removed = standingQueryIndexes.remove(standingQueryIndex.getStandingQuery(), standingQueryIndex);
        }
        else if (index instanceof CompoundIndex) {
            @SuppressWarnings({"unchecked"})
            CompoundIndex<O> compoundIndex = (CompoundIndex<O>) index;
            CompoundAttribute<O> compoundAttribute = compoundIndex.getAttribute();

            removed = compoundIndexes.remove(compoundAttribute, compoundIndex);
        }
        else if (index instanceof AttributeIndex) {
            @SuppressWarnings({"unchecked"})
            AttributeIndex<?, O> attributeIndex = (AttributeIndex<?, O>) index;
            Attribute<O, ?> indexedAttribute = attributeIndex.getAttribute();

            if (indexedAttribute instanceof StandingQueryAttribute) {
                @SuppressWarnings("unchecked")
                StandingQueryAttribute<O> standingQueryAttribute = (StandingQueryAttribute<O>) indexedAttribute;
                Query<O> standingQuery = standingQueryAttribute.getQuery();

                removed = standingQueryIndexes.remove(standingQuery, index);
            }
            else {
                Set<Index<O>> indexesOnThisAttribute = attributeIndexes.get(indexedAttribute);

                removed = indexesOnThisAttribute.remove(attributeIndex);

                if (attributeIndex instanceof UniqueIndex) {
                    // Remove from UniqueIndexes as well...
                    removed = uniqueIndexes.remove(indexedAttribute, attributeIndex) || removed;
                }

                if (indexesOnThisAttribute.isEmpty()) {
                    // If there are no more indexes left on this attribute,
                    // remove the Set which was used to store indexes on the attribute also...
                    attributeIndexes.remove(indexedAttribute);
                }
            }
        }
        else {
            throw new IllegalStateException("Unexpected type of index: " + (index == null ? null : index.getClass().getName()));
        }
        if (removed && !index.isMutable()) {
            // Remove from the set of immutable indexes; this is used by ensureMutable() and the isMutable() method...
            immutableIndexes.remove(index);
        }
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
    ResultSet<O> getEntireCollectionAsResultSet(final Query<O> query, final QueryOptions queryOptions) {
        return new ObjectStoreResultSet<O>(objectStore, query, queryOptions, Integer.MAX_VALUE) {
            // Override getMergeCost() to avoid calling size(),
            // which may be expensive for custom implementations of lazy backing sets...
            @Override
            public int getMergeCost() {
                return Integer.MAX_VALUE;
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
        // First check if a UniqueIndex is available, as this will have the lowest cost...
        Index<O> uniqueIndex = uniqueIndexes.get(query.getAttribute());
        if (uniqueIndex!= null && uniqueIndex.supportsQuery(query, queryOptions)){
            return uniqueIndex.retrieve(query, queryOptions);
        }

        // Examine other (non-unique) indexes...
        Iterable<Index<O>> indexesOnAttribute = getIndexesOnAttribute(query.getAttribute());

        // Choose the index with the lowest retrieval cost for this query...
        ResultSet<O> lowestCostResultSet = null;
        int lowestRetrievalCost = 0;
        for (Index<O> index : indexesOnAttribute) {
            if (index.supportsQuery(query, queryOptions)) {
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
        return new CostCachingResultSet<O>(lowestCostResultSet);
    }

    // -------------------- Methods for query processing --------------------

    /**
     * {@inheritDoc}
     */
    // Implementation note: this methods actually just pre-processes QueryOption arguments and then delegates
    // to the #retrieveRecursive() method.
    @Override
    public ResultSet<O> retrieve(final Query<O> query, final QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        OrderByOption<O> orderByOption = (OrderByOption<O>) queryOptions.get(OrderByOption.class);

        // Store the root query in the queryOptions, so that when retrieveRecursive() examines child branches, that
        // both the branch query and the root query will be available to PartialIndexes so they may determine if they
        // can be used to accelerate the overall query...
        queryOptions.put(ROOT_QUERY, query);

        // Log decisions made to the query log, if provided...
        final QueryLog queryLog = queryOptions.get(QueryLog.class); // might be null

        SortedKeyStatisticsAttributeIndex<?, O> indexForOrdering = null;
        if (orderByOption != null) {
            // Results must be ordered. Determine the ordering strategy to use: i.e. if we should use an index to order
            // results, or if we should retrieve results and sort them afterwards instead.

            Double selectivityThreshold = Thresholds.getThreshold(queryOptions, EngineThresholds.INDEX_ORDERING_SELECTIVITY);
            if (selectivityThreshold == null) {
                selectivityThreshold = EngineThresholds.INDEX_ORDERING_SELECTIVITY.getThresholdDefault();
            }
            final List<AttributeOrder<O>> allSortOrders = orderByOption.getAttributeOrders();
            if (selectivityThreshold != 0.0) {
                // Index ordering can be used.
                // Check if an index is actually available to support it...
                AttributeOrder<O> firstOrder = allSortOrders.iterator().next();
                @SuppressWarnings("unchecked")
                Attribute<O, Comparable> firstAttribute = (Attribute<O, Comparable>)firstOrder.getAttribute();
                if (firstAttribute instanceof OrderControlAttribute) {
                    @SuppressWarnings("unchecked")
                    Attribute<O, Comparable> firstAttributeDelegate = ((OrderControlAttribute)firstAttribute).getDelegateAttribute();
                    firstAttribute = firstAttributeDelegate;
                }

                // Before we check if an index is available to support index ordering, we need to account for the fact
                // that even if such an index is available, it might not contain all objects in the collection.
                //
                // An index built on a SimpleAttribute, is guaranteed to contain all objects in the collection, because
                // SimpleAttribute is guaranteed to return a value for every object.
                // OTOH an index built on a non-SimpleAttribute, is not guaranteed to contain all objects in the
                // collection, because non-SimpleAttributes are permitted to return *zero* or more values for any
                // object. Objects for which non-SimpleAttributes return zero values, will be omitted from the index.
                //
                // Therefore, if we will use an index to order results, we must ensure that the collection also has a
                // suitable index to allow the objects which are not in the index to be retrieved as well. When
                // ordering results, we must return those objects either before or after the objects which are found in
                // the index. Here we proceed to locate a suitable index to use for ordering results, only if we will
                // also be able to retrieve the objects missing from that index efficiently as well...
                if (firstAttribute instanceof SimpleAttribute || standingQueryIndexes.get(not(has(firstAttribute))) != null) {
                    // Either we are sorting by a SimpleAttribute, or we are sorting by a non-SimpleAttribute and we
                    // also will be able to retrieve objects which do not have values for the non-SimpleAttribute
                    // efficiently. Now check if an index exists which would allow index ordering...
                    for (Index<O> index : this.getIndexesOnAttribute(firstAttribute)) {
                        if (index instanceof SortedKeyStatisticsAttributeIndex && !index.isQuantized()) {
                            indexForOrdering = (SortedKeyStatisticsAttributeIndex<?, O>)index;
                            break;
                        }
                    }
                }


                if (queryLog != null) {
                    queryLog.log("indexForOrdering: " + (indexForOrdering == null ? null : indexForOrdering.getClass().getSimpleName()));
                }
                // At this point we might have found an appropriate indexForOrdering, or it might still be null.
                if (indexForOrdering != null) {
                    // We found an appropriate index.
                    // Determine if the selectivity of the query is below the selectivity threshold to use index ordering...
                    final double querySelectivity;
                    if (selectivityThreshold == 1.0) {
                        // Index ordering has been requested explicitly.
                        // Don't bother calculating query selectivity, assign low selectivity so we will use the index...
                        querySelectivity = 0.0;
                    }
                    else if (!indexForOrdering.supportsQuery(has(firstAttribute), queryOptions)) {
                        // Index ordering was not requested explicitly, and we cannot calculate the selectivity.
                        // In this case even though we have an index which supports index ordering,
                        // we don't have enough information to say that it would be beneficial.
                        // Assign high selectivity so that the materialize strategy will be used instead...
                        querySelectivity = 1.0;
                    }
                    else {
                        // The index supports has() queries, which allows us to calculate selectivity.
                        // Calculate query selectivity, based on the query cardinality and index cardinality...
                        final int queryCardinality = retrieveRecursive(query, queryOptions).getMergeCost();
                        final int indexCardinality = indexForOrdering.retrieve(has(firstAttribute), queryOptions).getMergeCost();
                        if (queryLog != null) {
                            queryLog.log("queryCardinality: " + queryCardinality);
                            queryLog.log("indexCardinality: " + indexCardinality);
                        }
                        if (indexCardinality == 0) {
                            // Handle edge case where the index is empty.
                            querySelectivity = 1.0; // treat is as if the query has high selectivity (tend to use materialize).
                        }
                        else if (queryCardinality > indexCardinality) {
                            // Handle edge case where query cardinality is greater than index cardinality.
                            querySelectivity = 0.0; // treat is as if the query has low selectivity (tend to use index ordering).
                        }
                        else {
                            querySelectivity = 1.0 - queryCardinality / (double)indexCardinality;
                        }
                    }

                    if (queryLog != null) {
                        queryLog.log("querySelectivity: " + querySelectivity);
                        queryLog.log("selectivityThreshold: " + selectivityThreshold);
                    }
                    if (querySelectivity > selectivityThreshold) {
                        // Selectivity is too high for index ordering strategy.
                        // Use the materialize ordering strategy instead.
                        indexForOrdering = null;
                    }
                    // else: querySelectivity <= selectivityThreshold, so we use the index ordering strategy.
                }
            }
        }
        ResultSet<O> resultSet;
        if (indexForOrdering != null) {
            // Retrieve results, using an index to accelerate ordering...
            resultSet = retrieveWithIndexOrdering(query, queryOptions, orderByOption, indexForOrdering);
            if (queryLog != null) {
                queryLog.log("orderingStrategy: index");
            }
        }
        else {
            // Retrieve results, without using an index to accelerate ordering...
            resultSet = retrieveWithoutIndexOrdering(query, queryOptions, orderByOption);
            if (queryLog != null) {
                queryLog.log("orderingStrategy: materialize");
            }
        }

        // Return the results, ensuring that the close() method will close any resources which were opened...
        // TODO: possibly not necessary to wrap here, as the IndexedCollections also ensure close() is called...
        return new CloseableResultSet<O>(resultSet, query, queryOptions) {
            @Override
            public void close() {
                super.close();
                CloseableRequestResources.closeForQueryOptions(queryOptions);
            }
        };
    }

    /**
     * Retrieve results and then sort them afterwards (if sorting is required).
     */
    ResultSet<O> retrieveWithoutIndexOrdering(Query<O> query, QueryOptions queryOptions, OrderByOption<O> orderByOption) {
        ResultSet<O> resultSet;
        resultSet = retrieveRecursive(query, queryOptions);

        // Check if we need to wrap ResultSet to order and/or deduplicate results (deduplicate using MATERIAIZE rather
        // than LOGICAL_ELIMINATION strategy)...
        final boolean applyMaterializedDeduplication = DeduplicationOption.isMaterialize(queryOptions);
        if (orderByOption != null) {
            // An OrderByOption was specified, wrap the results in an MaterializedOrderedResultSet.
            // -> This will implicitly sort AND deduplicate the results returned by the ResultSet.iterator() method.
            // -> However note this does not mean we will also deduplicate the count returned by ResultSet.size()!
            // -> Deduplicating the count returned by size() is expensive, so we only do this if the client
            //    requested both ordering AND deduplication explicitly (hence we pass applyMaterializeDeduplication)...
            Comparator<O> comparator = new AttributeOrdersComparator<O>(orderByOption.getAttributeOrders(), queryOptions);
            resultSet = new MaterializedOrderedResultSet<O>(resultSet, comparator, applyMaterializedDeduplication);
        }
        else if (applyMaterializedDeduplication) {
            // A DeduplicationOption was specified, wrap the results in an MaterializedDeduplicatedResultSet,
            // which will deduplicate (but not sort) results. O(n) time complexity to subsequently iterate...
            resultSet = new MaterializedDeduplicatedResultSet<O>(resultSet);
        }
        return resultSet;
    }


    /**
     * Use an index to order results.
     */
    ResultSet<O> retrieveWithIndexOrdering(final Query<O> query, final QueryOptions queryOptions, final OrderByOption<O> orderByOption, final SortedKeyStatisticsIndex<?, O> indexForOrdering) {
        final List<AttributeOrder<O>> allSortOrders = orderByOption.getAttributeOrders();

        final AttributeOrder<O> primarySortOrder = allSortOrders.get(0);

        // If the client wrapped the first attribute by which results should be ordered in an OrderControlAttribute,
        // assign it here...
        @SuppressWarnings("unchecked")
        final OrderControlAttribute<O> orderControlAttribute =
                (primarySortOrder.getAttribute() instanceof OrderControlAttribute)
                        ? (OrderControlAttribute<O>)primarySortOrder.getAttribute() : null;

        // If the first attribute by which results should be ordered was wrapped, unwrap it, and assign it here...
        @SuppressWarnings("unchecked")
        final Attribute<O, Comparable> primarySortAttribute =
                (orderControlAttribute == null)
                        ? (Attribute<O, Comparable>) primarySortOrder.getAttribute()
                        : (Attribute<O, Comparable>) orderControlAttribute.getDelegateAttribute();

        final boolean primarySortDescending = primarySortOrder.isDescending();

        final boolean attributeCanHaveZeroValues = !(primarySortAttribute instanceof SimpleAttribute);
        final boolean attributeCanHaveMoreThanOneValue = !(primarySortAttribute instanceof SimpleAttribute || primarySortAttribute instanceof SimpleNullableAttribute);

        @SuppressWarnings("unchecked")
        final RangeBounds<?> rangeBoundsFromQuery = getBoundsFromQuery(query, primarySortAttribute);

        return new ResultSet<O>() {
            @Override
            public Iterator<O> iterator() {
                Iterator<O> mainResults = retrieveWithIndexOrderingMainResults(query, queryOptions, indexForOrdering, allSortOrders, rangeBoundsFromQuery, attributeCanHaveMoreThanOneValue, primarySortDescending);

                // Combine the results from the index ordered search, with objects which would be missing from that
                // index, which is possible in the case that the primary sort attribute is nullable or multi-valued...

                Iterator<O> combinedResults;
                if (attributeCanHaveZeroValues) {
                    Iterator<O> missingResults = retrieveWithIndexOrderingMissingResults(query, queryOptions, primarySortAttribute, allSortOrders, attributeCanHaveMoreThanOneValue);

                    // Concatenate the main results and the missing objects, accounting for which batch should come first...
                    if (orderControlAttribute instanceof OrderMissingFirstAttribute) {
                        combinedResults = ConcatenatingIterator.concatenate(Arrays.asList(missingResults, mainResults));
                    }
                    else if (orderControlAttribute instanceof OrderMissingLastAttribute) {
                        combinedResults = ConcatenatingIterator.concatenate(Arrays.asList(mainResults, missingResults));
                    }
                    else if (primarySortOrder.isDescending()) {
                        combinedResults = ConcatenatingIterator.concatenate(Arrays.asList(mainResults, missingResults));
                    }
                    else {
                        combinedResults = ConcatenatingIterator.concatenate(Arrays.asList(missingResults, mainResults));
                    }
                }
                else {
                    combinedResults = mainResults;
                }

                if (attributeCanHaveMoreThanOneValue) {
                    // Deduplicate results in case the same object could appear in more than one bucket
                    // and so otherwise could be returned more than once...
                    combinedResults = new MaterializedDeduplicatedIterator<O>(combinedResults);
                }
                return combinedResults;
            }

            @Override
            public boolean contains(O object) {
                ResultSet<O> rs = retrieveWithoutIndexOrdering(query, queryOptions, null);
                try {
                    return rs.contains(object);
                }
                finally {
                    rs.close();
                }
            }

            @Override
            public boolean matches(O object) {
                return query.matches(object, queryOptions);
            }

            @Override
            public Query<O> getQuery() {
                return query;
            }

            @Override
            public QueryOptions getQueryOptions() {
                return queryOptions;
            }

            @Override
            public int getRetrievalCost() {
                ResultSet<O> rs = retrieveWithoutIndexOrdering(query, queryOptions, null);
                try {
                    return rs.getRetrievalCost();
                }
                finally {
                    rs.close();
                }
            }

            @Override
            public int getMergeCost() {
                ResultSet<O> rs = retrieveWithoutIndexOrdering(query, queryOptions, null);
                try {
                    return rs.getMergeCost();
                }
                finally {
                    rs.close();
                }
            }

            @Override
            public int size() {
                ResultSet<O> rs = retrieveWithoutIndexOrdering(query, queryOptions, null);
                try {
                    return rs.size();
                }
                finally {
                    rs.close();
                }
            }

            @Override
            public void close() {

            }
        };
    }

    Iterator<O> retrieveWithIndexOrderingMainResults(final Query<O> query, QueryOptions queryOptions, SortedKeyStatisticsIndex<?, O> indexForOrdering, List<AttributeOrder<O>> allSortOrders, RangeBounds<?> rangeBoundsFromQuery, boolean attributeCanHaveMoreThanOneValue, boolean primarySortDescending) {
        // Ensure that at the end of processing the request, that we close any resources we opened...
        final CloseableResourceGroup closeableResourceGroup = CloseableRequestResources.forQueryOptions(queryOptions).addGroup();

        final List<AttributeOrder<O>> sortOrdersForBucket = determineAdditionalSortOrdersForIndexOrdering(allSortOrders, attributeCanHaveMoreThanOneValue, indexForOrdering, queryOptions);

        final CloseableIterator<? extends KeyValue<? extends Comparable<?>, O>> keysAndValuesInRange = getKeysAndValuesInRange(indexForOrdering, rangeBoundsFromQuery, primarySortDescending, queryOptions);

        // Ensure this CloseableIterator gets closed...
        closeableResourceGroup.add(keysAndValuesInRange);

        final Iterator<O> sorted;
        if (sortOrdersForBucket.isEmpty()) {
            sorted = new LazyIterator<O>() {
                @Override
                protected O computeNext() {
                    return keysAndValuesInRange.hasNext() ? keysAndValuesInRange.next().getValue() : endOfData();
                }
            };
        }
        else {
            sorted = concatenate(groupAndSort(keysAndValuesInRange, new AttributeOrdersComparator<O>(sortOrdersForBucket, queryOptions)));
        }

        return filterIndexOrderingCandidateResults(sorted, query, queryOptions);
    }

    Iterator<O> retrieveWithIndexOrderingMissingResults(final Query<O> query, QueryOptions queryOptions, Attribute<O, Comparable> primarySortAttribute, List<AttributeOrder<O>> allSortOrders, boolean attributeCanHaveMoreThanOneValue) {
        // Ensure that at the end of processing the request, that we close any resources we opened...
        final CloseableResourceGroup closeableResourceGroup = CloseableRequestResources.forQueryOptions(queryOptions).addGroup();

        // Retrieve missing objects from the secondary index on objects which don't have a value for the primary sort attribute...
        Not<O> missingValuesQuery = not(has(primarySortAttribute));
        ResultSet<O> missingResults = retrieveRecursive(missingValuesQuery, queryOptions);

        // Ensure that this is closed...
        closeableResourceGroup.add(missingResults);

        Iterator<O> missingResultsIterator = missingResults.iterator();
        // Filter the objects from the secondary index, to ensure they match the query...
        missingResultsIterator = filterIndexOrderingCandidateResults(missingResultsIterator, query, queryOptions);

        // Determine if we need to sort the missing objects...
        Index<O> indexForMissingObjects = standingQueryIndexes.get(missingValuesQuery);
        final List<AttributeOrder<O>> sortOrdersForBucket = determineAdditionalSortOrdersForIndexOrdering(allSortOrders, attributeCanHaveMoreThanOneValue, indexForMissingObjects, queryOptions);

        if (!sortOrdersForBucket.isEmpty()) {
            // We do need to sort the missing objects...
            Comparator<O> comparator = new AttributeOrdersComparator<O>(sortOrdersForBucket, queryOptions);
            missingResultsIterator = IteratorUtil.materializedSort(missingResultsIterator, comparator);
        }

        return missingResultsIterator;
    }

    /**
     * Filters the given sorted candidate results to ensure they match the query, using either the default merge
     * strategy or the index merge strategy as appropriate.
     * <p/>
     * This method will add any resources which need to be closed to {@link CloseableRequestResources} in the query options.
     *
     * @param sortedCandidateResults The candidate results to be filtered
     * @param query The query
     * @param queryOptions The query options
     * @return A filtered iterator which returns the subset of candidate objects which match the query
     */
    Iterator<O> filterIndexOrderingCandidateResults(final Iterator<O> sortedCandidateResults, final Query<O> query, final QueryOptions queryOptions) {
        final boolean indexMergeStrategyEnabled = isFlagEnabled(queryOptions, PREFER_INDEX_MERGE_STRATEGY);
        if (indexMergeStrategyEnabled) {
            final ResultSet<O> indexAcceleratedQueryResults = retrieveWithoutIndexOrdering(query, queryOptions, null);
            if (indexAcceleratedQueryResults.getRetrievalCost() == Integer.MAX_VALUE) {
                // No index is available to accelerate the index merge strategy...
                indexAcceleratedQueryResults.close();
                // We fall back to filtering via query.matches() below.
            }
            else {
                // Ensure that indexAcceleratedQueryResults is closed at the end of processing the request...
                final CloseableResourceGroup closeableResourceGroup = CloseableRequestResources.forQueryOptions(queryOptions).addGroup();
                closeableResourceGroup.add(indexAcceleratedQueryResults);
                // This is the index merge strategy where indexes are used to filter the sorted results...
                return new FilteringIterator<O>(sortedCandidateResults, queryOptions) {
                    @Override
                    public boolean isValid(O object, QueryOptions queryOptions) {
                        return indexAcceleratedQueryResults.contains(object);
                    }
                };
            }

        }
        // Either index merge strategy is not enabled, or no suitable indexes are available for it.
        // We filter results by examining values returned by attributes referenced in the query instead...
        return new FilteringIterator<O>(sortedCandidateResults, queryOptions) {
            @Override
            public boolean isValid(O object, QueryOptions queryOptions) {
                return query.matches(object, queryOptions);
            }
        };
    }

    static <O, A extends Comparable<A>> Persistence<O, A> getPersistenceFromQueryOptions(QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        Persistence<O, A> persistence = (Persistence<O, A>) queryOptions.get(Persistence.class);
        if (persistence == null) {
            throw new IllegalStateException("A required Persistence object was not supplied in query options");
        }
        return persistence;
    }

    /**
     * Called when using an index to order results, to determine if or how results within each bucket
     * in that index should be sorted.
     * <p/>
     *
     * We must sort results within each bucket, when:
     * <ol>
     *     <li>
     *         The index is quantized.
     *     </li>
     *     <li>
     *         The attribute can have multiple values (if object 1 values ["a"] and  object 2 has values
     *         ["a", "b"] then objects 1 & 2 will both be in the same bucket, but object 1 should sort first ascending).
     *         However this case can be suppressed with
     *         {@link EngineFlags#INDEX_ORDERING_ALLOW_FAST_ORDERING_OF_MULTI_VALUED_ATTRIBUTES}.
     *     </li>
     *     <li>
     *         There are additional sort orders after the first one.
     *     </li>
     * </ol>
     *
     * @param allSortOrders The user-specified sort orders
     * @param attributeCanHaveMoreThanOneValue If the primary attribute used for sorting can return more than one value
     * @param index The index from which the bucket is accessed
     * @return A list of AttributeOrder objects representing the sort order to apply to objects in the bucket
     */
    static <O> List<AttributeOrder<O>> determineAdditionalSortOrdersForIndexOrdering(List<AttributeOrder<O>> allSortOrders, boolean attributeCanHaveMoreThanOneValue, Index<O> index, QueryOptions queryOptions) {
        return (index.isQuantized() || (attributeCanHaveMoreThanOneValue && !isFlagEnabled(queryOptions, INDEX_ORDERING_ALLOW_FAST_ORDERING_OF_MULTI_VALUED_ATTRIBUTES)))
                ? allSortOrders // We must re-sort on all sort orders within each bucket.
                : allSortOrders.subList(1, allSortOrders.size());
    }

    static <A extends Comparable<A>, O> CloseableIterator<KeyValue<A, O>> getKeysAndValuesInRange(SortedKeyStatisticsIndex<A, O> index, RangeBounds<?> queryBounds, boolean descending, QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        RangeBounds<A> typedBounds = (RangeBounds<A>) queryBounds;
        if (!descending) {
            return index.getKeysAndValues(
                    typedBounds.lowerBound, typedBounds.lowerInclusive,
                    typedBounds.upperBound, typedBounds.upperInclusive,
                    queryOptions
            ).iterator();
        }
        else {
            return index.getKeysAndValuesDescending(
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

    static <A extends Comparable<A>, O> RangeBounds getBoundsFromQuery(Query<O> query, Attribute<O, A> attribute) {
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
     * {@link #retrieveIntersection(Collection, QueryOptions, boolean)} and {@link #retrieveUnion(Collection, QueryOptions)}
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
        final boolean indexMergeStrategyEnabled = isFlagEnabled(queryOptions, PREFER_INDEX_MERGE_STRATEGY);

        // Check if we can process this query from a standing query index...
        Index<O> standingQueryIndex = standingQueryIndexes.get(query);
        if (standingQueryIndex != null) {
            // No deduplication required for standing queries.
            if (standingQueryIndex instanceof StandingQueryIndex) {
                return standingQueryIndex.retrieve(query, queryOptions);
            }
            else {
                return standingQueryIndex.retrieve(equal(forStandingQuery(query), Boolean.TRUE), queryOptions);
            }
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
                    if (compoundIndex != null && compoundIndex.supportsQuery(compoundQuery, queryOptions)) {
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
                                return retrieveIntersection(and.getSimpleQueries(), queryOptions, indexMergeStrategyEnabled);
                            }
                            // Recursively call this method for logical queries...
                            return retrieveRecursive(logicalQueriesIterator.next(), queryOptions);
                        }
                    };
                }
            }, query, queryOptions, indexMergeStrategyEnabled);
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
            ResultSet<O> union;
            // *** Deduplication can be required for unions... ***
            if (DeduplicationOption.isLogicalElimination(queryOptionsForOrUnion)) {
                union = new ResultSetUnion<O>(resultSetsToUnion, query, queryOptions, indexMergeStrategyEnabled);
            }
            else {
                union = new ResultSetUnionAll<O>(resultSetsToUnion, query, queryOptions);
            }

            if (union.getRetrievalCost() == Integer.MAX_VALUE) {
                // Either no indexes are available for any branches of the or() query, or indexes are only available
                // for some of the branches.
                // If we were to delegate to the FallbackIndex to retrieve results for any of the branches which
                // don't have indexes, then the FallbackIndex would scan the entire collection to locate results.
                // This would happen for *each* of the branches which don't have indexes - so the entire collection
                // could be scanned multiple times.
                // So to avoid that, here we will scan the entire collection once, to find all objects which match
                // all of the child branches in a single scan.
                // Note: there is no need to deduplicate results which were fetched this way.
                union = new FilteringResultSet<O>(getEntireCollectionAsResultSet(query, queryOptions), or, queryOptions) {
                    @Override
                    public boolean isValid(O object, QueryOptions queryOptions) {
                        return or.matches(object, queryOptions);
                    }
                };
            }
            return union;
        }
        else if (query instanceof Not) {
            final Not<O> not = (Not<O>) query;
            // No deduplication required for negation (the entire collection is a Set, contains no duplicates).
            // Retrieve the ResultSet for the negated query, by calling this method recursively...
            ResultSet<O> resultSetToNegate = retrieveRecursive(not.getNegatedQuery(), queryOptions);
            // Return the negation of this result set, by subtracting it from the entire collection of objects...
            return new ResultSetDifference<O>(getEntireCollectionAsResultSet(query, queryOptions), resultSetToNegate, query, queryOptions, indexMergeStrategyEnabled);
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
    <A> ResultSet<O> retrieveIntersection(Collection<SimpleQuery<O, ?>> queries, QueryOptions queryOptions, boolean indexMergeStrategyEnabled) {
        List<ResultSet<O>> resultSets = new ArrayList<ResultSet<O>>(queries.size());
        for (SimpleQuery query : queries) {
            // Work around type erasure...
            @SuppressWarnings({"unchecked"})
            SimpleQuery<O, A> queryTyped = (SimpleQuery<O, A>) query;
            ResultSet<O> resultSet = getResultSetWithLowestRetrievalCost(queryTyped, queryOptions);
            resultSets.add(resultSet);
        }
        @SuppressWarnings("unchecked")
        Collection<Query<O>> queriesTyped = (Collection<Query<O>>)(Collection<? extends Query<O>>)queries;
        Query<O> query = queriesTyped.size() == 1 ? queriesTyped.iterator().next() : new And<O>(queriesTyped);
        // The rest of the algorithm is implemented in ResultSetIntersection...
        return new ResultSetIntersection<O>(resultSets, query, queryOptions, indexMergeStrategyEnabled);
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
        @SuppressWarnings("unchecked")
        Collection<Query<O>> queriesTyped = (Collection<Query<O>>)(Collection<? extends Query<O>>)queries;
        Query<O> query = queriesTyped.size() == 1 ? queriesTyped.iterator().next() : new Or<O>(queriesTyped);
        // Perform deduplication as necessary...
        if (DeduplicationOption.isLogicalElimination(queryOptions)) {
            boolean indexMergeStrategyEnabled = isFlagEnabled(queryOptions, PREFER_INDEX_MERGE_STRATEGY);
            return new ResultSetUnion<O>(resultSetsToUnion, query, queryOptions, indexMergeStrategyEnabled);
        }
        else {
            return new ResultSetUnionAll<O>(resultSetsToUnion, query, queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(final ObjectSet<O> objectSet, final QueryOptions queryOptions) {
        ensureMutable();
        final FlagHolder modified = new FlagHolder();
        forEachIndexDo(new IndexOperation<O>() {
            @Override
            public boolean perform(Index<O> index) {
                modified.value |= index.addAll(objectSet, queryOptions);
                return true;
            }
        });
        return modified.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(final ObjectSet<O> objectSet, final QueryOptions queryOptions) {
        ensureMutable();
        final FlagHolder modified = new FlagHolder();
        forEachIndexDo(new IndexOperation<O>() {
            @Override
            public boolean perform(Index<O> index) {
                modified.value |= index.removeAll(objectSet, queryOptions);
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
        return immutableIndexes.isEmpty();
    }

    /**
     * Throws an {@link IllegalStateException} if all indexes are not mutable.
     */
    void ensureMutable() {
        if (!immutableIndexes.isEmpty()) {
            throw new IllegalStateException("Cannot modify indexes, an immutable index has been added.");
        }
    }

    /**
     * A closure/callback object invoked for each index in turn by method
     * {@link CollectionQueryEngine#forEachIndexDo(IndexOperation)}.
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
