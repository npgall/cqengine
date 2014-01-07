package com.googlecode.cqengine.query.parser.common.valuetypes;

import com.googlecode.cqengine.query.parser.common.AttributeValueParser;

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
