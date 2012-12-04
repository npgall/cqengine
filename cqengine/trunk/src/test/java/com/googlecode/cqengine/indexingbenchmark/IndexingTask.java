package com.googlecode.cqengine.indexingbenchmark;

import com.googlecode.cqengine.testutil.Car;

import java.util.Collection;

/**
 * Defines the methods which all indexing tasks must implement,
 * to measure the overhead of building indexes of various types.
 *
 * @author Niall Gallagher
 */
public interface IndexingTask {

    /**
     * Initializes the task with the given collection.
     * Typically this will copy the collection into a new instance of CQEngine.
     * <p/>
     * Note that time taken by this method is NOT factored into the overhead to build the index.
     * Applications should store objects in IndexedCollection in the first place.
     *
     * @param collection The collection of cars to be indexed
     */
    void init(Collection<Car> collection);

    /**
     * Builds an in index on the collection, the type of index  determined by the task implementation.
     */
    void buildIndex();
}
