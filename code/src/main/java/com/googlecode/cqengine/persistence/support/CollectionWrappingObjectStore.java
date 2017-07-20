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

import java.util.Collection;
import java.util.Iterator;

/**
 * An {@link ObjectStore} which wraps a {@link Collection} and delegates all method calls to the collection.
 *
 * @author niall.gallagher
 */
public class CollectionWrappingObjectStore<O> implements ObjectStore<O> {

    final Collection<O> backingCollection;

    public CollectionWrappingObjectStore(Collection<O> backingCollection) {
        this.backingCollection = backingCollection;
    }

    public Collection<O> getBackingCollection() {
        return backingCollection;
    }

    @Override
    public int size(QueryOptions queryOptions) {
        return backingCollection.size();
    }

    @Override
    public boolean contains(Object o, QueryOptions queryOptions) {
        return backingCollection.contains(o);
    }

    @Override
    public CloseableIterator<O> iterator(QueryOptions queryOptions) {
        final Iterator<O> i = backingCollection.iterator();
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
        return backingCollection.isEmpty();
    }

    @Override
    public boolean add(O object, QueryOptions queryOptions) {
        return backingCollection.add(object);
    }

    @Override
    public boolean remove(Object o, QueryOptions queryOptions) {
        return backingCollection.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c, QueryOptions queryOptions) {
        return backingCollection.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends O> c, QueryOptions queryOptions) {
        return backingCollection.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c, QueryOptions queryOptions) {
        return backingCollection.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c, QueryOptions queryOptions) {
        // The following code is a workaround for a performance bottleneck in JDK 8 and earlier.

        // See the following issues for details:
        // JDK - https://bugs.openjdk.java.net/browse/JDK-8160751
        // CQEngine - https://github.com/npgall/cqengine/issues/154

        // We avoid calling backingCollection.removeAll().
        // The backingCollection is typically backed by a ConcurrentHashMap,
        // due to being created via Collections.newSetFromMap(new ConcurrentHashMap<O, Boolean>()).
        // Thus we cannot rely on the good performance of backingCollection.removeAll()
        // when CQEngine is run on JDK 8 or earlier.

        boolean modified = false;
        for (Object e : c) {
            modified |= backingCollection.remove(e);
        }
        return modified;
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        backingCollection.clear();
    }

}
