package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.Test;


import static com.googlecode.cqengine.CQEngine.copyFrom;
import static com.googlecode.cqengine.attribute.SelfAttribute.self;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class NoneTest {

    @Test
    public void testNone() {
        IndexedCollection<Integer> collection = copyFrom(asList(1, 2, 3, 4, 5));
        Query<Integer> query = none(Integer.class);
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(0, results.size());
        assertFalse(results.iterator().hasNext());
    }

    @Test
    public void testNoneAnd() {
        IndexedCollection<Integer> collection = copyFrom(asList(1, 2, 3, 4, 5));
        final And<Integer> query = and(
                none(Integer.class),
                lessThan(self(Integer.class), 3)
        );
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(0, results.size());
        assertFalse(results.iterator().hasNext());
    }

    @Test
    public void testNoneOr() {
        IndexedCollection<Integer> collection = copyFrom(asList(1, 2, 3, 4, 5));
        final Or<Integer> query = or(
                none(Integer.class),
                lessThan(self(Integer.class), 3)
        );
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }
}