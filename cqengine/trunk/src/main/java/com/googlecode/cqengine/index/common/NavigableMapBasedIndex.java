package com.googlecode.cqengine.index.common;

import java.util.NavigableSet;

/**
 * An index which allows the set of distinct keys to be queried in sorted order, and which can return statistics on the
 * number of objects stored for each key.
 *
 * Created by niall.gallagher on 09/01/2015.
 */
public interface NavigableMapBasedIndex<A, O> extends MapBasedIndex<A, O> {

    @Override
    public NavigableSet<A> getDistinctKeys();
}
