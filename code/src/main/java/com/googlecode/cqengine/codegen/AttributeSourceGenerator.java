/**
 * Copyright 2012-2015 Niall Gallagher
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

import java.lang.reflect.*;
import java.util.*;

import static com.googlecode.cqengine.codegen.MemberFilters.ALL_MEMBERS;
import static com.googlecode.cqengine.codegen.MemberFilters.FIELDS_ONLY;

/**
 * Automatically generates source code defining CQEngine attributes for accessing the fields and methods (aka members)
 * of a given target class.
 * <p/>
 * Generates CQEngine {@link com.googlecode.cqengine.attribute.SimpleAttribute}s or
 * {@link com.googlecode.cqengine.attribute.SimpleNullableAttribute}s for members which return singular values,
 * and generates CQEngine {@link com.googlecode.cqengine.attribute.MultiValueNullableAttribute}s for members
 * which return Iterables or arrays.
 * <p/>
 * Note that by default this code generator is cautious and generates "Nullable" attributes by default, for all members
 * except those which return primitive types. Nullable attributes check for and handle nulls automatically, however
 * checking for nulls incurs a performance penalty at runtime. So for non-primitive fields and accessor methods which
 * will not actually contain or return null, it is recommended to replace those attributes with non-nullable variants
 * as discussed in the comments in the generated source code. This is optional but can remove the overhead of
 * unnecessary null checks at runtime.
 * <p/>
 * Methods are provided both to generate source code for attributes which can be copy-pasted into the target class,
 * and to generate entirely separate attributes classes. In the latter case, for example if class "Car" is given
 * as the target class, source code for a companion class "CQCar" will be generated containing attributes for accessing
 * the fields and accessor methods in class "Car".
 * <p/>
 * @author Niall Gallagher
 */
public class AttributeSourceGenerator {

    /**
     * Generates source code which defines attributes for accessing all fields in the given target class, for the
     * purpose of copy-pasting directly into the target class.
     *
     * @param targetClass The POJO class containing fields for which attributes are to be generated
     * @return Source code defining attributes for accessing each of the fields in the target class
     */
    public static String generateAttributesForPastingIntoTargetClass(final Class<?> targetClass) {
        return generateAttributesForClass(targetClass, false, "", FIELDS_ONLY);
    }

    /**
     * Generates source code which defines attributes for accessing all members (fields and methods) in the given target
     * class which are acceptable to the given filter, for the purpose of copy-pasting directly into the target class.
     *
     * @param targetClass The POJO class containing members for which attributes are to be generated
     * @param memberFilter A filter which determines the subset of the members of a class (fields and accessor methods)
     * for which attributes should be generated
     * @return Source code defining attributes for accessing each of the members in the target class
     */
    public static String generateAttributesForPastingIntoTargetClass(final Class<?> targetClass, MemberFilter memberFilter) {
        return generateAttributesForClass(targetClass, false, "", memberFilter);
    }

    /**
     * Generates source code of a complete separate class, containing attributes for accessing all members
     * (fields and methods) in the given target class.
     *
     * @param targetClass The POJO class containing members for which attributes are to be generated
     * @param packageOfAttributesClass The desired package name of the attributes class. Note this will be used to
     * determine which members will be visible to the generated class, i.e. attributes for package-private members will
     * only be generated if the generated class will be in the same package as the target class
     * @return Source code of a complete separate class, containing attributes for accessing members in the given target
     * class
     */
    public static String generateSeparateAttributesClass(final Class<?> targetClass, String packageOfAttributesClass) {
        return generateAttributesForClass(targetClass, true, packageOfAttributesClass, ALL_MEMBERS);
    }

    /**
     * Generates source code of a complete separate class, containing attributes for accessing all members
     * (fields and methods) in the given target class which are acceptable to the given filter.
     *
     * @param targetClass The POJO class containing members for which attributes are to be generated
     * @param packageOfAttributesClass The desired package name of the attributes class. Note this will be used to
     * determine which members will be visible to the generated class, i.e. attributes for package-private members will
     * only be generated if the generated class will be in the same package as the target class
     * @param memberFilter A filter which determines the subset of the members of a class (fields and accessor methods)
     * for which attributes should be generated
     * @return Source code of a complete separate class, containing attributes for accessing members in the given target
     * class
     */
    public static String generateSeparateAttributesClass(final Class<?> targetClass, String packageOfAttributesClass, MemberFilter memberFilter) {
        return generateAttributesForClass(targetClass, true, packageOfAttributesClass, memberFilter);
    }

