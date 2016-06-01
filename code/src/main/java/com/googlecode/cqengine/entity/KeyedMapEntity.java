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
package com.googlecode.cqengine.entity;

import java.util.Map;

/**
 * Wrapper for Map and its key to allow efficient use in an IndexCollection.
 * Note changing the key is not supported, but other Map fields can be changed as
 * long as they are not indexed. Alternatively, remove/re-add Map to collection.
 */
public class KeyedMapEntity extends MapEntity {

    private final Object primaryKey;

    public KeyedMapEntity(Map mapToWrap, Object mapPrimaryKey)
    {
        super(mapToWrap);
        primaryKey = mapPrimaryKey;
        cachedHashCode = get(primaryKey).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyedMapEntity)) return false;

        KeyedMapEntity that = (KeyedMapEntity) o;
        if (cachedHashCode != that.cachedHashCode) return false;

        return this.get(primaryKey).equals(that.get(primaryKey));
    }
}
