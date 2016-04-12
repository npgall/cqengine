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
package com.googlecode.cqengine.persistence;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.disk.DiskIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.offheap.OffHeapIndex;
import com.googlecode.cqengine.index.sqlite.SQLitePersistence;
import com.googlecode.cqengine.index.sqlite.support.SQLiteIndexFlags;
import com.googlecode.cqengine.persistence.disk.DiskPersistence;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.testutil.Car;
import com.googlecode.cqengine.testutil.CarFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import static com.googlecode.cqengine.query.QueryFactory.enableFlags;

/**
 * @author niall.gallagher
 */
public class PersistenceIndexingBenchmark {

    enum IndexType { ON_HEAP, OFF_HEAP, DISK }

    static final IndexType INDEX_TYPE_TO_TEST = IndexType.DISK;
    static final Long BYTES_TO_PREALLOCATE = null; //= 42693632L;
    static final boolean BULK_IMPORT_FLAG = true;
    static final int[] NUM_OBJECTS = {1000, 100000, 1000000};
    static final int[] NUM_INDEXES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    static final int BATCH_SIZE = 50000;
    static final int NUM_RUNS = 2;

    static class IndexedAttribute extends SimpleAttribute<Car, String> {
        public IndexedAttribute(String attributeName) {
            super(attributeName);
        }
        @Override
        public String getValue(Car object, QueryOptions queryOptions) {
            return object.getModel();
        }
    }

    static final SimpleAttribute<Car, String> CAR_ID_STRING = new SimpleAttribute<Car, String>("carIdString") {
        @Override
        public String getValue(Car car, QueryOptions queryOptions) {
            return String.valueOf(car.getCarId());
        }
    };

    public static void main(String[] args) throws IOException {
        for (int runNumber = 1; runNumber <= NUM_RUNS; runNumber++) {
            long runStartTime = System.currentTimeMillis();
            System.out.println("Run " + runNumber);
            System.out.println("Objects\tIndexes\tSizeMB\tCreateMs\tInserts/sec\tDelta");
            for (int numObjects : NUM_OBJECTS) {
                System.gc();
                double previousInsertsPerSecond = Double.NaN;
                for (int numIndexes : NUM_INDEXES) {
                    IndexedCollection<Car> collection;
                    SQLitePersistence<Car, String> persistence;
                    switch (INDEX_TYPE_TO_TEST) {
                        case ON_HEAP: {
                            collection = new ConcurrentIndexedCollection<Car>();
                            addNavigableIndexes(collection, numIndexes);
                            persistence = null;
                            break;
                        }
                        case OFF_HEAP: {
                            OffHeapPersistence<Car, String> offHeapPersistence = OffHeapPersistence.onPrimaryKey(CAR_ID_STRING);
                            collection = new ConcurrentIndexedCollection<Car>(offHeapPersistence);
                            addOffHeapIndexes(offHeapPersistence, collection, numIndexes);
                            persistence = offHeapPersistence;
                            break;
                        }
                        case DISK: {
//                            File tempFile = File.createTempFile("cqengine_", ".db", new File("/Volumes/SATA_500GB"));
//                            DiskPersistence<Car, String> persistence = DiskPersistence.onPrimaryKeyInFile(new CarIdStringAttribute(), tempFile);
                            DiskPersistence<Car, String> diskPersistence = DiskPersistence.onPrimaryKey(CAR_ID_STRING);
                            collection = new ConcurrentIndexedCollection<Car>(diskPersistence);
                            addDiskIndexes(diskPersistence, collection, numIndexes);
                            persistence = diskPersistence;
                            break;
                        }
                        default: throw new IllegalStateException(String.valueOf(INDEX_TYPE_TO_TEST));
                    }
                    if (persistence != null && BYTES_TO_PREALLOCATE != null) {
                        persistence.expand(BYTES_TO_PREALLOCATE);
                    }
                    long start = System.nanoTime();
                    populateCollection(collection, numObjects, BATCH_SIZE);
                    double timeTakenMs = (System.nanoTime() - start) / 1000000.0;
                    System.gc();
                    Runtime runtime = Runtime.getRuntime();
                    final double megaBytesUsed;
                    if (persistence == null) {
                        double heapSizeInMegaBytes = megaBytes(runtime.totalMemory());
                        megaBytesUsed = heapSizeInMegaBytes - megaBytes(runtime.freeMemory());
                    }
                    else {
                        megaBytesUsed = megaBytes(persistence.getBytesUsed());
                    }

                    double insertsPerSecond = (1000 * numObjects) / timeTakenMs;
                    double costOfAdditionalIndex = (insertsPerSecond - previousInsertsPerSecond) / previousInsertsPerSecond;
                    previousInsertsPerSecond = insertsPerSecond;
                    System.out.println(numObjects + "\t" + numIndexes + "\t" + megaBytesUsed + "\t" + timeTakenMs + "\t" + insertsPerSecond + "\t" + costOfAdditionalIndex);
                }
            }
            System.out.println("Run finished in: " + ((System.currentTimeMillis() - runStartTime) / 1000) + " secs");
            System.out.println();
        }

    }

