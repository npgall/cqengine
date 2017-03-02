# CQEngine Release Notes #

### Version 2.9.3 - 2017-03-02 ###
  * Maintenance release.
  * Updated the SQL grammar to allow NOT queries to be embedded in parentheses (Thanks to jarey for suggestions, issue #119).
  
### Version 2.9.2 - 2016-11-30 ###
  * Maintenance release.
  * Minor change to UniqueIndex to examine query types consistently (issue #109).
  
### Version 2.9.1 - 2016-11-29 ###
  * Maintenance release. Performance improvements.
  * Improved performance of `or()` queries, by avoiding use of indexes entirely for all child branches if one or more of the child branches do not have suitable indexes. This incurs a single collection scan for outer `or()` queries, instead potentially more than one scan, when no suitable indexes are available for some of the child branches (Thanks to Simon McDuff for suggestions).
  * Improved performance of `existsIn()` query used in JOINs, to avoid calculating the hashCode of the foreign collection (Thanks to uujava for suggestions, issue #102).
  * Improved performance of queries which can be accelerated by `UniqueIndex` (Thanks to uujava for pull request, issue #105).

### Version 2.9.0 - 2016-11-02 ###
  * Maintenance release. The minor version has been bumped only because of the following minor API change.
  * The `StreamFactory` class has been moved from package `com.googlecode.cqengine.query` to package `com.googlecode.cqengine.stream`.
  * This fixes an OSGi packaging problem, discussed in issue #96.
  
### Version 2.8.0 - 2016-09-19 ###
  * CQEngine attributes can now be created from Java 8 lambda expressions and method references:
    * `Attribute<Car, Double> PRICE = attribute(Car::getPrice);`
    * `Attribute<Car, Boolean> IS_CHEAP = attribute(car -> car.getPrice() < 4000);`
      * See [LambdaAttributes](LambdaAttributes.md) for more details.
  * Improved support for Java 8 Streams:
      * A `StreamFactory` class is now provided to convert CQEngine ResultSets to Java 8 Streams.
        * See [Streams](Streams.md) for more details.
      * Note: despite these Java 8 features, CQEngine remains fully compatible with Java 6 & 7.
  * Support for DateMath queries (via DateMathParser), and support for SQL boolean literals has been added to the SQL query dialect (issue #88).
  * Fixed issue where JOINs to a TransactionalIndexedCollection did not release read locks (issue #89).

### Version 2.7.1 - 2016-08-12 ###
  * Maintenance release.
  * Improved performance of `ResultSet.isEmpty()`/`isNotEmpty()` when results are to be ordered (issue #78).
  * Added `HashIndex.onSemiUniqueAttribute()` as a way to reduce memory overhead of a `HashIndex` (issue #67).
  * Fixed potential ClassCastException with combinations of certain queries with `CompoundIndex` (issue #85).
  * Improved `IndexedCollection.retainAll()` to avoid opening two connections to non-heap persistence and to reuse a single connection instead.
  * Improved `IndexedCollection.iterator().remove()` to integrate better with how resources are closed.

### Version 2.7.0 - 2016-07-13 ###
  * CQEngine now supports Partial Indexes.
    * The following new indexes are provided:
      * [PartialDiskIndex](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/disk/PartialDiskIndex.html)
      * [PartialOffHeapIndex](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/offheap/PartialOffHeapIndex.html)
      * [PartialNavigableIndex](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/navigable/PartialNavigableIndex.html)
    * Partial Indexes are similar to regular indexes and provide the same features, but they are also configured with an arbitrary _filter query_ which restricts the objects which will be stored in the index to those which match the _filter query_.
    * CQEngine will then use a partial index to accelerate a query, when it finds that a partial index on an attribute referenced in the query is available, *and* the set of objects which would match the query are logically a subset of objects which would match the _filter query_ of the partial index. See [PartialIndex](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/support/PartialIndex.html) for details.
    * As such, a partial index accelerates queries on an "interesting subset" of the collection, without incurring the overhead of indexing the entire collection.
    * Partial indexes require less storage space or memory than non-partial indexes, and they can yield better query performance as well, because they will contain fewer irrelevant entries not pertaining to the query.
    * Partial indexes are also particularly useful when used with [index-accelerated ordering](OrderingStrategies.md). They can store results which match a given filter query in pre-sorted order of the given attribute, which means that requesting results for that query and ordered by that attribute at runtime, can be answered quickly by the partial index without requiring any post-filtering or post-sorting.
  * A new type of persistence: WrappingPersistence
    * WrappingPersistence can wrap any Java collection, in a CQEngine IndexedCollection without any copying of objects.
    * This can be a convenient way to run queries or build indexes on existing collections.
    * However some caveats related to concurrency support and the performance of the underlying collection apply, see [WrappingPersistence](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/persistence/wrapping/WrappingPersistence.html) for details.
  * New support for `IndexedCollection<Map>` (IndexedCollections where dynamic Maps are used instead of POJOs)
    * [QueryFactory.mapAttribute()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/QueryFactory.html#mapAttribute-K-java.lang.Class-) can dynamically create an attribute which will read the value of an entry in these maps.
    * [QueryFactory.mapEntity()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/QueryFactory.html#mapEntity-java.util.Map-) and [QueryFactory.primaryKeyedMapEntity()](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/QueryFactory.html#primaryKeyedMapEntity-java.util.Map-java.lang.Object-) and can wrap these maps in optimized maps which accelerate equality and hashCode calculations improving query performance beyond what regular maps would allow.
    * Many thanks to Chris Kimpton for this contribution via pull request #68.
  * CQEngine is now OSGi compatible
    * Many thanks to Eduardo Fernández León for this contribution via pull request #72.
  * The MVCC algorithm in `TransactionalIndexedCollection` has been improved to prevent race conditions on the write path (issue #69).
  * The internal interface which indexes implement has been updated slightly, to introduce an [ObjectSet](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/persistence/support/ObjectSet.html) abstraction, which allows iterators and resources used by indexes to be released sooner during indexing operations, instead of as batches when indexing is complete.

### Version 2.6.0 - 2016-05-17 ###
  * Maintenance release.
  * This version is not radically different from 2.5.0; the minor version was bumped only due to the following minor API change.
    * Fixed issue #53, which involved removing some methods in QueryFactory which allowed `and()` and `or()` queries to be created programmatically with only a single child query.
      * `and()` and `or()` queries can only be created with a minimum of two child queries. Previously, calling these methods and supplying a single child resulted in an IllegalStateException at runtime. This change prevents these errors at compile time instead.
    * In some rare cases, applications might still wish to create `and()` and `or()` queries from a Collection of child queries.
      * The method which allowed to create these queries from a Collection of child queries has also been removed, because it allowed that collection to be empty or to contain only one child query - which similarly would have resulted in an IllegalStateException.
      * However applications which still wish to do this, can use the constructor of the And and Or query objects directly, instead of creating these queries via QueryFactory.
  * Other changes in this release:
    * Fixed issue #57: CQNParser now supports negative numbers.
    * Fixed issue #49: The NodeFactory used by the following indexes can now be configured, which can be useful to **tune memory usage**: `RadixTreeIndex`, `InvertedRadixTreeIndex`, `ReversedRadixTreeIndex`, `SuffixTreeIndex`.
    * Merged Pull Request #61
      * This adds supports to **reduce the latency of bulk imports** into DiskIndex, or when populating large IndexedCollections which are persisted to disk, by suspending synchronous writes to disk and suspending journaling, while the bulk import is in progress. For details see `SQLiteIndexFlags.BULK_IMPORT_SUSPEND_SYNC_AND_JOURNALING`.

### Version 2.5.0 - 2016-05-06 ###
  * Significant improvements in the write performance of disk and off-heap indexes, via IO batching
    * Previously, when objects were added to the collection, they were added to each index on disk in a separate IO operation per index.
    * Now, all operations are batched into a single round trip to disk, containing updates to all indexes which share the same type of persistence.
  * Improved concurrency in DiskIndex
    * Previously, the DiskIndex was unlike the on-heap indexes in that it did not fully support concurrent reads and writes: it supported concurrent reads, but writes were blocking.
    * Now, the DiskIndex supports fully concurrent reads and writes, and reads are lock-free using MVCC.
  * Improved locking support in OffHeapIndex
    * The OffHeapIndex does not yet support full concurrency: it supports concurrent reads, but writes will block reads.
      * This is a limitation in SQLite, on which OffHeapIndex depends.
    * Nonetheless, the locking support in OffHeapIndex has been improved. Previously if this index was in use, a writing thread would receive an exception. Now writes will transparently wait for reads to finish.
  * CompositePersistence and pluggable persistence implementations
    * CQEngine now allows custom indexes using arbitrary [Persistence](https://github.com/npgall/cqengine/blob/master/code/src/main/java/com/googlecode/cqengine/persistence/Persistence.java) implementations to be plugged in seamlessly.
    * Different types of persistence may be combined using the [CompositePersistence](https://github.com/npgall/cqengine/blob/master/code/src/main/java/com/googlecode/cqengine/persistence/composite/CompositePersistence.java) class.
    * A new abstraction called [ObjectStore](https://github.com/npgall/cqengine/blob/master/code/src/main/java/com/googlecode/cqengine/persistence/support/ObjectStore.java) allows the collection itself to be persisted on top of distributed caches, external databases, and still integrate with existing indexes.
  * Improved query performance for off-heap and disk persistence
    * Previously CQEngine would always use indexes to locate the smallest candidate set of objects, and it would then use on-the-fly filtering to reduce the candidate set to the final set of objects which match the query.
      * This is retrospectively known as the "default merge strategy".
    * This worked fine when the objects to be filtered were already on-heap, however it was expensive when the collection of objects itself was located off-heap or on disk, because candidate objects would need to be deserialized in order to perform this filtering.
    * CQEngine can now answer the query without deserializing candidate objects, by performing joins between multiple indexes on-the-fly instead
      * Only the final set of objects which actually match the query, will be deserialized; and lazily as the application requests them.
      * This strategy can be requested by supplying query option EngineFlags.PREFER_INDEX_MERGE_STRATEGY.
      * This requires that indexes are available for all of the attributes referenced in a query. If required indexes are not available, the default merge strategy will be used instead.
  * Performance improvements for in() queries
    * Previously in() queries were converted to potentially many equal() queries wrapped in an or() query.
    * This meant that potentially many index lookups or filtering steps would be performed, to evaluate all of the values provided in the in() query.
    * Now, all indexes support for in() queries directly, allowing these queries to be evaluated in fewer round trips.
  * **Backward compatibility**
    * The deduplication characteristics of ResultSet.size() has been changed, when ordering is requested but deduplication is not requested:
      * Previously, if a query requested that the results were to be ordered, but it did not request that they were to be deduplicated as well: the ResultSet.iterator() would return ordered and deduplicated results AND the ResultSet.size() method would reflect the outcome of that deduplication in its calculation of size too.
      * Now, if a query requested that the results are to be ordered, but it does not request that they are to be deduplicated as well: the ResultSet.iterator() will return ordered and deduplicated results as before BUT the ResultSet.size() method will not reflect the outcome of that deduplication in its calculation of size, and the size returned may count duplicate objects.
      * This change makes the calculation of size() faster when the application wants results to be ordered, but doesn't care about deduplication per-se, or when the query would not produce duplicates anyway.
      * The previous behaviour can be reinstated by requesting both ordering and deduplication.
      * In general, applications which want deduplication should request it explicitly, and they should not rely on the fact that ordering results sometimes provides deduplication as well.

### Version 2.1.3 - 2016-03-24 ###
  * Maintenance release.
  * Further improvements in index-accelerated ordering strategy.
  * ResultSet now implements java.io.Closeable, allowing use with Java 7 try-with-resources - closes issue #52.
  * Deployed to Maven Central.
 
### Version 2.1.2 - 2016-03-23 ###
  * Maintenance release.
  * Merged pull request #54 from devinrsmith which fixes an issue where calling MaterializingResultSet.hasNext() repeatedly without calling next() advances the iterator when it should not.
  * Fixed an edge case when index-accelerated ordering is enabled: if the primary attribute used for sorting was multi-valued, and some objects did not have any value for that attribute, then the objects which did not have a value for the attribute could be returned after the main results even if they did not fully match the query.
  * Deployed to Maven Central.

### Version 2.1.1 - 2016-01-27 ###
  * Maintenance release.
  * Merged pull requests from gzsombor and kminder which improve the handling of in() queries - many thanks!
  * Deployed to Maven Central.

### Version 2.1.0 - 2015-08-24 ###
  * Support for running SQL queries on the collection.
  * Support for running CQN (CQEngine Native) string-based queries on the collection (queries with the same syntax as programmatic queries, but in string form).
  * Significant performance improvements for complex queries.
  * Bulk import support for Off-heap and Disk persistence.
  * More fine-grained control over the ordering of objects by attributes where some objects might not have values for the attribute: orderBy(missingFirst(attribute)) and orderBy(missingLast(attribute)).
  * Nearly all indexes (On-heap, Off-heap and Disk) can now accelerate standing queries; StandingQueryIndex, which was on-heap only, is deprecated.
  * The statistics APIs exposed by indexes, now provide additional statistics on the distribution of values in the index, and allow applications to traverse indexes directly (for advanced use cases).
  * The performance of the "index" ordering strategy, useful for time time-series queries is improved.
  * Deployed to Maven central.
  * For more information and usage examples for this release see [this post](http://groups.google.com/d/msg/cqengine-discuss/Pvs0reAYC9U/DsyoCkwBBAAJ).

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
