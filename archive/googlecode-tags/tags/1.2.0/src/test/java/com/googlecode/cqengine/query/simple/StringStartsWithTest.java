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
package com.googlecode.cqengine.query.simple;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Niall Gallagher
 */
public class StringStartsWithTest {

    @Test
    public void testStringStartsWith() {
        assertTrue(StringStartsWith.startsWithPrefix("THIS IS A TEST", "THIS"));
        assertFalse(StringStartsWith.startsWithPrefix("THIS IS A TEST", "TEST"));
        assertFalse(StringStartsWith.startsWithPrefix("THIS IS A TEST", "HIS"));
        assertTrue(StringStartsWith.startsWithPrefix("THIS IS A TEST", ""));
        assertTrue(StringStartsWith.startsWithPrefix("", ""));
        assertFalse(StringStartsWith.startsWithPrefix("", "TEST"));
    }
}
