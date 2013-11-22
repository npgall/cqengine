package com.googlecode.cqengine.collection.impl;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.engine.impl.QueryEngineImpl;
import com.googlecode.cqengine.index.hash.HashIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 */
public class ConcurrentIndexedCollectionTest {

    public static class TestEntity {
        private final int value;

        public TestEntity(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestEntity that = (TestEntity) o;

            if (value != that.value) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }

    private static final Attribute<TestEntity, Integer> valueAttribute = new SimpleAttribute<TestEntity, Integer>() {
        @Override
        public Integer getValue(TestEntity object) {
            return object.getValue();
        }
    };

    private ConcurrentIndexedCollection<TestEntity> concurrentIndexedCollection;

    @Before
    public void setUp() {
        concurrentIndexedCollection = new ConcurrentIndexedCollection<TestEntity>(
                new DefaultConcurrentSetFactory<TestEntity>(),
                new QueryEngineImpl<TestEntity>());

        concurrentIndexedCollection.addIndex(HashIndex.onAttribute(valueAttribute));
    }

    @Test
    @SuppressWarnings({"SuspiciousMethodCalls"})
    public void testRemove() {
        concurrentIndexedCollection.add(new TestEntity(2));
        Assert.assertEquals(1, concurrentIndexedCollection.size());

        // Remove object of a type which cannot be stored in the collection...
        boolean removed = concurrentIndexedCollection.remove("foobar");
        Assert.assertFalse(removed);
        Assert.assertEquals(1, concurrentIndexedCollection.size());

        // Remove object of correct type but not equal to one stored in the collection...
        removed = concurrentIndexedCollection.remove(new TestEntity(1));
        Assert.assertFalse(removed);
        Assert.assertEquals(1, concurrentIndexedCollection.size());

        // Remove object equal to one stored in the collection...
        removed = concurrentIndexedCollection.remove(new TestEntity(2));
        Assert.assertTrue(removed);
        Assert.assertEquals(0, concurrentIndexedCollection.size());
    }

}
