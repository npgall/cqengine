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
package com.googlecode.cqengine.index.compound.support;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author Niall Gallagher
 */
public class TupleCombinationGeneratorTest {

    @Test
    public void testGenerateCombinations_EmptyOuterList() {
        List<List<Object>> permutations = TupleCombinationGenerator.generateCombinations(Collections.<List<Object>>emptyList());
        Assert.assertTrue(permutations.isEmpty());
    }

    @Test
    public void testGenerateCombinations_AscendingListSize() {
        List<List<Object>> inputLists = new ArrayList<List<Object>>() {{
            add(Arrays.<Object>asList(1));
            add(Arrays.<Object>asList("bar", "baz"));
            add(Arrays.<Object>asList(2.0, 3.0, 4.0));
        }};
        List<List<Object>> permutations = TupleCombinationGenerator.generateCombinations(inputLists);
        Assert.assertEquals(
                "[[1, bar, 2.0], [1, bar, 3.0], [1, bar, 4.0], [1, baz, 2.0], [1, baz, 3.0], [1, baz, 4.0]]",
                permutations.toString()
        );
    }

    @Test
    public void testGenerateCombinations_EqualListSize() {
        List<List<Object>> inputLists = new ArrayList<List<Object>>() {{
            add(Arrays.<Object>asList(1, 2));
            add(Arrays.<Object>asList("bar", "baz"));
            add(Arrays.<Object>asList(3.0, 4.0));
        }};
        List<List<Object>> permutations = TupleCombinationGenerator.generateCombinations(inputLists);
        Assert.assertEquals(
                "[[1, bar, 3.0], [1, bar, 4.0], [1, baz, 3.0], [1, baz, 4.0], [2, bar, 3.0], [2, bar, 4.0], [2, baz, 3.0], [2, baz, 4.0]]",
                permutations.toString()
        );
    }

    @Test
    public void testGenerateCombinations_DescendingListSize() {
        List<List<Object>> inputLists = new ArrayList<List<Object>>() {{
            add(Arrays.<Object>asList(1, 2, 3));
            add(Arrays.<Object>asList("bar", "baz"));
            add(Arrays.<Object>asList(2.0));
        }};
        List<List<Object>> permutations = TupleCombinationGenerator.generateCombinations(inputLists);
        Assert.assertEquals(
                "[[1, bar, 2.0], [1, baz, 2.0], [2, bar, 2.0], [2, baz, 2.0], [3, bar, 2.0], [3, baz, 2.0]]",
                permutations.toString()
        );
    }

    @Test
    public void testGenerateCombinations_EmptySubList1() {
        List<List<Object>> inputLists = new ArrayList<List<Object>>() {{
            add(Collections.emptyList());
            add(Arrays.<Object>asList("bar", "baz"));
            add(Arrays.<Object>asList(2.0, 3.0, 4.0));
        }};
        List<List<Object>> permutations = TupleCombinationGenerator.generateCombinations(inputLists);
        Assert.assertTrue(permutations.isEmpty());
    }

    @Test
    public void testGenerateCombinations_EmptySubList2() {
        List<List<Object>> inputLists = new ArrayList<List<Object>>() {{
            add(Arrays.<Object>asList(1));
            add(Collections.emptyList());
            add(Arrays.<Object>asList(2.0, 3.0, 4.0));
        }};
        List<List<Object>> permutations = TupleCombinationGenerator.generateCombinations(inputLists);
        Assert.assertTrue(permutations.isEmpty());
    }

    @Test
    public void testGenerateCombinations_EmptySubList3() {
        List<List<Object>> inputLists = new ArrayList<List<Object>>() {{
            add(Arrays.<Object>asList(1));
            add(Arrays.<Object>asList("bar", "baz"));
            add(Collections.emptyList());
        }};
        List<List<Object>> permutations = TupleCombinationGenerator.generateCombinations(inputLists);
        Assert.assertTrue(permutations.isEmpty());
    }

    @Test
    public void testConstructor() {
        // Test the constructor (for test coverage only)...
        TupleCombinationGenerator combinationGenerator = new TupleCombinationGenerator();
        Assert.assertNotNull(combinationGenerator);
    }
}
