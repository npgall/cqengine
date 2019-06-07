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

import static com.googlecode.cqengine.query.support.QueryValidation.checkQueryValueNotNull;

/**
 * Asserts than an attribute equals a certain value.
 *
 * @author Niall Gallagher
 */
public class Equal<O, A> extends SimpleQuery<O, A> {

    private final A value;

    public Equal(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.value = checkQueryValueNotNull(value);
    }

    public A getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "equal("+ asLiteral(super.getAttributeName()) +
                ", " + asLiteral(value) +
                ")";
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        return value.equals(attribute.getValue(object, queryOptions));
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        for (A attributeValue : attribute.getValues(object, queryOptions)) {
            if (value.equals(attributeValue)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equal)) return false;

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
