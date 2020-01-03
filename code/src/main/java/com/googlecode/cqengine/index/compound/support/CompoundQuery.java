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
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Equal;
import com.googlecode.cqengine.query.simple.SimpleQuery;
import com.googlecode.cqengine.query.logical.And;

import java.util.ArrayList;
import java.util.List;

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
        List<Object> attributeValues = new ArrayList<Object>(andQuery.getSimpleQueries().size());
        for (SimpleQuery<O, ?> simpleQuery : andQuery.getSimpleQueries()) {
            Equal<O, ?> equal = (Equal<O, ?>) simpleQuery;
            attributeValues.add(equal.getValue());
        }
        return new CompoundValueTuple<O>(attributeValues);
    }

    public static <O> CompoundQuery<O> fromAndQueryIfSuitable(And<O> andQuery) {
        if (andQuery.hasLogicalQueries() || andQuery.hasComparativeQueries()) {
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

}
