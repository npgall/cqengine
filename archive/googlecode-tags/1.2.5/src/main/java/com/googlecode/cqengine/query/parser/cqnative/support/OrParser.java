package com.googlecode.cqengine.query.parser.cqnative.support;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.QueryParser;

import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author Niall Gallagher
 */
public class OrParser<O> implements QueryTypeParser<O> {

    @Override
    public String getQueryType() {
        return "or";
    }

    @Override
    public <A> Query<O> parse(QueryParser<O> queryParser, List<String> arguments) {
        return or(queryParser.parse(arguments));
    }
}
