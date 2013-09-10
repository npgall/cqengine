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

/**
 * Represents an attribute and an associated preference for sorting results according to that attribute
 * in ascending or descending order.
 *
 * @author Roberto Socrates
 * @author Niall Gallagher
 */
public class AttributeOrder<O> {
	
    private final Attribute<O, ? extends Comparable> attribute;
    private final boolean descending;

    public AttributeOrder(Attribute<O, ? extends Comparable> attribute, boolean descending) {
		
		this.attribute = attribute;
		this.descending = descending;
	}

    public Attribute<O, ? extends Comparable> getAttribute() {
        return attribute;
    }

    public boolean isDescending() {
        return descending;
    }

    @Override
    public String toString() {
        return descending
                ? "descending(" + attribute.getObjectType().getSimpleName() + "." + attribute.getAttributeName() + ")"
                : "ascending(" + attribute.getObjectType().getSimpleName() + "." + attribute.getAttributeName() + ")";
    }
}
