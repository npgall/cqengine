package com.googlecode.cqengine.query.option;

/**
 * @author Niall Gallagher
 */
public class OrderingStrategyOption {


    private final OrderingStrategy strategy;

    public OrderingStrategyOption(OrderingStrategy strategy) {
        this.strategy = strategy;
    }

    public OrderingStrategy getStrategy() {
        return strategy;
    }

    /**
     * Utility method to extract a {@link OrderingStrategyOption} object from the query options provided, and to check
     * if the strategy specified is {@link OrderingStrategy#MATERIALIZE}.
     *
     * @param queryOptions The query options to check
     * @return True if {@link OrderingStrategyOption} object is contained in those provided and its strategy is
     * {@link OrderingStrategy#MATERIALIZE}
     */
    public static <O> boolean isMaterialize(QueryOptions queryOptions) {
        OrderingStrategyOption option = queryOptions.get(OrderingStrategyOption.class);
        return option != null && OrderingStrategy.MATERIALIZE.equals(option.getStrategy());
    }

    /**
     * Utility method to extract a {@link OrderingStrategyOption} object from the query options provided, and to check
     * if the strategy specified is {@link OrderingStrategy#INDEX}.
     *
     * @param queryOptions The query options to check
     * @return True if {@link OrderingStrategyOption} object is contained in those provided and its strategy is
     * {@link OrderingStrategy#INDEX}
     */
    public static <O> boolean isIndex(QueryOptions queryOptions) {
        OrderingStrategyOption option = queryOptions.get(OrderingStrategyOption.class);
        return option != null && OrderingStrategy.INDEX.equals(option.getStrategy());
    }

    @Override
    public String toString() {
        return "orderingStrategy(" + strategy + ")";
    }
}
