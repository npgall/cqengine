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

/**
 * A generic factory for an object of type T.
 * <p/>
 * This is "inspired" by Guava's Supplier or Guice's Provider interface. The purpose of this CQEngine-specific
 * interface is allow a degree of dependency injection/decoupling internally in CQEngine, without imposing a
 * dependency on a particular dependency injection framework on the application.
 *
 * @author Niall Gallagher
 */
public interface Factory<T> {

    /**
     * Creates an object.
     * @return a new instance of the object
     */
    public T create();
}
