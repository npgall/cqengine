/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.persistence.disk;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.disk.DiskIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.offheap.OffHeapIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static java.util.stream.Collectors.toSet;

/**
 * @author niall.gallagher
 */
public class DiskPersistenceTest {

    @Test
    public void testGetBytesUsed() {
        DiskPersistence<Car, Integer> persistence = DiskPersistence.onPrimaryKey(Car.CAR_ID);
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);
        cars.addAll(CarFactory.createCollectionOfCars(50));
        long bytesUsed = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used should be greater than zero: " + bytesUsed, bytesUsed > 0);
        Assert.assertTrue("Failed to delete temp file:" + persistence.getFile(), persistence.getFile().delete());
    }

    @Test
    public void testCompact() {
        DiskPersistence<Car, Integer> persistence = DiskPersistence.onPrimaryKey(Car.CAR_ID);
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);
        cars.addAll(CarFactory.createCollectionOfCars(100));
        long bytesUsedWhenFullyPopulated = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used when fully populated should be greater than zero: " + bytesUsedWhenFullyPopulated, bytesUsedWhenFullyPopulated > 0);
        cars.removeAll(CarFactory.createCollectionOfCars(100));
        long bytesUsedWhenObjectsRemoved = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used when objects removed (" + bytesUsedWhenObjectsRemoved + ") should remain the same as when fully populated (" + bytesUsedWhenFullyPopulated + ")", bytesUsedWhenObjectsRemoved == bytesUsedWhenFullyPopulated);
        persistence.compact(); // Truncates size of the database, but not to zero as the tables which were created remain (although empty)
        long bytesUsedAfterCompaction = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used after compaction (" + bytesUsedAfterCompaction + ") should be less than when fully populated (" + bytesUsedWhenFullyPopulated + ")", bytesUsedAfterCompaction < bytesUsedWhenFullyPopulated);
        Assert.assertTrue("Failed to delete temp file:" + persistence.getFile(), persistence.getFile().delete());
    }

    @Test
    public void testExpand() {
        final long bytesToExpand = 102400;  // Expand by 100KB;
        DiskPersistence<Car, Integer> persistence = DiskPersistence.onPrimaryKey(Car.CAR_ID);
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);
        cars.addAll(CarFactory.createCollectionOfCars(50));
        persistence.compact();
        long initialBytesUsed = persistence.getBytesUsed();
        Assert.assertTrue("Initial bytes used should be greater than zero: " + initialBytesUsed, initialBytesUsed > 0);
        persistence.expand(bytesToExpand);
        long bytesUsedAfterExpanding = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used after expanding (" + bytesUsedAfterExpanding + ") should have been increased by at least bytes to expand (" + bytesToExpand + ") above initial bytes used (" + initialBytesUsed + ")", bytesUsedAfterExpanding >= (initialBytesUsed + bytesToExpand));
        persistence.compact();
        long bytesUsedAfterCompaction = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used after compaction (" + bytesUsedAfterCompaction + ") should be equal to initial bytes used (" + initialBytesUsed + ")", bytesUsedAfterCompaction == initialBytesUsed);
        Assert.assertTrue("Failed to delete temp file:" + persistence.getFile(), persistence.getFile().delete());
    }

    @Test
    public void testSupportsIndex() {
        DiskPersistence<Car, Integer> persistence = DiskPersistence.onPrimaryKey(Car.CAR_ID);

        Index<Car> diskIndex = DiskIndex.onAttribute(Car.MANUFACTURER);
        Index<Car> offHeapIndex = OffHeapIndex.onAttribute(Car.MANUFACTURER);
        Index<Car> navigableIndex = NavigableIndex.onAttribute(Car.MANUFACTURER);

        Assert.assertTrue(persistence.supportsIndex(diskIndex));
        Assert.assertFalse(persistence.supportsIndex(offHeapIndex));
        Assert.assertFalse(persistence.supportsIndex(navigableIndex));
    }

    @Test
    public void testEqualsAndHashCode() {
        SQLiteDataSource ds1 = new SQLiteDataSource(new SQLiteConfig());
        ds1.setUrl("foo");
        SQLiteDataSource ds2 = new SQLiteDataSource(new SQLiteConfig());
        ds2.setUrl("bar");
        EqualsVerifier.forClass(DiskPersistence.class)
                .withIgnoredFields("sqLiteDataSource", "persistentConnection", "closed", "useReadWriteLock", "readWriteLock")
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE)
                .withPrefabValues(SQLiteDataSource.class, ds1, ds2)
                .verify();
    }

    @Test
    public void testEndToEndDiskPersistence() {
        Set<Integer> expectedCarIds = new HashSet<Integer>();
        Set<Integer> actualCarIds = new HashSet<Integer>();
        File persistenceFile;
        {
            // Create a collection of 50 cars persisted to disk...
            DiskPersistence<Car, Integer> persistence = DiskPersistence.onPrimaryKey(Car.CAR_ID);
            persistenceFile = persistence.getFile();
            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
            IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);
            cars.addIndex(DiskIndex.onAttribute(Car.MANUFACTURER));
            cars.addAll(CarFactory.createCollectionOfCars(50));

            // Record the carIds of all "Ford" cars...
            try (ResultSet<Car> blueCars = cars.retrieve(equal(Car.MANUFACTURER, "Ford"))) {
                for (Car car : blueCars) {
                    expectedCarIds.add(car.getCarId());
                }
                // At this point we discard collection but we leave the persistence file on disk.
            }
        }
        {
            // Create a new collection, whose data is retrieved from the persistence file created earlier...
            DiskPersistence<Car, Integer> persistence = DiskPersistence.onPrimaryKeyInFile(Car.CAR_ID, persistenceFile);
            IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);
            cars.addIndex(DiskIndex.onAttribute(Car.MANUFACTURER));

            // Record the carIds of all "Ford" cars, as loaded from the persistence file...
            try (ResultSet<Car> blueCars = cars.retrieve(equal(Car.MANUFACTURER, "Ford"))) {
                for (Car car : blueCars) {
                    actualCarIds.add(car.getCarId());
                }
            }
        }
        // Assert we got the same results both times...
        Assert.assertEquals(expectedCarIds, actualCarIds);
        Assert.assertTrue("Failed to delete temp file:" + persistenceFile, persistenceFile.delete());
    }


    // ================================================================================================
    // === Manual tests, used to verify disk persistence compatibility between CQEngine versions... ===
    // ================================================================================================

    @Test @Ignore
    public void testSaveToDisk() {
        Set<Car> collectionOfCars = CarFactory.createCollectionOfCars(50);
        Set<Integer> expectedCarIds = collectionOfCars.stream().map(Car::getCarId).collect(toSet());

        File persistenceFile = new File("cqengine-persisted.dat");
        System.out.println("Persistence file: " + persistenceFile.getAbsolutePath());

        // Create a collection (it will initially be empty if the file does not exist)...
        DiskPersistence<Car, Integer> persistence = DiskPersistence.onPrimaryKeyInFile(Car.CAR_ID, persistenceFile);
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);

        // Populate the collection (saving it to disk)...
        cars.addAll(collectionOfCars);

        // Sanity check that we saved the cars correctly...
        Set<Integer> actualCarIds = cars.stream().map(Car::getCarId).collect(toSet());

        Assert.assertEquals(expectedCarIds, actualCarIds);
        System.out.println("Saved to disk: " + actualCarIds);
    }

    @Test @Ignore
    public void testReadFromDisk() {
        Set<Car> collectionOfCars = CarFactory.createCollectionOfCars(50);
        Set<Integer> expectedCarIds = collectionOfCars.stream().map(Car::getCarId).collect(toSet());

        File persistenceFile = new File("cqengine-persisted.dat");
        System.out.println("Persistence file: " + persistenceFile.getAbsolutePath());

        // Create a collection (it will read from the pre-existing file on disk)...
        DiskPersistence<Car, Integer> persistence = DiskPersistence.onPrimaryKeyInFile(Car.CAR_ID, persistenceFile);
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);

        // Retrieve the cars from disk...
        Set<Integer> actualCarIds = cars.stream().map(Car::getCarId).collect(toSet());

        Assert.assertEquals(expectedCarIds, actualCarIds);
        System.out.println("Loaded from disk: " + actualCarIds);
    }
}