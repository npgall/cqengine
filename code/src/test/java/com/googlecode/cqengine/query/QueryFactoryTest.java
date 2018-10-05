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
package com.googlecode.cqengine.query;

import com.googlecode.cqengine.attribute.support.MultiValueFunction;
import com.googlecode.cqengine.attribute.support.SimpleFunction;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.query.option.*;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import net.jodah.typetools.TypeResolver;
import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for methods in {@link QueryFactory} which are not covered by the functional test.
 *
 * @author niall.gallagher
 */
public class QueryFactoryTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testOrderByMethodOverloading() {
        AttributeOrder<Car> o1 = descending(Car.CAR_ID);
        AttributeOrder<Car> o2 = descending(Car.MANUFACTURER);
        AttributeOrder<Car> o3 = descending(Car.MODEL);
        AttributeOrder<Car> o4 = descending(Car.FEATURES);
        AttributeOrder<Car> o5 = descending(Car.PRICE);
        assertEquals(orderBy(o1), orderBy(asList(o1)));
        assertEquals(orderBy(o1, o2), orderBy(asList(o1, o2)));
        assertEquals(orderBy(o1, o2, o3), orderBy(asList(o1, o2, o3)));
        assertEquals(orderBy(o1, o2, o3, o4), orderBy(asList(o1, o2, o3, o4)));
        assertEquals(orderBy(o1, o2, o3, o4, o5), orderBy(asList(o1, o2, o3, o4, o5)));
        assertEquals(orderBy(o1, o2, o3, o4, o5), orderBy(new AttributeOrder[]{o1, o2, o3, o4, o5}));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAndMethodOverloading() {
        Query<Car> q1 = has(Car.CAR_ID);
        Query<Car> q2 = has(Car.MANUFACTURER);
        Query<Car> q3 = has(Car.MODEL);
        Query<Car> q4 = has(Car.FEATURES);
        Query<Car> q5 = has(Car.PRICE);
        assertEquals(and(q1, q2), new And<Car>(asList(q1, q2)));
        assertEquals(and(q1, q2, q3), new And<Car>(asList(q1, q2, q3)));
        assertEquals(and(q1, q2, q3, q4), new And<Car>(asList(q1, q2, q3, q4)));
        assertEquals(and(q1, q2, q3, q4, q5), new And<Car>(asList(q1, q2, q3, q4, q5)));
        assertEquals(and(q1, q2, q3, q4, q5), and(q1, q2, new Query[]{q3, q4, q5}));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testOrMethodOverloading() {
        Query<Car> q1 = has(Car.CAR_ID);
        Query<Car> q2 = has(Car.MANUFACTURER);
        Query<Car> q3 = has(Car.MODEL);
        Query<Car> q4 = has(Car.FEATURES);
        Query<Car> q5 = has(Car.PRICE);
        assertEquals(or(q1, q2), new Or<Car>(asList(q1, q2)));
        assertEquals(or(q1, q2, q3), new Or<Car>(asList(q1, q2, q3)));
        assertEquals(or(q1, q2, q3, q4), new Or<Car>(asList(q1, q2, q3, q4)));
        assertEquals(or(q1, q2, q3, q4, q5), new Or<Car>(asList(q1, q2, q3, q4, q5)));
        assertEquals(or(q1, q2, q3, q4, q5), or(q1, q2, new Query[]{q3, q4, q5}));
    }

    @Test
    public void testQueryOptionsMethodOverloading() {
        QueryOptions queryOptions = queryOptions(orderBy(descending(Car.CAR_ID)), deduplicate(DeduplicationStrategy.MATERIALIZE));
        assertEquals(orderBy(descending(Car.CAR_ID)), queryOptions.get(OrderByOption.class));
        assertEquals(deduplicate(DeduplicationStrategy.MATERIALIZE), queryOptions.get(DeduplicationOption.class));

        assertEquals(queryOptions(new Object[] {"foo", "bar"}), queryOptions("foo", "bar"));
    }

    @Test
    public void testMatchesRegexMethodOverloading() {
        assertEquals(matchesRegex(Car.MODEL, "F.*"), matchesRegex(Car.MODEL, Pattern.compile("F.*")));
    }

    @Test
    public void testPredicate() {
        Car redCar = CarFactory.createCar(1);
        Car blueCar = CarFactory.createCar(7);
        Predicate<Car> predicate = predicate(equal(Car.COLOR, Car.Color.BLUE));

        assertFalse(predicate.test(redCar));
        assertTrue(predicate.test(blueCar));
    }

    @Test
    public void testConstructor() {
        assertNotNull(new QueryFactory());
    }

    // ===== Tests for validateSimpleFunctionGenericTypes()... =====

    @Test
    public void testValidateSimpleFunctionGenericTypes_Success() {
        Class<?>[] typeArgs = new Class<?>[] {Car.class, Integer.class};
        validateSimpleFunctionGenericTypes(typeArgs, SimpleFunction.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateSimpleFunctionGenericTypes_NullTypeArgs() {
        validateSimpleFunctionGenericTypes(null, SimpleFunction.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateSimpleFunctionGenericTypes_IncorrectNumberOfTypeArgs() {
        Class<?>[] typeArgs = new Class<?>[] {Car.class, Integer.class, Integer.class};
        validateSimpleFunctionGenericTypes(typeArgs, SimpleFunction.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateSimpleFunctionGenericTypes_InvalidTypeArgs1() {
        Class<?>[] typeArgs = new Class<?>[] {TypeResolver.Unknown.class, Integer.class};
        validateSimpleFunctionGenericTypes(typeArgs, SimpleFunction.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateSimpleFunctionGenericTypes_InvalidTypeArgs2() {
        Class<?>[] typeArgs = new Class<?>[] {Car.class, TypeResolver.Unknown.class};
        validateSimpleFunctionGenericTypes(typeArgs, SimpleFunction.class);
    }

    // ===== Tests for validateMultiValueFunctionGenericTypes()... =====

    @Test
    public void testValidateMultiValueFunctionGenericTypes_Success() {
        Class<?>[] typeArgs = new Class<?>[] {Car.class, Integer.class, List.class};
        validateMultiValueFunctionGenericTypes(typeArgs, MultiValueFunction.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateMultiValueFunctionGenericTypes_NullTypeArgs() {
        validateMultiValueFunctionGenericTypes(null, MultiValueFunction.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateMultiValueFunctionGenericTypes_IncorrectNumberOfTypeArgs() {
        Class<?>[] typeArgs = new Class<?>[] {Car.class, Integer.class};
        validateMultiValueFunctionGenericTypes(typeArgs, MultiValueFunction.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateMultiValueFunctionGenericTypes_InvalidTypeArgs() {
        Class<?>[] typeArgs = new Class<?>[] {TypeResolver.Unknown.class, Integer.class, List.class};
        validateMultiValueFunctionGenericTypes(typeArgs, MultiValueFunction.class);
    }
}
