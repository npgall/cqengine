# Transaction isolation in CQEngine (MVCC) #

As of CQEngine 2.0, [Multi-Version Concurrency Control](http://en.wikipedia.org/wiki/Multiversion_concurrency_control) (MVCC) is supported, providing [READ\_COMMITTED transaction isolation](http://en.wikipedia.org/wiki/Isolation_%28database_systems%29) out of the box via the [TransactionalIndexedCollection](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/TransactionalIndexedCollection.html).

A transaction is composed of a set of objects to be added to the collection, and a set of objects to be removed from the collection. Either one of those sets can be empty, so this supports bulk atomic addition and atomic removal of objects from the collection. But if both sets are non-empty then it allows bulk atomic replacement of objects in the collection.

Reading threads are guaranteed to see a consistent version of the collection where transactions have been applied in their entirety. Reads are lock-free, however writes are serialized. When using this feature it is also necessary to call close() on ResultSets.

Example usage (source code [here](../code/src/test/java/com/googlecode/cqengine/examples/transactions/TransactionalIndexedCollectionDemo.java)):
```java
// Create example Car objects...
Car car1 = CarFactory.createCar(1); // "Ford Fusion"
Car car2 = CarFactory.createCar(2); // "Ford Taurus"
Car car3 = CarFactory.createCar(3); // "Honda Civic"
Car car4 = CarFactory.createCar(4); // "Honda Accord"

// We will store the cars in TransactionalIndexedCollection, which provides MVCC support...
IndexedCollection<Car> cars = new TransactionalIndexedCollection<Car>(Car.class);

// ===== Examples of modifying the collection using MVCC transactions... =====

// Add 4 cars in a single transaction...
cars.addAll(asList(car1, car2, car3, car4));

// Remove 2 cars in a single transaction...
cars.removeAll(asList(car3, car4));

// Replace 1 car with 2 other cars in a single transaction...
cars.update(asList(car2), asList(car3, car4));

// ===== Examples of querying the collection using MVCC transactions... =====

// Retrieve with READ_COMMITTED transaction isolation...
ResultSet<Car> results = cars.retrieve(equal(Car.MANUFACTURER, "Ford"));
try {
    for (Car car : results) {
        System.out.println(car); // prints car 1 ("Ford Fusion")
    }
}
finally {
    results.close(); // ..close the ResultSet when finished reading!
}
```

## Atomically replacing the same object ##

Note that if you would like to replace an existing object with a new version of what is effectively the _same object_, then you will need to add a hidden version field to your object as discussed in the [JavaDoc](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/TransactionalIndexedCollection.html).

# Transaction Isolation without MVCC #

_**This section mostly applies to versions of CQEngine prior to 2.0 which did not include `TransactionalIndexedCollection` and MVCC support. However the information is still correct and may be useful to understand CQEngine's internal concurrency model - especially when using one of the other, non-transactional, [implementations of IndexedCollection](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/package-summary.html).**_

CQEngine provides lock-free reads by default.

Depending on the types of queries which may be ongoing while the collection is being modified, by default CQEngine provides a level of transaction isolation which is somewhere between READ\_COMMITTED, and READ\_UNCOMMITTED. But CQEngine supports higher isolation levels than this default, for applications which need it. Transaction isolation can be controlled on a query-by-query basis.

This section discusses:
  * Which isolation levels can be expected by default for various types of query, and
  * How those isolation levels may be upgraded using locks on a per-query basis, for applications which need it.

## Consistency Guarantees ##

CQEngine's consistency model is that objects are always added to and removed from `IndexedCollection` and indexes, with _volatile semantics_.

This guarantees that:
  * When `IndexedCollection.add`/`remove`/`addAll` etc. methods return, data has been committed to the collection and all indexes.
  * Any queries performed after those methods return, are guaranteed to see a fully consistent view (READ\_COMMITTED) of objects across all data structures.

Therefore, the addition or removal of a single object while queries are in-progress, does not require any locking. Ongoing queries will see READ\_COMMITTED-like transaction isolation with respect to a single object insertion or removal.

However queries which are ongoing while batch or several modifications are made to the collection, are not covered by this consistency guarantee. Those queries may see a subset of modifications made during their execution (READ\_UNCOMMITTED).

## READ\_COMMITTED Isolation for Batch Modifications ##

Applications can use a [ReadWriteLock](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReadWriteLock.html) (for write _always_ scenarios) or a [ReadWriteUpdateLock](http://github.com/npgall/concurrent-locks) (for read and _potentially_ write scenarios) to obtain transactional or READ\_COMMITTED isolation with CQEngine.

Applications should cache the lock in the same place as the `IndexedCollection` it will protect, and use it as follows to ensure READ\_COMMITTED isolation, only when needed:

  * Methods which will make batch or multiple _modifications_ which should be viewed as an atomic transaction by reading threads, should:
    * Acquire the write lock before modifying the collection, and release it when the last modification completes.
  * Methods which perform _queries_ requiring READ\_COMMITTED isolation, should:
    * Acquire a read lock before doing so, and hold it until they have completely finished iterating through the `ResultSet`.
  * Methods which perform _queries_ which would be compatible with READ\_UNCOMMITTED isolation, should:
    * Perform reads _without acquiring the read lock_.
  * Applications which only do single object insertions/removals at a time, or multiple object insertions/removals which do not need to be transactional:
    * Do not need to use any locking.
