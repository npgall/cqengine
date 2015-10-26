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

import java.util.Comparator;
import java.util.List;

/**
 * A {@link java.util.Comparator} which can sort a collection of objects based on a list of {@link Attribute}s referring
 * to comparable fields within those objects.
 * <p/>
 * Since {@link Attribute}s can return more than one value for each referenced field, this comparator iterates
 * all values returned by each attribute for both objects being compared, attempting to sort by these individual values.
 * If both objects return a different number of values for the same attribute, sorts the object with the fewer values
 * first.
 *
 * @author Niall Gallagher
 */
public class AttributeListComparator<O> implements Comparator<O> {

    private final List<Attribute<O, ? extends Comparable>> attributes;

    public AttributeListComparator(List<Attribute<O, ? extends Comparable>> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int compare(O o1, O o2) {
        for (Attribute<O, ? extends Comparable> attribute : attributes) {
            int comparison = compareAttributeValues(attribute, o1, o2);
            if (comparison != 0) {
                // Found a difference, return it...
                return comparison;
            }
            // else continue checking remaining attributes.
        }
        // No differences found...
        return 0;
    }

    <A extends Comparable<A>> int compareAttributeValues(Attribute<O, A> attribute, O o1, O o2) {
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
