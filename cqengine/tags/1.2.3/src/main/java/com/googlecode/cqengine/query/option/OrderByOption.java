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
package com.googlecode.cqengine.query.option;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents a list of attributes and associated preferences for sorting results according to those attributes each
 * in ascending or descending order.
 *
 * @author Roberto Socrates
 * @author Niall Gallagher
 */
public class OrderByOption<O> implements QueryOption<O> {

    private final List<AttributeOrder<O>> attributeOrders;

    public OrderByOption(List<AttributeOrder<O>> attributeOrders) {
        this.attributeOrders = attributeOrders;
    }

    public List<AttributeOrder<O>> getAttributeOrders() {
        return attributeOrders;
    }

    public static <O> OrderByOption<O> extract(Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        QueryOption<O> option = queryOptions.get(OrderByOption.class);
        if (option instanceof OrderByOption) {
            return (OrderByOption<O>) option;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("orderBy(");
        for (Iterator<AttributeOrder<O>> iterator = attributeOrders.iterator(); iterator.hasNext(); ) {
            AttributeOrder<O> childQuery = iterator.next();
            sb.append(childQuery);
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
