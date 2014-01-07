package com.googlecode.cqengine.query.parser.common.valuetypes;

import com.googlecode.cqengine.query.parser.common.AttributeValueParser;

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
