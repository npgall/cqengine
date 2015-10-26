# The Limits of Iteration #
The classic way to retrieve objects matching some criteria from a collection, is to iterate through the collection and apply some tests to each object. If the object matches the criteria, then it is added to a result set. This is repeated for every object in the collection.

**Example: Perform a Query via _Naive Iteration_**

This is hugely inefficient, it performs up to 20,000 tests, for a collection of 10,000 cars:
```
    public static Collection<Car> getBlueCarsWithFourDoors(Collection<Car> allCars) {
        List<Car> results = new LinkedList<Car>();
        for (Car car : allCars) {
            if (car.getColor().equals(Car.Color.BLUE) && car.getDoors() == 4) {
                results.add(car);
            }
        }
        return results;
    }
```
  * It should be clear that performing queries using iteration, is like _performing SQL queries on a database table which does not have any indexes_.

**Time Complexity**

Let number of objects in the collection = _n_. Let number of tests to be applied to each object = _t_. If there were 10,000 objects in the collection and 5 tests to apply to each object, performing the query would require _n_ x _t_ tests, or 50,000 tests in total. Every _additional_ object added to the collection would require _five_ additional tests to be performed. Performance of queries would _degrade_ as additional objects were added, which is not scalable. The [time complexity](http://en.wikipedia.org/wiki/Time_complexity) of this (worst case) is O(_n_ _t_).

**_Optimized Iteration_**

In fact, the iteration above could be improved:

  1. If it was known that **_fewer cars have four doors than are blue_**, the test for number of doors should be performed **_first_**. That way, Java would short-circuit the `&&` operator and not actually test the color for the majority of cars which did not already have four doors.
  1. In most situations, the application will just iterate through the results returned. In that respect, time complexity would be O(_r_ + _nt_), and the method would have _allocated a `LinkedList` needlessly_. A further problem with the approach, is that it **causes latency**, because the _entire result set must be assembled before the first object can be processed_. The method could be further enhanced to accept a `Handler` object, which it would invoke supplying each Car object which matches the criteria. The handler could do whatever the application otherwise would have done inside its iteration loop; yet a `LinkedList` would not be allocated, the first object would be processed as soon as it was encountered, and all objects would be processed in a single pass.
  1. An even nicer way to avoid allocating a `LinkedList`, and which would allow the first object to be processed sooner, and yet which would still allow applications to iterate results in the normal way (without supplying a `Handler`), would be for the method to return an `Iterable` object which acted as a _filtered view_ over `allCars`. As the application iterated through the view, the `Iterable` would scan `allCars` for the _next_ matching object. This is known as _lazy evaluation_.

The problem with optimization #1, is that implementing it requires **statistical knowledge** of the makeup of the collection, based on the values of the fields within each `Car` object.

**Comparison with CQEngine**

  * CQEngine **maintains statistical information** on the makeup of collections, and in fact will always re-order queries to maximize execution speed.
  * CQEngine uses **lazy evaluation** heavily: the result sets returned by CQEngine are rarely _materialized_, and instead act as a _view_ over the set of objects in the collection which ultimately match the query.
  * Of course, the main problem with the approach above, is that it **uses iteration in the first place**, always having minimum time complexity O(_n_). It would be useful if it could somehow **_jump_** to the sets of cars which have four doors, or which are blue, or both, without having to scan the collection at all.