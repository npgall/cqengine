CQEngine provides two methods for generating attributes automatically: **bytecode**, or **source code**.



# AttributeBytecodeGenerator #

The following example uses [AttributeBytecodeGenerator](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/codegen/AttributeBytecodeGenerator.html) to generate attribute bytecode automatically, and then uses the generated attribute in a query at runtime.

```java
package com.googlecode.cqengine.examples.codegen;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.codegen.AttributeBytecodeGenerator;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * Demonstrates how to auto-generate bytecode for CQEngine attributes which access fields in a given class, which
 * can then be used directly at runtime.
 *
 * @author Niall Gallagher
 */
public class GenerateAttributeByteCode {

    public static void main(String[] args) throws Exception {
        // Generate an attribute from bytecode to read the Car.model field...
        Class<? extends SimpleAttribute<Car, String>> carModelAttributeClass = AttributeBytecodeGenerator.generateSimpleAttributeForField(Car.class, String.class, "model", "model");
        SimpleAttribute<Car, String> MODEL = carModelAttributeClass.newInstance();

        // Create a collection of 10 Car objects (Ford Focus, Honda Civic etc.)...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addAll(CarFactory.createCollectionOfCars(10));

        // Retrieve the cars whose Car.model field is "Civic" (i.e. the Honda Civic)...
        ResultSet<Car> results = cars.retrieve(equal(MODEL, "Civic"));
        for (Car car : results) {
            System.out.println(car);
        }
        // ..prints:
        // Car{carId=3, manufacturer='Honda', model='Civic', color=WHITE, doors=5, price=4000.0, features=[grade b]}
    }
}
```

# AttributeSourceGenerator #
CQEngine also provides an [AttributeSourceGenerator](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/codegen/AttributeSourceGenerator.html), which given any target class, can automatically generate source code for CQEngine attributes which access the fields in the target class.

The source code for attributes can either be generated for the purpose of copy & pasting into the target  class, or attributes can be generated into a separate companion class.

