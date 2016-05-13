/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.Test;


import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class NoneTest {

    @Test
    public void testNone() {
        IndexedCollection<Integer> indexedCollection = new ConcurrentIndexedCollection<Integer>();
        indexedCollection.addAll(asList(1, 2, 3, 4, 5));
        IndexedCollection<Integer> collection = indexedCollection;
        Query<Integer> query = none(Integer.class);
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(0, results.size());
        assertFalse(results.iterator().hasNext());
    }

    @Test
    public void testNoneAnd() {
        IndexedCollection<Integer> indexedCollection = new ConcurrentIndexedCollection<Integer>();
        indexedCollection.addAll(asList(1, 2, 3, 4, 5));
        IndexedCollection<Integer> collection = indexedCollection;
        final And<Integer> query = and(
                none(Integer.class),
                lessThan(selfAttribute(Integer.class), 3)
        );
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(0, results.size());
        assertFalse(results.iterator().hasNext());
    }

    @Test
    public void testNoneOr() {
        IndexedCollection<Integer> indexedCollection = new ConcurrentIndexedCollection<Integer>();
        indexedCollection.addAll(asList(1, 2, 3, 4, 5));
        IndexedCollection<Integer> collection = indexedCollection;
        final Or<Integer> query = or(
                none(Integer.class),
                lessThan(selfAttribute(Integer.class), 3)
        );
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }
}