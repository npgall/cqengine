package com.googlecode.cqengine.resultset;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.resultset.common.ResultSets;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;
import org.junit.Test;

import java.util.Collection;

import static com.googlecode.cqengine.query.QueryFactory.all;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class ResultSetsTest {

    @Test
    public void testAsCollection() throws Exception {
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(asList("how", "now", "brown", "cow"));

        ResultSet<String> resultSet = indexedCollection.retrieve(all(String.class));
        Collection<String> collection = ResultSets.asCollection(resultSet);

        assertEquals(resultSet.size(), collection.size());
        assertEquals(resultSet.size(), IteratorUtil.countElements(collection));
        assertEquals(resultSet.isEmpty(), collection.isEmpty());
        assertTrue(collection.contains("now"));
        assertFalse(collection.contains("baz"));
    }
}