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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

/**
 * Asserts than an attribute is less than an upper bound.
 *
 * @author Niall Gallagher
 */
public class LessThan<O, A extends Comparable<A>> extends SimpleQuery<O, A> {

    private final Attribute<O, A> attribute;
    private final A value;
    private final boolean valueInclusive;

    public LessThan(Attribute<O, A> attribute, A value, boolean valueInclusive) {
        super(attribute);
        this.attribute = attribute;
        this.value = value;
        this.valueInclusive = valueInclusive;
    }

    public A getValue() {
        return value;
    }

    public boolean isValueInclusive() {
        return valueInclusive;
    }

    @Override
    public String toString() {
        if (valueInclusive) {
            return "lessThanOrEqualTo(" + super.getAttribute().getObjectType().getSimpleName() + "." + super.getAttributeName() +
                    ", " + value +
                    ")";
        }
        else {
            return "lessThan(" + super.getAttribute().getObjectType().getSimpleName() + "." + super.getAttributeName() +
                    ", " + value +
                    ")";
        }
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object) {
        A attributeValue = attribute.getValue(object);
        if (valueInclusive) {
            return value.compareTo(attributeValue) >= 0;
        }
        else {
            return value.compareTo(attributeValue) > 0;
        }
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object) {
        Iterable<A> attributeValues = attribute.getValues(object);
        if (valueInclusive) {
            for (A attributeValue : attributeValues) {
                if (value.compareTo(attributeValue) >= 0) {
                    return true;
                }
            }
        }
        else {
            for (A attributeValue : attributeValues) {
                if (value.compareTo(attributeValue) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessThan lessThan = (LessThan) o;

        if (!attribute.equals(lessThan.attribute)) return false;
        if (valueInclusive != lessThan.valueInclusive) return false;
        if (!value.equals(lessThan.value)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + (valueInclusive ? 1 : 0);
        return result;
    }

}
