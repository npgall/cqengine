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
package com.googlecode.cqengine.attribute;

/**
 * An attribute which returns the object itself.
 * <p/>
 * This can be useful when performing queries on objects in the collection itself (rather than on fields in the
 * objects in the collection). Typical use case would be for performing <code>startsWith</code> queries on an
 * <code>IndexedCollection&lt;String&gt;</code>.
 *
 * @author Niall Gallagher
 */
public class SelfAttribute<O> extends SimpleAttribute<O, O> {

    public SelfAttribute(Class<O> objectType, String attributeName) {
        super(objectType, objectType, attributeName);
    }

    public SelfAttribute(Class<O> objectType) {
        super(objectType, objectType);
    }

    @Override
    public O getValue(O object) {
        return object;
    }
}
