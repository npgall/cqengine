package com.googlecode.cqengine.query.parser;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract implementation of a parser which converts string queries to CQEngine native queries.
 * <p/>
 * Subclasses can implement this to support string-based queries in various dialects,
 * such as SQL or a string representation of a CQEngine native query.
 *
 * @author Niall Gallagher
 */
public abstract class QueryParser<O> {

    protected final Class<O> objectType;
    protected final Map<String, Attribute<O, ?>> attributes = new HashMap<String, Attribute<O, ?>>();

    public QueryParser(Class<O> objectType) {
        this.objectType = objectType;
    }

    public void registerAttribute(Attribute<O, ?> attribute) {
        attributes.put(attribute.getAttributeName(), attribute);
    }

    public abstract Query<O> parse(String query);
}
