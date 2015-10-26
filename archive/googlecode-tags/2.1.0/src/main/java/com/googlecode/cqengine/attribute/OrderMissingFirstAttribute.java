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

import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Has;

/**
 * An {@link OrderControlAttribute} which orders results such that objects without values for the given delegate
 * attribute are always returned <i>before</i> objects with values for the attribute.
 *
 * @author niall.gallagher
 */
public class OrderMissingFirstAttribute<O> extends OrderControlAttribute<O> {

    final Has<O, ? extends Comparable> hasQuery;

    public <A extends Comparable<A>> OrderMissingFirstAttribute(Attribute<O, A> delegateAttribute) {
        super(delegateAttribute, "missingFirst_" + delegateAttribute.getAttributeName());
        this.hasQuery = QueryFactory.has(delegateAttribute);
    }

    @Override
    public Integer getValue(O object, QueryOptions queryOptions) {
        return hasQuery.matches(object, queryOptions) ? 1 : 0;
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof OrderMissingFirstAttribute;
    }
}
