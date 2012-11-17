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

import java.util.Comparator;
import java.util.List;

/**
 * An optimized comparator similar to {@link AttributeListComparator}, but efficiency-optimized for the case that all
 * attributes are {@link SimpleAttribute}s.
 *
 * @author Niall Gallagher
 */
public class SimpleAttributeListComparator<O> implements Comparator<O> {

    private final List<Attribute<O, ? extends Comparable>> attributes;

    public SimpleAttributeListComparator(List<Attribute<O, ? extends Comparable>> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int compare(O o1, O o2) {
        for (Attribute<O, ? extends Comparable> attribute : attributes) {
            SimpleAttribute<O, ? extends Comparable> attributeTyped = (SimpleAttribute<O, ? extends Comparable>) attribute;
            int comparison = compareAttributeValues(attributeTyped, o1, o2);
            if (comparison != 0) {
                // If we found a difference, return it...
                return comparison;
            }
            // else continue checking remaining attributes.
        }
        // No differences found...
        return 0;
    }

    <A extends Comparable<A>> int compareAttributeValues(SimpleAttribute<O, A> attribute, O o1, O o2) {
        A o1attributeValue = attribute.getValue(o1);
        A o2attributeValue = attribute.getValue(o2);
        return o1attributeValue.compareTo(o2attributeValue);
    }
}
