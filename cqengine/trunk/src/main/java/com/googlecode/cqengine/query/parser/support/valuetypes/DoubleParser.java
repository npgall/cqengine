package com.googlecode.cqengine.query.parser.support.valuetypes;

import com.googlecode.cqengine.query.parser.support.AttributeValueParser;

/**
 * @author Niall Gallagher
 */
public class DoubleParser extends AttributeValueParser<Double> {

    public DoubleParser() {
        super(Double.class);
    }

    @Override
    public Double parseToAttributeType(String stringValue) {
        return Double.valueOf(stringValue);
    }
}
