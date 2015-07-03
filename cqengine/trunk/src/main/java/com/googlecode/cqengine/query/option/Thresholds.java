package com.googlecode.cqengine.query.option;

import com.googlecode.cqengine.query.QueryFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A wrapper around {@link Threshold}s which have been set into query options.
 * <p/>
 * Example usage using {@link QueryFactory}:<br/>
 * <code>QueryOptions queryOptions = queryOptions(applyThresholds(INDEX_ORDERING_SELECTIVITY, 0.4))</code>
 *
 * @author niall.gallagher
 */
public class Thresholds {

    final Map<Object, Double> thresholds = new LinkedHashMap<Object, Double>();

    public Thresholds(Collection<Threshold> thresholds) {
        for (Threshold threshold : thresholds) {
            this.thresholds.put(threshold.key, threshold.value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Thresholds)) {
            return false;
        }
        Thresholds that = (Thresholds) o;
        return thresholds.equals(that.thresholds);
    }

    @Override
    public int hashCode() {
        return thresholds.hashCode();
    }

    public Double getThreshold(Object key) {
        return thresholds.get(key);
    }

    public static Double getThreshold(QueryOptions queryOptions, Object key) {
        Thresholds thresholds = queryOptions.get(Thresholds.class);
        return thresholds == null ? null : thresholds.getThreshold(key);
    }
}
