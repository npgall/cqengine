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
 * Asserts than an attribute is between a lower and an upper bound.

 * @author Niall Gallagher
 */
public class Between<O, A extends Comparable<A>> extends SimpleQuery<O, A> {

    private final A lowerValue;
    private final boolean lowerInclusive;
    private final A upperValue;
    private final boolean upperInclusive;

    public Between(Attribute<O, A> attribute, A lowerValue, boolean lowerInclusive, A upperValue, boolean upperInclusive) {
        super(attribute);
        this.lowerValue = lowerValue;
        this.lowerInclusive = lowerInclusive;
        this.upperValue = upperValue;
        this.upperInclusive = upperInclusive;
    }

    public A getLowerValue() {
        return lowerValue;
    }

    public boolean isLowerInclusive() {
        return lowerInclusive;
    }

    public A getUpperValue() {
        return upperValue;
    }

    public boolean isUpperInclusive() {
        return upperInclusive;
    }

    @Override
    public String toString() {
        if (lowerInclusive && upperInclusive) {
            return "between(" + asLiteral(super.getAttributeName()) +
                    ", " + asLiteral(lowerValue) +
                    ", " + asLiteral(upperValue) +
                    ")";
        }
        else {
            return "between(" + asLiteral(super.getAttributeName()) +
                    ", " + asLiteral(lowerValue) +
                    ", " + lowerInclusive +
                    ", " + asLiteral(upperValue) +
                    ", " + upperInclusive +
                    ")";
        }

    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        A attributeValue = attribute.getValue(object, queryOptions);
        if (lowerInclusive && upperInclusive) {
            if (lowerValue.compareTo(attributeValue) <= 0 && upperValue.compareTo(attributeValue) >= 0) {
                return true;
            }
        }
        else if (lowerInclusive) {
            if (lowerValue.compareTo(attributeValue) <= 0 && upperValue.compareTo(attributeValue) > 0) {
                return true;
            }
        }
        else if (upperInclusive) {
            if (lowerValue.compareTo(attributeValue) < 0 && upperValue.compareTo(attributeValue) >= 0) {
                return true;
            }
        }
        else {
            if (lowerValue.compareTo(attributeValue) < 0 && upperValue.compareTo(attributeValue) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        Iterable<A> attributeValues = attribute.getValues(object, queryOptions);
        if (lowerInclusive && upperInclusive) {
            for (A attributeValue : attributeValues) {
                if (lowerValue.compareTo(attributeValue) <= 0 && upperValue.compareTo(attributeValue) >= 0) {
                    return true;
                }
            }
        }
        else if (lowerInclusive) {
            for (A attributeValue : attributeValues) {
                if (lowerValue.compareTo(attributeValue) <= 0 && upperValue.compareTo(attributeValue) > 0) {
                    return true;
                }
            }
        }
        else if (upperInclusive) {
            for (A attributeValue : attributeValues) {
                if (lowerValue.compareTo(attributeValue) < 0 && upperValue.compareTo(attributeValue) >= 0) {
                    return true;
                }
            }
        }
        else {
            for (A attributeValue : attributeValues) {
                if (lowerValue.compareTo(attributeValue) < 0 && upperValue.compareTo(attributeValue) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Between)) return false;

        Between between = (Between) o;

        if (!attribute.equals(between.attribute)) return false;
        if (lowerInclusive != between.lowerInclusive) return false;
        if (upperInclusive != between.upperInclusive) return false;
        if (!lowerValue.equals(between.lowerValue)) return false;
        if (!upperValue.equals(between.upperValue)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + lowerValue.hashCode();
        result = 31 * result + (lowerInclusive ? 1 : 0);
        result = 31 * result + upperValue.hashCode();
        result = 31 * result + (upperInclusive ? 1 : 0);
        return result;
    }
}
