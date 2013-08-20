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
import java.util.NoSuchElementException;

/**
 * @author Niall Gallagher
 */
public abstract class ConcatenatingIterator<O> extends UnmodifiableIterator<O> {

    private Iterator<O> currentIterator = null;


    @Override
    public boolean hasNext() {
        while (currentIterator == null || !currentIterator.hasNext()) {
            currentIterator = getNextIterator();
            if (currentIterator == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public O next() {
        Iterator<O> currentIterator = this.currentIterator;
        if (currentIterator == null) {
            throw new NoSuchElementException("No more elements");
        }
        return currentIterator.next();
    }

    public abstract Iterator<O> getNextIterator();
}
