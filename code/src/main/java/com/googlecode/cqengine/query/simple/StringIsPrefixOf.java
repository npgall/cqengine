package com.googlecode.cqengine.query.simple;

import static com.googlecode.cqengine.query.support.QueryValidation.checkQueryValueNotNull;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

public class StringIsPrefixOf<O, A extends CharSequence> extends SimpleQuery<O, A> {

    private final A value;
    
    public StringIsPrefixOf(Attribute<O, A> attribute, A value) {
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
        for (A attributeValue : attribute.getValues(object, queryOptions)) {
            if (StringStartsWith.matchesValue(value, attributeValue, queryOptions)) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringIsPrefixOf)) return false;

        StringIsPrefixOf<?, ?> that = (StringIsPrefixOf<?, ?>) o;

        if (!attribute.equals(that.attribute)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    
}
