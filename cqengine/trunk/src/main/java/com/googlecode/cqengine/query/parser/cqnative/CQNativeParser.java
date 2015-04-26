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
package com.googlecode.cqengine.query.parser.cqnative;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.antlr4.cqnative.NativeQueryLexer;
import com.googlecode.cqengine.query.parser.antlr4.cqnative.NativeQueryParser;
import com.googlecode.cqengine.query.parser.common.QueryParser;
import com.googlecode.cqengine.query.parser.cqnative.support.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A parser for CQEngine native queries represented as strings.
 *
 * @author Niall Gallagher
 */
public class CQNativeParser<O> extends QueryParser<O> {

    public CQNativeParser(Class<O> objectType) {
        super(objectType);

    }

    @Override
    public Query<O> parse(String query) {
        NativeQueryLexer lexer = new NativeQueryLexer(new ANTLRInputStream(query));

        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Pass the tokens to the parser
        NativeQueryParser parser = new NativeQueryParser(tokens);
    parser.setErrorHandler(new BailErrorStrategy());

        // Specify our entry point
        NativeQueryParser.QueryContext queryContext = parser.query();


        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        NativeQueryAntlrListener<O> listener = new NativeQueryAntlrListener<O>(this);
        walker.walk(listener, queryContext);
        return listener.getParsedQuery();
    }

}
