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
package com.googlecode.cqengine.collection.impl;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.engine.ModificationListener;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Wraps another {@link IndexedCollection} and notifies a given {@link ModificationListener} when objects are added
 * to or removed from the backing collection. The listener is invoked by the same thread which modifies the collection.
 * <p/>
 * @author Niall Gallagher
 */
public class ObservableIndexedCollection<O> implements IndexedCollection<O> {

    final IndexedCollection<O> collection;
    final ModificationListener<O> listener;

    public ObservableIndexedCollection(IndexedCollection<O> collection, ModificationListener<O> listener) {
        this.collection = collection;
        this.listener = listener;
    }
    // ----------- Query Engine Methods -------------

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(Query<O> query) {
        return collection.retrieve(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(Query<O> query, Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        return collection.retrieve(query, queryOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIndex(Index<O> index) {
        collection.addIndex(index);
    }

    // ----------- Collection Accessor Methods -------------

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return collection.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] a) {
        //noinspection SuspiciousToArrayCall
        return collection.toArray(a);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return collection.containsAll(c);
    }

    // ----------- Collection Mutator Methods -------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<O> iterator() {
        return new Iterator<O>() {
            private final Iterator<O> collectionIterator = collection.iterator();
            @Override
            public boolean hasNext() {
                return collectionIterator.hasNext();
            }

            private O currentObject = null;
            @Override
            public O next() {
                O next = collectionIterator.next();
                currentObject = next;
                return next;
            }

            @Override
            public void remove() {
                collectionIterator.remove();
                listener.notifyObjectsRemoved(Collections.singleton(currentObject));
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(O o) {
        // Add the object to the index.
        // Indexes handle gracefully the case that the objects supplied already exist in the index...
        boolean modified = collection.add(o);
        if (modified) {
            listener.notifyObjectsAdded(Collections.singleton(o));
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object object) {
        @SuppressWarnings({"unchecked"})
        O o = (O) object;
        boolean modified = collection.remove(o);
        if  (modified) {
            listener.notifyObjectsRemoved(Collections.singleton(o));
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends O> c) {
        @SuppressWarnings({"unchecked"})
        Collection<O> objects = (Collection<O>) c;
        boolean modified = this.collection.addAll(objects);
        if  (modified) {
            listener.notifyObjectsAdded(objects);
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        @SuppressWarnings({"unchecked"})
        Collection<O> objects = (Collection<O>) c;
        boolean modified = this.collection.removeAll(objects);
        if (modified) {
            listener.notifyObjectsRemoved(objects);
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<O> iterator = iterator();
        while (iterator.hasNext()) {
            O next = iterator.next();
            if (!c.contains(next)) {
                iterator.remove(); // Delegates to Iterator returned by iterator() above
                modified = true;
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        collection.clear();
        listener.notifyObjectsCleared();
    }
}
