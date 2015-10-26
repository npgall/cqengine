# Deduplication Strategies #

It is possible that a query would result in the same object being returned more than once by the same result set.

For example if an object matches several attribute values specified in an `or`-type query, then the object will be returned multiple times, one time for each attribute matched. Intersections (`and`-type queries) do not produce duplicates.

By default, CQEngine **does not** perform de-duplication of results; _however it can be instructed to do so_, using various strategies, which can be supplied via _query options_.

CQEngine supports the following de-duplication strategies.




---


## Duplicates Allowed Strategy ##

This is the default.

Example usage:
```
    DeduplicationOption deduplication = deduplicate(DeduplicationStrategy.DUPLICATES_ALLOWED);
    ResultSet<Car> results = cars.retrieve(query, queryOptions(deduplication));
```


---


## Logical Elimination Strategy ##

Eliminate duplicates using the rules of set theory, without _materializing_ (copying) the results into an intermediate set.

This is best explained as follows:

  * Let ∪ = conventional set union, duplicates are eliminated
  * Let ∪ₐ = union all, union without eliminating duplicates
  * Let – = set difference

Conventional set union, ∪, can be achieved by combining ∪ₐ (union all) with set difference –.

  * A ∪ B ∪ C = (A ∪ₐ (B – A)) ∪ₐ ((C – B) – A)

CQEngine implements this using the following algorithm (using lazy evaluation during iteration):

  1. Iterate and return all objects in set A
  1. Move on to objects in set B. For each object in set B, check if it is also contained in set A. If not, return it; otherwise skip it
  1. Move on to objects in set C. For each object in set C, check if it is contained in set A or set B. If not, return it; otherwise skip it

**Some notes**

  * _If it is known that for a given `or` query, that sets matching the query will be **disjoint**_ (will not contain duplicates), _logical elimination can be disabled at the query fragment level_. To do so, use [this](http://cqengine.googlecode.com/svn/cqengine/javadoc/apidocs/com/googlecode/cqengine/query/logical/Or.html#Or(java.util.Collection,%20boolean)) constructor of the `Or` query class.

  * _For `in`-type queries_ (which are equivalent to `or(equal(..), equal(..))`), _logical elimination is disabled by default_. This is because the values supplied for `in` queries all refer to the same attribute, and it is assumed that the application will take care of not supplying nonsense queries containing duplicate values for the same attribute. Note that `MultiValueAttribute`s can complicate this. To prevent logical deduplication being disabled for `in`-type queries, compose the query using nested `or(equal(..), ...)` queries instead.

Example usage:
```
    DeduplicationOption deduplication = deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION);
    ResultSet<Car> results = cars.retrieve(query, queryOptions(deduplication));
```

**Characteristics of this strategy**

  * Guarantees that no duplicates will be returned **provided the collection is not modified concurrently**
    * If an object was concurrently removed from set A _after it had already been returned_, it might be returned _again_
  * Has O(_r_ log(_s_)) time complexity (where _r_ is number of result objects matching the query, _s_ is number of sets to be unioned; use of log is a crude approximation; number of tests for containment depends on number of objects in each subsequent set and number of sets in total)
  * Slows iteration slightly
  * Has no significant memory overhead


---


## Materialize Strategy ##

Despite the typical interpretation, this does not materialize all objects up-front. Instead this strategy configures the result set to start returning objects matching the query immediately, and to record during iteration the objects issued so far in a temporary collection. If the query matches the same object more than once, this strategy will use the temporary collection to detect the duplicate, and it will transparently skip it on subsequent encounters.

Example usage:
```
    DeduplicationOption deduplication = deduplicate(DeduplicationStrategy.MATERIALIZE);
    ResultSet<Car> results = cars.retrieve(query, queryOptions(deduplication));
```

**Characteristics of this strategy**

  * Guarantees that no duplicates will ever be returned
  * Has O(_r_) time complexity
  * Slows iteration slightly
  * Memory overhead


---
