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

import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.queryOptions;
import static com.googlecode.cqengine.query.QueryFactory.disableFlags;
import static com.googlecode.cqengine.query.option.FlagsDisabled.isFlagDisabled;
import static org.junit.Assert.*;

/**
 * @author niall.gallagher
 */
public class FlagsDisabledTest {

    @Test
    public void testFlagsDisabled() {
        QueryOptions queryOptions = queryOptions(disableFlags("a", "b"));
        assertTrue(isFlagDisabled(queryOptions, "a"));
        assertTrue(isFlagDisabled(queryOptions, "b"));
        assertFalse(isFlagDisabled(queryOptions, "c"));
    }
}