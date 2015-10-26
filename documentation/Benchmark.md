Benchmarks to compare the retrieval performance (latency) of CQEngine versus two types of iteration: _naive iteration_ and _optimized iteration_, previously discussed [here](TheLimitsOfIteration.md).

For those not familiar with microbenchmarks, be aware that results below are useful only for comparing the _relative_ latency of the approaches (and even then with caveats), and that _absolute_ latency is likely to be higher in production environments:  [Myths, Lies and Microbenchmarks](http://code.google.com/p/cqengine/wiki/FrequentlyAskedQuestions#Myths,_Lies_and_Microbenchmarks:_Isn't_x_milliseconds_fast)




---


# Benchmark Methodology #

The benchmark is based on the example of an online car dealership. The dealership has a website with a catalogue of 100,000 cars. So it has a collection of 100,000 `Car` objects. The site needs to retrieve cars matching criteria encapsulated in queries.

Several individual tests within the benchmark simulate retrieving cars based on various queries and with various CQEngine indexes in place. These tests measure retrieval time (or latency) for the same query using _naive iteration_, _optimized iteration_ and _CQEngine_.

The tests additionally measure the latency of _"CQEngine Statistics"_ which refers to the speed at which CQEngine can calculate the number of objects which _would_ match the query using its accelerated `ResultSet.size()` method. That method can determine the number of objects very quickly from statistics contained in indexes in some but not all of the test cases.

Class [Car (source)](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/testutil/Car.java) is a simple immutable object with CQEngine attributes defined for its fields: `carId`, `manufacturer`, `model`, `color`, `doors`, `price`.

Class [CarFactory (source)](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/testutil/CarFactory.java) contains a method which generates a collection of cars of any given size. The method creates the collection from a set of 10 templates. The first car is a Ford Focus, the second a Ford Fusion and so on. The method assigns a unique monotonically increasing `carId` to every car it creates. When more than 10 cars are requested, the method starts cycling through the templates again. Therefore car 0 and car 10 are both a Ford Focus, car 1 and car 11 are both a Ford Fusion and so on.

As such, some statistics about the makeup of the collection of cars:
| **Color** | **% of cars** | **Manufacturer** | **% of cars** | **Number of doors** | **% of cars** |
|:----------|:--------------|:-----------------|:--------------|:--------------------|:--------------|
| Red       | 30%           | Ford             | 30%           | 5                   | 50%           |
| Green     | 30%           | Honda            | 30%           | 4                   | 20%           |
| Blue      | 20%           | Toyota           | 30%           | 3                   | 20%           |
| Black     | 10%           | BMW              | 10%           | 2                   | 10%           |
| White     | 10%           |                  |               |                     |               |

Given that the benchmark creates a collection of 100,000 cars, as an example: 30,000 of those cars will be manufactured by Ford, and 10,000 cars will specifically be model Ford Focus, all evenly distributed throughout the collection.

The benchmarks were run on a 1.8GHz Intel Core i7 Apple machine running OS X 10.7.4 and Java 1.6.0\_26. All benchmarks were each run 10,000 times as a warmup, before running 10,000 times again for measurement. Garbage collection was performed before each measurement. Source code for the benchmark framework logic can be found [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/testutil/) and [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/BenchmarkRunner.java). Source code for individual tests is referenced in each test below.

The original tab-separated output from the benchmark is available as a spreadsheet [here](http://cqengine.googlecode.com/svn/wiki/documents/benchmark-results.ods).

Because CQEngine uses _lazy evaluation_ throughout, and so it returns `ResultSet`s for every query almost immediately; to measure the performance of CQEngine, these benchmarks retrieve a `ResultSet` from CQEngine, and then iterate through all cars in the result set, simply counting the number of results. As discussed in some of the examples below, it is unlikely that a real-world application would do this and therefore this is somewhat _harsh_ on CQEngine, _understating_ its advantages. However this seems the only way to compare performance versus iteration on an equal footing in this microbenchmark scenario.

CQEngine is the baseline in these results, and note the use of _"faster"_ instead of _"as fast as"_. If a result says that CQEngine is 1600% faster than iteration, it means that CQEngine can process 1600% _more_ requests in unit time than iteration (it is 1700%, or 17 times _as fast_).

The benchmark is single-threaded to measure latency rather than throughput, so used only one of the 1.8GHz CPU cores. Multi-threaded access, which CQEngine supports, would yield significantly higher throughput, and as such **throughput figures are significantly understated** by a factor of (up to) the number of cores.

# Benchmarks and Results #


---


## `UniqueIndex`: _Retrieve By Unique Key_ ##

Retrieve a single `Car` object from the collection based on `Car.carId` having value 500, which uniquely identifies a car. `UniqueIndex` on `Car.CAR_ID`.

This example demonstrates CQEngine's support for _constant_ retrieval time regardless of the size of the collection.

  * CQEngine query: `equal(Car.CAR_ID, 500)`
  * SQL equivalent: `SELECT * FROM cars WHERE carId = 500`
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/UniqueIndex_CarId.java)

**Benchmark Results**

  * **2,967,359 queries per second** (on a single 1.8GHz CPU core)
  * **0.337 microseconds per query**
  * CQEngine is **778473.29% faster** than naive iteration
  * CQEngine is **751179.23% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/unique-index-carid.png](http://cqengine.googlecode.com/svn/wiki/images/unique-index-carid.png)


---


## `HashIndex`: _Retrieve Manufacturer "Ford" (large result set)_ ##

Retrieve and iterate `Car` objects from the collection based on `Car.manufacturer` having value "`Ford`". `HashIndex` on `Car.MANUFACTURER`.

This query matches 30,000 cars; 30% of the collection of 100,000 cars. This benchmark example forces CQEngine to iterate all 30,000 cars, even though most applications would not require that many results. In contrast, the iteration approaches can _require_ the _entire_ collection to be iterated, because matching results could be located towards the end of the collection. CQEngine `ResultSet`s intentionally support _paging_ through results, and due to _lazy evaluation_, if the application stops iterating, no unnecessary computations would have been performed. This example is nonetheless to demonstrate CQEngine's performance when a large fraction of the collection is requested and processed.

  * CQEngine query: `equal(Car.MANUFACTURER, "Ford")`
  * SQL equivalent: `SELECT * FROM cars WHERE manufacturer = 'Ford'`
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/HashIndex_ManufacturerFord.java)

