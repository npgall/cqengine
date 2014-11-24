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
import static com.googlecode.cqengine.query.QueryFactory.lessThan;
import static com.googlecode.cqengine.query.QueryFactory.all;
import static com.googlecode.cqengine.query.option.DeduplicationStrategy.LOGICAL_ELIMINATION;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class AllTest {

    @Test
    public void testAll() {
        IndexedCollection<Integer> collection = copyFrom(asList(1, 2, 3, 4, 5));
        Query<Integer> query = all(Integer.class);
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(5, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testAllAnd() {
        IndexedCollection<Integer> collection = copyFrom(asList(1, 2, 3, 4, 5));
        final And<Integer> query = and(
                all(Integer.class),
                lessThan(self(Integer.class), 3)
        );
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testAllOr() {
        IndexedCollection<Integer> collection = copyFrom(asList(1, 2, 3, 4, 5));
        final Or<Integer> query = or(
                all(Integer.class),
                lessThan(self(Integer.class), 3)
        );
        ResultSet<Integer> results = collection.retrieve(query, queryOptions(deduplicate(Integer.class, LOGICAL_ELIMINATION)));
        assertEquals(5, results.size());
        assertTrue(results.iterator().hasNext());
    }
}