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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SelfAttribute;
import org.junit.Test;

import static com.googlecode.cqengine.query.QueryFactory.endsWith;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Niall Gallagher
 */
public class StringEndsWithTest {

    @Test
    public void testStringEndsWith() {

        Attribute<String, String> stringIdentity = new SelfAttribute<String>(String.class, "identity");
        assertTrue(endsWith(stringIdentity, "TEST").matches("THIS IS A TEST", noQueryOptions()));
        assertFalse(endsWith(stringIdentity, "THIS").matches("THIS IS A TEST", noQueryOptions()));
        assertFalse(endsWith(stringIdentity, "TES").matches("THIS IS A TEST", noQueryOptions()));
        assertTrue(endsWith(stringIdentity, "").matches("THIS IS A TEST", noQueryOptions()));
        assertTrue(endsWith(stringIdentity, "").matches("", noQueryOptions()));
        assertFalse(endsWith(stringIdentity, "TEST").matches("", noQueryOptions()));
    }
}
