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

import java.util.Collection;
import java.util.Set;

/**
 * An interface providing similar methods as {@link java.util.Set}, except the methods accept {@link QueryOptions}.
 * <p/>
 * This interface can thus wrap a standard on-heap Set, or an off-heap or disk implementation of a Set where the
 * implementation can extract details of the persistence to use from the supplied query options.
 */
public interface ObjectStore<O> {
    int size(QueryOptions queryOptions);

    boolean contains(Object o, QueryOptions queryOptions);

    CloseableIterator<O> iterator(QueryOptions queryOptions);

    boolean isEmpty(QueryOptions queryOptions);

    boolean add(O object, QueryOptions queryOptions);

    boolean remove(Object o, QueryOptions queryOptions);

    boolean containsAll(Collection<?> c, QueryOptions queryOptions);

    boolean addAll(Collection<? extends O> c, QueryOptions queryOptions);

    boolean retainAll(Collection<?> c, QueryOptions queryOptions);

    boolean removeAll(Collection<?> c, QueryOptions queryOptions);

    void clear(QueryOptions queryOptions);
}
