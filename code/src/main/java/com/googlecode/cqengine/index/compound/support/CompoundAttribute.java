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
package com.googlecode.cqengine.index.compound.support;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.*;

/**
 * A private implementation of {@link Attribute} used internally by
 * {@link com.googlecode.cqengine.index.compound.CompoundIndex}, which groups several other attributes.
 * <p/>
 * Note that, being like regular {@link com.googlecode.cqengine.attribute.Attribute}s, objects of this type
 * do not represent values but rather the means to obtain values from fields in an object. <i>Values</i> for compound
 * attributes are encapsulated separately in {@link CompoundValueTuple} objects. The {@link Attribute#getValues(Object, com.googlecode.cqengine.query.option.QueryOptions)} method
 * returns a list of these tuples for a given object.
 * <p/>
 * <b><u>Algorithm to generate tuples</u></b><br/>
 * The list of tuples generated for a {@code CompoundAttribute} referencing a given object, depends on whether the
 * {@code CompoundAttribute} groups only {@link com.googlecode.cqengine.attribute.SimpleAttribute}s, or whether it
 * groups any {@link com.googlecode.cqengine.attribute.MultiValueAttribute}s.
 * <ul>
 *     <li>
 *         <b>Generating tuples for a group of {@code SimpleAttribute}s</b><br/>
 *         If a compound attribute groups only {@link com.googlecode.cqengine.attribute.SimpleAttribute}s, then the list
 *         of tuples generated by the {@code CompoundAttribute} is straightforward and will contain just one tuple,
 *         comprising the values from each of the fields referenced by the {@code SimpleAttribute}s
 *     </li>
 *     <li>
 *         <b>Generating tuples for {@code MultiValueAttribute}s</b><br/>
 *         If a compound attribute groups one or more {@link com.googlecode.cqengine.attribute.MultiValueAttribute}s,
 *         then the determination of tuples is more complicated, and is as follows.
 *         <p/>
 *         {@code MultiValueAttribute}s can return multiple values from a single field in an object. The purpose of
 *         {@code MultiValueAttribute} is to allow an object to be indexed separately against <i>each</i> of the values
 *         from this field. Given that a {@code CompoundAttribute} spans multiple attributes, the presence of one or
 *         more {@code MultiValueAttribute}s means that <u>several possible combinations of values</u> should match the
 *         object in the index.
 *         <p/>
 *         To generate tuples for {@code CompoundAttribute}s spanning {@code MultiValueAttribute}s, the
 *         {@link Attribute#getValues(Object, com.googlecode.cqengine.query.option.QueryOptions)} method will retrieve a list of values from the object for each of the component
 *         attributes. It will then generate all possible combinations of values between these lists as tuples, using
 *         {@link TupleCombinationGenerator#generateCombinations(java.util.List)}.
 *         <p/>
 *         <u>Example:</u><br/>
 *         If we have the following lists of values from attributes:<br/>
 *         Values from first attribute:  <code>1</code><br/>
 *         Values from second attribute: <code>"bar", "baz"</code><br/>
 *         Values from third attribute:  <code>2.0, 3.0, 4.0</code><br/>
 *         The following tuples will be generated:<br/>
 *         <code>[[1, bar, 2.0], [1, bar, 3.0], [1, bar, 4.0], [1, baz, 2.0], [1, baz, 3.0], [1, baz, 4.0]]</code><br/>
 *         The {@link Attribute#getValues(Object, com.googlecode.cqengine.query.option.QueryOptions)} method would then return these tuples as a list of {@link CompoundValueTuple}
 *         objects
 *     </li>
 * </ul>
 *
 * @author Niall Gallagher
 */
public class CompoundAttribute<O> implements Attribute<O, CompoundValueTuple<O>> {

    private final List<Attribute<O, ?>> attributes;

    public CompoundAttribute(List<Attribute<O, ?>> attributes) {
        if (attributes.size() < 2) {
            throw new IllegalStateException("Cannot create a compound index on fewer than two attributes: " + attributes.size());
        }
        this.attributes = attributes;
    }

    public int size() {
        return attributes.size();
    }

    @Override
    public Class<O> getObjectType() {
        throw new UnsupportedOperationException("Method not supported by CompoundAttribute");
    }

    @Override
    public Class<CompoundValueTuple<O>> getAttributeType() {
        throw new UnsupportedOperationException("Method not supported by CompoundAttribute");
    }

    @Override
    public String getAttributeName() {
        throw new UnsupportedOperationException("Method not supported by CompoundAttribute");
    }


    /**
     * Returns a list of {@link CompoundValueTuple} objects for the given object, containing all possible combinations
     * of attribute values against which the object can be indexed.
     * <p/>
     * See documentation on this class itself for details of the algorithm used to generate these tuples.
     *
     * @param object The object from which all {@link com.googlecode.cqengine.index.compound.support.CompoundValueTuple}s are required
     * @param queryOptions
     * @return tuples representing all possible combinations of attribute values against which the object can be indexed
     */
    @Override
    public Iterable<CompoundValueTuple<O>> getValues(O object, QueryOptions queryOptions) {
        // STEP 1.
        // For each individual attribute comprising the compound attribute,
        // ask the attribute to return a list of values for the field it references in the object.
        // We get a list of values for each field because, because some fields can have multiple values,
        // as supported by MultiValueAttribute. We end up with List<List<Object>>, the outer list containing
        // individual lists of values from each attribute, inner lists containing values...
        List<Iterable<Object>> attributeValueLists = new ArrayList<Iterable<Object>>(attributes.size());
        for (Attribute<O, ?> attribute : attributes) {
            @SuppressWarnings({"unchecked"})
            Iterable<Object> values = (Iterable<Object>) attribute.getValues(object, queryOptions);
            attributeValueLists.add(values);
        }
        // STEP 2.
        // Generate all combinations of attribute values across these attributes.
        // For example, if we have the following lists of values from attributes:
        // Values for first attribute:  1
        // Values for second attribute: "bar", "baz"
        // Values for third attribute:  2.0, 3.0, 4.0
        //
        // or in list form :
        // [[1], [bar, baz], [2.0, 3.0, 4.0]]
        //
        // ...then we should generate and index the object against the following tuples:
        // [[1, bar, 2.0], [1, bar, 3.0], [1, bar, 4.0], [1, baz, 2.0], [1, baz, 3.0], [1, baz, 4.0]]
        // Note that ordering is preserved, we take advantage of this below
        List<List<Object>> listsOfValueCombinations = TupleCombinationGenerator.generateCombinations(attributeValueLists);

        // STEP 3.
        // Wrap each of the unique combinations in a CompoundValueTuple object...
        List<CompoundValueTuple<O>> tuples = new ArrayList<CompoundValueTuple<O>>(listsOfValueCombinations.size());
        for (List<Object> valueCombination : listsOfValueCombinations) {
            Map<Attribute<O, ?>, Object> mappedTuples = new HashMap<Attribute<O, ?>, Object>();
            // here we take advantage of the fact that ordering is preserved when the tuples are generated
            for (int i = 0; i < attributes.size(); i++) {
                mappedTuples.put(attributes.get(i), valueCombination.get(i));
            }
            tuples.add(new CompoundValueTuple<O>(mappedTuples));
        }
        // Return the list of CompoundValueTuple objects...
        return tuples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompoundAttribute)) return false;

        CompoundAttribute that = (CompoundAttribute) o;

        if (!attributes.equals(that.attributes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return attributes.hashCode();
    }

    @Override
    public String toString() {
        return "CompoundAttribute{" +
                "attributes=" + attributes +
                '}';
    }
}
