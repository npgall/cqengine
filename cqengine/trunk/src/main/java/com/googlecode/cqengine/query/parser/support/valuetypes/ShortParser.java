package com.googlecode.cqengine.query.parser.support.valuetypes;

import com.googlecode.cqengine.query.parser.support.AttributeValueParser;

/**
 * @author Niall Gallagher
 */
public class ShortParser extends AttributeValueParser<Short> {

    public ShortParser() {
        super(Short.class);
    }

    @Override
    public Short parseToAttributeType(String stringValue) {
        return Short.valueOf(stringValue);
    }
}
