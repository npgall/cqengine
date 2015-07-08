package com.googlecode.cqengine.attribute;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Collections;

/**
 * An attribute which wraps a query. Any index built on this attribute, will accelerate the entire query or query
 * fragment encapsulated in this attribute, returning results for that query in constant time complexity O(1).
 * <p/>
 * When an index based on a {@code StandingQueryAttribute} is added to the collection, objects added to the collection
 * will then be tested to see if they match this query. If and only if they match the query exactly, they will be
 * added to the index. Subsequently if CQEngine finds the given query in its entirety, or as a branch or
 * fragment of a larger query, then it will use the index to answer that query/query fragment in O(1) time complexity.
 *
 * @author niall.gallagher
 */
public class StandingQueryAttribute<O> extends MultiValueAttribute<O, Boolean> {

    final Query<O> standingQuery;

    public StandingQueryAttribute(Class<O> objectType, Query<O> standingQuery) {
        super(objectType, Boolean.class, "sq_" + standingQuery.toString());
        this.standingQuery = standingQuery;
    }

    @Override
    public Iterable<Boolean> getValues(O object, QueryOptions queryOptions) {
        if (standingQuery.matches(object, queryOptions)) {
            return Collections.singleton(Boolean.TRUE);
        }
        return Collections.emptySet();
    }
}