**Benchmark Results**

  * **1,256 queries per second** (on a single 1.8GHz CPU core)
  * **795.942 microseconds per query**
  * CQEngine is **423.17% faster** than naive iteration
  * CQEngine is **241.83% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/hash-index-manufacturer-ford.png](http://cqengine.googlecode.com/svn/wiki/images/hash-index-manufacturer-ford.png)


---


## `HashIndex`: _Retrieve Model "Focus" (smaller result set)_ ##

Retrieve and iterate `Car` objects from the collection based on `Car.model` having value "`Focus`". `HashIndex` on `Car.MODEL`.

This query matches 10,000 cars; 10% of the collection of 100,000 cars. CQEngine is less severely penalised compared with the example above; nevertheless this is a large fraction. CQEngine outperforms iteration by a wider margin in this case.

  * CQEngine query: `equal(Car.MODEL, "Focus")`
  * SQL equivalent: `SELECT * FROM cars WHERE model = 'Focus'`
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/HashIndex_ModelFocus.java)

**Benchmark Results**

  * **4,341 queries per second** (on a single 1.8GHz CPU core)
  * **230.361 microseconds per query**
  * CQEngine is **1627.06% faster** than naive iteration
  * CQEngine is **1324.17% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/hash-index-model-focus.png](http://cqengine.googlecode.com/svn/wiki/images/hash-index-model-focus.png)


---


## `NavigableIndex`: _Retrieve Price Between_ ##

Retrieve and iterate `Car` objects from the collection based on `Car.price` being _greater than or equal to_ 3000.00 and _less than_ 4000.00. `NavigableIndex` on `Car.PRICE`.

This query matches 20,000 cars; 20% of the collection of 100,000 cars.

  * CQEngine query: `between(Car.PRICE, 3000.0, true, 4000.0, false)`
    * _Note: true = inclusive, false = exclusive_
  * SQL equivalent: `SELECT * FROM cars WHERE price >= 3000.0 AND price < 4000.0`
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/NavigableIndex_PriceBetween.java)

