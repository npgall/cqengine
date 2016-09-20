# CQEngine Lambda Attributes #

Examples of how to create CQEngine attributes from Java 8 lambda expressions and method references.

CQEngine attributes can be created from lambda expressions via the static factory methods in [QueryFactory](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/QueryFactory.html) which begin with `attribute()` or `nullableAttribute()`. 
* To use these methods, add import statement to your class: `import static com.googlecode.cqengine.query.QueryFactory.*`

*Recommendations:*
 * Although attributes can be created from lambda expressions on-the-fly, it is recommended to cache them for performance reasons; for example in `static` `final` variables, as is typical in other CQEngine examples.
 * It is not mandatory, but recommended, to provide a name for an attribute created from a lambda expression; because otherwise the JVM-assigned name of the lambda expression will be used, which could change between versions or instances of the application.
 * If your application targets non-server or less mainstream JVMs, it is recommended to specify the generic types of lambda expressions (see below).
 
*Limitations of Java 8 type inference*
  * Some of the methods in `QueryFactory` allow attributes to be created from lambda expressions, without the need to specify the generic types of the attributes. CQEngine attempts to dynamically detect the generic types of these lambda expressions at runtime.
  * However, note that as of Java 8, there are limitations on the ability to detect the generic types of lambda expressions at runtime in general, and so this might not always work correctly on all platforms. The support is somewhat JVM-specific, although at least OpenJDK and Oracle JDK are supported. CQEngine uses [TypeTools](https://github.com/jhalterman/typetools) to infer generic types; see that library for details.
  * If generic type information cannot be inferred from lambda expressions on a particular platform at runtime, CQEngine will throw an exception explaining the problem.
  * As a workaround, additional overloaded variants of the static factory methods in QueryFactory are provided, which allow the application to specify the generic types explicitly. Examples are provided below.
  * See the JavaDocs of [QueryFactory](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/QueryFactory.html#attribute-com.googlecode.cqengine.attribute.support.SimpleFunction-) for more details.

# Regular Attributes #

## SimpleAttribute ##

Create a [SimpleAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/SimpleAttribute.html) from a method reference to a POJO's getter method:
```java
public static final Attribute<Car, Double> PRICE = attribute(Car::getPrice);
```

Or with a name:
```java
public static final Attribute<Car, Double> PRICE = attribute("price", Car::getPrice);
```

## SimpleNullableAttribute ##
Create attributes which read from fields in a POJO by calling getter methods.

Create a [SimpleNullableAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/SimpleNullableAttribute.html) from a method reference to a POJO's getter method:
```java
public static final Attribute<Car, Double> PRICE = nullableAttribute(Car::getPrice);
```

Or with a name:
```java
public static final Attribute<Car, Double> PRICE = nullableAttribute("price", Car::getPrice);
```

## MultiValueAttribute ##

Create a [MultiValueAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/MultiValueAttribute.html) from a method reference to a POJO's getter method:
```java
public static final Attribute<Car, String> FEATURES = attribute(String.class, Car::getFeatures);
```

Or with a name:
```java
public static final Attribute<Car, String> FEATURES = attribute(String.class, "features", Car::getFeatures);
```

## MultiValueNullableAttribute ##

Create a [MultiValueNullableAttribute](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/attribute/MultiValueNullableAttribute.html) from a method reference to a POJO's getter method:
```java
public static final Attribute<Car, String> FEATURES = nullableAttribute(String.class, Car::getFeatures);
```

Or with a name:
```java
public static final Attribute<Car, String> FEATURES = nullableAttribute(String.class, "features", Car::getFeatures);
```

# Specifying Generic Types Explicitly #

The following example shows how to specify the generic types of an attribute (a `SimpleAttribute` in this case) explicitly.
This attribute therefore will work on all platforms.

```java
public static final Attribute<Car, Double> PRICE = attribute(Car.class, Double.class, "price", Car::getPrice);
```

# Virtual Attributes #
Create attributes which apply functions over values read from a POJO or other data sources.

```java
Attribute<Car, Boolean> IS_CHEAP = attribute(car -> car.getPrice() < 4000);

```
