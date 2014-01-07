/**
 * Copyright 2012 Niall Gallagher
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
