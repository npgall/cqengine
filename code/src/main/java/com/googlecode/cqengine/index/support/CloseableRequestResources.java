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
package com.googlecode.cqengine.index.support;

import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.io.Closeable;
import java.util.*;

/**
 * A QueryOption that allows to keep track of query resources which were opened to process a request and
 * which need to be closed when processing of the request is finished.
 * <p/>
 * The {@link #add(Closeable)} method is used to add {@link Closeable} objects to this object.
 * Then when processing the request has finished (often when {@link ResultSet#close()} is called), the engine will
 * retrieve this object from the query options and call the {@link #close()} method on this object, which will close
 * all resources which had been added.
 *
 * @author Silvano Riz
 */
public class CloseableRequestResources implements Closeable {

    final Collection<Closeable> requestResources = Collections.newSetFromMap(new IdentityHashMap<Closeable, Boolean>());

    /**
     * Add a new resource that needs to be closed.
     *
     * @param closeable The resource that needs to be closed
     */
    public void add(Closeable closeable) {
        requestResources.add(closeable);
    }

    public CloseableResourceGroup addGroup() {
        CloseableResourceGroup group = new CloseableResourceGroup();
        add(group);
        return group;
    }

    /**
     * Close and removes all resources and resource groups which have been added so far.
     */
    @Override
    public void close() {
        for (Iterator<Closeable> iterator = requestResources.iterator(); iterator.hasNext(); ) {
            Closeable closeable = iterator.next();
            closeQuietly(closeable);
            iterator.remove();
        }
    }

    /**
     * Returns an existing {@link CloseableRequestResources} from the QueryOptions, or adds a new
     * instance to the query options and returns that.
     *
     * @param queryOptions The {@link QueryOptions}
     * @return The existing QueryOptions's CloseableRequestResources or a new instance.
     */
    public static CloseableRequestResources forQueryOptions(final QueryOptions queryOptions) {
        CloseableRequestResources closeableRequestResources = queryOptions.get(CloseableRequestResources.class);
        if (closeableRequestResources == null) {
            closeableRequestResources = new CloseableRequestResources();
            queryOptions.put(CloseableRequestResources.class, closeableRequestResources);
        }
        return closeableRequestResources;
    }

    /**
     * Closes an existing {@link CloseableRequestResources} if one is stored the QueryOptions and then removes
     * it from the QueryOptions.
     *
     * @param queryOptions The {@link QueryOptions}
     */
    public static void closeForQueryOptions(QueryOptions queryOptions) {
        closeQuietly(queryOptions.get(CloseableRequestResources.class));

    }
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public class CloseableResourceGroup implements Closeable {
        final Set<Closeable> groupResources = Collections.newSetFromMap(new IdentityHashMap<Closeable, Boolean>());

        public boolean add(Closeable closeable) {
            return groupResources.add(closeable);
        }

        /**
         * Closes all resources in this group, and then removes the group from the request resources.
         */
        @Override
        public void close() {
            for (Iterator<Closeable> iterator = groupResources.iterator(); iterator.hasNext(); ) {
                Closeable closeable = iterator.next();
                CloseableRequestResources.closeQuietly(closeable);
                iterator.remove();
            }
            requestResources.remove(this);
        }

        @Override
        public String toString() {
            return groupResources.toString();
        }
    }
}
