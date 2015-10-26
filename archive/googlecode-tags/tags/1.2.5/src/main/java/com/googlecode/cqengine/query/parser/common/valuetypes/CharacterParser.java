package com.googlecode.cqengine.query.parser.common.valuetypes;

import com.googlecode.cqengine.query.parser.common.AttributeValueParser;

/**
 * @author Niall Gallagher
 */
public class CharacterParser extends AttributeValueParser<Character> {

    public CharacterParser() {
        super(Character.class);
    }

    @Override
    public Character parseToAttributeType(String stringValue) {
        if (stringValue.length() != 1) {
            throw new IllegalArgumentException();
        }
        return stringValue.charAt(0);
    }
}
