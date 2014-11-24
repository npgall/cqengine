package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SelfAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

/**
 * A query which matches no objects in the collection.
 * <p/>
 * This is equivalent to a literal boolean 'false'.
 *
 * @author ngallagher
 */
public class None<O> extends SimpleQuery<O, O> {

    final Class<O> objectType;

    public None(Class<O> objectType) {
        super(new SelfAttribute<O>(objectType, "false"));
        this.objectType = objectType;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, O> attribute, O object) {
        return false;
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, O> attribute, O object) {
        return false;
    }

    @Override
    protected int calcHashCode() {
        return 1357656699; // chosen randomly
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof None)) return false;
        None that = (None) o;
        return this.objectType.equals(that.objectType);
    }
}