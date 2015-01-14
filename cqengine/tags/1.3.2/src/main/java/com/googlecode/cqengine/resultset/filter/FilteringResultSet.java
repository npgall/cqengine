/**
 * Copyright 2012 Niall Gallagher
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
package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.Collection;
import java.util.Iterator;

/**
 * A {@link ResultSet} which wraps an {@link Iterable} and which calls an abstract {@link #isValid(Object)} method
 * for each object. Returns the object if the implementation of the method returns true, or skips to the
 * next object if the method returns false.
 *
 * @author Niall Gallagher
 */
public abstract class FilteringResultSet<O> extends ResultSet<O> {

    private final ResultSet<O> wrappedResultSet;

    public FilteringResultSet(ResultSet<O> wrappedResultSet) {
        this.wrappedResultSet = wrappedResultSet;
    }

    @Override
    public Iterator<O> iterator() {
        return new FilteringIterator<O>(wrappedResultSet.iterator()) {
            @Override
            public boolean isValid(O object) {
                return FilteringResultSet.this.isValid(object);
            }
        };
    }

    @Override
    public final boolean contains(O object) {
        // Check if this ResultSet contains the given object by iterating the ResultSet...
        return IteratorUtil.iterableContains(this, object);
    }

    public abstract boolean isValid(O object);

    @Override
    public int size() {
        return IteratorUtil.countElements(this);
    }

    @Override
    public int getRetrievalCost() {
        return wrappedResultSet.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
        return wrappedResultSet.getMergeCost();
    }
}
