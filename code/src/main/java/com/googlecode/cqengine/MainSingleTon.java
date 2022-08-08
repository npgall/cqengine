package com.googlecode.cqengine;

import com.googlecode.cqengine.index.indexOrdering.IndexOrderingConcurrentTreeHolder;

public class MainSingleTon {
    public static void main(String[] args) {

        final IndexOrderingConcurrentTreeHolder instance = IndexOrderingConcurrentTreeHolder.INSTANCE;


    }
}
