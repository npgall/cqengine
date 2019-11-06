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
package com.googlecode.cqengine.index.support;

import com.googlecode.cqengine.metadata.KeyFrequency;

/**
 * Statistics of an index key.
 *
 * @author niall.gallagher
 */
public class KeyStatistics<A> implements KeyFrequency<A> {

    final A key;
    final Integer count;

    public KeyStatistics(A key, Integer count) {
        this.key = key;
        this.count = count;
    }

    public A getKey() {
        return key;
    }

    public Integer getCount() {
        return count;
    }

    /**
     * Equivalent to {@link #getCount()}.
     */
    @Override
    public int getFrequency() {
        return getCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KeyStatistics)) {
            return false;
        }

        KeyStatistics<?> that = (KeyStatistics<?>) o;

        if (count != that.count) {
            return false;
        }
        return key.equals(that.key);

    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return key + " " + count;
    }
}
