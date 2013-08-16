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
package com.googlecode.cqengine.examples.replace;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A modified implementation of a Car object, which implements automatic versioning for use with the
 * concurrent replace approach in the {@link Replace} example.
 *
 * @author Niall Gallagher
 */
public class Car {

    static final AtomicLong VERSION_GENERATOR = new AtomicLong();

    final int carId;
    final String name;
    // Version field should have a value such that if two objects with the same carId are created,
    // their version numbers will be different.
    // We cheat on this point, by simply assigning a unique version number to every car...
    final long version = VERSION_GENERATOR.incrementAndGet();

    public Car(int carId, String name) {
        this.carId = carId;
        this.name = name;
    }

    // Return hashCode as normal, ignoring the version field...
    @Override
    public int hashCode() {
        return carId;
    }

    // Implement equals, to return true only if carIds are equal AND version fields are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car other = (Car) o;
        return this.carId == other.carId && this.version == other.version;
    }

    @Override
    public String toString() {
        return "Car{carId=" + carId + ", name='" + name + "', version=" + version + "}";
    }

    // -------------------------- Attributes --------------------------
    public static final Attribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("carId") {
        public Integer getValue(Car car) { return car.carId; }
    };

}
