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
package com.googlecode.cqengine.attribute;

import com.googlecode.cqengine.attribute.impl.AbstractAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an attribute in an object which has multiple values (such as a field which is itself a collection),
 * where some of the values might be null, or where the collection of values itself might be null.
 * <p/>
 * Provides a method to read the values from the field given such an object.
 * <p/>
 * This type of attribute performs runtime validation of values to handle nulls, which can impact performance.
 * When it is known that values will not be null, it is recommended to use {@link MultiValueAttribute} instead.
 *
 * @author Niall Gallagher
 */
public abstract class MultiValueNullableAttribute<O, A> extends AbstractAttribute<O, A> {

    final boolean componentValuesNullable;

    /**
     * Creates an attribute with the given name.
     *
     * This name is not actually used by the query engine except in providing informative exception and debug messages.
     * As such it is recommended, but not required, that a name be provided.
     * <p/>
     * A suitable name might be the name of the field to which an attribute refers.
     *
     * @param attributeName The name for this attribute
     * @param componentValuesNullable Supply true if some of the multiple values in the list returned might be null,
     * supply false if only the collection returned itself might be null
     *
     * @see #MultiValueNullableAttribute(boolean)
     */
    public MultiValueNullableAttribute(String attributeName, boolean componentValuesNullable) {
        super(attributeName);
        this.componentValuesNullable = componentValuesNullable;
    }

    /**
     * Creates an attribute with no name. A name for the attribute will be generated automatically from the name of the
     * subclass (or anonymous class) which implements the attribute.
     *
     * @param componentValuesNullable Supply true if some of the multiple values in the list returned might be null,
     * supply false if only the collection returned itself might be null
     *
     * @see #MultiValueNullableAttribute(String, boolean)
     */
    public MultiValueNullableAttribute(boolean componentValuesNullable) {
        this.componentValuesNullable = componentValuesNullable;
    }

    /**
     * Returns the values of the attribute from the object, omitting any null values.
     * <p/>
     * @param object The object from which the values of the attribute are required
     * @return The values for the attribute
     */
    @Override
    public List<A> getValues(O object) {
        List<A> values = getNullableValues(object);
        // Handle the collection of values itself being null...
        values = (values == null ? Collections.<A>emptyList() : values);
        // Check if we need to check for nulls in the collection of values...
        if (!componentValuesNullable) {
            // No need to check for nulls in the collection of values...
            return values;
        }
        // Check for and skip any nulls in the collection of values...
        List<A> nonNullValues = new ArrayList<A>(values.size());
        for (A value : values) {
            if (value != null) {
                nonNullValues.add(value);
            }
        }
        return nonNullValues;
    }

    /**
     * Returns the values of the attribute from the object, some of which can be null.
     * The actual list returned can also be null.
     * <p/>
     * @param object The object from which the values of the attribute are required
     * @return The values for the attribute, some of which might be null
     */
    public abstract List<A> getNullableValues(O object);
}
