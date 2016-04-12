package com.googlecode.cqengine.persistence;

import com.googlecode.cqengine.attribute.SimpleAttribute;

/**
 * Implemented by non-heap implementations of persistence.
 * Those implementations cannot rely on object references and so refer to objects in the collection via a primary key.
 *
 * @author niall.gallagher
 */
public interface ExternalPersistence<O, A extends Comparable<A>> extends Persistence<O> {

    SimpleAttribute<O, A> getPrimaryKeyAttribute();
}
