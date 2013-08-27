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

import java.util.Map;

/**
 * @author Niall Gallagher
 */
public class DeduplicationOption<O> implements QueryOption<O> {

    private final DeduplicationStrategy strategy;

    public DeduplicationOption(DeduplicationStrategy strategy) {
        this.strategy = strategy;
    }

    public DeduplicationStrategy getStrategy() {
        return strategy;
    }

    /**
     * Utility method to extract a {@link DeduplicationOption} object from the query options provided, and to check
     * if the strategy specified is {@link DeduplicationStrategy#LOGICAL_ELIMINATION}.
     *
     * @param queryOptions The query options to check
     * @return True if {@link DeduplicationOption} object is contained in those provided and its strategy is
     * {@link DeduplicationStrategy#LOGICAL_ELIMINATION}
     */
    public static <O> boolean isLogicalElimination(Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        QueryOption<O> option = queryOptions.get(DeduplicationOption.class);
        return option instanceof DeduplicationOption
                && DeduplicationStrategy.LOGICAL_ELIMINATION.equals(((DeduplicationOption<O>) option).getStrategy());
    }

    /**
     * Utility method to extract a {@link DeduplicationOption} object from the query options provided, and to check
     * if the strategy specified is {@link DeduplicationStrategy#MATERIALIZE}.
     *
     * @param queryOptions The query options to check
     * @return True if {@link DeduplicationOption} object is contained in those provided and its strategy is
     * {@link DeduplicationStrategy#MATERIALIZE}
     */
    public static <O> boolean isMaterialize(Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
        QueryOption<O> option = queryOptions.get(DeduplicationOption.class);
        return option instanceof DeduplicationOption
                && DeduplicationStrategy.MATERIALIZE.equals(((DeduplicationOption<O>) option).getStrategy());
    }

    @Override
    public String toString() {
        return "deduplicate(" + strategy + ")";
    }
}
