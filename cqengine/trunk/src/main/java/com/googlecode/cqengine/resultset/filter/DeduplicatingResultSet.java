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

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.Iterator;

/**
 * A {@link ResultSet} which wraps another, to suppress duplicate objects, where a duplicate object is one which has
 * the same value(s) for the given attribute.
 * <p/>
 * Say an object had an attribute {@code COLOR}. This result set would return only one object of each color from the
 * wrapped result set.
 * <p/>
 * Only one of the potentially duplicate objects will be returned, but which one is unspecified (the query engine is
 * usually free for performance reasons to return objects in any order).
 *
 * @author Niall Gallagher
 */
public class DeduplicatingResultSet<O, A> extends ResultSet<O> {

    private final ResultSet<O> wrappedResultSet;

    private final Attribute<O, A> uniqueAttribute;

    public DeduplicatingResultSet(Attribute<O, A> uniqueAttribute, ResultSet<O> wrappedResultSet) {
        this.uniqueAttribute = uniqueAttribute;
        this.wrappedResultSet = wrappedResultSet;
    }

    @Override
    public Iterator<O> iterator() {
        return new DeduplicatingIterator<O, A>(uniqueAttribute, wrappedResultSet.iterator());
    }

    @Override
    public final boolean contains(O object) {
        // Check if this ResultSet contains the given object by iterating the ResultSet...
        return IteratorUtil.iterableContains(this, object);
    }

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
