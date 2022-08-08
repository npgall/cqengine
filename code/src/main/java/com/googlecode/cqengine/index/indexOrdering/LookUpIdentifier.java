package com.googlecode.cqengine.index.indexOrdering;

import java.util.Objects;

public class LookUpIdentifier {

    private final String lookup;
    private final String field;

    public LookUpIdentifier(String lookup, String field) {
        this.lookup = lookup;
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;

        }
        LookUpIdentifier that = (LookUpIdentifier) o;
        return lookup.equals(that.lookup) && field.equals(that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lookup, field);
    }

    @Override
    public String toString() {
        return "LookUpIdentifier{" +
                "lookup='" + lookup + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}
