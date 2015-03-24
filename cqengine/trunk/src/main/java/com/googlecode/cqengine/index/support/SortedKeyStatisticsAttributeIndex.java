package com.googlecode.cqengine.index.support;

import com.googlecode.cqengine.index.AttributeIndex;

/**
 * Implemented by indexes which support sorted key statistics and are attribute-centric.
 * See the extended interfaces for details.
 */
public interface SortedKeyStatisticsAttributeIndex<A extends Comparable<A>, O> extends SortedKeyStatisticsIndex<A, O>, AttributeIndex<A, O> {
}
