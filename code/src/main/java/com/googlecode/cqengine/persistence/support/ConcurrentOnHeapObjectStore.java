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
package com.googlecode.cqengine.persistence.support;

import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An {@link ObjectStore} which wraps a concurrent implementation of {@link java.util.Set}.
 */
public class ConcurrentOnHeapObjectStore<O> implements ObjectStore<O> {

    final Set<O> set;

    public ConcurrentOnHeapObjectStore() {
        this.set = Collections.newSetFromMap(new ConcurrentHashMap<O, Boolean>());
    }

    public ConcurrentOnHeapObjectStore(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.set = Collections.newSetFromMap(new ConcurrentHashMap<O, Boolean>(initialCapacity, loadFactor, concurrencyLevel));
    }

    @Override
    public int size(QueryOptions queryOptions) {
        return set.size();
    }

    @Override
    public boolean contains(Object o, QueryOptions queryOptions) {
        return set.contains(o);
    }

    @Override
    public CloseableIterator<O> iterator(QueryOptions queryOptions) {
        final Iterator<O> i = set.iterator();
        return new CloseableIterator<O>() {
            @Override
            public void close() {
                // No op
            }

            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public O next() {
                return i.next();
            }

            @Override
            public void remove() {
                i.remove();
            }
        };
    }

    @Override
    public boolean isEmpty(QueryOptions queryOptions) {
        return set.isEmpty();
    }

    @Override
    public boolean add(O object, QueryOptions queryOptions) {
        return set.add(object);
    }

    @Override
    public boolean remove(Object o, QueryOptions queryOptions) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c, QueryOptions queryOptions) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends O> c, QueryOptions queryOptions) {
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c, QueryOptions queryOptions) {
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c, QueryOptions queryOptions) {
        return set.removeAll(c);
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        set.clear();
    }

}
