# CQEngine Merge Strategies #

Merge strategies are the algorithms CQEngine uses to evaluate queries which have multiple branches.

## Merge strategy: _default_ ##

By default, CQEngine's algorithm to evaluate queries, is to use indexes to locate the _smallest candidate set_ of objects,
and then to use on-the-fly filtering to reduce the candidate set to the final set of objects which match the query.

To perform filtering, this strategy asks the attributes referenced in the query, to return the value of that attribute from each
candidate object.

**Generally speaking, this provides best performance when the objects from which attributes read the values, are already in memory
on the Java heap. Typically this is the case when the IndexedCollection is located on-heap.**

However, this strategy can be expensive when objects from which attributes read the values are located off-heap or on disk,
because it is usually necessary to deserialize the candidate objects in order to read their values.

## Merge strategy: _index_ ##

The "index" merge strategy uses indexes to locate the _smallest candidate set_ of objects, however instead of asking the attributes
to return values from the candidate objects, it evaluates the query by performing joins between indexes on-the-fly instead.

Only the final set of objects which actually match the query, will be deserialized; and lazily as the application requests them.

**Generally speaking, this provides best performance when the objects from which attributes read the values, are not stored on the Java heap. Typically this is the case when the IndexedCollection is located off-heap, or on disk.**

This strategy can be requested by supplying query option [EngineFlags.PREFER_INDEX_MERGE_STRATEGY](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/option/EngineFlags.html#PREFER_INDEX_MERGE_STRATEGY).

Note this strategy requires that indexes are available for all of the attributes referenced in a query. If required indexes
are not available, the default merge strategy will be used instead.
