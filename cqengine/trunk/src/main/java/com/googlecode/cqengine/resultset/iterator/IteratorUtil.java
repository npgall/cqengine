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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

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
     * @return A flattened stream of {@code KeyValue} objects
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
}
