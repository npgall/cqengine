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
package com.googlecode.cqengine.collection.impl;

import com.googlecode.cqengine.index.common.Factory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Creates a concurrent implementation of a {@link java.util.Set}, based on {@link ConcurrentHashMap}.
 *
 * @author Niall Gallagher
 */
public class DefaultConcurrentSetFactory<O> implements Factory<Set<O>> {

    final int initialSize;

    public DefaultConcurrentSetFactory() {
        this.initialSize = 16;
    }

    public DefaultConcurrentSetFactory(int initialSize) {
        this.initialSize = initialSize;
    }

    @Override
    public Set<O> create() {
        return Collections.newSetFromMap(new ConcurrentHashMap<O, Boolean>(initialSize));
    }
}
