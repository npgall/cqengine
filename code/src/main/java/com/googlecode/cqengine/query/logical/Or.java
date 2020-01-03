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
 * Represents a logical OR on child queries, which when evaluated yields the set union of the result sets
 * from child queries.
 *
 * @author ngallagher
 * @since 2012-04-30 17:00
 */
public class Or<O> extends LogicalQuery<O> {

    private final boolean disjoint;

    /**
     * Constructor.
     * <p/>
     * Delegates to {@link #Or(java.util.Collection, boolean)} supplying false for disjointness.
     *
     * @param childQueries Child queries for which a set union is required
     */
    public Or(Collection<Query<O>> childQueries) {
        this(childQueries, false);
    }
    /**
     * Constructor with a hint regarding deduplication.
     *
     * @param childQueries Child queries for which a set union is required
     * @param disjoint A hint to the query engine: if true indicates that results from the child queries will be
     * disjoint and so there will be no need to perform deduplication; if false disjointness is unknown, deduplication
     * might be required
     */
    public Or(Collection<Query<O>> childQueries, boolean disjoint) {
        super(childQueries);
        if (this.size() < 2) {
            throw new IllegalStateException("An 'Or' query cannot have fewer than 2 child queries, " + childQueries.size() + " were supplied");
        }
        this.disjoint = disjoint;
    }

    /**
     * Returns true if at least one child query matches the given object, returns false if none match.
     * @return true if at least one child query matches the given object, returns false if none match
     */
    @Override
    public boolean matches(O object, QueryOptions queryOptions) {
        if (super.hasComparativeQueries()) {
            throw new UnsupportedOperationException("This method is not supported on logical queries which encapsulate comparative queries");
        }
        for (Query<O> query : super.getSimpleQueries()) {
            if (query.matches(object, queryOptions)) {
                return true;
            }
        }
        for (Query<O> query : super.getLogicalQueries()) {
            if (query.matches(object, queryOptions)) {
                return true;
            }
        }
        // No queries evaluated true...
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Or)) return false;

        Or or = (Or) o;

        if (disjoint != or.disjoint) return false;
        if (!childQueries.equals(or.childQueries)) return false;

        return true;
    }

    /**
     * Returns the value of the <code>disjoint</code> flag supplied to the constructor
     * {@link #Or(java.util.Collection, boolean)}.
     * @return The value of the <code>disjoint</code> flag supplied to the constructor
     */
    public boolean isDisjoint() {
        return disjoint;
    }

    @Override
    protected int calcHashCode() {
        int result = childQueries.hashCode();
        result = 31 * result + (disjoint ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("or(");
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
