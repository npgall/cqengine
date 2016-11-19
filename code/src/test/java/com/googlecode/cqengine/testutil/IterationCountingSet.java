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
package com.googlecode.cqengine.testutil;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A {@link Set} which wraps another set, and which counts the number of objects returned by the set.iterator() method.
 *
 * @author niall.gallagher
 */
public class IterationCountingSet<T> extends AbstractSet<T> {

    final Set<T> backingSet;

    int itemsIteratedCount;

    public IterationCountingSet(Set<T> backingSet) {
        this.backingSet = backingSet;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<T> backingIterator = backingSet.iterator();
            @Override
            public boolean hasNext() {
                return backingIterator.hasNext();
            }

            @Override
            public T next() {
                T next = backingIterator.next();
                itemsIteratedCount++;
                return next;
            }

            @Override
            public void remove() {
                backingIterator.remove();
            }
        };
    }

    @Override
    public int size() {
        return backingSet.size();
    }

    public int getItemsIteratedCount() {
        return itemsIteratedCount;
    }
}