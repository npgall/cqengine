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

import com.googlecode.cqengine.attribute.*;
import com.googlecode.cqengine.codegen.support.GeneratedAttributeSupport;
import com.googlecode.cqengine.query.option.QueryOptions;
import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.SignatureAttribute;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

import static com.googlecode.cqengine.codegen.AttributeSourceGenerator.*;
import static com.googlecode.cqengine.codegen.MemberFilters.FIELDS_ONLY;
import static java.lang.reflect.Modifier.*;

/**
 * Generates Attributes by synthesizing bytecode, avoiding the need to write attributes by hand.
 * The synthesized attributes should perform as well at runtime as hand-written ones in most cases.
 * <p/>
 * Generated attributes are loaded into the ClassLoader of the given POJO classes.
 * <p/>
 * See {@link MemberFilter} and {@link MemberFilters} for some common filters which can be used with this class
 * to determine the subset of fields or methods in a POJO for which attributes should be generated.
 */
public class AttributeBytecodeGenerator {

    /**
     * Auto-generates and instantiates a set of attributes which read values from the fields in the given POJO class.
     * <p>
     * This is equivalent to calling {@link #createAttributes(Class, MemberFilter)} with
     * {@link MemberFilters#FIELDS_ONLY}.
     *
     * @param pojoClass The POJO class containing fields for which attributes are to be created
     * @return A map of field/attribute names to Attribute objects which read values from the fields in the given POJO
     * class
     */
    public static <O> Map<String, ? extends Attribute<O, ?>> createAttributes(Class<O> pojoClass) {
        return createAttributes(pojoClass, FIELDS_ONLY);
    }

    /**
     * Auto-generates and instantiates a set of attributes which read values from the members (fields or methods)
     * in the given POJO class.
     * <p>
     * This is equivalent to calling {@link #createAttributes(Class, MemberFilter, Function)} with
     * {@link MemberFilters#FIELDS_ONLY} and {@link AttributeNameProducers#USE_MEMBER_NAMES_VERBATIM}.
     *
     * @param pojoClass The POJO class containing fields for which attributes are to be created
     * @param memberFilter A filter which determines the subset of the members of a class (fields and methods)
     * for which attributes should be generated
     * @return A map of field/attribute names to Attribute objects which read values from the members in the given POJO
     * class
     */
    @SuppressWarnings("unchecked")
    public static <O> Map<String, ? extends Attribute<O, ?>> createAttributes(Class<O> pojoClass, MemberFilter memberFilter) {
        return createAttributes(pojoClass, memberFilter, AttributeNameProducers.USE_MEMBER_NAMES_VERBATIM);
    }

