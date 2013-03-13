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
