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

/**
 * A wrapper around a key and an associated {@link Double} value, representing the value for that threshold key
 * which is to be applied to tune CQEngine query performance. These thresholds can be supplied as query options.
 * <p/>
 * See {@link EngineThresholds} for information about some thresholds which can be set.
 *
 * @author niall.gallagher
 */
public class Threshold {

    final Object key;
    final Double value;

    public Threshold(Object key, Double value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Threshold)) {
            return false;
        }

        Threshold threshold = (Threshold) o;

        if (!key.equals(threshold.key)) {
            return false;
        }
        return value.equals(threshold.value);

    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
