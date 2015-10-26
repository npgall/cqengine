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
package com.googlecode.cqengine.query;

import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.Not;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.query.simple.*;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

/**
 * @author Niall Gallagher
 */
@RunWith(DataProviderRunner.class)
public class QueriesEqualsAndHashCodeTest {

    /**
     * Returns Query classes whose equals() and hashCode() methods can be validated by EqualsVerifier.
     */
    @SuppressWarnings("unchecked")
    @DataProvider
    public static List<List<Class>> getQueryClassesForAutomatedValidation() {
        return Arrays.asList(
                Arrays.<Class>asList(And.class),
                Arrays.<Class>asList(Between.class),
                Arrays.<Class>asList(Equal.class),
                Arrays.<Class>asList(ExistsIn.class),
                Arrays.<Class>asList(GreaterThan.class),
                Arrays.<Class>asList(Has.class),
                Arrays.<Class>asList(LessThan.class),
                Arrays.<Class>asList(Not.class),
                Arrays.<Class>asList(Or.class),
                Arrays.<Class>asList(StringContains.class),
                Arrays.<Class>asList(StringEndsWith.class),
                Arrays.<Class>asList(StringIsContainedIn.class),
                Arrays.<Class>asList(StringMatchesRegex.class),
                Arrays.<Class>asList(StringStartsWith.class)
        );
    }

    /**
     * Parameterized test which validates a Query class using EqualsVerifier.
     */
    @Test
    @UseDataProvider(value = "getQueryClassesForAutomatedValidation")
    public void testQueryClass(Class<? extends Query> queryClass) {
        EqualsVerifier.forClass(queryClass)
                .withCachedHashCode("cachedHashCode", "calcHashCode", null)
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE, Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    /**
     * Query class {@link All} has a non-standard hashCode implementation.
     */
    @Test
    public void testQueryClass_All() {
        Query<String> allStrings1 = QueryFactory.all(String.class);
        Query<String> allStrings2 = QueryFactory.all(String.class);
        Query<Integer> allIntegers1 = QueryFactory.all(Integer.class);
        Query<Integer> allIntegers2 = QueryFactory.all(Integer.class);

        Assert.assertEquals(allStrings1, allStrings1);
        Assert.assertEquals(allStrings1, allStrings2);
        Assert.assertEquals(allIntegers1, allIntegers1);
        Assert.assertEquals(allIntegers1, allIntegers2);

        Assert.assertNotEquals(allStrings1, allIntegers1);

        // HashCode is a constant in All...
        Assert.assertEquals(765906512, allStrings1.hashCode());
        Assert.assertEquals(765906512, allStrings2.hashCode());
        Assert.assertEquals(765906512, allIntegers1.hashCode());
        Assert.assertEquals(765906512, allIntegers2.hashCode());
    }

    /**
     * Query class {@link None} has a non-standard hashCode implementation.
     */
    @Test
    public void testQueryClass_None() {
        Query<String> noneStrings1 = QueryFactory.none(String.class);
        Query<String> noneStrings2 = QueryFactory.none(String.class);
        Query<Integer> noneIntegers1 = QueryFactory.none(Integer.class);
        Query<Integer> noneIntegers2 = QueryFactory.none(Integer.class);

        Assert.assertEquals(noneStrings1, noneStrings1);
        Assert.assertEquals(noneStrings1, noneStrings2);
        Assert.assertEquals(noneIntegers1, noneIntegers1);
        Assert.assertEquals(noneIntegers1, noneIntegers2);

        Assert.assertNotEquals(noneStrings1, noneIntegers1);

        // HashCode is a constant in None...
        Assert.assertEquals(1357656699, noneStrings1.hashCode());
        Assert.assertEquals(1357656699, noneStrings2.hashCode());
        Assert.assertEquals(1357656699, noneIntegers1.hashCode());
        Assert.assertEquals(1357656699, noneIntegers2.hashCode());
    }
}
