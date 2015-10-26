### Version 2.0.3 - 2015-05-16 ###
  * Updated Xerial SQLite driver used by `DiskIndex` and `OffHeapIndex` to 3.8.10.1, improves hot redeploy support in webapps
  * Deployed to Maven central

### Version 2.0.2 - 2015-04-28 ###
  * Improved the performance of `DiskIndex` and `OffHeapIndex` when processing string `startsWith` queries
  * Deployed to Maven central

### Version 2.0.1 - 2015-04-20 ###
  * Fixed exception when using index ordering strategy, when a range of values in the query is not in the collection
  * Added validation to prevent duplicate indexes getting added
  * Deployed to Maven central

### Version 2.0.0 - 2015-04-07 ###
  * New `TransactionalIndexedCollection` supports read-committed transaction isolation using Multi-Version Concurrency Control (MVCC)
  * New ordering algorithm which can take advantage of indexes (see `orderingStrategy(INDEX)`)
  * New support to generate attributes automatically as bytecode (see `AttributeBytecodeGenerator`)
  * `IndexedCollection` can now be located in off-heap memory, or persisted on disk (see `OffHeapPersistence` and `DiskPersistence`)
  * New indexes can be located in off-heap memory, or persisted on disk (see `OffHeapIndex` and `DiskIndex`)
  * Tested with an IndexedCollections of 100 million objects
  * **CQEngine 2.0 contains some API changes since 1.3.2 - see the updated examples on the site for details**
  * Deployed to Maven central

### Version 1.3.2 - 2015-01-14 ###
  * Bugfix to `NavigableIndex`'s handling of range queries where the range values are exclusive and a quantizer is configured
  * Deployed to Maven central

### Version 1.3.1 - 2014-11-25 ###
  * Added new query types: `all()`, `none()`, and `regexMatches()`
  * Deployed to Maven central

### Version 1.2.7 - 2014-04-07 ###
  * Maintenance release
  * Bugfix for `ObjectLockingIndexedCollection.StripedLock`'s handling of negative hashcodes ([issue 35](https://code.google.com/p/cqengine/issues/detail?id=35))
  * Deployed to Maven central

