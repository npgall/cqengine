package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.offheap.support.CloseableIterable;

/**
 * An index which allows the set of distinct keys to be queried, and which can return statistics on the number of
 * objects stored in the buckets for each key.
 * <p/>
 * Note that this interface reads statistics about keys and NOT about attribute values from the index.
 * Often those statistics will be the same, however if a {@link com.googlecode.cqengine.quantizer.Quantizer} is
 * configured for an index, then often objects for several attribute values may be stored in the same bucket.
 * The statistics returned by this interface refer to the bucket as a whole.
 *
 * Created by niall.gallagher on 09/01/2015.
 */
public interface KeyStatisticsIndex<A, O> extends Index<O> {

    /**
     * @return The distinct keys in the index
     */
    public CloseableIterable<A> getDistinctKeys();

    /**
     * @param key A key which may be contained in the index
     * @return The number of objects stored in the bucket in the index with the given key
     */
    public Integer getCountForKey(A key);
}
