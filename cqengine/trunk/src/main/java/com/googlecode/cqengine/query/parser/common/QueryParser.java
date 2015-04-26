/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.query.parser.common;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.valuetypes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A service provider interface for parsers which can convert string queries to CQEngine native queries.
 * <p/>
 * Subclasses can implement this to common string-based queries in various dialects,
 * such as SQL or a string representation of a CQEngine native query.
 *
 * @author Niall Gallagher
 */
public abstract class QueryParser<O> {

    protected final Class<O> objectType;
    protected final Map<String, Attribute<O, ?>> attributes = new HashMap<String, Attribute<O, ?>>();
    protected final Map<Class<?>, ValueParser<?>> valueParsers = new HashMap<Class<?>, ValueParser<?>>();

    public QueryParser(Class<O> objectType) {
        registerValueParser(new BooleanParser());
        registerValueParser(new ByteParser());
        registerValueParser(new CharacterParser());
        registerValueParser(new ShortParser());
        registerValueParser(new IntegerParser());
        registerValueParser(new LongParser());
        registerValueParser(new FloatParser());
        registerValueParser(new DoubleParser());
        registerValueParser(new BigIntegerParser());
        registerValueParser(new BigDecimalParser());
        registerValueParser(new StringParser());
        this.objectType = objectType;
    }

    public <A> void registerAttribute(Attribute<O, A> attribute) {
        attributes.put(attribute.getAttributeName(), attribute);
    }

    public <A> void registerValueParser(ValueParser<A> valueParser) {
        valueParsers.put(valueParser.getValueType(), valueParser);
    }

    public Class<O> getObjectType() {
        return objectType;
    }

    public Attribute<O, ?> getRegisteredAttribute(String name) {
        Attribute<O, ?> attribute = attributes.get(name);
        if (attribute == null) {
            throw new IllegalStateException("No such attribute has been registered with the parser: " + name);
        }
        return attribute;
    }

    public <A> ValueParser<A> getValueParser(Class<A> attributeType) {
        @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
        ValueParser<A> parser = (ValueParser<A>) valueParsers.get(attributeType);
        if (parser == null) {
            throw new IllegalStateException("No value parser has been registered to parse type: " + attributeType.getName());
        }
        return parser;
    }

    /**
     * Utility to parse multiple queries. Delegates to {@link #parse(String)}.
     * @param queries The string queries to parse
     * @return A list of {@link Query} objects
     */
    public List<Query<O>> parse(List<String> queries) {
        List<Query<O>> parsedQueries = new ArrayList<Query<O>>(queries.size());
        for (String argument : queries) {
            parsedQueries.add(parse(argument));
        }
        return parsedQueries;
    }

    public abstract Query<O> parse(String query);
}
