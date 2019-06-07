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
 * Asserts than a {@link CharSequence}-based attribute ends with a given {@link CharSequence}-based suffix.

 * @author Niall Gallagher
 */
public class StringContains<O, A extends CharSequence> extends SimpleQuery<O, A> {

    private final A value;

    /**
     * Creates a new {@link SimpleQuery} initialized to make assertions on values of the specified attribute
     *
     * @param attribute The attribute on which the assertion is to be made
     */
    public StringContains(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.value = checkQueryValueNotNull(value);
    }

    public A getValue() {
        return value;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        CharSequence attributeValue = attribute.getValue(object, queryOptions);
        return containsFragment(attributeValue, value);
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        for (A attributeValue : attribute.getValues(object, queryOptions)) {
            if (containsFragment(attributeValue, value)) {
                return true;
            }
        }
        return false;
    }

    static boolean containsFragment(CharSequence document, CharSequence fragment) {
        final int documentLength = document.length();
        final int fragmentLength = fragment.length();
        final int lastStartOffset = documentLength - fragmentLength;
        for (int startOffset = 0; startOffset <= lastStartOffset; startOffset++) {
            int charsMatched = 0;
            for (int endOffset = startOffset, j = 0; j < fragmentLength; j++, endOffset++) {
                char documentChar = document.charAt(endOffset);
                char fragmentChar = fragment.charAt(j);
                if (documentChar != fragmentChar) {
                    break; // break inner loop
                }
                charsMatched++;
            }
            if (charsMatched == fragmentLength) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringContains)) return false;

        StringContains that = (StringContains) o;

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
        return "contains(" + asLiteral(super.getAttributeName()) +
                ", " + asLiteral(value) +
                ")";
    }
}
