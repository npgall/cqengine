package com.googlecode.cqengine.query.option;

/**
 * A wrapper around a key and an associated {@link Double} value, representing the value for that threshold key
 * which is to be applied to tune CQEngine query performance. These thresholds can be supplied as query options.
 * <p/>
 * See {@link EngineThresholds} for information about some thresholds which can be set.
 *
 * @author niall.gallagher
 */
public class Threshold {

    final Object key;
    final Double value;

    public Threshold(Object key, Double value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Threshold)) {
            return false;
        }

        Threshold threshold = (Threshold) o;

        if (!key.equals(threshold.key)) {
            return false;
        }
        return value.equals(threshold.value);

    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
