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
package com.googlecode.cqengine.resultset.closeable;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.io.Closeable;
import java.util.Iterator;

/**
 * A ResultSet which throws exceptions if an attempt to use it is made after its {@link #close} method has been called.
 */
public class CloseableResultSet<O> extends ResultSet<O> implements Closeable {

    final ResultSet<O> wrapped;
    final Query<O> query;
    final QueryOptions queryOptions;
    boolean closed = false;

    public CloseableResultSet(ResultSet<O> wrapped, Query<O> query, QueryOptions queryOptions) {
        this.wrapped = wrapped;
        this.query = query;
        this.queryOptions = queryOptions;
    }

    @Override
    public Iterator<O> iterator() {
        ensureNotClosed();
        return wrapped.iterator();
    }

    @Override
    public boolean contains(O object) {
        ensureNotClosed();
        return wrapped.contains(object);
    }

    @Override
    public boolean matches(O object) {
        ensureNotClosed();
        return query.matches(object, queryOptions);
    }

    @Override
    public int size() {
        ensureNotClosed();
        return wrapped.size();
    }

    @Override
    public int getRetrievalCost() {
        ensureNotClosed();
        return wrapped.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
        ensureNotClosed();
        return wrapped.getMergeCost();
    }

    @Override
    public O uniqueResult() {
        ensureNotClosed();
        return wrapped.uniqueResult();
    }

    @Override
    public boolean isEmpty() {
        ensureNotClosed();
        return wrapped.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        ensureNotClosed();
        return wrapped.isNotEmpty();
    }

    void ensureNotClosed() {
        if (closed) {
            throw new IllegalStateException("ResultSet is closed");
        }
    }

    @Override
    public void close() {
        wrapped.close();
        closed = true;
    }

    @Override
    public Query<O> getQuery() {
        return query;
    }

    @Override
    public QueryOptions getQueryOptions() {
        return queryOptions;
    }
}
