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
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.List;

/**
 * @author Niall Gallagher
 */
public class SportsCar extends Car {

    final int horsepower;

    public SportsCar(int carId, String name, String description, List<String> features, int horsepower) {
        super(carId, name, description, features);
        this.horsepower = horsepower;
    }

    @Override
    public String toString() {
        return "SportsCar{carId=" + carId + ", name='" + name + "', description='" + description + "', features=" + features + ", horsepower=" + horsepower + "}";
    }

    public static final Attribute<Car, Integer> HORSEPOWER = new SimpleNullableAttribute<Car, Integer>("horsepower") {
        public Integer getValue(Car car, QueryOptions queryOptions) { return car instanceof SportsCar ? ((SportsCar)car).horsepower : null; }
    };
}
