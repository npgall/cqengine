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
package com.googlecode.cqengine.examples.introduction;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

import java.util.List;

/**
 * @author Niall Gallagher
 */
public class Car {
    public final int carId;
    public final String name;
    public final String description;
    public final List<String> features;

    public Car(int carId, String name, String description, List<String> features) {
        this.carId = carId;
        this.name = name;
        this.description = description;
        this.features = features;
    }

    @Override
    public String toString() {
        return "Car{carId=" + carId + ", name='" + name + "', description='" + description + "', features=" + features + "}";
    }

    // -------------------------- Attributes --------------------------
    public static final Attribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("carId") {
        public Integer getValue(Car car) { return car.carId; }
    };

    public static final Attribute<Car, String> NAME = new SimpleAttribute<Car, String>("name") {
        public String getValue(Car car) { return car.name; }
    };

    public static final Attribute<Car, String> DESCRIPTION = new SimpleAttribute<Car, String>("description") {
        public String getValue(Car car) { return car.description; }
    };

    public static final Attribute<Car, String> FEATURES = new MultiValueAttribute<Car, String>("features") {
        public List<String> getValues(Car car) { return car.features; }
    };
}
