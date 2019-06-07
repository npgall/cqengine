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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SelfAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import static com.googlecode.cqengine.query.support.QueryValidation.checkObjectTypeNotNull;

/**
 * A query which matches all objects in the collection.
 * <p/>
 * This is equivalent to a literal boolean 'true'.
 *
 * @author ngallagher
 */
public class All<O> extends SimpleQuery<O, O> {

    final Class<O> objectType;

    public All(Class<O> objectType) {
        super(new SelfAttribute<O>(checkObjectTypeNotNull(objectType), "all"));
        this.objectType = objectType;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, O> attribute, O object, QueryOptions queryOptions) {
        return true;
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, O> attribute, O object, QueryOptions queryOptions) {
        return true;
    }

    @Override
    protected int calcHashCode() {
        return 765906512; // chosen randomly
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof All)) return false;
        All that = (All) o;
        return this.objectType.equals(that.objectType);
    }

    @Override
    public String toString() {
        return "all(" + super.getAttribute().getObjectType().getSimpleName() + ".class)";
    }
}