    /**
     * Generates source code of a complete separate class, containing attributes for accessing all members
     * (fields and methods) in the given target class.
     *
     * @param targetClass The POJO class containing members for which attributes are to be generated
     * @param packageOfAttributesClass The desired package name of the attributes class. Note this will be used to
     * determine which members will be visible to the generated class, i.e. attributes for package-private members will
     * only be generated if the generated class will be in the same package as the target class
     * @return Source code of a complete separate class, containing attributes for accessing members in the given target class
     */
    public static String generateSeparateAttributesClass(final Class<?> targetClass, Package packageOfAttributesClass) {
        return generateAttributesForClass(targetClass, true, packageOfAttributesClass.getName(), ALL_MEMBERS);
    }

    /**
     * Generates source code of a complete separate class, containing attributes for accessing all members
     * (fields and methods) in the given target class which are acceptable to the given filter.
     *
     * @param targetClass The POJO class containing members for which attributes are to be generated
     * @param packageOfAttributesClass The desired package name of the attributes class. Note this will be used to
     * determine which members will be visible to the generated class, i.e. attributes for package-private members will
     * only be generated if the generated class will be in the same package as the target class
     * @param memberFilter A filter which determines the subset of the members of a class (fields and accessor methods)
     * for which attributes should be generated
     * @return Source code of a complete separate class, containing attributes for accessing members in the given target
     * class
     */
    public static String generateSeparateAttributesClass(final Class<?> targetClass, Package packageOfAttributesClass, MemberFilter memberFilter) {
        return generateAttributesForClass(targetClass, true, packageOfAttributesClass.getName(), memberFilter);
    }


