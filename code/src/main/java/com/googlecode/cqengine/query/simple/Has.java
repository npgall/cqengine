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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * Asserts that an attribute has a value (is not null).
 *
 * @author Niall Gallagher
 */
public class Has<O, A> extends SimpleQuery<O, A> {

    public Has(Attribute<O, A> attribute) {
        super(attribute);
    }

    @Override
    public String toString() {
        return "has(" + asLiteral(super.getAttributeName()) + ")";
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        return attribute.getValue(object, queryOptions) != null;
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        for (A attributeValue : attribute.getValues(object, queryOptions)) {
            if (attributeValue != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Has)) return false;

        Has equal = (Has) o;

        if (!attribute.equals(equal.attribute)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        return attribute.hashCode();
    }
}
