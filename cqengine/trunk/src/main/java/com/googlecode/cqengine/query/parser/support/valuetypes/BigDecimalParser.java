package com.googlecode.cqengine.query.parser.support.valuetypes;

import com.googlecode.cqengine.query.parser.support.AttributeValueParser;

import java.math.BigDecimal;

/**
 * @author Niall Gallagher
 */
public class BigDecimalParser extends AttributeValueParser<BigDecimal> {

    public BigDecimalParser() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal parseToAttributeType(String stringValue) {
        return new BigDecimal(stringValue);
    }
}
