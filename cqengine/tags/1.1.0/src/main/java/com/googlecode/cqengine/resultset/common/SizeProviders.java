/**
 * Copyright 2012 Niall Gallagher
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
package com.googlecode.cqengine.resultset.common;

import com.googlecode.cqengine.resultset.iterator.IteratorUtil;

import java.util.Collection;

/**
 * @author Niall Gallagher
 */
public class SizeProviders {

    private static final SizeProvider ZERO_PROVIDER = new SizeProvider() {
        @Override
        public int size() {
            return 0;
        }
    };

    /**
     * Private constructor, not used.
     */
    SizeProviders() {
    }

    public static SizeProvider forEmptySet() {
        return ZERO_PROVIDER;
    }

    public static SizeProvider forIterable(final Iterable<?> iterable) {
        return new SizeProvider() {
            @Override
            public int size() {
                return IteratorUtil.countElements(iterable);
            }
        };
    }

    public static SizeProvider forCollection(final Collection<?> collection) {
        return new SizeProvider() {
            @Override
            public int size() {
                return collection.size();
            }
        };
    }
}
