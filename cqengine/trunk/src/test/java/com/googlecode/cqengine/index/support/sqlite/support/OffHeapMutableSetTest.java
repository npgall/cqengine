package com.googlecode.cqengine.index.support.sqlite.support;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.googlecode.cqengine.index.support.sqlite.TemporaryDatabase;
import com.googlecode.cqengine.index.support.sqlite.TemporaryDatabase.TemporaryInMemoryDatabase;
import com.googlecode.cqengine.testutil.Car;
import junit.extensions.TestSetup;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;

import static com.googlecode.cqengine.testutil.CarFactory.createCar;

public class OffHeapMutableSetTest extends TestCase {

    static final Collection<TemporaryInMemoryDatabase> databasesToClose = new ArrayList<TemporaryInMemoryDatabase>();


    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(SetTestSuiteBuilder.using(datasetGenerator())
                .withFeatures(CollectionSize.ANY, CollectionFeature.SUPPORTS_ADD, CollectionFeature.SUPPORTS_REMOVE, CollectionFeature.RESTRICTS_ELEMENTS)
                .named("OffHeapMutableSetAPICompliance")
                .createTestSuite());
//        suite.addTestSuite(OffHeapMutableSetTest.class);
        return new TestSetup(suite) {
            @Override
            protected void tearDown() throws Exception {
                for (TemporaryInMemoryDatabase database : databasesToClose) {
                    database.after();
                }
            }
        };
    }

    private static TestSetGenerator<Car> datasetGenerator() {
        return new TestSetGenerator<Car>() {
            @Override
            public SampleElements<Car> samples() {
                return new SampleElements<Car>(createCar(0), createCar(1), createCar(2), createCar(3), createCar(4));
            }

            @Override
            public Set<Car> create(Object... elements) {
                TemporaryInMemoryDatabase database = new TemporaryDatabase.TemporaryInMemoryDatabase();
                database.before();
                databasesToClose.add(database);
                Set<Car> offHeapMutableSet = new OffHeapMutableSet<Car, Integer>(Car.CAR_ID, database.getConnectionManager(true), 0);
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