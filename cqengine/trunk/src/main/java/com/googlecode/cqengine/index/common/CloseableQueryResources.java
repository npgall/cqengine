package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.query.option.QueryOptions;

import java.io.Closeable;
import java.util.*;

/**
 * A QueryOption that allows to keep track of query resources that need to be closed.<br>
 * The engine will then make sure that, when the outer result-set close method is called, all the resources are closed.
 *
 * @author Silvano Riz
 */
public class CloseableQueryResources {

    final Collection<Closeable> closeableQueryResources = Collections.newSetFromMap(new IdentityHashMap<Closeable, Boolean>());

    CloseableQueryResources() {
    }

    /**
     * Method that returns the existing CloseableQueryResources in the QueryOptions or a new
     * instance.<br>
     * The method takes care of adding the new instance to the passed {@link QueryOptions}.
     *
     * @param queryOptions The {@link QueryOptions}
     * @return The existing QueryOptions's CloseableQueryResources or a new instance.
     */
    public static CloseableQueryResources from(final QueryOptions queryOptions) {
        CloseableQueryResources closeableQueryResources = queryOptions.get(CloseableQueryResources.class);
        if (closeableQueryResources == null) {
            closeableQueryResources = new CloseableQueryResources();
            queryOptions.put(CloseableQueryResources.class, closeableQueryResources);
        }
        return closeableQueryResources;
    }

    /**
     * Add a new resource that needs to be closed.
     *
     * @param closeable The resource that needs to be closed
     */
    public void add(Closeable closeable) {
        closeableQueryResources.add(closeable);
    }

    /**
     * Close and release
     *
     * @param closeable the resource to close and release.
     */
    public void closeAndRemove(Closeable closeable) {
        closeQuietly(closeable);
        closeableQueryResources.remove(closeable);
    }

    /**
     * Close and release all the resources.
     */
    public void closeAll() {
        for (Closeable closeable : closeableQueryResources) {
            closeQuietly(closeable);
        }
        closeableQueryResources.clear();
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        }
        catch (Exception e) {
            // Ignore
        }
    }
}
