package com.googlecode.cqengine.query.comparative;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.HashSet;
import java.util.Set;
import static com.googlecode.cqengine.query.simple.SimpleQuery.asLiteral;

/**
 * A comparative query which matches objects in the collection whose attribute values sort lower than all others.
 */
public class Min<O, A extends Comparable<A>> extends SimpleComparativeQuery<O, A> {

    /**
     * Creates a new {@link SimpleComparativeQuery} initialized to make assertions on values of the specified attribute
     *
     * @param attribute The attribute on which the assertion is to be made
     */
    public Min(Attribute<O, A> attribute) {
        super(attribute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Min)) return false;

        Min min = (Min) o;

        return super.attribute.equals(min.attribute);
    }

    @Override
    protected int calcHashCode() {
        return super.attribute.hashCode();
    }

    @Override
    public Iterable<O> getMatchesForSimpleAttribute(SimpleAttribute<O, A> attribute, ObjectSet<O> objectsInCollection, QueryOptions queryOptions) {
        A minimumValue = null;
        Set<O> results = new HashSet<>();
        for (O object : objectsInCollection) {
            A attributeValue = attribute.getValue(object, queryOptions);
            minimumValue = evaluate(object, attributeValue, minimumValue, (Set<O>) results);
        }
        return results;
    }

    @Override
    public Iterable<O> getMatchesForNonSimpleAttribute(Attribute<O, A> attribute, ObjectSet<O> objectsInCollection, QueryOptions queryOptions) {
        A minimumValue = null;
        Set<O> results = new HashSet<>();
        for (O object : objectsInCollection) {
            Iterable<A> attributeValues = attribute.getValues(object, queryOptions);
            for (A attributeValue : attributeValues) {
                minimumValue = evaluate(object, attributeValue, minimumValue, results);
            }
        }
        return results;
    }

    /**
     * Helper method which evaluates each attribute value encountered. Adds the object to the given set of
     * results if the attribute value equals the max, or clears the results if a new maximum value is detected.
     *
     * @return The new maximum value
     */
    A evaluate(O currentObject, A currentAttributeValue, A currentMinimumValue, Set<O> results) {
        if (currentMinimumValue == null) {
            currentMinimumValue = currentAttributeValue;
            results.add(currentObject);
            return currentMinimumValue;
        }
        final int cmp = currentAttributeValue.compareTo(currentMinimumValue);
        if (cmp == 0) {
            // We found another object whose attribute value is the same as the current minimum value.
            // Add that object to the set of results...
            results.add(currentObject);
        } else if (cmp < 0) {
            // We found an object whose attribute value is less than the minimum value found so far.
            // Clear all results encountered so far, and add this object to the set of results...
            currentMinimumValue = currentAttributeValue;
            results.clear();
            results.add(currentObject);
        }
        return currentMinimumValue;
    }

    @Override
    public String toString() {
        return "min(" + asLiteral(super.getAttributeName()) + ")";
    }
}
