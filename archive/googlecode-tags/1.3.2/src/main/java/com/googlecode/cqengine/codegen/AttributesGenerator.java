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
package com.googlecode.cqengine.codegen;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Automatically generates source code defining CQEngine attributes, for the fields in a given target class.
 * <p/>
 * Generates CQEngine {@link com.googlecode.cqengine.attribute.SimpleAttribute}s or
 * {@link com.googlecode.cqengine.attribute.SimpleNullableAttribute}s for regular fields, and generates
 * CQEngine {@link com.googlecode.cqengine.attribute.MultiValueNullableAttribute}s for fields which themselves are
 * Lists or arrays.
 * <p/>
 * Note that by default this code generator is cautious and generates "Nullable" attributes by default, for all fields
 * which are not primitive. Checking for nulls involves a performance penalty at runtime. So for fields which will not
 * actually be null, it is recommended to replace those attributes with non-nullable variants as discussed in the
 * comments in the generated source code.
 * <p/>
 * Methods are provided both to generate source code for attributes which can be copy-pasted into the target class,
 * and to generate entirely separate attributes classes. In the latter case, for example if class "Car" is given
 * as the target class, source code for a companion class "CQCar" will be generated containing attributes for accessing
 * the fields in class "Car".
 * <p/>
 * @author Niall Gallagher
 */
public class AttributesGenerator {

    /**
     * Generates source code which defines attributes for all fields in the given target class, for the purpose of
     * copy-pasting directly into the target class.
     *
     * @param targetClass The POJO class containing fields for which attributes are to be generated
     * @return Source code defining attributes for each of the fields in the target class
     */
    public static String generateAttributesForPastingIntoTargetClass(final Class<?> targetClass) {
        return generateAttributesForClass(targetClass, false, "");
    }

    /**
     * Generates source code of a complete separate class, containing attributes for fields in the given target class.
     *
     * @param targetClass The POJO class containing fields for which attributes are to be generated
     * @param packageOfAttributesClass The desired package name of the attributes class. Note this will be used to
     * determine which fields will be visible to the generated class, i.e. attributes for package-private fields will
     * only be generated if the generated class will be in the same package as the target class
     * @return Source code of a complete separate class, containing attributes for fields in the given target class
     */
    public static String generateSeparateAttributesClass(final Class<?> targetClass, String packageOfAttributesClass) {
        return generateAttributesForClass(targetClass, true, packageOfAttributesClass);
    }

    /**
     * Generates source code of a complete separate class, containing attributes for fields in the given target class.
     *
     * @param targetClass The POJO class containing fields for which attributes are to be generated
     * @param packageOfAttributesClass The desired package name of the attributes class. Note this will be used to
     * determine which fields will be visible to the generated class, i.e. attributes for package-private fields will
     * only be generated if the generated class will be in the same package as the target class
     * @return Source code of a complete separate class, containing attributes for fields in the given target class
     */
    public static String generateSeparateAttributesClass(final Class<?> targetClass, Package packageOfAttributesClass) {
        return generateAttributesForClass(targetClass, true, packageOfAttributesClass.getName());
    }

