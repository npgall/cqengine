package com.googlecode.cqengine.persistence.disk.chronicle;

import com.googlecode.cqengine.index.support.CloseableIterator;
import net.openhft.chronicle.map.ChronicleMap;

import java.util.Iterator;
import java.util.Map;

public class ChronicleMapIterator<O, A extends Comparable<A>> implements CloseableIterator<O> {
    private final Iterator<Map.Entry<A, O>> iterator;

    public ChronicleMapIterator(ChronicleMap<A, O> map) {
        iterator = map.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public O next() {
        return iterator.next().getValue();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void close() {
    }
}
