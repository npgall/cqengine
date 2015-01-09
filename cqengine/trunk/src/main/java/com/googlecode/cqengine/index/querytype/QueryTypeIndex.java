package com.googlecode.cqengine.index.querytype;

import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.query.Query;

import java.util.Set;

/**
 * An index that can evaluate a specific type of query(ies)
 *
 * @author Silvano Riz
 */
public interface QueryTypeIndex<O> extends Index<O> {

    /**
     * Returns the types of the supported queries
     *
     * @return The type of the supported queries.
     */
    public Set<Class<? extends Query>> getSupportedQueryTypes();

}
