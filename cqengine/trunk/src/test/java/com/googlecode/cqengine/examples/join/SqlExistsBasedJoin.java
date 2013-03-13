package com.googlecode.cqengine.examples.join;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.query.Query;

import java.util.Arrays;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * Demonstrates a join between two indexed collections. Given a collection of Cars, and a collections of Garages,
 * find cars which are convertible or which have a sunroof, which can be serviced by garages in Dublin, along with
 * the names of those garages.
 *
 * @author Niall Gallagher
 */
public class SqlExistsBasedJoin {

    public static void main(String[] args) {
        // Create an indexed collection of cars...
        IndexedCollection<Car> cars = CQEngine.newInstance();
        cars.add(new Car(1, "Ford Focus", "great condition, low mileage", Arrays.asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "Ford Taurus", "dirty and unreliable, flat tyre", Arrays.asList("spare tyre", "radio")));
        cars.add(new Car(3, "Honda Civic", "has a flat tyre and high mileage", Arrays.asList("radio")));
        cars.add(new Car(4, "BMW M3", "2013 model", Arrays.asList("radio", "convertible")));

        // Create an indexed collection of garages...
        final IndexedCollection<Garage> garages = CQEngine.newInstance();
        garages.add(new Garage(1, "Joe's garage", "London", Arrays.asList("Ford Focus", "Honda Civic")));
        garages.add(new Garage(2, "Jane's garage", "Dublin", Arrays.asList("BMW M3")));
        garages.add(new Garage(3, "John's garage", "Dublin", Arrays.asList("Ford Focus", "Ford Taurus")));
        garages.add(new Garage(4, "Jill's garage", "Dublin", Arrays.asList("Ford Focus")));

        // Query: Cars which are convertible or which have a sunroof, which can be serviced in Dublin...
        Query<Car> carsQuery = and(
                in(Car.FEATURES, "sunroof", "convertible"),
                existsIn(garages,
                        Car.NAME,
                        Garage.BRANDS_SERVICED,
                        equal(Garage.LOCATION, "Dublin")
                )
        );

        for (Car car : cars.retrieve(carsQuery)) {
            Query<Garage> garagesWhichServiceThisCarInDublin
                    = and(equal(Garage.BRANDS_SERVICED, car.name), equal(Garage.LOCATION, "Dublin"));
            boolean first = true;
            for (Garage garage : garages.retrieve(garagesWhichServiceThisCarInDublin)) {
                if (first) {
                    // Print this only when we have actually retrieved the first garage, in case the
                    // collection was modified removing the first garage before the inner loop :)...
                    System.out.println(car.name + " has a sunroof or is convertible, " +
                            "and can be serviced in Dublin at the following garages:- " );
                    first = false;
                }
                System.out.println("---> " + garage);
            }
            System.out.println();
        }
        /* ..prints:

            BMW M3 has a sunroof or is convertible, and can be serviced in Dublin at the following garages:-
            ---> Garage{garageId=2, name='Jane's garage', location='Dublin', brandsServiced=[BMW M3]}

            Ford Focus has a sunroof or is convertible, and can be serviced in Dublin at the following garages:-
            ---> Garage{garageId=3, name='John's garage', location='Dublin', brandsServiced=[Ford Focus, Ford Taurus]}
            ---> Garage{garageId=4, name='Jill's garage', location='Dublin', brandsServiced=[Ford Focus]}
         */
    }
}