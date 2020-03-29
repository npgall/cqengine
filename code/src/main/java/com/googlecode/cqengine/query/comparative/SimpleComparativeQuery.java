package com.googlecode.cqengine.query.comparative;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.query.ComparativeQuery;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * A superclass of classes implementing {@link ComparativeQuery}.
 * This class simply factors out some repetitive logic from subclasses.
 *
 * @param <A> The type of the attribute to which this query relates
 * @param <O> The type of the objects in the collection
 *
 * @author Niall Gallagher
 */
public abstract class SimpleComparativeQuery<O, A> implements ComparativeQuery<O, A> {

    protected final boolean attributeIsSimple;
    protected final Attribute<O, A> attribute;
    protected final SimpleAttribute<O, A> simpleAttribute;


    // Lazy calculate and cache hash code...
    private transient int cachedHashCode = 0;

    /**
     * Creates a new {@link SimpleComparativeQuery} initialized to make assertions on values of the specified attribute
     * @param attribute The attribute on which the assertion is to be made
     */
    public SimpleComparativeQuery(Attribute<O, A> attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("The attribute argument was null.");
        }
        this.attribute = attribute;
        if (attribute instanceof SimpleAttribute) {
            this.attributeIsSimple = true;
            this.simpleAttribute = (SimpleAttribute<O, A>) attribute;
        }
        else {
            this.attributeIsSimple = false;
            this.simpleAttribute = null;
        }
    }

    /**
     * Returns the type of the attribute originally supplied to the constructor, a shortcut for
     * {@link Attribute#getAttributeType()}.
     * @return the type of the attribute originally supplied to the constructor
     */
    public Class<A> getAttributeType() {
        return attribute.getAttributeType();
    }

    /**
     * Returns the name of the attribute originally supplied to the constructor, a shortcut for
     * {@link Attribute#getAttributeName()}.
     * @return the name of the attribute originally supplied to the constructor
     */
    public String getAttributeName() {
        return attribute.getAttributeName();
    }

    /**
     * Returns the attribute originally supplied to the constructor.
     * @return The attribute originally supplied to the constructor
     */
    public Attribute<O, A> getAttribute() {
        return attribute;
    }

    @Override
    public int hashCode() {
        // Lazy calculate and cache hash code...
        int h = this.cachedHashCode;
        if (h == 0) { // hash code not cached
            h = calcHashCode();
            if (h == 0) { // 0 is normally a valid hash code, coalesce with another...
                h = -1838660945; // was randomly chosen
            }
            this.cachedHashCode = h; // cache hash code
        }
        return h;
    }

    protected abstract int calcHashCode();

    @Override
    public Iterable<O> getMatches(ObjectSet<O> objectsInCollection, QueryOptions queryOptions) {
        return attributeIsSimple
                ? getMatchesForSimpleAttribute(simpleAttribute, objectsInCollection, queryOptions)
                : getMatchesForNonSimpleAttribute(attribute, objectsInCollection, queryOptions);
    }

    public abstract Iterable<O> getMatchesForSimpleAttribute(SimpleAttribute<O, A> attribute, ObjectSet<O> objectsInCollection, QueryOptions queryOptions);

    public abstract Iterable<O> getMatchesForNonSimpleAttribute(Attribute<O, A> attribute, ObjectSet<O> objectsInCollection, QueryOptions queryOptions);
}
