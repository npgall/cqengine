package com.googlecode.cqengine.query.simple;

import static com.googlecode.cqengine.query.support.QueryValidation.checkQueryValueNotNull;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

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
public class LongestPrefix<O, A extends CharSequence> extends SimpleQuery<O, A> {

    private final A value;
    public LongestPrefix(Attribute<O, A> attribute, A value) {
        super(attribute);
        this.value = checkQueryValueNotNull(value);
    }
    public A getValue() {
        return value;
    }
    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        CharSequence attributeValue = attribute.getValue(object, queryOptions);
        //swap the order of values
        return StringStartsWith.matchesValue(value, attributeValue, queryOptions);
    }
    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        // longest prefix doesn't make sense for non-simple attributes
        return false;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongestPrefix)) return false;

        LongestPrefix<?, ?> that = (LongestPrefix<?, ?>) o;

        if (!attribute.equals(that.attribute)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "longestPrefix(" + asLiteral(super.getAttributeName()) +
                ", " + asLiteral(value) +
                ")";
    }

}
