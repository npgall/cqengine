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
package com.googlecode.cqengine.query.parser.cqn;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.ParseResult;
import com.googlecode.cqengine.query.parser.common.QueryParser;
import com.googlecode.cqengine.query.parser.common.InvalidQueryException;
import com.googlecode.cqengine.query.parser.cqn.grammar.CQNGrammarLexer;
import com.googlecode.cqengine.query.parser.cqn.grammar.CQNGrammarParser;
import com.googlecode.cqengine.query.parser.cqn.support.*;
import com.googlecode.cqengine.query.parser.cqn.support.StringParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Map;

/**
 * A parser for CQN queries - CQEngine-Native syntax.
 * <p/>
 * CQN syntax is based on how CQEngine queries look in native Java code, and the format returned by
 * {@link Query#toString()}.
 *
 * @author Niall Gallagher
 */
public class CQNParser<O> extends QueryParser<O> {

    public CQNParser(Class<O> objectType) {
        super(objectType);
        StringParser stringParser = new StringParser();
        super.registerValueParser(String.class, stringParser);
        super.registerFallbackValueParser(new FallbackValueParser(stringParser));
    }

    @Override
    public ParseResult<O> parse(String query) {
        try {
            if (query == null) {
                throw new IllegalArgumentException("Query was null");
            }
            CQNGrammarLexer lexer = new CQNGrammarLexer(new ANTLRInputStream(query));
            lexer.removeErrorListeners();
            lexer.addErrorListener(SYNTAX_ERROR_LISTENER);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            CQNGrammarParser parser = new CQNGrammarParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(SYNTAX_ERROR_LISTENER);

            CQNGrammarParser.StartContext queryContext = parser.start();

            ParseTreeWalker walker = new ParseTreeWalker();
            CQNAntlrListener<O> listener = new CQNAntlrListener<O>(this);
            walker.walk(listener, queryContext);
            return new ParseResult<O>(listener.getParsedQuery(), listener.getQueryOptions());
        }
        catch (InvalidQueryException e) {
            throw e;
        }
        catch (Exception e) {
            throw new InvalidQueryException("Failed to parse query", e);
        }
    }

    /**
     * Creates a new CQNParser for the given POJO class.
     * @param pojoClass The type of object stored in the collection
     * @return a new CQNParser for the given POJO class
     */
    public static <O> CQNParser<O> forPojo(Class<O> pojoClass) {
        return new CQNParser<O>(pojoClass);
    }

    /**
     * Creates a new CQNParser for the given POJO class, and registers the given attributes with it.
     * @param pojoClass The type of object stored in the collection
     * @param attributes The attributes to register with the parser
     * @return a new CQNParser for the given POJO class
     */
    public static <O> CQNParser<O> forPojoWithAttributes(Class<O> pojoClass, Map<String, ? extends Attribute<O, ?>> attributes) {
        CQNParser<O> parser = forPojo(pojoClass);
        parser.registerAttributes(attributes);
        return parser;
    }
}
