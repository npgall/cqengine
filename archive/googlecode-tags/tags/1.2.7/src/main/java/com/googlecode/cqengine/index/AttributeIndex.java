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
package com.googlecode.cqengine.index;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.engine.ModificationListener;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;

/**
 * @author Niall Gallagher
 */
public interface AttributeIndex<A, O> extends Index<O> {

    /**
     * Returns the attribute indexed by this index.
     *
     * @return The attribute indexed by this index
     */
    public Attribute<O, A> getAttribute();
}