**Benchmark Results**

  * **1,478 queries per second** (on a single 1.8GHz CPU core)
  * **676.763 microseconds per query**
  * CQEngine is **506.08% faster** than naive iteration
  * CQEngine is **325.89% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/navigable-index-price-between.png](http://cqengine.googlecode.com/svn/wiki/images/navigable-index-price-between.png)


---


## `RadixTreeIndex`: _Retrieve Models Starting With_ ##

Retrieve and iterate `Car` objects from the collection based on `Car.model` starting with "`P`". `RadixTreeIndex` on `Car.model`.

This query matches 10,000 cars; 10% of the collection of 100,000 cars. Note: See also `ReversedRadixTreeIndex` which supports _ends with_-type queries. It would have identical performance characteristics as this `RadixTreeIndex` so it was not benchmarked separately.

  * CQEngine query: `startsWith(Car.MODEL, "P")`
  * SQL equivalent: `SELECT * FROM cars WHERE model LIKE 'P%'`
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/RadixTreeIndex_ModelStartsWithP.java)

**Benchmark Results**

  * **2,788 queries per second** (on a single 1.8GHz CPU core)
  * **358.622 microseconds per query**
  * CQEngine is **1357.81% faster** than naive iteration
  * CQEngine is **1110.12% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/radix-tree-index-model-starts-with-p.png](http://cqengine.googlecode.com/svn/wiki/images/radix-tree-index-model-starts-with-p.png)


---


## `SuffixTreeIndex`: _Retrieve Models Containing_ ##

Retrieve and iterate `Car` objects from the collection based on `Car.model` containing "`g`". `SuffixTreeIndex` on `Car.model`.

This query matches 10,000 cars; 10% of the collection of 100,000 cars.

  * CQEngine query: `contains(Car.MODEL, "g")`
  * SQL equivalent: `SELECT * FROM cars WHERE model LIKE '%g%'`
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/SuffixTreeIndex_ModelContainsG.java)

**Benchmark Results**

  * **3,053 queries per second** (on a single 1.8GHz CPU core)
  * **327.574 microseconds per query**
  * CQEngine is **1860.53% faster** than naive iteration
  * CQEngine is **1605.31% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/suffix-tree-index-model-contains-g.png](http://cqengine.googlecode.com/svn/wiki/images/suffix-tree-index-model-contains-g.png)


---


## No indexes: _Retrieve Model "Focus"_ ##

Retrieve and iterate `Car` objects from the collection based on `Car.model` having value "`Focus`". No indexes.

This example demonstrates a **mis-configured application** which has not added any indexes. CQEngine can process any query, but doing so without any suitable indexes in place prevents CQEngine from accelerating the query, and it can _underperform_ iteration. Applications should ensure that they add suitable indexes. This query matches 10,000 cars; 10% of the collection of 100,000 cars.

  * CQEngine query: `equal(Car.MODEL, "Focus")`
  * SQL equivalent: `SELECT * FROM cars WHERE model = 'Focus'`
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/NoIndexes_ModelFocus.java)

**Benchmark Results**

  * **153 queries per second** (on a single 1.8GHz CPU core)
  * **6524.906 microseconds per query**
  * **Naive iteration** is **43.21% faster** than CQEngine
  * **Optimized iteration** is **53.27% faster** than CQEngine

