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
package com.googlecode.cqengine.attribute;

import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Map;

/**
 * An attribute which reads the value from an entry in a map given the map key. This can be used when Map objects
 * are stored in the IndexedCollection.
 * <p/>
 * These attributes can be created via {@link QueryFactory#mapAttribute(Object, Class)}.
 * <p/>
 * Also see {@link QueryFactory#mapEntity(Map)} as a way to improve the performance when working with collections
 * of Maps.
 *
 * Created by npgall on 23/05/2016.
 */
public class SimpleNullableMapAttribute<K, A> extends SimpleNullableAttribute<Map, A> {

    final K mapKey;

    public SimpleNullableMapAttribute(K mapKey, Class<A> mapValueType) {
        super(Map.class, mapValueType, mapKey.toString());
        this.mapKey = mapKey;
    }

    public SimpleNullableMapAttribute(K mapKey, Class<A> mapValueType, String attributeName) {
        super(Map.class, mapValueType, attributeName);
        this.mapKey = mapKey;
    }

    @Override
    public A getValue(Map map, QueryOptions queryOptions) {
        Object result = map.get(mapKey);
        if (result == null || getAttributeType().isAssignableFrom(result.getClass())) {
            return getAttributeType().cast(result);
        }
        throw new ClassCastException("Cannot cast " + result.getClass().getName() + " to " + getAttributeType().getName() + " for map key: " + mapKey);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mapKey.hashCode();
        return result;
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof SimpleNullableMapAttribute;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && this.mapKey.equals(((SimpleNullableMapAttribute)other).mapKey);
    }
}
