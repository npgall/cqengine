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
package com.googlecode.cqengine.persistence;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.index.sqlite.ConnectionManager;

import java.util.Set;

/**
 * An interface with multiple implementations, which provide details on how a collection or indexes should be persisted
 * off-heap (for example to off-heap memory, or to disk).
 *
 * @author niall.gallagher
 */
public interface Persistence<O, A extends Comparable<A>> extends ConnectionManager, Factory<Set<O>> {

    SimpleAttribute<O, A> getPrimaryKeyAttribute();

    /**
     * @return The number of bytes used to persist the collection and/or indexes
     */
    long getBytesUsed();

    /**
     * Compacts the underlying persistence, which returns unused memory or disk space to the operating system.
     */
    void compact();
}
