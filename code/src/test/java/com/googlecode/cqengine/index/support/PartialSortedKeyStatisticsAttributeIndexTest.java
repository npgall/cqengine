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
package com.googlecode.cqengine.index.support;

import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link PartialSortedKeyStatisticsAttributeIndex}.
 *
 * @author niall.gallagher
 */
public class PartialSortedKeyStatisticsAttributeIndexTest {

    @Test
    public void testGetDistinctKeys1() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getDistinctKeys(noQueryOptions());
        verify(backingIndex, times(1)).getDistinctKeys(noQueryOptions());
    }

    @Test
    public void testGetDistinctKeys2() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getDistinctKeys(1, true, 2, true, noQueryOptions());
        verify(backingIndex, times(1)).getDistinctKeys(1, true, 2, true, noQueryOptions());
    }

    @Test
    public void testGetDistinctKeysDescending1() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getDistinctKeysDescending(noQueryOptions());
        verify(backingIndex, times(1)).getDistinctKeysDescending(noQueryOptions());
    }

    @Test
    public void testGetDistinctKeysDescending2() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getDistinctKeysDescending(1, true, 2, true, noQueryOptions());
        verify(backingIndex, times(1)).getDistinctKeysDescending(1, true, 2, true, noQueryOptions());
    }

    @Test
    public void testGetStatisticsForDistinctKeysDescending() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getStatisticsForDistinctKeysDescending(noQueryOptions());
        verify(backingIndex, times(1)).getStatisticsForDistinctKeysDescending(noQueryOptions());
    }

    @Test
    public void testGetKeysAndValues1() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getKeysAndValues(noQueryOptions());
        verify(backingIndex, times(1)).getKeysAndValues(noQueryOptions());
    }

    @Test
    public void testGetKeysAndValues2() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getKeysAndValues(1, true, 2, true, noQueryOptions());
        verify(backingIndex, times(1)).getKeysAndValues(1, true, 2, true, noQueryOptions());
    }

    @Test
    public void testGetKeysAndValuesDescending() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getKeysAndValuesDescending(noQueryOptions());
        verify(backingIndex, times(1)).getKeysAndValuesDescending(noQueryOptions());
    }

    @Test
    public void testGetKeysAndValuesDescending1() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getKeysAndValuesDescending(1, true, 2, true, noQueryOptions());
        verify(backingIndex, times(1)).getKeysAndValuesDescending(1, true, 2, true, noQueryOptions());
    }

    @Test
    public void testGetCountForKey() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getCountForKey(1, noQueryOptions());
        verify(backingIndex, times(1)).getCountForKey(1, noQueryOptions());
    }

    @Test
    public void testGetCountOfDistinctKeys() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getCountOfDistinctKeys(noQueryOptions());
        verify(backingIndex, times(1)).getCountOfDistinctKeys(noQueryOptions());
    }

    @Test
    public void testGetStatisticsForDistinctKeys() {
        SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialSortedKeyStatisticsAttributeIndex<Integer, Car> index = wrapWithPartialIndex(backingIndex);

        index.getStatisticsForDistinctKeys(noQueryOptions());
        verify(backingIndex, times(1)).getStatisticsForDistinctKeys(noQueryOptions());
    }

    static PartialSortedKeyStatisticsAttributeIndex<Integer, Car> wrapWithPartialIndex(final SortedKeyStatisticsAttributeIndex<Integer, Car> mockedBackingIndex) {
        return new PartialSortedKeyStatisticsAttributeIndex<Integer, Car>(Car.CAR_ID, QueryFactory.between(Car.CAR_ID, 2, 5)) {
            @Override
            protected SortedKeyStatisticsAttributeIndex<Integer, Car> createBackingIndex() {
                return mockedBackingIndex;
            }
        };
    }

    @SuppressWarnings("unchecked")
    static SortedKeyStatisticsAttributeIndex<Integer, Car> mockBackingIndex() {
        return mock(SortedKeyStatisticsAttributeIndex.class);
    }
}