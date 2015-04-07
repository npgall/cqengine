/**
 * Copyright 2012-2015 Niall Gallagher
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
 * @author niall.gallagher
 */
public class ArgumentValidationOption {


    private final ArgumentValidationStrategy strategy;

    public ArgumentValidationOption(ArgumentValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public ArgumentValidationStrategy getStrategy() {
        return strategy;
    }

    /**
     * Utility method to extract a {@link ArgumentValidationOption} object from the query options provided, and to check
     * if the strategy specified is {@link ArgumentValidationStrategy#SKIP}.
     *
     * @param queryOptions The query options to check
     * @return True if {@link ArgumentValidationOption} object is contained in those provided and its strategy is
     * {@link com.googlecode.cqengine.query.option.ArgumentValidationStrategy#SKIP}
     */
    public static <O> boolean isSkip(QueryOptions queryOptions) {
        ArgumentValidationOption option = queryOptions.get(ArgumentValidationOption.class);
        return option != null && ArgumentValidationStrategy.SKIP.equals(option.getStrategy());
    }

    @Override
    public String toString() {
        return "argumentValidation(" + strategy + ")";
    }
}
