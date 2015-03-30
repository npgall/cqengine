package com.googlecode.cqengine.index.support;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

/**
 * Utility class to facilitate the addition of ResultSet's resources in the QueryOptions' CloseableQueryResources.
 *
 * @author Silvano Riz
 */
public class CloseableSet implements Closeable {

    final Set<Closeable> closeables = Collections.newSetFromMap(new IdentityHashMap<Closeable, Boolean>());

    public boolean add(Closeable closeable) {
        return closeables.add(closeable);
    }

    @Override
    public void close() throws IOException {
        for (Iterator<Closeable> iterator = closeables.iterator(); iterator.hasNext(); ) {
            Closeable closeable = iterator.next();
            CloseableQueryResources.closeQuietly(closeable);
            iterator.remove();
        }
    }
}
