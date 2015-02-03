package com.googlecode.cqengine.query.option;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class QueryOptionsTest {
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(QueryOptions.class)
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify();
    }
}