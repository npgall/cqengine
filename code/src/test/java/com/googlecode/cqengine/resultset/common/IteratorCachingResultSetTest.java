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
package com.googlecode.cqengine.resultset.common;

import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.Iterator;

import static com.googlecode.cqengine.testutil.CarFactory.createCar;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link IteratorCachingResultSet}.
 *
 * @author niall.gallagher
 */
public class IteratorCachingResultSetTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testIteratorCaching() {
        ResultSet<Car> backingResultSet = mock(ResultSet.class);
        when(backingResultSet.iterator()).thenReturn(mock(Iterator.class), mock(Iterator.class), mock(Iterator.class), mock(Iterator.class), mock(Iterator.class), mock(Iterator.class), mock(Iterator.class));
        IteratorCachingResultSet<Car> iteratorCachingResultSet = new IteratorCachingResultSet<Car>(backingResultSet);

        Iterator<Car> i1 = iteratorCachingResultSet.iterator();
        Iterator<Car> i2 = iteratorCachingResultSet.iterator();
        assertSame(i1, i2);

        i2.hasNext();
        i2.hasNext();
        Iterator<Car> i3 = iteratorCachingResultSet.iterator();
        assertSame(i1, i3);

        i3.next();
        Iterator<Car> i4 = iteratorCachingResultSet.iterator();
        assertNotSame(i3, i4);

        i4.remove();
        Iterator<Car> i5 = iteratorCachingResultSet.iterator();
        assertNotSame(i4, i5);

        i5.hasNext();
        i5.hasNext();
        Iterator<Car> i6 = iteratorCachingResultSet.iterator();
        assertSame(i5, i6);

        iteratorCachingResultSet.isEmpty();
        iteratorCachingResultSet.isNotEmpty();
        Iterator<Car> i7 = iteratorCachingResultSet.iterator();
        assertSame(i6, i7);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDelegatingMethods() {
        ResultSet<Car> backingResultSet = mock(ResultSet.class);
        IteratorCachingResultSet<Car> iteratorCachingResultSet = new IteratorCachingResultSet<Car>(backingResultSet);

        iteratorCachingResultSet.contains(createCar(1));
        verify(backingResultSet, times(1)).contains(createCar(1));

        iteratorCachingResultSet.matches(createCar(2));
        verify(backingResultSet, times(1)).matches(createCar(2));

        iteratorCachingResultSet.getQuery();
        verify(backingResultSet, times(1)).getQuery();

        iteratorCachingResultSet.getQueryOptions();
        verify(backingResultSet, times(1)).getQueryOptions();

        iteratorCachingResultSet.getRetrievalCost();
        verify(backingResultSet, times(1)).getRetrievalCost();

        iteratorCachingResultSet.getMergeCost();
        verify(backingResultSet, times(1)).getMergeCost();

        iteratorCachingResultSet.size();
        verify(backingResultSet, times(1)).size();

        iteratorCachingResultSet.close();
        verify(backingResultSet, times(1)).close();
    }
}