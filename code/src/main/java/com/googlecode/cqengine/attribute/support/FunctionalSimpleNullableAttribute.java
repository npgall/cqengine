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
package com.googlecode.cqengine.attribute.support;

import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * A {@link SimpleNullableAttribute} which wraps a {@link SimpleFunction}, for the purpose of allowing
 * attributes to be created from lambda expressions.
 * <p/>
 * These attributes can be created via {@link QueryFactory#nullableAttribute(SimpleFunction)}.
 *
 * @author npgall
 */
public class FunctionalSimpleNullableAttribute<O, A> extends SimpleNullableAttribute<O, A> {

    final SimpleFunction<O, A> function;

    public FunctionalSimpleNullableAttribute(Class<O> objectType, Class<A> attributeType, String attributeName, SimpleFunction<O, A> function) {
        super(objectType, attributeType, attributeName);
        this.function = function;
    }

    @Override
    public A getValue(O object, QueryOptions queryOptions) {
        return function.apply(object);
    }
}
