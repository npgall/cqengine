/**
 * Copyright 2012-2015 Niall Gallagher
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
package com.googlecode.cqengine;

import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.query.QueryFactory;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.Set;

/**
 * Unit tests for {@link ObjectLockingIndexedCollection}. Note that tests for support behavior (such as query processing)
 * which applies to all implementations of {@link IndexedCollection} can be found in
 * {@link com.googlecode.cqengine.IndexedCollectionFunctionalTest}.
 * <p/>
 * In addition to the unit tests in this class, this class also runs a further several hundred unit tests in
 * <a href="https://code.google.com/p/guava-libraries/source/browse/guava-testlib">guava-testlib</a> on the
 * IndexedCollection to validate its compliance with the API specifications of java.util.Set.
 *
 * @author Niall Gallagher
 */
public class ObjectLockingIndexedCollectionTest extends TestCase {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(SetTestSuiteBuilder.using(onHeapIndexedCollectionGenerator())
                .withFeatures(CollectionSize.ANY, CollectionFeature.GENERAL_PURPOSE)
                .named("OnHeap_ObjectLockingIndexedCollectionAPICompliance")
                .createTestSuite());
        suite.addTest(SetTestSuiteBuilder.using(offHeapIndexedCollectionGenerator())
                .withFeatures(CollectionSize.ANY, CollectionFeature.GENERAL_PURPOSE)
                .named("OffHeap_ObjectLockingIndexedCollectionAPICompliance")
                .createTestSuite());
        suite.addTestSuite(ObjectLockingIndexedCollectionTest.class);
        return suite;
    }

    private static TestStringSetGenerator onHeapIndexedCollectionGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                IndexedCollection<String> indexedCollection = new ObjectLockingIndexedCollection<String>(OnHeapPersistence.onPrimaryKey(QueryFactory.selfAttribute(String.class)));
                indexedCollection.addAll(Arrays.asList(elements));
                return indexedCollection;
            }
        };
    }

    private static TestStringSetGenerator offHeapIndexedCollectionGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                IndexedCollection<String> indexedCollection = new ObjectLockingIndexedCollection<String>(OffHeapPersistence.onPrimaryKey(QueryFactory.selfAttribute(String.class)));
                indexedCollection.addAll(Arrays.asList(elements));
                return indexedCollection;
            }
        };
    }

    public void testConstructor() {
        ObjectLockingIndexedCollection<Integer> collection1 = new ObjectLockingIndexedCollection<Integer>();
        ObjectLockingIndexedCollection<Integer> collection2 = new ObjectLockingIndexedCollection<Integer>(new OnHeapPersistence<Integer, Integer>());
        ObjectLockingIndexedCollection<Integer> collection3 = new ObjectLockingIndexedCollection<Integer>(64);

        assertEquals(64, collection1.stripedLock.concurrencyLevel);
        assertEquals(64, collection2.stripedLock.concurrencyLevel);
        assertEquals(64, collection3.stripedLock.concurrencyLevel);
    }
}
