package com.googlecode.cqengine.query.parser.cqnative.support;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.QueryParser;

import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.not;

/**
 * @author Niall Gallagher
 */
public class NotParser<O> implements QueryTypeParser<O> {

    @Override
    public String getQueryType() {
        return "not";
    }

    @Override
    public <A> Query<O> parse(QueryParser<O> queryParser, List<String> arguments) {
        if (arguments.size() != 1) {
            throw new IllegalStateException("Invalid number of arguments for 'not' query: " + arguments);
        }
        return not(queryParser.parse(arguments.get(0)));
    }
}
