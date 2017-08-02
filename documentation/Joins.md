# Performing Joins with CQEngine #

Although it is often easier and more performant to de-normalize data, it is possible with CQEngine to perform JOINs and SQL EXISTS-type queries between `IndexedCollection`s at runtime with reasonable performance (at least, outperforming external databases).



## SQL EXISTS ##
Given a collection of Cars, and a collections of Garages, find cars which are convertible or which have a sunroof, which can be serviced by garages in a particular city.
Source code of this example [here](../code/src/test/java/com/googlecode/cqengine/examples/join/SqlExists.java).

```java
package com.googlecode.cqengine.examples.join;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.query.Query;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;

public class SqlExists {

    public static void main(String[] args) {
        // Create an indexed collection of cars...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.add(new Car(1, "Ford Focus", "great condition, low mileage", asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "Ford Taurus", "dirty and unreliable, flat tyre", asList("spare tyre", "radio")));
        cars.add(new Car(3, "Honda Civic", "has a flat tyre and high mileage", asList("radio")));
        cars.add(new Car(4, "BMW M3", "2013 model", asList("radio", "convertible")));

        // Create an indexed collection of garages...
        final IndexedCollection<Garage> garages = new ConcurrentIndexedCollection<Garage>();
        garages.add(new Garage(1, "Joe's garage", "London", asList("Ford Focus", "Honda Civic")));
        garages.add(new Garage(2, "Jane's garage", "Dublin", asList("BMW M3")));
        garages.add(new Garage(3, "John's garage", "Dublin", asList("Ford Focus", "Ford Taurus")));
        garages.add(new Garage(4, "Jill's garage", "Dublin", asList("Ford Focus")));

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
            System.out.println(car.name + " has a sunroof or is convertible, and can be serviced in Dublin");
        }
    }
}
```

The example above prints:
<pre>
Ford Focus has a sunroof or is convertible, and can be serviced in Dublin
BMW M3 has a sunroof or is convertible, and can be serviced in Dublin
</pre>

## SQL EXISTS-based JOIN ##
Given a collection of Cars, and a collections of Garages, find cars which are convertible or which have a sunroof, which can be serviced by garages in a particular city, along with the names of those garages.
Source code of this example [here](../code/src/test/java/com/googlecode/cqengine/examples/join/SqlExistsBasedJoin.java).

```java
package com.googlecode.cqengine.examples.join;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.examples.introduction.Car;
import com.googlecode.cqengine.query.Query;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Arrays.asList;

public class SqlExistsBasedJoin {

    public static void main(String[] args) {
        // Create an indexed collection of cars...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.add(new Car(1, "Ford Focus", "great condition, low mileage", asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "Ford Taurus", "dirty and unreliable, flat tyre", asList("spare tyre", "radio")));
        cars.add(new Car(3, "Honda Civic", "has a flat tyre and high mileage", asList("radio")));
        cars.add(new Car(4, "BMW M3", "2013 model", asList("radio", "convertible")));

        // Create an indexed collection of garages...
        final IndexedCollection<Garage> garages = new ConcurrentIndexedCollection<Garage>();
        garages.add(new Garage(1, "Joe's garage", "London", asList("Ford Focus", "Honda Civic")));
        garages.add(new Garage(2, "Jane's garage", "Dublin", asList("BMW M3")));
        garages.add(new Garage(3, "John's garage", "Dublin", asList("Ford Focus", "Ford Taurus")));
        garages.add(new Garage(4, "Jill's garage", "Dublin", asList("Ford Focus")));

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
    }
}
```

The example above prints:
<pre>
Ford Focus has a sunroof or is convertible, and can be serviced in Dublin at the following garages:-
---> Garage{garageId=4, name='Jill's garage', location='Dublin', brandsServiced=[Ford Focus]}
---> Garage{garageId=3, name='John's garage', location='Dublin', brandsServiced=[Ford Focus, Ford Taurus]}
<br>
BMW M3 has a sunroof or is convertible, and can be serviced in Dublin at the following garages:-
---> Garage{garageId=2, name='Jane's garage', location='Dublin', brandsServiced=[BMW M3]}
</pre>

## JOINs between an arbitrary number of IndexedCollections ##

The following shows an approach to perform a JOIN between three IndexedCollections.

However, the approach to do so involves nesting `existsIn()` queries, and this can be done to any depth, allowing JOINs between any number of collections.

The full source code for this example can be found [here](../code/src/test/java/com/googlecode/cqengine/examples/join/ThreeWayJoin.java).
```java
public static void main(String[] args) {
    IndexedCollection<User> users = new ConcurrentIndexedCollection<User>();
    users.add(new User(1, "Joe"));
    users.add(new User(2, "Jane"));
    users.add(new User(3, "Jesse"));

    IndexedCollection<Role> roles = new ConcurrentIndexedCollection<Role>();
    roles.add(new Role(1, "CEO"));
    roles.add(new Role(2, "Manager"));
    roles.add(new Role(3, "Employee"));

    IndexedCollection<UserRole> userRoles = new ConcurrentIndexedCollection<UserRole>();
    userRoles.add(new UserRole(1, 3)); // Joe is an Employee
    userRoles.add(new UserRole(2, 2)); // Jane is a Manager
    userRoles.add(new UserRole(3, 2)); // Jesse is a Manager

    // Retrieve Users who are managers...
    Query<User> usersWhoAreManagers =
            existsIn(userRoles, User.USER_ID, UserRole.USER_ID,
                    existsIn(roles, UserRole.ROLE_ID, Role.ROLE_ID, equal(Role.ROLE_NAME, "Manager")));

    for (User u : users.retrieve(usersWhoAreManagers)) {
        System.out.println(u.userName);
    }
    // ..prints: Jane, Jesse
}
```
