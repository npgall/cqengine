package com.googlecode.cqengine.pk;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.unique.UniqueIndex;

import java.util.concurrent.ConcurrentMap;

/**
 * Indexed collection backed by primary key to object map
 */
public class PrimaryKeyIndexedCollection<O, A extends Comparable<A>> extends ConcurrentIndexedCollection<O> {

    private ConcurrentMap<A, O> pkMap;

    public PrimaryKeyIndexedCollection(SimpleAttribute<O, A> attribute) {
        this(new PrimaryKeyOnHeapPersistence<>(attribute));
    }

    public PrimaryKeyIndexedCollection(PrimaryKeyOnHeapPersistence<O, A> persistence) {
        super(persistence);
        pkMap = ((PrimaryKeyOnHeapObjectStore) objectStore).getPkMap();
        UniqueIndex<A, O> pkIndex = UniqueIndex.onAttribute(persistence.getPrimaryKeyAttribute());
        addIndex(pkIndex);

    }

    /**
     * Fast access to entry by Key
     * @param key
     * @return
     */
    public O get(A key){
        return pkMap.get(key);
    }
}
