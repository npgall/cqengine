package com.googlecode.cqengine.query.parser.cqnative;

import org.junit.Test;

/**
 * @author Niall Gallagher
 */
public class CQNativeParserTest {

    @Test
    public void testParseStruct() {
        CQNativeParser.QueryStruct struct = CQNativeParser.parseStruct("or(equal(Car.DOORS, 5), equal(Car.DOORS, 4), equal(Car.DOORS, 3))");
        System.out.println(struct.queryType);
        for (String s : struct.arguments) {
            System.out.println("[" + s + "]");
        }

    }
}
