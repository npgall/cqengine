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

/**
 * Generates Attributes by synthesizing bytecode, avoiding the need to write attributes by hand.
 * The synthesized attributes should perform as well at runtime as hand-written ones in most cases.
 * <p/>
 * Note this class uses the <i>javassist</i> library to generate bytecode, which is an <i>optional</i> dependency in
 * CQEngine's pom.xml. If you wish to use this class, ensure that you <b>re-declare the dependency on javassist</b>
 * again in your application's pom.xml.
 * <p/>
 * Generated attributes are loaded into the ClassLoader of the given POJO classes.
 */
public class AttributeBytecodeGenerator {

    /**
     * Generates a {@link SimpleAttribute} which reads a non-null value from the given field in a POJO.
     * <p/>
     * The type of value returned by the attribute should match the type of the field. However reading primitive values
     * is also supported. In that case, the attribute will box the primitive value to its wrapper type automatically,
     * and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the field
     * @param attributeValueType The type of value returned by the attribute
     * @param fieldName The name of the field
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends SimpleAttribute<O, A>> generateSimpleAttributeForField(Class<O> pojoClass, Class<A> attributeValueType, String fieldName, String attributeName) {
        ensureFieldExists(pojoClass, attributeValueType, fieldName, attributeName);
        return generateSimpleAttribute(SimpleAttribute.class, pojoClass, attributeValueType, attributeName, "object." + fieldName);
    }

    /**
     * Generates a {@link SimpleAttribute} which reads a non-null value from the given getter method in a POJO.
     * The getter method should not take any arguments.
     * <p/>
     * The type of value returned by the attribute should match the return type of the getter. However reading from
     * getters which return primitive values is also supported. In that case, the attribute will box the primitive value
     * to its wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName The name of the getter method
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends SimpleAttribute<O, A>> generateSimpleAttributeForGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String attributeName) {
        ensureGetterExists(pojoClass, attributeValueType, getterMethodName, attributeName);
        return generateSimpleAttribute(SimpleAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "()");
    }

    /**
     * Generates a {@link SimpleAttribute} which reads a non-null value from the given <i>parameterized</i> getter
     * method in a POJO. The getter method should take a single string argument.
     * <p/>
     * The type of value returned by the attribute should match the return type of the getter. However reading from
     * getters which return primitive values is also supported. In that case, the attribute will box the primitive value
     * to its wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName The name of the getter method
     * @param getterParameter The string argument to supply to the getter method
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends SimpleAttribute<O, A>> generateSimpleAttributeForParameterizedGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String getterParameter, String attributeName) {
        ensureParameterizedGetterExists(pojoClass, attributeValueType, getterMethodName, getterParameter, attributeName);
        return generateSimpleAttribute(SimpleAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "(\"" + getterParameter + "\")");
    }

    /**
     * Generates a {@link SimpleNullableAttribute} which reads a possibly-null value from the given field in a POJO.
     * <p/>
     * The type of value returned by the attribute should match the type of the field. However reading primitive values
     * is also supported. In that case, the attribute will box the primitive value to its wrapper type automatically,
     * and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the field
     * @param attributeValueType The type of value returned by the attribute
     * @param fieldName The name of the field
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends SimpleNullableAttribute<O, A>> generateSimpleNullableAttributeForField(Class<O> pojoClass, Class<A> attributeValueType, String fieldName, String attributeName) {
        ensureFieldExists(pojoClass, attributeValueType, fieldName, attributeName);
        return generateSimpleAttribute(SimpleNullableAttribute.class, pojoClass, attributeValueType, attributeName, "object." + fieldName);
    }

    /**
     * Generates a {@link SimpleNullableAttribute} which reads a possibly-null value from the given getter method in a
     * POJO. The getter method should not take any arguments.
     * <p/>
     * The type of value returned by the attribute should match the return type of the getter. However reading from
     * getters which return primitive values is also supported. In that case, the attribute will box the primitive value
     * to its wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName The name of the getter method
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends SimpleNullableAttribute<O, A>> generateSimpleNullableAttributeForGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String attributeName) {
        ensureGetterExists(pojoClass, attributeValueType, getterMethodName, attributeName);
        return generateSimpleAttribute(SimpleNullableAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "()");
    }

    /**
     * Generates a {@link SimpleNullableAttribute} which reads a possibly-null value from the given <i>parameterized</i>
     * getter method in a POJO. The getter method should take a single string argument.
     * <p/>
     * The type of value returned by the attribute should match the return type of the getter. However reading from
     * getters which return primitive values is also supported. In that case, the attribute will box the primitive value
     * to its wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName The name of the getter method
     * @param getterParameter The string argument to supply to the getter method
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends SimpleNullableAttribute<O, A>> generateSimpleNullableAttributeForParameterizedGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String getterParameter, String attributeName) {
        ensureParameterizedGetterExists(pojoClass, attributeValueType, getterMethodName, getterParameter, attributeName);
        return generateSimpleAttribute(SimpleNullableAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "(\"" + getterParameter + "\")");
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads non-null values from the given field in a POJO.
     * <p/>
     * The type of values returned by the attribute should match the type of values stored in the field. The field may
     * be a List or an array, of objects of the same type as the attribute. However reading from primitive arrays is
     * also supported. In that case, the attribute will box the primitive values to their wrapper type automatically,
     * and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the field
     * @param attributeValueType The type of values returned by the attribute
     * @param fieldName The name of the field
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of values returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends MultiValueAttribute<O, A>> generateMultiValueAttributeForField(Class<O> pojoClass, Class<A> attributeValueType, String fieldName, String attributeName) {
        ensureFieldExists(pojoClass, attributeValueType, fieldName, attributeName);
        return generateMultiValueAttribute(MultiValueAttribute.class, pojoClass, attributeValueType, attributeName, "object." + fieldName);
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads non-null values from the given getter method in a POJO.
     * The getter method should not take any arguments.
     * <p/>
     * The type of values returned by the attribute should match the type of values returned by the getter. The getter
     * may return a List or an array, of objects of the same type as the attribute. However reading from getters which
     * return primitive arrays is also supported. In that case, the attribute will box the primitive values to their
     * wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName The name of the getter method
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends MultiValueAttribute<O, A>> generateMultiValueAttributeForGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String attributeName) {
        ensureGetterExists(pojoClass, attributeValueType, getterMethodName, attributeName);
        return generateMultiValueAttribute(MultiValueAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "()");
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads non-null values from the given <i>parameterized</i> getter
     * method in a POJO. The getter method should take a single string argument.
     * <p/>
     * The type of values returned by the attribute should match the type of values returned by the getter. The getter
     * may return a List or an array, of objects of the same type as the attribute. However reading from getters which
     * return primitive arrays is also supported. In that case, the attribute will box the primitive values to their
     * wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName The name of the getter method
     * @param getterParameter The string argument to supply to the getter method
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends MultiValueAttribute<O, A>> generateMultiValueAttributeForParameterizedGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String getterParameter, String attributeName) {
        ensureParameterizedGetterExists(pojoClass, attributeValueType, getterMethodName, getterParameter, attributeName);
        return generateMultiValueAttribute(MultiValueAttribute.class, pojoClass, attributeValueType, attributeName, "object." + getterMethodName + "(\"" + getterParameter + "\")");
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads possibly-null values from the given field in a POJO.
     * <p/>
     * The type of values returned by the attribute should match the type of values stored in the field. The field may
     * be a List or an array, of objects of the same type as the attribute. However reading from primitive arrays is
     * also supported. In that case, the attribute will box the primitive values to their wrapper type automatically,
     * and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the field
     * @param attributeValueType The type of values returned by the attribute
     * @param fieldName The name of the field
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of values returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends MultiValueNullableAttribute<O, A>> generateMultiValueNullableAttributeForField(Class<O> pojoClass, Class<A> attributeValueType, String fieldName, boolean componentValuesNullable, String attributeName) {
        ensureFieldExists(pojoClass, attributeValueType, fieldName, attributeName);
        return generateMultiValueNullableAttribute(MultiValueNullableAttribute.class, pojoClass, attributeValueType, attributeName, componentValuesNullable, "object." + fieldName);
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads possibly-null values from the given getter method in a POJO.
     * The getter method should not take any arguments.
     * <p/>
     * The type of values returned by the attribute should match the type of values returned by the getter. The getter
     * may return a List or an array, of objects of the same type as the attribute. However reading from getters which
     * return primitive arrays is also supported. In that case, the attribute will box the primitive values to their
     * wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName The name of the getter method
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends MultiValueNullableAttribute<O, A>> generateMultiValueNullableAttributeForGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, boolean componentValuesNullable, String attributeName) {
        ensureGetterExists(pojoClass, attributeValueType, getterMethodName, attributeName);
        return generateMultiValueNullableAttribute(MultiValueNullableAttribute.class, pojoClass, attributeValueType, attributeName, componentValuesNullable, "object." + getterMethodName + "()");
    }

    /**
     * Generates a {@link MultiValueAttribute} which reads possibly-null values from the given <i>parameterized</i>
     * getter method in a POJO. The getter method should take a single string argument.
     * <p/>
     * The type of values returned by the attribute should match the type of values returned by the getter. The getter
     * may return a List or an array, of objects of the same type as the attribute. However reading from getters which
     * return primitive arrays is also supported. In that case, the attribute will box the primitive values to their
     * wrapper type automatically, and so the type of the attribute itself should be the wrapper type.
     *
     * @param pojoClass The class containing the getter method
     * @param attributeValueType The type of value returned by the attribute
     * @param getterMethodName The name of the getter method
     * @param getterParameter The string argument to supply to the getter method
     * @param attributeName The name to give to the attribute
     * @param <O> Type of the POJO object
     * @param <A> The type of value returned by the attribute
     * @return A generated class for an attribute which reads from the POJO as discussed above
     */
    public static <O, A> Class<? extends MultiValueNullableAttribute<O, A>> generateMultiValueNullableAttributeForParameterizedGetter(Class<O> pojoClass, Class<A> attributeValueType, String getterMethodName, String getterParameter, boolean componentValuesNullable, String attributeName) {
        ensureParameterizedGetterExists(pojoClass, attributeValueType, getterMethodName, getterParameter, attributeName);
        return generateMultiValueNullableAttribute(MultiValueNullableAttribute.class, pojoClass, attributeValueType, attributeName, componentValuesNullable, "object." + getterMethodName + "(\"" + getterParameter + "\")");
    }

