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
 * @author Niall Gallagher
 */
public class AttributeListComparators {

    public static <O> Comparator<O> ascendingComparator(List<Attribute<O, ? extends Comparable>> attributes) {
        if (allAttributesAreSimple(attributes)) {
            // Return a Comparator optimized for the case that all attributes are SimpleAttributes...
            return new SimpleAttributeListComparator<O>(attributes);
        }
        else {
            // Return a generic Comparator that works with all attributes (e.g. MultiValueAttributes)...
            return new AttributeListComparator<O>(attributes);
        }
    }

    public static <O> Comparator<O> descendingComparator(List<Attribute<O, ? extends Comparable>> attributes) {
        if (allAttributesAreSimple(attributes)) {
            // Return a Comparator optimized for the case that all attributes are SimpleAttributes.
            return new SimpleAttributeListComparator<O>(attributes) {
                @Override
                public int compare(O o1, O o2) {
                    int result = super.compare(o1, o2);
                    // Reverse the sort order from the superclass...
                    return result < 0 ? +1 : result > 0 ? -1 : 0;
                }
            };
        }
        else {
            // Return a generic Comparator that works with all attributes (e.g. MultiValueAttributes)...
            return new AttributeListComparator<O>(attributes) {
                @Override
                public int compare(O o1, O o2) {
                    int result = super.compare(o1, o2);
                    // Reverse the sort order from the superclass...
                    return result < 0 ? +1 : result > 0 ? -1 : 0;
                }
            };
        }
    }

    static <O> boolean allAttributesAreSimple(List<Attribute<O, ? extends Comparable>> attributes) {
        for (Attribute<?, ?> attribute : attributes) {
            if (!(attribute instanceof SimpleAttribute)) {
                return false;
            }
        }
        return true;
    }
}
