package com.googlecode.cqengine.index.indexOrdering;

import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;

public interface IConcurrentInvertedRadixTreesHolder {

    boolean containsKey(String fieldName);

    ConcurrentInvertedRadixTree getConcurrentInvertedRadixTree(String fieldName);


    void addConcurrentInvertedRadixTree(String fieldName, ConcurrentInvertedRadixTree concurrentInvertedRadixTree);



    void deleteConcurrentInvertedRadixTree(String fieldName);


}
