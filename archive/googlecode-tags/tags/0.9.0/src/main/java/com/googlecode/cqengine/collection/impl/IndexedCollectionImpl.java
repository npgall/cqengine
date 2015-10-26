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
import com.googlecode.cqengine.engine.QueryEngineInternal;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Niall Gallagher
 */
public class IndexedCollectionImpl<O> implements IndexedCollection<O> {

    private final Set<O> collection;
    private final QueryEngineInternal<O> indexEngine;

    /**
     * Constructor.
     *
     * @param initialSize The initial size for the collection
     * @param queryEngine The query engine
     */
    public IndexedCollectionImpl(int initialSize, QueryEngineInternal<O> queryEngine) {
        this.collection = createSet(initialSize);
        queryEngine.init(collection);
        this.indexEngine = queryEngine;
    }

    /**
     * Creates a {@link java.util.Set} in which this collection will store objects.
     * <p/>
     * By default, this creates a set via: <code>Collections.newSetFromMap(new ConcurrentHashMap&lt;O, Boolean&gt;(initialSize))</code>
     * <p/>
     * Applications could optionally extend this class and override this method to return an alternative implementation.
     *
     * @param initialSize The initial size for the set
     * @return A new instance of the {@link java.util.Set}
     */
    protected Set<O> createSet(int initialSize) {
        return Collections.newSetFromMap(new ConcurrentHashMap<O, Boolean>(initialSize));
    }


    // ----------- Query Engine Methods -------------

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(Query<O> query) {
        return indexEngine.retrieve(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(Query<O> query, Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        return indexEngine.retrieve(query, queryOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIndex(Index<O> index) {
        indexEngine.addIndex(index);
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
                indexEngine.notifyObjectsRemoved(Collections.singleton(currentObject));
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
        indexEngine.notifyObjectsAdded(Collections.singleton(o));
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
        indexEngine.notifyObjectsRemoved(Collections.singleton(o));
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
        indexEngine.notifyObjectsAdded(objects);
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
        indexEngine.notifyObjectsRemoved(objects);
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
        indexEngine.notifyObjectsCleared();
    }

}
