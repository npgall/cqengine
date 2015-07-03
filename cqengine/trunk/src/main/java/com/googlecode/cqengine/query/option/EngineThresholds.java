package com.googlecode.cqengine.query.option;

/**
 * An enum whose members can be used to set query options with {@link Double} values, representing thresholds which
 * can be overridden to tune CQEngine query performance.
 * <p/>
 * If a threshold is not set as a query option, a default threshold will be applied.
 * <i>Note that default thresholds might change between CQEngine versions.</i>
 *
 * @author niall.gallagher
 */
public enum EngineThresholds {

    /**
     * A threshold between 0.0 and 1.0, which refers to the selectivity of the query which triggers CQEngine to
     * use an index to order results, instead of retrieving all results and ordering them afterwards.
     * <p/>
     * There is a trade-off between these strategies, because index ordering forces use of a particular index whereas
     * that index might not always be the most optimal to quickly locate objects which match the query. On the other
     * hand if the query matches a large number of results then sorting those results afterwards can be relatively
     * expensive. Hence this threshold, which refers to the point at which CQEngine will automatically choose one
     * of these strategies over the other for each particular query.
     * <p/>
     * CQEngine calculates the selectivity of the query roughly as:<br/>
     * <code>1.0 - [number of objects the query matches]/[number of objects in the collection]</code><br/>
     * Therefore a query with low selectivity matches most of the collection, and a query with high selectivity matches
     * a small number of objects in the collection.
     * <p/>
     * When CQEngine determines that the selectivity of the query is lower than this configured threshold,
     * then it will use an index to order results. When query selectivity is higher than this threshold then it will
     * retrieve all results and order them afterwards.
     */
    INDEX_ORDERING_SELECTIVITY(0.5);

    final double thresholdDefault;

    EngineThresholds(double thresholdDefault) {
        this.thresholdDefault = thresholdDefault;
    }

    public double getThresholdDefault() {
        return thresholdDefault;
    }
}
