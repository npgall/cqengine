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
package com.googlecode.cqengine.resultset.iterator;


import java.util.Iterator;

/**
 * @author Niall Gallagher
 */
public class IteratorUtil {

    public static <O> boolean iterableContains(Iterable<O> iterable, O element) {
        for (O contained : iterable) {
            if (contained.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public static int countElements(Iterable<?> iterable) {
        int count = 0;
        // UnusedDeclaration warning is due to iterator.next() being invoked but not used.
        // Actually we intentionally invoke iterator.next() even though we don't use it,
        // in case iterator implementation requires this per typical usage...
        //noinspection UnusedDeclaration
        for (Object object : iterable) {
            count++;
        }
        return count;
    }
}
