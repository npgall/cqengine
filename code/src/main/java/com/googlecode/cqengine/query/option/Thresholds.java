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

import com.googlecode.cqengine.query.QueryFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A wrapper around {@link Threshold}s which have been set into query options.
 * <p/>
 * Example usage using {@link QueryFactory}:<br/>
 * <code>QueryOptions queryOptions = queryOptions(applyThresholds(INDEX_ORDERING_SELECTIVITY, 0.4))</code>
 *
 * @author niall.gallagher
 */
public class Thresholds {

    final Map<Object, Double> thresholds = new LinkedHashMap<Object, Double>();

    public Thresholds(Collection<Threshold> thresholds) {
        for (Threshold threshold : thresholds) {
            this.thresholds.put(threshold.key, threshold.value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Thresholds)) {
            return false;
        }
        Thresholds that = (Thresholds) o;
        return thresholds.equals(that.thresholds);
    }

    @Override
    public int hashCode() {
        return thresholds.hashCode();
    }

    public Double getThreshold(Object key) {
        return thresholds.get(key);
    }

    @Override
    public String toString() {
        return thresholds.toString();
    }

    public static Double getThreshold(QueryOptions queryOptions, Object key) {
        Thresholds thresholds = queryOptions.get(Thresholds.class);
        return thresholds == null ? null : thresholds.getThreshold(key);
    }
}
