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
package com.googlecode.cqengine.query.parser.cqnative;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.QueryParser;
import com.googlecode.cqengine.query.parser.cqnative.support.*;

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

    protected final Map<String, QueryTypeParser<O>> queryTypeParsers = new HashMap<String, QueryTypeParser<O>>();

    public CQNativeParser(Class<O> objectType) {
        super(objectType);
        registerQueryTypeParser(new AndParser<O>());
        registerQueryTypeParser(new OrParser<O>());
        registerQueryTypeParser(new NotParser<O>());
        registerQueryTypeParser(new EqualParser<O>());
        // TODO: write parsers for other types of query: lessThan, stringContains etc.
    }

    public void registerQueryTypeParser(QueryTypeParser<O> queryTypeParser) {
        queryTypeParsers.put(queryTypeParser.getQueryType(), queryTypeParser);
    }

    public QueryTypeParser<O> getQueryTypeParser(String queryType) {
        QueryTypeParser<O> queryTypeParser = queryTypeParsers.get(queryType);
        if (queryTypeParser == null) {
            throw new IllegalStateException("No parser registered for query type: " + queryType);
        }
        return queryTypeParser;
    }

    @Override
    public Query<O> parse(String query) {
        QueryStructure structure = parseNativeQueryStructure(query);
        QueryTypeParser<O> queryTypeParser = getQueryTypeParser(structure.queryType);
        return queryTypeParser.parse(this, structure.queryArguments);
    }

    /**
     * Parses a query in native CQEngine programmatic syntax, "foo(a, b[, n...])", where foo is the query type,
     * and a, b, to n are arguments to the query foo.
     *
     * For example in query "and(equal(...), not(equal(...)))",
     * "and" is the type of the query, and strings "equal(...)" and "not(equal(...))" are its arguments.
     * <p/>
     * This method parses only the outer structure of the query, and not the internal structure of arguments.
     * The methods calling this method, can call this method recursively to parse arguments in the same way.
     *
     * @param query A query in native CQEngine programmatic syntax
     * @return The parsed structure of the outer components of the query
     */
    protected QueryStructure parseNativeQueryStructure(String query) {
        query = query.trim();
        int closingParenthesis = query.lastIndexOf(")");
        int openingParenthesis = query.indexOf("(");
        if (openingParenthesis < 2) {
            throw new IllegalStateException("Invalid query type: " + query);
        }
        if (closingParenthesis < 3 || closingParenthesis != query.length() - 1) {
            throw new IllegalStateException("Invalid query arguments: " + query);
        }
        String queryType = query.substring(0, openingParenthesis);
        if (queryType.contains(",") || queryType.contains("(") || queryType.contains(")")) {
            throw new IllegalStateException("Invalid query type: " + query);
        }
        String argumentsStr = query.substring(openingParenthesis + 1, closingParenthesis);
        List<String> arguments = new ArrayList<String>();

        boolean inQuotes = false;
        int parenthesesDepth = 0;
        char current = ' ', last;
        int lastComma = -1;
        for (int i = 0; i < argumentsStr.length(); i++) {
            last = current;
            current = argumentsStr.charAt(i);
            if (current == '"' && last != '\\') {
                inQuotes = !inQuotes;
            }
            if (current == '(' && !inQuotes) {
                parenthesesDepth++;
            }
            else if (current == ')' && !inQuotes) {
                parenthesesDepth--;
                if (parenthesesDepth < 0) {
                    throw new IllegalStateException("Too many closing parentheses: " + argumentsStr);
                }
            }
            if (!inQuotes && parenthesesDepth == 0 && current == ',') {
                String argument = argumentsStr.substring(lastComma + 1, i).trim();
                if (argument.length() == 0) {
                    throw new IllegalStateException("An argument is blank: " + argumentsStr);
                }
                arguments.add(argument);
                lastComma = i;
            }
        }
        if (parenthesesDepth != 0) {
            throw new IllegalStateException("Too many opening parentheses: " + argumentsStr);
        }
        String argument = argumentsStr.substring(lastComma + 1).trim();
        if (argument.length() == 0) {
            throw new IllegalStateException("An argument is blank: " + argumentsStr);
        }
        arguments.add(argument);
        return new QueryStructure(queryType, arguments);
    }

    static class QueryStructure {
        final String queryType; // "and", "or", "not", "lessThan" etc.
        final List<String> queryArguments; // list of comma-separated arguments to this outer query

        QueryStructure(String queryType, List<String> queryArguments) {
            this.queryType = queryType;
            this.queryArguments = queryArguments;
        }

        @Override
        public String toString() {
            return "QueryStructure{" +
                    "queryType='" + queryType + '\'' +
                    ", queryArguments=" + queryArguments +
                    '}';
        }
    }
}
