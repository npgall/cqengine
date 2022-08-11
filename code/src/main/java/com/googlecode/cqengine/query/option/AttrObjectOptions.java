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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class AttrObjectOptions {

    final Map<Object, Object> attrStringOptions = new LinkedHashMap();

    public AttrObjectOptions(Collection<AttrObjectOption> attrObjectOptions) {
        for (AttrObjectOption attrObjectOption : attrObjectOptions) {
            this.attrStringOptions.put(attrObjectOption.key, attrObjectOption.value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttrObjectOptions)) {
            return false;
        }
        AttrObjectOptions that = (AttrObjectOptions) o;
        return attrStringOptions.equals(that.attrStringOptions);
    }

    @Override
    public int hashCode() {
        return attrStringOptions.hashCode();
    }

    public Object getAttrObjectOption(Object key) {
        return attrStringOptions.get(key);
    }

    @Override
    public String toString() {
        return attrStringOptions.toString();
    }

    public static Object getAttrObjectOption(QueryOptions queryOptions, Object key) {
        AttrObjectOptions attrObjectOptions = queryOptions.get(AttrObjectOptions.class);
        return attrObjectOptions == null ? null : attrObjectOptions.getAttrObjectOption(key);
    }
}
