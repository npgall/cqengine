package com.googlecode.cqengine.query.parser.support;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.support.valuetypes.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A service provider interface for parsers which can convert string queries to CQEngine native queries.
 * <p/>
 * Subclasses can implement this to support string-based queries in various dialects,
 * such as SQL or a string representation of a CQEngine native query.
 *
 * @author Niall Gallagher
 */
public abstract class QueryParser<O> {

    protected final Class<O> objectType;
    protected final Map<String, Attribute<O, ?>> attributes = new HashMap<String, Attribute<O, ?>>();
    protected final Map<Class<?>, AttributeValueParser<?>> valueParsers = new HashMap<Class<?>, AttributeValueParser<?>>();

    public QueryParser(Class<O> objectType) {
        registerAttributeValueParser(new BooleanParser());
        registerAttributeValueParser(new ByteParser());
        registerAttributeValueParser(new CharacterParser());
        registerAttributeValueParser(new ShortParser());
        registerAttributeValueParser(new IntegerParser());
        registerAttributeValueParser(new LongParser());
        registerAttributeValueParser(new FloatParser());
        registerAttributeValueParser(new DoubleParser());
        registerAttributeValueParser(new BigIntegerParser());
        registerAttributeValueParser(new BigDecimalParser());
        this.objectType = objectType;
    }

    public <A> void registerAttribute(Attribute<O, A> attribute, AttributeValueParser<A> attributeValueParser) {
        attributes.put(attribute.getAttributeName(), attribute);
    }

    public <A> void registerAttributeValueParser(AttributeValueParser<A> attributeValueParser) {
        valueParsers.put(attributeValueParser.getAttributeType(), attributeValueParser);
    }

    public abstract Query<O> parse(String query);
}
