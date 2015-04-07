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
 * @author Niall Gallagher
 */
public class IsolationOption {

    private final IsolationLevel isolationLevel;

    public IsolationOption(IsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    public IsolationLevel getIsolationLevel() {
        return isolationLevel;
    }

    @Override
    public String toString() {
        return "isolationLevel(" + isolationLevel + ")";
    }

    /**
     * Utility method to extract an {@link IsolationOption} object from the query options provided, and to check
     * if its level is the same as the one supplied.
     *
     * @param queryOptions The query options to check
     * @param level The isolation level to compare with
     * @return True if {@link IsolationOption} object is contained in those provided and its level matches that given
     */
    public static boolean isIsolationLevel(QueryOptions queryOptions, IsolationLevel level) {
        IsolationOption option = queryOptions.get(IsolationOption.class);
        return option != null && level.equals(option.getIsolationLevel());
    }
}