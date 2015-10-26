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
package com.googlecode.cqengine.query.option;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.OrderControlAttribute;
import com.googlecode.cqengine.attribute.OrderMissingFirstAttribute;
import com.googlecode.cqengine.attribute.OrderMissingLastAttribute;

/**
 * Represents an attribute and an associated preference for sorting results according to that attribute
 * in ascending or descending order.
 *
 * @author Roberto Socrates
 * @author Niall Gallagher
 */
public class AttributeOrder<O> {
	
    private final Attribute<O, ? extends Comparable> attribute;
    private final boolean descending;

    public AttributeOrder(Attribute<O, ? extends Comparable> attribute, boolean descending) {
		
		this.attribute = attribute;
		this.descending = descending;
	}

    public Attribute<O, ? extends Comparable> getAttribute() {
        return attribute;
    }

    public boolean isDescending() {
        return descending;
    }

    @Override
    public String toString() {
        if (attribute instanceof OrderMissingLastAttribute) {
            OrderControlAttribute orderControlAttribute = (OrderControlAttribute) attribute;
            @SuppressWarnings("unchecked")
            Attribute<O, ? extends Comparable> delegateAttribute = orderControlAttribute.getDelegateAttribute();
            return descending
                    ? "descending(missingLast(" + delegateAttribute.getObjectType().getSimpleName() + "." + delegateAttribute.getAttributeName() + "))"
                    : "ascending(missingLast(" + delegateAttribute.getObjectType().getSimpleName() + "." + delegateAttribute.getAttributeName() + "))";
        }
        if (attribute instanceof OrderMissingFirstAttribute) {
            OrderControlAttribute orderControlAttribute = (OrderControlAttribute) attribute;
            @SuppressWarnings("unchecked")
            Attribute<O, ? extends Comparable> delegateAttribute = orderControlAttribute.getDelegateAttribute();
            return descending
                    ? "descending(missingFirst(" + delegateAttribute.getObjectType().getSimpleName() + "." + delegateAttribute.getAttributeName() + "))"
                    : "ascending(missingFirst(" + delegateAttribute.getObjectType().getSimpleName() + "." + delegateAttribute.getAttributeName() + "))";
        }
        else {
            return descending
                    ? "descending(" + attribute.getObjectType().getSimpleName() + "." + attribute.getAttributeName() + ")"
                    : "ascending(" + attribute.getObjectType().getSimpleName() + "." + attribute.getAttributeName() + ")";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttributeOrder)) return false;

        AttributeOrder that = (AttributeOrder) o;

        if (descending != that.descending) return false;
        if (!attribute.equals(that.attribute)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attribute.hashCode();
        result = 31 * result + (descending ? 1 : 0);
        return result;
    }
}