### Version 1.2.6 - 2014-01-07 ###
  * Maintenance release
  * Bugfix for deduplication MATERIALIZE strategy ([issue 32](https://code.google.com/p/cqengine/issues/detail?id=32))
  * Deployed to Maven central

### Version 1.2.4 - 2013-11-22 ###
  * Maintenance release
  * Fixed _"FilteringIterator.hasNext advances iterator every time it is called"_, with thanks to Gabe Hicks for patch ([issue 23](https://code.google.com/p/cqengine/issues/detail?id=23))
  * Fixed performance bottleneck in `Query.hashCode()`, with thanks to Atul Vasu for contribution ([issue 25](https://code.google.com/p/cqengine/issues/detail?id=25))
  * Fixed _"Set remove() and removeAll() doesn't follow substitutability principle"_, with thanks to Atul Vasu for patch ([issue 27](https://code.google.com/p/cqengine/issues/detail?id=27))
  * Deployed to Maven central

### Version 1.2.2 - 2013-09-22 ###
  * Added `AttributesGenerator`, it is no longer necessary to write attributes by hand ([issue 22](https://code.google.com/p/cqengine/issues/detail?id=22))
  * See wiki page AttributesGenerator
  * Deployed to Maven central

### Version 1.2.1 - 2013-09-10 ###
  * Improved the toString representation of Query objects, query toStrings are now human readable ([issue 19](https://code.google.com/p/cqengine/issues/detail?id=19))
  * Bugfix in `SelfAttribute` ([issue 21](https://code.google.com/p/cqengine/issues/detail?id=21))
  * Deployed to Maven central

### Version 1.2.0 - 2013-08-20 ###
  * Added support to sort results in ascending/descending order on an attribute-by-attribute basis, with thanks to Roberto Socrates for patch ([issue 16](https://code.google.com/p/cqengine/issues/detail?id=16))
  * Example usage: `cars.retrieve(query, queryOptions(orderBy(ascending(Car.DOORS), descending(Car.PRICE))))`
  * This release involves a minor **API change** from CQEngine 1.1.x, so is not a drop-in replacement
  * Code using the old API to order results such as `cars.retrieve(query, queryOptions(orderByDescending(Car.PRICE, Car.DOORS)))` should be changed to that above
  * Deployed to Maven central

### Version 1.1.1 - 2013-08-18 ###
  * Additional work on `ReflectiveAttribute` to play nicer with inherited fields ([issue 18](https://code.google.com/p/cqengine/issues/detail?id=18))
  * Deployed to Maven central

### Version 1.1.0 - 2013-08-16 ###
  * Updated `RadixTreeIndex`, `ReversedRadixTreeIndex`, `InvertedRadixTreeIndex`, `SuffixTreeIndex` to use [concurrent-trees library 2.1](http://code.google.com/p/concurrent-trees/wiki/ReleaseNotes) for lower memory usage and lower latency ([issue 14](https://code.google.com/p/cqengine/issues/detail?id=14))
  * Added `CQEngine.newObjectLockingInstance(int concurrencyLevel)` providing object-level locking on the write path for applications in which threads might race each other to add/remove the same object ([issue 9](https://code.google.com/p/cqengine/issues/detail?id=9))
  * Bugfix to hashCode implementation in Not.java, would reduce collisions if queries stored in hash maps ([issue 13](https://code.google.com/p/cqengine/issues/detail?id=13))
  * Added support to `SimpleAttribute` to specify generic types manually via constructor to bypass reflection ([issue 17](https://code.google.com/p/cqengine/issues/detail?id=17))
  * Bugfix to `ReflectiveAttribute` to allow it to access private fields ([issue 18](https://code.google.com/p/cqengine/issues/detail?id=18))
  * Deployed to Maven central

### Version 1.0.3 - 2012-12-07 ###
  * Added new type of index, `UniqueIndex`, with thanks to Kinz Liu ([issue 8](https://code.google.com/p/cqengine/issues/detail?id=8)) - can be used with primary-key type attributes to reduce memory usage and yield faster query performance
  * Added `ReflectiveAttribute` - reflection-based attribute, for trading performance for convenience/flexibility/simplifying dynamic queries in some cases ([issue 6](https://code.google.com/p/cqengine/issues/detail?id=6))
  * Added benchmarks for indexing overhead ([issue 7](https://code.google.com/p/cqengine/issues/detail?id=7))
  * Deployed to Maven central

### Version 1.0.2 - 2012-11-22 ###
  * Added support for Has queries - `has(Car.DESCRIPTION)` and `not(has(Car.DESCRIPTION))` - the equivalent of SQL `IS NOT NULL` and `IS NULL` respectively. Use with `SimpleNullableAttribute`. These queries can be accelerated via `StandingQueryIndex`
  * Bugfix to updating compound indexes for objects added/removed after index added
  * Deployed to Maven central

### Version 1.0.1 - 2012-11-17 ###
  * Added support for customizing via factories, the construction of maps and sets used internally by `HashIndex`, `NavigableIndex` and `CompoundIndex` (concurrency level, load factor etc.) ([issue 5](https://code.google.com/p/cqengine/issues/detail?id=5))
  * Deployed to Maven central

### Version 1.0.0 - 2012-10-29 ###
  * Added support for attributes returning null values via `SimpleNullableAttribute`, `MultiValueNullableAttribute` ([issue 2](https://code.google.com/p/cqengine/issues/detail?id=2))
  * Bugfix to `ResultSet.uniqueResult()` ([issue 4](https://code.google.com/p/cqengine/issues/detail?id=4))
  * API same as in 0.9.1, no code changes necessary
  * Deployed to Maven central


### Version 0.9.1 - 2012-07-18 ###
  * Deployed to Maven central


### Version 0.9.0 - 2012-07-14 ###
  * Public upload of source