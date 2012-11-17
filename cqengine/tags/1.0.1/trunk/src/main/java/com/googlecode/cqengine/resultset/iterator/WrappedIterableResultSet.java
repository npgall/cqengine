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
package com.googlecode.cqengine.resultset.iterator;

import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.common.SizeProvider;
import com.googlecode.cqengine.resultset.common.SizeProviders;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Adapts (wraps) a regular {@link Iterable} to the {@link ResultSet} interface.
 *
 * @author Niall Gallagher
 */
public class WrappedIterableResultSet<O> extends ResultSet<O> {

    private final Iterable<O> wrappedIterable;
    private final SizeProvider sizeProvider;
    private final int retrievalCost;
    private final boolean collection;

    public WrappedIterableResultSet(int retrievalCost, Iterable<O> wrappedIterable, SizeProvider sizeProvider) {
        if (wrappedIterable instanceof ResultSet) {
            throw new IllegalArgumentException("The iterable supplied is already a ResultSet");
        }
        boolean collection = (wrappedIterable instanceof Collection);
        this.wrappedIterable = wrappedIterable;
        this.collection = collection;
        this.retrievalCost = retrievalCost;
        this.sizeProvider = sizeProvider;
    }

    @Override
    public Iterator<O> iterator() {
        return wrappedIterable.iterator();
    }

    @Override
    public boolean contains(O object) {
        if (collection) {
            return ((Collection<O>) wrappedIterable).contains(object);
        }
        for (O contained : wrappedIterable) {
            if (contained.equals(object)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return sizeProvider.size();
    }

    @Override
    public int getRetrievalCost() {
        return retrievalCost;
    }

    @Override
    public int getMergeCost() {
        return sizeProvider.size();
    }
}
