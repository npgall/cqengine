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
package com.googlecode.cqengine.persistence.composite;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author niall.gallagher
 */
public class CompositePersistenceTest {

    /*
    @Test
    public void testGetPrimaryKeyAttribute() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, noAdditionalPersistences());
        assertEquals(Car.CAR_ID, compositePersistence.getPrimaryKeyAttribute());
    }

    @Test
    public void testGetBytesUsed() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence1.getBytesUsed()).thenReturn(10L);
        when(persistence2.getBytesUsed()).thenReturn(5L);
        when(persistence3.getBytesUsed()).thenReturn(3L);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, singletonList(persistence3));
        assertEquals(18, compositePersistence.getBytesUsed());
    }

    @Test
    public void testCompact() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        Persistence<Car, Integer> persistence4 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, singletonList(persistence3));
        compositePersistence.compact();

        verify(persistence1, times(1)).compact();
        verify(persistence2, times(1)).compact();
        verify(persistence3, times(1)).compact();
        verify(persistence4, times(0)).compact();
    }

    @Test
    public void testExpand() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, singletonList(persistence3));
        compositePersistence.expand(5); // 5 bytes / 3 persistences = 1 byte per persistence, with 2 bytes remainder

        verify(persistence1, times(1)).expand(3); // primary persistence is expanded by the remainder bytes too
        verify(persistence2, times(1)).expand(1);
        verify(persistence3, times(1)).expand(1);
    }

    @Test
    public void testGetConnection() throws Exception {
        Index<Car> index1 = mockIndex("index1");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.supportsIndex(index1)).thenReturn(true);
        Connection connection = Mockito.mock(Connection.class, "connection");
        when(persistence2.getConnection(index1)).thenReturn(connection);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, noAdditionalPersistences());
        assertEquals(connection, compositePersistence.getConnection(index1));
    }

    @Test
    public void testSupportsIndex() throws Exception {
        Index<Car> index1 = mockIndex("index1");
        Index<Car> index2 = mockIndex("index2");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence1.supportsIndex(index1)).thenReturn(true);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, noAdditionalPersistences());
        assertTrue(compositePersistence.supportsIndex(index1));
        assertFalse(compositePersistence.supportsIndex(index2));
    }

    @Test
    public void testIsApplyUpdateForIndexEnabled() throws Exception {
        Index<Car> index1 = mockIndex("index1");
        Index<Car> index2 = mockIndex("index2");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);

        when(persistence1.supportsIndex(index1)).thenReturn(true);
        when(persistence2.supportsIndex(index2)).thenReturn(true);
        when(persistence1.isApplyUpdateForIndexEnabled(index1)).thenReturn(true);
        when(persistence2.isApplyUpdateForIndexEnabled(index2)).thenReturn(false);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, singletonList(persistence3));
        assertTrue(compositePersistence.isApplyUpdateForIndexEnabled(index1));
        assertFalse(compositePersistence.isApplyUpdateForIndexEnabled(index2));
    }

    @Test
    public void testCreate() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence3.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        Set<Car> set = new HashSet<Car>();
        when(persistence1.create()).thenReturn(set);

        CompositePersistence<Car, Integer> compositePersistence = new CompositePersistence<Car, Integer>(persistence1, persistence2, singletonList(persistence3));
        Set<Car> result = compositePersistence.create();
        assertEquals(set, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPersistenceForIndex_NoPersistence() throws Exception {
        Index<Car> index = mockIndex("index1");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");

        CompositePersistence.getPersistenceForIndex(index, persistence1, persistence2, singletonList(persistence3));
    }

    @Test
    public void testGetPersistenceForIndex_PrimaryPersistence() throws Exception {
        Index<Car> index = mockIndex("index1");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence1.supportsIndex(index)).thenReturn(true);

        Persistence<Car, Integer> result = CompositePersistence.getPersistenceForIndex(index, persistence1, persistence2, singletonList(persistence3));
        assertEquals(persistence1, result);
    }

    @Test
    public void testGetPersistenceForIndex_SecondaryPersistence() throws Exception {
        Index<Car> index = mockIndex("index1");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence2.supportsIndex(index)).thenReturn(true);

        Persistence<Car, Integer> result = CompositePersistence.getPersistenceForIndex(index, persistence1, persistence2, singletonList(persistence3));
        assertEquals(persistence2, result);
    }

    @Test
    public void testGetPersistenceForIndex_AdditionalPersistence() throws Exception {
        Index<Car> index = mockIndex("index1");
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        Persistence<Car, Integer> persistence3 = mockPersistence("persistence3");
        when(persistence3.supportsIndex(index)).thenReturn(true);

        Persistence<Car, Integer> result = CompositePersistence.getPersistenceForIndex(index, persistence1, persistence2, singletonList(persistence3));
        assertEquals(persistence3, result);
    }

    @Test
    public void testValidateBackingPersistences_Success() throws Exception {
        @SuppressWarnings("unchecked")
        Persistence<Car, Integer> persistence1 = mock(Persistence.class);
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);

        CompositePersistence.validatePersistenceArguments(persistence1, persistence1, singletonList(persistence1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateBackingPersistences_DifferentPrimaryKeys1() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.DOORS);

        CompositePersistence.validatePersistenceArguments(persistence1, persistence2, noAdditionalPersistences());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateBackingPersistences_DifferentPrimaryKeys2() throws Exception {
        Persistence<Car, Integer> persistence1 = mockPersistence("persistence1");
        Persistence<Car, Integer> persistence2 = mockPersistence("persistence2");
        when(persistence1.getPrimaryKeyAttribute()).thenReturn(Car.CAR_ID);
        when(persistence2.getPrimaryKeyAttribute()).thenReturn(Car.DOORS);

        CompositePersistence.validatePersistenceArguments(persistence1, persistence1, singletonList(persistence2));
    }

    @SuppressWarnings("unchecked")
    static Persistence<Car, Integer> mockPersistence(String name) {
        return mock(Persistence.class, name);
    }

    @SuppressWarnings("unchecked")
    static Index<Car> mockIndex(String name) {
        return mock(Index.class, name);
    }

    static List<Persistence<Car, Integer>> noAdditionalPersistences() {
        return Collections.emptyList();
    }
    */
}