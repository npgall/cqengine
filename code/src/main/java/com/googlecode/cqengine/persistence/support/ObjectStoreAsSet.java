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
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.io.IOException;
import java.util.AbstractSet;
import java.util.Collection;

/**
 * Adapts an {@link ObjectStore} to behave like a {@link java.util.Set}, using an existing connection to the
 * persistence when methods belonging to the Set interface are called.
 */
public class ObjectStoreAsSet<O> extends AbstractSet<O> {

    final ObjectStore<O> objectStore;
    final QueryOptions queryOptions;

    public ObjectStoreAsSet(ObjectStore<O> objectStore, QueryOptions queryOptions) {
        this.objectStore = objectStore;
        this.queryOptions = queryOptions;
    }

    @Override
    public int size() {
        return objectStore.size(queryOptions);
    }

    @Override
    public boolean contains(Object o) {
        return objectStore.contains(o, queryOptions);
    }

    @Override
    public CloseableIterator<O> iterator() {
        return objectStore.iterator(queryOptions);
    }

    @Override
    public boolean isEmpty() {
        return objectStore.isEmpty(queryOptions);
    }

    @Override
    public boolean add(O object) {
        return objectStore.add(object, queryOptions);
    }

    @Override
    public boolean remove(Object o) {
        return objectStore.remove(o, queryOptions);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return objectStore.containsAll(c, queryOptions);
    }

    @Override
    public boolean addAll(Collection<? extends O> c) {
        return objectStore.addAll(c, queryOptions);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return objectStore.retainAll(c, queryOptions);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return objectStore.removeAll(c, queryOptions);
    }

    @Override
    public void clear() {
        objectStore.clear(queryOptions);
    }
}
