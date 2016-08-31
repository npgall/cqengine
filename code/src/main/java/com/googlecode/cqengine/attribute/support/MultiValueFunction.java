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

import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.MultiValueNullableAttribute;

/**
 * A functional interface which, when used with Java 8+, allows
 * CQEngine attributes {@link MultiValueAttribute} and {@link MultiValueNullableAttribute}
 * to be created from lambda expressions.
 *
 * @author npgall
 */
public interface MultiValueFunction<O, A, I extends Iterable<A>> {

    /**
     * Applies this function to the given object.
     *
     * @param object the function argument
     * @return the function result
     */
    I apply(O object);
}