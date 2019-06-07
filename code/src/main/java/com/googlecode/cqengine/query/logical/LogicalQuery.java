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
import com.googlecode.cqengine.query.simple.SimpleQuery;

import java.util.*;

/**
 * The superclass of {@link Query}s which logically connect other queries together to form complex queries.
 *
 * @param <O> The type of the object containing the attributes on which child queries in the query make assertions
 *
 * @author ngallagher
 * @since 2012-04-30 16:56
 */
public abstract class LogicalQuery<O> implements Query<O> {

    protected final Collection<Query<O>> childQueries;
    private final List<LogicalQuery<O>> logicalQueries = new ArrayList<LogicalQuery<O>>();
    private final List<SimpleQuery<O, ?>> simpleQueries = new ArrayList<SimpleQuery<O, ?>>();
    private final boolean hasLogicalQueries;
    private final boolean hasSimpleQueries;
    private final int size;
    // Lazy calculate and cache hash code...
    private transient int cachedHashCode = 0;

    /**
     * Creates a new {@link LogicalQuery} initialized to logically connect the supplied set of child queries.
     *
     * @param childQueries The child queries which this {@code LogicalQuery} is to logically connect
     */
    public LogicalQuery(Collection<Query<O>> childQueries) {
        Objects.requireNonNull(childQueries, "The child queries supplied to a logical query cannot be null");
        for (Query<O> query : childQueries) {
            if (query instanceof LogicalQuery) {
                logicalQueries.add((LogicalQuery<O>) query);
            }
            else if (query instanceof SimpleQuery) {
                simpleQueries.add((SimpleQuery<O, ?>) query);
            }
            else {
                throw new IllegalStateException("Unexpected type of query: " + (query == null ? null : query + ", " + query.getClass()));
            }
        }
        this.hasLogicalQueries = !logicalQueries.isEmpty();
        this.hasSimpleQueries = !simpleQueries.isEmpty();
        this.size = childQueries.size();
        this.childQueries = childQueries;
    }

    /**
     * Returns a collection of child queries which are themselves {@link LogicalQuery}s.
     * @return a collection of child queries which are themselves {@link LogicalQuery}s
     */
    public List<LogicalQuery<O>> getLogicalQueries() {
        return logicalQueries;
    }

    /**
     * Returns a collection of child queries which are themselves {@link SimpleQuery}s.
     * @return a collection of child queries which are themselves {@link SimpleQuery}s
     */
    public List<SimpleQuery<O, ?>> getSimpleQueries() {
        return simpleQueries;
    }

    /**
     * The entire collection of child queries, which may be either {@link SimpleQuery}s or {@link LogicalQuery}s.
     * @return The entire collection of child queries, which may be either {@link SimpleQuery}s or {@link LogicalQuery}s.
     */
    public Collection<Query<O>> getChildQueries() {
        return childQueries;
    }

    /**
     * Returns true if this logical query has child queries which are themselves {@link LogicalQuery}s.
     * @return true if this logical query has child queries which are themselves {@link LogicalQuery}s,
     * false if this logical query has no child queries which are themselves {@link LogicalQuery}s
     */
    public boolean hasLogicalQueries() {
        return hasLogicalQueries;
    }

    /**
     * Returns true if this logical query has child queries which are themselves {@link SimpleQuery}s.
     * @return true if this logical query has child queries which are themselves {@link SimpleQuery}s,
     * false if this logical query has no child queries which are themselves {@link SimpleQuery}s
     */
    public boolean hasSimpleQueries() {
        return hasSimpleQueries;
    }

    /**
     * Returns the total number of child queries (both logical and simple).
     * @return the total number of child queries (both logical and simple)
     */
    public int size() {
        return size;
    }

    @Override
    public int hashCode() {
        // Lazy calculate and cache hash code...
        int h = this.cachedHashCode;
        if (h == 0) { // hash code not cached
            h = calcHashCode();
            if (h == 0) { // 0 is normally a valid hash code, coalesce with another...
                h = -976419167; // was randomly chosen
            }
            this.cachedHashCode = h; // cache hash code
        }
        return h;
    }

    abstract protected int calcHashCode();

}
