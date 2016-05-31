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

import com.googlecode.cqengine.index.disk.PartialDiskIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.navigable.PartialNavigableIndex;
import com.googlecode.cqengine.index.offheap.PartialOffHeapIndex;
import com.googlecode.cqengine.testutil.Car;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.between;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author niall.gallagher
 */
public class PartialIndexTest {

    @Test
    public void testEqualsAndHashCode_PartialNavigableIndex() {
        EqualsVerifier.forClass(PartialNavigableIndex.class)
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void testEqualsAndHashCode_PartialDiskIndex() {
        EqualsVerifier.forClass(PartialDiskIndex.class)
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void testEqualsAndHashCode_PartialOffHeapIndex() {
        EqualsVerifier.forClass(PartialOffHeapIndex.class)
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void testGetterMethods() {
        PartialIndex partialIndex = PartialNavigableIndex.onAttributeWithFilterQuery(Car.MANUFACTURER, between(Car.CAR_ID, 2, 5));
        assertEquals(Car.MANUFACTURER, partialIndex.getAttribute());
        assertEquals(between(Car.CAR_ID, 2, 5), partialIndex.getFilterQuery());
        assertTrue(partialIndex.getBackingIndex() instanceof NavigableIndex);
        assertTrue(partialIndex.getEffectiveIndex() == partialIndex);
        assertTrue(partialIndex.getBackingIndex().getEffectiveIndex() == partialIndex);
    }
}