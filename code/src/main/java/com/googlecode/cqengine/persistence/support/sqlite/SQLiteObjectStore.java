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
package com.googlecode.cqengine.persistence.support.sqlite;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.sqlite.SQLiteIdentityIndex;
import com.googlecode.cqengine.index.sqlite.SQLitePersistence;
import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.has;

/**
 * @author niall.gallagher
 */
public class SQLiteObjectStore<O, A extends Comparable<A>> implements ObjectStore<O> {

    final SQLitePersistence<O, A> persistence;
    final SQLiteIdentityIndex<A, O> backingIndex;
    final SimpleAttribute<O, A> primaryKeyAttribute;
    final Class<O> objectType;

    public SQLiteObjectStore(final SQLitePersistence<O, A> persistence) {
        this.persistence = persistence;
        this.objectType = persistence.getPrimaryKeyAttribute().getObjectType();
        this.primaryKeyAttribute = persistence.getPrimaryKeyAttribute();
        this.backingIndex = persistence.createIdentityIndex();
    }

    public void init(QueryOptions queryOptions) {
        backingIndex.init(this, queryOptions);
    }

    public SQLitePersistence<O, A> getPersistence() {
        return persistence;
    }

    public SQLiteIdentityIndex<A, O> getBackingIndex() {
        return backingIndex;
    }

    @Override
    public int size(QueryOptions queryOptions) {
        return backingIndex.retrieve(has(primaryKeyAttribute), queryOptions).size();
    }

    @Override
    public boolean contains(Object o, QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        O object = (O) o;
        A objectId = primaryKeyAttribute.getValue(object, queryOptions);
        return backingIndex.retrieve(equal(primaryKeyAttribute, objectId), queryOptions).size() > 0;
    }

    @Override
    public CloseableIterator<O> iterator(QueryOptions queryOptions) {
        final ResultSet<O> rs = backingIndex.retrieve(has(primaryKeyAttribute), queryOptions);
        final Iterator<O> i = rs.iterator();
        class CloseableIteratorImpl extends UnmodifiableIterator<O> implements CloseableIterator<O> {

            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public O next() {
                return i.next();
            }

            @Override
            public void close() {
                rs.close();
            }
        }
        return new CloseableIteratorImpl();
    }

    @Override
    public boolean isEmpty(QueryOptions queryOptions) {
        return size(queryOptions) == 0;
    }

    @Override
    public boolean add(O object, QueryOptions queryOptions) {
        return backingIndex.addAll(ObjectSet.fromCollection(Collections.singleton(object)), queryOptions);
    }

    @Override
    public boolean remove(Object o, QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        O object = (O) o;
        return backingIndex.removeAll(ObjectSet.fromCollection(Collections.singleton(object)), queryOptions);
    }

    @Override
    public boolean containsAll(Collection<?> c, QueryOptions queryOptions) {
        for (Object o : c) {
            if (!contains(o, queryOptions)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends O> c, QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        Collection<O> objects = (Collection<O>) c;
        return backingIndex.addAll(ObjectSet.fromCollection(objects), queryOptions);
    }

    @Override
    public boolean retainAll(Collection<?> c, QueryOptions queryOptions) {
        // Note: this could be optimized...
        Collection<O> objectsToRemove = new ArrayList<O>();
        ResultSet<O> allObjects = backingIndex.retrieve(has(primaryKeyAttribute), queryOptions);
        try {
            for (O object : allObjects) {
                if (!c.contains(object)) {
                    objectsToRemove.add(object);
                }
            }
        }
        finally {
            allObjects.close();
        }
        return backingIndex.removeAll(ObjectSet.fromCollection(objectsToRemove), queryOptions);
    }

    @Override
    public boolean removeAll(Collection<?> c, QueryOptions queryOptions) {
        @SuppressWarnings("unchecked")
        Collection<O> objects = (Collection<O>) c;
        return backingIndex.removeAll(ObjectSet.fromCollection(objects), queryOptions);
    }

    @Override
    public void clear(QueryOptions queryOptions) {
        backingIndex.clear(queryOptions);
    }

}