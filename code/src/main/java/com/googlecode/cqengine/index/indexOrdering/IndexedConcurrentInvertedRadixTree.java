package com.googlecode.cqengine.index.indexOrdering;

import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;

import java.util.Objects;

public class IndexedConcurrentInvertedRadixTree {

    private final ConcurrentInvertedRadixTree concurrentInvertedRadixTree;

    private Integer index;

    public IndexedConcurrentInvertedRadixTree(ConcurrentInvertedRadixTree concurrentInvertedRadixTree) {
        this.concurrentInvertedRadixTree = concurrentInvertedRadixTree;
        index = 0;
    }

    public ConcurrentInvertedRadixTree getConcurrentInvertedRadixTree() {
        return concurrentInvertedRadixTree;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexedConcurrentInvertedRadixTree)) {
            return false;

        }
        IndexedConcurrentInvertedRadixTree that = (IndexedConcurrentInvertedRadixTree) o;
        return concurrentInvertedRadixTree.equals(that.concurrentInvertedRadixTree) && index.equals(that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(concurrentInvertedRadixTree, index);
    }
}
