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

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

/**
 * @author Niall Gallagher
 */
public class Car {

    public static final Attribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("carId") {
        public Integer getValue(Car car) { return car.carId; }
    };

    public static final Attribute<Car, String> MANUFACTURER = new SimpleAttribute<Car, String>("manufacturer") {
        public String getValue(Car car) { return car.manufacturer; }
    };

    public static final Attribute<Car, String> MODEL = new SimpleAttribute<Car, String>("model") {
        public String getValue(Car car) { return car.model; }
    };

    public static final Attribute<Car, Color> COLOR = new SimpleAttribute<Car, Color>("color") {
        public Color getValue(Car car) { return car.color; }
    };

    public static final Attribute<Car, Integer> DOORS = new SimpleAttribute<Car, Integer>("doors") {
        public Integer getValue(Car car) { return car.doors; }
    };

    public static final Attribute<Car, Double> PRICE = new SimpleAttribute<Car, Double>("price") {
        public Double getValue(Car car) { return car.price; }
    };

    public enum Color {RED, GREEN, BLUE, BLACK, WHITE}
    private final int carId;
    private final String manufacturer;
    private final String model;
    private final Color color;
    private final int doors;
    private final double price;

    public Car(int carId, String manufacturer, String model, Color color, int doors, double price) {
        this.carId = carId;
        this.manufacturer = manufacturer;
        this.model = model;
        this.color = color;
        this.doors = doors;
        this.price = price;
    }

    public int getCarId() {
        return carId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public Color getColor() {
        return color;
    }

    public int getDoors() {
        return doors;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", color=" + color +
                ", doors=" + doors +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (carId != car.carId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return carId;
    }
}