![http://cqengine.googlecode.com/svn/wiki/images/no-indexes-model-focus.png](http://cqengine.googlecode.com/svn/wiki/images/no-indexes-model-focus.png)


---


## Non-Optimal Indexes: _Retrieve Blue Toyota Cars with Three Doors_ ##

Retrieve and iterate `Car` objects from the collection based on `Car.manufacturer` having value "`Toyota`" AND `Car.color` having value `Car.Color.BLUE` AND `Car.doors` having value 3.

`HashIndex` on `Car.DOORS`. _No indexes for Manufacturer or Color_.

This example demonstrates CQEngine's handling of queries for which optimal indexes do not exist. An ideal configuration for this type of query would have a `CompoundIndex` on the three fields above to enable constant time retrieval. However in this instance, only a `HashIndex` on `Car.DOORS` has been added. CQEngine makes the most of the available indexes. In this case it uses the `Car.DOORS` index to reduce the _candidate set_ from 100,000 cars to 20,000 cars, and then it _iterates_ the candidate set, applying _on-the-fly_ filtering to return only those cars which match the rest of the query. This query ultimately matches 10,000 cars; 10% of the collection of 100,000 cars.

Note also that retrieving **CQEngine Statistics** is much slower in this example than in others. CQEngine Statistics refers to calling **`resultSet.size()`** on a `ResultSet` returned by CQEngine, in preference to determining size by actually iterating and counting the results.

CQEngine can often calculate how many objects _would match_ a query without actually iterating through any objects, based on the internal counts available in entries within indexes. In this example, because indexes are not available to provide complete statistics, CQEngine falls back to counting objects by applying _on-the-fly_ filtering to the candidate set, which incurs a higher cost.

CQEngine can often _accelerate_ the **`resultSet.contains()`** method in a similar fashion (applying set containment tests to individual sets within indexes). In this example, that method would be similarly affected, internally falling back to on-the-fly filtering of the candidate set.

  * CQEngine query:
```
and(
    equal(Car.MANUFACTURER, "Toyota"),
    equal(Car.COLOR, Car.Color.BLUE),
    equal(Car.DOORS, 3)
)
```
  * SQL equivalent:
```
SELECT * FROM cars
WHERE
    manufacturer = 'Toyota'
    AND color = 'blue'
    AND doors = 3
```
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/NonOptimalIndexes_ManufacturerToyotaColorBlueDoorsThree.java)

**Benchmark Results**

  * **848 queries per second** (on a single 1.8GHz CPU core)
  * **1179.856 microseconds per query**
  * CQEngine is **172.60% faster** than naive iteration
  * CQEngine is **136.61% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/non-optimal-indexes-manufacturer-color-doors.png](http://cqengine.googlecode.com/svn/wiki/images/non-optimal-indexes-manufacturer-color-doors.png)


---


## Optimal Indexes - `CompoundIndex`: _Retrieve Blue Toyota Cars with Three Doors_ ##

Retrieve and iterate `Car` objects from the collection based on `Car.manufacturer` having value "`Toyota`" AND `Car.color` having value `Car.Color.BLUE` AND `Car.doors` having value 3. `CompoundIndex` on `Car.MANUFACTURER`, `Car.COLOR`, `Car.DOORS`.

This example demonstrates the improvement in performance when an **optimal** index is added, for the _same query_ as in the non-optimal indexes example above. This query matches 10,000 cars; 10% of the collection of 100,000 cars.

  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/CompoundIndex_ManufacturerToyotaColorBlueDoorsThree.java)

