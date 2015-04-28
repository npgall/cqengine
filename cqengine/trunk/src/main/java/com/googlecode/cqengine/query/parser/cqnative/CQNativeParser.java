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
import com.googlecode.cqengine.query.parser.antlr4.cqnative.CQEngineNativeLexer;
import com.googlecode.cqengine.query.parser.antlr4.cqnative.CQEngineNativeParser;
import com.googlecode.cqengine.query.parser.common.QueryParser;
import com.googlecode.cqengine.query.parser.common.InvalidQueryException;
import com.googlecode.cqengine.query.parser.cqnative.support.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * A parser for CQEngine native queries represented as strings.
 *
 * @author Niall Gallagher
 */
public class CQNativeParser<O> extends QueryParser<O> {

    public CQNativeParser(Class<O> objectType) {
        super(objectType);

    }

    static class ThrowingErrorListener extends BaseErrorListener {

        static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

       @Override
       public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
          throws ParseCancellationException {
             throw new InvalidQueryException("Failed to parse query at line " + line + ":" + charPositionInLine + ": " + msg);
          }
    }


    @Override
    public Query<O> parse(String query) {
        try {
            if (query == null) {
                throw new IllegalArgumentException("Query was null");
            }
            CQEngineNativeLexer lexer = new CQEngineNativeLexer(new ANTLRInputStream(query));
            lexer.removeErrorListeners();
            lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

            // Get a list of matched tokens
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Pass the tokens to the parser
            CQEngineNativeParser parser = new CQEngineNativeParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(ThrowingErrorListener.INSTANCE);

            // Specify our entry point
            CQEngineNativeParser.StartContext queryContext = parser.start();


            // Walk it and attach our listener
            ParseTreeWalker walker = new ParseTreeWalker();
            NativeQueryAntlrListener<O> listener = new NativeQueryAntlrListener<O>(this);
            walker.walk(listener, queryContext);
            return listener.getParsedQuery();
        }
        catch (InvalidQueryException e) {
            throw e;
        }
        catch (Exception e) {
            throw new InvalidQueryException("Failed to parse query", e);
        }

    }

}
