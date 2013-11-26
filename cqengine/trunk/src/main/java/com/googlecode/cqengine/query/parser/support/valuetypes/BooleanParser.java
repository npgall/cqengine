package com.googlecode.cqengine.query.parser.support.valuetypes;

import com.googlecode.cqengine.query.parser.support.AttributeValueParser;

/**
 * @author Niall Gallagher
 */
public class BooleanParser extends AttributeValueParser<Boolean> {

    public BooleanParser() {
        super(Boolean.class);
    }

    @Override
    public Boolean parseToAttributeType(String stringValue) {
        return Boolean.valueOf(stringValue);
    }
}
