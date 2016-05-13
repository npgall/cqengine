package com.googlecode.cqengine.index.radixinverted;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radix.node.concrete.SmartArrayBasedNodeFactory;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link InvertedRadixTreeIndex}.
 *
 * Created by npgall on 13/05/2016.
 */
public class InvertedRadixTreeIndexTest {

    @Test
    public void testNodeFactory() {
        InvertedRadixTreeIndex<String, Car> index1 = InvertedRadixTreeIndex.onAttribute(Car.MANUFACTURER);
        InvertedRadixTreeIndex<String, Car> index2 = InvertedRadixTreeIndex.onAttributeUsingNodeFactory(Car.MANUFACTURER, new DefaultCharArrayNodeFactory());
        InvertedRadixTreeIndex<String, Car> index3 = InvertedRadixTreeIndex.onAttributeUsingNodeFactory(Car.MANUFACTURER, new SmartArrayBasedNodeFactory());

        assertTrue(index1.nodeFactory instanceof DefaultCharArrayNodeFactory);
        assertTrue(index2.nodeFactory instanceof DefaultCharArrayNodeFactory);
        assertTrue(index3.nodeFactory instanceof SmartArrayBasedNodeFactory);
    }
}