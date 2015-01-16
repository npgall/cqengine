package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.index.AttributeIndex;

/**
 * Implemented by indexes which are both navigable map-based and attribute-centric.
 * See the extended interfaces for details.
 */
public interface NavigableMapBasedAttributeIndex<A, O> extends NavigableMapBasedIndex<A, O>, AttributeIndex<A, O> {
}
