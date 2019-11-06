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
package com.googlecode.cqengine.query.parser.cqn;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.InvalidQueryException;
import com.googlecode.cqengine.query.parser.common.ParseResult;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import com.googlecode.cqengine.testutil.MobileTerminating;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static com.googlecode.cqengine.IndexedCollectionFunctionalTest.asSet;
import static com.googlecode.cqengine.IndexedCollectionFunctionalTest.extractCarIds;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;

/**
 * @author Niall Gallagher
 */
public class CQNParserTest {

    final CQNParser<Car> parser = new CQNParser<Car>(Car.class){{
        registerAttribute(Car.CAR_ID);
        registerAttribute(Car.MANUFACTURER);
        registerAttribute(Car.MODEL);
        registerAttribute(Car.COLOR);
        registerAttribute(Car.DOORS);
        registerAttribute(Car.PRICE);
        registerAttribute(Car.FEATURES);
    }};
    
    final CQNParser<MobileTerminating> mtParser = new CQNParser<MobileTerminating>(MobileTerminating.class) {{
        registerAttribute(MobileTerminating.PREFIX);
        registerAttribute(MobileTerminating.OPERATOR_NAME);
        registerAttribute(MobileTerminating.REGION);
        registerAttribute(MobileTerminating.ZONE);
    }};

    
    @Test
    public void testValidQueries() {
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parser.query("equal(\"manufacturer\", \"Ford\")"));
        assertQueriesEquals(lessThanOrEqualTo(Car.PRICE, 1000.0), parser.query("lessThanOrEqualTo(\"price\", 1000.0)"));
        assertQueriesEquals(lessThan(Car.PRICE, 1000.0), parser.query("lessThan(\"price\", 1000.0)"));
        assertQueriesEquals(greaterThanOrEqualTo(Car.PRICE, 1000.0), parser.query("greaterThanOrEqualTo(\"price\", 1000.0)"));
        assertQueriesEquals(greaterThan(Car.PRICE, 1000.0), parser.query("greaterThan(\"price\", 1000.0)"));
        assertQueriesEquals(between(Car.PRICE, 1000.0, 2000.0), parser.query("between(\"price\", 1000.0, 2000.0)"));
        assertQueriesEquals(between(Car.PRICE, 1000.0, false, 2000.0, true), parser.query("between(\"price\", 1000.0, false, 2000.0, true)"));
        assertQueriesEquals(in(Car.MANUFACTURER, "Ford", "Honda"), parser.query("in(\"manufacturer\", \"Ford\", \"Honda\")"));
        assertQueriesEquals(startsWith(Car.MODEL, "Fo"), parser.query("startsWith(\"model\", \"Fo\")"));
        assertQueriesEquals(endsWith(Car.MODEL, "rd"), parser.query("endsWith(\"model\", \"rd\")"));
        assertQueriesEquals(contains(Car.MODEL, "or"), parser.query("contains(\"model\", \"or\")"));
        assertQueriesEquals(isContainedIn(Car.MODEL, "a b c"), parser.query("isContainedIn(\"model\", \"a b c\")"));
        assertQueriesEquals(matchesRegex(Car.MODEL, "Fo.*"), parser.query("matchesRegex(\"model\", \"Fo.*\")"));
        assertQueriesEquals(has(Car.FEATURES), parser.query("has(\"features\")"));
        assertQueriesEquals(all(Car.class), parser.query("all(Car.class)"));
        assertQueriesEquals(none(Car.class), parser.query("none(Car.class)"));
        assertQueriesEquals(and(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Focus")), parser.query("and(equal(\"manufacturer\", \"Ford\"), equal(\"model\", \"Focus\"))"));
        assertQueriesEquals(or(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Focus")), parser.query("or(equal(\"manufacturer\", \"Ford\"), equal(\"model\", \"Focus\"))"));
        assertQueriesEquals(not(equal(Car.MANUFACTURER, "Ford")), parser.query("not(equal(\"manufacturer\", \"Ford\"))"));
        assertQueriesEquals(equal(Car.CAR_ID, -1), parser.query("equal(\"carId\", -1)"));
        assertQueriesEquals(equal(Car.PRICE, -1.5), parser.query("equal(\"price\", -1.5)"));
        assertQueriesEquals(isPrefixOf(Car.MANUFACTURER, "Ford"), parser.query("isPrefixOf(\"manufacturer\", \"Ford\")"));
        assertQueriesEquals(longestPrefix(MobileTerminating.PREFIX, "12345"), mtParser.query("longestPrefix(\"prefix\", \"12345\")"));
        assertQueriesEquals(
                or(
                        and( // Cars less than 5K which have at least 4 doors
                                lessThan(Car.PRICE, 5000.0),
                                greaterThanOrEqualTo(Car.DOORS, 4)
                        ),
                        and( // OR cars less than 8K which have at least 4 doors and are a hybrid, but are not blue or green
                                lessThan(Car.PRICE, 8000.0),
                                greaterThanOrEqualTo(Car.DOORS, 4),
                                equal(Car.FEATURES, "hybrid"),
                                not(in(Car.COLOR, Car.Color.BLUE, Car.Color.GREEN))
                        )
                ),
                parser.query(
                        "or(" +
                                "and(" +
                                "lessThan(\"price\", 5000.0), " +
                                "greaterThanOrEqualTo(\"doors\", 4)" +
                                "), " +
                                "and(" +
                                "lessThan(\"price\", 8000.0), " +
                                "greaterThanOrEqualTo(\"doors\", 4), " +
                                "equal(\"features\", \"hybrid\"), " +
                                "not(in(\"color\", BLUE, GREEN))" +
                                ")" +
                                ")"
                )
        );
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_DuplicateQueries() {
        parser.query("all(Car.class)all(Car.class)");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_TrailingGibberish() {
        parser.query("all(Car.class)abc");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_LeadingGibberish() {
        parser.query("abc all(Car.class)");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_UnclosedQuery() {
        parser.query("all(Car.class");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_InvalidParameters1() {
        parser.query("equal(\"manufacturer\", x, \"Ford\")");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_InvalidParameters2() {
        parser.query("equal(\"manufacturer\", 1, \"Ford\")");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_InvalidParameterType() {
        parser.query("equal(\"doors\", \"foo\")");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_NullQuery() {
        parser.query(null);
    }

    @Test
    public void testOrderBy_NoOrdering() {
        ParseResult<Car> parseResult = parser.parse("equal(\"manufacturer\", \"Ford\")");
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parseResult.getQuery());
        Assert.assertEquals(noQueryOptions(), parseResult.getQueryOptions());
    }

    @Test
    public void testOrderBy_SimpleOrdering() {
        ParseResult<Car> parseResult = parser.parse("equal(\"manufacturer\", \"Ford\"), queryOptions(orderBy(ascending(\"manufacturer\")))");
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parseResult.getQuery());
        Assert.assertEquals(queryOptions(orderBy(ascending(Car.MANUFACTURER))), parseResult.getQueryOptions());
    }

    @Test
    public void testOrderBy_ComplexOrdering() {
        ParseResult<Car> parseResult = parser.parse("equal(\"manufacturer\", \"Ford\"), queryOptions(orderBy(ascending(\"manufacturer\"), descending(\"carId\")))");
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parseResult.getQuery());
        Assert.assertEquals(queryOptions(orderBy(ascending(Car.MANUFACTURER), descending(Car.CAR_ID))), parseResult.getQueryOptions());
    }

    static <T> void assertQueriesEquals(Query<T> expected, Query<T> actual) {
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    

    @Test
    public void testRetrieve() {
        // CQN syntax does not yet support ordering, so here we just test parsing a query without ordering...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addAll(CarFactory.createCollectionOfCars(10));

        ResultSet<Car> results = parser.retrieve(cars, "and(equal(\"manufacturer\", \"Honda\"), not(equal(\"color\", \"WHITE\")))");
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(asSet(5, 4), extractCarIds(results, new HashSet<Integer>()));
    }
}
