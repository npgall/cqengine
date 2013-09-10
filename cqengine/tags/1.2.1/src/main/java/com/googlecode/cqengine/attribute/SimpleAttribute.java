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

import java.util.*;

/**
 * Represents an attribute in an object (such as a field) which will not be null.
 *
 * Provides a method to read the value of the field given such an object.
 *
 * @see SimpleNullableAttribute
 * @author Niall Gallagher
 */
public abstract class SimpleAttribute<O, A> extends AbstractAttribute<O, A> {

    /**
     * Creates an attribute with no name. A name for the attribute will be generated automatically from the name of the
     * subclass (or anonymous class) which implements the attribute.
     *
     * @see #SimpleAttribute(String)
     */
    public SimpleAttribute() {
        super();
    }

    /**
     * Creates an attribute with the given name.
     *
     * This name is not actually used by the query engine except in providing informative exception and debug messages.
     * As such it is recommended, but not required, that a name be provided.
     * <p/>
     * A suitable name might be the name of the field to which an attribute refers.
     *
     * @param attributeName The name for this attribute
     * @see #SimpleAttribute()
     */
    public SimpleAttribute(String attributeName) {
        super(attributeName);
    }

    /**
     * Creates an attribute with no name, and manually specifies the type of the attribute and its enclosing
     * object.
     *
     * @param objectType The type of the object containing this attribute
     * @param attributeType The type of this attribute
     */
    public SimpleAttribute(Class<O> objectType, Class<A> attributeType) {
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
    public SimpleAttribute(Class<O> objectType, Class<A> attributeType, String attributeName) {
        super(objectType, attributeType, attributeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<A> getValues(O object) {
        return Collections.singletonList(getValue(object));
    }

    /**
     * Returns the (non-null) value of the attribute from the object.
     * <p/>
     * @param object The object from which the value of the attribute is required
     * @return The value for the attribute, which should never be null
     */
    public abstract A getValue(O object);
}
