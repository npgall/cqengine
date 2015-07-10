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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * Extracting a value from an object via an {@link Attribute} can sometimes be expensive, for example if the attribute
 * is <i>virtual</i> wherein the data it reads is not already stored in memory, but has to be uncompressed or decoded
 * on-the-fly, or if it reads the data on-the-fly from a remote datasource.
 * <p/>
 * Often there may be an index on such attributes available locally, wherein a copy of the attribute values would be
 * available locally in already-decoded form. Indexes would typically report that they can accelerate standard CQEngine
 * queries on those attributes. However some queries cannot be accelerated by indexes in a straightforward manner; for
 * example queries which perform regular expressions or some other function on the raw data. Indexes are unlikely
 * to report that they can natively accelerate regular expression queries, because regular expressions must be evaluated
 * by filtering. Therefore CQEngine will typically fall back to evaluating regular expression queries on the fly by
 * filtering values returned by <i>the attribute</i> from which the query reads. If reading from the attribute is
 * expensive, then it makes sense to allow the query to filter data from the index instead.
 * <p/>
 * Queries which implement this interface are evaluated by filtering the data contained in an index built on
 * the attribute, as opposed to the data returned by the attribute.
 * <p/>
 * Note that most standard CQEngine queries do not implement this interface, because CQEngine cannot know how expensive
 * user-defined attributes are (plus, ordinarily reading from attributes is cheap). If the application requires this
 * behaviour for some queries, it can subclass some of the existing queries and have them implement this interface,
 * or it can define custom queries which implement this interface.
 * <p/>
 * Note that the existing {@link StringMatchesRegex} and {@link StringEndsWith} queries are already compatible with this
 * interface. To have those queries answered by filtering data in an index instead of from an attribute, <i>subclass
 * those queries and declare that they implement this interface</i>. They already implement the required
 * {@link #matchesValue(Object, QueryOptions)} method.
 *
 * @author Silvano Riz
 */
public interface FilterQuery<O, A> extends Query<O>{

     /**
      * Asserts the value matches the query.
      *
      * @param value The value to check. It can come from an {@link Attribute} or from any other source, for example an
      * {@link com.googlecode.cqengine.index.Index}
      * @param queryOptions The {@link QueryOptions}
      * @return true if the value matches the query, false otherwise
      */
     boolean matchesValue(A value, QueryOptions queryOptions);
}
