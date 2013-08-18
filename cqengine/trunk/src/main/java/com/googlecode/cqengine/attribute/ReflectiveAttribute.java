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

import java.lang.reflect.Field;

/**
 * A type of {@link SimpleAttribute} which is implemented using reflection.
 * <p/>
 * This type of attribute will not perform as well as an attribute defined in code,
 * but this type can sometimes be convenient.
 *
 * @author Niall Gallagher
 */
public class ReflectiveAttribute<O, A> extends SimpleAttribute<O, A> {

    final Field field;

    /**
     * Creates an attribute which reads values from the field indicated using reflection.
     *
     * @param objectType The type of the object containing the field
     * @param fieldType The type of the field in the object
     * @param fieldName The name of the field
     */
    public ReflectiveAttribute(Class<O> objectType, Class<A> fieldType, String fieldName) {
        super(objectType, fieldType, fieldName);
        Field field;
        try {
            field = getField(objectType, fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
        }
        catch (Exception e) {
            throw new IllegalStateException("Invalid attribute definition: No such field '" + fieldName + "' in object '" + objectType.getName() + "'");
        }
        if (!fieldType.isAssignableFrom(field.getType())) {
            throw new IllegalStateException("Invalid attribute definition: The type of field '" + fieldName + "', type '" + field.getType() + "', in object '" + objectType.getName() + "', is not assignable to the type indicated: " + fieldType.getName());
        }
        this.field = field;
    }

    /**
     * Searches the given class and its superclasses for the given field. Supports private and non-private fields.
     * @param cls The class to search
     * @param fieldName The name of the field
     * @return The field with the given name
     * @throws NoSuchFieldException If no such field can be found
     */
    static Field getField(Class<?> cls, String fieldName) throws NoSuchFieldException {
        while (cls != null && cls != Object.class) {
            try {
                return cls.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException("No such field: " + fieldName);
    }

    @Override
    public A getValue(O object) {
        try {
            @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
            A value = (A) field.get(object);
            return value;
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to read value from field '" + field.getName() + "'", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ReflectiveAttribute that = (ReflectiveAttribute) o;

        if (!field.equals(that.field)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + field.hashCode();
        return result;
    }

    /**
     * Returns an attribute which reads values from the field indicated using reflection.
     *
     * @param objectType The type of the object containing the field
     * @param fieldType The type of the field in the object
     * @param fieldName The name of the field
     * @return An attribute which reads values from the field indicated using reflection
     */
    public static <O, A> ReflectiveAttribute<O, A> forField(Class<O> objectType, Class<A> fieldType, String fieldName) {
        return new ReflectiveAttribute<O, A>(objectType, fieldType, fieldName);
    }
}
