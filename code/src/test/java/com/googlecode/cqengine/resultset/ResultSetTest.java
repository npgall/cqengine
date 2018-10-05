package com.googlecode.cqengine.resultset;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.QueryFactory.selfAttribute;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

/**
 * Unit tests for the base {@link ResultSet}.
 */
public class ResultSetTest {

    @Test
    public void testStream() {
        List<String> input = asList("a", "b", "c", "d");
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(input);

        ResultSet<String> resultSet = indexedCollection.retrieve(all(String.class), queryOptions(orderBy(ascending(selfAttribute(String.class)))));

        Stream<String> stream = resultSet.stream();

        List<String> output = stream.collect(toList());
        assertEquals(input, output);
    }

    @Test
    public void testStreamClose() {
        AtomicBoolean closeCalled = new AtomicBoolean();
        ResultSet<Integer> resultSet = new StoredSetBasedResultSet<Integer>(emptySet()) {
            @Override
            public void close() {
                super.close();
                closeCalled.set(true);
            }
        };

        Stream<Integer> stream = resultSet.stream();

        assertFalse(closeCalled.get());
        stream.close();
        assertTrue(closeCalled.get());
    }
}