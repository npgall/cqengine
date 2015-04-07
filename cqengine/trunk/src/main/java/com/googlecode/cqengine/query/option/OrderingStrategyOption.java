/**
 * Copyright 2012 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
