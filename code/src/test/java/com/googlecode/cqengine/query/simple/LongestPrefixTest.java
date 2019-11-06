package com.googlecode.cqengine.query.simple;

import static com.googlecode.cqengine.query.QueryFactory.longestPrefix;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SelfAttribute;

public class LongestPrefixTest {

    @Test
    public void testLongestPrefix() {
        Attribute<String, String> stringIdentity = new SelfAttribute<String>(String.class, "identity");
        assertTrue(longestPrefix(stringIdentity, "35387123456").matches("35387", noQueryOptions()));
        assertTrue(longestPrefix(stringIdentity, "35387").matches("35387", noQueryOptions()));
        assertFalse(longestPrefix(stringIdentity, "35386123456").matches("35387", noQueryOptions()));
        assertFalse(longestPrefix(stringIdentity, "35386123456").matches("35387", noQueryOptions()));
        assertFalse(longestPrefix(stringIdentity, "3538").matches("35387", noQueryOptions()));
        
    }
}
