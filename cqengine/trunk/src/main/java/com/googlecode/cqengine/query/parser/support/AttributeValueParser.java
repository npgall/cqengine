package com.googlecode.cqengine.query.parser.support;

/**
 * @author Niall Gallagher
 */
public abstract class AttributeValueParser<A> {

    protected final Class<A> attributeType;

    public AttributeValueParser(Class<A> attributeType) {
        this.attributeType = attributeType;
    }

    public Class<A> getAttributeType() {
        return attributeType;
    }

    public abstract A parseToAttributeType(String stringValue);
}
