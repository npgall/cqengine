package com.googlecode.cqengine.codegen;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author Niall Gallagher
 */
public class AttributeGenerator {

    public static String generateAttributesForPastingIntoTargetClass(final Class<?> targetClass) {
        return generateAttributesForClass(targetClass, false, "");
    }

    public static String generateSeparateAttributesClass(final Class<?> targetClass, String packageOfAttributesClass) {
        return generateAttributesForClass(targetClass, true, packageOfAttributesClass);
    }

    static String generateAttributesForClass(final Class<?> targetClass, boolean separateAttributesClass, String packageOfAttributesClass) {
        StringBuilder sb = new StringBuilder();
        if (separateAttributesClass) {
            sb.append("public class ").append(targetClass.getSimpleName()).append("Attributes {");
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
                                || field.getDeclaringClass().getPackage().toString().equals(packageOfAttributesClass)) {
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
                        // into a separate attributes class in the same package as the class containing the protected
                        // field (which may be a superclass of the target class)...
                        if ((!separateAttributesClass && currentClass.equals(targetClass))
                                || field.getDeclaringClass().getPackage().toString().equals(packageOfAttributesClass)) {
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
                return generateMultiValueAttributeForList(enclosingClass.getSimpleName(), genericType.getSimpleName(), field.getName());
            }
            else if (field.getType().isArray()) {
                if (field.getType().getComponentType().isPrimitive()) {
                    return generateMultiValueAttributeForPrimitiveArray(enclosingClass.getSimpleName(), PRIMITIVES_TO_WRAPPERS.get(field.getType().getComponentType()).getSimpleName(), field.getType().getComponentType().getSimpleName(), field.getName());
                }
                else {
                    return generateMultiValueAttributeForObjectArray(enclosingClass.getSimpleName(), field.getType().getComponentType().getSimpleName(), field.getName());
                }
            }
            else {
                return generateSimpleAttribute(enclosingClass.getSimpleName(), field.getType().getSimpleName(), field.getName());
            }
        }
        catch (Exception e) {
            return "    // *** Note: Could not generate CQEngine attribute automatically for field: " + enclosingClass.getSimpleName() + "." + field.getName() + " ***";
        }
    }


    static String generateSimpleAttribute(String objectType, String attributeType, String fieldName) {
        return "    /**\n" +
                "     * CQEngine attribute for field {@code " + objectType + "." + fieldName + "}.\n" +
                "     */\n" +
                "    public static final SimpleAttribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(fieldName) + " = new SimpleAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(fieldName) + "\") {\n" +
                "        public " + attributeType + " getValue(" + objectType + " " + objectType.toLowerCase() + ") { return " + objectType.toLowerCase() + "." + fieldName + "; }\n" +
                "    };";
    }

    static String generateMultiValueAttributeForList(String objectType, String attributeType, String fieldName) {
        return "    /**\n" +
                "     * CQEngine attribute for field {@code " + objectType + "." + fieldName + "}.\n" +
                "     */\n" +
                "    public static final MultiValueAttribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(fieldName) + " = new MultiValueAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(fieldName) + "\") {\n" +
                "        public List<" + attributeType + "> getValues(" + objectType + " " + objectType.toLowerCase() + ") { return " + objectType.toLowerCase() + "." + fieldName + "; }\n" +
                "    };";
    }

    static String generateMultiValueAttributeForObjectArray(String objectType, String attributeType, String fieldName) {
        return "    /**\n" +
                "     * CQEngine attribute for field {@code " + objectType + "." + fieldName + "}.\n" +
                "     */\n" +
                "    public static final MultiValueAttribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(fieldName) + " = new MultiValueAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(fieldName) + "\") {\n" +
                "        public List<" + attributeType + "> getValues(" + objectType + " " + objectType.toLowerCase() + ") { return Arrays.asList(" + objectType.toLowerCase() + "." + fieldName + "); }\n" +
                "    };";
    }

    static String generateMultiValueAttributeForPrimitiveArray(String objectType, String attributeType, String primitiveType, String fieldName) {
        return "    /**\n" +
                "     * CQEngine attribute for field {@code " + objectType + "." + fieldName + "}.\n" +
                "     */\n" +
                "    public static final MultiValueAttribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(fieldName) + " = new MultiValueAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(fieldName) + "\") {\n" +
                "        public List<" + attributeType + "> getValues(final " + objectType + " " + objectType.toLowerCase() + ") {\n" +
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

}
