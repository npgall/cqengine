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
package com.googlecode.cqengine.testutil;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Utility methods useful in unit tests.
 *
 * @author Niall Gallagher
 */
public class TestUtil {

    /**
     * Returns the values of the given attribute in objects returned by the given result set.
     * <p/>
     * The set returned preserves the order of objects returned by the result set, but eliminates duplicates.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O, A> Set<A> valuesOf(Attribute<O, A> attribute, ResultSet<O> resultSet) {
        Set<A> attributeValues = new LinkedHashSet<A>();
        for (O object : resultSet) {
            attributeValues.addAll(attribute.getValues(object));
        }
        return attributeValues;
    }

    /**
     * Returns a set of the given vararg values. The set preserves the order of values given, but eliminates duplicates.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <A> Set<A> setOf(A... values) {
        return new LinkedHashSet<A>(Arrays.asList(values));
    }

}
