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
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.googlecode.cqengine.query.QueryFactory.between;

/**
 * A test of concurrent reads and writes to a collection using {@link DiskPersistence}
 * with journam modes WAL and DELETE.
 */
public class DiskPersistenceConcurrencyTest {

    @Test
    public void testDiskPersistenceConcurrency_JournalModeWal() {
        File tempFile = DiskPersistence.createTempFile();
        final IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>(
                DiskPersistence.onPrimaryKeyInFile(Car.CAR_ID, tempFile)
        );
        collection.addAll(CarFactory.createCollectionOfCars(100));

        final List<String> sequenceLog = Collections.synchronizedList(new ArrayList<String>());
        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(new ReadingTask("ReadingTask 1", collection, 1500, sequenceLog)); // start immediately, pause for 1 second, then resume to completion
        sleep(500);
        executorService.submit(new ReadingTask("ReadingTask 2", collection, 1500, sequenceLog)); // start immediately, pause for 1 second, then resume to completion
        sleep(500);
        executorService.submit(new WritingTask("WritingTask 1", collection, sequenceLog)); // start this task after waiting 500ms

        executorService.shutdown();
        awaitTermination(executorService);

        List<String> expected = Arrays.asList(
                "ReadingTask 1 started and about to access collection",
                "ReadingTask 1 pausing mid-read", // Successfully acquired the first read lock
                "ReadingTask 2 started and about to access collection",
                "ReadingTask 2 pausing mid-read", // Successfully acquired a second read lock concurrently
                "WritingTask 1 started and about to access collection", // Should not block because WAL allows concurrent reads and writes
                "WritingTask 1 finished removing 1 item", // Successfully acquired write lock even though reads are ongoing
                "ReadingTask 1 resuming read",
                "ReadingTask 1 finished reading 20 items",
                "ReadingTask 2 resuming read",
                "ReadingTask 2 finished reading 20 items"

        );
        Assert.assertEquals(expected, sequenceLog);
        tempFile.delete();
    }

    @Test
    public void testDiskPersistenceConcurrency_JournalModeDelete() {
        File tempFile = DiskPersistence.createTempFile();
        final IndexedCollection<Car> collection = new ConcurrentIndexedCollection<Car>(
                DiskPersistence.onPrimaryKeyInFileWithProperties(
                    Car.CAR_ID,
                    tempFile,
                    new Properties() {{
                        setProperty("journal_mode", "DELETE");
                    }}
                )
        );
        collection.addAll(CarFactory.createCollectionOfCars(100));

        final List<String> sequenceLog = Collections.synchronizedList(new ArrayList<String>());
        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(new ReadingTask("ReadingTask 1", collection, 1500, sequenceLog)); // start immediately, pause for 1 second, then resume to completion
        sleep(500);
        executorService.submit(new ReadingTask("ReadingTask 2", collection, 1500, sequenceLog)); // start immediately, pause for 1 second, then resume to completion
        sleep(500);
        executorService.submit(new WritingTask("WritingTask 1", collection, sequenceLog)); // start this task after waiting 500ms

        executorService.shutdown();
        awaitTermination(executorService);

        List<String> expected = Arrays.asList(
                "ReadingTask 1 started and about to access collection",
                "ReadingTask 1 pausing mid-read", // Successfully acquired the first read lock
                "ReadingTask 2 started and about to access collection",
                "ReadingTask 2 pausing mid-read", // Successfully acquired a second read lock concurrently
                "WritingTask 1 started and about to access collection", // Should block until both reads finish
                "ReadingTask 1 resuming read",
                "ReadingTask 1 finished reading 20 items",
                "ReadingTask 2 resuming read",
                "ReadingTask 2 finished reading 20 items",
                "WritingTask 1 finished removing 1 item" // Finally was granted write lock

        );
        Assert.assertEquals(expected, sequenceLog);
        tempFile.delete();
    }

    static class ReadingTask implements Runnable {

        final String taskName;
        final IndexedCollection<Car> collection;
        final long millisecondsToPauseMidRequest;
        final List<String> sequenceLog;

        public ReadingTask(String taskName, IndexedCollection<Car> collection, long millisecondsToPauseMidRequest, List<String> sequenceLog) {
            this.taskName = taskName;
            this.collection = collection;
            this.millisecondsToPauseMidRequest = millisecondsToPauseMidRequest;
            this.sequenceLog = sequenceLog;
        }

        @Override
        public void run() {
            sequenceLog.add(taskName + " started and about to access collection");
            ResultSet<Car> backgroundResults = collection.retrieve(between(Car.CAR_ID, 40, 59));
            Iterator<Car> iterator = backgroundResults.iterator();
            int count = 0;
            for (; iterator.hasNext() && count < 5; count++) {
                iterator.next();
            }
            sequenceLog.add(taskName + " pausing mid-read");
            sleep(millisecondsToPauseMidRequest);
            sequenceLog.add(taskName + " resuming read");
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
            backgroundResults.close();
            sequenceLog.add(taskName + " finished reading " + count + " items");
        }
    }

    static class WritingTask implements Runnable {
        final String taskName;
        final IndexedCollection<Car> indexedCollection;
        final List<String> sequenceLog;

        public WritingTask(String taskName, IndexedCollection<Car> indexedCollection, List<String> sequenceLog) {
            this.taskName = taskName;
            this.indexedCollection = indexedCollection;
            this.sequenceLog = sequenceLog;
        }

        @Override
        public void run() {
            sequenceLog.add(taskName + " started and about to access collection");
            indexedCollection.remove(CarFactory.createCar(71));
            sequenceLog.add(taskName + " finished removing 1 item");
        }
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    static void awaitTermination(ExecutorService executorService) {
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
