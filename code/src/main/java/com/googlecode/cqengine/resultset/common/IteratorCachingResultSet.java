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
 * A {@link ResultSet} which wraps a backing ResultSet, and which caches the iterator returned by
 * the backing ResultSet, to enable repeated invocations on {@code IteratorCachingResultSet.iterator().hasNext()},
 * to avoid each time requesting a new iterator from the backing ResultSet.
 * <p/>
 * An effect of this caching is that if the application calls {@link #isEmpty()} or {@link #isNotEmpty()} on
 * this ResultSet before it begins iteration, only one iterator will actually be requested from the backing ResultSet
 * because the cached iterator will be reused each time.
 * <p/>
 * On the other hand, whenever the application actually begins to iterate through results, this ResultSet will
 * detect it, and if {@link #iterator()} is invoked again, it will avoid returning the same cached iterator
 * and will obtain a new iterator instead.
 *
 * @author niall.gallagher
 */
public class IteratorCachingResultSet<O> extends WrappedResultSet<O> {

    public IteratorCachingResultSet(ResultSet<O> backingResultSet) {
        super(backingResultSet);
    }

    Iterator<O> cachedIterator = null;


    @Override
    public Iterator<O> iterator() {
        if (cachedIterator != null) {
            return cachedIterator;
        }
        final Iterator<O> backingIterator = wrappedResultSet.iterator();
        Iterator<O> wrappingIterator = new Iterator<O>() {

            @Override
            public boolean hasNext() {
                return backingIterator.hasNext();
            }

            @Override
            public O next() {
                cachedIterator = null;
                return backingIterator.next();
            }

            @Override
            public void remove() {
                cachedIterator = null;
                backingIterator.remove();
            }
        };
        this.cachedIterator = wrappingIterator;
        return wrappingIterator;
    }
}
