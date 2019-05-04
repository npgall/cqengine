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
import com.googlecode.cqengine.examples.inheritance.SportsCar;
import org.junit.Test;

import java.util.*;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.*;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

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

    static class AnotherPojoWithGetter {
        Integer getBar() { return 1; }
    }

    static class SuperCar extends SportsCar { // ...SportCar in turn extends Car
        final double[] tyrePressures;
        final Float[] wheelSpeeds;
        public Float[] getWheelSpeeds() { return wheelSpeeds; }

        public SuperCar(int carId, String name, String description, List<String> features, int horsepower, double[] tyrePressures, Float[] wheelSpeeds) {
            super(carId, name, description, features, horsepower);
            this.tyrePressures = tyrePressures;
            this.wheelSpeeds = wheelSpeeds;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateAttributesForFields() {
        SuperCar car1 = new SuperCar(0, "Ford Focus", "Blue", Arrays.asList("sunroof", "radio"), 5000, new double[] {1536.5, 1782.9}, new Float[] {56700.9F, 83321.0F});
        SuperCar car2 = new SuperCar(1, "Ford Fusion", "Red", Arrays.asList("coupe", "cd player"), 6000, new double[] {12746.2, 2973.1}, new Float[] {43424.4F, 61232.7F});

        Map<String, ? extends Attribute<SuperCar, ?>> attributes = AttributeBytecodeGenerator.createAttributes(SuperCar.class);
        assertFalse(attributes.containsKey("horsepower")); // horsepower is not accessible from the subclass
        // 2 fields in SuperCar, plus 0 fields inherited from SportsCar, plus 4 inherited from Car...
        assertEquals(6, attributes.size());
        // Validate attributes reading fields from car1...
        validateAttribute(((Attribute<SuperCar, Integer>)attributes.get("carId")), SuperCar.class, Integer.class, "carId", car1, Collections.singletonList(0));
        validateAttribute(((Attribute<SuperCar, String>)attributes.get("name")), SuperCar.class, String.class, "name", car1, Collections.singletonList("Ford Focus"));
        validateAttribute(((Attribute<SuperCar, String>)attributes.get("description")), SuperCar.class, String.class, "description", car1, Collections.singletonList("Blue"));
        validateAttribute(((Attribute<SuperCar, String>)attributes.get("features")), SuperCar.class, String.class, "features", car1, Arrays.asList("sunroof", "radio"));
        validateAttribute(((Attribute<SuperCar, Double>)attributes.get("tyrePressures")), SuperCar.class, Double.class, "tyrePressures", car1, Arrays.asList(1536.5, 1782.9));
        validateAttribute(((Attribute<SuperCar, Float>)attributes.get("wheelSpeeds")), SuperCar.class, Float.class, "wheelSpeeds", car1, Arrays.asList(56700.9F, 83321.0F));
        // Validate attributes reading fields from car2...
        validateAttribute(((Attribute<SuperCar, Integer>)attributes.get("carId")), SuperCar.class, Integer.class, "carId", car2, Collections.singletonList(1));
        validateAttribute(((Attribute<SuperCar, String>)attributes.get("name")), SuperCar.class, String.class, "name", car2, Collections.singletonList("Ford Fusion"));
        validateAttribute(((Attribute<SuperCar, String>)attributes.get("description")), SuperCar.class, String.class, "description", car2, Collections.singletonList("Red"));
        validateAttribute(((Attribute<SuperCar, String>)attributes.get("features")), SuperCar.class, String.class, "features", car2, Arrays.asList("coupe", "cd player"));
        validateAttribute(((Attribute<SuperCar, Double>)attributes.get("tyrePressures")), SuperCar.class, Double.class, "tyrePressures", car2, Arrays.asList(12746.2, 2973.1));
        validateAttribute(((Attribute<SuperCar, Float>)attributes.get("wheelSpeeds")), SuperCar.class, Float.class, "wheelSpeeds", car2, Arrays.asList(43424.4F, 61232.7F));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateAttributesForGetterMethods() {
        SuperCar car1 = new SuperCar(0, "Ford Focus", "Blue", Arrays.asList("sunroof", "radio"), 5000, new double[] {1536.5, 1782.9}, new Float[] {56700.9F, 83321.0F});
        SuperCar car2 = new SuperCar(1, "Ford Fusion", "Red", Arrays.asList("coupe", "cd player"), 6000, new double[] {12746.2, 2973.1}, new Float[] {43424.4F, 61232.7F});

        Map<String, ? extends Attribute<SuperCar, ?>> attributes = AttributeBytecodeGenerator.createAttributes(SuperCar.class, MemberFilters.GETTER_METHODS_ONLY);
        assertEquals(1, attributes.size());
        // Validate attributes reading fields from car1...
        validateAttribute(((Attribute<SuperCar, Float>)attributes.get("getWheelSpeeds")), SuperCar.class, Float.class, "getWheelSpeeds", car1, Arrays.asList(56700.9F, 83321.0F));
        // Validate attributes reading fields from car2...
        validateAttribute(((Attribute<SuperCar, Float>)attributes.get("getWheelSpeeds")), SuperCar.class, Float.class, "getWheelSpeeds", car2, Arrays.asList(43424.4F, 61232.7F));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateAttributesForGetterMethods_HumanReadable() {
        AnotherPojoWithGetter pojo = new AnotherPojoWithGetter();

        Map<String, ? extends Attribute<AnotherPojoWithGetter, ?>> attributes = AttributeBytecodeGenerator.createAttributes(AnotherPojoWithGetter.class, MemberFilters.GETTER_METHODS_ONLY, AttributeNameProducers.USE_HUMAN_READABLE_NAMES_FOR_GETTERS);
        assertEquals(1, attributes.size());

        validateAttribute(((Attribute<AnotherPojoWithGetter, Integer>)attributes.get("bar")), AnotherPojoWithGetter.class, Integer.class, "bar", pojo, Collections.singletonList(1));
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

    @Test(expected = IllegalStateException.class)
    public void testGetWrapperForPrimitive_NonPrimitiveHandling() {
        AttributeBytecodeGenerator.getWrapperForPrimitive(String.class);
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