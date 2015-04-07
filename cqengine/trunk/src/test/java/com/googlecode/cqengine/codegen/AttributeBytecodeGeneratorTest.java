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
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.*;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AttributeBytecodeGeneratorTest {

    // ====== Tests for SimpleAttribute ======

    static class PojoWithField {
        final String foo = "bar";
    }

    static class PojoWithPrimitiveField {
        final int foo = 5;
    }

    static class PojoWithGetter {
        String getFoo() {
            return "bar";
        }
    }

    static class PojoWithParameterizedGetter {
        String getFoo(String name) {
            return "bar_" + name;
        }
    }

    @Test
    public void testGenerateSimpleAttributeForField() throws Exception {
        Class<? extends SimpleAttribute<PojoWithField, String>> attributeClass = generateSimpleAttributeForField(PojoWithField.class, String.class, "foo", "foo");
        SimpleAttribute<PojoWithField, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, PojoWithField.class, String.class, "foo", new PojoWithField(), asList("bar"));
    }

    @Test
    public void testGenerateSimpleAttributeForField_PrimitiveField() throws Exception {
        Class<? extends SimpleAttribute<PojoWithPrimitiveField, Integer>> attributeClass = generateSimpleAttributeForField(PojoWithPrimitiveField.class, Integer.class, "foo", "foo");
        SimpleAttribute<PojoWithPrimitiveField, Integer> attribute = attributeClass.newInstance();
        validateAttribute(attribute, PojoWithPrimitiveField.class, Integer.class, "foo", new PojoWithPrimitiveField(), asList(5));
    }

    @Test
    public void testGenerateSimpleAttributeForGetterMethod() throws Exception {
        Class<? extends SimpleAttribute<PojoWithGetter, String>> attributeClass = generateSimpleAttributeForGetter(PojoWithGetter.class, String.class, "getFoo", "foo");
        SimpleAttribute<PojoWithGetter, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, PojoWithGetter.class, String.class, "foo", new PojoWithGetter(), asList("bar"));
    }

    @Test
    public void testGenerateSimpleAttributeForParameterizedGetterMethod() throws Exception {
        Class<? extends SimpleAttribute<PojoWithParameterizedGetter, String>> attributeClass = generateSimpleAttributeForParameterizedGetter(PojoWithParameterizedGetter.class, String.class, "getFoo", "baz", "foo");
        SimpleAttribute<PojoWithParameterizedGetter, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, PojoWithParameterizedGetter.class, String.class, "foo", new PojoWithParameterizedGetter(), asList("bar_baz"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateSimpleAttributeForField_ExceptionHandling1() throws Exception {
        generateSimpleAttributeForField(null, String.class, "foo", "foo");
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateSimpleAttributeForField_ExceptionHandling2() throws Exception {
        generateSimpleAttributeForField(PojoWithField.class, null, "foo", "foo");
    }

    // ====== Tests for SimpleNullableAttribute ======

    static class NullablePojoWithField {
        final String foo = null;
    }

    static class NullablePojoWithGetter {
        String getFoo() {
            return null;
        }
    }

    static class NullablePojoWithParameterizedGetter {
        String getFoo(String name) {
            return null;
        }
    }

    @Test
    public void testGenerateSimpleNullableAttributeForField() throws Exception {
        Class<? extends SimpleNullableAttribute<NullablePojoWithField, String>> attributeClass = generateSimpleNullableAttributeForField(NullablePojoWithField.class, String.class, "foo", "foo");
        SimpleNullableAttribute<NullablePojoWithField, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, NullablePojoWithField.class, String.class, "foo", new NullablePojoWithField(), Collections.<String>emptyList());
    }

    @Test
    public void testGenerateSimpleNullableAttributeForGetter() throws Exception {
        Class<? extends SimpleNullableAttribute<NullablePojoWithGetter, String>> attributeClass = generateSimpleNullableAttributeForGetter(NullablePojoWithGetter.class, String.class, "getFoo", "foo");
        SimpleNullableAttribute<NullablePojoWithGetter, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, NullablePojoWithGetter.class, String.class, "foo", new NullablePojoWithGetter(), Collections.<String>emptyList());
    }

    @Test
    public void testGenerateSimpleNullableAttributeForParameterizedGetter() throws Exception {
        Class<? extends SimpleNullableAttribute<NullablePojoWithParameterizedGetter, String>> attributeClass = generateSimpleNullableAttributeForParameterizedGetter(NullablePojoWithParameterizedGetter.class, String.class, "getFoo", "baz", "foo");
        SimpleNullableAttribute<NullablePojoWithParameterizedGetter, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, NullablePojoWithParameterizedGetter.class, String.class, "foo", new NullablePojoWithParameterizedGetter(), Collections.<String>emptyList());
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateSimpleNullableAttributeForField_ExceptionHandling1() throws Exception {
        generateSimpleNullableAttributeForField(null, String.class, "foo", "foo");
    }

    // ====== Tests for MultiValueAttribute ======

    static class PojoWithMultiValueField {
        final List<String> foo = asList("bar1", "bar2");
    }

    static class PojoWithMultiValuePrimitiveArrayField {
        final int[] foo = {5, 6};
    }

    static class PojoWithMultiValueObjectArrayField {
        final String[] foo = {"bar1", "bar2"};
    }

    static class PojoWithMultiValueGetter {
        List<String> getFoo() {
            return asList("bar1", "bar2");
        }
    }

    static class PojoWithMultiValueParameterizedGetter {
        List<String> getFoo(String name) {
            return asList("bar1_" + name, "bar2_" + name);
        }
    }

    @Test
    public void testGenerateMultiValueAttributeForField_ListField() throws Exception {
        Class<? extends MultiValueAttribute<PojoWithMultiValueField, String>> attributeClass = generateMultiValueAttributeForField(PojoWithMultiValueField.class, String.class, "foo", "foo");
        MultiValueAttribute<PojoWithMultiValueField, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, PojoWithMultiValueField.class, String.class, "foo", new PojoWithMultiValueField(), asList("bar1", "bar2"));
    }

    @Test
    public void testGenerateMultiValueAttributeForField_PrimitiveArrayField() throws Exception {
        Class<? extends MultiValueAttribute<PojoWithMultiValuePrimitiveArrayField, Integer>> attributeClass = generateMultiValueAttributeForField(PojoWithMultiValuePrimitiveArrayField.class, Integer.class, "foo", "foo");
        MultiValueAttribute<PojoWithMultiValuePrimitiveArrayField, Integer> attribute = attributeClass.newInstance();
        validateAttribute(attribute, PojoWithMultiValuePrimitiveArrayField.class, Integer.class, "foo", new PojoWithMultiValuePrimitiveArrayField(), asList(5, 6));
    }

    @Test
    public void testGenerateMultiValueAttributeForField_ObjectArrayField() throws Exception {
        Class<? extends MultiValueAttribute<PojoWithMultiValueObjectArrayField, String>> attributeClass = generateMultiValueAttributeForField(PojoWithMultiValueObjectArrayField.class, String.class, "foo", "foo");
        MultiValueAttribute<PojoWithMultiValueObjectArrayField, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, PojoWithMultiValueObjectArrayField.class, String.class, "foo", new PojoWithMultiValueObjectArrayField(), asList("bar1", "bar2"));
    }

    @Test
    public void testGenerateMultiValueAttributeForGetter() throws Exception {
        Class<? extends MultiValueAttribute<PojoWithMultiValueGetter, String>> attributeClass = generateMultiValueAttributeForGetter(PojoWithMultiValueGetter.class, String.class, "getFoo", "foo");
        MultiValueAttribute<PojoWithMultiValueGetter, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, PojoWithMultiValueGetter.class, String.class, "foo", new PojoWithMultiValueGetter(), asList("bar1", "bar2"));
    }

    @Test
    public void testGenerateMultiValueAttributeForParameterizedGetter() throws Exception {
        Class<? extends MultiValueAttribute<PojoWithMultiValueParameterizedGetter, String>> attributeClass = generateMultiValueAttributeForParameterizedGetter(PojoWithMultiValueParameterizedGetter.class, String.class, "getFoo", "baz", "foo");
        MultiValueAttribute<PojoWithMultiValueParameterizedGetter, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, PojoWithMultiValueParameterizedGetter.class, String.class, "foo", new PojoWithMultiValueParameterizedGetter(), asList("bar1_baz", "bar2_baz"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateMultiValueAttributeForField_ExceptionHandling1() throws Exception {
        generateMultiValueAttributeForField(null, String.class, "foo", "object.foo");
    }


    @Test(expected = IllegalStateException.class)
    public void testGenerateMultiValueAttributeForField_ExceptionHandling2() throws Exception {
        generateMultiValueAttributeForField(PojoWithMultiValueField.class, null, "foo", "foo");
    }

    // ====== Tests for MultiValueNullableAttribute ======

    static class NullablePojoWithMultiValueField {
        final List<String> foo = null;
    }

    static class NullablePojoWithMultiValueGetter {
        List<String> getFoo() {
            return null;
        }
    }

    static class NullablePojoWithMultiValueParameterizedGetter {
        List<String> getFoo(String name) {
            return null;
        }
    }

    @Test
    public void testGenerateMultiValueNullableAttributeForField() throws Exception {
        Class<? extends MultiValueNullableAttribute<NullablePojoWithMultiValueField, String>> attributeClass = generateMultiValueNullableAttributeForField(NullablePojoWithMultiValueField.class, String.class, "foo", true, "foo");
        MultiValueNullableAttribute<NullablePojoWithMultiValueField, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, NullablePojoWithMultiValueField.class, String.class, "foo", new NullablePojoWithMultiValueField(), Collections.<String>emptyList());
    }

    @Test
    public void testGenerateMultiValueNullableAttributeForGetter() throws Exception {
        Class<? extends MultiValueNullableAttribute<NullablePojoWithMultiValueGetter, String>> attributeClass = generateMultiValueNullableAttributeForGetter(NullablePojoWithMultiValueGetter.class, String.class, "getFoo", true, "foo");
        MultiValueNullableAttribute<NullablePojoWithMultiValueGetter, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, NullablePojoWithMultiValueGetter.class, String.class, "foo", new NullablePojoWithMultiValueGetter(), Collections.<String>emptyList());
    }

    @Test
    public void testGenerateMultiValueNullableAttributeForParameterizedGetter() throws Exception {
        Class<? extends MultiValueNullableAttribute<NullablePojoWithMultiValueParameterizedGetter, String>> attributeClass = generateMultiValueNullableAttributeForParameterizedGetter(NullablePojoWithMultiValueParameterizedGetter.class, String.class, "getFoo", "baz", true, "foo");
        MultiValueNullableAttribute<NullablePojoWithMultiValueParameterizedGetter, String> attribute = attributeClass.newInstance();
        validateAttribute(attribute, NullablePojoWithMultiValueParameterizedGetter.class, String.class, "foo", new NullablePojoWithMultiValueParameterizedGetter(), Collections.<String>emptyList());
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateMultiValueNullableAttributeForField_ExceptionHandling1() throws Exception {
        generateMultiValueNullableAttributeForField(null, String.class, "foo", true, "object.foo");
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateMultiValueNullableAttributeForField_ExceptionHandling2() throws Exception {
        generateMultiValueNullableAttributeForField(NullablePojoWithMultiValueField.class, null, "foo", true, "foo");
    }

    // ====== Tests for support methods ======

    @Test(expected = IllegalStateException.class)
    public void testEnsureFieldExists_Negative() {
        ensureFieldExists(String.class, Integer.class, "xxxxxxxxxx", "na");
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureGetterExists_Negative() {
        ensureGetterExists(String.class, Integer.class, "xxxxxxxxxx", "na");
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureParameterizedGetterExists_Negative1() {
        ensureParameterizedGetterExists(String.class, Integer.class, "xxxxxxxxxx", "x", "na");
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureParameterizedGetterExists_Negative2() {
        ensureParameterizedGetterExists(String.class, Integer.class, "xxxxxxxxxx", "\"foo\"", "na");
    }

    @Test(expected = IllegalStateException.class)
    public void testEnsureParameterizedGetterExists_Negative3() {
        ensureParameterizedGetterExists(String.class, Integer.class, "xxxxxxxxxx", "\\foo", "na");
    }

    @Test
    public void testConstructor() {
        assertNotNull(new AttributeBytecodeGenerator());
    }

    // ====== Test helper methods ======

    static <O, A, T extends Attribute<O, A>> void validateAttribute(T attribute, Class<O> pojoClass, Class<A> attributeType, String attributeName, O pojo, List<A> expectedPojoValues) {
        Object values = attribute.getValues(pojo, noQueryOptions());
        assertNotNull("getValues() should not return null", values);
        assertEquals(pojoClass, attribute.getObjectType());
        assertEquals(attributeType, attribute.getAttributeType());
        assertEquals(attributeName, attribute.getAttributeName());
        assertTrue("getValues() should return a List, actual was: " + values.getClass().getName(), Iterable.class.isAssignableFrom(values.getClass()));
        List<A> actualAttributeValues = new ArrayList<A>();
        for (A actualValue : attribute.getValues(pojo, noQueryOptions())) {
            actualAttributeValues.add(actualValue);
        }
        assertEquals(expectedPojoValues, actualAttributeValues);
    }
}