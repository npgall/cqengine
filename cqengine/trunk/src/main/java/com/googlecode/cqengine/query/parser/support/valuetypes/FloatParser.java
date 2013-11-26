package com.googlecode.cqengine.query.parser.support.valuetypes;

import com.googlecode.cqengine.query.parser.support.AttributeValueParser;

/**
 * @author Niall Gallagher
 */
public class FloatParser extends AttributeValueParser<Float> {

    public FloatParser() {
        super(Float.class);
    }

    @Override
    public Float parseToAttributeType(String stringValue) {
        return Float.valueOf(stringValue);
    }
}