**Benchmark Results**

  * **4,735 queries per second** (on a single 1.8GHz CPU core)
  * **211.196 microseconds per query**
  * CQEngine is **1625.70% faster** than naive iteration
  * CQEngine is **1283.20% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/compound-index-manufacturer-color-doors.png](http://cqengine.googlecode.com/svn/wiki/images/compound-index-manufacturer-color-doors.png)


---


## `StandingQueryIndex`: _Retrieve Blue Toyota Cars with Not Five Doors_ ##

Retrieve and iterate `Car` objects from the collection based on `Car.manufacturer` having value "`Toyota`" AND `Car.color` having value `Car.Color.BLUE` AND `Car.doors` **NOT** having value 5.

`StandingQueryIndex` on the query. No other indexes.

This example demonstrates `StandingQueryIndex`. This type of index maintains a set of objects matching a _query_, or fragment of a query. As objects are added to and removed from the collection, it tests objects to see if they match the query, adding them to or removing them from this set as necessary. When CQEngine sees a query or fragment of a query for which a standing query index exists, it can retrieve matching objects for that query or query fragment with constant time complexity. This query matches 10,000 cars; 10% of the collection of 100,000 cars.

  * CQEngine query:
```
and(
    equal(Car.MANUFACTURER, "Toyota"),
    equal(Car.COLOR, Car.Color.BLUE),
    not(equal(Car.DOORS, 5))
)
```
  * SQL equivalent:
```
SELECT * FROM cars
WHERE
    manufacturer = 'Toyota'
    AND color = 'blue'
    AND doors <> 3
```
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/StandingQueryIndex_ManufacturerToyotaColorBlueDoorsNotFive.java)

**Benchmark Results**

  * **4,796 queries per second** (on a single 1.8GHz CPU core)
  * **208.515 microseconds per query**
  * CQEngine is **1607.43% faster** than naive iteration
  * CQEngine is **1299.96% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/standing-query-index-manufacturer-color-doors.png](http://cqengine.googlecode.com/svn/wiki/images/standing-query-index-manufacturer-color-doors.png)


---


## Quantized `HashIndex`: _Retrieve By Unique Key_ ##

Retrieve a single `Car` object from the collection based on `Car.carId` having value 500, which uniquely identifies a car. _Quantized_ `HashIndex` on `Car.CAR_ID` with compression factor 5.

This example demonstrates the effect of **_quantization_** on indexes. Quantization allows the _granularity_ of indexes to be controlled. In this example, the index was created with an `IntegerQuantizer` with compression factor 5. This means that every 5 consecutive `carId`s will be grouped together and stored as a single entry in the index. This reduces the _size_ of the index (reduces memory overhead) and trades it instead for slightly higher _CPU utilization_ during retrieval. The index will need to filter results retrieved from the entry _on-the-fly_, to ensure that they actually match the query.

  * CQEngine query: `equal(Car.CAR_ID, 501)`
  * SQL equivalent: `SELECT * FROM cars WHERE carId = 501`
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/Quantized_HashIndex_CarId.java)

