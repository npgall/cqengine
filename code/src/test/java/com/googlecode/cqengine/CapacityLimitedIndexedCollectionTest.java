package com.googlecode.cqengine;

import com.google.common.collect.testing.TestStringSetGenerator;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static java.util.Arrays.asList;

/**
 * @author wayne
 * @since 0.1.0
 */
public class CapacityLimitedIndexedCollectionTest extends TestCase {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(CapacityLimitedIndexedCollectionTest.class);
        return suite;
    }

    private static TestStringSetGenerator onHeapIndexedCollectionGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<>(OnHeapPersistence.onPrimaryKey(QueryFactory.selfAttribute(String.class)));
                indexedCollection.addAll(asList(elements));
                return indexedCollection;
            }
        };
    }

    private static TestStringSetGenerator offHeapIndexedCollectionGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<>(OffHeapPersistence.onPrimaryKey(QueryFactory.selfAttribute(String.class)));
                indexedCollection.addAll(asList(elements));
                return indexedCollection;
            }
        };
    }

    public void testAdd() {
        IndexedCollection<TestEntry> coll = new CapacityLimitedIndexedCollection<>(5);

        coll.addIndex(HashIndex.onAttribute(TestEntry.NAME));

        for (int i = 0; i < 5; i++) {
            coll.add(new TestEntry(String.valueOf(i), (long) i));
        }

        Query<TestEntry> q = equal(TestEntry.NAME, "3");
        coll.retrieve(q).forEach(System.out::println);

        Exception ex = Assert.assertThrows(RuntimeException.class, () -> coll.add(new TestEntry("5", 5L)));
        Assert.assertTrue(ex.getMessage().startsWith("Reached maximum"));

        System.out.println("Expected error occurred: " + ex.getMessage());
    }

    public void testAddWithFIFO() {
        IndexedCollection<TestEntry> coll = new CapacityLimitedIndexedCollection<>(
                5,
                3,
                CapacityLimitedIndexedCollection.Eviction.FIFO,
                TestEntry.ORDER
        );

        coll.addIndex(HashIndex.onAttribute(TestEntry.NAME));

        for (int i = 0; i <= 100; i++) {
            coll.add(new TestEntry(String.valueOf(i), (long) i));
        }

        Assert.assertTrue(coll.size() <= 5);
    }

    public void testAddAll() {
        IndexedCollection<TestEntry> coll = new CapacityLimitedIndexedCollection<>(5);

        coll.addIndex(HashIndex.onAttribute(TestEntry.NAME));

        for (int i = 0; i < 3; i++) {
            coll.add(new TestEntry(String.valueOf(i), (long) i));
        }

        Query<TestEntry> q = equal(TestEntry.NAME, "2");
        coll.retrieve(q).forEach(System.out::println);

        Collection<TestEntry> list = new ArrayList<>();
        list.add(new TestEntry("3", 3L));
        list.add(new TestEntry("4", 4L));
        list.add(new TestEntry("5", 5L));

        Exception ex = Assert.assertThrows(RuntimeException.class, () -> coll.addAll(list));
        Assert.assertTrue(ex.getMessage().startsWith("Reached maximum"));

        System.out.println("Expected error occurred: " + ex.getMessage());
    }

    static class TestEntry {
        private final String name;
        private final Long order;

        public TestEntry(String name, Long order) {
            this.name = name;
            this.order = order;
        }

        public String getName() {
            return name;
        }

        public Long getOrder() {
            return order;
        }

        // -------------------------- Attributes --------------------------
        public static final Attribute<TestEntry, String> NAME = new SimpleAttribute<TestEntry, String>("name") {
            public String getValue(TestEntry entry, QueryOptions queryOptions) { return entry.name; }
        };

        public static final Attribute<TestEntry, Long> ORDER = new SimpleAttribute<TestEntry, Long>("order") {
            public Long getValue(TestEntry entry, QueryOptions queryOptions) { return entry.order; }
        };

        @Override
        public String toString() {
            return "TestEntry{" +
                   "name='" + name + '\'' +
                   '}';
        }
    }
}
