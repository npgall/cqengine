package com.googlecode.cqengine.persistence.support.sqlite;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;
import com.googlecode.cqengine.testutil.Car;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;

import static com.googlecode.cqengine.testutil.CarFactory.createCar;

public class SQLitePersistentSetTest extends TestCase {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(SetTestSuiteBuilder.using(datasetGenerator())
                .withFeatures(CollectionSize.ANY, CollectionFeature.SUPPORTS_ADD, CollectionFeature.SUPPORTS_REMOVE, CollectionFeature.RESTRICTS_ELEMENTS)
                .named("SQLitePersistentSetCompliance")
                .createTestSuite());
//        suite.addTestSuite(SQLitePersistentSetTest.class);
        return suite;
    }

    private static TestSetGenerator<Car> datasetGenerator() {
        return new TestSetGenerator<Car>() {
            @Override
            public SampleElements<Car> samples() {
                return new SampleElements<Car>(createCar(0), createCar(1), createCar(2), createCar(3), createCar(4));
            }

            @Override
            public Set<Car> create(Object... elements) {
                Set<Car> offHeapMutableSet = new SQLitePersistentSet<Car, Integer>(OffHeapPersistence.onPrimaryKey(Car.CAR_ID));
                for (Object o : elements) {
                    offHeapMutableSet.add((Car)o);
                }
                return offHeapMutableSet;
            }

            @Override
            public Car[] createArray(int length) {
                return new Car[length];
            }

            @Override
            public Iterable<Car> order(List<Car> insertionOrder) {
                return insertionOrder;
            }
        };
    }
}