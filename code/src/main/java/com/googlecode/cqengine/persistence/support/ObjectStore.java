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

//    Set<O> asSet();
}
