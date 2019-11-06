package com.googlecode.cqengine.metadata;

/**
 * Represents the frequency (i.e. the count of the number of occurrences of) a given key.
 */
public interface KeyFrequency<A> {

    A getKey();

    int getFrequency();
}
