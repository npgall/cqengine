# Index Granularity and Quantization, Indexing Continuous Values #

[Quantization](http://en.wikipedia.org/wiki/Quantization_(signal_processing)) involves converting fine-grained or continuous values, to discrete or coarse-grained values. A Quantizer is a _function_ which takes fine-grained values as input, and maps those values to coarse-grained counterparts as its output, by discarding some precision.

Discrete values (e.g. `Integer`, `Long`, `BigInteger`) are values which have only a finite number of possible values, or which have a fixed spacing between possible values. Continuous values (e.g. `Float`, `Double`, `BigDecimal`) are values which do not have fixed spacing and which therefore can have an arbitrarily high precision.

Building indexes on continuous values can be challenging.

## Example ##

Consider a collection of `Car` objects having `Car.price` values `5000.00`, `5000.000001`, `5000.0000011`.

If an index was built on `Car.price` using full precision - `cars.addIndex(NavigableIndex.onAttribute(Car.PRICE))` - it would look like this:
```
5000.00 -> { Car{name=A, price=5000.00} }
5000.000001 -> { Car{name=B, price=5000.000001} }
5000.0000011 -> { Car{name=C, price=5000.0000011} }
```
This index would contain many entries, consuming a lot of memory, for only minor variations in `Car.price`.

The same index using a quantizer - `cars.addIndex(NavigableIndex.withQuantizerOnAttribute(DoubleQuantizer.withCompressionFactor(1), Car.Price))` - would look like this:
```
5000.0 -> { Car{name=A, price=5000.00}, Car{name=B, price=5000.000001}, Car{name=C, price=5000.0000011} }
```
Above, _compression factor 1_,  for `DoubleQuantizer`,  means _"truncate everything after the decimal point"_. Compression factor 5, would mean truncate everything after the decimal point AND group every 5 adjacent values in the index into the same entry.

## Retrieving objects from quantized indexes ##

CQEngine uses the following algorithm to retrieve objects matching a query from a quantized index:
  1. Apply the same quantization function as used to create the index, to values contained in the query
  1. Retrieve _candidate sets_ of objects matching the quantized values from the index
  1. Apply lazily-evaluated filters to the result set, which when iterated or queried, causes the result set to filter out objects which do not match the _original query_ thus restoring full precision for objects returned
    * For _equality-type_ queries such as `equal` or `in`, apply the filter to all candidate sets matching the query
    * For _range-type_ queries such as `between`, `lessThan`, `greaterThan` as supported by `NavigableIndex`, apply the filter to only the candidate sets at the _ends_ of the range; objects in candidate sets in the middle of the range inherently will not require filtering and can be retrieved at full speed

As such quantization can greatly reduce the memory requirements of indexes, and often without significant CPU overhead.

## Quantizers Provided ##

CQEngine provides the following Quantizers:

[IntegerQuantizer](http://cqengine.googlecode.com/svn/cqengine/javadoc/apidocs/com/googlecode/cqengine/quantizer/IntegerQuantizer.html), [LongQuantizer](http://cqengine.googlecode.com/svn/cqengine/javadoc/apidocs/com/googlecode/cqengine/quantizer/LongQuantizer.html), [BigIntegerQuantizer](http://cqengine.googlecode.com/svn/cqengine/javadoc/apidocs/com/googlecode/cqengine/quantizer/BigIntegerQuantizer.html), [FloatQuantizer](http://cqengine.googlecode.com/svn/cqengine/javadoc/apidocs/com/googlecode/cqengine/quantizer/FloatQuantizer.html), [DoubleQuantizer](http://cqengine.googlecode.com/svn/cqengine/javadoc/apidocs/com/googlecode/cqengine/quantizer/DoubleQuantizer.html), [BigDecimalQuantizer](http://cqengine.googlecode.com/svn/cqengine/javadoc/apidocs/com/googlecode/cqengine/quantizer/BigDecimalQuantizer.html)

Additional Quantizers can be used by implementing the [Quantizer](http://cqengine.googlecode.com/svn/cqengine/javadoc/apidocs/com/googlecode/cqengine/quantizer/Quantizer.html) interface.