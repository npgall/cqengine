package com.googlecode.cqengine.index.sqlite;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.AttributeIndex;

/**
 * Implemented by indexes which persist serialized objects directly in the index instead of persisting foreign keys.
 *
 * @author niall.gallagher
 */
public interface IdentityAttributeIndex<A, O> extends AttributeIndex<A, O> {

    /**
     * Returns an attribute which given a primary key of a stored object can read (deserialize) the corresponding
     * object from the identity index. This is called a foreign key attribute, because typically those keys will
     * be stored in other indexes, referring to the primary keys of this index.
     */
    SimpleAttribute<A, O> getForeignKeyAttribute();
}
