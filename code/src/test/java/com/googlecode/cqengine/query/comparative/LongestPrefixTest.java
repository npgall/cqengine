package com.googlecode.cqengine.query.comparative;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.radixinverted.InvertedRadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.MobileTerminating;
import com.googlecode.cqengine.testutil.MobileTerminatingFactory;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SelfAttribute;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Unit tests for {@link LongestPrefix} query.
 * <p>
 * This class contains regular unit tests, AND parameterized tests for the longest prefix matching.
 * <p>
 * These tests are based on a use case of finding the longest matching mobile phone number, which is a more
 * typical use case for prefix matching than the car examples used in other tests.
 * <p>
 * These tests and the support for the {@link LongestPrefix} query, were contributed by Glen Lockhart of Openet-Labs.
 */
@RunWith(DataProviderRunner.class)
public class LongestPrefixTest {

    private static IndexedCollection<MobileTerminating> mobileTerminatingCache;
    private static IndexedCollection<MobileTerminating> mobileTerminatingCacheNoIndex;

    @DataProvider
    public static Object[][] mobileTerminatingScenarios() {
        return new Object[][] {
                {"35380",          "op1",       1},
                {"35380123",       "op1",       1},
                {"3538123",        "op2",       1},
                {"35382",          "op3",       1},
                {"353822",         "op4",       1},
                {"35387",          "op5",       1},
                {"3538712345",     "op6",       1},
                {"44123",          "op7",       1},
                {"4480",           "op8,op9",   2},
                {"33380",          "op10",      1},
                {"33381",          "op11",      1},
                {"1234",           "op12",      1},
                {"111",            "op13",      1},
                {"777",            "",          0},
                {"353",            "na",        1},
                {"354",            "",          0},
        };
    }

    @BeforeClass
    public static void setupMTCache() {
        mobileTerminatingCache = new ConcurrentIndexedCollection<MobileTerminating>();
        mobileTerminatingCache.addIndex(InvertedRadixTreeIndex.onAttribute(MobileTerminating.PREFIX));
        mobileTerminatingCache.addAll(MobileTerminatingFactory.getCollectionOfMobileTerminating());

        mobileTerminatingCacheNoIndex = new ConcurrentIndexedCollection<MobileTerminating>();
        mobileTerminatingCacheNoIndex.addAll(MobileTerminatingFactory.getCollectionOfMobileTerminating());
    }

    @Test
    @UseDataProvider(value = "mobileTerminatingScenarios")
    public void testLongestPrefix(String prefix, String expectedOperator, Integer expectedCount) {
        Query<MobileTerminating> q = longestPrefix(MobileTerminating.PREFIX, prefix);
        validateLongestPrefixWithCache(q, mobileTerminatingCache, expectedOperator, expectedCount);
    }

    @Test
    @UseDataProvider(value = "mobileTerminatingScenarios")
    public void testLongestPrefixWithoutIndex(String prefix, String expectedOperator, Integer expectedCount) {
        Query<MobileTerminating> q = longestPrefix(MobileTerminating.PREFIX, prefix);
        validateLongestPrefixWithCache(q, mobileTerminatingCacheNoIndex, expectedOperator, expectedCount);
    }


    public void validateLongestPrefixWithCache(Query<MobileTerminating> q, IndexedCollection<MobileTerminating> cache, String expectedOperator, Integer expectedCount) {
        ResultSet<MobileTerminating> res = cache.retrieve(q, queryOptions(orderBy(ascending(MobileTerminating.OPERATOR_NAME))));
        assertEquals(expectedCount, (Integer)res.size());
        Iterator<String> expectedOperators = Arrays.asList(expectedOperator.split(",")).iterator();
        for (MobileTerminating mt : res) {
            assertEquals(expectedOperators.next(), mt.getOperatorName());
        }
    }

    @Test
    public void testLongestPrefix() {
        Attribute<String, String> stringIdentity = new SelfAttribute<String>(String.class, "identity");
        assertTrue(LongestPrefix.countPrefixChars( "35387123456", "35387") > 0);
        assertEquals(5, LongestPrefix.countPrefixChars( "35387123456", "35387"));

        assertTrue(LongestPrefix.countPrefixChars("35387", "35387") > 0);
        assertEquals(5, LongestPrefix.countPrefixChars("35387", "35387"));

        assertFalse(LongestPrefix.countPrefixChars("35386123456", "35387") > 0);
        assertEquals(0, LongestPrefix.countPrefixChars("35386123456", "35387"));

        assertFalse(LongestPrefix.countPrefixChars("35386123456", "35387") > 0);
        assertEquals(0, LongestPrefix.countPrefixChars("35386123456", "35387"));

        assertFalse(LongestPrefix.countPrefixChars("3538", "35387") > 0);
        assertEquals(0, LongestPrefix.countPrefixChars("3538", "35387"));
    }
}
