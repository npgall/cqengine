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
package com.googlecode.cqengine.resultset.filter;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FilteringIteratorTest {
    @Test
    public void testHasNextDoesNotAdvanceIterator(){
        List<String> testList = Arrays.asList("abc", "bcd", "cde");
        FilteringIterator<String> iterator = new FilteringIterator<String>(testList.iterator()) {
            @Override
            public boolean isValid(String object) {
                return true;
            }
        };
        iterator.hasNext();
        iterator.hasNext();
        iterator.hasNext();
        assertThat(iterator.next(), is("abc"));
    }

    @Test
    public void testNextPopulatedWithoutCallingHasNext(){
        List<String> testList = Arrays.asList("abc", "bcd", "cde");
        FilteringIterator<String> iterator = new FilteringIterator<String>(testList.iterator()) {
            @Override
            public boolean isValid(String object) {
                return true;
            }
        };
        assertThat(iterator.next(), is("abc"));
    }

    @Test
    public void testDelegatedIteratorHasNulls() {
        List<String> testList = Arrays.asList("abc", null, "cde");
        FilteringIterator<String> iterator = new FilteringIterator<String>(testList.iterator()) {
            @Override
            public boolean isValid(String object) {
                return true;
            }
        };
        assertThat(iterator.next(), is("abc"));
        assertThat(iterator.next(), nullValue());
        assertThat(iterator.next(), is("cde"));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testFiltering() {
        List<String> testList = Arrays.asList("aaa", "bbb", "aab", "bba");
        FilteringIterator<String> iterator = new FilteringIterator<String>(testList.iterator()) {
            @Override
            public boolean isValid(String object) {
                return object.startsWith("aa");
            }
        };
        assertThat(iterator.next(), is("aaa"));
        assertThat(iterator.next(), is("aab"));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyDelegate() {
        List<String> testList = Arrays.asList();
        FilteringIterator<String> iterator = new FilteringIterator<String>(testList.iterator()) {
            @Override
            public boolean isValid(String object) {
                return true;
            }
        };
        iterator.next();
    }
}
