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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.Query;

/**
 * The superclass of {@link Query}s which make assertions on attribute values.
 *
 * @param <A> The type of the attribute on which this query makes assertions
 * @param <O> The type of the object containing the attribute
 *
 * @author Niall Gallagher
 */
public abstract class SimpleQuery<O, A> implements Query<O> {

    protected final boolean attributeIsSimple;
    protected final Attribute<O, A> attribute;
    protected final SimpleAttribute<O, A> simpleAttribute;
    // Lazy calculate and cache hashCode...
    private int hashCode = 0;

    /**
     * Creates a new {@link SimpleQuery} initialized to make assertions on values of the specified attribute
     * @param attribute The attribute on which the assertion is to be made
     */
    public SimpleQuery(Attribute<O, A> attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("The attribute argument was null.");
        }
        this.attribute = attribute;
        if (attribute instanceof SimpleAttribute) {
            this.attributeIsSimple = true;
            this.simpleAttribute = (SimpleAttribute<O, A>) attribute;
        }
        else {
            this.attributeIsSimple = false;
            this.simpleAttribute = null;
        }
    }

    /**
     * Returns the type of the attribute originally supplied to the constructor, a shortcut for
     * {@link Attribute#getAttributeType()}.
     * @return the type of the attribute originally supplied to the constructor
     */
    public Class<A> getAttributeType() {
        return attribute.getAttributeType();
    }

    /**
     * Returns the name of the attribute originally supplied to the constructor, a shortcut for
     * {@link Attribute#getAttributeName()}.
     * @return the name of the attribute originally supplied to the constructor
     */
    public String getAttributeName() {
        return attribute.getAttributeName();
    }

    /**
     * Returns the attribute originally supplied to the constructor.
     * @return The attribute originally supplied to the constructor
     */
    public Attribute<O, A> getAttribute() {
        return attribute;
    }

    @Override
    public final boolean matches(O object) {
        if (attributeIsSimple) {
            return matchesSimpleAttribute(simpleAttribute, object);
        }
        else {
            return matchesNonSimpleAttribute(attribute, object);
        }
    }

    protected abstract boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object);

    protected abstract boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object);

    @Override
    public int hashCode() {
        // Lazy calculate and cache hashCode...
        int h = this.hashCode;
        if (h == 0) { // hashCode not cached
            h = calcHashCode();
            if (h == 0) { // 0 is normally a valid hashCode, coalesce with another...
                h = -1838660945; // was randomly chosen
            }
            this.hashCode = h; // cache hashCode
        }
        return h;
    }

    abstract protected int calcHashCode();
}
