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
import com.googlecode.cqengine.testutil.Car;
import org.junit.Assert;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author Niall Gallagher
 */
public class CQNativeParserTest {

    @Test
    public void testCQNativeParser() {
        CQNativeParser<Car> parser = new CQNativeParser<Car>(Car.class);
        parser.registerAttribute(Car.CAR_ID);
        parser.registerAttribute(Car.MANUFACTURER);
        parser.registerAttribute(Car.MODEL);
        parser.registerAttribute(Car.COLOR);
        parser.registerAttribute(Car.DOORS);
        parser.registerAttribute(Car.PRICE);
        parser.registerAttribute(Car.FEATURES);

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


        // TODO: failing...
//        parser.registerValueParser(new ValueParser<Car.Color>(Car.Color.class) {
//            @Override
//            public Car.Color parse(String stringValue) {
//                return Car.Color.valueOf(StringParser.stripQuotes(stringValue.replaceFirst("Car.Color.", "")));
//            }
//        });
//        Query<Car> query = and(
//                        equal(Car.COLOR, Car.Color.BLUE),
//                        equal(Car.COLOR, Car.Color.RED),
//                        or(
//                                equal(Car.COLOR, Car.Color.GREEN),
//                                equal(Car.COLOR, Car.Color.BLACK)
//                        )
//        );
//        String input =
//                "and(" +
//                        "equal(\"color\", \"BLUE\"), " +
//                        "equal(\"color\", \"RED\"), " +
//                        "or(" +
//                            "equal(\"color\", \"GREEN\"), " +
//                            "equal(\"color\", \"BLACK\")" +
//                        ")" +
//                "   )";
//        Query<Car> parsed = parser.parse(input);
//        System.out.println("Parsed: " + parsed);
    }

    static void assertQueriesEquals(Query<Car> expected, Query<Car> actual) {
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.toString(), actual.toString());
    }
}
