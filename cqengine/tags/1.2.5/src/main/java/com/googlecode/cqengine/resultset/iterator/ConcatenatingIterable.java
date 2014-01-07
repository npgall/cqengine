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

import java.util.Iterator;

/**
 * @author Niall Gallagher
 */
public class ConcatenatingIterable<O> implements Iterable<O> {

    private final Iterable<? extends Iterable<O>> iterables;

    public ConcatenatingIterable(Iterable<? extends Iterable<O>> iterables) {
        this.iterables = iterables;
    }

    @Override
    public Iterator<O> iterator() {
        final Iterator<? extends Iterable<O>> iterator = iterables.iterator();
        return new ConcatenatingIterator<O>() {
            @Override
            public Iterator<O> getNextIterator() {
                return iterator.hasNext() ? iterator.next().iterator() : null;
            }
        };
    }
}