    static void addOffHeapIndexes(OffHeapPersistence<Car, String> persistence, IndexedCollection<Car> collection, int numIndexesToAdd) {
        for (int i = 0; i < numIndexesToAdd; i++) {
            collection.addIndex(OffHeapIndex.onAttribute(new IndexedAttribute("attribute_" + (i + 1))));
        }
    }

    static void addDiskIndexes(DiskPersistence<Car, String> persistence, IndexedCollection<Car> collection, int numIndexesToAdd) {
        for (int i = 0; i < numIndexesToAdd; i++) {
            collection.addIndex(DiskIndex.onAttribute(new IndexedAttribute("attribute_" + (i + 1))));
        }
    }

    static void addNavigableIndexes(IndexedCollection<Car> collection, int numIndexesToAdd) {
        for (int i = 0; i < numIndexesToAdd; i++) {
            collection.addIndex(NavigableIndex.onAttribute(new IndexedAttribute("attribute_" + (i + 1))));
        }
    }

    static void populateCollection(IndexedCollection<Car> collection, int total, int batchSize) {
        int count = 0;
        long start = System.currentTimeMillis();
        Queue<Car> batch = new ArrayBlockingQueue<Car>(batchSize);
        for (Car next : CarFactory.createIterableOfCars(total)) {
            if (!batch.offer(next)) {
                if (BULK_IMPORT_FLAG) {
                    collection.update(Collections.<Car>emptySet(), batch, QueryFactory.queryOptions(enableFlags(SQLiteIndexFlags.BULK_IMPORT)));
                }
                else {
                    collection.update(Collections.<Car>emptySet(), batch);
                }
                count += batchSize;
                batch.clear();
                batch.add(next);
//                printProgress(start, count, total);
            }
        }
        count += batch.size();
        collection.addAll(batch);
//        printProgress(start, count, total);
//        System.out.println();
    }

    static void printProgress(long startTime, int objectsProcessed, int totalObjects) {
        double elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0;
        double objectsPerSecond = (((double) objectsProcessed) / elapsedSeconds);
        double percentComplete = 100 * (((double) objectsProcessed) / totalObjects);
        double etaSeconds = ((totalObjects - objectsProcessed) / objectsPerSecond);
        System.out.printf("\rObjects processed: %8d, rate: %8.2f objects/sec, progress: %5.2f%%, elapsed: %4.2f secs, remaining: %4.2f secs", objectsProcessed, objectsPerSecond, percentComplete, elapsedSeconds, etaSeconds);
    }

    static void printHeapUsage() {
        Runtime runtime = Runtime.getRuntime();
        double heapSizeInMegaBytes = megaBytes(runtime.totalMemory());
        double heapUsedInMegaBytes = heapSizeInMegaBytes - megaBytes(runtime.freeMemory());
        System.out.printf("Heap size: %6.2f MB, heap used: %6.2f MB%n", heapSizeInMegaBytes, heapUsedInMegaBytes);
    }

    static double megaBytes(long bytes) {
        return ((double)bytes) / 1024 / 1024;
    }
}