    /**
     * Auto-generates and instantiates a set of attributes which read values from the members (fields or methods)
     * in the given POJO class.
     * <p>
     * By default, attributes will be generated for all non-private members declared directly in the POJO class,
     * and for inherited members as well, as long as the access modifiers on inherited members in their superclass(es)
     * allow those members to be accessed from the package of the POJO class. So if the POJO class is in the same
     * package as a superclass, then attributes will be generated for package-private, protected, and public members
     * in the superclass. If the POJO class is in a different package from the superclass, then attributes will only be
     * generated for protected and public members in the superclass.
     *
     * @param pojoClass The POJO class containing fields for which attributes are to be created
     * @param memberFilter A filter which determines the subset of the members of a class (fields and methods)
     * for which attributes should be generated
     * @param attributeNameProducer A function which generates a name for an attribute, given the {@link Member}
     * for which the attribute will be generated
     * @return A map of field/attribute names to Attribute objects which read values from the members in the given POJO
     * class
     */
    @SuppressWarnings("unchecked")
    public static <O> Map<String, ? extends Attribute<O, ?>> createAttributes(Class<O> pojoClass, MemberFilter memberFilter, Function<Member, String> attributeNameProducer) {
        final Map<String, Attribute<O, ?>> attributes = new TreeMap<String, Attribute<O, ?>>();
        Class currentClass = pojoClass;
        Set<String> membersEncountered = new HashSet<String>();
        while (currentClass != null && currentClass != Object.class) {
            for (Member member : getMembers(currentClass)) {
                try {
                    if (!memberFilter.accept(member)) {
                        continue;
                    }
                    if (membersEncountered.contains(member.getName())) {
                        // We already generated an attribute for a member with this name in a subclass of the current class...
                        continue;
                    }
                    int modifiers = member.getModifiers();
                    String memberName = member.getName();
                    String attributeName = attributeNameProducer.apply(member);
                    Class<?> memberType = getType(member);

                    Class<? extends Attribute<O, ?>> attributeClass;
                    if (!isStatic(modifiers) && !isPrivate(modifiers)) {
                        // If the member is declared in a superclass in a different package,
                        // only generate an attribute for it, if the member is actually accessible from the subclass;
                        // that is it is declared public or protected...
                        if (!member.getDeclaringClass().getPackage().getName().equals(pojoClass.getPackage().getName())
                                && !(isProtected(modifiers) || isPublic(modifiers))) {
                            continue;
                        }
                        if (memberType.isPrimitive()) {
                            Class<?> wrapperType = getWrapperForPrimitive(memberType);
                            attributeClass = (member instanceof Method)
                                    ? generateSimpleAttributeForGetter(pojoClass, wrapperType, memberName, attributeName)
                                    : generateSimpleAttributeForField(pojoClass, wrapperType, memberName, attributeName);
                        } else if (Iterable.class.isAssignableFrom(memberType)) {
                            ParameterizedType parameterizedType = getGenericType(member);
                            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                            if (actualTypeArguments.length != 1) {
                                throw new UnsupportedOperationException();
                            }
                            Class<?> genericType = (Class<?>) actualTypeArguments[0];
                            attributeClass = (member instanceof Method)
                                    ? generateMultiValueNullableAttributeForGetter(pojoClass, genericType, memberName, true, attributeName)
                                    : generateMultiValueNullableAttributeForField(pojoClass, genericType, memberName, true, attributeName);
                        } else if (memberType.isArray()) {
                            Class<?> componentType = memberType.getComponentType();
                            if (componentType.isPrimitive()) {
                                Class<?> wrapperType = getWrapperForPrimitive(componentType);
                                attributeClass = (member instanceof Method)
                                        ? generateMultiValueNullableAttributeForGetter(pojoClass, wrapperType, memberName, false, attributeName)
                                        : generateMultiValueNullableAttributeForField(pojoClass, wrapperType, memberName, false, attributeName);
                            }
                            else {
                                attributeClass = (member instanceof Method)
                                        ? generateMultiValueNullableAttributeForGetter(pojoClass, componentType, memberName, true, attributeName)
                                        : generateMultiValueNullableAttributeForField(pojoClass, componentType, memberName, true, attributeName);
                            }
                        } else {
                            attributeClass = (member instanceof Method)
                                    ? generateSimpleNullableAttributeForGetter(pojoClass, memberType, memberName, attributeName)
                                    : generateSimpleNullableAttributeForField(pojoClass, memberType, memberName, attributeName);
                        }
                        Attribute<O, ?> attribute = attributeClass.newInstance();
                        attributes.put(attribute.getAttributeName(), attribute);
                        membersEncountered.add(member.getName());
                    }
                } catch (Throwable e) {
                    throw new IllegalStateException("Failed to create attribute for member: " + member.toString(), e);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return attributes;
    }

    /**
     * Generates a {@link SimpleAttribute} which reads a non-null value from the given field in a POJO.
     * <p>
     * The type of value returned by the attribute should match the type of the field. However reading primitive values
     * is also supported. In that case, the attribute will box the primitive value to its wrapper type automatically,
     * and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the field
     * @param attributeValueType The type of value returned by the attribute
     * @param fieldName          The name of the field
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends SimpleAttribute<O, A>> generateSimpleAttributeForField(Class<O> pojoClass, Class<A> attributeValueType, String fieldName, String attributeName) {
        ensureFieldExists(pojoClass, attributeValueType, fieldName, attributeName);
        return generateSimpleAttribute(SimpleAttribute.class, pojoClass, attributeValueType, attributeName, "object." + fieldName);
    }

    /**
     * Generates a {@link SimpleAttribute} which reads a non-null value from the given getter method in a POJO.
     * The getter method should not take any arguments.
     * <p>
     * The type of value returned by the attribute should match the return type of the getter. However reading from
     * getters which return primitive values is also supported. In that case, the attribute will box the primitive value
     * to its wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName   The name of the getter method
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends SimpleAttribute<O, A>> generateSimpleAttributeForGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String attributeName) {
        ensureGetterExists(pojoClass, attributeValueType, getterMethodName, attributeName);
        return generateSimpleAttribute(SimpleAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "()");
    }

    /**
     * Generates a {@link SimpleAttribute} which reads a non-null value from the given <i>parameterized</i> getter
     * method in a POJO. The getter method should take a single string argument.
     * <p>
     * The type of value returned by the attribute should match the return type of the getter. However reading from
     * getters which return primitive values is also supported. In that case, the attribute will box the primitive value
     * to its wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName   The name of the getter method
     * @param getterParameter    The string argument to supply to the getter method
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends SimpleAttribute<O, A>> generateSimpleAttributeForParameterizedGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String getterParameter, String attributeName) {
        ensureParameterizedGetterExists(pojoClass, attributeValueType, getterMethodName, getterParameter, attributeName);
        return generateSimpleAttribute(SimpleAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "(\"" + getterParameter + "\")");
    }

    /**
     * Generates a {@link SimpleNullableAttribute} which reads a possibly-null value from the given field in a POJO.
     * <p>
     * The type of value returned by the attribute should match the type of the field. However reading primitive values
     * is also supported. In that case, the attribute will box the primitive value to its wrapper type automatically,
     * and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the field
     * @param attributeValueType The type of value returned by the attribute
     * @param fieldName          The name of the field
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends SimpleNullableAttribute<O, A>> generateSimpleNullableAttributeForField(Class<O> pojoClass, Class<A> attributeValueType, String fieldName, String attributeName) {
        ensureFieldExists(pojoClass, attributeValueType, fieldName, attributeName);
        return generateSimpleAttribute(SimpleNullableAttribute.class, pojoClass, attributeValueType, attributeName, "object." + fieldName);
    }

    /**
     * Generates a {@link SimpleNullableAttribute} which reads a possibly-null value from the given getter method in a
     * POJO. The getter method should not take any arguments.
     * <p>
     * The type of value returned by the attribute should match the return type of the getter. However reading from
     * getters which return primitive values is also supported. In that case, the attribute will box the primitive value
     * to its wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName   The name of the getter method
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends SimpleNullableAttribute<O, A>> generateSimpleNullableAttributeForGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String attributeName) {
        ensureGetterExists(pojoClass, attributeValueType, getterMethodName, attributeName);
        return generateSimpleAttribute(SimpleNullableAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "()");
    }

    /**
     * Generates a {@link SimpleNullableAttribute} which reads a possibly-null value from the given <i>parameterized</i>
     * getter method in a POJO. The getter method should take a single string argument.
     * <p>
     * The type of value returned by the attribute should match the return type of the getter. However reading from
     * getters which return primitive values is also supported. In that case, the attribute will box the primitive value
     * to its wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName   The name of the getter method
     * @param getterParameter    The string argument to supply to the getter method
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends SimpleNullableAttribute<O, A>> generateSimpleNullableAttributeForParameterizedGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String getterParameter, String attributeName) {
        ensureParameterizedGetterExists(pojoClass, attributeValueType, getterMethodName, getterParameter, attributeName);
        return generateSimpleAttribute(SimpleNullableAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "(\"" + getterParameter + "\")");
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads non-null values from the given field in a POJO.
     * <p>
     * The type of values returned by the attribute should match the type of values stored in the field. The field may
     * be a List or an array, of objects of the same type as the attribute. However reading from primitive arrays is
     * also supported. In that case, the attribute will box the primitive values to their wrapper type automatically,
     * and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the field
     * @param attributeValueType The type of values returned by the attribute
     * @param fieldName          The name of the field
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of values returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends MultiValueAttribute<O, A>> generateMultiValueAttributeForField(Class<O> pojoClass, Class<A> attributeValueType, String fieldName, String attributeName) {
        ensureFieldExists(pojoClass, attributeValueType, fieldName, attributeName);
        return generateMultiValueAttribute(MultiValueAttribute.class, pojoClass, attributeValueType, attributeName, "object." + fieldName);
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads non-null values from the given getter method in a POJO.
     * The getter method should not take any arguments.
     * <p>
     * The type of values returned by the attribute should match the type of values returned by the getter. The getter
     * may return a List or an array, of objects of the same type as the attribute. However reading from getters which
     * return primitive arrays is also supported. In that case, the attribute will box the primitive values to their
     * wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName   The name of the getter method
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends MultiValueAttribute<O, A>> generateMultiValueAttributeForGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String attributeName) {
        ensureGetterExists(pojoClass, attributeValueType, getterMethodName, attributeName);
        return generateMultiValueAttribute(MultiValueAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "()");
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads non-null values from the given <i>parameterized</i> getter
     * method in a POJO. The getter method should take a single string argument.
     * <p>
     * The type of values returned by the attribute should match the type of values returned by the getter. The getter
     * may return a List or an array, of objects of the same type as the attribute. However reading from getters which
     * return primitive arrays is also supported. In that case, the attribute will box the primitive values to their
     * wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName   The name of the getter method
     * @param getterParameter    The string argument to supply to the getter method
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends MultiValueAttribute<O, A>> generateMultiValueAttributeForParameterizedGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String getterParameter, String attributeName) {
        ensureParameterizedGetterExists(pojoClass, attributeValueType, getterMethodName, getterParameter, attributeName);
        return generateMultiValueAttribute(MultiValueAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "(\"" + getterParameter + "\")");
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads possibly-null values from the given field in a POJO.
     * <p>
     * The type of values returned by the attribute should match the type of values stored in the field. The field may
     * be a List or an array, of objects of the same type as the attribute. However reading from primitive arrays is
     * also supported. In that case, the attribute will box the primitive values to their wrapper type automatically,
     * and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the field
     * @param attributeValueType The type of values returned by the attribute
     * @param fieldName          The name of the field
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of values returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends MultiValueNullableAttribute<O, A>> generateMultiValueNullableAttributeForField(Class<O> pojoClass, Class<A> attributeValueType, String fieldName, boolean componentValuesNullable, String attributeName) {
        ensureFieldExists(pojoClass, attributeValueType, fieldName, attributeName);
        return generateMultiValueNullableAttribute(MultiValueNullableAttribute.class, pojoClass, attributeValueType, attributeName, componentValuesNullable, "object." + fieldName);
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads possibly-null values from the given getter method in a POJO.
     * The getter method should not take any arguments.
     * <p>
     * The type of values returned by the attribute should match the type of values returned by the getter. The getter
     * may return a List or an array, of objects of the same type as the attribute. However reading from getters which
     * return primitive arrays is also supported. In that case, the attribute will box the primitive values to their
     * wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName   The name of the getter method
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends MultiValueNullableAttribute<O, A>> generateMultiValueNullableAttributeForGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, boolean componentValuesNullable, String attributeName) {
        ensureGetterExists(pojoClass, attributeValueType, getterMethodName, attributeName);
        return generateMultiValueNullableAttribute(MultiValueNullableAttribute.class, pojoClass, attributeValueType, attributeName, componentValuesNullable, "object." + getterMethodName + "()");
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads possibly-null values from the given <i>parameterized</i>
     * getter method in a POJO. The getter method should take a single string argument.
     * <p>
     * The type of values returned by the attribute should match the type of values returned by the getter. The getter
     * may return a List or an array, of objects of the same type as the attribute. However reading from getters which
     * return primitive arrays is also supported. In that case, the attribute will box the primitive values to their
     * wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass          The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName   The name of the getter method
     * @param getterParameter    The string argument to supply to the getter method
     * @param attributeName      The name to give to the attribute
     * @param <O>                Type of the POJO object
     * @param <A>                The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    @SuppressWarnings("unchecked")
    public static <O, A> Class<? extends MultiValueNullableAttribute<O, A>> generateMultiValueNullableAttributeForParameterizedGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String getterParameter, boolean componentValuesNullable, String attributeName) {
        ensureParameterizedGetterExists(pojoClass, attributeValueType, getterMethodName, getterParameter, attributeName);
        return generateMultiValueNullableAttribute(MultiValueNullableAttribute.class, pojoClass, attributeValueType, attributeName, componentValuesNullable, "object." + getterMethodName + "(\"" + getterParameter + "\")");
    }

    /**
     * Helper method for generating SimpleAttribute AND SimpleNullableAttribute.
     *
     * @param target Snippet of code which reads the value, such as: <code>object.fieldName</code>, <code>object.getFoo()</code>, or <code>object.getFoo("bar")</code>
     */
    private static <O, A, C extends Attribute<O, A>, R extends Class<? extends C>> R generateSimpleAttribute(Class<C> attributeSuperClass, Class<O> pojoClass, Class<A> attributeValueType, String attributeName, String target) {
        try {
            ClassPool pool = new ClassPool(false);
            pool.appendClassPath(new ClassClassPath(pojoClass));

            CtClass attributeClass = pool.makeClass(pojoClass.getName() + "$$CQEngine_" + attributeSuperClass.getSimpleName() + "_" + attributeName);
            attributeClass.setSuperclass(pool.get(attributeSuperClass.getName()));

            SignatureAttribute.ClassType genericTypeOfAttribute = new SignatureAttribute.ClassType(
                    attributeSuperClass.getName(),
                    new SignatureAttribute.TypeArgument[]{
                            new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(pojoClass.getName())),
                            new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(attributeValueType.getName()))
                    }
            );
            attributeClass.setGenericSignature(genericTypeOfAttribute.encode());

            // Add a no-arg constructor which pass the attribute name to the superclass...
            CtConstructor constructor = CtNewConstructor.make(
                    "public " + attributeClass.getSimpleName() + "() { "
                            + "super(\"" + attributeName + "\");"
                            + " }", attributeClass);
            attributeClass.addConstructor(constructor);

            // Add the getter method...
            CtMethod getterMethod = CtMethod.make(
                    "public " + attributeValueType.getName() + " getValue(" + pojoClass.getName() + " object, " + QueryOptions.class.getName() + " queryOptions) { "
                            + "return (" + attributeValueType.getName() + ") " + GeneratedAttributeSupport.class.getName() + ".valueOf(" + target + ");"
                            + " }", attributeClass);
            attributeClass.addMethod(getterMethod);

            // Add a bridge method for the getter method to account for type erasure (see https://docs.oracle.com/javase/tutorial/java/generics/bridgeMethods.html)...
            CtMethod getterBridgeMethod = CtMethod.make(
                    "public java.lang.Object getValue(java.lang.Object object, " + QueryOptions.class.getName() + " queryOptions) { "
                            + "return getValue((" + pojoClass.getName() + ")object, queryOptions);"
                            + " }", attributeClass);
            getterBridgeMethod.setModifiers(getterBridgeMethod.getModifiers() | AccessFlag.BRIDGE);
            attributeClass.addMethod(getterBridgeMethod);

            @SuppressWarnings("unchecked")
            R result = (R) attributeClass.toClass(pojoClass.getClassLoader(), pojoClass.getProtectionDomain());
            attributeClass.detach();
            return result;
        } catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    /**
     * Helper method for generating MultiValueAttribute.
     *
     * @param target Snippet of code which reads the value, such as: <code>object.fieldName</code>, <code>object.getFoo()</code>, or <code>object.getFoo("bar")</code>
     */
    private static <O, A, C extends MultiValueAttribute<O, A>, R extends Class<? extends C>> R generateMultiValueAttribute(Class<C> attributeSuperClass, Class<O> pojoClass, Class<A> attributeValueType, String attributeName, String target) {
        try {
            ClassPool pool = new ClassPool(false);
            pool.appendClassPath(new ClassClassPath(pojoClass));

            CtClass attributeClass = pool.makeClass(pojoClass.getName() + "$$CQEngine_" + attributeSuperClass.getSimpleName() + "_" + attributeName);
            attributeClass.setSuperclass(pool.get(attributeSuperClass.getName()));

            SignatureAttribute.ClassType genericTypeOfAttribute = new SignatureAttribute.ClassType(
                    attributeSuperClass.getName(),
                    new SignatureAttribute.TypeArgument[]{
                            new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(pojoClass.getName())),
                            new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(attributeValueType.getName()))
                    }
            );
            attributeClass.setGenericSignature(genericTypeOfAttribute.encode());

            // Add a no-arg constructor which pass the attribute name to the superclass...
            CtConstructor constructor = CtNewConstructor.make(
                    "public " + attributeClass.getSimpleName() + "() { "
                            + "super(\"" + attributeName + "\");"
                            + " }", attributeClass);
            attributeClass.addConstructor(constructor);

            // Add the getter method...
            CtMethod getterMethod = CtMethod.make(
                    "public java.lang.Iterable getValues(" + pojoClass.getName() + " object, " + QueryOptions.class.getName() + " queryOptions) { "
                            + "return " + GeneratedAttributeSupport.class.getName() + ".valueOf(" + target + ");"
                            + " }", attributeClass);

            getterMethod.setGenericSignature(new SignatureAttribute.MethodSignature(
                    new SignatureAttribute.TypeParameter[0],
                    new SignatureAttribute.Type[]{new SignatureAttribute.ClassType(pojoClass.getName())},
                    new SignatureAttribute.ClassType(
                            java.lang.Iterable.class.getName(),
                            new SignatureAttribute.TypeArgument[]{
                                    new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(attributeValueType.getName()))
                            }
                    ),
                    new SignatureAttribute.ObjectType[0]
            ).encode());
            attributeClass.addMethod(getterMethod);

            // Add a bridge method for the getter method to account for type erasure (see https://docs.oracle.com/javase/tutorial/java/generics/bridgeMethods.html)...
            CtMethod getterBridgeMethod = CtMethod.make(
                    "public java.lang.Iterable getValues(java.lang.Object object, " + QueryOptions.class.getName() + " queryOptions) { "
                            + "return getValues((" + pojoClass.getName() + ")object, queryOptions);"
                            + " }", attributeClass);
            getterBridgeMethod.setModifiers(getterBridgeMethod.getModifiers() | AccessFlag.BRIDGE);
            attributeClass.addMethod(getterBridgeMethod);

            @SuppressWarnings("unchecked")
            R result = (R) attributeClass.toClass(pojoClass.getClassLoader(), pojoClass.getProtectionDomain());
            attributeClass.detach();
            return result;
        } catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    /**
     * Helper method for generating MultiValueNullableAttribute.
     *
     * @param target Snippet of code which reads the value, such as: <code>object.fieldName</code>, <code>object.getFoo()</code>, or <code>object.getFoo("bar")</code>
     */
    private static <O, A, C extends MultiValueNullableAttribute<O, A>, R extends Class<? extends C>> R generateMultiValueNullableAttribute(Class<C> attributeSuperClass, Class<O> pojoClass, Class<A> attributeValueType, String attributeName, boolean componentValuesNullable, String target) {
        try {
            ClassPool pool = new ClassPool(false);
            pool.appendClassPath(new ClassClassPath(pojoClass));

            CtClass attributeClass = pool.makeClass(pojoClass.getName() + "$$CQEngine_" + attributeSuperClass.getSimpleName() + "_" + attributeName);
            attributeClass.setSuperclass(pool.get(attributeSuperClass.getName()));

            SignatureAttribute.ClassType genericTypeOfAttribute = new SignatureAttribute.ClassType(
                    attributeSuperClass.getName(),
                    new SignatureAttribute.TypeArgument[]{
                            new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(pojoClass.getName())),
                            new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(attributeValueType.getName()))
                    }
            );
            attributeClass.setGenericSignature(genericTypeOfAttribute.encode());

