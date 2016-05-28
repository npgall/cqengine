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
 * Wrapper for Map to allow efficient use in an IndexCollection
 */
public class MapEntity {

    private final Map wrappedMap;
    private final int cachedHashCode;

    public MapEntity(Map mapToWrap)
    {
        wrappedMap = mapToWrap;
        cachedHashCode = wrappedMap.hashCode();
    }

    public Object get(Object key)
    {
        return wrappedMap.get(key);
    }

    // TODO how to access wrappedMap
    // - make MapEntity implement Map and passthru calls, seems like best option for users
    // - method to get the wrapped Map, but then makes usage more problematic
    // - could extend Map but that seems wrong way to go

    @Override
    public int hashCode() {
//        return super.hashCode();
        return cachedHashCode;
    }

    @Override
    public boolean equals(Object o) {
//        return super.equals(o);
        if (this == o) return true;
        if (!(o instanceof MapEntity)) return false;

        MapEntity that = (MapEntity) o;

        return this.wrappedMap.equals(that.wrappedMap); // TODO will be slow
    }
}
