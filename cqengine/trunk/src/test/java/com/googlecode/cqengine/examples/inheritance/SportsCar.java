package com.googlecode.cqengine.examples.inheritance;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.examples.introduction.Car;

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
        public Integer getValue(Car car) { return car instanceof SportsCar ? ((SportsCar)car).horsepower : null; }
    };
}