            // Add a no-arg constructor which pass the attribute name to the superclass...
            CtConstructor constructor = CtNewConstructor.make(
                    "public " + attributeClass.getSimpleName() + "() { "
                            + "super(\"" + attributeName + "\", " + componentValuesNullable + ");"
                            + " }", attributeClass);
            attributeClass.addConstructor(constructor);

            // Add the getter method...
            CtMethod getterMethod = CtMethod.make(
                    "public java.lang.Iterable getNullableValues(" + pojoClass.getName() + " object, " + QueryOptions.class.getName() + " queryOptions) { "
                            + "return " + GeneratedAttributeSupport.class.getName() + ".valueOf(" + target + ");"
                            + " }", attributeClass);

            getterMethod.setGenericSignature(new SignatureAttribute.MethodSignature(
                    new SignatureAttribute.TypeParameter[0],
                    new SignatureAttribute.Type[]{new SignatureAttribute.ClassType(pojoClass.getName())},
                    new SignatureAttribute.ClassType(
                            java.lang.Iterable.class.getName(),
                            new SignatureAttribute.TypeArgument[]{
                                    new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(attributeValueType.getName()))
                            }
                    ),
                    new SignatureAttribute.ObjectType[0]
            ).encode());
            attributeClass.addMethod(getterMethod);

            // Add a bridge method for the getter method to account for type erasure (see https://docs.oracle.com/javase/tutorial/java/generics/bridgeMethods.html)...
            CtMethod getterBridgeMethod = CtMethod.make(
                    "public java.lang.Iterable getNullableValues(java.lang.Object object, " + QueryOptions.class.getName() + " queryOptions) { "
                            + "return getNullableValues((" + pojoClass.getName() + ")object, queryOptions);"
                            + " }", attributeClass);
            getterBridgeMethod.setModifiers(getterBridgeMethod.getModifiers() | AccessFlag.BRIDGE);
            attributeClass.addMethod(getterBridgeMethod);

            @SuppressWarnings("unchecked")
            R result = (R) attributeClass.toClass(pojoClass.getClassLoader(), pojoClass.getProtectionDomain());
            attributeClass.detach();
            return result;
        } catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    static String getClassName(Class<?> cls) {
        return cls != null ? cls.getName() : null;
    }

    static void ensureFieldExists(Class<?> pojoClass, Class<?> attributeValueType, String fieldName, String attributeName) {
        try {
            // Validate that the field exists...
            while (pojoClass != null) {
                try {
                    pojoClass.getDeclaredField(fieldName);
                    return;
                } catch (NoSuchFieldException e) {
                    pojoClass = pojoClass.getSuperclass();
                }
            }
            throw new NoSuchFieldException(fieldName);
        } catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    static void ensureGetterExists(Class<?> pojoClass, Class<?> attributeValueType, String getterMethodName, String attributeName) {
        try {
            while (pojoClass != null) {
                try {
                    pojoClass.getDeclaredMethod(getterMethodName);
                    return;
                } catch (NoSuchMethodException e) {
                    pojoClass = pojoClass.getSuperclass();
                }
            }
            throw new NoSuchMethodException(getterMethodName);
        } catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    static void ensureParameterizedGetterExists(Class<?> pojoClass, Class<?> attributeValueType, String parameterizedGetterMethodName, String getterParameter, String attributeName) {
        try {
            if (getterParameter.contains("\"") || getterParameter.contains("\\")) {
                throw new IllegalArgumentException("Getter parameter contains unsupported characters: " + getterParameter);
            }
            while (pojoClass != null) {
                try {
                    pojoClass.getDeclaredMethod(parameterizedGetterMethodName, String.class);
                    return;
                } catch (NoSuchMethodException e) {
                    pojoClass = pojoClass.getSuperclass();
                }
            }
            throw new NoSuchMethodException(parameterizedGetterMethodName + "(String)");
        } catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    static String getExceptionMessage(Class<?> pojoClass, Class<?> attributeValueType, String attributeName) {
        return "Failed to generate attribute for class " + getClassName(pojoClass) + ", type " + getClassName(attributeValueType) + ", name '" + attributeName + "'";
    }

    static Class<?> getWrapperForPrimitive(Class<?> primitiveType) {

        Class<?> wrapperType = PRIMITIVES_TO_WRAPPERS.get(primitiveType);
        if (wrapperType == null) {
            throw new IllegalStateException("No wrapper type for primitive type: " + primitiveType);
        }
        return wrapperType;
    }

    static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new LinkedHashMap<Class<?>, Class<?>>() {{
        put(boolean.class, Boolean.class);
        put(byte.class, Byte.class);
        put(char.class, Character.class);
        put(double.class, Double.class);
        put(float.class, Float.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(short.class, Short.class);
        put(void.class, Void.class);
    }};
}
