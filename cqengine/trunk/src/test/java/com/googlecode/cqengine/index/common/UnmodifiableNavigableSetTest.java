package com.googlecode.cqengine.index.common;

import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.testing.NavigableSetTestSuiteBuilder;
import com.google.common.collect.testing.SafeTreeSet;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;

/**
 * Unit tests for {@link UnmodifiableNavigableSet}.
 * <p/>
 * Uses guava-testlib to validate compliance; in total there are 175 tests.
 *
 * @author Niall Gallagher
 */
public class UnmodifiableNavigableSetTest extends TestCase {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(NavigableSetTestSuiteBuilder.using(testStringSetGenerator())
                .named("UnmodifiableNavigableSetAPICompliance")
                .withFeatures(CollectionSize.ANY, CollectionFeature.KNOWN_ORDER)
                .createTestSuite());

        suite.addTestSuite(UnmodifiableNavigableSetTest.class);
        return suite;
    }

    static TestStringSetGenerator testStringSetGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                SafeTreeSet<String> set = new SafeTreeSet<String>(Arrays.asList(elements));
                return new UnmodifiableNavigableSet<String>(set);
            }

            @Override
            public List<String> order(List<String> insertionOrder) {
                return Ordering.natural().sortedCopy(insertionOrder);
            }
        };
    }

    @SuppressWarnings("EmptyCatchBlock")
    public void testUnmodifiableNavigableSet() {
        TreeSet<Integer> mod = Sets.newTreeSet();
        mod.add(1);
        mod.add(2);
        mod.add(3);

        NavigableSet<Integer> unmod = new UnmodifiableNavigableSet<Integer>(mod);

        mod.add(4);
        assertTrue(unmod.contains(4));
        assertTrue(unmod.descendingSet().contains(4));

        ensureNotDirectlyModifiable(unmod);
        ensureNotDirectlyModifiable(unmod.descendingSet());
        ensureNotDirectlyModifiable(unmod.headSet(2));
        ensureNotDirectlyModifiable(unmod.headSet(2, true));
        ensureNotDirectlyModifiable(unmod.tailSet(2));
        ensureNotDirectlyModifiable(unmod.tailSet(2, true));
        ensureNotDirectlyModifiable(unmod.subSet(1, 3));
        ensureNotDirectlyModifiable(unmod.subSet(1, true, 3, true));

        NavigableSet<Integer> reverse = unmod.descendingSet();
        try {
            reverse.add(4);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            reverse.addAll(Collections.singleton(4));
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            reverse.remove(4);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    static void ensureNotDirectlyModifiable(SortedSet<Integer> unmod) {
        try {
            unmod.add(4);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            unmod.remove(4);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            unmod.addAll(Collections.singleton(4));
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            Iterator<Integer> iterator = unmod.iterator();
            iterator.next();
            iterator.remove();
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    static void ensureNotDirectlyModifiable(NavigableSet<Integer> unmod) {
        try {
            unmod.add(4);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            unmod.remove(4);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            unmod.addAll(Collections.singleton(4));
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            unmod.pollFirst();
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            unmod.pollLast();
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            Iterator<Integer> iterator = unmod.iterator();
            iterator.next();
            iterator.remove();
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            Iterator<Integer> iterator = unmod.descendingIterator();
            iterator.next();
            iterator.remove();
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
        }
    }
}