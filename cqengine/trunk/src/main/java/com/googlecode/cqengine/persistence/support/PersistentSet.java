package com.googlecode.cqengine.persistence.support;

import com.googlecode.cqengine.index.sqlite.IdentityAttributeIndex;
import com.googlecode.cqengine.persistence.Persistence;

import java.util.Set;

/**
 * @author niall.gallagher
 */
public interface PersistentSet<O, A extends Comparable<A>> extends Set<O> {

    Persistence<O, A> getPersistence();

    IdentityAttributeIndex<A, O> getBackingIndex();
}
