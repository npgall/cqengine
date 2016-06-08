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
package com.googlecode.cqengine.persistence.support;

import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.googlecode.cqengine.query.QueryFactory.between;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link FilteredObjectStore}.
 *
 * @author niall.gallagher
 */
public class FilteredObjectStoreTest {

    final FilteredObjectStore<Car> filteredObjectStore = new FilteredObjectStore<Car>(
            nonEmptyObjectStore(CarFactory.createCollectionOfCars(10)),
            between(Car.CAR_ID, 2, 4)
    );

    final Car dummyCar = CarFactory.createCar(0);


    @Test
    public void testIterator_Filtering() {
        Set<Integer> results = new HashSet<Integer>();
        CloseableIterator<Car> iterator = filteredObjectStore.iterator(noQueryOptions());
        while (iterator.hasNext()) {
            results.add(iterator.next().getCarId());
        }
        assertEquals(new HashSet<Integer>(asList(2,3,4)), results);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIterator_Close() {
        CloseableIterator<Car> mockIterator = mock(CloseableIterator.class);
        ObjectStore<Car> mockObjectStore = mock(ObjectStore.class);
        Mockito.when(mockObjectStore.iterator(Mockito.<QueryOptions>any())).thenReturn(mockIterator);

        FilteredObjectStore<Car> filteredObjectStore = new FilteredObjectStore<Car>(
                mockObjectStore,
                between(Car.CAR_ID, 2, 4)
        );

        filteredObjectStore.iterator(noQueryOptions()).close();
        verify(mockIterator, times(1)).close();

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        filteredObjectStore.addAll(Collections.<Car>emptySet(), noQueryOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        filteredObjectStore.clear(noQueryOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        filteredObjectStore.contains(dummyCar, noQueryOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        filteredObjectStore.remove(dummyCar, noQueryOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        filteredObjectStore.add(dummyCar, noQueryOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSize() {
        filteredObjectStore.size(noQueryOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        filteredObjectStore.containsAll(Collections.singleton(dummyCar), noQueryOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsEmpty() {
        filteredObjectStore.isEmpty(noQueryOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        filteredObjectStore.retainAll(Collections.singleton(dummyCar), noQueryOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        filteredObjectStore.removeAll(Collections.singleton(dummyCar), noQueryOptions());
    }

    static ConcurrentOnHeapObjectStore<Car> nonEmptyObjectStore(Collection<Car> contents) {
        ConcurrentOnHeapObjectStore<Car> objectStore = new ConcurrentOnHeapObjectStore<Car>();
        objectStore.addAll(contents, noQueryOptions());
        return objectStore;
    }
}