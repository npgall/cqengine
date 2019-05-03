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
package com.googlecode.cqengine.query.parser.sql;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.parser.common.InvalidQueryException;
import com.googlecode.cqengine.query.parser.common.ParseResult;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static com.googlecode.cqengine.IndexedCollectionFunctionalTest.extractCarIds;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.QueryFactory.not;
import static com.googlecode.cqengine.query.QueryFactory.or;
import static java.util.Arrays.asList;

public class SQLParserTest {

    static final Attribute<Car, Boolean> IS_BLUE = new SimpleAttribute<Car, Boolean>("is_blue") {
        @Override
        public Boolean getValue(Car object, QueryOptions queryOptions) {
            return object.getColor().equals(Car.Color.BLUE);
        }
    };

    final SQLParser<Car> parser = new SQLParser<Car>(Car.class){{
        registerAttribute(Car.CAR_ID);
        registerAttribute(Car.MANUFACTURER);
        registerAttribute(Car.MODEL);
        registerAttribute(Car.COLOR);
        registerAttribute(Car.DOORS);
        registerAttribute(Car.PRICE);
        registerAttribute(Car.FEATURES);
        registerAttribute(IS_BLUE);
    }};

    @Test
    public void testValidQueries() {
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parser.query("SELECT * FROM cars WHERE 'manufacturer' = 'Ford'"));
        assertQueriesEquals(lessThanOrEqualTo(Car.PRICE, 1000.0), parser.query("SELECT * FROM cars WHERE 'price' <= 1000.0"));
        assertQueriesEquals(lessThan(Car.PRICE, 1000.0), parser.query("SELECT * FROM cars WHERE 'price' < 1000.0"));
        assertQueriesEquals(greaterThanOrEqualTo(Car.PRICE, 1000.0), parser.query("SELECT * FROM cars WHERE 'price' >= 1000.0"));
        assertQueriesEquals(greaterThan(Car.PRICE, 1000.0), parser.query("SELECT * FROM cars WHERE 'price' > 1000.0"));
        assertQueriesEquals(between(Car.PRICE, 1000.0, 2000.0), parser.query("SELECT * FROM cars WHERE 'price' BETWEEN 1000.0 AND 2000.0"));
        assertQueriesEquals(not(between(Car.PRICE, 1000.0, 2000.0)), parser.query("SELECT * FROM cars WHERE 'price' NOT BETWEEN 1000.0 AND 2000.0"));
        assertQueriesEquals(in(Car.MANUFACTURER, "Ford", "Honda"), parser.query("SELECT * FROM cars WHERE 'manufacturer' IN ('Ford', 'Honda')"));
        assertQueriesEquals(not(in(Car.MANUFACTURER, "Ford", "Honda")), parser.query("SELECT * FROM cars WHERE 'manufacturer' NOT IN ('Ford', 'Honda')"));
        assertQueriesEquals(startsWith(Car.MODEL, "Fo"), parser.query("SELECT * FROM cars WHERE 'model' LIKE 'Fo%'"));
        assertQueriesEquals(endsWith(Car.MODEL, "rd"), parser.query("SELECT * FROM cars WHERE 'model' LIKE '%rd'"));
        assertQueriesEquals(contains(Car.MODEL, "or"), parser.query("SELECT * FROM cars WHERE 'model' LIKE '%or%'"));
        assertQueriesEquals(has(Car.FEATURES), parser.query("SELECT * FROM cars WHERE 'features' IS NOT NULL"));
        assertQueriesEquals(all(Car.class), parser.query("SELECT * FROM cars"));
        assertQueriesEquals(and(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Focus")), parser.query("SELECT * FROM cars WHERE ('manufacturer' = 'Ford' AND 'model' = 'Focus')"));
        assertQueriesEquals(or(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Focus")), parser.query("SELECT * FROM cars WHERE ('manufacturer' = 'Ford' OR 'model' = 'Focus')"));
        assertQueriesEquals(not(equal(Car.MANUFACTURER, "Ford")), parser.query("SELECT * FROM cars WHERE 'manufacturer' <> 'Ford'"));
        assertQueriesEquals(not(equal(Car.MANUFACTURER, "Ford")), parser.query("SELECT * FROM cars WHERE NOT ('manufacturer' = 'Ford')"));
        assertQueriesEquals(not(equal(Car.MANUFACTURER, "Ford")), parser.query("SELECT * FROM cars WHERE (NOT ('manufacturer' = 'Ford'))"));
        assertQueriesEquals(equal(IS_BLUE, true), parser.query("SELECT * FROM cars WHERE (is_blue = true)"));
        assertQueriesEquals(equal(IS_BLUE, false), parser.query("SELECT * FROM cars WHERE (is_blue = false)"));
        assertQueriesEquals(equal(Car.DOORS, 3), parser.query("SELECT * FROM cars WHERE 'doors' = 3"));
        assertQueriesEquals(equal(Car.DOORS, 3), parser.query("SELECT * FROM cars WHERE 'doors' = +3"));
        assertQueriesEquals(equal(Car.DOORS, -3), parser.query("SELECT * FROM cars WHERE 'doors' = -3"));
        assertQueriesEquals(equal(Car.PRICE, 3.0), parser.query("SELECT * FROM cars WHERE 'price' = 3"));
        assertQueriesEquals(equal(Car.PRICE, -3.0), parser.query("SELECT * FROM cars WHERE 'price' = -3"));
        assertQueriesEquals(equal(Car.PRICE, 3.1), parser.query("SELECT * FROM cars WHERE 'price' = 3.1"));
        assertQueriesEquals(equal(Car.PRICE, 3.1), parser.query("SELECT * FROM cars WHERE 'price' = +3.1"));
        assertQueriesEquals(equal(Car.PRICE, -3.1), parser.query("SELECT * FROM cars WHERE 'price' = -3.1"));
        assertQueriesEquals(equal(Car.MODEL, "Sam's car"), parser.query("SELECT * FROM cars WHERE 'model' = 'Sam''s car'"));

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
                        "SELECT * FROM cars WHERE " +
                                "(" +
                                "(" +
                                "'price' < 5000.0" +
                                " AND " +
                                "'doors' >= 4" +
                                ")" +
                                " OR " +
                                "(" +
                                "'price' < 8000.0" +
                                " AND " +
                                "'doors' >= 4" +
                                " AND " +
                                "'features' = 'hybrid'" +
                                " AND " +
                                "'color' NOT IN ('BLUE', 'GREEN')" +
                                ")" +
                                ")"
                )
        );
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_DuplicateQueries() {
        parser.query("SELECT * FROM cars WHERE 'price' < 1000.0 SELECT * FROM cars WHERE 'price' < 1000.0");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_TrailingGibberish() {
        parser.query("SELECT * FROM cars WHERE 'price' < 1000.0 abc");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_LeadingGibberish() {
        parser.query("abc SELECT * FROM cars WHERE 'price' < 1000.0");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_UnclosedQuery() {
        parser.query("SELECT * FROM cars WHERE 'price' < 1000.0 AND ");
    }


    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_InvalidParameterType() {
        parser.query("SELECT * FROM cars WHERE 'doors' = 'foo'");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_NullQuery() {
        parser.query(null);
    }

    static void assertQueriesEquals(Query<Car> expected, Query<Car> actual) {
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOrderBy_NoOrdering() {
        ParseResult<Car> parseResult = parser.parse("SELECT * FROM cars WHERE 'manufacturer' = 'Ford'");
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parseResult.getQuery());
        Assert.assertEquals(noQueryOptions(), parseResult.getQueryOptions());
    }

    @Test
    public void testOrderBy_SimpleOrdering1() {
        ParseResult<Car> parseResult = parser.parse("SELECT * FROM cars WHERE 'manufacturer' = 'Ford' ORDER BY manufacturer ASC");
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parseResult.getQuery());
        Assert.assertEquals(queryOptions(orderBy(ascending(Car.MANUFACTURER))), parseResult.getQueryOptions());
    }

    @Test
    public void testOrderBy_SimpleOrdering2() {
        ParseResult<Car> parseResult = parser.parse("SELECT * FROM cars WHERE 'manufacturer' = 'Ford' ORDER BY manufacturer asc");
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parseResult.getQuery());
        Assert.assertEquals(queryOptions(orderBy(ascending(Car.MANUFACTURER))), parseResult.getQueryOptions());
    }

    @Test
    public void testOrderBy_ComplexOrdering() {
        ParseResult<Car> parseResult = parser.parse("SELECT * FROM cars WHERE 'manufacturer' = 'Ford' ORDER BY manufacturer ASC, carId DESC");
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parseResult.getQuery());
        Assert.assertEquals(queryOptions(orderBy(ascending(Car.MANUFACTURER), descending(Car.CAR_ID))), parseResult.getQueryOptions());
    }

    @Test
    public void testOrderBy_AscIsOptional() {
        ParseResult<Car> parseResult = parser.parse("SELECT * FROM cars WHERE 'manufacturer' = 'Ford' ORDER BY manufacturer, carId DESC");
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parseResult.getQuery());
        Assert.assertEquals(queryOptions(orderBy(ascending(Car.MANUFACTURER), descending(Car.CAR_ID))), parseResult.getQueryOptions());
    }

    @Test
    public void testRetrieve() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addAll(CarFactory.createCollectionOfCars(10));

        ResultSet<Car> results = parser.retrieve(cars, "SELECT * FROM cars WHERE (manufacturer = 'Honda' AND color <> 'WHITE') ORDER BY carId DESC");
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(asList(5, 4), extractCarIds(results, new ArrayList<Integer>()));
    }
}