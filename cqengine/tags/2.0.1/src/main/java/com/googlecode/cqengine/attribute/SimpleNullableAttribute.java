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
package com.googlecode.cqengine.attribute;

import com.googlecode.cqengine.attribute.support.AbstractAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.*;

/**
 * Represents an attribute in an object (such as a field) which can be null.
 *
 * Provides a method to read the value of the field given such an object.
 *
 * @see SimpleAttribute
 * @author Niall Gallagher
 */
public abstract class SimpleNullableAttribute<O, A> extends AbstractAttribute<O, A> {

    /**
     * Creates an attribute with the given name.
     *
     * This name is not actually used by the query engine except in providing informative exception and debug messages.
     * As such it is recommended, but not required, that a name be provided.
     * <p/>
     * A suitable name might be the name of the field to which an attribute refers.
     *
     * @param attributeName The name for this attribute
     * @see #SimpleNullableAttribute()
     */
    public SimpleNullableAttribute(String attributeName) {
        super(attributeName);
    }

    /**
     * Creates an attribute with no name. A name for the attribute will be generated automatically from the name of the
     * subclass (or anonymous class) which implements the attribute.
     *
     * @see #SimpleNullableAttribute(String)
     */
    public SimpleNullableAttribute() {
        super();
    }

    /**
     * Creates an attribute with no name, and manually specifies the type of the attribute and its enclosing
     * object.
     *
     * @param objectType The type of the object containing this attribute
     * @param attributeType The type of this attribute
     */
    public SimpleNullableAttribute(Class<O> objectType, Class<A> attributeType) {
        super(objectType, attributeType);
    }

    /**
     * Creates an attribute with the given name, and manually specifies the type of the attribute and its enclosing
     * object.
     *
     * @param objectType The type of the object containing this attribute
     * @param attributeType The type of this attribute
     * @param attributeName The name for this attribute
     */
    public SimpleNullableAttribute(Class<O> objectType, Class<A> attributeType, String attributeName) {
        super(objectType, attributeType, attributeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<A> getValues(O object, QueryOptions queryOptions) {
        A value = getValue(object, queryOptions);
        return value == null ? Collections.<A>emptyList() : Collections.singletonList(value);
    }

    /**
     * Returns the (possibly null) value of the attribute from the object.
     * <p/>
     * @param object The object from which the value of the attribute is required
     * @param queryOptions Optional parameters supplied by the application along with the operation which is causing
     * this attribute to be invoked (either a query, or an update to the collection)
     * @return The value for the attribute, which can be null
     */
    public abstract A getValue(O object, QueryOptions queryOptions);
}
