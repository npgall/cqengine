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

import java.util.List;

/**
 * Represents an attribute in an object which has multiple values (such as a field which is itself a collection),
 * and provides a method to read the values from the field given such an object.
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
     * Returns the values of the attribute from the object.
     * <p/>
     * @param object The object from which the values of the attribute are required
     * @return The values for the attribute
     */
    @Override
    public abstract List<A> getValues(O object);
}
