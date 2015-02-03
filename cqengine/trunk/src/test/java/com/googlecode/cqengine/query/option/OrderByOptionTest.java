package com.googlecode.cqengine.query.option;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class OrderByOptionTest {
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(OrderByOption.class)
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify();
    }
}