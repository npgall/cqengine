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

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author niall.gallagher
 */
public class MarkableIteratorTest {

    @Test
    public void testMarkAndResetDuringRead() {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        MarkableIterator<Integer> markableIterator= new MarkableIterator<Integer>(input.iterator());
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);

        // Advance 5...
        Assert.assertEquals(5, Iterators.advance(markableIterator, 5));
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);

        // Mark this position...
        markableIterator.mark(Integer.MAX_VALUE);
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);

        // Advance 3...
        Assert.assertEquals(3, Iterators.advance(markableIterator, 3));
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);

        // Reset to position 5...
        markableIterator.reset();
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);
        Assert.assertEquals(Arrays.asList(5, 6, 7, 8, 9), Lists.newArrayList(markableIterator));
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);
    }

    @Test
    public void testMarkAndResetDuringReplay() {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        MarkableIterator<Integer> markableIterator= new MarkableIterator<Integer>(input.iterator());
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);

        // Advance 5...
        Assert.assertEquals(5, Iterators.advance(markableIterator, 5));
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);

        // Mark this position...
        markableIterator.mark(Integer.MAX_VALUE);
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);

        // Advance 3...
        Assert.assertEquals(3, Iterators.advance(markableIterator, 3));
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);

        // Reset to position 5...
        markableIterator.reset();
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);

        // Advance/replay 1 (should find integer 5)...
        Assert.assertEquals(Collections.singletonList(5), Lists.newArrayList(Iterators.limit(markableIterator, 1)));
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);

        // Mark this position 6...
        markableIterator.mark(Integer.MAX_VALUE);
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);

        // Advance 2 (should find integers 6 & 7)...
        Assert.assertEquals(Arrays.asList(6, 7), Lists.newArrayList(Iterators.limit(markableIterator, 2)));
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);

        // Advance 1 (should find integer 8)...
        Assert.assertEquals(Collections.singletonList(8), Lists.newArrayList(Iterators.limit(markableIterator, 1)));
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);

        // Reset to position 6...
        markableIterator.reset();
        // Replay the remainder of the buffer...
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);
        Assert.assertEquals(Arrays.asList(6, 7), Lists.newArrayList(Iterators.limit(markableIterator, 2)));
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);
        // Then read the next object from the backing iterator, and note that state changes...
        Assert.assertEquals(Collections.singletonList(8), Lists.newArrayList(Iterators.limit(markableIterator, 1)));
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);
        // Read the rest of the stream from backing iterator...
        Assert.assertEquals(Collections.singletonList(9), Lists.newArrayList(markableIterator));
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);
    }

    @Test
    public void testMarkAndResetDuringBuffer() {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        MarkableIterator<Integer> markableIterator= new MarkableIterator<Integer>(input.iterator());
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);

        // Advance 5...
        Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4), Lists.newArrayList(Iterators.limit(markableIterator, 5)));
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);

        // Mark this position...
        markableIterator.mark(1);
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);

        // Advance 1...
        Assert.assertEquals(Collections.singletonList(5), Lists.newArrayList(Iterators.limit(markableIterator, 1)));
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);

        // Mark this position...
        markableIterator.mark(2);
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);

        // Advance 1...
        Assert.assertEquals(Collections.singletonList(6), Lists.newArrayList(Iterators.limit(markableIterator, 1)));
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);

        // Reset to position 6...
        markableIterator.reset();
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);


        // Replay the remainder of the buffer...
        Assert.assertEquals(Collections.singletonList(6), Lists.newArrayList(Iterators.limit(markableIterator, 1)));
        Assert.assertEquals(MarkableIterator.State.REPLAY, markableIterator.state);
        Assert.assertEquals(Collections.singletonList(7), Lists.newArrayList(Iterators.limit(markableIterator, 1)));
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);
        Assert.assertEquals(Collections.singletonList(8), Lists.newArrayList(Iterators.limit(markableIterator, 1)));
        // Mark is invalidated...
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);
        Assert.assertEquals(Collections.singletonList(9), Lists.newArrayList(markableIterator));
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);
    }

    @Test(expected = IllegalStateException.class)
    public void testResetWithoutMark() {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        MarkableIterator<Integer> markableIterator= new MarkableIterator<Integer>(input.iterator());
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);

        markableIterator.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testMarkInvalidated() {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        MarkableIterator<Integer> markableIterator= new MarkableIterator<Integer>(input.iterator());
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);

        markableIterator.mark(1);
        Assert.assertEquals(MarkableIterator.State.BUFFER, markableIterator.state);
        Assert.assertEquals(Arrays.asList(0, 1), Lists.newArrayList(Iterators.limit(markableIterator, 2)));
        Assert.assertEquals(MarkableIterator.State.READ, markableIterator.state);
        markableIterator.reset();
    }
}