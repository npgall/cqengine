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

import static org.junit.Assert.*;
/**
 * @author Niall Gallagher
 */
public class StringContainsTest {

    @Test
    public void testStringContains() {
        assertTrue(StringContains.containsFragment("THIS IS A TEST", "THIS"));
        assertTrue(StringContains.containsFragment("THIS IS A TEST", "TEST"));
        assertTrue(StringContains.containsFragment("THIS IS A TEST", "IS A"));
        assertFalse(StringContains.containsFragment("THIS IS A TEST", "FOO"));
        assertTrue(StringContains.containsFragment("THIS IS A TEST", ""));
        assertTrue(StringContains.containsFragment("", ""));
        assertFalse(StringContains.containsFragment("", "TEST"));
    }
}
