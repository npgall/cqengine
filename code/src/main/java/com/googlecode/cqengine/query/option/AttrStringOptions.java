/**
 * Copyright 2012-2015 Niall Gallagher
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.query.option;

import com.googlecode.cqengine.query.QueryFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class AttrStringOptions {

    final Map<Object, String> attrStringOptions = new LinkedHashMap();

    public AttrStringOptions(Collection<AttrStringOption> attrStringOptions) {
        for (AttrStringOption attrStringOption : attrStringOptions) {
            this.attrStringOptions.put(attrStringOption.key, attrStringOption.value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttrStringOptions)) {
            return false;
        }
        AttrStringOptions that = (AttrStringOptions) o;
        return attrStringOptions.equals(that.attrStringOptions);
    }

    @Override
    public int hashCode() {
        return attrStringOptions.hashCode();
    }

    public String getAttrStringOption(Object key) {
        return attrStringOptions.get(key);
    }

    @Override
    public String toString() {
        return attrStringOptions.toString();
    }

    public static String getAttrStringOption(QueryOptions queryOptions, Object key) {
        AttrStringOptions attrStringOptions = queryOptions.get(AttrStringOptions.class);
        return attrStringOptions == null ? null : attrStringOptions.getAttrStringOption(key);
    }
}
