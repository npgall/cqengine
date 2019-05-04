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
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;
import com.googlecode.cqengine.testutil.Car;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static com.googlecode.cqengine.testutil.TestUtil.setOf;
import static java.util.Arrays.asList;

/**
 * Unit tests for {@link ConcurrentIndexedCollection}. Note that tests for support behavior (such as query processing)
 * which applies to all implementations of {@link IndexedCollection} can be found in
 * {@link com.googlecode.cqengine.IndexedCollectionFunctionalTest}.
 * <p/>
 * In addition to the unit tests in this class, this class also runs a further several hundred unit tests in
 * <a href="https://code.google.com/p/guava-libraries/source/browse/guava-testlib">guava-testlib</a> on the
 * IndexedCollection to validate its compliance with the API specifications of java.util.Set.
 *
 * @author Niall Gallagher
 */
public class ConcurrentIndexedCollectionTest extends TestCase {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(SetTestSuiteBuilder.using(onHeapIndexedCollectionGenerator())
                .withFeatures(CollectionSize.ANY, CollectionFeature.GENERAL_PURPOSE)
                .named("OnHeap_ConcurrentIndexedCollectionAPICompliance")
                .createTestSuite());
        suite.addTest(SetTestSuiteBuilder.using(offHeapIndexedCollectionGenerator())
                .withFeatures(CollectionSize.ANY, CollectionFeature.GENERAL_PURPOSE)
                .named("OffHeap_ConcurrentIndexedCollectionAPICompliance")
                .createTestSuite());
        suite.addTestSuite(ConcurrentIndexedCollectionTest.class);
        return suite;
    }

    private static TestStringSetGenerator onHeapIndexedCollectionGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>(OnHeapPersistence.onPrimaryKey(QueryFactory.selfAttribute(String.class)));
                indexedCollection.addAll(asList(elements));
                return indexedCollection;
            }
        };
    }

    private static TestStringSetGenerator offHeapIndexedCollectionGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>(OffHeapPersistence.onPrimaryKey(QueryFactory.selfAttribute(String.class)));
                indexedCollection.addAll(asList(elements));
                return indexedCollection;
            }
        };
    }

    public void testUpdate() {
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        Assert.assertTrue(indexedCollection.update(Collections.<String>emptyList(), asList("a", "b", "c")));

        Assert.assertEquals(setOf("a", "b", "c"), indexedCollection);

        Assert.assertTrue(indexedCollection.update(asList("b"), Collections.<String>emptyList()));
        Assert.assertEquals(setOf("a", "c"), indexedCollection);

        Assert.assertTrue(indexedCollection.update(asList("a"), asList("d")));
        Assert.assertEquals(setOf("c", "d"), indexedCollection);

        Assert.assertFalse(indexedCollection.update(asList("a"), Collections.<String>emptyList()));
        Assert.assertEquals(setOf("c", "d"), indexedCollection);

        Assert.assertTrue(indexedCollection.update(asList("c", "e"), Collections.<String>emptyList()));
        Assert.assertEquals(setOf("d"), indexedCollection);
    }

    public void testUpdate_IterableArguments() {
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        Assert.assertTrue(indexedCollection.update(asIterable(Collections.<String>emptyList()), asIterable(asList("a", "b", "c"))));
        Assert.assertEquals(setOf("a", "b", "c"), indexedCollection);

        Assert.assertTrue(indexedCollection.update(asIterable(asList("b")), asIterable(Collections.<String>emptyList())));
        Assert.assertEquals(setOf("a", "c"), indexedCollection);

        Assert.assertTrue(indexedCollection.update(asIterable(asList("a")), asIterable(asList("d"))));
        Assert.assertEquals(setOf("c", "d"), indexedCollection);

        Assert.assertFalse(indexedCollection.update(asIterable(asList("a")), asIterable(Collections.<String>emptyList())));
        Assert.assertEquals(setOf("c", "d"), indexedCollection);

        Assert.assertTrue(indexedCollection.update(asIterable(asList("c", "e")), asIterable(Collections.<String>emptyList())));
        Assert.assertEquals(setOf("d"), indexedCollection);

        Assert.assertTrue(indexedCollection.update(asIterable(Collections.<String>emptyList()), asIterable(asList("e", "d"))));
        Assert.assertEquals(setOf("d", "e"), indexedCollection);

        Assert.assertFalse(indexedCollection.update(asIterable(Collections.<String>emptyList()), asIterable(asList("e", "d"))));
        Assert.assertEquals(setOf("d", "e"), indexedCollection);
    }



    public void testGetIndexes() {
        IndexedCollection<Car> indexedCollection = new ConcurrentIndexedCollection<Car>();
        indexedCollection.addIndex(HashIndex.onAttribute(Car.CAR_ID), noQueryOptions());

        List<Index<Car>> indexes = new ArrayList<Index<Car>>();
        for (Index<Car> index : indexedCollection.getIndexes()) {
            indexes.add(index);
        }

        Assert.assertEquals(1, indexes.size());
        Assert.assertEquals(HashIndex.class, indexes.get(0).getClass());
    }

    public void testRemoveIndex() {
        IndexedCollection<Car> indexedCollection = new ConcurrentIndexedCollection<Car>();
        HashIndex<Integer, Car> index = HashIndex.onAttribute(Car.CAR_ID);

        indexedCollection.addIndex(index);
        Assert.assertEquals(1, IteratorUtil.countElements(indexedCollection.getIndexes()));

        indexedCollection.removeIndex(index);
        Assert.assertEquals(0, IteratorUtil.countElements(indexedCollection.getIndexes()));
    }


    static <O> Iterable<O> asIterable(final Collection<O> collection) {
        return new Iterable<O>() {
            @Override
            public Iterator<O> iterator() {
                return collection.iterator();
            }
        };
    }
}
