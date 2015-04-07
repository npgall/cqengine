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

import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * @author Niall Gallagher
 */
public interface Attribute<O, A> {

    /**
     * Returns the type of the object which contains the attribute.
     * @return the type of the object which contains the attribute
     */
    Class<O> getObjectType();

    /**
     * Returns the type of the attribute.
     * @return the type of the attribute
     */
    Class<A> getAttributeType();

    /**
     * Returns the name of the attribute, as supplied to the constructor.
     * <p/>
     * @return the name of the attribute, as supplied to the constructor
     */
    String getAttributeName();

    /**
     * Returns the values belonging to the attribute in the given object.
     * <p/>
     * If the attribute is a {@link SimpleAttribute}, the list returned will contain a single value for the attribute.
     * If the attribute is a {@link MultiValueAttribute}, the list returned will contain any number of values for the
     * attribute.
     * <p/>
     * @param object The object from which the values of the attribute are required
     * @param queryOptions Optional parameters supplied by the application along with the operation which is causing
     * this attribute to be invoked (either a query, or an update to the collection)
     * @return The values for the attribute in the given object
     */
    Iterable<A> getValues(O object, QueryOptions queryOptions);
}