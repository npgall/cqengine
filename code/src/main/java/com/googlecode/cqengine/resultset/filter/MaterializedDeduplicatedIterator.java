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
package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An iterator which wraps another and prevents duplicate objects from being returned.
 * <p/>
 * The implementation starts returning objects from the wrapped iterator immediately, and records during iteration the
 * objects issued so far in a temporary collection. If the wrapped iterator returns the same object more than once,
 * this iterator will transparently skip it and move to the next object.
 *
 * @author niall.gallagher
 */
public class MaterializedDeduplicatedIterator<O> extends UnmodifiableIterator<O> {

    final Iterator<O> backingIterator;

    final Set<O> materializedSet = new HashSet<O>();

    O nextObject = null;

    public MaterializedDeduplicatedIterator(Iterator<O> backingIterator) {
        this.backingIterator = backingIterator;
    }

    @Override
    public boolean hasNext() {
        if (nextObject != null) {
            return true;
        }
        while(backingIterator.hasNext()) {
            O next = backingIterator.next();
            if (next == null) {
                throw new IllegalStateException("Unexpectedly received null from the backing iterator's iterator.next()");
            }
            if (!materializedSet.add(next)) {
                continue;
            }
            nextObject = next;
            return true;
        }
        nextObject = null;
        materializedSet.clear(); // ..help GC
        return false;
    }

    @Override
    public O next() {
        O next = nextObject;
        if (next == null) {
            throw new IllegalStateException("Detected an attempt to call iterator.next() without calling iterator.hasNext() immediately beforehand");
        }
        nextObject = null;
        return next;
    }
}
