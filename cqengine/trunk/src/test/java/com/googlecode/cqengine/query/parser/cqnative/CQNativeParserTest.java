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
package com.googlecode.cqengine.query.parser.cqnative;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.InvalidQueryException;
import com.googlecode.cqengine.query.parser.common.ValueParser;
import com.googlecode.cqengine.query.parser.cqnative.support.StringParser;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Assert;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author Niall Gallagher
 */
public class CQNativeParserTest {

    final CQNativeParser<Car> parser = new CQNativeParser<Car>(Car.class){{
        registerAttribute(Car.CAR_ID);
        registerAttribute(Car.MANUFACTURER);
        registerAttribute(Car.MODEL);
        registerAttribute(Car.COLOR);
        registerAttribute(Car.DOORS);
        registerAttribute(Car.PRICE);
        registerAttribute(Car.FEATURES);
    }};

    @Test
    public void testValidQueries() {
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parser.parse("equal(\"manufacturer\", \"Ford\")"));
        assertQueriesEquals(lessThanOrEqualTo(Car.PRICE, 1000.0), parser.parse("lessThanOrEqualTo(\"price\", 1000.0)"));
        assertQueriesEquals(lessThan(Car.PRICE, 1000.0), parser.parse("lessThan(\"price\", 1000.0)"));
        assertQueriesEquals(greaterThanOrEqualTo(Car.PRICE, 1000.0), parser.parse("greaterThanOrEqualTo(\"price\", 1000.0)"));
        assertQueriesEquals(greaterThan(Car.PRICE, 1000.0), parser.parse("greaterThan(\"price\", 1000.0)"));
        assertQueriesEquals(between(Car.PRICE, 1000.0, 2000.0), parser.parse("between(\"price\", 1000.0, 2000.0)"));
        assertQueriesEquals(between(Car.PRICE, 1000.0, false, 2000.0, true), parser.parse("between(\"price\", 1000.0, false, 2000.0, true)"));
        assertQueriesEquals(in(Car.MANUFACTURER, "Ford", "Honda"), parser.parse("in(\"manufacturer\", \"Ford\", \"Honda\")"));
        assertQueriesEquals(startsWith(Car.MODEL, "Fo"), parser.parse("startsWith(\"model\", \"Fo\")"));
        assertQueriesEquals(endsWith(Car.MODEL, "rd"), parser.parse("endsWith(\"model\", \"rd\")"));
        assertQueriesEquals(contains(Car.MODEL, "or"), parser.parse("contains(\"model\", \"or\")"));
        assertQueriesEquals(isContainedIn(Car.MODEL, "a b c"), parser.parse("isContainedIn(\"model\", \"a b c\")"));
        assertQueriesEquals(matchesRegex(Car.MODEL, "Fo.*"), parser.parse("matchesRegex(\"model\", \"Fo.*\")"));
        assertQueriesEquals(has(Car.FEATURES), parser.parse("has(\"features\")"));
        assertQueriesEquals(all(Car.class), parser.parse("all(Car.class)"));
        assertQueriesEquals(none(Car.class), parser.parse("none(Car.class)"));
        assertQueriesEquals(and(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Focus")), parser.parse("and(equal(\"manufacturer\", \"Ford\"), equal(\"model\", \"Focus\"))"));
        assertQueriesEquals(or(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Focus")), parser.parse("or(equal(\"manufacturer\", \"Ford\"), equal(\"model\", \"Focus\"))"));
        assertQueriesEquals(not(equal(Car.MANUFACTURER, "Ford")), parser.parse("not(equal(\"manufacturer\", \"Ford\"))"));

        parser.registerValueParser(new ValueParser<Car.Color>(Car.Color.class) {
            @Override
            public Car.Color parse(String stringValue) {
                return Car.Color.valueOf(StringParser.stripQuotes(stringValue.replaceFirst("Car.Color.", "")));
            }
        });
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
                parser.parse(
                    "or(" +
                            "and(" +
                                "lessThan(\"price\", 5000.0), " +
                                "greaterThanOrEqualTo(\"doors\", 4)" +
                            "), " +
                            "and(" +
                                "lessThan(\"price\", 8000.0), " +
                                "greaterThanOrEqualTo(\"doors\", 4), " +
                                "equal(\"features\", \"hybrid\"), " +
                                "not(in(\"color\", \"Car.Color.BLUE\", \"Car.Color.GREEN\"))" +
                            ")" +
                    ")"
                )
        );
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_DuplicateQueries() {
        parser.parse("all(Car.class)all(Car.class)");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_TrailingGibberish() {
        parser.parse("all(Car.class)abc");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_LeadingGibberish() {
        parser.parse("abc all(Car.class)");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_UnclosedQuery() {
        parser.parse("all(Car.class");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_InvalidParameters1() {
        parser.parse("equal(\"manufacturer\", x, \"Ford\")");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_InvalidParameters2() {
        parser.parse("equal(\"manufacturer\", 1, \"Ford\")");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_InvalidParameterType() {
        parser.parse("equal(\"doors\", \"foo\")");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_NullQuery() {
        parser.parse(null);
    }

    static void assertQueriesEquals(Query<Car> expected, Query<Car> actual) {
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.toString(), actual.toString());
    }
}
