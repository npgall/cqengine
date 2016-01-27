# Ordering Strategies #

There is often a tradeoff between the overhead to retrieve results, and the overhead to sort results.
Therefore CQEngine supports various strategies to minimise the overhead, described below.

## Ordering strategy: "materialize" ##

By default, CQEngine uses what's known as the "materialize" strategy to order results.
Essentially, this allows CQEngine to use the most suitable indexes to locate objects matching the query, and then it sorts the results explicity afterwards.

## Ordering strategy: "index" ##
The "index" ordering strategy causes CQEngine to use an index on an attribute by which results must be ordered, to drive its search.
No other indexes will be used.

## Best practices ##

The "index" ordering strategy causes CQEngine to use an index on an attribute by which results must be ordered, to drive its search. No other indexes will be used.
* This strategy can be useful when results must be ordered in time series (most recent first, for example), and the objects which match the query will be stored consecutively in the index used for ordering.
* It also makes sense to use this strategy, when a query matches a large fraction of the collection - because it avoids the need to sort a large fraction of the collection afterwards.

The "materialize" ordering strategy allows CQEngine to use other indexes to locate objects matching the query, and then to sort those results afterwards.
* This strategy is useful for general queries, where ultimately the objects to be returned will not necessarily be stored consecutively in any particular index used for ordering.
* It also makes sense to retrieve results and sort them afterwards, when a small number of results would need to be sorted, and when other indexes can narrow the candidate set of objects more effectively than the index used for ordering.

The application can enable the index ordering strategy by setting a threshold value  via query options: `applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0))`

Selectivity thresholds:
* Threshold 1.0 tells CQEngine to always use the index ordering strategy, if the required indexes are available.
* Threshold 0.0 (the default for now) tells CQEngine to never use the "index" ordering strategy, and to always use the regular "materialize" strategy instead.
* Setting a threshold between 0.0 and 1.0, such as 0.5, causes CQEngine to choose between the "index" strategy and the "materialize" strategy automatically, based on the selectivity of the query.

What is selectivity?
* The selectivity of the query is a measure of how "selective" (or "specific") the query is, or in other words how big the fraction of the collection it matches is.
* A query with high selectivity (approaching 1.0) is specific: it matches a small fraction of the collection.
* A query with a low selectivity (approaching 0.0) is vague: it matches a large fraction of the collection.

If a threshold between 0.0 and 1.0 is specified, then CQEngine will compute the selectivity of the query automatically.
It will then automatically use the "index" strategy if the selectivity is below the given threshold, and the "materialize" strategy if the selectivity is above the given threshold.

However, actually computing the selectivity of the query, itself introduces computation overhead.
Performance can sometimes be better, by forcing use of a particular strategy for certain types of query, than to incur the overhead to try to compute the best strategy on-the-fly.
