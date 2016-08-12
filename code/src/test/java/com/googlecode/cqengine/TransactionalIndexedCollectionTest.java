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
package com.googlecode.cqengine;

import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.googlecode.cqengine.persistence.offheap.OffHeapPersistence;
import com.googlecode.cqengine.persistence.onheap.OnHeapPersistence;
import com.googlecode.cqengine.persistence.support.CollectionWrappingObjectStore;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;
import com.googlecode.cqengine.testutil.Car;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

import java.util.*;
import java.util.concurrent.*;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.option.ArgumentValidationStrategy.SKIP;
import static com.googlecode.cqengine.testutil.CarFactory.createCar;
import static java.util.Arrays.asList;

/**
 * Unit tests for {@link TransactionalIndexedCollection}. Note that tests for support behavior (such as query processing)
 * which applies to all implementations of {@link IndexedCollection} can be found in
 * {@link com.googlecode.cqengine.IndexedCollectionFunctionalTest}.
 * <p/>
 * In addition to the unit tests in this class, this class also runs a further several hundred unit tests in
 * <a href="https://code.google.com/p/guava-libraries/source/browse/guava-testlib">guava-testlib</a> on the
 * IndexedCollection to validate its compliance with the API specifications of java.util.Set.
 *
 * @author Niall Gallagher
 */
