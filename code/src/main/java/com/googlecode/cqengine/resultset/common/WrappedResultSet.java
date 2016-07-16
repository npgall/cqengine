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
package com.googlecode.cqengine.resultset.common;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Iterator;

/**
 * A ResultSet which wraps another. Subclasses may extend this, to override some methods.
 *
 * @author niall.gallagher
 */
public class WrappedResultSet<O> extends ResultSet<O> {

    protected final ResultSet<O> wrappedResultSet;

    public WrappedResultSet(ResultSet<O> wrappedResultSet) {
        this.wrappedResultSet = wrappedResultSet;
    }

    @Override
    public Iterator<O> iterator() {
        return wrappedResultSet.iterator();
    }

    @Override
    public boolean contains(O object) {
        return wrappedResultSet.contains(object);
    }

    @Override
    public boolean matches(O object) {
        return wrappedResultSet.matches(object);
    }

    @Override
    public int size() {
        return wrappedResultSet.size();
    }

    @Override
    public int getRetrievalCost() {
        return wrappedResultSet.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
        return wrappedResultSet.getMergeCost();
    }

    @Override
    public void close() {
        wrappedResultSet.close();
    }

    @Override
    public Query<O> getQuery() {
        return wrappedResultSet.getQuery();
    }

    @Override
    public QueryOptions getQueryOptions() {
        return wrappedResultSet.getQueryOptions();
    }
}
