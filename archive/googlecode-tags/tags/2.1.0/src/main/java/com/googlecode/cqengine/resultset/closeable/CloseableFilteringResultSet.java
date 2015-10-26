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
import com.googlecode.cqengine.resultset.filter.FilteringResultSet;

import java.io.Closeable;
import java.util.Iterator;

/**
 * A FilteringResultSet which throws exceptions if an attempt to use it is made after its {@link #close} method has
 * been called.
 */
public abstract class CloseableFilteringResultSet<O> extends FilteringResultSet<O> implements Closeable {

    boolean closed = false;

    public CloseableFilteringResultSet(ResultSet<O> wrappedResultSet, Query<O> query, QueryOptions queryOptions) {
        super(wrappedResultSet, query, queryOptions);
    }

    @Override
    public Iterator<O> iterator() {
        ensureNotClosed();
        return super.iterator();
    }

    @Override
    public int size() {
        ensureNotClosed();
        return super.size();
    }

    @Override
    public int getRetrievalCost() {
        ensureNotClosed();
        return super.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
        ensureNotClosed();
        return super.getMergeCost();
    }

    @Override
    public O uniqueResult() {
        ensureNotClosed();
        return super.uniqueResult();
    }

    @Override
    public boolean isEmpty() {
        ensureNotClosed();
        return super.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        ensureNotClosed();
        return super.isNotEmpty();
    }

    void ensureNotClosed() {
        if (closed) {
            throw new IllegalStateException("ResultSet is closed");
        }
    }

    @Override
    public void close() {
        super.close();
        closed = true;
    }
}