    static String generateAttributesForClass(final Class<?> targetClass, boolean separateAttributesClass, String packageOfAttributesClass, MemberFilter memberFilter) {
        StringBuilder sb = new StringBuilder();
        if (separateAttributesClass) {
            sb.append("package ").append(packageOfAttributesClass).append(";\n\n");
            sb.append("import com.googlecode.cqengine.attribute.*;\n");
            sb.append("import com.googlecode.cqengine.query.option.QueryOptions;\n");
            sb.append("import java.util.*;\n");
            String targetClassName = targetClass.getName().replace("$", "."); // replacing $ handles nested classes
            sb.append("import ").append(targetClassName).append(";\n\n");
            sb.append("/**\n");
            sb.append(" * CQEngine attributes for accessing the members of class {@code ").append(targetClassName).append("}.\n");
            sb.append(" * <p/>.\n");
            sb.append(" * Auto-generated by CQEngine's {@code ").append(AttributeSourceGenerator.class.getSimpleName()).append("}.\n");
            sb.append(" */\n");
            sb.append("public class CQ").append(targetClass.getSimpleName()).append(" {");
        }
        Class currentClass = targetClass;
        Set<String> membersEncountered = new HashSet<String>();
        while (currentClass != null && currentClass != Object.class) {
            for (Member member : getMembers(currentClass)) {
                if (!memberFilter.accept(member)) {
                    continue;
                }
                if (membersEncountered.contains(member.getName())) {
                    // We already generated an attribute for a member with this name in a subclass of the current class...
                    continue;
                }
                int modifiers = member.getModifiers();
                if (!Modifier.isStatic(modifiers)) {

                    if (Modifier.isPrivate(modifiers)) {
                        // Generate attribute for this private member, if the attribute will be pasted into the target
                        // class and so it can access the private member...
                        if (!separateAttributesClass && currentClass.equals(targetClass)) {
                            sb.append("\n\n");
                            sb.append(generateAttributeForMember(targetClass, member));
                            membersEncountered.add(member.getName());
                        }
                    }
                    else if (Modifier.isProtected(modifiers)) {
                        // Generate attribute for this protected member, if the attribute will be pasted into the target
                        // class (and can access inherited protected member), or if the attribute will be generated
                        // into a separate attributes class in the same package as the class containing the protected
                        // member (which may be a superclass of the target class)...
                        if (!separateAttributesClass
                                || member.getDeclaringClass().getPackage().getName().equals(packageOfAttributesClass)) {
                            sb.append("\n\n");
                            sb.append(generateAttributeForMember(targetClass, member));
                            membersEncountered.add(member.getName());
                        }
                    }
                    else if (Modifier.isPublic(modifiers)) {
                        sb.append("\n\n");
                        sb.append(generateAttributeForMember(targetClass, member));
                        membersEncountered.add(member.getName());
                    }
                    else {
                        // Package-private member.
                        // Generate attribute for this package-private member, if the attribute will be pasted into the target
                        // class and the member is in that class, or if the attribute will be generated
                        // into a separate attributes class in the same package as the class containing the package-private
                        // member (which may be a superclass of the target class)...
                        if ((!separateAttributesClass && currentClass.equals(targetClass))
                                || member.getDeclaringClass().getPackage().getName().equals(packageOfAttributesClass)) {
                            sb.append("\n\n");
                            sb.append(generateAttributeForMember(targetClass, member));
                            membersEncountered.add(member.getName());
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

    static String generateAttributeForMember(Class<?> enclosingClass, Member member) {
        try {
            MemberType memberType = getMemberType(member);
            if (getType(member).isPrimitive()) {
                return generateSimpleAttribute(enclosingClass.getSimpleName(), PRIMITIVES_TO_WRAPPERS.get(getType(member)).getSimpleName(), member.getName(), memberType);
            }
            else if (Iterable.class.isAssignableFrom(getType(member))) {
                ParameterizedType parameterizedType = getGenericType(member);
                if (parameterizedType.getActualTypeArguments().length != 1) {
                    throw new UnsupportedOperationException();
                }
                Class<?> genericType = (Class<?>)parameterizedType.getActualTypeArguments()[0];
                return generateMultiValueNullableAttributeForIterable(enclosingClass.getSimpleName(), genericType.getSimpleName(), member.getName(), memberType);
            }
            else if (getType(member).isArray()) {
                if (getType(member).getComponentType().isPrimitive()) {
                    return generateMultiValueNullableAttributeForPrimitiveArray(enclosingClass.getSimpleName(), PRIMITIVES_TO_WRAPPERS.get(getType(member).getComponentType()).getSimpleName(), getType(member).getComponentType().getSimpleName(), member.getName(), memberType);
                }
                else {
                    return generateMultiValueNullableAttributeForObjectArray(enclosingClass.getSimpleName(), getType(member).getComponentType().getSimpleName(), member.getName(), memberType);
                }
            }
            else {
                return generateSimpleNullableAttribute(enclosingClass.getSimpleName(), getType(member).getSimpleName(), member.getName(), memberType);
            }
        }
        catch (Exception e) {
            return "    // *** Note: Could not generate CQEngine attribute automatically for member: " + enclosingClass.getSimpleName() + "." + member.getName() + " ***";
        }
    }

    static String generateSimpleAttribute(String objectType, String attributeType, String memberName, MemberType memberType) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing " + memberType.description + " {@code " + objectType + "." + memberName + memberType.accessSuffix + "}.\n" +
                "     */\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(memberName) + " = new SimpleAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(memberName) + "\") {\n" +
                "        public " + attributeType + " getValue(" + objectType + " " + objectType.toLowerCase() + ", QueryOptions queryOptions) { return " + objectType.toLowerCase() + "." + memberName + memberType.accessSuffix + "; }\n" +
                "    };";
    }

    static String generateSimpleNullableAttribute(String objectType, String attributeType, String memberName, MemberType memberType) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing " + memberType.description + " {@code " + objectType + "." + memberName + memberType.accessSuffix + "}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if this " + memberType.description + " cannot " + memberType.produce + " null, replace this SimpleNullableAttribute with a SimpleAttribute\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(memberName) + " = new SimpleNullableAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(memberName) + "\") {\n" +
                "        public " + attributeType + " getValue(" + objectType + " " + objectType.toLowerCase() + ", QueryOptions queryOptions) { return " + objectType.toLowerCase() + "." + memberName + memberType.accessSuffix + "; }\n" +
                "    };";
    }
    
    static String generateMultiValueNullableAttributeForIterable(String objectType, String attributeType, String memberName, MemberType memberType) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing " + memberType.description + " {@code " + objectType + "." + memberName + memberType.accessSuffix + "}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if the collection cannot contain null elements change true to false in the following constructor, or\n" +
                "    // - if the collection cannot contain null elements AND the " + memberType.description + " itself cannot " + memberType.produce + " null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(memberName) + " = new MultiValueNullableAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(memberName) + "\", true) {\n" +
                "        public Iterable<" + attributeType + "> getNullableValues(" + objectType + " " + objectType.toLowerCase() + ", QueryOptions queryOptions) { return " + objectType.toLowerCase() + "." + memberName + memberType.accessSuffix + "; }\n" +
                "    };";
    }

    static String generateMultiValueNullableAttributeForObjectArray(String objectType, String attributeType, String memberName, MemberType memberType) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing " + memberType.description + " {@code " + objectType + "." + memberName + memberType.accessSuffix + "}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if the array cannot contain null elements change true to false in the following constructor, or\n" +
                "    // - if the array cannot contain null elements AND the " + memberType.description + " itself cannot " + memberType.produce + " null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(memberName) + " = new MultiValueNullableAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(memberName) + "\", true) {\n" +
                "        public Iterable<" + attributeType + "> getNullableValues(" + objectType + " " + objectType.toLowerCase() + ", QueryOptions queryOptions) { return Arrays.asList(" + objectType.toLowerCase() + "." + memberName + memberType.accessSuffix + "); }\n" +
                "    };";
    }

    static String generateMultiValueNullableAttributeForPrimitiveArray(String objectType, String attributeType, String primitiveType, String memberName, MemberType memberType) {
        return "    /**\n" +
                "     * CQEngine attribute for accessing " + memberType.description + " {@code " + objectType + "." + memberName + memberType.accessSuffix + "}.\n" +
                "     */\n" +
                "    // Note: For best performance:\n" +
                "    // - if this " + memberType.description + " cannot " + memberType.produce + " null, replace this\n" +
                "    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())\n" +
                "    public static final Attribute<" + objectType + ", " + attributeType + "> " + toUpperCaseWithUnderscores(memberName) + " = new MultiValueNullableAttribute<" + objectType + ", " + attributeType + ">(\"" + toUpperCaseWithUnderscores(memberName) + "\", false) {\n" +
                "        public Iterable<" + attributeType + "> getNullableValues(final " + objectType + " " + objectType.toLowerCase() + ", QueryOptions queryOptions) {\n" +
                "            return new AbstractList<" + attributeType + ">() {\n" +
                "                public " + attributeType + " get(int i) { return " + objectType.toLowerCase() + "." + memberName + memberType.accessSuffix + "[i]; }\n" +
                "                public int size() { return " + objectType.toLowerCase() + "." + memberName + memberType.accessSuffix + ".length; }\n" +
                "            };\n" +
                "        }\n" +
                "    };";
    }

    enum MemberType {
        FIELD("field", "", "be"),
        METHOD("method", "()", "return");

        public final String description;
        public final String accessSuffix;
        public final String produce;

        MemberType(String description, String accessSuffix, String produce) {
            this.description = description;
            this.accessSuffix = accessSuffix;
            this.produce = produce;
        }

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

    static List<Member> getMembers(Class currentClass) {
        List<Member> declaredMembers = new ArrayList<Member>();
        for (Field member : currentClass.getDeclaredFields()) {
            if (!member.isSynthetic()) {
                declaredMembers.add(member);
            }
        }
        for (Method member : currentClass.getDeclaredMethods()) {
            if (!member.isSynthetic()
                    && !member.isBridge()
                    && !member.getReturnType().equals(Void.TYPE)
                    && member.getParameterTypes().length == 0) {
                declaredMembers.add(member);
            }
        }
        return declaredMembers;
    }

    static MemberType getMemberType(Member member) {
        if (member instanceof Field) {
            return MemberType.FIELD;
        }
        else if (member instanceof Method) {
            return MemberType.METHOD;
        }
        else throw new IllegalStateException("Unsupported member type: " + member);
    }

    static Class<?> getType(Member member) {
        if (member instanceof Field) {
            return ((Field) member).getType();
        }
        else if (member instanceof Method) {
            return ((Method) member).getReturnType();
        }
        else throw new IllegalStateException("Unsupported member type: " + member);
    }

    static ParameterizedType getGenericType(Member member) {
        if (member instanceof Field) {
            return (ParameterizedType)((Field) member).getGenericType();
        }
        else if (member instanceof Method) {
            return (ParameterizedType)((Method) member).getGenericReturnType();
        }
        else throw new IllegalStateException("Unsupported member type: " + member);
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
    AttributeSourceGenerator() {
    }
}
