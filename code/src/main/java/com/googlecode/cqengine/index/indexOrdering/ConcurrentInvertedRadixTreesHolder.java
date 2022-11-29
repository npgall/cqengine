package com.googlecode.cqengine.index.indexOrdering;

import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;

import java.util.HashMap;
import java.util.Map;

public class ConcurrentInvertedRadixTreesHolder implements IConcurrentInvertedRadixTreesHolder {

    private final Map<String, IndexedConcurrentInvertedRadixTree> concurrentInvertedRadixTreeHashMap;


    public ConcurrentInvertedRadixTreesHolder() {
        this.concurrentInvertedRadixTreeHashMap = new HashMap<>();
    }

    public boolean containsKey(String fieldName) {
        return concurrentInvertedRadixTreeHashMap.containsKey(fieldName);
    }

    public IndexedConcurrentInvertedRadixTree getConcurrentInvertedRadixTree(String fieldName) {
        return concurrentInvertedRadixTreeHashMap.get(fieldName);
    }

    public void addConcurrentInvertedRadixTree(String fieldName, IndexedConcurrentInvertedRadixTree concurrentInvertedRadixTree) {
        concurrentInvertedRadixTreeHashMap.put(fieldName, concurrentInvertedRadixTree);
    }

    public void deleteIndexedConcurrentInvertedRadixTree(String fieldName) {
        concurrentInvertedRadixTreeHashMap.remove(fieldName);
    }


}
