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
 * Asserts than a {@link CharSequence}-based attribute ends with a given {@link CharSequence}-based suffix.

 * @author Niall Gallagher
 */
public class StringEndsWith<O, A extends CharSequence> extends SimpleQuery<O, A> {

    private final Attribute<O, A> attribute;
    private final A value;

    /**
     * Creates a new {@link SimpleQuery} initialized to make assertions on values of the specified attribute
     *
     * @param attribute The attribute on which the assertion is to be made
     */
    public StringEndsWith(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.attribute = attribute;
        this.value = value;
    }

    public A getValue() {
        return value;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object) {
        CharSequence attributeValue = attribute.getValue(object);
        return endsWithSuffix(attributeValue, value);
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object) {
        for (A attributeValue : attribute.getValues(object)) {
            if (endsWithSuffix(attributeValue, value)) {
                return true;
            }
        }
        return false;
    }

    static boolean endsWithSuffix(CharSequence document, CharSequence suffix) {
        int charsMatched = 0;
        for (int i = document.length() - 1, j = suffix.length() - 1; i >= 0 && j >= 0; i--, j--) {
            char documentChar = document.charAt(i);
            char suffixChar = suffix.charAt(j);
            if (documentChar != suffixChar) {
                break;
            }
            charsMatched++;
        }
        return charsMatched == suffix.length();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringEndsWith that = (StringEndsWith) o;

        if (!attribute.equals(that.attribute)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "endsWith(" + super.getAttribute().getObjectType().getSimpleName() + "." + super.getAttributeName() +
                ", " + value +
                ")";
    }
}
