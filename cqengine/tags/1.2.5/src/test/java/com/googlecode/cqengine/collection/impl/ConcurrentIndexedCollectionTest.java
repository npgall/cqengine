/**
 * Copyright 2012 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.collection.impl;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.engine.impl.QueryEngineImpl;
import com.googlecode.cqengine.index.hash.HashIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Atul Vasu
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
        concurrentIndexedCollection.add(new TestEntity(1));
        Assert.assertEquals(1, concurrentIndexedCollection.size());

        // Remove object of a type which cannot be stored in the collection...
        boolean removed = concurrentIndexedCollection.remove("foobar");
        Assert.assertFalse(removed);
        Assert.assertEquals(1, concurrentIndexedCollection.size());

        // Remove object of correct type but not equal to one stored in the collection...
        removed = concurrentIndexedCollection.remove(new TestEntity(2));
        Assert.assertFalse(removed);
        Assert.assertEquals(1, concurrentIndexedCollection.size());

        // Remove object equal to one stored in the collection...
        removed = concurrentIndexedCollection.remove(new TestEntity(1));
        Assert.assertTrue(removed);
        Assert.assertEquals(0, concurrentIndexedCollection.size());
    }

}
