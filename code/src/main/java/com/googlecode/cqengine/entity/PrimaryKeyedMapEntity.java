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
 * An extension of {@link MapEntity} in which a particular entry in the map is designated as a primary key,
 * and this primary key will then be used to compute hashCode and test for equality. This can avoid computing
 * equality over all keys in the maps.
 */
public class PrimaryKeyedMapEntity extends MapEntity {

    private final Object primaryKey;

    public PrimaryKeyedMapEntity(Map mapToWrap, Object mapPrimaryKey) {
        super(mapToWrap, getPrimaryKeyHashCode(mapToWrap, mapPrimaryKey));
        primaryKey = mapPrimaryKey;
    }

    static int getPrimaryKeyHashCode(Map map, Object primaryKey) {
        Object value = map.get(primaryKey);
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrimaryKeyedMapEntity)) {
            return false;
        }

        PrimaryKeyedMapEntity that = (PrimaryKeyedMapEntity) o;
        if (cachedHashCode != that.cachedHashCode) {
            return false;
        }

        Object thisPkValue = this.get(primaryKey);
        Object thatPkValue = that.get(primaryKey);
        return thisPkValue.equals(thatPkValue);
    }

    @Override
    public String toString() {
        return "PrimaryKeyedMapEntity{" +
                "primaryKey=" + primaryKey +
                ", cachedHashCode=" + cachedHashCode +
                ", wrappedMap=" + wrappedMap +
                '}';
    }
}
