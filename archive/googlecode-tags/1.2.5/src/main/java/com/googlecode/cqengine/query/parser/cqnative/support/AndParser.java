package com.googlecode.cqengine.query.parser.cqnative.support;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.QueryParser;

import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.and;

/**
 * @author Niall Gallagher
 */
public class AndParser<O> implements QueryTypeParser<O> {

    @Override
    public String getQueryType() {
        return "and";
    }

    @Override
    public <A> Query<O> parse(QueryParser<O> queryParser, List<String> arguments) {
        return and(queryParser.parse(arguments));
    }
}
