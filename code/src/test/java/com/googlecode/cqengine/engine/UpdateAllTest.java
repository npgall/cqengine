package com.googlecode.cqengine.engine;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.option.DeduplicationStrategy;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class UpdateAllTest {
  @Test
  public void testUpdateIndexNoOption() {
    IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
    cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
    cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));
    cars.addAll(CarFactory.createCollectionOfCars(100));

    ResultSet<Car> results = cars.retrieve(has(Car.MANUFACTURER),
        queryOptions(
        orderBy(ascending(Car.CAR_ID)))
        );
    boolean carFound = false;
    for (Car car : results) {
      Assert.assertEquals(0, car.getCarId());
      Assert.assertEquals("Ford", car.getManufacturer());
      Assert.assertEquals("Focus", car.getModel());
      carFound = true;
      break;
    }

    assertTrue(carFound);
    Car carUpdated = new Car(0, "Renault", "Megane", Car.Color.RED, 5, 5000.00, null);
    cars.update(Arrays.asList(carUpdated), Arrays.asList(carUpdated));

    results = cars.retrieve(has(Car.MANUFACTURER),
        queryOptions(
        orderBy(ascending(Car.CAR_ID)))
        );
    carFound = false;
    for (Car car : results) {
      Assert.assertEquals(0, car.getCarId());
      Assert.assertEquals("Ford", car.getManufacturer());
      Assert.assertEquals("Focus", car.getModel());
      carFound = true;
      break;
    }
    assertTrue(carFound);
  }

  @Test
  public void testUpdateIndexOption() {
    IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
    cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
    cars.addIndex(NavigableIndex.onAttribute(Car.MANUFACTURER));
    cars.addAll(CarFactory.createCollectionOfCars(100));

    ResultSet<Car> results = cars.retrieve(has(Car.MANUFACTURER),
        queryOptions(
        orderBy(ascending(Car.CAR_ID)))
        );
    boolean carFound = false;
    for (Car car : results) {
      assertEquals(0, car.getCarId());
      assertEquals("Ford", car.getManufacturer());
      assertEquals("Focus", car.getModel());
      carFound = true;
      break;
    }

    assertTrue(carFound);
    Car carUpdated = new Car(0, "Renault", "Megane", Car.Color.RED, 5, 5000.00, null);
    cars.update(Arrays.asList(carUpdated), Arrays.asList(carUpdated),
        queryOptions(deduplicate(DeduplicationStrategy.MATERIALIZE)));

    results = cars.retrieve(has(Car.MANUFACTURER),
        queryOptions(
        orderBy(ascending(Car.CAR_ID)))
        );
    carFound = false;
    for (Car car : results) {
      assertEquals(0, car.getCarId());
      assertEquals("Renault", car.getManufacturer());
      assertEquals("Megane", car.getModel());
      carFound = true;
      break;
    }
    assertTrue(carFound);
  }
}
