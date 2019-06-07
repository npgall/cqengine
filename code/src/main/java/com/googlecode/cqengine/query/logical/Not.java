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
package com.googlecode.cqengine.query.logical;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Collections;

import static java.util.Objects.requireNonNull;

/**
 * Represents a logical negation on a child query, which when evaluated yields the set complement of the result set
 * from the child query.
 *
 * @author ngallagher
 * @since 2012-04-30 17:00
 */
public class Not<O> extends LogicalQuery<O> {

    private final Query<O> negatedQuery;

    public Not(Query<O> negatedQuery) {
        super(Collections.singleton(requireNonNull(negatedQuery, "The negated query cannot be null")));
        this.negatedQuery = negatedQuery;
    }

    public Query<O> getNegatedQuery() {
        return negatedQuery;
    }

    /**
     * Returns the <i>inverse</i> of whether the negated query matches the given object.
     * @return The <i>inverse</i> of whether the negated query matches the given object
     */
    @Override
    public boolean matches(O object, QueryOptions queryOptions) {
        return !negatedQuery.matches(object, queryOptions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Not)) return false;

        Not other = (Not) o;

        if (!negatedQuery.equals(other.negatedQuery)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        return negatedQuery.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("not(");
        sb.append(negatedQuery);
        sb.append(")");
        return sb.toString();
    }
}
