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
 * Asserts that an attribute has a value (is not null).
 * <p/>
 * To accelerate {@code has(...)} queries, add a Standing Query Index on {@code has(...)}.
 * <p/>
 * To assert that an attribute does <i>not</i> have a value (is null), use <code>not(has(...))</code>.
 * <p/>
 * To accelerate <code>not(has(...))</code> queries, add a Standing Query Index on <code>not(has(...))</code>.
 *
 * @author Niall Gallagher
 */
public class Has<O, A> extends SimpleQuery<O, A> {

    private final Attribute<O, A> attribute;

    public Has(Attribute<O, A> attribute) {
        super(attribute);
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return "has(" + super.getAttribute().getObjectType().getSimpleName() + "." + super.getAttributeName() +
                ")";
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object) {
        return attribute.getValue(object) != null;
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object) {
        for (A attributeValue : attribute.getValues(object)) {
            if (attributeValue != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Has equal = (Has) o;

        if (!attribute.equals(equal.attribute)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        return attribute.hashCode();
    }
}
