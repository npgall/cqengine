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
package com.googlecode.cqengine.attribute;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Collections;

/**
 * An attribute which wraps a query or query fragment. Any index built on this attribute, will accelerate the entire
 * query or query fragment encapsulated in this attribute, returning results for it in constant time complexity O(1).
 * <p/>
 * When an index based on a {@code StandingQueryAttribute} is added to the collection, objects added to the collection
 * will then be tested to see if they match this query. If and only if they match the query exactly, they will be
 * added to the index. Subsequently if CQEngine finds the given query in its entirety, or as a branch or
 * fragment of a larger query, then it will use the index to answer it in O(1) time complexity.
 *
 * @author niall.gallagher
 */
public class StandingQueryAttribute<O> extends MultiValueAttribute<O, Boolean> {

    final Query<O> standingQuery;

    @SuppressWarnings("unchecked")
    public StandingQueryAttribute(Query<O> standingQuery) {
        super((Class<O>)Object.class, Boolean.class, "<StandingQueryAttribute: " + standingQuery.toString() + ">");
        this.standingQuery = standingQuery;
    }

    @Override
    public Class<O> getObjectType() {
        throw new UnsupportedOperationException("Unsupported use of StandingQueryAttribute");
    }

    public Query<O> getQuery() {
        return standingQuery;
    }

    @Override
    public Iterable<Boolean> getValues(O object, QueryOptions queryOptions) {
        if (standingQuery.matches(object, queryOptions)) {
            return Collections.singleton(Boolean.TRUE);
        }
        return Collections.emptySet();
    }
}