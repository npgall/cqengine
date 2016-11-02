# Java 8 Streams #

Examples of how to create Java 8 Streams from CQEngine [ResultSet](http://htmlpreview.github.io/?http://raw.githubusercontent.com/npgall/cqengine/master/documentation/javadoc/apidocs/com/googlecode/cqengine/resultset/ResultSet.html)s.

* To convert a CQEngine ResultSet to a Java 8 Stream, add import statement to your class: `import static com.googlecode.cqengine.stream.StreamFactory.*`.
* Then, you can convert a ResultSet to a Stream by wrapping it in the `streamOf` method.

## Example ##
Here is a complete example of how to use Java 8 streams to compute the distinct set of Colors of cars which match a CQEngine query.

The resulting Stream will automatically leverage CQEngine indexes to accelerate the query.

```java
package com.googlecode.cqengine.examples.stream;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import java.util.Set;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.stream.StreamFactory.*;

public class StreamExample {

    public static void main(String[] args) {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<>();
        cars.addAll(CarFactory.createCollectionOfCars(10));
        cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));

        Set<Car.Color> distinctColorsOfFordCars =
                streamOf(cars.retrieve(equal(Car.MANUFACTURER, "Ford")))
                .map(Car::getColor)
                .distinct()
                .collect(Collectors.toSet());

        System.out.println(distinctColorsOfFordCars);
        // prints: [GREEN, RED]
    }
}
```
