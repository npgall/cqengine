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
package com.googlecode.cqengine.testutil;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.persistence.disk.DiskPersistence;

/**
 * For testing purposes - a {@link ConcurrentIndexedCollection} hard-wired to use disk persistence.
 *
 * @author niall.gallagher
 */
public class DiskConcurrentIndexedCollection extends ConcurrentIndexedCollection<Car> {

    public DiskConcurrentIndexedCollection() {
        super(DiskPersistence.onPrimaryKey(Car.CAR_ID));
    }
}
