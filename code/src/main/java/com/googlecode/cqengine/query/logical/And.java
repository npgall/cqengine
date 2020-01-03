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

import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a logical AND on child queries, which when evaluated yields the set intersection of the result sets
 * from child queries.
 * 
 * @author ngallagher
 * @since 2012-04-30 17:00
 */
public class And<O> extends LogicalQuery<O> {

    public And(Collection<Query<O>> childQueries) {
        super(childQueries);
        if (this.size() < 2) {
            throw new IllegalStateException("An 'And' query cannot have fewer than 2 child queries, " + childQueries.size() + " were supplied");
        }
    }

    /**
     * Returns true if and only if all child queries matches the given object, otherwise returns false.
     * @return True if and only if all child queries matches the given object, otherwise returns false
     */
    @Override
    public boolean matches(O object, QueryOptions queryOptions) {
        if (super.hasComparativeQueries()) {
            throw new UnsupportedOperationException("This method is not supported on logical queries which encapsulate comparative queries");
        }
        for (Query<O> query : super.getSimpleQueries()) {
            if (!query.matches(object, queryOptions)) {
                return false;
            }
        }
        for (Query<O> query : super.getLogicalQueries()) {
            if (!query.matches(object, queryOptions)) {
                return false;
            }
        }
        // No queries evaluated false therefore all evaluated true...
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof And)) return false;

        And and = (And) o;

        if (!childQueries.equals(and.childQueries)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        return childQueries.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("and(");
        for (Iterator<Query<O>> iterator = childQueries.iterator(); iterator.hasNext(); ) {
            Query<O> childQuery = iterator.next();
            sb.append(childQuery);
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
