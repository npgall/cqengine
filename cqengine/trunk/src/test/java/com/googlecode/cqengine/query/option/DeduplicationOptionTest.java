package com.googlecode.cqengine.query.option;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class DeduplicationOptionTest {
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(DeduplicationOption.class)
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE)
                .verify();
    }
}