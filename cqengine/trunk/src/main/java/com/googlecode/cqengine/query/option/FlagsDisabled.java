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

import com.googlecode.cqengine.query.QueryFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * A wrapper around object keys ("flags") which are said to be disabled.
 * <p/>
 * Example usage using {@link QueryFactory}:<br/>
 * <code>QueryOptions queryOptions = queryOptions(setFlagsDisabled("flag1", "flag2"))</code>
 *
 * @author niall.gallagher
 */
public class FlagsDisabled {

    final Set<Object> flags = new HashSet<Object>();

    public void add(Object flag) {
        flags.add(flag);
    }

    public boolean isFlagDisabled(Object flag) {
        return flags.contains(flag);
    }

    public static boolean isFlagDisabled(QueryOptions queryOptions, Object flag) {
        FlagsDisabled flagsDisabled = queryOptions.get(FlagsDisabled.class);
        return flagsDisabled != null && flagsDisabled.isFlagDisabled(flag);
    }
}
