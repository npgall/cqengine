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
package com.googlecode.cqengine.resultset.order;

import com.googlecode.cqengine.resultset.ResultSet;

import java.util.*;

/**
 * Copies objects from a {@link ResultSet} into a {@link List}, using insertion sort to allow the resulting list
 * to be built and sorted in a single pass. Uses binary search to insert objects at the correct index.
 *
 * @author Niall Gallagher
 */
public class InsertionSortedListBuilder {

    /**
     * Private constructor, not used.
     */
    InsertionSortedListBuilder() {
    }

    /**
     * A subclass of {@link ArrayList} which overrides the {@link ArrayList#add(Object)} method, performing a
     * binary search of the list to determine the appropriate index to insert at, then calling the
     * {@link ArrayList#add(int, Object)} method to insert at the appropriate index.
     *
     * @param <O> The type of objects to be stored in the list.
     */
    static class InsertionSortedArrayList<O> extends ArrayList<O> {

        private final Comparator<O> comparator;

        InsertionSortedArrayList(int initialCapacity, Comparator<O> comparator) {
            super(initialCapacity);
            this.comparator = comparator;
        }

        @Override
        public boolean add(O o) {
            int index = Collections.binarySearch(this, o, comparator);
            if (index < 0) index = ~index;
            super.add(index, o);
            return true;
        }
    }

    /**
     * Copies objects from a {@link ResultSet} into a {@link List}, using insertion sort to allow the resulting list
     * to be built and sorted in a single pass. Uses binary search to insert objects at the correct index.
     * <p/>
     * The list returned is <b>unmodifiable</b>.
     *
     * @param resultSet A ResultSet which provides objects to be copied into the list
     * @param comparator The comparator with which to sort the list
     * @param <O> The type of objects we are dealing with
     * @return A sorted, unmodifiable list
     */
    public static <O> List<O> build(ResultSet<O> resultSet, Comparator<O> comparator) {
        // Here we get the size of the ResultSet to make a best-effort to size the ArrayList correctly from the outset.
        // Note that *there is a race condition* - between the time we read the size from the ResultSet,
        // and the time we finish adding elements to the list,
        // the number of elements provided by the ResultSet might change.
        // Therefore our use of size is an optimization, but ultimately the ArrayList might have to be re-sized anyway.
        InsertionSortedArrayList<O> list = new InsertionSortedArrayList<O>(resultSet.size(), comparator);

        // Add elements to this list...
        for (O object : resultSet) {
            list.add(object);
        }
        // Return the sorted list (unmodifiable)...
        return Collections.unmodifiableList(list);
    }


}
