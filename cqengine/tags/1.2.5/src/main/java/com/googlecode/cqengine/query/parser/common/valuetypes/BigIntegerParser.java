package com.googlecode.cqengine.query.parser.common.valuetypes;

import com.googlecode.cqengine.query.parser.common.AttributeValueParser;

import java.math.BigInteger;

/**
 * @author Niall Gallagher
 */
public class BigIntegerParser extends AttributeValueParser<BigInteger> {

    public BigIntegerParser() {
        super(BigInteger.class);
    }

    @Override
    public BigInteger parseToAttributeType(String stringValue) {
        return new BigInteger(stringValue);
    }
}
