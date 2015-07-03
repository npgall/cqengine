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
package com.googlecode.cqengine.index;

import com.googlecode.cqengine.engine.ModificationListener;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

/**
 * @author Niall Gallagher
 */
public interface Index<O> extends ModificationListener<O> {
    
    /**
     * Indicates if objects can be added to or removed from the index after the index has been built.
     *
     * @return True if objects can be added to or removed from the index after the index has been built, false if
     * the index cannot be modified after it is built
     */
    public boolean isMutable();

    /**
     * Indicates if the index can perform retrievals for the type of query supplied.
     *
     *
     * @param query A query to check
     * @return True if the index can perform retrievals for the type of query supplied, false if it does not
     * common this type of query
     */
    public boolean supportsQuery(Query<O> query);

    /**
     * Indicates if the index is quantized, using a {@link com.googlecode.cqengine.quantizer.Quantizer}.
     * @return True if the index is quantized, false if not.
     */
    public boolean isQuantized();

    /**
     * Returns a {@link ResultSet} which when iterated will return objects from the index matching the query
     * supplied.
     * <p/>
     * Usually {@code ResultSet}s are <i>lazy</i> which means that they don't actually do any work, or encapsulate or
     * <i>materialize</i> matching objects, but rather they encapsulate logic to fetch matching objects from the index
     * on-the-fly as the application iterates through the {@code ResultSet}.
     *
     * @param query An object which specifies some restriction on an attribute of an object
     * @param queryOptions Optional parameters for the query
     * @return A set of objects with attributes matching the restriction imposed by the query
     * @throws IllegalArgumentException if the index does not common the given query
     * @see #supportsQuery(com.googlecode.cqengine.query.Query)
     */
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions);

}
