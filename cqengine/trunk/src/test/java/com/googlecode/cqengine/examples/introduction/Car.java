package com.googlecode.cqengine.examples.introduction;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

/**
 * @author Niall Gallagher
 */
public class Car {

    public final int carId;
    public final String manufacturer;
    public final String model;

    public Car(int carId, String manufacturer, String model) {
        this.carId = carId;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    @Override
    public String toString() {
        return "Car{carId=" + carId + ", manufacturer='" + manufacturer + "', model='" + model + "'}";
    }

    // -------------------------- Attributes --------------------------

    public static final Attribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("carId") {
        public Integer getValue(Car car) { return car.carId; }
    };

    public static final Attribute<Car, String> MANUFACTURER = new SimpleAttribute<Car, String>("manufacturer") {
        public String getValue(Car car) { return car.manufacturer; }
    };

    public static final Attribute<Car, String> MODEL = new SimpleAttribute<Car, String>("model") {
        public String getValue(Car car) { return car.model; }
    };
}
