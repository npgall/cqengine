package com.googlecode.cqengine.performance;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static org.junit.Assert.assertTrue;

/**
 * Created by kimptonc on 02/06/2016.
 */
public class PerformanceTest {

    private static final int TEST_COLLECTION_SIZE = 10000;
    private static final int WARMUP_LOOPS = 1500;
    public static final String SELECTED_MANUFACTURER = "Toyota";
    private boolean loggingEnabled = false;

    @Test
    public void testIndexedCollectionVsHashMapOnKey()
    {
        final Map<Integer, Car> map = new HashMap<Integer, Car>();
        final IndexedCollection<Car> ic = new ConcurrentIndexedCollection<Car>();
        ic.addIndex(HashIndex.onAttribute(Car.CAR_ID));
        for (int i = 0; i<TEST_COLLECTION_SIZE; i++)
        {
            Car car = randomCar(i);
            map.put(car.getCarId(), car);
            ic.add(car);
        }

        Worker<Integer,Integer> mapWorker = new Worker<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                Car car = map.get(param);
                return car.getCarId();
            }
        };

        Worker<Integer,Integer> icWorker = new Worker<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                Car car = ic.retrieve(equal(Car.CAR_ID, param)).uniqueResult();
                return car.getCarId();
            }
        };

        runPerformanceTest(mapWorker, icWorker, WARMUP_LOOPS, 30, TEST_COLLECTION_SIZE);
    }

    @Test
    public void testIndexedCollectionVsHashMapNotOnKey()
    {
        final Map<Integer, Car> map = new HashMap<Integer, Car>();
        final IndexedCollection<Car> ic = new ConcurrentIndexedCollection<Car>();
        ic.addIndex(HashIndex.onAttribute(Car.CAR_ID));
        ic.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
        for (int i = 0; i<TEST_COLLECTION_SIZE/10; i++)
        {
            Car car = randomCar(i);
            map.put(car.getCarId(), car);
            ic.add(car);
        }

        Worker<Integer,Integer> mapWorker = new Worker<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                int dummyCount = 0;
                for (Car car : map.values()) {
                    if (car.getManufacturer().equals(SELECTED_MANUFACTURER))
                    {
                        dummyCount += car.getDoors();
                    }
                }
                return dummyCount;
            }
        };

        Worker<Integer,Integer> icWorker = new Worker<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                int dummyCount = 0;
                ResultSet<Car> cars = ic.retrieve(equal(Car.MANUFACTURER, SELECTED_MANUFACTURER));
                for (Car car : cars) {
                    dummyCount += car.getDoors();
                }
                return dummyCount;
            }
        };

        runPerformanceTest(mapWorker, icWorker, WARMUP_LOOPS, 0.3, TEST_COLLECTION_SIZE/10);
    }

    @Test
    public void testPojoVsMap()
    {
        assertTrue("TODO", 1>2);

    }

    @Test
    public void testPojoVsMapEntity()
    {
        assertTrue("TODO", 1>2);

    }

    @Test
    public void testPojoVsKeyedMapEntity()
    {
        assertTrue("TODO", 1>2);

    }

    private void runPerformanceTest(Worker<Integer, Integer> mapWorker, Worker<Integer, Integer> icWorker, int warmupLoops, double worseCaseFactor, int testsToRun) {
        double elapsedForIC = -999;
        double elapsedForMap = -1;
        double actualPerformanceFactor = elapsedForIC / elapsedForMap;
        for (int j = 0; j<= warmupLoops; j++) {
            if (j == warmupLoops) loggingEnabled = true;
            elapsedForMap = runTests(mapWorker, testsToRun);

            elapsedForIC = runTests(icWorker, testsToRun);

            actualPerformanceFactor = elapsedForIC / elapsedForMap;
            log("Map vs IndexedCollection timing ratio:"+ actualPerformanceFactor);
        }

        assertTrue("IC is no worse than "+ worseCaseFactor +" times slower than Map (actual "+actualPerformanceFactor+")", worseCaseFactor > actualPerformanceFactor);
    }

    private long runTests(Worker<Integer, Integer> mapWorker, int testsToRun) {
        long elapsedForMap;
        long startTime = System.nanoTime();
        int dummyCount = 0;
        for (int i = 0; i< testsToRun; i++)
        {
            dummyCount += mapWorker.run(i);
        }
        elapsedForMap = System.nanoTime() - startTime;
        log("elapsed = " + elapsedForMap + ", dummy count:" + dummyCount);
        return elapsedForMap;
    }

    private void log(String s) {
        if (loggingEnabled) {
            System.out.println(s);
        }
    }

    private static Car.Color[] COLOURS =
            {Car.Color.BLACK, Car.Color.RED, Car.Color.BLUE, Car.Color.GREEN,Car.Color.WHITE};
    private static String[] MAKES = {SELECTED_MANUFACTURER, "Ford", "Honda", "Audi","Chrysler","Jaguar","Porsche","Merecedes","BMW"};
    private static String[] MODELS = {"A", "B", "C", "D","E","F","G","H","I"};

    private Car randomCar(int i) {
        return new Car(i, (String)getRandom(MAKES), (String)getRandom(MODELS), (Car.Color)getRandom(COLOURS), (int) (2+Math.random()*4), 5000+10000*Math.random(), new ArrayList<String>());
    }

    private Object getRandom(Object[] things) {
        return things[(int) (Math.random() * things.length)];
    }

}
