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
package com.googlecode.cqengine.attribute.impl;

import com.googlecode.cqengine.attribute.Attribute;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * @author Niall Gallagher
 */
public abstract class AbstractAttribute<O, A> implements Attribute<O, A> {

    private final Class<O> objectType;
    private final Class<A> attributeType;
    private final String attributeName;

    private final int hashCode;

    public AbstractAttribute() {
        this.attributeName = "<Unnamed attribute, " + getClass() + ">";
        this.objectType = readObjectType();
        this.attributeType = readAttributeType();
        this.hashCode = calcHashCode();
    }

    public AbstractAttribute(String attributeName) {
        this.attributeName = attributeName;
        this.objectType = readObjectType();
        this.attributeType = readAttributeType();
        this.hashCode = calcHashCode();
    }

    protected AbstractAttribute(Class<O> objectType, Class<A> attributeType, String attributeName) {
        this.attributeName = attributeName;
        this.objectType = objectType;
        this.attributeType = attributeType;
        this.hashCode = calcHashCode();
    }
    @Override
    public Class<O> getObjectType() {
        return objectType;
    }

    @Override
    public Class<A> getAttributeType() {
        return attributeType;
    }

    @Override
    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "objectType=" + objectType +
                ", attributeType=" + attributeType +
                ", attributeName='" + attributeName + '\'' +
                '}';
    }

    Class<O> readObjectType() {
        try {
            ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
            @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
            Class<O> cls = (Class<O>) superclass.getActualTypeArguments()[0];
            return cls;
        }
        catch (Exception e) {
            throw new IllegalStateException("Attribute '" + attributeName + "' is declared with invalid type parameters (" + getClass() + ")");
        }
    }

    Class<A> readAttributeType() {
        try {
            ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
            @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
            Class<A> cls = (Class<A>) superclass.getActualTypeArguments()[1];
            return cls;
        }
        catch (Exception e) {
            throw new IllegalStateException("Attribute '" + attributeName + "' is declared with invalid type parameters (" + getClass() + ")");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractAttribute)) return false;

        AbstractAttribute that = (AbstractAttribute) o;

        if (hashCode != that.hashCode) return false;
        if (!attributeName.equals(that.attributeName)) return false;
        if (!attributeType.equals(that.attributeType)) return false;
        if (!objectType.equals(that.objectType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    int calcHashCode() {
        int result = objectType.hashCode();
        result = 31 * result + attributeType.hashCode();
        result = 31 * result + attributeName.hashCode();
        return result;
    }
}
