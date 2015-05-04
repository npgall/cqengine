package com.googlecode.cqengine.query.parser.sql;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.InvalidQueryException;
import com.googlecode.cqengine.query.parser.common.ValueParser;
import com.googlecode.cqengine.query.parser.sql.support.StringParser;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Assert;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.QueryFactory.not;
import static com.googlecode.cqengine.query.QueryFactory.or;

public class SQLParserTest {
    final SQLParser<Car> parser = new SQLParser<Car>(Car.class){{
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
        assertQueriesEquals(equal(Car.MANUFACTURER, "Ford"), parser.parse("SELECT * FROM cars WHERE 'manufacturer' = 'Ford'"));
        assertQueriesEquals(lessThanOrEqualTo(Car.PRICE, 1000.0), parser.parse("SELECT * FROM cars WHERE 'price' <= 1000.0"));
        assertQueriesEquals(lessThan(Car.PRICE, 1000.0), parser.parse("SELECT * FROM cars WHERE 'price' < 1000.0"));
        assertQueriesEquals(greaterThanOrEqualTo(Car.PRICE, 1000.0), parser.parse("SELECT * FROM cars WHERE 'price' >= 1000.0"));
        assertQueriesEquals(greaterThan(Car.PRICE, 1000.0), parser.parse("SELECT * FROM cars WHERE 'price' > 1000.0"));
        assertQueriesEquals(between(Car.PRICE, 1000.0, 2000.0), parser.parse("SELECT * FROM cars WHERE 'price' BETWEEN 1000.0 AND 2000.0"));
        assertQueriesEquals(not(between(Car.PRICE, 1000.0, 2000.0)), parser.parse("SELECT * FROM cars WHERE 'price' NOT BETWEEN 1000.0 AND 2000.0"));
        assertQueriesEquals(in(Car.MANUFACTURER, "Ford", "Honda"), parser.parse("SELECT * FROM cars WHERE 'manufacturer' IN ('Ford', 'Honda')"));
        assertQueriesEquals(not(in(Car.MANUFACTURER, "Ford", "Honda")), parser.parse("SELECT * FROM cars WHERE 'manufacturer' NOT IN ('Ford', 'Honda')"));
        assertQueriesEquals(startsWith(Car.MODEL, "Fo"), parser.parse("SELECT * FROM cars WHERE 'model' LIKE 'Fo%'"));
        assertQueriesEquals(endsWith(Car.MODEL, "rd"), parser.parse("SELECT * FROM cars WHERE 'model' LIKE '%rd'"));
        assertQueriesEquals(contains(Car.MODEL, "or"), parser.parse("SELECT * FROM cars WHERE 'model' LIKE '%or%'"));
        assertQueriesEquals(has(Car.FEATURES), parser.parse("SELECT * FROM cars WHERE 'features' IS NOT NULL"));
        assertQueriesEquals(all(Car.class), parser.parse("SELECT * FROM cars"));
        assertQueriesEquals(and(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Focus")), parser.parse("SELECT * FROM cars WHERE ('manufacturer' = 'Ford' AND 'model' = 'Focus')"));
        assertQueriesEquals(or(equal(Car.MANUFACTURER, "Ford"), equal(Car.MODEL, "Focus")), parser.parse("SELECT * FROM cars WHERE ('manufacturer' = 'Ford' OR 'model' = 'Focus')"));
        assertQueriesEquals(not(equal(Car.MANUFACTURER, "Ford")), parser.parse("SELECT * FROM cars WHERE 'manufacturer' <> 'Ford'"));
        assertQueriesEquals(not(equal(Car.MANUFACTURER, "Ford")), parser.parse("SELECT * FROM cars WHERE NOT ('manufacturer' = 'Ford')"));

        parser.registerValueParser(new ValueParser<Car.Color>(Car.Color.class) {
            @Override
            public Car.Color parse(String stringValue) {
                return Car.Color.valueOf(StringParser.stripQuotes(stringValue));
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
        parser.parse("SELECT * FROM cars WHERE 'price' < 1000.0 SELECT * FROM cars WHERE 'price' < 1000.0");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_TrailingGibberish() {
        parser.parse("SELECT * FROM cars WHERE 'price' < 1000.0 abc");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_LeadingGibberish() {
        parser.parse("abc SELECT * FROM cars WHERE 'price' < 1000.0");
    }

    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_UnclosedQuery() {
        parser.parse("SELECT * FROM cars WHERE 'price' < 1000.0 AND ");
    }


    @Test(expected = InvalidQueryException.class)
    public void testInvalidQuery_InvalidParameterType() {
        parser.parse("SELECT * FROM cars WHERE 'doors' = 'foo'");
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