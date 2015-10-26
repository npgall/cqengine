package com.googlecode.cqengine.query.parser.common.valuetypes;

import com.googlecode.cqengine.query.parser.common.AttributeValueParser;

/**
 * @author Niall Gallagher
 */
public class ByteParser extends AttributeValueParser<Byte> {

    public ByteParser() {
        super(Byte.class);
    }

    @Override
    public Byte parseToAttributeType(String stringValue) {
        return Byte.valueOf(stringValue);
    }
}
