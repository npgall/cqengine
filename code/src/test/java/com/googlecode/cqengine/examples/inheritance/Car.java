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
package com.googlecode.cqengine.examples.inheritance;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.persistence.support.serialization.PersistenceConfig;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.List;

/**
 * A Car object which is configured with polymorphic = true.
 *
 * @author Niall Gallagher
 */
@PersistenceConfig(polymorphic = true)
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
    public static final SimpleAttribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("carId") {
        public Integer getValue(Car car, QueryOptions queryOptions) { return car.carId; }
    };

    public static final SimpleAttribute<Car, String> NAME = new SimpleAttribute<Car, String>("name") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.name; }
    };

    public static final SimpleAttribute<Car, String> DESCRIPTION = new SimpleAttribute<Car, String>("description") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.description; }
    };

    public static final Attribute<Car, String> FEATURES = new MultiValueAttribute<Car, String>("features") {
        public List<String> getValues(Car car, QueryOptions queryOptions) { return car.features; }
    };
}
