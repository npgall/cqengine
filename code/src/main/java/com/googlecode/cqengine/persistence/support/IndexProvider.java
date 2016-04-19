package com.googlecode.cqengine.persistence.support;

import com.googlecode.cqengine.index.AttributeIndex;

/**
 * A POJO which the query engine adds to query options to provide indexes which use {@link ExternalPersistence} and
 * which are on non-primary key attributes (aka secondary indexes), with access to the index on the primary key
 * attribute.
 * <p/>
 * Those secondary indexes will use the primary key index to look up the actual objects associated with the foreign keys
 * stored in the secondary indexes.
 */
public class IndexProvider<K, O> {

    final AttributeIndex<K, O> primaryKeyIndex;

    public IndexProvider(AttributeIndex<K, O> primaryKeyIndex) {
        if (primaryKeyIndex == null) {
            throw new NullPointerException("The primaryKeyIndex argument cannot be null");
        }
        this.primaryKeyIndex = primaryKeyIndex;
    }

    public AttributeIndex<K, O> getPrimaryKeyIndex() {
        return primaryKeyIndex;
    }
}
