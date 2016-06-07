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
 * Wrapper for Map to allow efficient use in an IndexCollection.
 * NOTE it is not safe to modify objects which have already been added to the collection,
 * because doing so can change their hash codes (which corrupts HashMaps
 * and HashSets etc.).  Alternatively, remove/re-add the Map to the collection.
 */
public class MapEntity {

    private final Map wrappedMap;
    int cachedHashCode;

    public MapEntity(Map mapToWrap)
    {
        wrappedMap = mapToWrap;
        cachedHashCode = wrappedMap.hashCode();
    }

    public Object get(Object key)
    {
        return wrappedMap.get(key);
    }

    /**
     * Returns the wrapped map - fine for cases with small result sets.
     * For large result sets, perhaps we should implement the Map interface, delegating to the wrapped Map.
     */
    public Map getMap()
    {
        return wrappedMap;
    }

    @Override
    public int hashCode() {
        return cachedHashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapEntity)) return false;

        MapEntity that = (MapEntity) o;
        if (cachedHashCode != that.cachedHashCode) return false;

        return this.wrappedMap.equals(that.wrappedMap);
    }
}
