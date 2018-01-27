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

import java.util.regex.Pattern;

/**
 * Asserts that an attribute's value matches a regular expression.
 * <p/>
 * To accelerate {@code matchesRegex(...)} queries, add a Standing Query Index on {@code matchesRegex(...)}.
 *
 * @author Niall Gallagher, Silvano Riz
 */
public class StringMatchesRegex<O, A extends CharSequence> extends SimpleQuery<O, A> {

    private final Pattern regexPattern;

    /**
     * Creates a new {@link StringMatchesRegex} initialized to make assertions on whether values of the specified
     * attribute match the given regular expression pattern.
     *
     * @param attribute The attribute on which the assertion is to be made
     * @param regexPattern The regular expression pattern with which values of the attribute should be tested
     */
    public StringMatchesRegex(Attribute<O, A> attribute, Pattern regexPattern) {
        super(attribute);
        this.regexPattern = regexPattern;
    }


    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        A attributeValue = attribute.getValue(object, queryOptions);
        return matchesValue(attributeValue, queryOptions);
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        for (A attributeValue : attribute.getValues(object, queryOptions)) {
            if (matchesValue(attributeValue, queryOptions)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public boolean matchesValue(A aValue, QueryOptions queryOptions){
        return regexPattern.matcher(aValue).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringMatchesRegex)) return false;
        StringMatchesRegex that = (StringMatchesRegex) o;
        return this.attribute.equals(that.attribute)
                && this.regexPattern.pattern().equals(that.regexPattern.pattern())
                && this.regexPattern.flags() == that.regexPattern.flags();
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + regexPattern.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "matchesRegex(" + asLiteral(super.getAttributeName()) +
                ", " + asLiteral(regexPattern.pattern()) + ")";
    }
}