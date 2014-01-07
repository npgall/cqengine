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
