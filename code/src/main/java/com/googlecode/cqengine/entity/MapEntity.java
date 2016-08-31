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

import com.googlecode.cqengine.query.QueryFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper for Map to allow efficient use in an IndexCollection.
 * MapEntities can be created via {@link QueryFactory#mapEntity(Map)}. Attributes can be created to read the entries
 * in these maps, using {@link QueryFactory#mapAttribute(Object, Class)}.
 * <p/>
 * This works by optimizing the performance of the {@link #hashCode()} and {@link #equals(Object)} methods - which
 * may be called frequently during query processing. The hashCode of the wrapped Map will be cached to improve the
 * performance of repeated invocations of {@link #hashCode()}. The cached hashCode will be used in the
 * implementation of the {@link #equals(Object)} method too, to avoid computing equality entirely when the cached
 * hashCodes are different.
 * <p/>
 * Note it is not safe to modify entries in maps which are indexed, although non-indexed entries may be modified
 * safely. Alternatively, remove and re-add the Map to the collection.
 */
@SuppressWarnings({"unchecked", "NullableProblems"})
public class MapEntity implements Map {

    final Map wrappedMap;
    final int cachedHashCode;

    public MapEntity(Map mapToWrap) {
        this(mapToWrap, mapToWrap.hashCode());
    }

    protected MapEntity(Map mapToWrap, int hashCode) {
        wrappedMap = mapToWrap;
        cachedHashCode = hashCode;
    }

    /**
     * Returns the wrapped map.
     */
    public Map getWrappedMap() {
        return wrappedMap;
    }

    /**
     * Returns the hashcode of the wrapped map which was cached when this MapEntity was created.
     * @return the hashcode of the wrapped map which was cached when this MapEntity was created.
     */
    @Override
    public int hashCode() {
        return cachedHashCode;
    }

    /**
     * Returns true if the cached hashcodes of both objects are equal and the wrapped maps are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapEntity)) {
            return false;
        }

        MapEntity that = (MapEntity) o;
        if (cachedHashCode != that.cachedHashCode) {
            return false;
        }

        return this.wrappedMap.equals(that.wrappedMap);
    }

    @Override
    public String toString() {
        return "MapEntity{" +
                "cachedHashCode=" + cachedHashCode +
                ", wrappedMap=" + wrappedMap +
                '}';
    }

    public Object get(Object key) {
        return wrappedMap.get(key);
    }

    @Override
    public Object put(Object o, Object o2) {
        return wrappedMap.put(o, o2);
    }

    @Override
    public Object remove(Object o) {
        return wrappedMap.remove(o);
    }

    @Override
    public void putAll(Map map) {
        wrappedMap.putAll(map);
    }

    @Override
    public void clear() {
        wrappedMap.clear();
    }

    @Override
    public Set keySet() {
        return wrappedMap.keySet();
    }

    @Override
    public Collection values() {
        return wrappedMap.values();
    }

    @Override
    public Set<Entry> entrySet() {
        return wrappedMap.entrySet();
    }

    @Override
    public int size() {
        return wrappedMap.size();
    }

    @Override
    public boolean isEmpty() {
        return wrappedMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return wrappedMap.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return wrappedMap.containsValue(o);
    }
}
