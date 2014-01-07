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
package com.googlecode.cqengine.resultset.stored;

import com.googlecode.cqengine.resultset.ResultSet;

/**
 * An abstract ResultSet implemented by subclass ResultSets which are stored directly in indexes.
 * <p/>
 * Extends {@link ResultSet}, adding additional methods to add and remove objects.
 * <p/>
 * This implementation is abstract.
 *
 * @author Niall Gallagher
 */
public abstract class StoredResultSet<O> extends ResultSet<O> {

    public abstract boolean add(O o);

    public abstract boolean remove(O o);

    public abstract void clear();

    public abstract boolean isEmpty();

    public abstract boolean isNotEmpty();
}
