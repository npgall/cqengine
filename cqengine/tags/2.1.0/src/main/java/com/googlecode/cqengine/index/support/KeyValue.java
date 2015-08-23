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

/**
 * Provides access to a key-value pair.
 * <p/>
 * The implementation is free to generate and encapsulate the key and value in this object eagerly,
 * or generated it on-demand lazily.
 *
 * @author niall.gallagher
 */
public interface KeyValue<A, O> {

    A getKey();

    O getValue();
}
