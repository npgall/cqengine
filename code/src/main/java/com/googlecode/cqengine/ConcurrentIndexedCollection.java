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
package com.googlecode.cqengine;

import com.googlecode.cqengine.engine.QueryEngineInternal;
import com.googlecode.cqengine.engine.CollectionQueryEngine;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.index.support.CloseableRequestResources;
import com.googlecode.cqengine.metadata.MetadataEngine;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectStoreAsSet;
import com.googlecode.cqengine.persistence.support.PersistenceFlags;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.FlagsEnabled;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.closeable.CloseableResultSet;

import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.queryOptions;
import static java.util.Collections.singleton;

/**
 * An implementation of {@link java.util.Set} and {@link com.googlecode.cqengine.engine.QueryEngine}, thus providing
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

    protected final Persistence<O, ?> persistence;
    protected final ObjectStore<O> objectStore;
    protected final QueryEngineInternal<O> indexEngine;
    protected final MetadataEngine<O> metadataEngine;

    /**
     * Creates a new {@link ConcurrentIndexedCollection} with default settings, using {@link OnHeapPersistence}.
     */
    @SuppressWarnings("unchecked")
    public ConcurrentIndexedCollection() {
        this(OnHeapPersistence.<O>withoutPrimaryKey());
    }

    /**
     * Creates a new {@link ConcurrentIndexedCollection} which will use the given persistence to create the backing set.
     *
     * @param persistence The {@link Persistence} implementation which will create a concurrent {@link java.util.Set}
     *                    in which objects added to the indexed collection will be stored, and which will provide
     *                    access to the underlying storage of indexes.
     */
    public ConcurrentIndexedCollection(Persistence<O, ? extends Comparable> persistence) {
        this.persistence = persistence;
        this.objectStore = persistence.createObjectStore();
        QueryEngineInternal<O> queryEngine = new CollectionQueryEngine<O>();
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            queryEngine.init(objectStore, queryOptions);
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
        this.indexEngine = queryEngine;
        this.metadataEngine = new MetadataEngine<>(
                this,
                () -> openRequestScopeResourcesIfNecessary(null),
                this::closeRequestScopeResourcesIfNecessary
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Persistence<O, ?> getPersistence() {
        return persistence;
    }

    @Override
    public MetadataEngine<O> getMetadataEngine() {
        return metadataEngine;
    }

    // ----------- Query Engine Methods -------------

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(Query<O> query) {
        return retrieve(query, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        final QueryOptions finalQueryOptions = openRequestScopeResourcesIfNecessary(queryOptions);
        flagAsReadRequest(finalQueryOptions);
        ResultSet<O> results = indexEngine.retrieve(query, finalQueryOptions);
        return new CloseableResultSet<O>(results, query, finalQueryOptions) {
            @Override
            public void close() {
                super.close();
                closeRequestScopeResourcesIfNecessary(finalQueryOptions);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(Iterable<O> objectsToRemove, Iterable<O> objectsToAdd) {
        return update(objectsToRemove, objectsToAdd, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(Iterable<O> objectsToRemove, Iterable<O> objectsToAdd, QueryOptions queryOptions) {
        queryOptions = openRequestScopeResourcesIfNecessary(queryOptions);
        try {
            boolean modified = doRemoveAll(objectsToRemove, queryOptions);
            return doAddAll(objectsToAdd, queryOptions) || modified;
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIndex(Index<O> index) {
        addIndex(index, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIndex(Index<O> index, QueryOptions queryOptions) {
        queryOptions = openRequestScopeResourcesIfNecessary(queryOptions);
        try {
            indexEngine.addIndex(index, queryOptions);
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeIndex(Index<O> index) {
        removeIndex(index, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeIndex(Index<O> index, QueryOptions queryOptions) {
        queryOptions = openRequestScopeResourcesIfNecessary(queryOptions);
        try {
            indexEngine.removeIndex(index, queryOptions);
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    @Override
    public Iterable<Index<O>> getIndexes() {
        return indexEngine.getIndexes();
    }

    // ----------- Collection Accessor Methods -------------

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            return objectStore.size(queryOptions);
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            return objectStore.isEmpty(queryOptions);
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            return objectStore.contains(o, queryOptions);
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            return getObjectStoreAsSet(queryOptions).toArray();
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] a) {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            //noinspection SuspiciousToArrayCall
            return getObjectStoreAsSet(queryOptions).toArray(a);
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            return objectStore.containsAll(c, queryOptions);
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    // ----------- Collection Mutator Methods -------------

    /**
     * {@inheritDoc}
     */
    @Override
    public CloseableIterator<O> iterator() {
        return new CloseableIterator<O>() {
            final QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);

            private final CloseableIterator<O> collectionIterator = objectStore.iterator(queryOptions);
            boolean autoClosed = false;
            @Override
            public boolean hasNext() {
                boolean hasNext = collectionIterator.hasNext();
                if (!hasNext) {
                    close();
                    autoClosed = true;
                }
                return hasNext;
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
                if (currentObject == null) {
                    throw new IllegalStateException();
                }
                // Handle an edge case where we might have retrieved the last object and called close() automatically,
                // but then the application calls remove() so we have to reopen request-scope resources temporarily
                // to remove the last object...
                if (autoClosed) {
                    ConcurrentIndexedCollection.this.remove(currentObject); // reopens resources temporarily
                }
                else {
                    doRemoveAll(Collections.singleton(currentObject), queryOptions); // uses existing resources
                }
                currentObject = null;
            }

            @Override
            public void close() {
                CloseableRequestResources.closeQuietly(collectionIterator);
                closeRequestScopeResourcesIfNecessary(queryOptions);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(O o) {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            // Add the object to the index.
            // Indexes handle gracefully the case that the objects supplied already exist in the index...
            boolean modified = objectStore.add(o, queryOptions);
            indexEngine.addAll(ObjectSet.fromCollection(singleton(o)), queryOptions);
            return modified;
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object object) {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            @SuppressWarnings({"unchecked"})
            O o = (O) object;
            boolean modified = objectStore.remove(o, queryOptions);
            indexEngine.removeAll(ObjectSet.fromCollection(singleton(o)), queryOptions);
            return modified;
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends O> c) {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            @SuppressWarnings({"unchecked"})
            Collection<O> objects = (Collection<O>) c;
            boolean modified = objectStore.addAll(objects, queryOptions);
            indexEngine.addAll(ObjectSet.fromCollection(objects), queryOptions);
            return modified;
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            @SuppressWarnings({"unchecked"})
            Collection<O> objects = (Collection<O>) c;
            boolean modified = objectStore.removeAll(objects, queryOptions);
            indexEngine.removeAll(ObjectSet.fromCollection(objects), queryOptions);
            return modified;
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        CloseableIterator<O> iterator = null;
        try {
            boolean modified = false;
            iterator = objectStore.iterator(queryOptions);
            while (iterator.hasNext()) {
                O next = iterator.next();
                if (!c.contains(next)) {
                    doRemoveAll(Collections.singleton(next), queryOptions);
                    modified = true;
                }
            }
            return modified;
        }
        finally {
            CloseableRequestResources.closeQuietly(iterator);
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            objectStore.clear(queryOptions);
            indexEngine.clear(queryOptions);
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    boolean doAddAll(Iterable<O> objects, QueryOptions queryOptions) {
        if (objects instanceof Collection) {
            Collection<O> c = (Collection<O>) objects;
            boolean modified = objectStore.addAll(c, queryOptions);
            indexEngine.addAll(ObjectSet.fromCollection(c), queryOptions);
            return modified;
        }
        else {
            boolean modified = false;
            for (O object : objects) {
                boolean added = objectStore.add(object, queryOptions);
                indexEngine.addAll(ObjectSet.fromCollection(singleton(object)), queryOptions);
                modified = added || modified;
            }
            return modified;
        }
    }

    boolean doRemoveAll(Iterable<O> objects, QueryOptions queryOptions) {
        if (objects instanceof Collection) {
            Collection<O> c = (Collection<O>) objects;
            boolean modified = objectStore.removeAll(c, queryOptions);
            indexEngine.removeAll(ObjectSet.fromCollection(c), queryOptions);
            return modified;
        } else {
            boolean modified = false;
            for (O object : objects) {
                boolean removed = objectStore.remove(object, queryOptions);
                indexEngine.removeAll(ObjectSet.fromCollection(singleton(object)), queryOptions);
                modified = removed || modified;
            }
            return modified;
        }
    }

    protected QueryOptions openRequestScopeResourcesIfNecessary(QueryOptions queryOptions) {
        if (queryOptions == null) {
            queryOptions = new QueryOptions();
        }
        if (!(persistence instanceof OnHeapPersistence)) {
            persistence.openRequestScopeResources(queryOptions);
        }
        queryOptions.put(Persistence.class, persistence);
        return queryOptions;
    }

    protected void closeRequestScopeResourcesIfNecessary(QueryOptions queryOptions) {
        if (!(persistence instanceof OnHeapPersistence)) {
            persistence.closeRequestScopeResources(queryOptions);
        }
    }


    protected ObjectStoreAsSet<O> getObjectStoreAsSet(QueryOptions queryOptions) {
        return new ObjectStoreAsSet<O>(objectStore, queryOptions);
    }

    @Override
    public boolean equals(Object o) {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            if (this == o) return true;
            if (!(o instanceof Set)) return false;
            Set that = (Set) o;

            if (!getObjectStoreAsSet(queryOptions).equals(that)) return false;

            return true;
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    @Override
    public int hashCode() {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            return getObjectStoreAsSet(queryOptions).hashCode();
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    @Override
    public String toString() {
        QueryOptions queryOptions = openRequestScopeResourcesIfNecessary(null);
        try {
            return getObjectStoreAsSet(queryOptions).toString();
        }
        finally {
            closeRequestScopeResourcesIfNecessary(queryOptions);
        }
    }

    /**
     * Sets a flag into the given query options to record that this request will read from the collection
     * but will not modify it.
     * This is used to facilitate locking in some persistence implementations.
     *
     * @param queryOptions The query options for the request
     */
    protected static void flagAsReadRequest(QueryOptions queryOptions) {
        FlagsEnabled.forQueryOptions(queryOptions).add(PersistenceFlags.READ_REQUEST);
    }
}
