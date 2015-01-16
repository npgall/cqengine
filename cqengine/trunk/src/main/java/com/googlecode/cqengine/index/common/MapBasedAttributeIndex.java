package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.index.AttributeIndex;

/**
 * Implemented by indexes which are both map-based and attribute-centric.
 * See the extended interfaces for details.
 */
public interface MapBasedAttributeIndex<A, O> extends AttributeIndex<A, O>, MapBasedIndex<A, O> {
}