The examples below generate attributes for a class `Car`. Note some fields are primitives, and others are objects, arrays or lists. The type of field determines the type of attribute that AttributesGenerator will generate. Also note that AttributesGenerator will generate comments in the source code for each attribute, providing guidance on how performance could be tuned for each field, depending on its nullability.
```java
public class Car {
    final int carId;
    final String manufacturer;
    final String model;
    final double[] prices;
    final String[] extras;
    final List<String> features;

    public Car(int carId, String manufacturer, String model, double[] prices, String[] extras, List<String> features) {
        this.carId = carId;
        this.manufacturer = manufacturer;
        this.model = model;
        this.prices = prices;
        this.extras = extras;
        this.features = features;
    }
}
```
## Option 1: Generate attributes only, for copy & paste ##
Usage:
```java
package com.googlecode.cqengine.examples.codegen;
import com.googlecode.cqengine.codegen.AttributeSourceGenerator;

public class GenerateAttributesForCopyPaste {
    public static void main(String[] args) {
        System.out.println(AttributeSourceGenerator.generateAttributesForPastingIntoTargetClass(Car.class));
    }
}
```
Outputs source code for attributes, which can be copy & pasted directly into the Car class:
```java
    /**
     * CQEngine attribute for accessing field {@code Car.carId}.
     */
    public static final Attribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("CAR_ID") {
        public Integer getValue(Car car, QueryOptions queryOptions) { return car.carId; }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.manufacturer}.
     */
    // Note: For best performance:
    // - if this field cannot be null, replace this SimpleNullableAttribute with a SimpleAttribute
    public static final Attribute<Car, String> MANUFACTURER = new SimpleNullableAttribute<Car, String>("MANUFACTURER") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.manufacturer; }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.model}.
     */
    // Note: For best performance:
    // - if this field cannot be null, replace this SimpleNullableAttribute with a SimpleAttribute
    public static final Attribute<Car, String> MODEL = new SimpleNullableAttribute<Car, String>("MODEL") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.model; }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.prices}.
     */
    // Note: For best performance:
    // - if this field cannot be null, replace this
    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())
    public static final Attribute<Car, Double> PRICES = new MultiValueNullableAttribute<Car, Double>("PRICES", false) {
        public Iterable<Double> getNullableValues(final Car car, QueryOptions queryOptions) {
            return new AbstractList<Double>() {
                public Double get(int i) { return car.prices[i]; }
                public int size() { return car.prices.length; }
            };
        }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.extras}.
     */
    // Note: For best performance:
    // - if the array cannot contain null elements change true to false in the following constructor, or
    // - if the array cannot contain null elements AND the field itself cannot be null, replace this
    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())
    public static final Attribute<Car, String> EXTRAS = new MultiValueNullableAttribute<Car, String>("EXTRAS", true) {
        public Iterable<String> getNullableValues(Car car, QueryOptions queryOptions) { return Arrays.asList(car.extras); }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.features}.
     */
    // Note: For best performance:
    // - if the list cannot contain null elements change true to false in the following constructor, or
    // - if the list cannot contain null elements AND the field itself cannot be null, replace this
    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())
    public static final Attribute<Car, String> FEATURES = new MultiValueNullableAttribute<Car, String>("FEATURES", true) {
        public Iterable<String> getNullableValues(Car car, QueryOptions queryOptions) { return car.features; }
    };
```
## Option 2: Generate a companion class ##
Usage:
```java
package com.googlecode.cqengine.examples.codegen;
import com.googlecode.cqengine.codegen.AttributeSourceGenerator;

public class GenerateSeparateAttributesClass {

    public static void main(String[] args) {
        System.out.println(AttributeSourceGenerator.generateSeparateAttributesClass(Car.class, Car.class.getPackage()));
    }
}
```
Outputs source code for a companion class containing the generated attributes:
```java
package com.googlecode.cqengine.examples.codegen;

import com.googlecode.cqengine.attribute.*;
import com.googlecode.cqengine.query.option.QueryOptions;
import java.util.*;
import com.googlecode.cqengine.examples.codegen.Car;

/**
 * CQEngine attributes for accessing fields in class {@code com.googlecode.cqengine.examples.codegen.Car}.
 * <p/>.
 * Auto-generated by CQEngine's {@code AttributeSourceGenerator}.
 */
public class CQCar {

    /**
     * CQEngine attribute for accessing field {@code Car.carId}.
     */
    public static final Attribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("CAR_ID") {
        public Integer getValue(Car car, QueryOptions queryOptions) { return car.carId; }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.manufacturer}.
     */
    // Note: For best performance:
    // - if this field cannot be null, replace this SimpleNullableAttribute with a SimpleAttribute
    public static final Attribute<Car, String> MANUFACTURER = new SimpleNullableAttribute<Car, String>("MANUFACTURER") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.manufacturer; }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.model}.
     */
    // Note: For best performance:
    // - if this field cannot be null, replace this SimpleNullableAttribute with a SimpleAttribute
    public static final Attribute<Car, String> MODEL = new SimpleNullableAttribute<Car, String>("MODEL") {
        public String getValue(Car car, QueryOptions queryOptions) { return car.model; }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.prices}.
     */
    // Note: For best performance:
    // - if this field cannot be null, replace this
    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())
    public static final Attribute<Car, Double> PRICES = new MultiValueNullableAttribute<Car, Double>("PRICES", false) {
        public Iterable<Double> getNullableValues(final Car car, QueryOptions queryOptions) {
            return new AbstractList<Double>() {
                public Double get(int i) { return car.prices[i]; }
                public int size() { return car.prices.length; }
            };
        }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.extras}.
     */
    // Note: For best performance:
    // - if the array cannot contain null elements change true to false in the following constructor, or
    // - if the array cannot contain null elements AND the field itself cannot be null, replace this
    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())
    public static final Attribute<Car, String> EXTRAS = new MultiValueNullableAttribute<Car, String>("EXTRAS", true) {
        public Iterable<String> getNullableValues(Car car, QueryOptions queryOptions) { return Arrays.asList(car.extras); }
    };

    /**
     * CQEngine attribute for accessing field {@code Car.features}.
     */
    // Note: For best performance:
    // - if the list cannot contain null elements change true to false in the following constructor, or
    // - if the list cannot contain null elements AND the field itself cannot be null, replace this
    //   MultiValueNullableAttribute with a MultiValueAttribute (and change getNullableValues() to getValues())
    public static final Attribute<Car, String> FEATURES = new MultiValueNullableAttribute<Car, String>("FEATURES", true) {
        public Iterable<String> getNullableValues(Car car, QueryOptions queryOptions) { return car.features; }
    };
}
```
