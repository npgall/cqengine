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

import com.googlecode.cqengine.attribute.Attribute;

import java.util.List;
import java.util.Map;

/**
 * @author Niall Gallagher
 */
public class OrderByOption<O> implements QueryOption<O> {

    private final List<Attribute<O, ? extends Comparable>> attributes;
    private final boolean descending;

    public OrderByOption(List<Attribute<O, ? extends Comparable>> attributes, boolean descending) {
        this.attributes = attributes;
        this.descending = descending;
    }

    public List<Attribute<O, ? extends Comparable>> getAttributes() {
        return attributes;
    }

    public boolean isDescending() {
        return descending;
    }

    public static <O> OrderByOption<O> extract(Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        QueryOption<O> option = queryOptions.get(OrderByOption.class);
        if (option instanceof OrderByOption) {
            return (OrderByOption<O>) option;
        }
        return null;
    }
}
