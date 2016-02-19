package com.googlecode.cqengine.resultset.order;

import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;
import org.junit.Test;
import org.junit.Assert;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author dsmith
 */
public class MaterializingResultSetTest {
    @Test
    public void testMaterializingResultSetIterator() throws Exception {
        final MaterializingResultSet<Object> set = new MaterializingResultSet<Object>(new StoredSetBasedResultSet<Object>(Collections.<Object>singleton(this)), null, null);
        final Iterator<Object> it = set.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertTrue(it.hasNext());
    }
}
