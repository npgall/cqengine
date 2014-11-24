package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SelfAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

/**
 * A query which matches all objects in the collection.
 * <p/>
 * This is equivalent to a literal boolean 'true'.
 *
 * @author ngallagher
 */
public class All<O> extends SimpleQuery<O, O> {

    final Class<O> objectType;

    public All(Class<O> objectType) {
        super(new SelfAttribute<O>(objectType, "true"));
        this.objectType = objectType;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, O> attribute, O object) {
        return true;
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, O> attribute, O object) {
        return true;
    }

    @Override
    protected int calcHashCode() {
        return 765906512; // chosen randomly
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof All)) return false;
        All that = (All) o;
        return this.objectType.equals(that.objectType);
    }
}