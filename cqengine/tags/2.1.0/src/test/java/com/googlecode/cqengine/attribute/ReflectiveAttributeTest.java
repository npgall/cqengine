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
package com.googlecode.cqengine.attribute;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.QueryFactory;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * @author Niall Gallagher
 */
public class ReflectiveAttributeTest {

    @Test
    public void testReflectiveAttribute() {
        // Create an indexed collection (note: could alternatively use CQEngine.copyFrom() existing collection)...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();

        // Define an attribute which will use reflection...
        Attribute<Car, String> NAME = ReflectiveAttribute.forField(Car.class, String.class, "name");

        cars.addIndex(HashIndex.onAttribute(NAME));
        // Add some objects to the collection...
        cars.add(new Car(1, "ford focus", "great condition, low mileage", Arrays.asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "ford taurus", "dirty and unreliable, flat tyre", Arrays.asList("spare tyre", "radio")));
        cars.add(new Car(3, "honda civic", "has a flat tyre and high mileage", Arrays.asList("radio")));

        Assert.assertEquals(cars.retrieve(equal(NAME, "honda civic")).size(), 1);
    }

    @Test
    public void testGetInheritedField() throws NoSuchFieldException {
        Assert.assertEquals("foo", ReflectiveAttribute.getField(Bar.class, "foo").getName());
        Assert.assertEquals("bar", ReflectiveAttribute.getField(Bar.class, "bar").getName());
        NoSuchFieldException expected = null;
        try {
            ReflectiveAttribute.getField(Bar.class, "baz");
        }
        catch (NoSuchFieldException nsfe) {
            expected = nsfe;
        }
        Assert.assertNotNull(expected);
    }

    @Test
    public void testEqualsAndHashCode() throws NoSuchFieldException {
        EqualsVerifier.forClass(ReflectiveAttribute.class)
                .withRedefinedSuperclass()
                .withPrefabValues(Field.class, Foo.class.getDeclaredField("foo"), Bar.class.getDeclaredField("bar"))
                .withCachedHashCode("cachedHashCode", "calcHashCode", null)
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE, Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidField() {
        ReflectiveAttribute.forField(Foo.class, int.class, "baz");
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidFieldType() {
        ReflectiveAttribute.forField(Foo.class, double.class, "foo");
    }

    @Test(expected = IllegalStateException.class)
    @SuppressWarnings("unchecked")
    public void testGetValueInvalidObject() {
        ReflectiveAttribute reflectiveAttribute = ReflectiveAttribute.forField(Foo.class, int.class, "foo");
        reflectiveAttribute.getValue("", QueryFactory.noQueryOptions());
    }

    static class Foo {
        private int foo;
    }

    static class Bar extends Foo {
        private int bar;
    }

}
