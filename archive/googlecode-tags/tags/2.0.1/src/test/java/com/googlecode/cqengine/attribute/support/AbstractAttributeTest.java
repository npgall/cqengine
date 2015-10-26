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
package com.googlecode.cqengine.attribute.support;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AbstractAttributeTest {

    static class ValidAttribute extends SimpleAttribute<Integer, String> {
        @Override
        public String getValue(Integer object, QueryOptions queryOptions) {
            return String.valueOf(object);
        }
    };

    static class ValidAttribute2 extends SimpleAttribute<Integer, String> {
        @Override
        public String getValue(Integer object, QueryOptions queryOptions) {
            return String.valueOf(object);
        }
    };

    static class ValidAttributeWithParameterizedTypes extends SimpleAttribute<Set<Integer>, List<String>> {
        @Override
        public List<String> getValue(Set<Integer> object, QueryOptions queryOptions) {
            return Arrays.asList(String.valueOf(object));
        }
    };

    static class ValidAttributeMultipleConstructors extends SimpleAttribute<Integer, String> {

        public ValidAttributeMultipleConstructors() {
        }

        public ValidAttributeMultipleConstructors(String attributeName) {
            super(attributeName);
        }

        public ValidAttributeMultipleConstructors(Class<Integer> objectType, Class<String> attributeType) {
            super(objectType, attributeType);
        }

        public ValidAttributeMultipleConstructors(Class<Integer> objectType, Class<String> attributeType, String attributeName) {
            super(objectType, attributeType, attributeName);
        }

        @Override
        public String getValue(Integer object, QueryOptions queryOptions) {
            return String.valueOf(object);
        }
    };

    static class InvalidAttribute extends SimpleAttribute {
        @Override
        public Object getValue(Object object, QueryOptions queryOptions) {
            return String.valueOf(object);
        }
    };

    @Test
    public void testReadGenericObjectType() throws Exception {
        //noinspection AssertEqualsBetweenInconvertibleTypes
        Assert.assertEquals(Integer.class, AbstractAttribute.readGenericObjectType(ValidAttribute.class, "foo"));
        //noinspection AssertEqualsBetweenInconvertibleTypes
        Assert.assertEquals(Set.class, AbstractAttribute.readGenericObjectType(ValidAttributeWithParameterizedTypes.class, "foo"));
    }

    @Test(expected = IllegalStateException.class)
    public void testReadGenericObjectType_InvalidAttribute() throws Exception {
        AbstractAttribute.readGenericObjectType(InvalidAttribute.class, "foo");
    }

    @Test
    public void testReadGenericAttributeType() throws Exception {
        //noinspection AssertEqualsBetweenInconvertibleTypes
        Assert.assertEquals(String.class, AbstractAttribute.readGenericAttributeType(ValidAttribute.class, "foo"));
        //noinspection AssertEqualsBetweenInconvertibleTypes
        Assert.assertEquals(List.class, AbstractAttribute.readGenericAttributeType(ValidAttributeWithParameterizedTypes.class, "foo"));
    }

    @Test(expected = IllegalStateException.class)
    public void testReadGenericAttributeType_InvalidAttribute() throws Exception {
        AbstractAttribute.readGenericAttributeType(InvalidAttribute.class, "foo");
    }

    @Test
    public void testConstructors() {
        ValidAttributeMultipleConstructors a1, a2, a3, a4;
        a1 = new ValidAttributeMultipleConstructors();
        a2 = new ValidAttributeMultipleConstructors("foo");
        a3 = new ValidAttributeMultipleConstructors(Integer.class, String.class);
        a4 = new ValidAttributeMultipleConstructors(Integer.class, String.class, "foo");

        Assert.assertEquals(Integer.class, a1.getObjectType());
        Assert.assertEquals(Integer.class, a2.getObjectType());
        Assert.assertEquals(Integer.class, a3.getObjectType());
        Assert.assertEquals(Integer.class, a4.getObjectType());

        Assert.assertEquals(String.class, a1.getAttributeType());
        Assert.assertEquals(String.class, a2.getAttributeType());
        Assert.assertEquals(String.class, a3.getAttributeType());
        Assert.assertEquals(String.class, a4.getAttributeType());

        Assert.assertEquals("<Unnamed attribute, class com.googlecode.cqengine.attribute.support.AbstractAttributeTest$ValidAttributeMultipleConstructors>", a1.getAttributeName());
        Assert.assertEquals("foo", a2.getAttributeName());
        Assert.assertEquals("<Unnamed attribute, class com.googlecode.cqengine.attribute.support.AbstractAttributeTest$ValidAttributeMultipleConstructors>", a3.getAttributeName());
        Assert.assertEquals("foo", a4.getAttributeName());
    }
}