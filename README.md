# CQEngine - Collection Query Engine #

CQEngine – Collection Query Engine – is a high-performance Java collection which can be searched with SQL-like queries, with _extremely_ low latency.

  * Search collections or arbitrary data sources with SQL-like queries
  * Achieve millions of queries per second, with query latencies measured in microseconds
  * Offload query traffic from databases - scale your application tier
  * Outperform databases by a factor of thousands, even on low-end hardware

Supports on-heap persistence, off-heap persistence, disk persistence, and supports MVCC transaction isolation.

Interesting reviews of CQEngine:
  * [syntx.io: Comparing search performance of CQEngine with standard Java collections](http://syntx.io/comparing-search-performance-of-cqengine-with-standard-java-collections/)
  * [syntx.io: Getting started with CQEngine: LINQ for Java, only faster](http://syntx.io/getting-started-with-cqengine-linq-for-java-only-faster/)
  * [excelian.com: Exposure and counterparty limit checking: NoSQL indexing and querying solution](http://www.excelian.com/exposure-and-counterparty-limit-checking)
  * [snapdeal.com/reducedata.com: How we scaled: 300K QPS & 3-5B requests a day](https://medium.com/@azifali/how-we-scaled-300k-qps-3-5b-requests-a-day-f69a641f1ae2)

## The Limits of Iteration ##
The classic way to retrieve objects matching some criteria from a collection, is to iterate through the collection and apply some tests to each object. If the object matches the criteria, then it is added to a result set. This is repeated for every object in the collection.

Conventional iteration is hugely inefficient, with time complexity O(_n_ _t_). It can be optimized, but requires **statistical knowledge** of the makeup of the collection. [Read more: The Limits of Iteration](documentation/TheLimitsOfIteration.md)

**Benchmark Sneak Peek**

Even with optimizations applied to convention iteration, CQEngine can outperform conventional iteration by wide margins. Here is a graph for a test comparing CQEngine latency with iteration for a range-type query:

![quantized-navigable-index-carid-between.png](documentation/images/quantized-navigable-index-carid-between.png)

  * **1,116,071 queries per second** (on a single 1.8GHz CPU core)
  * **0.896 microseconds per query**
  * CQEngine is **330187.50% faster** than naive iteration
  * CQEngine is **325727.79% faster** than optimized iteration

See the [Benchmark](documentation/Benchmark.md) wiki page for details of this test, and other tests with various types of query.


---


## CQEngine Overview ##

CQEngine solves the scalability and latency problems of iteration by making it possible to build _indexes_ on the fields of the objects stored in a collection, and applying algorithms based on the rules of set theory to _reduce the time complexity_ of accessing them.

**Indexing and Query Plan Optimization**

  * **Simple Indexes** can be added to any number of individual fields in a collection of objects, allowing queries on just those fields to be answered in O(_1_) time complexity
  * **Multiple indexes on the same field** can be added, each optimized for different types of query - for example equality, numerical range, string starts with etc.
  * **Compound Indexes** can be added which span multiple fields, allowing queries referencing several fields to also be answered in O(_1_) time complexity
  * **Nested Queries** are fully supported, such as the SQL equivalent of "`WHERE color = 'blue' AND(NOT(doors = 2 OR price > 53.00))`"
  * **Standing Query Indexes** can be added; these allow _arbitrarily complex queries_, or _nested query fragments_, to be answered in O(_1_) time complexity, regardless of the number of fields referenced. Large queries containing branches or query fragments for which standing query indexes exist, will automatically benefit from O(_1_) time complexity evaluation of their branches; in total several indexes might be used to accelerate complex queries
  * **Statistical Query Plan Optimization** - when several fields have suitable indexes, CQEngine will use statistical information from the indexes, to internally make a query plan which selects the indexes which can perform the query with minimum time complexity. When some referenced fields have suitable indexes and some do not, CQEngine will use the available indexes first, and will then iterate the smallest possible set of results from those indexes to filter objects for the rest of the query. In those cases time complexity will be greater than O(_1_), but usually significantly less than O(_n_)
  * **Iteration fallback** -  if no suitable indexes are available, CQEngine will evaluate the query via iteration, using lazy evaluation. CQEngine can always evaluate every query, even if no suitable indexes are available. Queries are not coupled with indexes, so indexes can be added after the fact, to speed up existing queries
  * **CQEngine supports full concurrency** and expects that objects will be added to and removed from the collection at runtime; CQEngine will take care of updating all registered indexes in realtime
  * **Type-safe** - nearly all errors in queries result in _compile-time_ errors instead of exceptions at runtime: all indexes, and all queries, are strongly typed using generics at both object-level and field-level
  * **On-heap/off-heap/disk** - objects can be stored on-heap (like a conventional Java collection), or off-heap (in native memory, within the JVM process but outside the Java heap), or persisted to disk

Several implementations of CQEngine's `IndexedCollection` are provided, supporting various concurrency and transaction isolation levels:

  * [ConcurrentIndexedCollection](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/ConcurrentIndexedCollection.html) - lock-free concurrent reads and writes with no transaction isolation
  * [ObjectLockingIndexedCollection](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/ObjectLockingIndexedCollection.html) - lock-free concurrent reads, and some locking of writes for object-level transaction isolation and consistency guarantees
  * [TransactionalIndexedCollection](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/TransactionalIndexedCollection.html)  - lock-free concurrent reads, and sequential writes for full [transaction isolation](documentation/TransactionIsolation.md) using Multi-Version Concurrency Control

For more details see [TransactionIsolation](documentation/TransactionIsolation.md).


---


## Feature Matrix for Included Indexes ##

**Legend for the feature matrix**

| **Abbreviation** | **Meaning** | **Example** |
|:-----------------|:------------|:------------|
| **EQ**           | _Equality_  | `equal(Car.DOORS, 4)` |
| **IN**           | _Equality, multiple values_ | `in(Car.DOORS, 3, 4, 5)` |
| **LT**           | _Less Than (numerical range / `Comparable`)_ | `lessThan(Car.PRICE, 5000.0)` |
| **GT**           | _Greater Than (numerical range / `Comparable`)_ | `greaterThan(Car.PRICE, 2000.0)` |
| **BT**           | _Between (numerical range / `Comparable`)_ | `between(Car.PRICE, 2000.0, 5000.0)` |
| **SW**           | _String Starts With_ | `startsWith(Car.NAME, "For")` |
| **EW**           | _String Ends With_ | `endsWith(Car.NAME, "ord")` |
| **SC**           | _String Contains_ | `contains(Car.NAME, "or")` |
| **CI**           | _String Is Contained In_ | `isContainedIn(Car.NAME, "I am shopping for a Ford Focus car")` |
| **RX**           | _String Matches Regular Expression_ | `matchesRegex(Car.MODEL, "Ford.*")` |
| **HS**           | _Has (aka `IS NOT NULL`)_ | `has(Car.DESCRIPTION)` / `not(has(Car.DESCRIPTION))` |
| **AQ**           | _Any Query_ | _Build an index to provide constant time complexity for any simple query, complex query, or fragment_ |
| **QZ**           | _Quantization_ | _Does the index accept a quantizer to control granularity_ |

Note: CQEngine also supports complex queries via **`and`**, **`or`**, **`not`**, and combinations thereof, across all indexes.

**Index Feature Matrix**

| <sub>**Index Type**</sub> | <sub>**EQ**</sub> | <sub>**IN**</sub> | <sub>**LT**</sub> | <sub>**GT**</sub> | <sub>**BT**</sub> | <sub>**SW**</sub> | <sub>**EW**</sub> | <sub>**SC**</sub> | <sub>**CI**</sub> | <sub>**HS**</sub> | <sub>**RX**</sub> | <sub>**AQ**</sub> | <sub>**QZ**</sub> |
|:---------------|:-------|:-------|:-------|:-------|:-------|:-------|:-------|:-------|:-------|:-------|:-------|:-------|:-------|
| [<sub>Hash</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/hash/HashIndex.html) | ✓      | ✓      |        |        |        |        |        |        |        |        |        |        | ✓      |
| [<sub>Unique</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/unique/UniqueIndex.html) | ✓      | ✓      |        |        |        |        |        |        |        |        |        |        |        |
| [<sub>Compound</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/compound/CompoundIndex.html) | ✓      | ✓      |        |        |        |        |        |        |        |        |        |        | ✓      |
| [<sub>Navigable</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/navigable/NavigableIndex.html) | ✓      | ✓      | ✓      | ✓      | ✓      |        |        |        |        |        |        |        | ✓      |
| [<sub>RadixTree</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/radix/RadixTreeIndex.html) | ✓      | ✓      |        |        |        | ✓      |        |        |        |        |        |        |        |
| [<sub>ReversedRadixTree</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/radixreversed/ReversedRadixTreeIndex.html) | ✓      | ✓      |        |        |        |        | ✓      |        |        |        |        |        |        |
| [<sub>InvertedRadixTree</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/radixinverted/InvertedRadixTreeIndex.html) | ✓      | ✓      |        |        |        |        |        |        | ✓      |        |        |        |        |
| [<sub>SuffixTree</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/suffix/SuffixTreeIndex.html) | ✓      | ✓      |        |        |        |        | ✓      | ✓      |        |        |        |        |        |
| [<sub>StandingQuery</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/standingquery/StandingQueryIndex.html) | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      |        |
| [<sub>Fallback</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/fallback/FallbackIndex.html) | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      |        |        |
| [<sub>OffHeap</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/offheap/OffHeapIndex.html) | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      |        |        |        |        |        |        |        |
| [<sub>Disk</sub>](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/disk/DiskIndex.html) | ✓      | ✓      | ✓      | ✓      | ✓      | ✓      |        |        |        |        |        |        |        |


The [Benchmark](documentation/Benchmark.md) page contains examples of how to add these indexes to a collection, and measures their impact on latency.


---


## Complete Example ##

In CQEngine applications mostly interact with [IndexedCollection](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/IndexedCollection.html), which is an implementation of [java.util.Set](http://docs.oracle.com/javase/6/docs/api/java/util/Set.html), and it provides two additional methods:

  * [addIndex(SomeIndex)](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/engine/QueryEngine.html#addIndex(com.googlecode.cqengine.index.Index)) allows indexes to be added to the collection
  * [retrieve(Query)](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/engine/QueryEngine.html#retrieve(com.googlecode.cqengine.query.Query)) accepts a [Query](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/Query.html) and returns a [ResultSet](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html) providing objects matching that query. `ResultSet` implements [java.lang.Iterable](http://docs.oracle.com/javase/6/docs/api/java/lang/Iterable.html), so accessing results is achieved by iterating the result set

Here is a **complete example** of how to build a collection, add indexes and perform queries. It does not discuss _attributes_, which are discussed below.

**STEP 1: Create a new indexed collection**

```java
IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
```

**STEP 2: Add some indexes to the collection**

```java
cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
cars.addIndex(ReversedRadixTreeIndex.onAttribute(Car.NAME));
cars.addIndex(SuffixTreeIndex.onAttribute(Car.DESCRIPTION));
cars.addIndex(HashIndex.onAttribute(Car.FEATURES));
```

**STEP 3: Add some objects to the collection**

```java
cars.add(new Car(1, "ford focus", "great condition, low mileage", Arrays.asList("spare tyre", "sunroof")));
cars.add(new Car(2, "ford taurus", "dirty and unreliable, flat tyre", Arrays.asList("spare tyre", "radio")));
cars.add(new Car(3, "honda civic", "has a flat tyre and high mileage", Arrays.asList("radio")));
```


**STEP 4: Run some queries**

Note: add import statement to your class: _`import static com.googlecode.cqengine.query.QueryFactory.*`_

* *Example 1: Find cars whose name ends with 'vic' or whose id is less than 2*

  Query:
  ```java
    Query<Car> query1 = or(endsWith(Car.NAME, "vic"), lessThan(Car.CAR_ID, 2));
    for (Car car : cars.retrieve(query1)) {
        System.out.println(car);
    }
  ```
  Prints:
  ```
    Car{carId=3, name='honda civic', description='has a flat tyre and high mileage', features=[radio]}
    Car{carId=1, name='ford focus', description='great condition, low mileage', features=[spare tyre, sunroof]}
  ```
  
* *Example 2: Find cars whose flat tyre can be replaced*

  Query:
  ```java
    Query<Car> query2 = and(contains(Car.DESCRIPTION, "flat tyre"), equal(Car.FEATURES, "spare tyre"));
    for (Car car : cars.retrieve(query2)) {
        System.out.println(car);
    }
  ```
  Prints:
  ```
    Car{carId=2, name='ford taurus', description='dirty and unreliable, flat tyre', features=[spare tyre, radio]}
  ```
  
* *Example 3: Find cars which have a sunroof or a radio but are not dirty*

  Query:
  ```java
    Query<Car> query3 = and(in(Car.FEATURES, "sunroof", "radio"), not(contains(Car.DESCRIPTION, "dirty")));
    for (Car car : cars.retrieve(query3)) {
        System.out.println(car);
    }
  ```
   Prints:
  ```
    Car{carId=1, name='ford focus', description='great condition, low mileage', features=[spare tyre, sunroof]}
    Car{carId=3, name='honda civic', description='has a flat tyre and high mileage', features=[radio]}
  ```

Complete source code for these examples can be found [here](http://github.com/npgall/cqengine/blob/master/code/src/test/java/com/googlecode/cqengine/examples/introduction/).


---


## Attributes ##

### Read Fields ###

CQEngine needs to access fields inside objects, so that it can build indexes on fields, and retrieve the value of a certain field from any given object.

CQEngine does not use reflection to do this; instead it uses **attributes**, which is a more powerful concept. An attribute is an object which can read the value of a certain field given an object.

Here's how to define an attribute for a Car object, which reads the `Car.carId` field:
```java
public static final Attribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("carId") {
    public Integer getValue(Car car, QueryOptions queryOptions) { return car.carId; }
};
```
Usually attributes are defined as anonymous `static` `final` objects like this. Supplying the `"carId"` string parameter to the constructor is actually optional, it is not used by the query engine per-se, except it will lead to more informative exception messages.

Since this attribute reads a field from a `Car` object, the usual place to put the attribute is inside the `Car` class - and this makes queries more readable. However it could really be defined in any class, such as in a `CarAttributes` class or similar. The example above is for a **[SimpleAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/SimpleAttribute.html)**, which is designed for fields containing only one value.

CQEngine also supports **[MultiValueAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/MultiValueAttribute.html)** which can read the values of fields which themselves are collections. And so it supports building indexes on objects based on things like keywords associated with those objects.

Here's how to define a `MultiValueAttribute` for a `Car` object which reads the values from `Car.features` where that field is a `List<String>`:
```java
public static final Attribute<Car, String> FEATURES = new MultiValueAttribute<Car, String>("features") {
    public Iterable<String> getValues(Car car, QueryOptions queryOptions) { return car.features; }
};
```

For nullable fields, CQEngine also includes **[SimpleNullableAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/SimpleNullableAttribute.html)** and **[MultiValueNullableAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/MultiValueNullableAttribute.html)**. These also allow CQEngine to work with object inheritance, where some objects in the collection might have some fields (e.g. subclasses) while others do not.

Dynamic queries can be composed at runtime by instantiating and combining Query objects directly; see [this package](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/simple/package-summary.html) and [this package](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/logical/package-summary.html). For advanced cases, it is also possible to define attributes at runtime, using [ReflectiveAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/ReflectiveAttribute.html) or [AttributeBytecodeGenerator](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/codegen/AttributeBytecodeGenerator.html).

#### Generate attributes automatically ####

CQEngine also provides several ways to generate attributes automatically.

Note these are an alternative to using [ReflectiveAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/ReflectiveAttribute.html), which was discussed above. Whereas `ReflectiveAttribute` is a special type of attribute which reads values at runtime using reflection, `AttributeSourceGenerator` and `AttributeBytecodeGenerator` generate code for attributes which is compiled and so does not use reflection at runtime, which can be more efficient.

  * [AttributeSourceGenerator](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/codegen/AttributeSourceGenerator.html) can automatically generate the source code for the simple and multi-value attributes discussed above.
  * [AttributeBytecodeGenerator](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/codegen/AttributeBytecodeGenerator.html) can automatically generate the class bytecode for the simple and multi-value attributes discussed above, and load them into the application at runtime as if they had been compiled from source code.

See [AutoGenerateAttributes](documentation/AutoGenerateAttributes.md) for more details.

### Attributes as Functions ###

It can be noted that attributes are only required to return a value given an object. Although most will do so, there is no requirement that an attribute must provide a value by reading a field in the object. As such attributes can be _virtual_, implemented as _functions_.

**Calculated Attributes**

An attribute can **_calculate_** an appropriate value for an object, based on a function applied to data contained in other fields or from external data sources.

Here's how to define a calculated (or virtual) attribute by applying a function over the Car's other fields:
```java
public static final Attribute<Car, Boolean> IS_DIRTY = new SimpleAttribute<Car, Boolean>("is_dirty") {
    public Boolean getValue(Car car, QueryOptions queryOptions) { return car.description.contains("dirty"); }
};
```

A `HashIndex` could be built on the virtual attribute above, enabling fast retrievals of cars which are either dirty or not dirty, without needing to scan the collection.

**Associations with other `IndexedCollections` or External Data Sources**

Here is an example for a virtual attribute which **associates** with each `Car` a list of locations which can service it, from an external data source:
```java
public static final Attribute<Car, String> SERVICE_LOCATIONS = new MultiValueAttribute<Car, String>() {
    public List<String> getValues(Car car, QueryOptions queryOptions) {
        return CarServiceManager.getServiceLocationsForCar(car);
    }
};
```
The attribute above would allow the `IndexedCollection` of cars to be searched for cars which have _servicing options in a particular location_.

The locations which service a car, could alternatively be retrieved from another `IndexedCollection`, of `Garage`s, for example. **Care should be taken if building indexes on virtual attributes** however, if referenced data might change leaving obsolete information in indexes. A **strategy to accommodate this** is: if no index exists for a virtual attribute referenced in a query, and other attributes are also referenced in the query for which indexes exist, CQEngine will automatically reduce the candidate set of objects to the minimum using other indexes before querying the virtual attribute. In turn if virtual attributes perform retrievals from _other_ `IndexedCollection`s, then those collections could be indexed appropriately without a risk of stale data.

### Joins ###

The examples above define attributes on a primary `IndexedCollection` which read data from secondary collections or external data sources.

It is also possible to perform SQL EXISTS-type queries and JOINs between `IndexedCollection`s on the query side (as opposed to on the attribute side). See [Joins](documentation/Joins.md) for examples.


---


## Persistence on-heap, off-heap, disk ##

CQEngine's `IndexedCollection`s can be configured to store objects added to them on-heap (the default), or off-heap, or on disk.

**On-heap**

Store the collection on the Java heap:
```java
IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
```

**Off-heap**

Store the collection in native memory, within the JVM process but outside the Java heap:
```java
IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(OffHeapPersistence.onPrimaryKey(Car.CAR_ID));
```

**Disk**

Store the collection in a temp file on disk (then see `DiskPersistence.getFile()`):
```java
IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(DiskPersistence.onPrimaryKey(Car.CAR_ID));
```
Or, store the collection in a particular file on disk:
```java
IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(DiskPersistence.onPrimaryKeyInFile(Car.CAR_ID, new File("cars.dat")));
```

### Index persistence ###

Indexes can similarly be stored on-heap, off-heap, or on disk. However the persistence used for indexes, depends on the type of index.

It is possible to store the collection on-heap, but to store some indexes off-heap. Similarly it is possible to have a variety of index types on the same collection, each using a different type of persistence. On-heap persistence is by far the fastest, followed by off-heap persistence, and then by disk persistence.

If both the collection and all of its indexes are stored off-heap or on disk, then it is possible to have extremely large collections which don't use any heap memory or RAM at all.

CQEngine has been tested using off-heap persistence with collections of 10 million objects, and using disk persistence with collections of 100 million objects.

**On-heap**

Add an on-heap index on "manufacturer":
```java
cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));
```

**Off-heap**

Add an off-heap index on "manufacturer":
```java
cars.addIndex(OffHeapIndex.onAttribute(Car.MANUFACTURER));
```

**Disk**

Add a disk index on "manufacturer":
```java
cars.addIndex(DiskIndex.onAttribute(Car.MANUFACTURER));
```

### Querying with persistence ###

When either the `IndexedCollection`, or one or more indexes are located off-heap or on disk, take care to close the ResultSet when finished reading.
```java
ResultSet<Car> results = cars.retrieve(equal(Car.MANUFACTURER, "Ford"));
try {
    for (Car car : results) {
        System.out.println(car);
    }
}
finally {
    results.close(); // ..close the ResultSet when finished reading!
}
```


---


## Result Sets ##

CQEngine [ResultSet](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html)s provide the following methods:

  * [iterator()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html#iterator()) - Allows the `ResultSet` to be iterated, returning the next object matching the query in each iteration as determined via _lazy evaluation_
    * Result sets support **concurrent iteration** while the collection is being modified; the set of objects returned simply may or may not reflect changes made during iteration (depending on whether changes are made to areas of the collection or indexes already iterated or not)

  * [uniqueResult()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html#uniqueResult()) - Useful if the query is expected to only match one object, this method returns the first object which would be returned by the iterator, and it throws an exception if zero or more than one object is found

  * [size()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html#size()) - Returns the number of objects which _would be returned by the `ResultSet` if it was iterated_; CQEngine can often **accelerate** this calculation of size, based on the sizes of individual sets in indexes; see JavaDoc for details

  * [contains()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html#contains(O)) -  Tests if a _given object_ would be contained in results matching a query; this is also an **accelerated** operation; when suitable indexes are available, CQEngine can avoid iterating results to test for containment; see JavaDoc for details

  * [getRetrievalCost()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html#getRetrievalCost()) - This is a metric used internally by CQEngine to allow it to _choose between multiple indexes_ which support the query. This could occasionally be used by applications to ascertain if suitable indexes are available for any particular query, this will be `Integer.MAX_VALUE` for queries for which no suitable indexes are available

  * [getMergeCost()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html#getMergeCost()) - This is a metric used internally by CQEngine to allow it to _re-order_ elements of the query to minimize time complexity; for example CQEngine will order intersections such that the smallest set drives the _merge_; this metric is _roughly_ based on the theoretical cost to iterate underlying result sets
    * For query fragments requiring _set union_ (`or`-based queries), this will be the _sum_ of merge costs from underlying result sets
    * For query fragments requiring _set intersection_ (`and`-based queries), this will be the _Math.min()_ of merge costs from underlying result sets, because intersections will be re-ordered to perform lowest-merge-cost intersections first
    * For query fragments requiring _set difference_ (`not`-based queries), this will be the merge cost from the first underlying result set

  * [close()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html#close()) -  Releases any resources or closes the transaction which was opened for the query. Whether or not it is necessary to close the ResultSet depends on which implementation of IndexedCollection is in use and the types of indexes added to it.


---


## Deduplicating Results ##

It is possible that a query would result in the same object being returned more than once.

For example if an object matches several attribute values specified in an `or`-type query, then the object will be returned multiple times, one time for each attribute matched. Intersections (`and`-type queries) and negations (`not`-type queries) do not produce duplicates.

By default, CQEngine does _not_ perform de-duplication of results; however it can be _instructed_ to do so, using various strategies.

### Logical Elimination Strategy ###

Eliminate duplicates using the rules of set theory, without _materializing_ (copying) the results into an intermediate set.

This is best explained as follows:

  * Let ∪ = conventional set union, duplicates are eliminated
  * Let ∪ₐ = union all, union without eliminating duplicates
  * Let – = set difference

Conventional set union, ∪, can be achieved by combining ∪ₐ (union all) with set difference –.

  * A ∪ B ∪ C = (A ∪ₐ (B – A)) ∪ₐ ((C – B) – A)

  * Read more: [Logical Elimination Strategy usage and tradeoffs](documentation/DeduplicationStrategies.md)

### Materialize Strategy ###

Despite the typical interpretation, this does not materialize all objects up-front.

  * Read more: [Materialize Strategy usage and tradeoffs](documentation/DeduplicationStrategies.md)


---


## Ordering Results ##

By default, CQEngine does not order results; it simply returns objects in the order it finds them in the collection or in indexes.

CQEngine can be instructed to order results via query options as follows.

**Order by price descending**
```java
ResultSet<Car> results = cars.retrieve(query, queryOptions(orderBy(descending(Car.PRICE))));
```

**Order by price descending, then number of doors ascending**
```java
ResultSet<Car> results = cars.retrieve(query, queryOptions(orderBy(descending(Car.PRICE), ascending(Car.DOORS))));
```

Note that ordering results as above uses the default "MATERIALIZE" ordering strategy. This is relatively expensive, dependent on the number of objects matching the query, and can cause latency in accessing the first object. It requires all results to be materialized into a sorted set up-front _before iteration can begin_. However ordering results in this way also implicitly eliminates duplicates.

### Index-driven ordering ###

CQEngine also supports an alternative ordering strategy, "INDEX", which uses an index to order results instead. The INDEX strategy reduces the latency to access the first object in the sorted results, at the expense of adding more total overhead if the entire ResultSet was iterated.

INDEX ordering can only be used when results are to be ordered by a single `SimpleAttribute`, and there is a `NavigableIndex`, `OffHeapIndex`, or `DiskIndex` on that attribute (or specifically, an index which implements the [SortedKeyStatisticsIndex](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/support/SortedKeyStatisticsIndex.html) interface).

The INDEX ordering strategy can be requested via a query option:
```java
ResultSet<Car> results = cars.retrieve(
        between(Car.CAR_ID, 40, 50),
        queryOptions(orderBy(descending(Car.CAR_ID)), orderingStrategy(INDEX))
);
```
A complete example can be found [here](http://github.com/npgall/cqengine/blob/master/code/src/test/java/com/googlecode/cqengine/examples/ordering/IndexOrderingDemo.java).

---


## Index Quantization, Granularity, and Continuous Values ##

[Quantization](http://en.wikipedia.org/wiki/Quantization_(signal_processing)) involves converting fine-grained or continuous values, to discrete or coarse-grained values. A Quantizer is a _function_ which takes fine-grained values as input, and maps those values to coarse-grained counterparts as its output, by discarding some precision.

Discrete values (e.g. `Integer`, `Long`, `BigInteger`) are values which have only a finite number of possible values, or which have a fixed spacing between possible values. Continuous values (e.g. `Float`, `Double`, `BigDecimal`) are values which do not have fixed spacing and which therefore can have an arbitrarily high precision.

CQEngine includes several Quantizers for numerical data types which support indexing continuous values efficiently, and allow the granularity of indexes to be controlled (trading a reduction in memory usage, for increases in CPU overhead).

Read more: [Quantization and included Quantizers](documentation/IndexQuantization.md)


---


## Grouping and Aggregation (GROUP BY, SUM...) ##

CQEngine has been designed with support for grouping and aggregation in mind, but note that this is not built into the CQEngine library itself, because CQEngine is designed to integrate with Java 8 lambda expressions. So the best approach for grouping or aggregating results, depends on the version of Java in use.

### Java version 8 or later - lambda expressions ###

When CQEngine is run on Java 8, additional methods will appear on CQEngine `ResultSet`s (inherited from the `Iterable` interface) which will allow CQEngine results to be grouped, aggregated, and transformed in flexible ways using lambda expressions.

Thus on Java 8 CQEngine can provide efficient query evaluation, and then lambda expressions can be used to group or aggregate results.

Here's how to transform a CQEngine `ResultSet` into a Java 8 `Stream` which can be grouped and aggregated using lambda expressions:
```java
public static <O> Stream<O> asStream(ResultSet<O> rs) {
    return StreamSupport.stream(rs.spliterator(), false);
}
```

**Performance Note**

  * Note that both Java 8 lambda expressions, and LambdaJ expressions below, are primitive transformations which are **evaluated via filtering** and they do not avail of indexes

  * So **for best performance as much of the overall query as possible should be encapsulated in the CQEngine query**, as opposed to in the post-processing lambda or LambdaJ expression


### Java versions prior to 8 - use LambdaJ ###

CQEngine also supports grouping and aggregation on Java versions prior to Java 8.

[LambdaJ](http://code.google.com/p/lambdaj/) is a very powerful and highly recommended library for manipulating Java collections, including support for grouping and aggregation, on versions of Java prior to Java 8. Applications requiring fast retrieval and aggregation can thus combine CQEngine with LambdaJ - LambdaJ accepts `Iterable`s as input, and CQEngine's `ResultSet`s implement that interface.


---


## CQEngine Performance Evaluation / Benchmark ##

See: [Benchmark](documentation/Benchmark.md) evaluating the performance of CQEngine versus _naive iteration_ and _optimized iteration_.


---


## Using CQEngine with Hibernate / JPA / ORM Frameworks ##

CQEngine has seamless integration with JPA/ORM frameworks such as Hibernate or EclipseLink.

Simply put, CQEngine can build indexes on, and query, any type of Java collection or arbitrary data source. ORM frameworks return entity objects loaded from database tables in Java collections, therefore CQEngine can act as a very fast in-memory query engine on top of such data.


---


## Usage in Maven and Non-Maven Projects ##

CQEngine is in Maven Central, and can be added to a Maven project as follows:
```
<dependency>
    <groupId>com.googlecode.cqengine</groupId>
    <artifactId>cqengine</artifactId>
    <version>x.x.x</version>
</dependency>
```
See [ReleaseNotes](documentation/ReleaseNotes.md) for the latest version number.

For non-Maven projects, a version built with [maven-shade-plugin](http://maven.apache.org/plugins/maven-shade-plugin/) is also provided, which contains CQEngine and all of its own dependencies packaged in a single jar file (ending "-all"). It can be downloaded from Maven central as "-all.jar" [here](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.googlecode.cqengine%22%20AND%20a%3A%22cqengine%22).

---


## Related Projects ##

  * CQEngine is somewhat similar to [Microsoft LINQ](http://en.wikipedia.org/wiki/Language_Integrated_Query), but a difference is LINQ queries on collections are evaluated via iteration/filtering whereas CQEngine uses set theory, thus CQEngine would outperform LINQ

  * [Concurrent Trees](http://github.com/npgall/concurrent-trees/) provides Concurrent Radix Trees and Concurrent Suffix Trees, used by some indexes in CQEngine


---


## Project Status ##

  * CQEngine 2.1.0 is the current release as of writing (October 2015), and is in Maven central
  * A [ReleaseNotes](documentation/ReleaseNotes.md) page has been added to document changes between releases
  * API / JavaDocs are available [here](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/index.html)

Report any bugs/feature requests in the [Issues](http://github.com/npgall/cqengine/issues) tab.
For support please use the [Discussion Forum](http://groups.google.com/forum/#!forum/cqengine-discuss), not direct email to the developers.


Many thanks to JetBrains for supporting CQEngine with free IntelliJ licenses!

[![](documentation/images/logo_jetbrains.png)](http://www.jetbrains.com)[![](documentation/images/logo_intellij_idea.png)](http://www.jetbrains.com/idea/)
