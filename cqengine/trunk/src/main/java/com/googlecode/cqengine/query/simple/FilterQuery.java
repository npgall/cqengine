package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * Extracting a value from an object via an {@link com.googlecode.cqengine.attribute.Attribute} can be expensive,
 * for example a compressed and encode text.
 * An {@link com.googlecode.cqengine.index.Index} can support {@link FilterQuery} and filter the already computed values removing
 * the overhead of recomputing the value.
 *
 * @author Silvano Riz
 */
public interface FilterQuery<O, A> extends Query<O>{

     /**
      * Asserts the value matches the query
      * @param value The value to check. It can come from an {@link com.googlecode.cqengine.attribute.Attribute} or from any other source, for example an {@link com.googlecode.cqengine.index.Index}
      * @param queryOptions The {@link QueryOptions}
      * @return true if the value matches the query, false otherwise.
      */
     boolean matchesValue(A value, QueryOptions queryOptions);
}
