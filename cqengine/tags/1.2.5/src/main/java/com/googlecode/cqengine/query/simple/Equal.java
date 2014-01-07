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
 * Asserts than an attribute equals a certain value.
 *
 * @author Niall Gallagher
 */
public class Equal<O, A> extends SimpleQuery<O, A> {

    private final Attribute<O, A> attribute;
    private final A value;

    public Equal(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.attribute = attribute;
        this.value = value;
    }

    public A getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "equal(" + super.getAttribute().getObjectType().getSimpleName() + "." + super.getAttributeName() +
                ", " + value +
                ")";
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object) {
        return value.equals(attribute.getValue(object));
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object) {
        for (A attributeValue : attribute.getValues(object)) {
            if (value.equals(attributeValue)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Equal equal = (Equal) o;

        if (!attribute.equals(equal.attribute)) return false;
        if (!value.equals(equal.value)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
