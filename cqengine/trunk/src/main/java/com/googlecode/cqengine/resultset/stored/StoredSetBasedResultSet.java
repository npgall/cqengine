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
package com.googlecode.cqengine.resultset.stored;

import java.util.Iterator;
import java.util.Set;

/**
 * A ResultSet which is stored directly in an index, and supports additional methods to add and remove objects.
 * <p/>
 * This implementation wraps a backing {@link java.util.Set}.
 *
 * @author Niall Gallagher
 */
public class StoredSetBasedResultSet<O> extends StoredResultSet<O> {

    private final Set<O> backingSet;
    private final int retrievalCost;

    /**
     * Constructor. Initialises the object with retrieval cost 0.
     *
     * @param backingSet A Set to which methods in this wrapper class will delegate
     */
    public StoredSetBasedResultSet(Set<O> backingSet) {
        this(backingSet, 0);
    }

    /**
     * Constructor.
     *
     * @param backingSet A Set to which methods in this wrapper class will delegate
     * @param retrievalCost The retrieval cost to subsequently return
     */
    public StoredSetBasedResultSet(Set<O> backingSet, int retrievalCost) {
        this.backingSet = backingSet;
        this.retrievalCost = retrievalCost;
    }

    @Override
    public boolean contains(O o) {
        return backingSet.contains(o);
    }

    @Override
    public Iterator<O> iterator() {
        return backingSet.iterator();
    }

    @Override
    public boolean add(O o) {
        return backingSet.add(o);
    }

    @Override
    public boolean remove(O o) {
        return backingSet.remove(o);
    }

    @Override
    public boolean isEmpty() {
        return backingSet.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        return !backingSet.isEmpty();
    }

    @Override
    public void clear() {
        backingSet.clear();
    }

    @Override
    public int size() {
        return backingSet.size();
    }

    @Override
    public int getRetrievalCost() {
        return retrievalCost;
    }

    /**
     * Returns the size of the backing set.
     * @return the size of the backing set
     */
    @Override
    public int getMergeCost() {
        return backingSet.size();
    }
}
