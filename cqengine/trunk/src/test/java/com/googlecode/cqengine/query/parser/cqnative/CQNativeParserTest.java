package com.googlecode.cqengine.query.parser.cqnative;

import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

/**
 * @author Niall Gallagher
 */
public class CQNativeParserTest {

    @Test
    public void testParseNativeQueryStructure() {
        CQNativeParser<Car> parser = new CQNativeParser<Car>(Car.class);
        CQNativeParser.QueryStructure structure = parser.parseNativeQueryStructure("or(equal(Car.DOORS, 5), equal(Car.DOORS, 4), equal(Car.DOORS, 3))");
        System.out.println(structure.queryType);
        for (String s : structure.queryArguments) {
            System.out.println("[" + s + "]");
        }

    }
}
