package com.googlecode.cqengine.index.radixreversed;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radix.node.concrete.SmartArrayBasedNodeFactory;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link ReversedRadixTreeIndex}.
 *
 * Created by npgall on 13/05/2016.
 */
public class ReversedRadixTreeIndexTest {

    @Test
    public void testNodeFactory() {
        ReversedRadixTreeIndex<String, Car> index1 = ReversedRadixTreeIndex.onAttribute(Car.MANUFACTURER);
        ReversedRadixTreeIndex<String, Car> index2 = ReversedRadixTreeIndex.onAttributeUsingNodeFactory(Car.MANUFACTURER, new DefaultCharArrayNodeFactory());
        ReversedRadixTreeIndex<String, Car> index3 = ReversedRadixTreeIndex.onAttributeUsingNodeFactory(Car.MANUFACTURER, new SmartArrayBasedNodeFactory());

        assertTrue(index1.nodeFactory instanceof DefaultCharArrayNodeFactory);
        assertTrue(index2.nodeFactory instanceof DefaultCharArrayNodeFactory);
        assertTrue(index3.nodeFactory instanceof SmartArrayBasedNodeFactory);
    }
}