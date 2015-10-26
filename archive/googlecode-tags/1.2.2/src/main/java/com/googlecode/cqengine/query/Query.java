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
package com.googlecode.cqengine.query;

/**
 * An interface implemented by all {@link Query} subclasses, both those descending from
 * {@link com.googlecode.cqengine.query.simple.SimpleQuery} and from
 * {@link com.googlecode.cqengine.query.logical.LogicalQuery}.
 *
 * @author ngallagher
 * @since 2012-04-30 16:52
 */
public interface Query<O> {

    /**
     * Tests an object to see if it matches the assertion represented by the query.
     *
     * @param object The object to test
     * @return True if the object matches the query, false if it does not
     */
    boolean matches(O object);
}
