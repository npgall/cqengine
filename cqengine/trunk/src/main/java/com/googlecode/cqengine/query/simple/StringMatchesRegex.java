package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

import java.util.regex.Pattern;

/**
 * Asserts that an attribute's value matches a regular expression.
 * <p/>
 * To accelerate {@code matchesRegex(...)} queries, add a Standing Query Index on {@code matchesRegex(...)}.
 *
 * @author Niall Gallagher, Silvano Riz
 */
public class StringMatchesRegex<O, A extends CharSequence> extends SimpleQuery<O, A> {

    private final Attribute<O, A> attribute;
    private final Pattern regexPattern;

    /**
     * Creates a new {@link StringMatchesRegex} initialized to make assertions on whether values of the specified
     * attribute match the given regular expression pattern.
     *
     * @param attribute The attribute on which the assertion is to be made
     * @param regexPattern The regular expression pattern with which values of the attribute should be tested
     */
    public StringMatchesRegex(Attribute<O, A> attribute, Pattern regexPattern) {
        super(attribute);
        this.attribute = attribute;
        this.regexPattern = regexPattern;
    }


    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object) {
        CharSequence attributeValue = attribute.getValue(object);
        return regexPattern.matcher(attributeValue).matches();
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object) {
        for (A attributeValue : attribute.getValues(object)) {
            if (regexPattern.matcher(attributeValue).matches()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringMatchesRegex that = (StringMatchesRegex) o;
        return this.attribute.equals(that.attribute)
                && this.regexPattern.equals(that.regexPattern);
    }

    @Override
    protected int calcHashCode() {
        int result = attribute.hashCode();
        result = 31 * result + regexPattern.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "matchesRegex(" + super.getAttribute().getObjectType().getSimpleName() + "." + super.getAttributeName() +
                ", " + regexPattern + ")";
    }
}