package com.googlecode.cqengine.persistence.offheap;

import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

/**
 * @author niall.gallagher
 */
public class OffHeapPersistenceTest {

    @Test
    public void testConstructor() {
        new OffHeapPersistence<Car, Integer>(Car.CAR_ID);
    }
}