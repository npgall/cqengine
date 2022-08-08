package com.googlecode.cqengine.index.indexOrdering;

import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;

import java.util.HashMap;
import java.util.Map;

public enum IndexOrderingConcurrentTreeHolder {

    INSTANCE;

    private final Map<LookUpIdentifier, ConcurrentInvertedRadixTree> concurrentInvertedRadixTreeHashMap;


    private IndexOrderingConcurrentTreeHolder() {
        System.out.println("Here");
        concurrentInvertedRadixTreeHashMap = new HashMap<>();
    }


    public boolean containsKey(LookUpIdentifier lookUpIdentifier) {
        return concurrentInvertedRadixTreeHashMap.containsKey(lookUpIdentifier);
    }

    public ConcurrentInvertedRadixTree getConcurrentInvertedRadixTree(LookUpIdentifier lookUpIdentifier) {
        return concurrentInvertedRadixTreeHashMap.get(lookUpIdentifier);
    }

    public void addConcurrentInvertedRadixTree(LookUpIdentifier lookUpIdentifier, ConcurrentInvertedRadixTree concurrentInvertedRadixTree) {
        concurrentInvertedRadixTreeHashMap.put(lookUpIdentifier, concurrentInvertedRadixTree);
    }

    public void deleteConcurrentInvertedRadixTree(LookUpIdentifier lookUpIdentifier) {
        concurrentInvertedRadixTreeHashMap.remove(lookUpIdentifier);
    }


}