public class TransactionalIndexedCollectionTest extends TestCase {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(SetTestSuiteBuilder.using(onHeapIndexedCollectionGenerator())
                .withFeatures(CollectionSize.ANY, CollectionFeature.GENERAL_PURPOSE)
                .named("OnHeap_TransactionalIndexedCollectionAPICompliance")
                .createTestSuite());
        suite.addTest(SetTestSuiteBuilder.using(offHeapIndexedCollectionGenerator())
                .withFeatures(CollectionSize.ANY, CollectionFeature.GENERAL_PURPOSE)
                .named("OffHeap_TransactionalIndexedCollectionAPICompliance")
                .createTestSuite());
        suite.addTestSuite(TransactionalIndexedCollectionTest.class);
        return suite;
    }

    private static TestStringSetGenerator onHeapIndexedCollectionGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                IndexedCollection<String> indexedCollection = new TransactionalIndexedCollection<String>(String.class, OnHeapPersistence.onPrimaryKey(QueryFactory.selfAttribute(String.class)));
                indexedCollection.addAll(Arrays.asList(elements));
                return indexedCollection;
            }
        };
    }

    private static TestStringSetGenerator offHeapIndexedCollectionGenerator() {
        return new TestStringSetGenerator() {
            @Override protected Set<String> create(String[] elements) {
                IndexedCollection<String> indexedCollection = new TransactionalIndexedCollection<String>(String.class, OffHeapPersistence.onPrimaryKey(QueryFactory.selfAttribute(String.class)));
                indexedCollection.addAll(Arrays.asList(elements));
                return indexedCollection;
            }
        };
    }

    public void testWritePath() {
        TransactionalIndexedCollection<Car> collection = new TransactionalIndexedCollection<Car>(Car.class);
        // Version number initially starts at 1...
        assertEquals(1, collection.currentVersion.versionNumber);

        // Adding objects to the collection should cause version number to be incremented twice...
        collection.addAll(asSet(createCar(1), createCar(2), createCar(3), createCar(4)));
        assertEquals(4, collection.size());
        assertEquals(asSet(createCar(1), createCar(2), createCar(3), createCar(4)), collection);
        assertEquals(3, collection.currentVersion.versionNumber);

        // Removing objects from the collection should cause version number to be incremented twice...
        collection.removeAll(asSet(createCar(2), createCar(3)));
        assertEquals(2, collection.size());
        assertEquals(asSet(createCar(1), createCar(4)), collection);
        assertEquals(5, collection.currentVersion.versionNumber);

        // Replacing objects in the collection should cause version number to be incremented thrice...
        collection.update(asSet(createCar(4)), asSet(createCar(5), createCar(6)));
        assertEquals(3, collection.size());
        assertEquals(asSet(createCar(1), createCar(5), createCar(6)), collection);
        assertEquals(8, collection.currentVersion.versionNumber);

        // Replacing no objects should not cause version number to change...
        collection.update(Collections.<Car>emptySet(), Collections.<Car>emptySet());
        assertEquals(8, collection.currentVersion.versionNumber);
    }

    public void testReadPath() throws InterruptedException, ExecutionException {
        // Set up the initial collection to contain 2 objects...
        final TransactionalIndexedCollection<Car> collection = new TransactionalIndexedCollection<Car>(Car.class);
        collection.addAll(asSet(createCar(1), createCar(2)));
        assertEquals(2, collection.size());

        // Set up an executor which will execute tasks in separate threads.
        ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        // This thread will subsequently act as a control thread, submitting tasks to background threads and validating the behaviour.

        // == STEP 1: Set up a background thread which will read from the collection... ==

        // Create a semaphore on which the background reading thread will block after it has opened a ResultSet,
        // but before it has actually read any objects from it...
        final Semaphore readBlock = new Semaphore(0);
        // Define a semaphore which allows this control thread to wait for the background read to begin...
        final Semaphore readStartedSignal = new Semaphore(0);

        // Submit a reading task to a background thread...
        Future<Set<Car>> resultsFromFirstRead = executor.submit(new Callable<Set<Car>>() {
            @Override
            public Set<Car> call() throws Exception {
                ResultSet<Car> resultSet = collection.retrieve(all(Car.class));
                // Signal that this reading thread has started...
                readStartedSignal.release();
                // BLOCK the reading thread here...
                readBlock.acquire();
                // At this point thread was unblocked...
                // Now read from this ResultSet...
                Set<Car> materializedObjects = asSet(resultSet);
                // Close the ResultSet, and return the objects which were read...
                resultSet.close();
                return materializedObjects;
            }
        });
        // Block this control thread until the reading thread has actually started...
        readStartedSignal.acquire();
        // ...at this point, a background thread has an open ResultSet,
        // so attempts to modify the collection should block.

        // Try to modify the collection in a different background thread...
        // Define a semaphore which allows this control thread to wait for the write to begin...
        final Semaphore writeStartedSignal = new Semaphore(0);
        // Define a semaphore which allows this control thread to determine that the write has finished...
        final Semaphore writeFinishedSignal = new Semaphore(0);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                // Signal that this writing thread has started...
                writeStartedSignal.release();
                // Remove Car 2, and add Cars 3 & 4...
                collection.update(asSet(createCar(2)), asSet(createCar(3), createCar(4)));
                writeFinishedSignal.release();
            }
        });
        writeStartedSignal.acquire();
        // Make this control thread sleep to allow time for the writing thread to enter update() where it should block...
        Thread.sleep(1000L);
        // Assert that the writing thread is indeed blocked...
        Assert.assertFalse("Writing thread should block while there is an open ResultSet", writeFinishedSignal.tryAcquire());

        // Unblock the reading thread to allow it to read objects from the ResultSet it opened earlier,
        // and then to close its ResultSet...
        readBlock.release();
        // Now validate that the reading thread did not see any of the modifications submitted to the writing thread...
        Assert.assertEquals(asSet(createCar(1), createCar(2)), resultsFromFirstRead.get());

        // Make this control thread sleep to allow time for the writing thread to finish...
        Thread.sleep(1000L);
        // Assert that the writing thread is now unblocked...
        Assert.assertTrue("Writing thread should have completed after the open ResultSet was closed", writeFinishedSignal.tryAcquire());

        // Now validate (in this control thread) that the modifications by the writing thread are visible...
        ResultSet<Car> resultsFromSecondRead = collection.retrieve(all(Car.class));
        Set<Car> materializedResultsFromSecondRead = asSet(resultsFromSecondRead);
        resultsFromSecondRead.close();
        Assert.assertEquals(asSet(createCar(1), createCar(3), createCar(4)), materializedResultsFromSecondRead);
    }

    public void testConstructor() {
        TransactionalIndexedCollection<Car> indexedCollection = new TransactionalIndexedCollection<Car>(
                Car.class,
                new OnHeapPersistence<Car, Integer>()
        );
        assertEquals(indexedCollection.objectType, Car.class);
        assertEquals(1L, indexedCollection.currentVersion.versionNumber);
    }

    public void testArgumentValidation_NotDisjoint() {
        Set<Integer> s1 = asSet(1, 2, 3);
        Set<Integer> s2 = asSet(3, 4, 5); // overlaps
        TransactionalIndexedCollection<Integer> indexedCollection = new TransactionalIndexedCollection<Integer>(Integer.class);
        try {
            indexedCollection.update(s1, s2);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {
            // Ignore
        }
    }

    public void testArgumentValidation_NotDisjoint_ValidationSkipped() {
        Set<Integer> s1 = asSet(1, 2, 3);
        Set<Integer> s2 = asSet(3, 4, 5); // overlaps
        TransactionalIndexedCollection<Integer> indexedCollection = new TransactionalIndexedCollection<Integer>(Integer.class);
        indexedCollection.update(s1, s2, queryOptions(argumentValidation(SKIP)));
    }

    public void testArgumentValidation_Disjoint() {
        Set<Integer> s1 = asSet(1, 2, 3);
        Set<Integer> s2 = asSet(4, 5, 6); // does not overlap
        TransactionalIndexedCollection<Integer> indexedCollection = new TransactionalIndexedCollection<Integer>(Integer.class);
        indexedCollection.update(s1, s2);
    }

    public void testStrictReplacement() {
        {
            // Verify that with STRICT_REPLACEMENT, when some objects to be replaced are not stored, collection is not modified...
            TransactionalIndexedCollection<Integer> indexedCollection = new TransactionalIndexedCollection<Integer>(Integer.class);
            indexedCollection.addAll(asSet(1, 2, 3));
            assertFalse(indexedCollection.update(asSet(3, 4), asSet(5, 6), queryOptions(enableFlags(TransactionalIndexedCollection.STRICT_REPLACEMENT))));
            assertEquals(indexedCollection, asSet(1, 2, 3));
        }
        {
            // Verify that without STRICT_REPLACEMENT, when some objects to be replaced are not stored, collection is modified...
            TransactionalIndexedCollection<Integer> indexedCollection = new TransactionalIndexedCollection<Integer>(Integer.class);
            indexedCollection.addAll(asSet(1, 2, 3));
            assertTrue(indexedCollection.update(asSet(3, 4), asSet(5, 6)));
            assertEquals(indexedCollection, asSet(1, 2, 5, 6));
        }
        {
            // Verify that with STRICT_REPLACEMENT, when no objects are to be replaced, collection is modified...
            TransactionalIndexedCollection<Integer> indexedCollection = new TransactionalIndexedCollection<Integer>(Integer.class);
            indexedCollection.addAll(asSet(1, 2, 3));
            assertTrue(indexedCollection.update(Collections.<Integer>emptySet(), asSet(4, 5), queryOptions(enableFlags(TransactionalIndexedCollection.STRICT_REPLACEMENT))));
            assertEquals(indexedCollection, asSet(1, 2, 3, 4, 5));
        }
    }

    public void testIterableContains() {
        final Set<Integer> collection = asSet(1, 2, 3);
        ResultSet<Integer> resultSet = new StoredSetBasedResultSet<Integer>(collection);
        Iterable<Integer> iterable = asIterable(collection);

        assertTrue(TransactionalIndexedCollection.iterableContains(collection, 1));
        assertFalse(TransactionalIndexedCollection.iterableContains(collection, 4));
        assertTrue(TransactionalIndexedCollection.iterableContains(resultSet, 1));
        assertFalse(TransactionalIndexedCollection.iterableContains(resultSet, 4));
        assertTrue(TransactionalIndexedCollection.iterableContains(iterable, 1));
        assertFalse(TransactionalIndexedCollection.iterableContains(iterable, 4));
    }

    public void testObjectStoreContainsAllIterable() {
        final ObjectStore<Integer> objectStore = new CollectionWrappingObjectStore<Integer>(asSet(1, 2, 3));

        assertTrue(TransactionalIndexedCollection.objectStoreContainsAllIterable(objectStore, asSet(1, 2, 3), noQueryOptions()));
        assertTrue(TransactionalIndexedCollection.objectStoreContainsAllIterable(objectStore, asIterable(asSet(1, 2, 3)), noQueryOptions()));
        assertFalse(TransactionalIndexedCollection.objectStoreContainsAllIterable(objectStore, asSet(1, 4, 3), noQueryOptions()));
        assertFalse(TransactionalIndexedCollection.objectStoreContainsAllIterable(objectStore, asIterable(asSet(1, 4, 3)), noQueryOptions()));
        assertFalse(TransactionalIndexedCollection.objectStoreContainsAllIterable(objectStore, asSet(1, 2, 3, 4), noQueryOptions()));
        assertFalse(TransactionalIndexedCollection.objectStoreContainsAllIterable(objectStore, asIterable(asSet(1, 2, 3, 4)), noQueryOptions()));
        assertTrue(TransactionalIndexedCollection.objectStoreContainsAllIterable(objectStore, Collections.<Integer>emptySet(), noQueryOptions()));
        assertTrue(TransactionalIndexedCollection.objectStoreContainsAllIterable(objectStore, asIterable(Collections.<Integer>emptySet()), noQueryOptions()));
    }

    static <O> Iterable<O> asIterable(final Collection<O> collection) {
        return new Iterable<O>() {
            @Override
            public Iterator<O> iterator() {
                return collection.iterator();
            }
        };
    }
    static <O> Set<O> asSet(O... objects) {
        return new LinkedHashSet<O>(asList(objects));
    }
    static <O> Set<O> asSet(ResultSet<O> resultSet) {
        Set<O> results = new LinkedHashSet<O>();
        for (O item : resultSet) {
            results.add(item);
        }
        return results;
    }
}
