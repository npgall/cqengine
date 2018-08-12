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
package com.googlecode.cqengine.index.compound.support;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.LogicalQuery;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Equal;
import com.googlecode.cqengine.query.simple.SimpleQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A query which wraps a {@link CompoundAttribute}, used only in the query engine's internal communication
 * with a {@link com.googlecode.cqengine.index.compound.CompoundIndex}.
 * <p/>
 * @author Niall Gallagher
 */
public class CompoundQuery<O> implements Query<O> {

    private final And<O> andQuery;
    private final CompoundAttribute<O> compoundAttribute;

    public CompoundQuery(And<O> andQuery, CompoundAttribute<O> compoundAttribute) {
        this.andQuery = andQuery;
        this.compoundAttribute = compoundAttribute;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation for {@link CompoundQuery} iterates each of the child {@link Equal} queries of the
     * {@link And} query from which the {@link CompoundQuery} was constructed, and for each child {@link Equal} query,
     * tests if the given object matches that query.
     *
     * @return True if the object matches all of the child {@link Equal} queries, false if the object does not match
     * one or more child {@link Equal} queries
     */
    @Override
    public boolean matches(O object, QueryOptions queryOptions) {
        for (SimpleQuery<O, ?> simpleQuery : andQuery.getSimpleQueries()) {
            Equal<O, ?> equal = (Equal<O, ?>) simpleQuery;
            if (!equal.matches(object, queryOptions)) {
                return false;
            }
        }
        return true;
    }

    public And<O> getAndQuery() {
        return andQuery;
    }

    public CompoundAttribute<O> getCompoundAttribute() {
        return compoundAttribute;
    }

    public CompoundValueTuple<O> getCompoundValueTuple() {
        Map<Attribute<O, ?>, Object> attributeValues = new HashMap<Attribute<O, ?>, Object>();
        for (SimpleQuery<O, ?> simpleQuery : andQuery.getSimpleQueries()) {
            Equal<O, ?> equal = (Equal<O, ?>) simpleQuery;
            attributeValues.put(equal.getAttribute(), equal.getValue());
        }
        return new CompoundValueTuple<O>(attributeValues);
    }

    public static <O> CompoundQuery<O> fromAndQueryIfSuitable(And<O> andQuery) {
        andQuery = flatten(andQuery);

        if (andQuery == null) {
            return null;
        }
        List<Attribute<O, ?>> attributeList = new ArrayList<Attribute<O, ?>>(andQuery.getSimpleQueries().size());
        for (SimpleQuery<O, ?> simpleQuery : andQuery.getSimpleQueries()) {
            if (!(simpleQuery instanceof Equal)) {
                return null;
            }
            attributeList.add(simpleQuery.getAttribute());
        }
        CompoundAttribute<O> compoundAttribute = new CompoundAttribute<O>(attributeList);

        return new CompoundQuery<O>(andQuery, compoundAttribute);
    }

    /**
     * Flatten an And query, bringing any nested And queries up the top level query if possible
     *
     * @param andQuery the And query to flatten
     * @return the flattened And query, or null if it cannot be converted into a flat And query
     */
    private static <O> And<O> flatten(And<O> andQuery) {
        final Set<Query<O>> flatQuerySet = new HashSet<Query<O>>();
        for (LogicalQuery<O> childQuery : andQuery.getLogicalQueries()) {
            if (childQuery instanceof And) {
                And<O> flatQuery = flatten((And<O>) childQuery);
                if (flatQuery == null) {
                    return null;
                }
                flatQuerySet.addAll(flatQuery.getSimpleQueries());
            } else {
                return null;
            }
        }
        flatQuerySet.addAll(andQuery.getSimpleQueries());
        return new And<O>(flatQuerySet);
    }
}
