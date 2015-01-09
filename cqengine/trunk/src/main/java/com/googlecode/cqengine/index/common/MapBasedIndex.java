package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.index.Index;

import java.util.Set;

/**
 * An index which allows the set of distinct keys to be queried, and which can return statistics on the number of
 * objects stored for each key.
 *
 * Created by niall.gallagher on 09/01/2015.
 */
public interface MapBasedIndex<A, O> extends Index<O> {

    public Set<A> getDistinctKeys();

    public Integer getCountForKey(A key);
}
