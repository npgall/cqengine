package com.googlecode.cqengine.index.radix;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radix.node.concrete.SmartArrayBasedNodeFactory;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link RadixTreeIndex}.
 *
 * Created by npgall on 13/05/2016.
 */
public class RadixTreeIndexTest {

    @Test
    public void testNodeFactory() {
        RadixTreeIndex<String, Car> index1 = RadixTreeIndex.onAttribute(Car.MANUFACTURER);
        RadixTreeIndex<String, Car> index2 = RadixTreeIndex.onAttributeUsingNodeFactory(Car.MANUFACTURER, new DefaultCharArrayNodeFactory());
        RadixTreeIndex<String, Car> index3 = RadixTreeIndex.onAttributeUsingNodeFactory(Car.MANUFACTURER, new SmartArrayBasedNodeFactory());

        assertTrue(index1.nodeFactory instanceof DefaultCharArrayNodeFactory);
        assertTrue(index2.nodeFactory instanceof DefaultCharArrayNodeFactory);
        assertTrue(index3.nodeFactory instanceof SmartArrayBasedNodeFactory);
    }
}