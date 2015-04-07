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
package com.googlecode.cqengine.persistence.support.sqlite;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.index.sqlite.SQLiteIdentityIndex;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.PersistentSet;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.io.IOException;
import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author Niall Gallagher
 */
public class SQLitePersistentSet<O, A extends Comparable<A>> extends AbstractSet<O> implements PersistentSet<O, A> {

    final Persistence<O, A> persistence;
    final SQLiteIdentityIndex<A, O> backingIndex;
    final SimpleAttribute<O, A> primaryKeyAttribute;
    final Class<O> objectType;

    public SQLitePersistentSet(final Persistence<O, A> persistence) {
        this.persistence = persistence;
        this.objectType = persistence.getPrimaryKeyAttribute().getObjectType();
        this.primaryKeyAttribute = persistence.getPrimaryKeyAttribute();
        this.backingIndex = SQLiteIdentityIndex.onAttribute(persistence.getPrimaryKeyAttribute(), persistence);
        this.backingIndex.init(Collections.<O>emptySet(), noQueryOptions());
    }

    @Override
    public Persistence<O, A> getPersistence() {
        return persistence;
    }

    public SQLiteIdentityIndex<A, O> getBackingIndex() {
        return backingIndex;
    }

    @Override
    public int size() {
        return backingIndex.retrieve(all(objectType), noQueryOptions()).size();
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        O object = (O) o;
        A objectId = primaryKeyAttribute.getValue(object, noQueryOptions());
        return backingIndex.retrieve(equal(primaryKeyAttribute, objectId), noQueryOptions()).size() > 0;
    }

    @Override
    public CloseableIterator<O> iterator() {
        final ResultSet<O> rs = backingIndex.retrieve(all(objectType), noQueryOptions());
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
            public void close() throws IOException {
                rs.close();
            }
        }
        return new CloseableIteratorImpl();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean add(O object) {
        return backingIndex.addAll(Collections.singleton(object), noQueryOptions());
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        O object = (O) o;
        return backingIndex.removeAll(Collections.singleton(object), noQueryOptions());
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends O> c) {
        @SuppressWarnings("unchecked")
        Collection<O> objects = (Collection<O>) c;
        return backingIndex.addAll(objects, noQueryOptions());
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Note: this could be optimized...
        Collection<O> objectsToRemove = new ArrayList<O>();
        ResultSet<O> allObjects = backingIndex.retrieve(all(objectType), noQueryOptions());
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
        return backingIndex.removeAll(objectsToRemove, noQueryOptions());
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        @SuppressWarnings("unchecked")
        Collection<O> objects = (Collection<O>) c;
        return backingIndex.removeAll(objects, noQueryOptions());
    }

    @Override
    public void clear() {
        backingIndex.clear(noQueryOptions());
    }
}
