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

import com.googlecode.concurrenttrees.common.LazyIterator;
import com.googlecode.cqengine.index.support.KeyValue;
import com.googlecode.cqengine.index.support.KeyValueMaterialized;
import com.googlecode.cqengine.resultset.filter.MaterializedDeduplicatedIterator;

import java.util.*;

/**
 * @author Niall Gallagher
 */
public class IteratorUtil {

    public static <O> boolean iterableContains(Iterable<O> iterable, O element) {
        for (O contained : iterable) {
            if (contained.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public static int countElements(Iterable<?> iterable) {
        int count = 0;
        // UnusedDeclaration warning is due to iterator.next() being invoked but not used.
        // Actually we intentionally invoke iterator.next() even though we don't use it,
        // in case iterator implementation requires this per typical usage...
        //noinspection UnusedDeclaration
        for (Object object : iterable) {
            count++;
        }
        return count;
    }

    /**
     * Returns the elements of {@code unfiltered} that are not null.
     */
    public static <T> Iterator<T> removeNulls(final Iterator<T> unfiltered) {
        return new LazyIterator<T>() {
            @Override protected T computeNext() {
                while (unfiltered.hasNext()) {
                    T element = unfiltered.next();
                    if (element != null) {
                        return element;
                    }
                }
                return endOfData();
            }
        };
    }

    /**
     * Wraps the given Iterator as an {@link UnmodifiableIterator}.
     */
    public static <T> UnmodifiableIterator<T> wrapAsUnmodifiable(final Iterator<T> iterator) {
        return new UnmodifiableIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }

    /**
     * Transforms a {@code Map&lt;A, Iterable&lt;O&gt;&gt;} into a stream of {@code KeyValue&lt;A, O&gt;} objects.
     *
     * @param map The map to be transformed
     * @param <A> Type of the key in the map
     * @param <O> Type of the objects returned by the Iterables in the map
     * @return A flattened stream of {@code KeyValue&lt;A, O&gt;} objects
     */
    public static <A, O> Iterable<KeyValue<A, O>> flatten(final Map<A, ? extends Iterable<O>> map) {
        return new Iterable<KeyValue<A, O>>() {
            @Override
            public Iterator<KeyValue<A, O>> iterator() {
                return new LazyIterator<KeyValue<A, O>>() {
                    Iterator<? extends Map.Entry<A, ? extends Iterable<O>>> entriesIterator = map.entrySet().iterator();
                    Iterator<KeyValue<A, O>> valuesIterator = Collections.<KeyValue<A, O>>emptySet().iterator();
                    @Override
                    protected KeyValue<A, O> computeNext() {
                        while (true) {
                            if (valuesIterator.hasNext()) {
                                return valuesIterator.next();
                            }
                            if (!entriesIterator.hasNext()) {
                                return endOfData();
                            }
                            Map.Entry<A, ? extends Iterable<O>> entry = entriesIterator.next();
                            valuesIterator = flatten(entry.getKey(), entry.getValue()).iterator();
                        }
                    }
                };
            }
        };
    }

    /**
     * Transforms a key of type {@code A} and an {@code Iterable&lt;O&gt;} into a stream of {@code KeyValue&lt;A, O&gt;}
     * objects.
     *
     * @param key The key to be transformed
     * @param values The values to be transformed
     * @param <A> Type of the key
     * @param <O> Type of the objects returned by the Iterable
     * @return A flattened stream of {@code KeyValue&lt;A, O&gt;} objects
     */
    public static <A, O> Iterable<KeyValue<A, O>> flatten(final A key, final Iterable<O> values) {
        return new Iterable<KeyValue<A, O>>() {
            @Override
            public Iterator<KeyValue<A, O>> iterator() {
                return new LazyIterator<KeyValue<A, O>>() {
                    final Iterator<O> valuesIterator = values.iterator();
                    @Override
                    protected KeyValue<A, O> computeNext() {
                        return valuesIterator.hasNext() ? new KeyValueMaterialized<A, O>(key, valuesIterator.next()) : endOfData();
                    }
                };
            }
        };
    }

    public static <O> Iterator<O> concatenate(final Iterator<? extends Iterable<O>> iterables) {
        return new ConcatenatingIterator<O>() {
            @Override
            public Iterator<O> getNextIterator() {
                return iterables.hasNext() ? iterables.next().iterator() : null;
            }
        };
    }

    public static <O> Iterator<Set<O>> groupAndSort(final Iterator<? extends KeyValue<?, O>> values, final Comparator<O> comparator) {
        return new LazyIterator<Set<O>>() {
            final Iterator<? extends KeyValue<?, O>> valuesIterator = values;
            Set<O> currentGroup = new TreeSet<O>(comparator);
            Object currentKey = null;

            @Override
            protected Set<O> computeNext() {

                while (valuesIterator.hasNext()) {
                    KeyValue<?, O> next = valuesIterator.next();
                    if (!next.getKey().equals(currentKey)) {
                        Set<O> result = currentGroup;
                        currentKey = next.getKey();
                        currentGroup = new TreeSet<O>(comparator);
                        currentGroup.add(next.getValue());
                        return result;
                    }
                    currentGroup.add(next.getValue());
                }
                if (currentGroup.isEmpty()) {
                    return endOfData();
                }
                else {
                    Set<O> result = currentGroup;
                    currentGroup = new TreeSet<O>(comparator);
                    return result;
                }
            }
        };
    }

    /**
     * Sorts the results returned by the given iterator, returning the sorted results as a new iterator, by performing
     * a merge-sort into an intermediate array in memory.
     * <p/>
     * The time complexity for copying the objects into the intermediate array is O(n), and then the cost of sorting is
     * additionally O(n log(n)). So overall complexity is O(n) + O(n log(n)).
     * <p>
     * Note this method does not perform any deduplication of objects. It can be combined with
     * {@link #materializedDeduplicate(Iterator)} to achieve that.
     *
     * @param unsortedIterator An iterator which provides unsorted objects
     * @param comparator The comparator to use for sorting
     * @param <O> The type of the objects to be sorted
     * @return An iterator which returns the objects in sorted order
     */
    public static <O> Iterator<O> materializedSort(Iterator<O> unsortedIterator, Comparator<O> comparator) {
        final List<O> result = new ArrayList<>();
        while (unsortedIterator.hasNext()) {
            result.add(unsortedIterator.next());
        }
        result.sort(comparator);
        return result.iterator();
    }

    /**
     * De-duplicates the results returned by the given iterator, by wrapping it in a
     * {@link MaterializedDeduplicatedIterator}.
     */
    public static <O> Iterator<O> materializedDeduplicate(Iterator<O> iterator) {
        return new MaterializedDeduplicatedIterator<O>(iterator);
    }
}
