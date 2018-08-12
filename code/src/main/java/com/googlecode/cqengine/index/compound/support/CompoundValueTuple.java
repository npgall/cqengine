/**
 * Copyright 2012-2015 Niall Gallagher
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.index.compound.support;

import com.googlecode.cqengine.attribute.Attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A tuple (ordered list) of values, extracted from the fields of an object, according to, and in the same order as, the
 * {@link Attribute}s encapsulated in a {@link CompoundAttribute}.
 * <p/>
 * The combination of values encapsulated in objects of this type are used as keys in compound indexes. This object
 * implements {@link Object#equals(Object)} and {@link Object#hashCode()} where the hash code is calculated from the
 * combination of values.
 *
 * @author Niall Gallagher
 */
public class CompoundValueTuple<O> {

    private final List<?> attributeValues;
    private final int hashCode;

    public CompoundValueTuple(Map<Attribute<O, ?>, ?> attributeToValues) {
        this.attributeValues = getOrderedAttributeValues(attributeToValues);
        this.hashCode = attributeValues.hashCode();
    }

    public Iterable<Object> getAttributeValues() {
        return Collections.unmodifiableList(attributeValues);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompoundValueTuple)) return false;

        CompoundValueTuple that = (CompoundValueTuple) o;

        if (hashCode != that.hashCode) return false;
        if (!attributeValues.equals(that.attributeValues)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "CompoundValueTuple{" +
                "attributeValues=" + attributeValues +
                ", hashCode=" + hashCode +
                '}';
    }

    /**
     * Given attribute values, sort them to ensure that compound index entries in the index engine
     * work regardless of how the given CompoundQuery is ordered
     * <p>
     * Note: to be more specific, without this, the {@code equals} method will return false when
     * two CompoundValueTuples contain the same values but are ordered differently
     *
     * @param attributeToValues a map of attributes and their corresponding values that were extracted from an object or query
     * @return a list of ordered attribute values that will be used in this CompoundValueTuple
     */
    private static <O> List<?> getOrderedAttributeValues(Map<Attribute<O, ?>, ?> attributeToValues) {
        final TreeMap<Attribute<O, ?>, Object> attributeValuesSortedByAttributeName = new TreeMap<Attribute<O, ?>, Object>(new Comparator<Attribute<O, ?>>() {
            @Override
            public int compare(Attribute<O, ?> o1, Attribute<O, ?> o2) {
                return o1.getAttributeName().compareTo(o2.getAttributeName());
            }
        });
        attributeValuesSortedByAttributeName.putAll(attributeToValues);
        return new ArrayList<Object>(attributeValuesSortedByAttributeName.values());
    }
}
