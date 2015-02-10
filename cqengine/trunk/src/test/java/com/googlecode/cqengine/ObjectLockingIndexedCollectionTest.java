package com.googlecode.cqengine;

import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.googlecode.cqengine.index.common.DefaultConcurrentSetFactory;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.Set;

/**
 * Unit tests for {@link ObjectLockingIndexedCollection}. Note that tests for common behavior (such as query processing)
 * which applies to all implementations of {@link IndexedCollection} can be found in
 * {@link com.googlecode.cqengine.IndexedCollectionFunctionalTest}.
 * <p/>
 * In addition to the unit tests in this class, this class also runs a further 197 unit tests in
 * <a href="https://code.google.com/p/guava-libraries/source/browse/guava-testlib">guava-testlib</a> on the
 * IndexedCollection to validate its compliance with the API specifications of java.util.Set.
 *
 * @author Niall Gallagher
 */
public class ObjectLockingIndexedCollectionTest extends TestCase {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(SetTestSuiteBuilder.using(indexedCollectionGenerator())
                .withFeatures(CollectionSize.ANY, CollectionFeature.GENERAL_PURPOSE)
                .named("ObjectLockingIndexedCollectionAPICompliance")
                .createTestSuite());
        suite.addTestSuite(ObjectLockingIndexedCollectionTest.class);
        return suite;
    }

    private static TestStringSetGenerator indexedCollectionGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                IndexedCollection<String> indexedCollection = new ObjectLockingIndexedCollection<String>();
                indexedCollection.addAll(Arrays.asList(elements));
                return indexedCollection;
            }
        };
    }

    public void testConstructor() {
        ObjectLockingIndexedCollection<Integer> collection1 = new ObjectLockingIndexedCollection<Integer>();
        ObjectLockingIndexedCollection<Integer> collection2 = new ObjectLockingIndexedCollection<Integer>(new DefaultConcurrentSetFactory<Integer>());
        ObjectLockingIndexedCollection<Integer> collection3 = new ObjectLockingIndexedCollection<Integer>(64);

        assertEquals(64, collection1.stripedLock.concurrencyLevel);
        assertEquals(64, collection2.stripedLock.concurrencyLevel);
        assertEquals(64, collection3.stripedLock.concurrencyLevel);
    }
}
