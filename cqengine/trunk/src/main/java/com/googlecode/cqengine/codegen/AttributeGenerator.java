package com.googlecode.cqengine.codegen;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Niall Gallagher
 */
public class AttributeGenerator {

    public static String generateAttributesForClass(final Class<?> cls, boolean generateSeparateAttributesClass) {
        StringBuilder sb = new StringBuilder();
        if (generateSeparateAttributesClass) {
            sb.append("public class ").append(cls.getSimpleName()).append("Attributes {");
        }
        Class current = cls;
        while (current != null && current != Object.class) {
            for (Field field : Arrays.asList(current.getDeclaredFields())) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (current.equals(cls) && !generateSeparateAttributesClass && Modifier.isPrivate(field.getModifiers())) {
                        // Generate attribute for private fields, but only if attributes will by copy-pasted into object class...
                        sb.append("\n\n");
                        sb.append(generateAttributeForField(current, field));
                    }
                    else if (Modifier.isPublic(field.getModifiers()) || Modifier.isProtected(field.getModifiers())) {
                        // Generate attribute for inherited fields...
                        sb.append("\n\n");
                        sb.append(generateAttributeForField(current, field));
                    }
                }
            }
            current = current.getSuperclass();
        }
        if (generateSeparateAttributesClass) {
            sb.append("\n}\n");
        }
        return sb.toString();
    }

    public static String generateAttributeForField(Class<?> enclosingClass, Field field) {
        final Class<?> fieldType = field.getType().isPrimitive() ? PRIMITIVES_TO_WRAPPERS.get(field.getType()) : field.getType();
        return generateAttribute(enclosingClass.getSimpleName(), fieldType.getSimpleName(), "SimpleAttribute", field.getName().toUpperCase(), field.getName());
    }


    public static String generateAttribute(String objectType, String attributeType, String attributeImplementation, String identifier, String attributeName) {
        return "    public static final " + attributeImplementation + "<" + objectType + ", " + attributeType + "> " + identifier + " = new " + attributeImplementation + "<" + objectType + ", " + attributeType + ">(\"" + attributeName.toUpperCase() + "\") {\n" +
               "        public " + attributeType + " getValue(" + objectType + " " + objectType.toLowerCase() + ") { return " + objectType.toLowerCase() + "." + attributeName + "; }\n" +
               "    };";
    }

    static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new HashMap<Class<?>, Class<?>>(){{
        put(boolean.class, Boolean.class);
        put(byte.class, Byte.class);
        put(short.class, Short.class);
        put(char.class, Character.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(float.class, Float.class);
        put(double.class, Double.class);
    }};

}
