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
package com.googlecode.cqengine.query.option;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a list of attributes and associated preferences for sorting results according to those attributes each
 * in ascending or descending order.
 *
 * @author Roberto Socrates
 * @author Niall Gallagher
 */
public class OrderByOption<O> {

    private final List<AttributeOrder<O>> attributeOrders;

    public OrderByOption(List<AttributeOrder<O>> attributeOrders) {
        if (attributeOrders.isEmpty()) {
            throw new IllegalArgumentException("The list of attribute orders cannot be empty");
        }
        this.attributeOrders = attributeOrders;
    }

    public List<AttributeOrder<O>> getAttributeOrders() {
        return attributeOrders;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("orderBy(");
        for (Iterator<AttributeOrder<O>> iterator = attributeOrders.iterator(); iterator.hasNext(); ) {
            AttributeOrder<?> childQuery = iterator.next();
            sb.append(childQuery);
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderByOption)) return false;

        OrderByOption that = (OrderByOption) o;

        if (!attributeOrders.equals(that.attributeOrders)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return attributeOrders.hashCode();
    }
}
