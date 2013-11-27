package com.googlecode.cqengine.query.parser.cqnative.support;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.common.QueryParser;

import java.util.List;

/**
 * @author Niall Gallagher
 */
public interface QueryTypeParser<O> {

    String getQueryType();

    <A> Query<O> parse(QueryParser<O> queryParser, List<String> arguments);
}
