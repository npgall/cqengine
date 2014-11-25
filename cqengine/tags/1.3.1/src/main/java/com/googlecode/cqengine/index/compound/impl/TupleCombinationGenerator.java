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
package com.googlecode.cqengine.index.compound.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains static utility methods for generating combinations of elements <i>between</i> lists.
 * <p/>
 * A major use of this class is to build compound indexes on several attributes where some attributes have multiple
 * values - {@link com.googlecode.cqengine.attribute.MultiValueAttribute}. In those cases the index must store entries
 * for all of the combinations of attribute values.
 *
 * @author Niall Gallagher
 */
public class TupleCombinationGenerator {

    /**
     * Given a list of lists as input, determines all combinations of objects <i>between</i> the input lists, with
     * no repetition. Combinations are returned as if the lists were navigated using preordered depth-first search.
     * <p/>
     * Example:
     * <pre>
     * List&lt;List&lt;Object&gt;&gt; inputLists = new ArrayList&lt;List&lt;Object&gt;&gt;() {{
     *     add(Arrays.&lt;Object&gt;asList(1));
     *     add(Arrays.&lt;Object&gt;asList("bar", "baz"));
     *     add(Arrays.&lt;Object&gt;asList(2.0, 3.0, 4.0));
     * }};
     * System.out.println(generateCombinations(inputLists));
     * </pre>
     * The example code above prints:
     * <pre>[[1, bar, 2.0], [1, bar, 3.0], [1, bar, 4.0], [1, baz, 2.0], [1, baz, 3.0], [1, baz, 4.0]]</pre>
     * <p/>
     * @param inputLists A list of lists, each inner list containing objects to be combined with objects from the
     * other lists
     * @param <T> The type of objects in the lists (supply {@link Object} if mixing types)
     * @return Combinations of objects between the input lists
     */
    public static <T> List<List<T>> generateCombinations(List<List<T>> inputLists) {
        if (inputLists.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<T>> results = new ArrayList<List<T>>();
        if (inputLists.size() == 1) {
            for (T object : inputLists.get(0)) {
                // This is the last list in the input lists supplied - processed first due to eager recursion below.
                // Add each object in this input list as a single element in its own new LinkedList.
                // The other branch will subsequently add objects from preceding input lists
                // to the _start_ of this LinkedList. We use LinkedList to avoid shuffling elements.
                results.add(new LinkedList<T>(Collections.singleton(object)));
            }
        }
        else {
            // Start processing objects from the first input list supplied,
            // but note that we will call this method recursively before we move on to the next object...
            for (T object : inputLists.get(0)) {
                // Prepare a tail list of the input lists (the input lists after this first one).
                // The tail list is actually a _view_ onto the original input lists, (no data is copied)...
                List<List<T>> tail = inputLists.subList(1, inputLists.size());
                // Call this method recursively, getting the first objects in the tail lists first...
                for (List<T> permutations : generateCombinations(tail)) {
                    // Insert the object from the first list at the _start_ of the permutations list...
                    permutations.add(0, object);
                    // As the stack unwinds, we have assembled permutations in the correct order.
                    // Add each permutation to results, to return to the preceding stack frame or to the caller...
                    results.add(permutations);
                }
            }
        }
        return results;
    }

    /**
     * Private constructor, not used.
     */
    TupleCombinationGenerator() {
    }
}
