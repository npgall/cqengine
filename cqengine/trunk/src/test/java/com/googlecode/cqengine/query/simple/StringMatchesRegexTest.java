package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.standingquery.StandingQueryIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.Test;

import static com.googlecode.cqengine.CQEngine.copyFrom;
import static com.googlecode.cqengine.attribute.SelfAttribute.self;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class StringMatchesRegexTest {

    @Test
    public void testStringMatchesRegex() {
        Query<String> query = matchesRegex(self(String.class), "f.*");
        IndexedCollection<String> collection = copyFrom(asList("foo1", "foo2", "bar", "baz", "car"));
        ResultSet<String> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testStringMatchesRegexWithIndex() {
        Query<String> query = matchesRegex(self(String.class), "f.*");
        IndexedCollection<String> collection = copyFrom(asList("foo1", "foo2", "bar", "baz", "car"));
        collection.addIndex(StandingQueryIndex.onQuery(query));
        ResultSet<String> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testStringMatchesRegexNegatedWithIndex() {
        Query<String> query = not(matchesRegex(self(String.class), "[fb].*"));
        IndexedCollection<String> collection = copyFrom(asList("foo1", "foo2", "bar", "baz", "car"));
        collection.addIndex(StandingQueryIndex.onQuery(query));
        ResultSet<String> results = collection.retrieve(query);
        assertEquals(1, results.size());
        assertTrue(results.iterator().hasNext());
    }
}