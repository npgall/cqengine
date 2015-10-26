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
package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.AttributeIndex;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.query.Query;

import java.util.*;

/**
 * A skeleton implementation of an index which implements the {@link Index#supportsQuery(Query)} method, based on a set
 * of queries supported by a subclass supplied to the constructor.
 *
 * @author Niall Gallagher
 *
 * @param <A> The type of the attribute on which this index will be built
 * @param <O> The type of the object containing the attribute
 */
public abstract class AbstractAttributeIndex<A, O> implements AttributeIndex<A, O> {

    private final Set<Class<? extends Query>> supportedQueries;

    protected final Attribute<O, A> attribute;

    /**
     * Protected constructor, called by subclasses.
     *
     * @param attribute The attribute on which the index will be built
     * @param supportedQueries The set of {@link Query} types which the subclass implementation supports
     */
    protected AbstractAttributeIndex(Attribute<O, A> attribute, Set<Class<? extends Query>> supportedQueries) {
        this.attribute = attribute;
        // Note: Ideally supportedQueries would be varargs to simplify subclasses, but varargs causes generic array
        // creation warnings.
        this.supportedQueries = Collections.unmodifiableSet(supportedQueries);
    }

    /**
     * Returns the attribute which was supplied to the constructor.
     *
     * @return the attribute which was supplied to the constructor
     */
    public Attribute<O, A> getAttribute() {
        return attribute;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsQuery(Query<O> query) {
        return supportedQueries.contains(query.getClass());
    }
}
