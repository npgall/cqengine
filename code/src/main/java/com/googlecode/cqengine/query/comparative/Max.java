package com.googlecode.cqengine.query.comparative;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.HashSet;
import java.util.Set;

import static com.googlecode.cqengine.query.simple.SimpleQuery.asLiteral;

/**
 * A comparative query which matches objects in the collection whose attribute values sort higher than all others.
 */
public class Max<O, A extends Comparable<A>> extends SimpleComparativeQuery<O, A> {

    /**
     * Creates a new {@link SimpleComparativeQuery} initialized to make assertions on values of the specified attribute
     *
     * @param attribute The attribute on which the assertion is to be made
     */
    public Max(Attribute<O, A> attribute) {
        super(attribute);
    }

    @Override
    protected int calcHashCode() {
        return super.attribute.hashCode();
    }

    @Override
    public Iterable<O> getMatchesForSimpleAttribute(SimpleAttribute<O, A> attribute, ObjectSet<O> objectsInCollection, QueryOptions queryOptions) {
        A maximumValue = null;
        Set<O> results = new HashSet<>();
        for (O object : objectsInCollection) {
            A attributeValue = attribute.getValue(object, queryOptions);
            if (maximumValue == null) {
                maximumValue = attributeValue;
                results.add(object);
                continue;
            }
            final int cmp = attributeValue.compareTo(maximumValue);
            if (cmp == 0) {
                // We found another object whose attribute value is the same as the current maximum value.
                // Add that object to the set of results...
                results.add(object);
            } else if (cmp > 0) {
                // We found an object whose attribute value is greater than the maximum value found so far.
                // Clear all results encountered so far, and add this object to the set of results...
                maximumValue = attributeValue;
                results.clear();
                results.add(object);
            }
        }
        return results;
    }

    @Override
    public Iterable<O> getMatchesForNonSimpleAttribute(Attribute<O, A> attribute, ObjectSet<O> objectsInCollection, QueryOptions queryOptions) {
        A maximumValue = null;
        Set<O> results = new HashSet<>();
        for (O object : objectsInCollection) {
            Iterable<A> attributeValues = attribute.getValues(object, queryOptions);
            for (A attributeValue : attributeValues) {
                if (maximumValue == null) {
                    maximumValue = attributeValue;
                    results.add(object);
                    continue;
                }
                final int cmp = attributeValue.compareTo(maximumValue);
                if (cmp == 0) {
                    // We found another object whose attribute value is the same as the current maximum value.
                    // Add that object to the set of results...
                    results.add(object);
                } else if (cmp > 0) {
                    // We found an object whose attribute value is greater than the maximum value found so far.
                    // Clear all results encountered so far, and add this object to the set of results...
                    maximumValue = attributeValue;
                    results.clear();
                    results.add(object);
                }
            }
        }
        return results;
    }

    @Override
    public String toString() {
        return "max(" + asLiteral(super.getAttributeName()) + ")";
    }
}
