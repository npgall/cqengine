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

import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.disk.PartialDiskIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.navigable.PartialNavigableIndex;
import com.googlecode.cqengine.index.offheap.PartialOffHeapIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.testutil.Car;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.mockito.Mockito;

import static com.googlecode.cqengine.index.support.PartialIndex.supportsQueryInternal;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link PartialIndex}.
 *
 * @author niall.gallagher
 */
public class PartialIndexTest {

    @Test
    public void testEqualsAndHashCode_PartialNavigableIndex() {
        EqualsVerifier.forClass(PartialNavigableIndex.class)
                .withNonnullFields("filterQuery", "attribute", "backingIndex")
                .withIgnoredFields("attribute", "indexMapFactory", "valueSetFactory")
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void testEqualsAndHashCode_PartialDiskIndex() {
        EqualsVerifier.forClass(PartialDiskIndex.class)
                .withNonnullFields("filterQuery", "attribute", "backingIndex")
                .withIgnoredFields("attribute", "tableNameSuffix")
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void testEqualsAndHashCode_PartialOffHeapIndex() {
        EqualsVerifier.forClass(PartialOffHeapIndex.class)
                .withNonnullFields("filterQuery", "attribute", "backingIndex")
                .withIgnoredFields("attribute", "tableNameSuffix")
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void testGetterMethods() {
        PartialIndex partialIndex = PartialNavigableIndex.onAttributeWithFilterQuery(Car.MANUFACTURER, between(Car.CAR_ID, 2, 5));
        assertEquals(Car.MANUFACTURER, partialIndex.getAttribute());
        assertEquals(between(Car.CAR_ID, 2, 5), partialIndex.getFilterQuery());
        assertFalse(partialIndex.isQuantized());
        assertTrue(partialIndex.getBackingIndex() instanceof NavigableIndex);
        assertTrue(partialIndex.getEffectiveIndex() == partialIndex);
        assertTrue(partialIndex.getBackingIndex().getEffectiveIndex() == partialIndex);
    }

    @Test
    public void testClear() {
        AttributeIndex<Integer, Car> backingIndex = mockBackingIndex();
        PartialIndex<Integer, Car, AttributeIndex<Integer, Car>> index = wrapWithPartialIndex(backingIndex);

        index.clear(noQueryOptions());
        verify(backingIndex, times(1)).clear(noQueryOptions());
    }

    @Test
    public <O> void testSupportsQuery_Positive_RootQueryEqualsFilterQuery() {
        // A partial index on attribute Car.MANUFACTURER, which is filtered to only index Ford and Honda cars.
        assertTrue(supportsQueryInternal(
                backingIndexSupportsQuery(in(Car.MANUFACTURER, "Ford", "Honda")),
                in(Car.MANUFACTURER, "Ford", "Honda"),
                in(Car.MANUFACTURER, "Ford", "Honda"),
                in(Car.MANUFACTURER, "Ford", "Honda"),
                noQueryOptions()
        ));
    }

    @Test
    public <O> void testSupportsQuery_Positive_RootQueryIsAnAndQueryWithFilterQueryAsChild() {
        // A partial index on attribute Car.PRICE, which is filtered to only index Ford and Honda cars.
        assertTrue(supportsQueryInternal(
                backingIndexSupportsQuery(lessThan(Car.PRICE, 5000.0)),
                in(Car.MANUFACTURER, "Ford", "Honda"),
                and(lessThan(Car.PRICE, 5000.0), in(Car.MANUFACTURER, "Ford", "Honda")),
                lessThan(Car.PRICE, 5000.0),
                noQueryOptions()
        ));
    }

    @Test
    public <O> void testSupportsQuery_Positive_RootAndFilterQueriesAreBothAndQueriesAndChildrenOfFilterQueryAreChildrenOfRootQuery() {
        // A partial index on attribute Car.PRICE, which is filtered to only index "Blue" Ford and Honda cars.
        assertTrue(supportsQueryInternal(
                backingIndexSupportsQuery(lessThan(Car.PRICE, 5000.0)),
                and(in(Car.MANUFACTURER, "Ford", "Honda"), equal(Car.COLOR, Car.Color.BLUE)),
                and(lessThan(Car.PRICE, 5000.0), in(Car.MANUFACTURER, "Ford", "Honda"), equal(Car.COLOR, Car.Color.BLUE)),
                lessThan(Car.PRICE, 5000.0),
                noQueryOptions()
        ));
    }

    @Test
    public <O> void testSupportsQuery_Negative_BackingIndexDoesNotSupportQuery() {
        assertFalse(supportsQueryInternal(
                backingIndexSupportsQuery(in(Car.MODEL, "Focus", "Civic")),
                in(Car.MANUFACTURER, "Ford", "Honda"),
                in(Car.MANUFACTURER, "Ford", "Honda"),
                in(Car.MANUFACTURER, "Ford", "Honda"),
                noQueryOptions()
        ));
    }

    @Test
    public <O> void testSupportsQuery_Negative_RootQueryDoesNotEqualFilterQueryAndRootQueryIsNotAnAndQuery() {
        assertFalse(supportsQueryInternal(
                backingIndexSupportsQuery(in(Car.MANUFACTURER, "Ford", "Honda")),
                or(in(Car.MANUFACTURER, "Ford", "Honda"), in(Car.MODEL, "Focus", "Civic")),
                in(Car.MANUFACTURER, "Ford", "Honda"),
                in(Car.MANUFACTURER, "Ford", "Honda"),
                noQueryOptions()
        ));
    }

    @Test
    public <O> void testSupportsQuery_Negative_RootQueryDoesNotEqualFilterQueryAndFilterQueryIsNotAnAndQuery() {
        assertFalse(supportsQueryInternal(
                backingIndexSupportsQuery(lessThan(Car.PRICE, 5000.0)),
                or(in(Car.MANUFACTURER, "Ford", "Honda"), equal(Car.COLOR, Car.Color.BLUE)),
                and(lessThan(Car.PRICE, 5000.0), in(Car.MANUFACTURER, "Ford", "Honda"), equal(Car.COLOR, Car.Color.BLUE)),
                lessThan(Car.PRICE, 5000.0),
                noQueryOptions()
        ));
    }

    @Test
    public <O> void testSupportsQuery_Negative_RootAndQueryDoesNotContainChildrenOfAndFilterQuery() {
        assertFalse(supportsQueryInternal(
                backingIndexSupportsQuery(lessThan(Car.PRICE, 5000.0)),
                and(in(Car.MANUFACTURER, "Ford", "Honda"), equal(Car.COLOR, Car.Color.BLUE)),
                and(lessThan(Car.PRICE, 5000.0), in(Car.MANUFACTURER, "Ford", "Honda"), equal(Car.COLOR, Car.Color.RED)),
                lessThan(Car.PRICE, 5000.0),
                noQueryOptions()
        ));
    }

    static PartialIndex<Integer, Car, AttributeIndex<Integer, Car>> wrapWithPartialIndex(final AttributeIndex<Integer, Car> mockedBackingIndex) {
        return new PartialIndex<Integer, Car, AttributeIndex<Integer, Car>>(Car.CAR_ID, in(Car.MANUFACTURER, "Ford", "Honda")) {
            @Override
            protected AttributeIndex<Integer, Car> createBackingIndex() {
                return mockedBackingIndex;
            }
        };
    }

    @SuppressWarnings("unchecked")
    static SortedKeyStatisticsAttributeIndex<Integer, Car> mockBackingIndex() {
        return mock(SortedKeyStatisticsAttributeIndex.class);
    }

    @SuppressWarnings("unchecked")
    static SortedKeyStatisticsAttributeIndex<Integer, Car> backingIndexSupportsQuery(Query<Car> querySupportedByBackingIndex) {
        SortedKeyStatisticsAttributeIndex attributeIndex = mock(SortedKeyStatisticsAttributeIndex.class);
        when(attributeIndex.supportsQuery(Mockito.eq(querySupportedByBackingIndex), Mockito.<QueryOptions>any())).thenReturn(true);
        return attributeIndex;
    }
}