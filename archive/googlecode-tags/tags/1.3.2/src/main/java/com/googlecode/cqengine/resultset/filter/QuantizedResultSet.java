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
package com.googlecode.cqengine.resultset.filter;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;

/**
 * An implementation of {@link FilteringResultSet} which filters objects based on whether they match a given query.
 * <p/>
 * This can be useful to index which use a {@link com.googlecode.cqengine.quantizer.Quantizer}.
 *
 * @author Niall Gallagher
 */
public class QuantizedResultSet<O> extends FilteringResultSet<O> {

    private final Query<O> query;

    public QuantizedResultSet(ResultSet<O> wrappedResultSet, Query<O> query) {
        super(wrappedResultSet);
        this.query = query;
    }

    @Override
    public boolean isValid(O object) {
        return query.matches(object);
    }
}
