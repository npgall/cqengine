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
package com.googlecode.cqengine.engine;

import java.util.Collection;
import java.util.Set;

/**
 * @author Niall Gallagher
 */
public interface ModificationListener<O> {

    /**
     * Notifies the listener that the specified objects are being added to the collection, and so it can take action
     * and update its internal data structures.
     *
     * @param objects The objects being added
     */
    public void notifyObjectsAdded(Collection<O> objects);

    /**
     * Notifies the listener that the specified objects are being removed from the collection, and so it can take action
     * and update its internal data structures.
     *
     * @param objects The objects being removed
     */
    public void notifyObjectsRemoved(Collection<O> objects);

    /**
     * Notifies the listener that all objects have been removed from the collection, and so it can take action
     * and update its internal data structures.
     */
    public void notifyObjectsCleared();

    /**
     * Notifies the listener that the given collection has just been created.
     *
     * @param collection The entire collection
     */
    public void init(Set<O> collection);
}
