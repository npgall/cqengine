package com.googlecode.cqengine.query.option;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates a map of optional key-value parameters supplied by the application to the query engine, attributes
 * and indexes.
 * <p/>
 * These parameters can request specific behaviour from the query engine such as specifying transaction
 * isolation levels.
 * <p/>
 * Query options also allow the application to pass arbitrary or request-scope objects to custom Attributes or Indexes.
 */
public class QueryOptions {

    final Map<Object, Object> options;

    public QueryOptions(Map<Object, Object> options) {
        this.options = options;
    }

    public QueryOptions() {
        this.options = new HashMap<Object, Object>();
    }

    public Map<Object, Object> getOptions() {
        return Collections.unmodifiableMap(this.options);
    }

    public <T> T get(Class<T> optionType) {
        @SuppressWarnings("unchecked")
        T optionValue = (T) get((Object)optionType);
        return optionValue;
    }

    public Object get(Object key) {
        return options.get(key);
    }

    public void put(Object key, Object value) {
        options.put(key, value);
    }


    static final QueryOptions EMPTY_OPTIONS = new QueryOptions(Collections.emptyMap());

    public static QueryOptions noQueryOptions() {
        return EMPTY_OPTIONS;
    }

    @Override
    public String toString() {
        return "queryOptions(" + options + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryOptions)) return false;

        QueryOptions that = (QueryOptions) o;

        if (!options.equals(that.options)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return options.hashCode();
    }
}
