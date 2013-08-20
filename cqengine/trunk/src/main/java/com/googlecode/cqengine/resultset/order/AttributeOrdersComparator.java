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
package com.googlecode.cqengine.resultset.order;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.AttributeOrder;

import java.util.Comparator;
import java.util.List;

/**
 * A comparator which sorts result objects according to a list of attributes each with an associated preference for
 * ascending or descending order.
 *
 * @author Roberto Socrates
 * @author Niall Gallagher
 */
public class AttributeOrdersComparator<O> implements Comparator<O> {

    final List<AttributeOrder<O>> attributeSortOrders;

    public AttributeOrdersComparator(List<AttributeOrder<O>> attributeSortOrders) {
        this.attributeSortOrders = attributeSortOrders;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compare(O o1, O o2) {
        for (AttributeOrder<O> attributeOrder : attributeSortOrders) {
            @SuppressWarnings("unchecked")
            int comparison = compareAttributeValues(attributeOrder.getAttribute(), o1, o2);
            if (comparison != 0) {
                // Found a difference. Invert the sign if order is descending, and return it...
                return attributeOrder.isDescending() ? comparison * -1 : comparison;
            }
            // else continue checking remaining attributes.
        }
        // No differences found according to ordering specified, but in case this comparator
        // will be used for object equality testing, return 0 only if objects really are equal...
        return o1.equals(o2) ? 0 : 1;
    }

    <A extends Comparable<A>> int compareAttributeValues(Attribute<O, A> attribute, O o1, O o2) {
        if (attribute instanceof SimpleAttribute) {
            // Fast code path...
            SimpleAttribute<O, A> simpleAttribute = (SimpleAttribute<O, A>)attribute;
            return simpleAttribute.getValue(o1).compareTo(simpleAttribute.getValue(o2));
        }
        else {
            // Slower code path...
            List<A> o1attributeValues = attribute.getValues(o1);
            List<A> o2attributeValues = attribute.getValues(o2);
            for (int i = 0, size = Math.min(o1attributeValues.size(), o2attributeValues.size()); i < size; i++) {
                A o1attributeValue = o1attributeValues.get(i);
                A o2attributeValue = o2attributeValues.get(i);
                int comparison = o1attributeValue.compareTo(o2attributeValue);
                if (comparison != 0) {
                    // If we found a difference, return it...
                    return comparison;
                }
            }
            // If the number of attribute values differs, return a difference, object with fewest elements first...
            if (o1attributeValues.size() < o2attributeValues.size()) {
                return -1;
            }
            else if (o1attributeValues.size() > o2attributeValues.size()) {
                return +1;
            }
            // No differences found...
            return 0;
        }
    }
}
