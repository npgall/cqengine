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
package com.googlecode.cqengine.testutil;

import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.List;

/**
 * @author Niall Gallagher
 */
public class Car {

    public static final SimpleAttribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("carId") {
        public Integer getValue(Car car, QueryOptions queryOptions) { return car.carId; }
    };

    public static final SimpleAttribute<Car, String> MANUFACTURER = new SimpleAttribute<Car, String>("manufacturer") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.manufacturer; }
    };

    public static final SimpleAttribute<Car, String> MODEL = new SimpleAttribute<Car, String>("model") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.model; }
    };

    public static final SimpleAttribute<Car, Color> COLOR = new SimpleAttribute<Car, Color>("color") {
        public Color getValue(Car car, QueryOptions queryOptions) { return car.color; }
    };

    public static final SimpleAttribute<Car, Integer> DOORS = new SimpleAttribute<Car, Integer>("doors") {
        public Integer getValue(Car car, QueryOptions queryOptions) { return car.doors; }
    };

    public static final SimpleAttribute<Car, Double> PRICE = new SimpleAttribute<Car, Double>("price") {
        public Double getValue(Car car, QueryOptions queryOptions) { return car.price; }
    };

    public static final MultiValueAttribute<Car, String> FEATURES = new MultiValueAttribute<Car, String>("features") {
        public Iterable<String> getValues(Car car, QueryOptions queryOptions) { return car.features; }
    };

    public static final MultiValueAttribute<Car, String> KEYWORDS = new MultiValueAttribute<Car, String>("keywords") {
        public Iterable<String> getValues(Car car, QueryOptions queryOptions) { return car.keywords; }
    };

    public enum Color {RED, GREEN, BLUE, BLACK, WHITE}
    final int carId;
    final String manufacturer;
    final String model;
    final Color color;
    final int doors;
    final double price;
    final List<String> features;
    final List<String> keywords;

    public Car(int carId, String manufacturer, String model, Color color, int doors, double price, List<String> features, List<String> keywords) {
        this.carId = carId;
        this.manufacturer = manufacturer;
        this.model = model;
        this.color = color;
        this.doors = doors;
        this.price = price;
        this.features = features;
        this.keywords = keywords;
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

    public List<String> getFeatures() {
        return features;
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
                ", features=" + features +
                ", keywords=" + keywords +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;

        Car car = (Car) o;

        if (carId != car.carId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return carId;
    }
}
