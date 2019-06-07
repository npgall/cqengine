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
package com.googlecode.cqengine.engine;

import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * @author Niall Gallagher
 */
public interface ModificationListener<O> {

    /**
     * Notifies the listener that the specified objects are being added to the collection, and so it can take action
     * and update its internal data structures.
     *  @param objectSet The objects being added
     * @param queryOptions Optional parameters for the update
     */
    public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions);

    /**
     * Notifies the listener that the specified objects are being removed from the collection, and so it can take action
     * and update its internal data structures.
     *  @param objectSet The objects being removed
     * @param queryOptions Optional parameters for the update
     */
    public boolean removeAll(ObjectSet<O> objectSet, QueryOptions queryOptions);

    /**
     * Notifies the listener that all objects have been removed from the collection, and so it can take action
     * and update its internal data structures.
     *
     * @param queryOptions Optional parameters for the update
     */
    public void clear(QueryOptions queryOptions);

    /**
     * Notifies the listener that the given ObjectStore has just been created.
     *
     * @param objectStore The ObjectStore which persists objects
     * @param queryOptions Optional parameters for the update
     */
    public void init(ObjectStore<O> objectStore, QueryOptions queryOptions);

    /**
     * Notifies the listener that it has been removed from the collection, so that it can take action
     * to delete internal data structures.
     * @param queryOptions Optional parameters for the update
     */
    public void destroy(QueryOptions queryOptions);
}
