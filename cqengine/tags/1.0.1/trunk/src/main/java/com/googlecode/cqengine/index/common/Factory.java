package com.googlecode.cqengine.index.common;

/**
 * A generic factory for an object of type T.
 * <p/>
 * This is "inspired" by Guava's Supplier or Guice's Provider interface. The purpose of this CQEngine-specific
 * interface is allow a degree of dependency injection/decoupling internally in CQEngine, without imposing a
 * dependency on a particular dependency injection framework on the application.
 *
 * @author Niall Gallagher
 */
public interface Factory<T> {

    /**
     * Creates an object.
     * @return a new instance of the object
     */
    public T create();
}