    /**
     * Helper method for generating SimpleAttribute AND SimpleNullableAttribute.
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
                    new SignatureAttribute.TypeArgument[] {
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
        }
        catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    /**
     * Helper method for generating MultiValueAttribute.
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
                    new SignatureAttribute.TypeArgument[] {
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
                    new SignatureAttribute.Type[] { new SignatureAttribute.ClassType(pojoClass.getName())},
                    new SignatureAttribute.ClassType(
                            java.lang.Iterable.class.getName(),
                            new SignatureAttribute.TypeArgument[] {
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
        }
        catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    /**
     * Helper method for generating MultiValueNullableAttribute.
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
                    new SignatureAttribute.TypeArgument[] {
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
                    new SignatureAttribute.Type[] { new SignatureAttribute.ClassType(pojoClass.getName())},
                    new SignatureAttribute.ClassType(
                            java.lang.Iterable.class.getName(),
                            new SignatureAttribute.TypeArgument[] {
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
        }
        catch (Exception e) {
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
                }
                catch (NoSuchFieldException e) {
                    pojoClass = pojoClass.getSuperclass();
                }
            }
            throw new NoSuchFieldException(fieldName);
        }
        catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    static void ensureGetterExists(Class<?> pojoClass, Class<?> attributeValueType, String getterMethodName, String attributeName) {
        try {
            while (pojoClass != null) {
                try {
                    pojoClass.getDeclaredMethod(getterMethodName);
                    return;
                }
                catch (NoSuchMethodException e) {
                    pojoClass = pojoClass.getSuperclass();
                }
            }
            throw new NoSuchMethodException(getterMethodName);
        }
        catch (Exception e) {
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
                }
                catch (NoSuchMethodException e) {
                    pojoClass = pojoClass.getSuperclass();
                }
            }
            throw new NoSuchMethodException(parameterizedGetterMethodName + "(String)");
        }
        catch (Exception e) {
            throw new IllegalStateException(getExceptionMessage(pojoClass, attributeValueType, attributeName), e);
        }
    }

    static String getExceptionMessage(Class<?> pojoClass, Class<?> attributeValueType, String attributeName) {
        return "Failed to generate attribute for class " + getClassName(pojoClass) + ", type " + getClassName(attributeValueType) + ", name '" + attributeName + "'";
    }
}