    static String generateAttributesForClass(final Class<?> targetClass, boolean separateAttributesClass, String packageOfAttributesClass) {
        StringBuilder sb = new StringBuilder();
        if (separateAttributesClass) {
            sb.append("package ").append(packageOfAttributesClass).append(";\n\n");
            sb.append("import com.googlecode.cqengine.attribute.*;\n");
            sb.append("import java.util.*;\n");
            String targetClassName = targetClass.getName().replace("$", "."); // replacing $ handles nested classes
            sb.append("import ").append(targetClassName).append(";\n\n");
            sb.append("/**\n");
            sb.append(" * CQEngine attributes for accessing fields in class {@code ").append(targetClassName).append("}.\n");
            sb.append(" * <p/>.\n");
            sb.append(" * Auto-generated by CQEngine's {@code ").append(AttributesGenerator.class.getSimpleName()).append("}.\n");
            sb.append(" */\n");
            sb.append("public class CQ").append(targetClass.getSimpleName()).append(" {");
        }
        Class currentClass = targetClass;
        while (currentClass != null && currentClass != Object.class) {
            for (Field field : Arrays.asList(currentClass.getDeclaredFields())) {
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers)) {

                    if (Modifier.isPrivate(modifiers)) {
                        // Generate attribute for this private field, if the attribute will be pasted into the target
                        // class and so it can access the private field...
                        if (!separateAttributesClass && currentClass.equals(targetClass)) {
                            sb.append("\n\n");
                            sb.append(generateAttributeForField(targetClass, field));
                        }
                    }
                    else if (Modifier.isProtected(modifiers)) {
                        // Generate attribute for this protected field, if the attribute will be pasted into the target
                        // class (and can access inherited protected field), or if the attribute will be generated
                        // into a separate attributes class in the same package as the class containing the protected
                        // field (which may be a superclass of the target class)...
                        if (!separateAttributesClass
                                || field.getDeclaringClass().getPackage().getName().equals(packageOfAttributesClass)) {
                            sb.append("\n\n");
                            sb.append(generateAttributeForField(targetClass, field));
                        }
                    }
                    else if (Modifier.isPublic(modifiers)) {
                        sb.append("\n\n");
                        sb.append(generateAttributeForField(targetClass, field));
                    }
                    else {
                        // Package-private field.
                        // Generate attribute for this package-private field, if the attribute will be pasted into the target
                        // class and the field is in that class, or if the attribute will be generated
                        // into a separate attributes class in the same package as the class containing the package-private
                        // field (which may be a superclass of the target class)...
                        if ((!separateAttributesClass && currentClass.equals(targetClass))
                                || field.getDeclaringClass().getPackage().getName().equals(packageOfAttributesClass)) {
                            sb.append("\n\n");
                            sb.append(generateAttributeForField(targetClass, field));
                        }
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        if (separateAttributesClass) {
            sb.append("\n}\n");
        }
        return sb.toString();
    }

    static String generateAttributeForField(Class<?> enclosingClass, Field field) {
        try {
            if (field.getType().isPrimitive()) {
                return generateSimpleAttribute(enclosingClass.getSimpleName(), PRIMITIVES_TO_WRAPPERS.get(field.getType()).getSimpleName(), field.getName());
            }
            else if (List.class.isAssignableFrom(field.getType())) {
                ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
                if (parameterizedType.getActualTypeArguments().length != 1) {
                    throw new UnsupportedOperationException();
                }
                Class<?> genericType = (Class<?>)parameterizedType.getActualTypeArguments()[0];
                return generateMultiValueNullableAttributeForList(enclosingClass.getSimpleName(), genericType.getSimpleName(), field.getName());
            }
            else if (field.getType().isArray()) {
                if (field.getType().getComponentType().isPrimitive()) {
                    return generateMultiValueNullableAttributeForPrimitiveArray(enclosingClass.getSimpleName(), PRIMITIVES_TO_WRAPPERS.get(field.getType().getComponentType()).getSimpleName(), field.getType().getComponentType().getSimpleName(), field.getName());
                }
                else {
                    return generateMultiValueNullableAttributeForObjectArray(enclosingClass.getSimpleName(), field.getType().getComponentType().getSimpleName(), field.getName());
                }
            }
            else {
                return generateSimpleNullableAttribute(enclosingClass.getSimpleName(), field.getType().getSimpleName(), field.getName());
            }
        }
        catch (Exception e) {
            return "    // *** Note: Could not generate CQEngine attribute automatically for field: " + enclosingClass.getSimpleName() + "." + field.getName() + " ***";
        }
    }

    static String generateSimpleAttribute(String objectType, String attributeType, String fieldName) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing field {@code " + objectType + "." + fieldName + "}.\n" +
                "     */\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(fieldName) + " = new SimpleAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(fieldName) + "\") {\n" +
                "        public " + attributeType + " getValue(" + objectType + " " + objectType.toLowerCase() + ") { return " + objectType.toLowerCase() + "." + fieldName + "; }\n" +
                "    };";
    }

    static String generateSimpleNullableAttribute(String objectType, String attributeType, String fieldName) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing field {@code " + objectType + "." + fieldName + "}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if this field cannot be null, replace this SimpleNullableAttribute with a SimpleAttribute\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(fieldName) + " = new SimpleNullableAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(fieldName) + "\") {\n" +
                "        public " + attributeType + " getValue(" + objectType + " " + objectType.toLowerCase() + ") { return " + objectType.toLowerCase() + "." + fieldName + "; }\n" +
                "    };";
    }
    
    static String generateMultiValueNullableAttributeForList(String objectType, String attributeType, String fieldName) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing field {@code " + objectType + "." + fieldName + "}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if the list cannot contain null elements change true to false in the following constructor, or\n" +
                "    // - if the list cannot contain null elements AND the field itself cannot be null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(fieldName) + " = new MultiValueNullableAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(fieldName) + "\", true) {\n" +
                "        public List<" + attributeType + "> getNullableValues(" + objectType + " " + objectType.toLowerCase() + ") { return " + objectType.toLowerCase() + "." + fieldName + "; }\n" +
                "    };";
    }

    static String generateMultiValueNullableAttributeForObjectArray(String objectType, String attributeType, String fieldName) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing field {@code " + objectType + "." + fieldName + "}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if the array cannot contain null elements change true to false in the following constructor, or\n" +
                "    // - if the array cannot contain null elements AND the field itself cannot be null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(fieldName) + " = new MultiValueNullableAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(fieldName) + "\", true) {\n" +
                "        public List<" + attributeType + "> getNullableValues(" + objectType + " " + objectType.toLowerCase() + ") { return Arrays.asList(" + objectType.toLowerCase() + "." + fieldName + "); }\n" +
                "    };";
    }

    static String generateMultiValueNullableAttributeForPrimitiveArray(String objectType, String attributeType, String primitiveType, String fieldName) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing field {@code " + objectType + "." + fieldName + "}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if this field cannot be null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(fieldName) + " = new MultiValueNullableAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(fieldName) + "\", false) {\n" +
                "        public List<" + attributeType + "> getNullableValues(final " + objectType + " " + objectType.toLowerCase() + ") {\n" +
                "            return new AbstractList<" + attributeType + ">() {\n" +
                "                public " + attributeType + " get(int i) { return " + objectType.toLowerCase() + "." + fieldName + "[i]; }\n" +
                "                public int size() { return " + objectType.toLowerCase() + "." + fieldName + ".length; }\n" +
                "            };\n" +
                "        }\n" +
                "    };";
    }

    static String toUpperCaseWithUnderscores(String camelCase) {
        String[] words = camelCase.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> iterator = Arrays.asList(words).iterator(); iterator.hasNext(); ) {
            String word = iterator.next();
            sb.append(word.toUpperCase());
            if (iterator.hasNext()) {
                sb.append("_");
            }
        }
        return sb.toString();
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

    /**
     * Private constructor, not used.
     */
    AttributesGenerator() {
    }
}
