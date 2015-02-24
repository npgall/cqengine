package com.googlecode.cqengine.index.offheap.support;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

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
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            }
            catch (Exception e) {
                // ignore.
            }
        }
        closeables.clear();
    }
}
