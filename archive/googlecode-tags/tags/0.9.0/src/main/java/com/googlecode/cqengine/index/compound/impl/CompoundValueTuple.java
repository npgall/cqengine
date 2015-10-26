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
package com.googlecode.cqengine.index.compound.impl;

import com.googlecode.cqengine.attribute.Attribute;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    CompoundValueTuple(List<?> attributeValues) {
        this.attributeValues = attributeValues;
        this.hashCode = attributeValues.hashCode();
    }

    public List<Object> getAttributeValues() {
        return Collections.unmodifiableList(attributeValues);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

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
}
