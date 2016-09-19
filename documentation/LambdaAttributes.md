# CQEngine Lambda Attributes #

Examples of how to create CQEngine attributes from Java 8 lambda expressions and method references.

CQEngine attributes can be created from lambda expressions via the static factory methods in [QueryFactory](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/query/QueryFactory.html) which begin with `attribute()` or `nullableAttribute()`. 
* To use these methods, add import statement to your class: `import static com.googlecode.cqengine.query.QueryFactory.*`

Recommendations:
 * Although attributes can be created from lambda expressions on-the-fly, it is recommended to cache them for performance reasons; for example in `static` `final` variables, as is typical in other CQEngine examples.
 * It is not mandatory, but recommended, to provide a name for an attribute created from a lambda expression; because otherwise the JVM-assigned name of the lambda expression will be used, which could change between versions or instances of the application.

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

# Virtual Attributes #
Create attributes which apply functions over values read from a POJO or other data sources.

```java
Attribute<Car, Boolean> IS_CHEAP = attribute(car -> car.getPrice() < 4000);

```
