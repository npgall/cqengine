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

import com.googlecode.cqengine.attribute.support.MultiValueFunction;
import com.googlecode.cqengine.attribute.support.SimpleFunction;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static org.junit.Assert.*;

/**
 * Tests creation of CQEngine attributes from lambda expressions.
 *
 * @author npgall
 */
public class LambdaFunctionalAttributesTest {

    // ====== Java 8... ======
//    final SimpleFunction<Car, Integer> carIdFunction = Car::getCarId;
//
//    final MultiValueFunction<Car, String, List<String>> featuresFunction = Car::getFeatures;
    // =======================


    // ====== Java 6... ======
    final SimpleFunction<Car, Integer> carIdFunction = new SimpleFunction<Car, Integer>() {
        public Integer apply(Car car) { return car.getCarId(); }
    };

    final MultiValueFunction<Car, String, List<String>> featuresFunction = new MultiValueFunction<Car, String, List<String>>() {
        public List<String> apply(Car car) { return car.getFeatures(); }
    };
    // =======================


    @Test
    public void testFunctionalSimpleAttribute() {
        SimpleAttribute<Car, Integer> CAR_ID = attribute(carIdFunction);
        assertEquals(Car.class, CAR_ID.getObjectType());
        assertEquals(Integer.class, CAR_ID.getAttributeType());
        assertTrue(CAR_ID.getAttributeName().startsWith(this.getClass().getName() + "$"));
    }

    @Test
    public void testFunctionalSimpleNullableAttribute() {
        SimpleNullableAttribute<Car, Integer> CAR_ID = nullableAttribute(carIdFunction);
        assertEquals(Car.class, CAR_ID.getObjectType());
        assertEquals(Integer.class, CAR_ID.getAttributeType());
        assertTrue(CAR_ID.getAttributeName().startsWith(this.getClass().getName() + "$"));
    }

    @Test
    public void testFunctionalMultiValueAttribute() {
        MultiValueAttribute<Car, String> CAR_ID = attribute(String.class, featuresFunction);
        assertEquals(Car.class, CAR_ID.getObjectType());
        assertEquals(String.class, CAR_ID.getAttributeType());
        assertTrue(CAR_ID.getAttributeName().startsWith(this.getClass().getName() + "$"));
    }

    @Test
    public void testFunctionalMultiValueNullableAttribute() {
        MultiValueNullableAttribute<Car, String> CAR_ID = nullableAttribute(String.class, featuresFunction);
        assertEquals(Car.class, CAR_ID.getObjectType());
        assertEquals(String.class, CAR_ID.getAttributeType());
        assertTrue(CAR_ID.getAttributeName().startsWith(this.getClass().getName() + "$"));
    }
}
