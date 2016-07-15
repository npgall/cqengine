# Ordering Strategies #

There is often a tradeoff between the overhead to retrieve results, and the overhead to sort results.
Therefore CQEngine supports various strategies to minimise the overhead, described below.

## Ordering strategy: _materialize_ ##

By default, CQEngine uses what's known as the _materialize_ strategy to order results.
Essentially, this allows CQEngine to use the most suitable indexes to locate objects matching the query, and then it sorts the results explicity afterwards.

## Ordering strategy: _index_ ##
The _index_ ordering strategy allows CQEngine to use an index on an attribute by which results must be ordered, to drive its search. No other indexes will be used for the search, but results will not need to be sorted afterwards.

This strategy can be enabled by configuring  [EngineThresholds.INDEX_ORDERING_SELECTIVITY](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/option/EngineThresholds.html#INDEX_ORDERING_SELECTIVITY) as discussed in _Best Practices_ below.

Note that the set of indexes required to support the _index_ ordering strategy on any particular attribute, depends on the type of attribute:
  * For `SimpleAttribute` (a type of attribute which returns exactly one value for every object in the collection):
    * An index is required on the attribute which will be used for ordering.
      * A single index is sufficient, because `SimpleAttribute` guarantees that every object in the collection will have exactly one value for this type of attribute.
  * For `SimpleNullableAttribute`, `MultiValueAttribute` or `MultiValueNullableAttribute`:
    * An index on the attribute which will be used for ordering, **AND**
    * An index on objects in the collection which are _missing_ values for the attribute which will be used for ordering.
      * A single index is NOT sufficient, because these types of attributes do not guarantee that every object in the collection will provide at least one value for these types of attributes.
      * For example, if results are to be ordered by an attribute `Car.FEATURES` but not every car in the collection has special features, then those objects in the collection will be missing from the index on `Car.FEATURES`.

The following is an example of how to enable the _index_ ordering strategy on a `MultiValueAttribute` (full source [here](../code/src/test/java/com/googlecode/cqengine/examples/ordering/IndexOrderingDemo.java)):

```java
public static void main(String[] args) {
    IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
    cars.addIndex(NavigableIndex.onAttribute(Car.FEATURES));
    cars.addIndex(NavigableIndex.onAttribute(forObjectsMissing(Car.FEATURES)));
    cars.addAll(CarFactory.createCollectionOfCars(100));

    ResultSet<Car> results = cars.retrieve(
            between(Car.CAR_ID, 40, 50),
            queryOptions(
                    orderBy(ascending(missingLast(Car.FEATURES))),
                    applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0))
            )
    );
    for (Car car : results) {
        System.out.println(car); // prints cars 40 -> 50, using the index on Car.FEATURES to accelerate ordering
    }
}
```



## Best practices ##

The _index_ ordering strategy forces CQEngine to use an index on an attribute by which results must be ordered (when available), to drive its search.
* This strategy can be useful when results must be ordered in time series (most recent first, for example), and the objects which match the query will be stored consecutively in the index used for ordering.
  * Therefore **[Partial Indexes](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/index/support/PartialIndex.html) can be extremely useful for index-accelerated ordering** as _they can be configured to store only the objects which match any query_.
* It also makes sense to use this strategy, when a query matches a large fraction of the collection - because it avoids the need to sort a large fraction of the collection afterwards.

The _materialize_ ordering strategy allows CQEngine to use other indexes to locate objects matching the query, and then to sort those results afterwards.
* This strategy is useful for general queries, where ultimately the objects to be returned will not necessarily be stored consecutively in any particular index used for ordering.
* It also makes sense to retrieve results and sort them afterwards, when a small number of results would need to be sorted, and when other indexes can narrow the candidate set of objects more effectively than the index used for ordering.

The application can enable the index ordering strategy by setting a threshold value  via query options: `applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0))`

**Selectivity thresholds:**
* Threshold 1.0 tells CQEngine to always use the index ordering strategy, if the required indexes are available.
* Threshold 0.0 (the default for now) tells CQEngine to never use the _index_ ordering strategy, and to always use the regular _materialize_ strategy instead.
* Setting a threshold between 0.0 and 1.0, such as 0.5, causes CQEngine to choose between the _index_ strategy and the _materialize_ strategy automatically, based on the selectivity of the query.

**What is selectivity?**
* The selectivity of the query is a measure of how "selective" (or "specific") the query is, or in other words how big the fraction of the collection it matches is.
* A query with high selectivity (approaching 1.0) is specific: it matches a small fraction of the collection.
* A query with a low selectivity (approaching 0.0) is vague: it matches a large fraction of the collection.

If a threshold between 0.0 and 1.0 is specified, then CQEngine will compute the selectivity of the query automatically.
It will then automatically use the _index_ strategy if the selectivity is below the given threshold, and the _materialize_ strategy if the selectivity is above the given threshold.

**Tradeoffs**

However note that there is a computation tradeoff:
* Computing the selectivity of the query, itself introduces computation overhead.
* Performance can sometimes be better, by forcing use of a particular strategy for certain types of query, than to incur the overhead to try to compute the best strategy on-the-fly.
