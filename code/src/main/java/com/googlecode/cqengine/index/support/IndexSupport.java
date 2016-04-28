package com.googlecode.cqengine.index.support;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.DeduplicationOption;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.In;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.connective.ResultSetUnion;
import com.googlecode.cqengine.resultset.connective.ResultSetUnionAll;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;

/**
 * <p> Index utilities
 *
 * @author Silvano Riz
 */
public class IndexSupport {

    private IndexSupport(){}

    /**
     * <p> If a query option specifying logical deduplication was supplied, wrap the given result sets in {@link ResultSetUnion}, otherwise wrap in {@link ResultSetUnionAll}.
     *
     * <p> There are two exceptions where a {@link ResultSetUnionAll} is used regardless of the logical deduplication query option:
     * <ul>
     *     <li>
     *         If the index is built on a SimpleAttribute which means that the same object could not exist in more than one {@link StoredResultSet}.
     *     </li>
     *     <li>
     *         If the query is an {@link In} query and it is marked as disjoint ( {@link In#isDisjoint()} returns true )
     *     </li>
     * </ul>
     *
     * @param results Provides the result sets to union
     * @param query The query for which the union is being constructed
     * @param queryOptions Specifies whether or not logical deduplication is required
     * @param retrievalCost The resultSet retrieval cost
     * @return A union view over the given result sets
     */
    public static <O, A> ResultSet<O> deduplicateIfNecessary(final Iterable<? extends ResultSet<O>> results,
                                                             final Query<O> query,
                                                             final Attribute<O, A> attribute,
                                                             final QueryOptions queryOptions,
                                                             final int retrievalCost) {
        boolean logicalElimination = DeduplicationOption.isLogicalElimination(queryOptions);
        boolean attributeHasAtMostOneValue = (attribute instanceof SimpleAttribute || attribute instanceof SimpleNullableAttribute);
        boolean queryIsADisjointInQuery = query instanceof In && ((In) query).isDisjoint();
        if (!logicalElimination || attributeHasAtMostOneValue || queryIsADisjointInQuery) {
            // No need to deduplicate...
            return new ResultSetUnionAll<O>(results, query, queryOptions) {
                @Override
                public int getRetrievalCost() {
                    return retrievalCost;
                }
            };
        } else {
            // We need to deduplicate...
            return new ResultSetUnion<O>(results, query, queryOptions) {
                @Override
                public int getRetrievalCost() {
                    return retrievalCost;
                }
            };
        }
    }

}
