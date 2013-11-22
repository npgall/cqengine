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
package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An Iterator which wraps another iterator, and for each object returned by the wrapped iterator, calls an
 * {@link #isValid(Object)} method. If this method returns true, this iterator returns the object, if it returns false
 * it skips the object and moves to the next object.
 *
 * @author ngallagher
 * @since 2012-05-03 17:38
 */
public abstract class FilteringIterator<O> extends UnmodifiableIterator<O> {

    private final Iterator<O> wrappedIterator;

    public FilteringIterator(Iterator<O> wrappedIterator) {
        this.wrappedIterator = wrappedIterator;
    }

    private O nextObject = null;
    private boolean nextObjectIsNull = false;

    @Override
    public boolean hasNext() {
        if(nextObjectIsNull || nextObject != null) {
            return true;
        }
        while (wrappedIterator.hasNext()) {
            nextObject = wrappedIterator.next();
            if (isValid(nextObject)) {
                nextObjectIsNull = (nextObject == null);
                return true;
            } // else object not valid, skip to next object
            nextObjectIsNull = false;
        }
        return false;
    }

    @Override
    public O next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        O objectToReturn = nextObject;
        nextObject = null;
        nextObjectIsNull = false;
        return objectToReturn;
    }

    public abstract boolean isValid(O object);

}
