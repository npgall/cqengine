package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Set;

/**
 * Asserts that an attribute has at least one of the values specified by the In query.
 *
 * @author Silvano Riz
 */
public class In<O, A> extends SimpleQuery<O, A> {

    private final Set<A> values;
    private final boolean disjoint;
    public In(Attribute<O, A> attribute, boolean disjoint, Set<A> values) {
        super(attribute);
        if (values == null || values.size() == 0){
            throw new IllegalArgumentException("The IN query must have at least one value.");
        }
        this.values = values;
        this.disjoint = disjoint;
    }

    public Set<A> getValues() {
        return values;
    }

    public boolean isDisjoint() {
        return disjoint;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        return values.contains(attribute.getValue(object, queryOptions));
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        for (A attributeValue : attribute.getValues(object, queryOptions)) {
            if (values.contains(attributeValue)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + values.hashCode();
        result = 31 * result + (disjoint ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof In)) return false;

        In<?, ?> in = (In<?, ?>) o;

        if (disjoint != in.disjoint) return false;
        return values.equals(in.values);
    }

    @Override
    public String toString() {
        return "in("+ asLiteral(super.getAttributeName()) +
                ", " + asLiteral(values) +
                ")";
    }
}
