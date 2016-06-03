package com.googlecode.cqengine.performance;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.entity.MapEntity;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by kimptonc on 02/06/2016.
 */
public class PerformanceTest {

    private static final int TEST_COLLECTION_SIZE = 10000;
    private static final int WARMUP_SECONDS = 120;
    private static final String SELECTED_MANUFACTURER = "Toyota";
    private static final String SELECTED_MODEL = "A";
    private boolean loggingEnabled = false;

    @Test
    public void testIndexedCollectionVsHashMapOnKey()
    {
        final Map<Integer, Car> map = new HashMap<Integer, Car>();
        final IndexedCollection<Car> ic = new ConcurrentIndexedCollection<Car>();
        ic.addIndex(UniqueIndex.onAttribute(Car.CAR_ID));
        for (int i = 0; i<TEST_COLLECTION_SIZE; i++)
        {
            Car car = randomCar(i);
            map.put(car.getCarId(), car);
            ic.add(car);
        }

        FunctionHelper<Integer,Integer> mapFunctionHelper = new FunctionHelper<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                Car car = map.get(param);
                return car.getCarId();
            }
        };

        FunctionHelper<Integer,Integer> icFunctionHelper = new FunctionHelper<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                Car car = ic.retrieve(equal(Car.CAR_ID, param)).uniqueResult();
                return car.getCarId();
            }
        };

        // TODO - this is very slow, is it really to be expected? Or is there a better query for this?
        runPerformanceTest(mapFunctionHelper, icFunctionHelper, 15, TEST_COLLECTION_SIZE);
    }

    @Test
    public void testIndexedCollectionVsHashMapNotOnKey()
    {
        final Map<Integer, Car> map = new HashMap<Integer, Car>();
        final IndexedCollection<Car> ic = new ConcurrentIndexedCollection<Car>();
        ic.addIndex(UniqueIndex.onAttribute(Car.CAR_ID));
        ic.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
        for (int i = 0; i<TEST_COLLECTION_SIZE/10; i++)
        {
            Car car = randomCar(i);
            map.put(car.getCarId(), car);
            ic.add(car);
        }

        FunctionHelper<Integer,Integer> mapFunctionHelper = new FunctionHelper<Integer,Integer>() {
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

        FunctionHelper<Integer,Integer> icFunctionHelper = new FunctionHelper<Integer,Integer>() {
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

        runPerformanceTest(mapFunctionHelper, icFunctionHelper, 0.2, TEST_COLLECTION_SIZE);
    }

    @Test
    public void testPojoVsMapOnUniqueKey()
    {
        final IndexedCollection<Car> icCar = new ConcurrentIndexedCollection<Car>();
        icCar.addIndex(UniqueIndex.onAttribute(Car.CAR_ID));

        final IndexedCollection<Map> icMap = new ConcurrentIndexedCollection<Map>();
        final Attribute<Map, Integer> idAttribute = mapAttribute(Car.CAR_ID.getAttributeName(), Integer.class);
        icMap.addIndex(UniqueIndex.onAttribute(idAttribute));

        for (int i = 0; i<TEST_COLLECTION_SIZE; i++)
        {
            Car car = randomCar(i);
            icCar.add(car);
            icMap.add(carToMap(car));
        }

        FunctionHelper<Integer,Integer> icMapFunctionHelper = new FunctionHelper<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                Map car = icMap.retrieve(equal(idAttribute, param % TEST_COLLECTION_SIZE)).uniqueResult();
                return (Integer) car.get(idAttribute.getAttributeName());
            }
        };

        FunctionHelper<Integer,Integer> icCarFunctionHelper = new FunctionHelper<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                Car car = icCar.retrieve(equal(Car.CAR_ID, param % TEST_COLLECTION_SIZE)).uniqueResult();
                return car.getCarId();
            }
        };

        runPerformanceTest(icCarFunctionHelper, icMapFunctionHelper, 1.3, TEST_COLLECTION_SIZE*2);

    }

    @Test
    public void testPojoVsMapOnNonUniqueKey()
    {
        final IndexedCollection<Car> icCar = new ConcurrentIndexedCollection<Car>();
        icCar.addIndex(HashIndex.onAttribute(Car.CAR_ID));
        icCar.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));

        final IndexedCollection<Map> icMap = new ConcurrentIndexedCollection<Map>();
        final Attribute<Map, Integer> idAttribute = mapAttribute(Car.CAR_ID.getAttributeName(), Integer.class);
        final Attribute<Map, String> manufacturerAttribute = mapAttribute(Car.MANUFACTURER.getAttributeName(), String.class);
        icMap.addIndex(HashIndex.onAttribute(idAttribute));
        icMap.addIndex(HashIndex.onAttribute(manufacturerAttribute));

        for (int i = 0; i<TEST_COLLECTION_SIZE; i++)
        {
            Car car = randomCar(i);
            icCar.add(car);
            icMap.add(carToMap(car));
        }

        FunctionHelper<Integer,Integer> icMapFunctionHelper = new FunctionHelper<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                ResultSet<Map> cars = icMap.retrieve(equal(manufacturerAttribute, SELECTED_MANUFACTURER));
                int dummyCount = 0;
                for (Map car : cars) {
                    dummyCount += (Integer) car.get(idAttribute.getAttributeName());
                }
                return dummyCount;
            }
        };

        FunctionHelper<Integer,Integer> icCarFunctionHelper = new FunctionHelper<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                ResultSet<Car> cars = icCar.retrieve(equal(Car.MANUFACTURER, SELECTED_MANUFACTURER));
                int dummyCount = 0;
                for (Car car : cars) {
                    dummyCount += car.getCarId();
                }
                return dummyCount;
            }
        };

        runPerformanceTest(icCarFunctionHelper, icMapFunctionHelper, 5, TEST_COLLECTION_SIZE/10);

    }

    @Test
    public void testPojoVsMapEntity()
    {
        final IndexedCollection<Car> icCar = new ConcurrentIndexedCollection<Car>();
        icCar.addIndex(HashIndex.onAttribute(Car.CAR_ID));
        icCar.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
        icCar.addIndex(HashIndex.onAttribute(Car.MODEL));

        final IndexedCollection<MapEntity> icMap = new ConcurrentIndexedCollection<MapEntity>();
        final Attribute<MapEntity, Integer> idAttribute = mapEntityAttribute(Car.CAR_ID.getAttributeName(), Integer.class);
        final Attribute<MapEntity, String> manufacturerAttribute = mapEntityAttribute(Car.MANUFACTURER.getAttributeName(), String.class);
        final Attribute<MapEntity, String> modelAttribute = mapEntityAttribute(Car.MODEL.getAttributeName(), String.class);
        icMap.addIndex(HashIndex.onAttribute(idAttribute));
        icMap.addIndex(HashIndex.onAttribute(manufacturerAttribute));
        icMap.addIndex(HashIndex.onAttribute(modelAttribute));

        for (int i = 0; i<TEST_COLLECTION_SIZE; i++)
        {
            Car car = randomCar(i);
            icCar.add(car);
            icMap.add(mapEntity(carToMap(car)));
        }

        FunctionHelper<Integer,Integer> icMapFunctionHelper = new FunctionHelper<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                ResultSet<MapEntity> cars = icMap.retrieve(and(equal(manufacturerAttribute, SELECTED_MANUFACTURER),equal(modelAttribute, SELECTED_MODEL)));
                int dummyCount = 0;
                for (MapEntity car : cars) {
                    dummyCount += (Integer) car.get(idAttribute.getAttributeName());
                }
                return dummyCount;
            }
        };

        FunctionHelper<Integer,Integer> icCarFunctionHelper = new FunctionHelper<Integer,Integer>() {
            @Override
            public Integer run(Integer param) {
                ResultSet<Car> cars = icCar.retrieve(and(equal(Car.MANUFACTURER, SELECTED_MANUFACTURER), equal(Car.MODEL, SELECTED_MODEL)));
                int dummyCount = 0;
                for (Car car : cars) {
                    dummyCount += car.getCarId();
                }
                return dummyCount;
            }
        };

        runPerformanceTest(icCarFunctionHelper, icMapFunctionHelper, 4, TEST_COLLECTION_SIZE/20);

    }

    @Test
    public void testPojoVsKeyedMapEntity()
    {
        assertTrue("TODO", 1>2);

    }

    @Test
    public void testQueryUsingAnd()
    {
        assertTrue("TODO", 1>2);

    }

    @Test
    public void testQueryUsingOr()
    {
        assertTrue("TODO", 1>2);

    }

    private void runPerformanceTest(FunctionHelper<Integer, Integer> baseFunctionHelper, FunctionHelper<Integer, Integer> otherFunctionHelper, double worseCaseFactor, int testsToRun) {
        double elapsedFor1 = -1;
        double elapsedFor2 = -999;
        double actualPerformanceFactor = elapsedFor2 / elapsedFor1;
        long warmupStartTime = System.currentTimeMillis();
        int warmupLoops = 0;
        while ((System.currentTimeMillis() - warmupStartTime) < 1000*WARMUP_SECONDS)
        {
            runTests(baseFunctionHelper, testsToRun, 1);
            runTests(otherFunctionHelper, testsToRun, 2);
            warmupLoops++;
        }

        loggingEnabled = true;
        elapsedFor1 = runTests(baseFunctionHelper, testsToRun, 1);
        elapsedFor2 = runTests(otherFunctionHelper, testsToRun, 2);

        actualPerformanceFactor = elapsedFor2 / elapsedFor1;
        log("Timing ratio:"+ actualPerformanceFactor+" vs worst case allowed:"+worseCaseFactor+". Warmup Loops:"+warmupLoops);

        assertTrue("Should be no worse than a factor of "+ worseCaseFactor +" times different (actual "+actualPerformanceFactor+")", worseCaseFactor > actualPerformanceFactor);
    }

    private long runTests(FunctionHelper<Integer, Integer> mapFunctionHelper, int testsToRun, int testCase) {
        long elapsedForMap;
        long startTime = System.nanoTime();
        int dummyCount = 0;
        for (int i = 0; i< testsToRun; i++)
        {
            dummyCount += mapFunctionHelper.run(i);
        }
        elapsedForMap = System.nanoTime() - startTime;
        log(testCase+ ") elapsed = " + elapsedForMap + ", dummy count:" + dummyCount);
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

    private Map carToMap(Car car) {
        Map carMap = new HashMap();
        putAttribute(car, carMap, Car.CAR_ID);
        putAttribute(car, carMap, Car.COLOR);
        putAttribute(car, carMap, Car.MANUFACTURER);
        putAttribute(car, carMap, Car.MODEL);
        putAttribute(car, carMap, Car.DOORS);
        putAttribute(car, carMap, Car.FEATURES);
        putAttribute(car, carMap, Car.PRICE);
        return carMap;
    }

    private void putAttribute(Car car, Map carMap, SimpleAttribute<Car, ?> attribute) {
        carMap.put(attribute.getAttributeName(), attribute.getValue(car, noQueryOptions()));
    }

    private void putAttribute(Car car, Map carMap, MultiValueAttribute<Car, ?> attribute) {
        carMap.put(attribute.getAttributeName(), attribute.getValues(car, noQueryOptions()));
    }

    private Object getRandom(Object[] things) {
        return things[(int) (Math.random() * things.length)];
    }

}
