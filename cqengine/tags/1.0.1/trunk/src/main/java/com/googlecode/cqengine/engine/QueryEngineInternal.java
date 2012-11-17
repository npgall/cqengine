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
package com.googlecode.cqengine.engine;

/**
 * @author Niall Gallagher
 */
public interface QueryEngineInternal<O> extends QueryEngine<O>, ModificationListener<O> {

    /**
     * Indicates if all indexes maintained by the query engine are mutable.
     * Returns true in the edge case that there are no indexes.
     *
     * @return True if all indexes are mutable, false if any indexes are not mutable
     */
    public boolean isMutable();
}
