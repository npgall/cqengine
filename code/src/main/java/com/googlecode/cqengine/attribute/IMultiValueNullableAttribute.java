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

public interface IMultiValueNullableAttribute<O, A> extends Attribute<O, A> {
    /**
     * Returns the values of the attribute from the object, omitting any null values.
     * <p/>
     * @param object The object from which the values of the attribute are required
     * @param queryOptions Optional parameters supplied by the application along with the operation which is causing
     * this attribute to be invoked (either a query, or an update to the collection)
     * @return The values for the attribute
     */
    @Override Iterable<A> getValues(O object, QueryOptions queryOptions);

    /**
     * Returns the values of the attribute from the object, some of which can be null.
     * The actual list returned can also be null.
     * <p/>
     * @param object The object from which the values of the attribute are required
     * @param queryOptions Optional parameters supplied by the application along with the operation which is causing
     * this attribute to be invoked (either a query, or an update to the collection)
     * @return The values for the attribute, some of which might be null
     */
    Iterable<A> getNullableValues(O object, QueryOptions queryOptions);
}
