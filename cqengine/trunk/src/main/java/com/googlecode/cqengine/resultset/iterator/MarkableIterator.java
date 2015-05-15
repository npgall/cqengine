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
package com.googlecode.cqengine.resultset.iterator;

import java.io.InputStream;
import java.util.*;

/**
 * An iterator which wraps a backing iterator, and which adds support for {@link #mark(int)} and {@link #reset()},
 * similar to an {@link InputStream}.
 * <p/>
 * When {@link #mark(int)} is called, the iterator will start buffering the objects it serves, up to the given limit.
 * When {@link #reset()} is called, the iterator will start to replay the objects it buffered since {@link #mark(int)}
 * was called. When the iterator has then finished replaying the buffered objects, it will continue to read from the
 * backing iterator and add them to the buffer, until {@link #mark(int)} is called again.
 * <p/>
 * To repeatedly replay objects from a certain point, call {@link #reset()} immediately followed by {@link #mark(int)}.
 * <br/>
 * To stop buffering objects, call mark with {@code readLimit} 0.
 *
 * @author niall.gallagher
 */
public class MarkableIterator<T> implements Iterator<T> {

    // A constant, iterator which returns no elements...
    final Iterator<T> emptyIterator = Collections.<T>emptyList().iterator();

    // The backing iterator supplied to the constructor, which provides objects we will buffer...
    final Iterator<T> backingIterator;

    // The possible states this MarkableIterator may be in...
    enum State { READ, BUFFER, REPLAY }

    // The current state...
    State state = State.READ;

    // List of objects which have been served since mark() was last called...
    List<T> replayBuffer = Collections.emptyList();

    // An iterator over objects which are now being replayed, since reset() was called...
    Iterator<T> replayIterator = emptyIterator;

    // The max number of objects to buffer...
    int readLimit = 0;

    public MarkableIterator(Iterator<T> backingIterator) {
        this.backingIterator = backingIterator;
    }

    @Override
    public boolean hasNext() {
        switch (state) {
            case READ: case BUFFER: {
                return backingIterator.hasNext();
            }
            case REPLAY: {
                return replayIterator.hasNext() || backingIterator.hasNext();
            }
            default: throw new IllegalStateException(String.valueOf(state));
        }
    }

    @Override
    public T next() {
        switch (state) {
            case READ: {
                return backingIterator.next();
            }
            case BUFFER: {
                if (replayBuffer.size() >= readLimit) {
                    // Invalidate the mark...
                    replayBuffer.clear();
                    replayIterator = emptyIterator;
                    state = State.READ;
                    return next();
                }
                T next = backingIterator.next();
                replayBuffer.add(next);
                return next;
            }
            case REPLAY: {
                if (replayIterator.hasNext()) {
                    return replayIterator.next();
                }
                replayIterator = emptyIterator;
                state = State.BUFFER;
                return next();
            }
            default: throw new IllegalStateException(String.valueOf(state));
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Starts buffering objects from the backing iterator so that they may be replayed when {@link #reset()} is called.
     *
     * @param readLimit The maximum limit of objects that can be read before the mark position becomes invalid.
     * @see InputStream#mark(int)
     */
    public void mark(int readLimit) {
        this.readLimit = readLimit;
        switch (state) {
            case READ: {
                replayBuffer = new ArrayList<T>(); // we don't supply initialCapacity, because readLimit can be large e.g. Integer.MAX_VALUE
                replayIterator = emptyIterator;
                state = State.BUFFER;
                return;
            }
            case BUFFER: {
                replayBuffer.clear();
                replayIterator = emptyIterator;
                return;
            }
            case REPLAY: {
                // Replace the buffer so that objects replayed already are removed, but objects not replayed are kept...
                replayBuffer = populateFromIterator(new ArrayList<T>(), replayIterator);
                replayIterator = replayBuffer.iterator();
                return;
            }
            default: throw new IllegalStateException(String.valueOf(state));
        }
    }

    /**
     * Repositions this iterator to the position at the time the mark method was last called.
     * <p/>
     * If the mark method has not been called since the iterator was created, or the number of objects read from the
     * iterator since mark was last called is larger than the argument to mark at that last call,
     * then an IllegalStateException will be thrown.
     * <p/>
     * Otherwise, the iterator is reset to a state such that all the objects read since the most recent call to mark
     * will be resupplied to subsequent callers of the next method, followed by any objects that otherwise would have
     * been the next input data as of the time of the call to reset.
     *
     * @throws IllegalStateException If the iterator has not been marked or the mark has been invalidated
     * @see InputStream#reset()
     */
    public void reset() {
        if (state == State.READ) {
            throw new IllegalStateException("Iterator has not been marked or the mark has been invalidated");
        }
        replayIterator = replayBuffer.iterator();
        state = State.REPLAY;
    }

    List<T> populateFromIterator(List<T> collection, Iterator<T> iterator) {
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
        return collection;
    }
}
