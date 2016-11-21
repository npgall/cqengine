/**
 * Copyright 2012-2015 Niall Gallagher
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
 * A wrapper around object keys ("flags") which are said to be enabled.
 * <p/>
 * Example usage using {@link QueryFactory}:<br/>
 * <code>QueryOptions queryOptions = queryOptions(enableFlags("flag1", "flag2"))</code>
 *
 * @author niall.gallagher
 */
public class FlagsEnabled {

    private Set<Object> flags;
    private boolean readRequest;

    public void add(EngineFlags flag) {
        switch (flag) {
            case READ_REQUEST:
                readRequest = true;
                return;
            default:
                if (flags == null) {
                    flags = new HashSet();
                }
                flags.add(flag);
        }
    }

    public void remove(EngineFlags flag) {
        switch (flag) {
            case READ_REQUEST:
                readRequest = false;
                return;
            default:
                if (flags != null) {
                    flags.remove(flag);
                }
                flags.add(flag);
        }
    }

    public boolean isFlagEnabled(EngineFlags flag) {
        switch (flag) {
            case READ_REQUEST:
                return readRequest;
            default:
                if (flags == null) {
                    return false;
                } else {
                    return flags.contains(flag);
                }
        }
    }

    @Override
    public String toString() {
        return "flagsEnabled=" + flags + ",readRequest=" + readRequest;
    }

    /**
     * Returns an existing {@link FlagsEnabled} from the QueryOptions, or adds a new
     * instance to the query options and returns that.
     *
     * @param queryOptions The {@link QueryOptions}
     * @return The existing QueryOptions's FlagsEnabled or a new instance.
     */
    public static FlagsEnabled forQueryOptions(final QueryOptions queryOptions) {
        FlagsEnabled flags = queryOptions.getFlagsEnabled();
        if (flags == null) {
            flags = new FlagsEnabled();
            queryOptions.setFlagsEnabled(flags);
        }
        return flags;
    }

    public static boolean isFlagEnabled(QueryOptions queryOptions, EngineFlags flag) {
        FlagsEnabled flagsDisabled = queryOptions.getFlagsEnabled();
        return flagsDisabled != null && flagsDisabled.isFlagEnabled(flag);
    }
}
