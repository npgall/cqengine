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

/**
 * Represents an attribute in an object which has multiple values (such as a field which is itself a collection),
 * where all of the values are known to be non-null.
 * <p/>
 * Provides a method to read the values from the field given such an object.
 * <p/>
 * This type of attribute skips null checks on values at runtime, thereby allowing maximum performance.
 * If this type of attribute encounters a null, it is likely that a {@code NullPointerException} will be thrown.
 * Therefore when it is not possible to know in advance if values might be null, it is recommended to use
 * {@link MultiValueNullableAttribute} instead.
 *
 * @author Niall Gallagher
 */
public abstract class MultiValueAttribute<O, A> extends AbstractAttribute<O, A> {

    /**
     * Creates an attribute with the given name.
     *
     * This name is not actually used by the query engine except in providing informative exception and debug messages.
     * As such it is recommended, but not required, that a name be provided.
     * <p/>
     * A suitable name might be the name of the field to which an attribute refers.
     *
     * @param attributeName The name for this attribute
     * @see #MultiValueAttribute()
     */
    public MultiValueAttribute(String attributeName) {
        super(attributeName);
    }

    /**
     * Creates an attribute with no name. A name for the attribute will be generated automatically from the name of the
     * subclass (or anonymous class) which implements the attribute.
     *
     * @see #MultiValueAttribute(String)
     */
    public MultiValueAttribute() {
    }

    /**
     * Creates an attribute with no name, and manually specifies the type of the attribute and its enclosing
     * object.
     *
     * @param objectType The type of the object containing this attribute
     * @param attributeType The type of this attribute
     */
    public MultiValueAttribute(Class<O> objectType, Class<A> attributeType) {
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
    public MultiValueAttribute(Class<O> objectType, Class<A> attributeType, String attributeName) {
        super(objectType, attributeType, attributeName);
    }

    /**
     * Returns the non-null values of the attribute from the object.
     * <p/>
     * @param object The object from which the values of the attribute are required
     * @param queryOptions Optional parameters supplied by the application along with the operation which is causing
     * this attribute to be invoked (either a query, or an update to the collection)
     * @return The values for the attribute, which should never be null
     */
    @Override
    public abstract Iterable<A> getValues(O object, QueryOptions queryOptions);
}
