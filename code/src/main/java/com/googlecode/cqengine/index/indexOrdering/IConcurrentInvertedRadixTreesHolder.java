package com.googlecode.cqengine.index.indexOrdering;

import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;

public interface IConcurrentInvertedRadixTreesHolder {

    boolean containsKey(String fieldName);

    IndexedConcurrentInvertedRadixTree getConcurrentInvertedRadixTree(String fieldName);


    void addConcurrentInvertedRadixTree(String fieldName, IndexedConcurrentInvertedRadixTree concurrentInvertedRadixTree);



    void deleteIndexedConcurrentInvertedRadixTree(String fieldName);


}
