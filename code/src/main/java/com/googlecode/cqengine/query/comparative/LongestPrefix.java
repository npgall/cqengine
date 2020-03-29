package com.googlecode.cqengine.query.comparative;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.cqengine.query.simple.SimpleQuery.asLiteral;
import static com.googlecode.cqengine.query.support.QueryValidation.checkQueryValueNotNull;

/**
 * LongestPrefix queries are used to find the longest prefix in a dataset for a given query term.
 * Given a data set with the following records
 * 
 * <pre>
 * | id | code   | Operator  |
 * | 0  | 35387  | Foo       |
 * | 1  | 35385  | Bar       |
 * | 2  | 353855 | A.N.Other |
 * </pre>
 * A longest prefix query using a  query as shown below would return the entity with id 0
 * <p/>
 *  {@code Query<Foo> query1 = longestPrefix(Foo.CODE, "35387123456")); }
 * 
 * @author Glen Lockhart
 */
public class LongestPrefix<O, A extends CharSequence> extends SimpleComparativeQuery<O, A> {

    private final A value;
    public LongestPrefix(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.value = checkQueryValueNotNull(value);
    }
    public A getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongestPrefix)) return false;

        LongestPrefix<?, ?> that = (LongestPrefix<?, ?>) o;

        if (!super.attribute.equals(that.attribute)) return false;
        return this.value.equals(that.value);
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public Iterable<O> getMatchesForSimpleAttribute(SimpleAttribute<O, A> attribute, ObjectSet<O> objectsInCollection, QueryOptions queryOptions) {
        List<O> results = new ArrayList<>();
        int currentCount = -1;
        for (O object : objectsInCollection) {
            CharSequence attributeValue = attribute.getValue(object, queryOptions);
            int count = countPrefixChars(value, attributeValue);
            if (count == 0) {
                continue;
            }
            if (count > currentCount) {
                currentCount = count;
                results.clear();
                results.add(object);
            } else if (count == currentCount) {
                results.add(object);
            }
        }
        return results;
    }

    @Override
    public Iterable<O> getMatchesForNonSimpleAttribute(Attribute<O, A> attribute, ObjectSet<O> objectsInCollection, QueryOptions queryOptions) {
        List<O> results = new ArrayList<>();
        int currentCount = -1;
        for (O object : objectsInCollection) {
            Iterable<A> attributeValues = attribute.getValues(object, queryOptions);
            for (A attributeValue : attributeValues) {
                int count = countPrefixChars(value, attributeValue);
                if (count == 0) {
                    continue;
                }
                if (count > currentCount) {
                    currentCount = count;
                    results.clear();
                    results.add(object);
                } else if (count == currentCount) {
                    results.add(object);
                }
            }
        }
        return results;
    }

    @Override
    public String toString() {
        return "longestPrefix(" + asLiteral(super.getAttributeName()) + ", " + asLiteral(value) + ")";
    }

    /**
     * Returns the length of the given candidate prefix if it is a prefix of the given main sequence.
     * Returns 0 if the candidate is not actually a prefix of the main sequence.
     */
    static int countPrefixChars(CharSequence mainSequence, CharSequence candidatePrefix) {
        int charsMatched = 0;
        for (int i = 0, length = Math.min(mainSequence.length(), candidatePrefix.length()); i < length; i++) {
            if (mainSequence.charAt(i) != candidatePrefix.charAt(i)) {
                break;
            }
            charsMatched++;
        }
        return charsMatched == candidatePrefix.length() ? charsMatched : 0;
    }

}
