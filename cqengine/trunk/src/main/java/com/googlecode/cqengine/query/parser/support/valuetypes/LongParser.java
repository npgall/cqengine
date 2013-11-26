package com.googlecode.cqengine.query.parser.support.valuetypes;

import com.googlecode.cqengine.query.parser.support.AttributeValueParser;

/**
 * @author Niall Gallagher
 */
public class LongParser extends AttributeValueParser<Long> {

    public LongParser() {
        super(Long.class);
    }

    @Override
    public Long parseToAttributeType(String stringValue) {
        return Long.valueOf(stringValue);
    }
}
