package com.googlecode.cqengine.query.parser.cqnative.support;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.AttributeValueParser;
import com.googlecode.cqengine.query.parser.common.QueryParser;

import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * @author Niall Gallagher
 */
public class EqualParser<O> implements QueryTypeParser<O> {

    @Override
    public String getQueryType() {
        return "equal";
    }

    @Override
    public <A> Query<O> parse(QueryParser<O> queryParser, List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalStateException("Invalid number of arguments for 'equal' query: " + arguments);
        }
        // First argument should be attribute, second argument should be a value for the attribute...
        @SuppressWarnings({"unchecked"})
        Attribute<O, A> attribute = (Attribute<O, A>) queryParser.getRegisteredAttribute(arguments.get(0));
        AttributeValueParser<A> valueParser = queryParser.getAttributeValueParser(attribute.getAttributeType());
        A attributeValue = valueParser.parseToAttributeType(arguments.get(1));
        return equal(attribute, attributeValue);
    }
}
