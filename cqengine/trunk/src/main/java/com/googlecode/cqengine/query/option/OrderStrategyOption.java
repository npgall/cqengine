package com.googlecode.cqengine.query.option;

/**
 * @author Niall Gallagher
 */
public class OrderStrategyOption {


    private final OrderStrategy strategy;

    public OrderStrategyOption(OrderStrategy strategy) {
        this.strategy = strategy;
    }

    public OrderStrategy getStrategy() {
        return strategy;
    }

    /**
     * Utility method to extract a {@link OrderStrategyOption} object from the query options provided, and to check
     * if the strategy specified is {@link com.googlecode.cqengine.query.option.OrderStrategy#MATERIALIZE}.
     *
     * @param queryOptions The query options to check
     * @return True if {@link OrderStrategyOption} object is contained in those provided and its strategy is
     * {@link com.googlecode.cqengine.query.option.OrderStrategy#MATERIALIZE}
     */
    public static <O> boolean isMaterialize(QueryOptions queryOptions) {
        OrderStrategyOption option = queryOptions.get(OrderStrategyOption.class);
        return option != null && OrderStrategy.MATERIALIZE.equals(option.getStrategy());
    }

    /**
     * Utility method to extract a {@link OrderStrategyOption} object from the query options provided, and to check
     * if the strategy specified is {@link com.googlecode.cqengine.query.option.OrderStrategy#INDEX}.
     *
     * @param queryOptions The query options to check
     * @return True if {@link OrderStrategyOption} object is contained in those provided and its strategy is
     * {@link com.googlecode.cqengine.query.option.OrderStrategy#INDEX}
     */
    public static <O> boolean isIndex(QueryOptions queryOptions) {
        OrderStrategyOption option = queryOptions.get(OrderStrategyOption.class);
        return option != null && OrderStrategy.INDEX.equals(option.getStrategy());
    }

    @Override
    public String toString() {
        return "orderStrategy(" + strategy + ")";
    }
}
