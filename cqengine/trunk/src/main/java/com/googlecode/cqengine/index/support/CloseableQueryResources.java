/**
 * Copyright 2012 Niall Gallagher
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
package com.googlecode.cqengine.index.support;

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
     * Close and release all the resources.
     */
    public void closeAll() {
        for (Iterator<Closeable> iterator = closeableQueryResources.iterator(); iterator.hasNext(); ) {
            Closeable closeable = iterator.next();
            closeQuietly(closeable);
            iterator.remove();
        }
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
