package com.googlecode.cqengine.query.parser.cqnative;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.support.QueryParser;

import java.util.ArrayList;
import java.util.List;

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
        throw new UnsupportedOperationException("Not implemented");
    }

    static QueryStruct parseStruct(String query) {
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
        return new QueryStruct(queryType, arguments);
    }

    static class QueryStruct {
        final String queryType; // "and", "or", "not", "lessThan" etc.
        final List<String> arguments; // list of comma-separated arguments to this outer query

        QueryStruct(String queryType, List<String> arguments) {
            this.queryType = queryType;
            this.arguments = arguments;
        }

        @Override
        public String toString() {
            return "QueryStruct{" +
                    "queryType='" + queryType + '\'' +
                    ", arguments=" + arguments +
                    '}';
        }
    }
}
