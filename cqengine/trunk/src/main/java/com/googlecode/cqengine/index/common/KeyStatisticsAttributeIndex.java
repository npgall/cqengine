package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.index.AttributeIndex;

/**
 * Implemented by indexes which support key statistics and are attribute-centric.
 * See the extended interfaces for details.
 */
public interface KeyStatisticsAttributeIndex<A, O> extends AttributeIndex<A, O>, KeyStatisticsIndex<A, O> {
}
