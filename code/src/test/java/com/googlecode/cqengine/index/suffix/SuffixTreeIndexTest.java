package com.googlecode.cqengine.index.suffix;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radix.node.concrete.SmartArrayBasedNodeFactory;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SuffixTreeIndex}.
 *
 * Created by npgall on 13/05/2016.
 */
public class SuffixTreeIndexTest {

    @Test
    public void testNodeFactory() {
        SuffixTreeIndex<String, Car> index1 = SuffixTreeIndex.onAttribute(Car.MANUFACTURER);
        SuffixTreeIndex<String, Car> index2 = SuffixTreeIndex.onAttributeUsingNodeFactory(Car.MANUFACTURER, new DefaultCharArrayNodeFactory());
        SuffixTreeIndex<String, Car> index3 = SuffixTreeIndex.onAttributeUsingNodeFactory(Car.MANUFACTURER, new SmartArrayBasedNodeFactory());

        assertTrue(index1.nodeFactory instanceof DefaultCharArrayNodeFactory);
        assertTrue(index2.nodeFactory instanceof DefaultCharArrayNodeFactory);
        assertTrue(index3.nodeFactory instanceof SmartArrayBasedNodeFactory);
    }
}