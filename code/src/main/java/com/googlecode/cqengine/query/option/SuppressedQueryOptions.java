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

import java.util.Set;

/**
 * A {@link QueryOptions} wrapper whose implementation of the {@link QueryOptions#get(Object)} method will return
 * null if the supplied key is in a set of keys to be suppressed as supplied to the constructor.
 *
 * @author npgall
 */
public class SuppressedQueryOptions extends QueryOptions {

    final Set<Object> suppressedOptions;

    public SuppressedQueryOptions(QueryOptions queryOptions, Set<Object> suppressedOptions) {
        super.persistenceOption = queryOptions.persistenceOption;
        super.connectionManagerOption = queryOptions.connectionManagerOption;
        super.queryEngineOption = queryOptions.queryEngineOption;
        super.deduplicationOption = queryOptions.deduplicationOption;
        super.isolationOption = queryOptions.isolationOption;
        super.flagsEnabledOption = queryOptions.flagsEnabledOption;
        super.flagsDisabledOption = queryOptions.flagsDisabledOption;
        super.thresholdsOption = queryOptions.thresholdsOption;
        super.closeableRequestResourcesOption = queryOptions.closeableRequestResourcesOption;
        super.orderByOption = queryOptions.orderByOption;
        super.rootQueryOption = queryOptions.rootQueryOption;
        super.extraOptions = queryOptions.extraOptions;
        this.suppressedOptions = suppressedOptions;
    }

    @Override
    public Object get(Object key) {
        if (suppressedOptions.contains(key)) {
            return null;
        }
        return super.get(key);
    }
}
