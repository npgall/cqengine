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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

    protected AbstractAttribute(Class<O> objectType, Class<A> attributeType) {
        this.attributeName = "<Unnamed attribute, " + getClass() + ">";
        this.objectType = objectType;
        this.attributeType = attributeType;
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
            Type actualType = superclass.getActualTypeArguments()[0];
            Class<O> cls;
            if (actualType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType)actualType;
                @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
                Class<O> actualClass = (Class<O>) parameterizedType.getRawType();
                cls = actualClass;
            }
            else {
                @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
                Class<O> actualClass = (Class<O>) actualType;
                cls = actualClass;
            }
            return cls;
        }
        catch (Exception e) {
            String attributeClassStr = attributeName.startsWith("<Unnamed attribute, class ") ? "" : " (" + getClass() + ")";
            throw new IllegalStateException("Attribute '" + attributeName + "'" + attributeClassStr + " is invalid, cannot read generic type information from it. Attributes should typically EITHER be declared in code with generic type information as a (possibly anonymous) subclass of one of the provided attribute types, OR you can use a constructor of the attribute which allows the types to be specified manually.");
        }
    }

    Class<A> readAttributeType() {
        try {
            ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
            Type actualType = superclass.getActualTypeArguments()[1];
            Class<A> cls;
            if (actualType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType)actualType;
                @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
                Class<A> actualClass = (Class<A>) parameterizedType.getRawType();
                cls = actualClass;
            }
            else {
                @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
                Class<A> actualClass = (Class<A>) actualType;
                cls = actualClass;
            }
            return cls;
        }
        catch (Exception e) {
            String attributeClassStr = attributeName.startsWith("<Unnamed attribute, class ") ? "" : " (" + getClass() + ")";
            throw new IllegalStateException("Attribute '" + attributeName + "'" + attributeClassStr + " is invalid, cannot read generic type information from it. Attributes should typically EITHER be declared in code with generic type information as a (possibly anonymous) subclass of one of the provided attribute types, OR you can use a constructor of the attribute which allows the types to be specified manually.");
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
