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

    @Test(expected = IllegalStateException.class)
    public void testValidateObjectEquality() {
        PojoSerializer.validateObjectEquality(1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateHashCodeEquality() {
        PojoSerializer.validateHashCodeEquality(1, 2);
    }

    @SuppressWarnings("unused")
    static class KryoSerializablePojo {
        int i;
        KryoSerializablePojo() {
        }
        KryoSerializablePojo(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof KryoSerializablePojo)) return false;

            KryoSerializablePojo that = (KryoSerializablePojo) o;

            return i == that.i;
        }

        @Override
        public int hashCode() {
            return i;
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