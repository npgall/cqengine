package com.googlecode.cqengine.query.parser.support.valuetypes;

import com.googlecode.cqengine.query.parser.support.AttributeValueParser;

/**
 * @author Niall Gallagher
 */
public class IntegerParser extends AttributeValueParser<Integer> {

    public IntegerParser() {
        super(Integer.class);
    }

    @Override
    public Integer parseToAttributeType(String stringValue) {
        return Integer.valueOf(stringValue);
    }
}