**Benchmark Results**

  * **1,639,344 queries per second** (on a single 1.8GHz CPU core)
  * **0.61 microseconds per query**
  * CQEngine is **481984.43% faster** than naive iteration
  * CQEngine is **467645.90% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/quantized-hash-index-unique-carid.png](http://cqengine.googlecode.com/svn/wiki/images/quantized-hash-index-unique-carid.png)


---


## Quantized `NavigableIndex`: _Retrieve CarId Between_ ##

Retrieve three `Car` objects from the collection based on `Car.carId` having values between 500 and 502. _Quantized_ `NavigableIndex` on `Car.CAR_ID` with compression factor 5.

This example demonstrates that _quantization_ can be applied to navigable indexes, with minimal overhead on _range_-type queries. CQEngine applies _on-the-fly_ filtering only to quantized entries at the _start_ and/or _end_ of ranges.

  * CQEngine query: `between(Car.CAR_ID, 500, 502)`
  * SQL equivalent: `SELECT * FROM cars WHERE carId BETWEEN 500 AND 502`
  * Source code: [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/benchmark/tasks/Quantized_NavigableIndex_CarId.java)

**Benchmark Results**

  * **1,116,071 queries per second** (on a single 1.8GHz CPU core)
  * **0.896 microseconds per query**
  * CQEngine is **330187.50% faster** than naive iteration
  * CQEngine is **325727.79% faster** than optimized iteration

![http://cqengine.googlecode.com/svn/wiki/images/quantized-navigable-index-carid-between.png](http://cqengine.googlecode.com/svn/wiki/images/quantized-navigable-index-carid-between.png)


---


# Object Insertion Speed / Indexing Overhead #

The benchmarks above measure the performance of CQEngine when retrieving objects. This section measures the performance and overhead in CQEngine to build indexes on objects in the first place.

The source code for these benchmarks can be found [here](http://cqengine.googlecode.com/svn/cqengine/trunk/src/test/java/com/googlecode/cqengine/indexingbenchmark/). The original tab-separated output from the benchmark is available as a spreadsheet [here](http://cqengine.googlecode.com/svn/wiki/documents/indexing-benchmark-results.ods).

This benchmark followed the same methodology discussed in the methodology section above, on the same 1.8GHz Apple machine.

The benchmark was run with a collection of 100,000 car objects, measuring the time taken to build the following types of index on the collection.

Typically, the overhead to index an entire collection would only be incurred when objects are first added to an `IndexedCollection` in bulk. Subsequently, objects can be added to and removed from the `IndexedCollection` directly, so incurring only the per-object overhead.

It should be noted that if all indexes are added to an `IndexedCollection` before objects are added in bulk, then all indexes will be built in a _single pass_; such that if multiple indexes are added in advance, the overhead would be _less than_ the sum of those indicated.

**Results**

| **Index** | **Microseconds to index 100,000-object collection** | **Microseconds per object** | **Inserts per second (single 1.8GHz core)** |
|:----------|:----------------------------------------------------|:----------------------------|:--------------------------------------------|
| `UniqueIndex` on CarId | 24032.8                                             | 0.24                        | 4,166,667                                   |
| `HashIndex` on CarId | 874702.26                                           | 8.747                       | 114,324                                     |
| Quantized `HashIndex` on CarId (compression factor 10) | 85204.84                                            | 0.852                       | 1,173,709                                   |
| `HashIndex` on Manufacturer | 28487.08                                            | 0.284                       | 3,521,127                                   |
| `HashIndex` on Model | 27310.68                                            | 0.273                       | 3,663,004                                   |
| `CompoundIndex` on Manufacturer, Color, Doors | 109761.58                                           | 1.097                       | 911,577                                     |
| `NavigableIndex` on Price | 30393.94                                            | 0.303                       | 3,300,330                                   |
| `RadixTreeIndex` on Model | 58100.02                                            | 0.581                       | 1,721,170                                   |
| `SuffixTreeIndex` on Model | 27386.04                                            | 0.273                       | 3,663,004                                   |

**Findings**

  * It takes 24 milliseconds to bulk-index 100,000 objects (4,166,667 inserts per second, on a single 1.8GHz CPU core), when the associated attribute has a unique value per object and a unique index is used
  * It takes 875 milliseconds to bulk-index 100,000 objects in the worst case, when the associated attribute has a unique value per object and a non-unique index is used
  * It takes 27 milliseconds to bulk-index 100,000 objects, when a non-unique index is used and the associated attribute has only 10 distinct values across the collection
  * For non-unique indexes, indexing speed correlates closely with the number of distinct values in the attribute(s) on which an index is built
  * Supplying a Quantizer to non-unique indexes, reduces the number of unique entries required in the index, and so reduces the time required to build those indexes significantly
  * Indexing speed increases approximately in line with the compression factor supplied to a Quantizer
  * It takes a few hundred nanoseconds to add add/remove individual objects to/from existing indexes
  * Overall, CQEngine exhibits very low latency in building indexes