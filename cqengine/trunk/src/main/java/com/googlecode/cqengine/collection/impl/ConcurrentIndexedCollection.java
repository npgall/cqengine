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
import com.googlecode.cqengine.index.common.Factory;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.*;

/**
 * An implementation of {@link java.util.Set} which additionally wraps {@link QueryEngineInternal}, thus providing
 * {@link #retrieve(com.googlecode.cqengine.query.Query)} methods for performing queries on the collection to retrieve
 * matching objects, and {@link #addIndex(com.googlecode.cqengine.index.Index)} methods allowing indexes to be
 * added to the collection to improve query performance.
 * <p/>
 * This collection takes care of automatically updating any indexes with objects added to/from the collection.
 * <p/>
 * This collection is thread-safe for concurrent reads in all cases.
 * <p/>
 * This collection is thread-safe for concurrent writes in cases where multiple threads might try to add/remove
 * <i>different</i> objects to/from the collection concurrently.
 * <p/>
 * This collection is <b>not</b> thread-safe in cases where two or more threads might try to add or remove the
 * <i>same</i> object to/from the collection concurrently. There is a risk that indexes might get out of sync causing
 * inconsistent results in that scenario with this implementation.
 * <p/>
 * In applications where multiple threads might add/remove the same object concurrently, then the subclass
 * {@link ObjectLockingIndexedCollection} should be used instead. That subclass allows concurrent writes, but with
 * additional safeguards against concurrent modification for the same object, with some additional overhead.
 * <p/>
 * Note that in this context the <i>same object</i> refers to either the same object instance, OR two object instances
 * having the same hash code and being equal according to their {@link #equals(Object)} methods.
 *
 * @author Niall Gallagher
 */
public class ConcurrentIndexedCollection<O> implements IndexedCollection<O> {

    protected final Set<O> collection;
    protected final QueryEngineInternal<O> indexEngine;

    /**
     * Constructor.
     *
     * @param backingSetFactory A factory which will create a concurrent {@link java.util.Set} in which objects
     * added to the indexed collection will be stored
     * @param queryEngine The query engine
     */
    public ConcurrentIndexedCollection(Factory<Set<O>> backingSetFactory, QueryEngineInternal<O> queryEngine) {
        this.collection = backingSetFactory.create();
        queryEngine.init(collection);
        this.indexEngine = queryEngine;
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
        if (modified) {
            indexEngine.notifyObjectsAdded(Collections.singleton(o));
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
            indexEngine.notifyObjectsRemoved(Collections.singleton(o));
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
            indexEngine.notifyObjectsAdded(objects);
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
            indexEngine.notifyObjectsRemoved(objects);
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
        indexEngine.notifyObjectsCleared();
    }

}
