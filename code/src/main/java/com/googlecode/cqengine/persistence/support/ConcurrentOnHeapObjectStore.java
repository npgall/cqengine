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
package com.googlecode.cqengine.persistence.support;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An {@link ObjectStore} which wraps a concurrent implementation of {@link java.util.Set}.
 *
 * @author niall.gallagher
 */
public class ConcurrentOnHeapObjectStore<O> extends CollectionWrappingObjectStore<O> {

    public ConcurrentOnHeapObjectStore() {
        super(Collections.newSetFromMap(new ConcurrentHashMap<O, Boolean>()));
    }

    public ConcurrentOnHeapObjectStore(int initialCapacity, float loadFactor, int concurrencyLevel) {
        super(Collections.newSetFromMap(new ConcurrentHashMap<O, Boolean>(initialCapacity, loadFactor, concurrencyLevel)));
    }

}
