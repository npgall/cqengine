package com.googlecode.cqengine.collection.impl;

import java.util.Collection;

/**
 * A listener interface invoked by {@link ObservableIndexedCollection} when objects are added to or removed from
 * the collection.
 *
 * @author Niall Gallagher
 */
public interface CollectionListener<O> {

    void objectsAdded(Collection<O> objects);

    void objectsRemoved(Collection<O> objects);

    void objectsCleared();
}
