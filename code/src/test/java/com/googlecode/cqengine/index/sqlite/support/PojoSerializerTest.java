package com.googlecode.cqengine.index.sqlite.support;

import org.junit.Test;

/**
 * Unit tests for {@link PojoSerializer}.
 *
 * @author npgall
 */
public class PojoSerializerTest {

    @Test
    public void testPositiveSerializability() {
        PojoSerializer.validateObjectIsRoundTripSerializable(new KryoSerializablePojo(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeSerializability() {
        PojoSerializer.validateObjectIsRoundTripSerializable(new NonKryoSerializablePojo(1));
    }

    @SuppressWarnings("unused")
    static class KryoSerializablePojo {
        int i;
        KryoSerializablePojo() {
        }
        KryoSerializablePojo(int i) {
            this.i = i;
        }
    }

    @SuppressWarnings("unused")
    static class NonKryoSerializablePojo {
        int i;
        NonKryoSerializablePojo() {
            throw new IllegalStateException("Intentional exception");
        }
        NonKryoSerializablePojo(int i) {
            this.i = i;
        }
    }

}