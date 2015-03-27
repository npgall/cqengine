package com.googlecode.cqengine.persistence;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.index.sqlite.ConnectionManager;

import java.util.Set;

/**
 * @author niall.gallagher
 */
public interface Persistence<O, A extends Comparable<A>> extends ConnectionManager, Factory<Set<O>> {

    SimpleAttribute<O, A> getPrimaryKeyAttribute();
}
