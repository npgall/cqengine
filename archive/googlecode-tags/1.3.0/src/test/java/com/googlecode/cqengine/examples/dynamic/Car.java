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
package com.googlecode.cqengine.examples.dynamic;

/**
 * @author Niall Gallagher
 */
public class Car {
    public final Integer carId;
    public final String manufacturer;
    public final String model;
    public final Integer doors;
    public final Integer horsepower;

    public Car(Integer carId, String manufacturer, String model, Integer doors, Integer horsepower) {
        this.carId = carId;
        this.manufacturer = manufacturer;
        this.model = model;
        this.doors = doors;
        this.horsepower = horsepower;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", doors=" + doors +
                ", horsepower=" + horsepower +
                '}';
    }
}
