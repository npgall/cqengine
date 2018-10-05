This page collects tips on using CQEngine with other JVM languages besides Java.

# Scala #

## Defining CQEngine attributes in Scala ##

The following is a summary of [a discussion](https://groups.google.com/forum/#!topic/cqengine-discuss/PDodDlYBNOY) in the forum. See the full discussion for more details.

Consider the following scala code which defines a simple class, with CQEngine attributes to access its fields:

```scala
class TestClass {
  private var _x1: Int = 0
  private var _x2: Int = 1

  def x1 = _x1
  def x2 = _x2

  def this(y1: Int, y2: Int) {
    this()
    this._x1 = y1
    this._x2 = y2
  }
}

object TestClass {
  def doSomething(x: Int, y: Int): Int = x * y

  final val X_1: Attribute[TestClass, Int] = new SimpleAttribute[TestClass,Int]("X_1") {
    def getValue(testClass: TestClass, queryOptions: QueryOptions): Int = testClass.x1
  }

  final val X_2: Attribute[TestClass, Int] = new SimpleAttribute[TestClass,Int]("X_2") {
    def getValue(testClass: TestClass, queryOptions: QueryOptions): Int = testClass.x2
  }
}
```

**Problem**

If an attempt is made as follows in a different class, to add an index on the attribute:

```scala
val testClasses: IndexedCollection[TestClass] = new ConcurrentIndexedCollection[TestClass]()
testClasses.addIndex(NavigableIndex.onAttribute(TestClass.X_1))
```

..then the compiler might produce error: _"Cannot resolve reference onAttribute with such signature"_
..even though `TestClass.X_1` is of type `Attribute[TestClass, Int]` (or `Attribute<O, A>` in Java).

Additionally, if the code above is changed to:
```scala
testClasses.addIndex(NavigableIndex.onAttribute(TestClass.X_1()));
```

..a more detailed compilation error is produced: _"no instance(s) of type variable(s) A exist so that Object confirms to Comparable<A>"_.

**Solution**

The errors above indicate that Scala's `Int` is not the same as a Java `Integer`.

However as Scala `Integer` _is_ the same as a Java `Integer`, a solution is to change the definition of the `X_1` attribute in the `TestClass` companion object to:

```scala
final val X_1: Attribute[TestClass, Integer] = new SimpleAttribute[TestClass, Integer]("X_1") {
  def getValue(testClass: TestClass, queryOptions: QueryOptions): Integer = testClass.x1
}
```
The code should then compile and run successfully.

### Alternative workaround ###

Another user [mentioned](https://groups.google.com/d/msg/cqengine-discuss/PDodDlYBNOY/NUuWJ43lAQAJ) it's possible to work around this issue by tweaking Scala to use Java types rather than the ones from Scala. In the Scala main class, set:

```scala
type jLong = java.lang.Long
type jInt = java.lang.Integer
```

# Kotlin #

## Defining CQEngine attributes in Kotlin ##

**Problem**

When using `QueryFactory.attribute(Foo::bar)` to generate an attribute, the underlying call to
`TypeResolver.resolveRawArguments` might be unable to extract the type information from a kotlin-generated function call.

**Solution**

An inline helper method in Kotlin, will give you the same functionality, in simple oneliner:

```kotlin
inline fun <reified O, reified A> attribute(accessor: KProperty1<O, A>): FunctionalSimpleAttribute<O, A> {
    return FunctionalSimpleAttribute(O::class.java, A::class.java, accessor.javaClass.simpleName, SimpleFunction { accessor.get(it) })
}
```
Then you can use:
```kotlin
class Foo(
    val bar: Int
) {
    companion object {
        val BAR = attribute(Foo::bar)
    }
}
```

