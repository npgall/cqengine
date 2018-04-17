package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Objects;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class LevenshteinDistance<O> extends SimpleQuery<O, String> {

    private final String value;
    private final int maxDistance;

    /**
     * Creates a new {@link SimpleQuery} initialized to make assertions on values of the specified attribute
     *
     * @param attribute The attribute on which the assertion is to be made
     */
    public LevenshteinDistance(Attribute<O, String> attribute, String value, int maxDistance) {
        super(attribute);
        this.value = value;
        this.maxDistance = maxDistance;
    }

    public String getValue() {
        return value;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, String> attribute, O object, QueryOptions queryOptions) {
        throw new RuntimeException("Missing Levenshtein index on attribute " + attribute.toString());
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, String> attribute, O object, QueryOptions queryOptions) {
        throw new RuntimeException("Missing Levenshtein index on attribute " + attribute.toString());
    }

    @Override
    protected int calcHashCode() {
        return Objects.hashCode(value) + 31 * maxDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LevenshteinDistance<?> that = (LevenshteinDistance<?>) o;

        if (!attribute.equals(that.attribute)) return false;
        if (maxDistance != that.maxDistance) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public String toString() {
        return "distance("+ asLiteral(super.getAttributeName())
                + ", " + asLiteral(value)
                + ")<=" + maxDistance;
    }
}
