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
import com.googlecode.cqengine.query.option.AttributeOrder;
import com.googlecode.cqengine.query.simple.Has;

/**
 * An attribute which wraps another delegate attribute, and can be used in a {@link QueryFactory#orderBy(AttributeOrder)}
 * clause to control the placement in results of objects which have and do not have values for the delegate attribute.
 * <p/>
 * Essentially this attribute allows results to be sorted based on whether a {@link Has} query on the delegate attribute
 * returns true or false.
 * <p/>
 * The default behaviour of CQEngine is as follows:
 * <ul>
 *     <li>
 *         When the sort order requested for a given query is {@link QueryFactory#ascending(Attribute)}, objects without
 *         values for the attribute will be returned first, followed by objects which have values for the attribute,
 *         sorted ascending by their values.
 *     </li>
 *     <li>
 *         When the sort order requested is {@link QueryFactory#descending(Attribute)}, objects with
 *         values for the attribute will be returned first sorted descending by their values, followed by objects
 *         without values for the attribute.
 *     </li>
 * </ul>
 * The subclass implementations of this class alter that default behaviour, allowing objects without values for the
 * attribute to <i>always</i> be returned before or after objects with values.
 *
 * @author niall.gallagher
 */
public abstract class OrderControlAttribute<O> extends SimpleAttribute<O, Integer> {

    protected final Attribute<O, ? extends Comparable> delegateAttribute;

    protected OrderControlAttribute(Attribute<O, ? extends Comparable> delegateAttribute, String delegateAttributeName) {
        super(delegateAttribute.getObjectType(), Integer.class, delegateAttributeName);
        if (delegateAttribute instanceof OrderControlAttribute) {
            throw new IllegalArgumentException("Delegate attribute cannot also be an OrderControlAttribute: " + delegateAttribute);
        }
        this.delegateAttribute = delegateAttribute;
    }

    public Attribute<O, ?> getDelegateAttribute() {
        return delegateAttribute;
    }
}
