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
package com.googlecode.cqengine.query.parser.common;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * Encapsulates the result of parsing a string-based query: the parsed query itself, and any query options associated
 * with the query.
 * <p/>
 * Query options specify optional non-query parameters which might have been parsed along with the query, such as how
 * results should be ordered etc.
 *
 * @author niall.gallagher
 */
public class ParseResult<O> {

    final Query<O> query;
    final QueryOptions queryOptions;

    public ParseResult(Query<O> query, QueryOptions queryOptions) {
        this.query = query;
        this.queryOptions = queryOptions;
    }

    /**
     * Returns the parsed query.
     * @return The parsed query (never null).
     */
    public Query<O> getQuery() {
        return query;
    }

    /**
     * Returns the parsed query options, which may include {@link com.googlecode.cqengine.query.option.OrderByOption}
     * if ordering was specified in the string query.
     * <p/>
     * If no query options were specified in the string then the returned query options will not be null, but will be
     * empty.
     *
     * @return The parsed query options.
     */
    public QueryOptions getQueryOptions() {
        return queryOptions;
    }
}
