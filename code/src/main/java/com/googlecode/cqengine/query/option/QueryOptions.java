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
package com.googlecode.cqengine.query.option;

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
        return this.options;
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

    public void remove(Object key) {
        options.remove(key);
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
