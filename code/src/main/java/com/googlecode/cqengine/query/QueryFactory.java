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

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.*;
import com.googlecode.cqengine.attribute.support.*;
import com.googlecode.cqengine.entity.MapEntity;
import com.googlecode.cqengine.entity.PrimaryKeyedMapEntity;
import com.googlecode.cqengine.query.comparative.LongestPrefix;
import com.googlecode.cqengine.query.comparative.Max;
import com.googlecode.cqengine.query.comparative.Min;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.Not;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.query.option.*;
import com.googlecode.cqengine.query.simple.*;
import net.jodah.typetools.TypeResolver;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * A static factory for creating {@link Query} objects and its descendants.
 *
 * @author Niall Gallagher
 */
public class QueryFactory {

    /**
     * Private constructor, not used.
     */
    QueryFactory() {
    }

    /**
     * Creates an {@link Equal} query which asserts that an attribute equals a certain value.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValue The value to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link Equal} query
     */
    public static <O, A> Equal<O, A> equal(Attribute<O, A> attribute, A attributeValue) {
        return new Equal<O, A>(attribute, attributeValue);
    }

    /**
     * Creates a {@link LessThan} query which asserts that an attribute is less than or equal to an upper bound
     * (i.e. less than, inclusive).
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValue The upper bound to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link LessThan} query
     */
    public static <O, A extends Comparable<A>> LessThan<O, A> lessThanOrEqualTo(Attribute<O, A> attribute, A attributeValue) {
        return new LessThan<O, A>(attribute, attributeValue, true);
    }

    /**
     * Creates a {@link LessThan} query which asserts that an attribute is less than (but not equal to) an upper
     * bound (i.e. less than, exclusive).
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValue The upper bound to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link LessThan} query
     */
    public static <O, A extends Comparable<A>> LessThan<O, A> lessThan(Attribute<O, A> attribute, A attributeValue) {
        return new LessThan<O, A>(attribute, attributeValue, false);
    }

    /**
     * Creates a {@link GreaterThan} query which asserts that an attribute is greater than or equal to a lower
     * bound (i.e. greater than, inclusive).
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValue The lower bound to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link GreaterThan} query
     */
    public static <O, A extends Comparable<A>> GreaterThan<O, A> greaterThanOrEqualTo(Attribute<O, A> attribute, A attributeValue) {
        return new GreaterThan<O, A>(attribute, attributeValue, true);
    }

    /**
     * Creates a {@link LessThan} query which asserts that an attribute is greater than (but not equal to) a lower
     * bound (i.e. greater than, exclusive).
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValue The lower bound to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link GreaterThan} query
     */
    public static <O, A extends Comparable<A>> GreaterThan<O, A> greaterThan(Attribute<O, A> attribute, A attributeValue) {
        return new GreaterThan<O, A>(attribute, attributeValue, false);
    }

    /**
     * Creates a {@link Between} query which asserts that an attribute is between a lower and an upper bound.
     *
     * @param attribute The attribute to which the query refers
     * @param lowerValue The lower bound to be asserted by the query
     * @param lowerInclusive Whether the lower bound is inclusive or not (true for "greater than or equal to")
     * @param upperValue The upper bound to be asserted by the query
     * @param upperInclusive Whether the upper bound is inclusive or not (true for "less than or equal to")
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link GreaterThan} query
     */
    public static <O, A extends Comparable<A>> Between<O, A> between(Attribute<O, A> attribute, A lowerValue, boolean lowerInclusive, A upperValue, boolean upperInclusive) {
        return new Between<O, A>(attribute, lowerValue, lowerInclusive, upperValue, upperInclusive);
    }

    /**
     * Creates a {@link Between} query which asserts that an attribute is between a lower and an upper bound,
     * inclusive.
     *
     * @param attribute The attribute to which the query refers
     * @param lowerValue The lower bound to be asserted by the query
     * @param upperValue The upper bound to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link GreaterThan} query
     */
    public static <O, A extends Comparable<A>> Between<O, A> between(Attribute<O, A> attribute, A lowerValue, A upperValue) {
        return new Between<O, A>(attribute, lowerValue, true, upperValue, true);
    }

    /**
     * <p> Creates a {@link In} query which asserts that an attribute has at least one value matching any value in a set of values.
     * <p> If the given attribute is a {@link SimpleAttribute}, this method will set a hint in the query to
     * indicate that results for the child queries will inherently be disjoint and so will not require deduplication.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValues The set of values to match
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link In} query
     */
    public static <O, A> Query<O> in(Attribute<O, A> attribute, A... attributeValues) {
        return in(attribute, Arrays.asList(attributeValues));
    }

    /**
     * <p> Creates a {@link In} query which asserts that an attribute has at least one value matching any value in a set of values.
     * <p> If the given attribute is a {@link SimpleAttribute}, this method will set a hint in the query to
     * indicate that results for the child queries will inherently be disjoint and so will not require deduplication.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValues TThe set of values to match
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link In} query
     */
    public static <O, A> Query<O> in(Attribute<O, A> attribute, Collection<A> attributeValues) {
        return in(attribute, attribute instanceof SimpleAttribute, attributeValues);
    }

    /**
     * <p> Creates a {@link In} query which asserts that an attribute has at least one value matching any value in a set of values.
     * <p> Note that <b><u>this can result in more efficient queries</u></b> than several {@link Equal} queries "OR"ed together using other means.
     *
     * @param attribute The attribute to which the query refers
     * @param disjoint Set it to {@code true} if deduplication is not necessary because the results are disjoint. Set it to {@code false} deduplication is needed
     * @param attributeValues The set of values to match
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link In} query
     */
    public static <O, A> Query<O> in(Attribute<O, A> attribute, boolean disjoint, Collection<A> attributeValues) {
        int n = attributeValues.size();
        switch (n) {
            case 0:
                return none(attribute.getObjectType());
            case 1:
                A singleValue = attributeValues.iterator().next();
                return equal(attribute, singleValue);
            default:
                // Copy the values into a Set if necessary...
                Set<A> values = (attributeValues instanceof Set ? (Set<A>)attributeValues : new HashSet<A>(attributeValues));
                return new In<O, A>(attribute, disjoint, values);
        }
    }

    /**
     * Creates a {@link StringStartsWith} query which asserts that an attribute starts with a certain string fragment.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValue The value to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link StringStartsWith} query
     */
    public static <O, A extends CharSequence> StringStartsWith<O, A> startsWith(Attribute<O, A> attribute, A attributeValue) {
        return new StringStartsWith<O, A>(attribute, attributeValue);
    }
    
    /**
     * Creates a {@link LongestPrefix} query which finds the object with the longest matching prefix.
     * 
     * @param attribute The attribute to which the query refers
     * @param attributeValue The value to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link LongestPrefix} query
     */
    public static <O, A extends CharSequence> LongestPrefix<O, A> longestPrefix(Attribute<O, A> attribute, A attributeValue) {
        return new LongestPrefix<>(attribute, attributeValue);
    }

    /**
     * Creates a {@link Min} query which finds the object(s) which have the minimum value of the given attribute.
     *
     * @param attribute The attribute to which the query refers
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link Min} query
     */
    public static <O, A extends Comparable<A>> Min<O, A> min(Attribute<O, A> attribute) {
        return new Min<>(attribute);
    }

    /**
     * Creates a {@link Max} query which finds the object(s) which have the maximum value of the given attribute.
     *
     * @param attribute The attribute to which the query refers
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return A {@link Max} query
     */
    public static <O, A extends Comparable<A>> Max<O, A> max(Attribute<O, A> attribute) {
        return new Max<>(attribute);
    }

    /**
     * Creates a {@link StringIsPrefixOf} query which finds all attributes that are prefixes of a certain string
     * 
     * @param attribute The attribute to which the query refers
     * @param attributeValue The value to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link StringIsPrefixOf} query
     */
    public static <O, A extends CharSequence> StringIsPrefixOf<O, A> isPrefixOf(Attribute<O, A> attribute, A attributeValue) {
        return new StringIsPrefixOf<>(attribute, attributeValue);
    }
    /**
     * Creates a {@link StringEndsWith} query which asserts that an attribute ends with a certain string fragment.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValue The value to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link StringEndsWith} query
     */
    public static <O, A extends CharSequence> StringEndsWith<O, A> endsWith(Attribute<O, A> attribute, A attributeValue) {
        return new StringEndsWith<O, A>(attribute, attributeValue);
    }

    /**
     * Creates a {@link StringContains} query which asserts that an attribute contains with a certain string fragment.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValue The value to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link StringContains} query
     */
    public static <O, A extends CharSequence> StringContains<O, A> contains(Attribute<O, A> attribute, A attributeValue) {
        return new StringContains<O, A>(attribute, attributeValue);
    }

    /**
     * Creates a {@link StringIsContainedIn} query which asserts that an attribute is contained in a certain string
     * fragment.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValue The value to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link StringStartsWith} query
     */
    public static <O, A extends CharSequence> StringIsContainedIn<O, A> isContainedIn(Attribute<O, A> attribute, A attributeValue) {
        return new StringIsContainedIn<O, A>(attribute, attributeValue);
    }

    /**
     * Creates a {@link StringMatchesRegex} query which asserts that an attribute's value matches a regular expression.
     * <p/>
     * To accelerate {@code matchesRegex(...)} queries, add a Standing Query Index on {@code matchesRegex(...)}.
     *
     * @param attribute The attribute to which the query refers
     * @param regexPattern The regular expression pattern to be asserted by the query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link StringStartsWith} query
     */
    public static <O, A extends CharSequence> StringMatchesRegex<O, A> matchesRegex(Attribute<O, A> attribute, Pattern regexPattern) {
        return new StringMatchesRegex<O, A>(attribute, regexPattern);
    }

    /**
     * Creates a {@link StringMatchesRegex} query which asserts that an attribute's value matches a regular expression.
     * <p/>
     * To accelerate {@code matchesRegex(...)} queries, add a Standing Query Index on {@code matchesRegex(...)}.
     *
     * @param attribute The attribute to which the query refers
     * @param regex The regular expression to be asserted by the query (this will be compiled via {@link Pattern#compile(String)})
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link StringStartsWith} query
     */
    public static <O, A extends CharSequence> StringMatchesRegex<O, A> matchesRegex(Attribute<O, A> attribute, String regex) {
        return new StringMatchesRegex<O, A>(attribute, Pattern.compile(regex));
    }

    /**
     * Creates an {@link Has} query which asserts that an attribute has a value (is not null).
     * <p/>
     * To accelerate {@code has(...)} queries, add a Standing Query Index on {@code has(...)}.
     * <p/>
     * To assert that an attribute does <i>not</i> have a value (is null), use <code>not(has(...))</code>.
     * <p/>
     * To accelerate <code>not(has(...))</code> queries, add a Standing Query Index on <code>not(has(...))</code>.
     *
     * @param attribute The attribute to which the query refers
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link Has} query
     */
    public static <O, A> Has<O, A> has(Attribute<O, A> attribute) {
        return new Has<O, A>(attribute);
    }

    /**
     * Creates an {@link And} query, representing a logical AND on child queries, which when evaluated yields the
     * <u>set intersection</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical AND
     * @param query2 The second child query to be connected via a logical AND
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link And} query, representing a logical AND on child queries
     */
    public static <O> And<O> and(Query<O> query1, Query<O> query2) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2);
        return new And<O>(queries);
    }

    /**
     * Creates an {@link And} query, representing a logical AND on child queries, which when evaluated yields the
     * <u>set intersection</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical AND
     * @param query2 The second child query to be connected via a logical AND
     * @param additionalQueries Additional child queries to be connected via a logical AND
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link And} query, representing a logical AND on child queries
     */
    public static <O> And<O> and(Query<O> query1, Query<O> query2, Query<O>... additionalQueries) {
        Collection<Query<O>> queries = new ArrayList<Query<O>>(2 + additionalQueries.length);
        queries.add(query1);
        queries.add(query2);
        Collections.addAll(queries, additionalQueries);
        return new And<O>(queries);
    }

    /**
     * Creates an {@link And} query, representing a logical AND on child queries, which when evaluated yields the
     * <u>set intersection</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical AND
     * @param query2 The second child query to be connected via a logical AND
     * @param additionalQueries Additional child queries to be connected via a logical AND
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link And} query, representing a logical AND on child queries
     */
    public static <O> And<O> and(Query<O> query1, Query<O> query2, Collection<Query<O>> additionalQueries) {
        Collection<Query<O>> queries = new ArrayList<Query<O>>(2 + additionalQueries.size());
        queries.add(query1);
        queries.add(query2);
        queries.addAll(additionalQueries);
        return new And<O>(queries);
    }

    /**
     * Creates an {@link Or} query, representing a logical OR on child queries, which when evaluated yields the
     * <u>set union</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical OR
     * @param query2 The second child query to be connected via a logical OR
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link Or} query, representing a logical OR on child queries
     */
    public static <O> Or<O> or(Query<O> query1, Query<O> query2) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2);
        return new Or<O>(queries);
    }

    /**
     * Creates an {@link Or} query, representing a logical OR on child queries, which when evaluated yields the
     * <u>set union</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical OR
     * @param query2 The second child query to be connected via a logical OR
     * @param additionalQueries Additional child queries to be connected via a logical OR
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link Or} query, representing a logical OR on child queries
     */
    public static <O> Or<O> or(Query<O> query1, Query<O> query2, Query<O>... additionalQueries) {
        Collection<Query<O>> queries = new ArrayList<Query<O>>(2 + additionalQueries.length);
        queries.add(query1);
        queries.add(query2);
        Collections.addAll(queries, additionalQueries);
        return new Or<O>(queries);
    }

    /**
     * Creates an {@link Or} query, representing a logical OR on child queries, which when evaluated yields the
     * <u>set union</u> of the result sets from child queries.
     *
     * @param query1 The first child query to be connected via a logical OR
     * @param query2 The second child query to be connected via a logical OR
     * @param additionalQueries Additional child queries to be connected via a logical OR
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link Or} query, representing a logical OR on child queries
     */
    public static <O> Or<O> or(Query<O> query1, Query<O> query2, Collection<Query<O>> additionalQueries) {
        Collection<Query<O>> queries = new ArrayList<Query<O>>(2 + additionalQueries.size());
        queries.add(query1);
        queries.add(query2);
        queries.addAll(additionalQueries);
        return new Or<O>(queries);
    }

    /**
     * Creates a {@link Not} query, representing a logical negation of a child query, which when evaluated
     * yields the <u>set complement</u> of the result set from the child query.
     *
     * @param query The child query to be logically negated
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return A {@link Not} query, representing a logical negation of a child query
     */
    public static <O> Not<O> not(Query<O> query) {
        return new Not<O>(query);
    }

    /**
     * Creates a query supporting the equivalent of SQL <code>EXISTS</code>.
     * <p/>
     * Asserts that objects in a local {@code IndexedCollection} match objects in a foreign collection,
     * based on a key attribute of local objects being equal to a key attribute of the foreign objects.
     * This query can be performed on the local collection, supplying the foreign collection and the
     * relevant attributes, as arguments to the query.
     * <p/>
     * This supports the SQL equivalent of:<br/>
     * <pre>
     * SELECT * From LocalCollection
     * WHERE EXISTS (
     *     SELECT * FROM ForeignCollection
     *     WHERE LocalCollection.localAttribute = ForeignCollection.foreignAttribute
     * )
     * </pre>
     *
     * @param foreignCollection The collection of foreign objects
     * @param localKeyAttribute An attribute of the local object
     * @param foreignKeyAttribute An attribute of objects in the foreign collection
     * @param <O> The type of the local object
     * @param <F> The type of the foreign objects
     * @param <A> The type of the common attributes
     * @return A query which checks if the local object matches any objects in the foreign collection based on the given
     * key attributes being equal
     */
    public static <O, F, A> Query<O> existsIn(final IndexedCollection<F> foreignCollection, final Attribute<O, A> localKeyAttribute, final Attribute<F, A> foreignKeyAttribute) {
        return new ExistsIn<O, F, A>(foreignCollection, localKeyAttribute, foreignKeyAttribute);
    }

    /**
     * Creates a query supporting the equivalent of SQL <code>EXISTS</code>,
     * with some additional restrictions on foreign objects.
     * <p/>
     * Asserts that objects in a local {@code IndexedCollection} match objects in a foreign collection,
     * based on a key attribute of local objects being equal to a key attribute of the foreign objects,
     * AND objects in the foreign collection matching some additional criteria.
     * This query can be performed on the local collection, supplying the foreign collection and the
     * relevant attributes, as arguments to the query.
     * <p/>
     * This supports the SQL equivalent of:<br/>
     * <pre>
     * SELECT * From LocalCollection
     * WHERE EXISTS (
     *     SELECT * FROM ForeignCollection
     *     WHERE LocalCollection.localAttribute = ForeignCollection.foreignAttribute
     *         AND ([AND|OR|NOT](ForeignCollection.someOtherAttribute = x) ...)
     * )
     * </pre>
     * @param foreignCollection The collection of foreign objects
     * @param localKeyAttribute An attribute of the local object
     * @param foreignKeyAttribute An attribute of objects in the foreign collection
     * @param foreignRestrictions A query specifying additional restrictions on foreign objects
     * @param <O> The type of the local object
     * @param <F> The type of the foreign objects
     * @param <A> The type of the common attributes
     * @return A query which checks if the local object matches any objects in the foreign collection based on the given
     * key attributes being equal
     */
    public static <O, F, A> Query<O> existsIn(final IndexedCollection<F> foreignCollection, final Attribute<O, A> localKeyAttribute, final Attribute<F, A> foreignKeyAttribute, final Query<F> foreignRestrictions) {
        return new ExistsIn<O, F, A>(foreignCollection, localKeyAttribute, foreignKeyAttribute, foreignRestrictions);
    }

    /**
     * Creates a query which matches all objects in the collection.
     * <p/>
     * This is equivalent to a literal boolean 'true'.
     *
     * @param <O> The type of the objects in the collection
     * @return A query which matches all objects in the collection
     */
    public static <O> Query<O> all(Class<O> objectType) {
        return new All<O>(objectType);
    }

    /**
     * Creates a query which matches no objects in the collection.
     * <p/>
     * This is equivalent to a literal boolean 'false'.
     *
     * @param <O> The type of the objects in the collection
     * @return A query which matches no objects in the collection
     */
    public static <O> Query<O> none(Class<O> objectType) {
        return new None<O>(objectType);
    }

    /**
     * Creates an {@link OrderByOption} query option, encapsulating the given list of {@link AttributeOrder} objects
     * which pair an attribute with a preference to sort results by that attribute in either ascending or descending
     * order.
     *
     * @param attributeOrders The list of attribute orders by which objects should be sorted
     * @param <O> The type of the object containing the attributes
     * @return An {@link OrderByOption} query option, requests results to be sorted in the given order
     */
    public static <O> OrderByOption<O> orderBy(List<AttributeOrder<O>> attributeOrders) {
        return new OrderByOption<O>(attributeOrders);
    }

    /**
     * Creates an {@link OrderByOption} query option, encapsulating the given list of {@link AttributeOrder} objects
     * which pair an attribute with a preference to sort results by that attribute in either ascending or descending
     * order.
     *
     * @param attributeOrders The list of attribute orders by which objects should be sorted
     * @param <O> The type of the object containing the attributes
     * @return An {@link OrderByOption} query option, requests results to be sorted in the given order
     */
    public static <O> OrderByOption<O> orderBy(AttributeOrder<O>... attributeOrders) {
        return new OrderByOption<O>(Arrays.asList(attributeOrders));
    }

    /**
     * Creates an {@link AttributeOrder} object which pairs an attribute with a preference to sort results by that
     * attribute in ascending order. These {@code AttributeOrder} objects can then be passed to the
     * {@link #orderBy(com.googlecode.cqengine.query.option.AttributeOrder[])} method to create a query option which
     * sorts results by the indicated attributes and ascending/descending preferences.
     *
     * @param attribute An attribute to sort by
     * @param <O> The type of the object containing the attributes
     * @return An {@link AttributeOrder} object, encapsulating the attribute and a preference to sort results by it
     * in ascending order
     */
    public static <O> AttributeOrder<O> ascending(Attribute<O, ? extends Comparable> attribute) {
        return new AttributeOrder<O>(attribute, false);
    }

    /**
     * Creates an {@link AttributeOrder} object which pairs an attribute with a preference to sort results by that
     * attribute in descending order. These {@code AttributeOrder} objects can then be passed to the
     * {@link #orderBy(com.googlecode.cqengine.query.option.AttributeOrder[])} method to create a query option which
     * sorts results by the indicated attributes and ascending/descending preferences.
     *
     * @param attribute An attribute to sort by
     * @param <O> The type of the object containing the attributes
     * @return An {@link AttributeOrder} object, encapsulating the attribute and a preference to sort results by it
     * in descending order
     */
    public static <O> AttributeOrder<O> descending(Attribute<O, ? extends Comparable> attribute) {
        return new AttributeOrder<O>(attribute, true);
    }

    /**
     * Creates a {@link DeduplicationOption} query option, encapsulating a given {@link DeduplicationStrategy}, which
     * when supplied to the query engine requests it to eliminate duplicates objects from the results returned using
     * the strategy indicated.
     *
     * @param deduplicationStrategy The deduplication strategy the query engine should use
     * @return A {@link DeduplicationOption} query option, requests duplicate objects to be eliminated from results
     */
    public static DeduplicationOption deduplicate(DeduplicationStrategy deduplicationStrategy) {
        return new DeduplicationOption(deduplicationStrategy);
    }

    /**
     * Creates a {@link IsolationOption} query option, encapsulating a given {@link IsolationLevel}, which
     * when supplied to the query engine requests that level of transaction isolation.
     *
     * @param isolationLevel The transaction isolation level to request
     * @return An {@link IsolationOption} query option
     */
    public static IsolationOption isolationLevel(IsolationLevel isolationLevel) {
        return new IsolationOption(isolationLevel);
    }

    /**
     * Creates an {@link ArgumentValidationOption} query option, encapsulating a given
     * {@link ArgumentValidationStrategy}, which when supplied to the query engine requests that some argument
     * validation may be disabled (or enabled) for performance or reliability reasons.
     *
     * @param strategy The argument validation strategy to request
     * @return An {@link ArgumentValidationOption} query option
     */
    public static ArgumentValidationOption argumentValidation(ArgumentValidationStrategy strategy) {
        return new ArgumentValidationOption(strategy);
    }

    /**
     * A convenience method to encapsulate several objects together as {@link com.googlecode.cqengine.query.option.QueryOptions},
     * where the class of the object will become its key in the QueryOptions map.
     *
     * @param queryOptions The objects to encapsulate as QueryOptions
     * @return A {@link QueryOptions} object
     */
    public static QueryOptions queryOptions(Object... queryOptions) {
        return queryOptions(Arrays.asList(queryOptions));
    }

    /**
     * A convenience method to encapsulate a collection of objects as {@link com.googlecode.cqengine.query.option.QueryOptions},
     * where the class of the object will become its key in the QueryOptions map.
     *
     * @param queryOptions The objects to encapsulate as QueryOptions
     * @return A {@link QueryOptions} object
     */
    public static QueryOptions queryOptions(Collection<Object> queryOptions) {
        QueryOptions resultOptions = new QueryOptions();
        for (Object queryOption : queryOptions) {
            resultOptions.put(queryOption.getClass(), queryOption);
        }
        return resultOptions;
    }

    /**
     * A convenience method to encapsulate an empty collection of objects as
     * {@link com.googlecode.cqengine.query.option.QueryOptions}.
     *
     * @return A {@link QueryOptions} object
     */
    public static QueryOptions noQueryOptions() {
        return new QueryOptions();
    }

    /**
     * Creates a {@link FlagsEnabled} object which may be added to query options.
     * This object encapsulates arbitrary "flag" objects which are said to be "enabled".
     * <p/>
     * Some components such as indexes allow their default behaviour to be overridden by
     * setting flags in this way.
     *
     * @param flags Arbitrary objects which represent flags which may be interpreted by indexes etc.
     * @return A populated {@link FlagsEnabled} object which may be added to query options
     */
    public static FlagsEnabled enableFlags(Object... flags) {
        FlagsEnabled result = new FlagsEnabled();
        for (Object flag: flags) {
            result.add(flag);
        }
        return result;
    }

    /**
     * Creates a {@link FlagsDisabled} object which may be added to query options.
     * This object encapsulates arbitrary "flag" objects which are said to be "disabled".
     * <p/>
     * Some components such as indexes allow their default behaviour to be overridden by
     * setting flags in this way.
     *
     * @param flags Arbitrary objects which represent flags which may be interpreted by indexes etc.
     * @return A populated {@link FlagsDisabled} object which may be added to query options
     */
    public static FlagsDisabled disableFlags(Object... flags) {
        FlagsDisabled result = new FlagsDisabled();
        for (Object flag: flags) {
            result.add(flag);
        }
        return result;
    }

    /**
     * Creates a {@link Thresholds} object which may be added to query options.
     * It encapsulates individual {@link Threshold} objects which are to override default values for thresholds which
     * can be set to tune query performance.
     *
     * @param thresholds Encapsulates Double values relating to thresholds to be overridden
     * @return A populated {@link Thresholds} object which may be added to query options
     */
    public static Thresholds applyThresholds(Threshold... thresholds) {
        return new Thresholds(Arrays.asList(thresholds));
    }

    /**
     * Creates a {@link Threshold} object which may be added to query options.
     *
     * @param key The key of the threshold value to set
     * @param value The value to set for the threshold
     * @return A populated {@link Threshold} object encapsulating the given arguments
     */
    public static Threshold threshold(Object key, Double value) {
        return new Threshold(key, value);
    }

    /**
     * Creates a {@link SelfAttribute} for the given object.
     *
     * @param objectType The type of object
     * @return a {@link SelfAttribute} for the given object
     */
    public static <O> SelfAttribute<O> selfAttribute(Class<O> objectType) {
        return new SelfAttribute<O>(objectType);
    }

    /**
     * Creates a {@link SimpleNullableMapAttribute} which retrieves the value for the given key from an
     * {@link IndexedCollection} of {@link Map} objects.
     *
     * @param mapKey The map key
     * @param mapValueType The type of the value stored for the given key
     * @param <K> The type of the map key
     * @param <A> The type of the resulting attribute; which is the type of the value stored
     * @return a {@link SimpleNullableMapAttribute} which retrieves the value for the given key from a map
     */
    public static <K, A> Attribute<Map, A> mapAttribute(K mapKey, Class<A> mapValueType) {
        return new SimpleNullableMapAttribute<K, A>(mapKey, mapValueType);
    }

    /**
     * Wraps the given Map in a {@link MapEntity}, which can improve its performance when used in an IndexedCollection.
     * If the given Map already is a {@link MapEntity}, the same object will be returned.
     *
     * @param map The map to wrap
     * @return a {@link MapEntity} wrapping the given map
     */
    public static Map mapEntity(Map map) {
        return map instanceof MapEntity ? map : new MapEntity(map);
    }

    /**
     * Wraps the given Map in a {@link PrimaryKeyedMapEntity}, which can improve its performance when used in an
     * IndexedCollection.
     * If the given Map already is a {@link PrimaryKeyedMapEntity}, the same object will be returned.
     *
     * @param map The map to wrap
     * @param primaryKey The key of the entry in the map to be used as a primary key
     * @return a {@link PrimaryKeyedMapEntity} wrapping the given map
     */
    public static Map primaryKeyedMapEntity(Map map, Object primaryKey) {
        return map instanceof PrimaryKeyedMapEntity ? map : new PrimaryKeyedMapEntity(map, primaryKey);
    }

    /**
     * Returns an {@link OrderMissingLastAttribute} which which can be used in an {@link #orderBy(AttributeOrder)}
     * clause to specify that objects which do not have values for the given delegate attribute should be returned after
     * objects which do have values for the attribute.
     * <p/>
     * Essentially, this attribute can be used to order results based on whether a {@link #has(Attribute)} query on the
     * delegate attribute would return true or false. See documentation in {@link OrderMissingLastAttribute} for more
     * details.
     *
     * @param delegateAttribute The attribute which may or may not return values, based on which results should be
     * ordered
     * @param <O> The type of the object containing the attribute
     * @return An {@link OrderMissingLastAttribute} which orders objects with values before those without values
     */
    @SuppressWarnings("unchecked")
    public static <O> OrderMissingLastAttribute<O> missingLast(Attribute<O, ? extends Comparable> delegateAttribute) {
        return new OrderMissingLastAttribute<O>(delegateAttribute);
    }

    /**
     * Returns an {@link OrderMissingFirstAttribute} which which can be used in an {@link #orderBy(AttributeOrder)}
     * clause to specify that objects which do not have values for the given delegate attribute should be returned
     * before objects which do have values for the attribute.
     * <p/>
     * Essentially, this attribute can be used to order results based on whether a {@link #has(Attribute)} query on the
     * delegate attribute would return true or false. See documentation in {@link OrderMissingFirstAttribute} for more
     * details.
     *
     * @param delegateAttribute The attribute which may or may not return values, based on which results should be
     * ordered
     * @param <O> The type of the object containing the attribute
     * @return An {@link OrderMissingFirstAttribute} which orders objects without values before those with values
     */
    @SuppressWarnings("unchecked")
    public static <O> OrderMissingFirstAttribute<O> missingFirst(Attribute<O, ? extends Comparable> delegateAttribute) {
        return new OrderMissingFirstAttribute<O>(delegateAttribute);
    }

    /**
     * Creates a {@link StandingQueryAttribute} based on the given query. An index can then be built on this attribute,
     * and it will be able to to answer the query in constant time complexity O(1).
     *
     * @param standingQuery The standing query to encapsulate
     * @return a {@link StandingQueryAttribute} encapsulating the given query
     */
    public static <O> StandingQueryAttribute<O> forStandingQuery(Query<O> standingQuery) {
        return new StandingQueryAttribute<O>(standingQuery);
    }

    /**
     * Creates a {@link StandingQueryAttribute} which returns true if the given attribute does not have values for
     * an object.
     * <p/>
     * An index can then be built on this attribute, and it will be able to to answer a <code>not(has(attribute))</code>
     * query, returning objects which do not have values for that attribute, in constant time complexity O(1).
     *
     * @param attribute The attribute which will be used in a <code>not(has(attribute))</code> query
     * @return a {@link StandingQueryAttribute} which returns true if the given attribute does not have values for
     * an object
     */
    public static <O, A> StandingQueryAttribute<O> forObjectsMissing(Attribute<O, A> attribute) {
        return forStandingQuery(not(has(attribute)));
    }

    // ***************************************************************************************************************
    // Factory methods to create attributes from lambda expressions...
    // ***************************************************************************************************************

    /**
     * Creates a {@link SimpleAttribute} from the given function or lambda expression,
     * while attempting to infer generic type information for the attribute automatically.
     * <p/>
     * <b>Limitations of type inference</b><br/>
     * As of Java 8, there are limitations of this type inference.
     * CQEngine uses <a href="https://github.com/jhalterman/typetools">TypeTools</a> to infer generic types.
     * See documentation of that library for details.
     * If generic type information cannot be inferred, as a workaround you may use the overloaded variant of this method
     * which allows the types to be specified explicitly.
     * <p/>
     * This is a convenience method, which delegates to {@link #attribute(String, SimpleFunction)},
     * supplying {@code function.getClass().getName()} as the name of the attribute.
     *
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link SimpleAttribute} created from the given function or lambda expression
     */
    public static <O, A> SimpleAttribute<O, A> attribute(SimpleFunction<O, A> function) {
        return attribute(function.getClass().getName(), function);
    }

    /**
     * Creates a {@link SimpleAttribute} from the given function or lambda expression,
     * while attempting to infer generic type information for the attribute automatically.
     * <p/>
     * <b>Limitations of type inference</b><br/>
     * As of Java 8, there are limitations of this type inference.
     * CQEngine uses <a href="https://github.com/jhalterman/typetools">TypeTools</a> to infer generic types.
     * See documentation of that library for details.
     * If generic type information cannot be inferred, as a workaround you may use the overloaded variant of this method
     * which allows the types to be specified explicitly.
     *
     * @param attributeName The name of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link SimpleAttribute} created from the given function or lambda expression
     */
    public static <O, A> SimpleAttribute<O, A> attribute(String attributeName, SimpleFunction<O, A> function) {
        FunctionGenericTypes<O, A> resolved = resolveSimpleFunctionGenericTypes(function.getClass());
        return attribute(resolved.objectType, resolved.attributeType, attributeName, function);
    }

    /**
     * Creates a {@link SimpleAttribute} from the given function or lambda expression,
     * allowing the generic types of the attribute to be specified explicitly.
     *
     * @param objectType The type of the object containing the attribute
     * @param attributeType The type of the attribute
     * @param attributeName The name of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link SimpleAttribute} created from the given function or lambda expression
     */
    public static <O, A> SimpleAttribute<O, A> attribute(Class<O> objectType, Class<A> attributeType, String attributeName, SimpleFunction<O, A> function) {
        return new FunctionalSimpleAttribute<O, A>(objectType, attributeType, attributeName, function);
    }

    /**
     * Creates a {@link SimpleNullableAttribute} from the given function or lambda expression,
     * while attempting to infer generic type information for the attribute automatically.
     * <p/>
     * <b>Limitations of type inference</b><br/>
     * As of Java 8, there are limitations of this type inference.
     * CQEngine uses <a href="https://github.com/jhalterman/typetools">TypeTools</a> to infer generic types.
     * See documentation of that library for details.
     * If generic type information cannot be inferred, as a workaround you may use the overloaded variant of this method
     * which allows the types to be specified explicitly.
     * <p/>
     * This is a convenience method, which delegates to {@link #nullableAttribute(String, SimpleFunction)},
     * supplying {@code function.getClass().getName()} as the name of the attribute.
     *
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link SimpleNullableAttribute} created from the given function or lambda expression
     */
    public static <O, A> SimpleNullableAttribute<O, A> nullableAttribute(SimpleFunction<O, A> function) {
        return nullableAttribute(function.getClass().getName(), function);
    }

    /**
     * Creates a {@link SimpleNullableAttribute} from the given function or lambda expression,
     * while attempting to infer generic type information for the attribute automatically.
     * <p/>
     * <b>Limitations of type inference</b><br/>
     * As of Java 8, there are limitations of this type inference.
     * CQEngine uses <a href="https://github.com/jhalterman/typetools">TypeTools</a> to infer generic types.
     * See documentation of that library for details.
     * If generic type information cannot be inferred, as a workaround you may use the overloaded variant of this method
     * which allows the types to be specified explicitly.
     *
     * @param attributeName The name of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link SimpleNullableAttribute} created from the given function or lambda expression
     */
    public static <O, A> SimpleNullableAttribute<O, A> nullableAttribute(String attributeName, SimpleFunction<O, A> function) {
        FunctionGenericTypes<O, A> resolved = resolveSimpleFunctionGenericTypes(function.getClass());
        return nullableAttribute(resolved.objectType, resolved.attributeType, attributeName, function);
    }

    /**
     * Creates a {@link SimpleNullableAttribute} from the given function or lambda expression,
     * allowing the generic types of the attribute to be specified explicitly.
     *
     * @param objectType The type of the object containing the attribute
     * @param attributeType The type of the attribute
     * @param attributeName The name of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link SimpleNullableAttribute} created from the given function or lambda expression
     */
    public static <O, A> SimpleNullableAttribute<O, A> nullableAttribute(Class<O> objectType, Class<A> attributeType, String attributeName, SimpleFunction<O, A> function) {
        return new FunctionalSimpleNullableAttribute<O, A>(objectType, attributeType, attributeName, function);
    }

    /**
     * Creates a {@link MultiValueAttribute} from the given function or lambda expression,
     * while attempting to infer generic type information for the attribute automatically.
     * <p/>
     * <b>Limitations of type inference</b><br/>
     * As of Java 8, there are limitations of this type inference.
     * CQEngine uses <a href="https://github.com/jhalterman/typetools">TypeTools</a> to infer generic types.
     * See documentation of that library for details.
     * If generic type information cannot be inferred, as a workaround you may use the overloaded variant of this method
     * which allows the types to be specified explicitly.
     * <p/>
     * This is a convenience method, which delegates to {@link #attribute(Class, String, MultiValueFunction)},
     * supplying {@code function.getClass().getName()} as the name of the attribute.
     *
     * @param attributeType The type of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link MultiValueAttribute} created from the given function or lambda expression
     */
    public static <O, A, I extends Iterable<A>> MultiValueAttribute<O, A> attribute(Class<A> attributeType, MultiValueFunction<O, A, I> function) {
        return attribute(attributeType, function.getClass().getName(), function);
    }

    /**
     * Creates a {@link MultiValueAttribute} from the given function or lambda expression,
     * while attempting to infer generic type information for the attribute automatically.
     * <p/>
     * <b>Limitations of type inference</b><br/>
     * As of Java 8, there are limitations of this type inference.
     * CQEngine uses <a href="https://github.com/jhalterman/typetools">TypeTools</a> to infer generic types.
     * See documentation of that library for details.
     * If generic type information cannot be inferred, as a workaround you may use the overloaded variant of this method
     * which allows the types to be specified explicitly.
     *
     * @param attributeType The type of the attribute
     * @param attributeName The name of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link MultiValueAttribute} created from the given function or lambda expression
     */
    public static <O, A, I extends Iterable<A>> MultiValueAttribute<O, A> attribute(Class<A> attributeType, String attributeName, MultiValueFunction<O, A, I> function) {
        Class<O> resolvedObjectType = resolveMultiValueFunctionGenericObjectType(function.getClass());
        return attribute(resolvedObjectType, attributeType, attributeName, function);
    }

    /**
     * Creates a {@link MultiValueAttribute} from the given function or lambda expression,
     * allowing the generic types of the attribute to be specified explicitly.
     *
     * @param objectType The type of the object containing the attribute
     * @param attributeType The type of the attribute
     * @param attributeName The name of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link MultiValueAttribute} created from the given function or lambda expression
     */
    public static <O, A, I extends Iterable<A>> MultiValueAttribute<O, A> attribute(Class<O> objectType, Class<A> attributeType, String attributeName, MultiValueFunction<O, A, I> function) {
        return new FunctionalMultiValueAttribute<O, A, I>(objectType, attributeType, attributeName, function);
    }

    /**
     * Creates a {@link MultiValueNullableAttribute} from the given function or lambda expression,
     * while attempting to infer generic type information for the attribute automatically.
     * <p/>
     * <b>Limitations of type inference</b><br/>
     * As of Java 8, there are limitations of this type inference.
     * CQEngine uses <a href="https://github.com/jhalterman/typetools">TypeTools</a> to infer generic types.
     * See documentation of that library for details.
     * If generic type information cannot be inferred, as a workaround you may use the overloaded variant of this method
     * which allows the types to be specified explicitly.
     * <p/>
     * This is a convenience method, which delegates to {@link #nullableAttribute(Class, String, MultiValueFunction)},
     * supplying {@code function.getClass().getName()} as the name of the attribute.
     *
     * @param attributeType The type of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link MultiValueNullableAttribute} created from the given function or lambda expression
     */
    public static <O, A, I extends Iterable<A>> MultiValueNullableAttribute<O, A> nullableAttribute(Class<A> attributeType, MultiValueFunction<O, A, I> function) {
        return nullableAttribute(attributeType, function.getClass().getName(), function);
    }

    /**
     * Creates a {@link MultiValueNullableAttribute} from the given function or lambda expression,
     * while attempting to infer generic type information for the attribute automatically.
     * <p/>
     * <b>Limitations of type inference</b><br/>
     * As of Java 8, there are limitations of this type inference.
     * CQEngine uses <a href="https://github.com/jhalterman/typetools">TypeTools</a> to infer generic types.
     * See documentation of that library for details.
     * If generic type information cannot be inferred, as a workaround you may use the overloaded variant of this method
     * which allows the types to be specified explicitly.
     *
     * @param attributeType The type of the attribute
     * @param attributeName The name of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link MultiValueNullableAttribute} created from the given function or lambda expression
     */
    public static <O, A, I extends Iterable<A>> MultiValueNullableAttribute<O, A> nullableAttribute(Class<A> attributeType, String attributeName, MultiValueFunction<O, A, I> function) {
        Class<O> resolvedObjectType = resolveMultiValueFunctionGenericObjectType(function.getClass());
        return nullableAttribute(resolvedObjectType, attributeType, attributeName, function);
    }

    /**
     * Creates a {@link MultiValueNullableAttribute} from the given function or lambda expression,
     * allowing the generic types of the attribute to be specified explicitly.
     *
     * @param objectType The type of the object containing the attribute
     * @param attributeType The type of the attribute
     * @param attributeName The name of the attribute
     * @param function A function or lambda expression
     * @param <O> The type of the object containing the attribute
     * @param <A> The type of the attribute
     * @return A {@link MultiValueNullableAttribute} created from the given function or lambda expression
     */
    public static <O, A, I extends Iterable<A>> MultiValueNullableAttribute<O, A> nullableAttribute(Class<O> objectType, Class<A> attributeType, String attributeName, MultiValueFunction<O, A, I> function) {
        return new FunctionalMultiValueNullableAttribute<O, A, I>(objectType, attributeType, attributeName, true, function);
    }

    // ***************************************************************************************************************
    // Helper methods for creating attributes from lambda expressions...
    // ***************************************************************************************************************

    static final String GENERIC_TYPE_RESOLUTION_FAILURE_MESSAGE =
            "If the function you supplied was created from a lambda expression, then it's likely " +
                    "that the host JVM does not allow the generic type information to be read from lambda expressions. " +
                    "Alternatively, if you supplied a class-based implementation of the function, then you must ensure " +
                    "that you specified the generic types of the function when it was compiled. " +
                    "As a workaround, you can use the counterpart methods in QueryFactory " +
                    "which allow the generic types to be specified explicitly.";

    static <O, A, F> FunctionGenericTypes<O, A> resolveSimpleFunctionGenericTypes(Class<?> subType) {
        Class<?>[] typeArgs = TypeResolver.resolveRawArguments(SimpleFunction.class, subType);

        validateSimpleFunctionGenericTypes(typeArgs, subType);

        @SuppressWarnings("unchecked") Class<O> objectType = (Class<O>) typeArgs[0];
        @SuppressWarnings("unchecked") Class<A> attributeType = (Class<A>) typeArgs[1];
        return new FunctionGenericTypes<O, A>(objectType, attributeType);
    }

    static void validateSimpleFunctionGenericTypes(Class<?>[] typeArgs, Class<?> subType) {
        if (typeArgs == null) {
            throw new IllegalStateException("Could not resolve any generic type information from the given " +
                    "function of type: " + subType.getName() + ". " + GENERIC_TYPE_RESOLUTION_FAILURE_MESSAGE);
        }
        if (typeArgs.length != 2 || typeArgs[0] == TypeResolver.Unknown.class || typeArgs[1] == TypeResolver.Unknown.class) {
            throw new IllegalStateException("Could not resolve sufficient generic type information from the given " +
                    "function of type: " + subType.getName() + ", resolved: " + Arrays.toString(typeArgs) + ". " +
                    GENERIC_TYPE_RESOLUTION_FAILURE_MESSAGE);
        }
    }

    static <O> Class<O> resolveMultiValueFunctionGenericObjectType(Class<?> subType) {
        Class<?>[] typeArgs = TypeResolver.resolveRawArguments(MultiValueFunction.class, subType);

        validateMultiValueFunctionGenericTypes(typeArgs, subType);

        @SuppressWarnings("unchecked") Class<O> objectType = (Class<O>) typeArgs[0];
        return objectType;
    }

    static void validateMultiValueFunctionGenericTypes(Class<?>[] typeArgs, Class<?> subType) {
        if (typeArgs == null) {
            throw new IllegalStateException("Could not resolve any generic type information from the given " +
                    "function of type: " + subType.getName() + ". " + GENERIC_TYPE_RESOLUTION_FAILURE_MESSAGE);
        }
        if (typeArgs.length != 3 || typeArgs[0] == TypeResolver.Unknown.class) {
            throw new IllegalStateException("Could not resolve sufficient generic type information from the given " +
                    "function of type: " + subType.getName() + ", resolved: " + Arrays.toString(typeArgs) + ". " +
                    GENERIC_TYPE_RESOLUTION_FAILURE_MESSAGE);
        }
    }


        static class FunctionGenericTypes<O, A> {
        final Class<O> objectType;
        final Class<A> attributeType;

        FunctionGenericTypes(Class<O> objectType, Class<A> attributeType) {
            this.objectType = objectType;
            this.attributeType = attributeType;
        }
    }


    // ***************************************************************************************************************
    // The following methods are just overloaded vararg variants of existing methods above.
    // These methods are unnecessary as of Java 7, and are provided only for backward compatibility with Java 6 and
    // earlier.
    // 
    // The methods exists to work around warnings output by the Java compiler for *client code* invoking generic
    // vararg methods: "unchecked generic array creation of type Query<Foo>[] for varargs parameter" and similar.
    // See http://mail.openjdk.java.net/pipermail/coin-dev/2009-March/000217.html - project coin feature
    // in Java 7 which addresses the issue.
    // ***************************************************************************************************************

    /**
     * Overloaded variant of {@link #and(Query, Query, Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> And<O> and(Query<O> query1, Query<O> query2, Query<O> query3) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2, query3);
        return new And<O>(queries);
    }

    /**
     * Overloaded variant of {@link #and(Query, Query, Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> And<O> and(Query<O> query1, Query<O> query2, Query<O> query3, Query<O> query4) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2, query3, query4);
        return new And<O>(queries);
    }

    /**
     * Overloaded variant of {@link #and(Query, Query, Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> And<O> and(Query<O> query1, Query<O> query2, Query<O> query3, Query<O> query4, Query<O> query5) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2, query3, query4, query5);
        return new And<O>(queries);
    }

    // ***************************************************************************************************************

    /**
     * Overloaded variant of {@link #or(Query, Query, Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> Or<O> or(Query<O> query1, Query<O> query2, Query<O> query3) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2, query3);
        return new Or<O>(queries);
    }

    /**
     * Overloaded variant of {@link #or(Query, Query, Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> Or<O> or(Query<O> query1, Query<O> query2, Query<O> query3, Query<O> query4) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2, query3, query4);
        return new Or<O>(queries);
    }

    /**
     * Overloaded variant of {@link #or(Query, Query, Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> Or<O> or(Query<O> query1, Query<O> query2, Query<O> query3, Query<O> query4, Query<O> query5) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2, query3, query4, query5);
        return new Or<O>(queries);
    }

    // ***************************************************************************************************************

    /**
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.query.option.AttributeOrder[])} - see that method
     * for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> OrderByOption<O> orderBy(AttributeOrder<O> attributeOrder) {
        @SuppressWarnings({"unchecked"})
        List<AttributeOrder<O>> attributeOrders = Collections.singletonList(attributeOrder);
        return new OrderByOption<O>(attributeOrders);
    }
    
    /**
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.query.option.AttributeOrder[])} - see that method
     * for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> OrderByOption<O> orderBy(AttributeOrder<O> attributeOrder1, AttributeOrder<O> attributeOrder2) {
        @SuppressWarnings({"unchecked"})
        List<AttributeOrder<O>> attributeOrders = Arrays.asList(attributeOrder1, attributeOrder2);
        return new OrderByOption<O>(attributeOrders);
    }
    
    /**
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.query.option.AttributeOrder[])} - see that method
     * for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> OrderByOption<O> orderBy(AttributeOrder<O> attributeOrder1, AttributeOrder<O> attributeOrder2,
                                             AttributeOrder<O> attributeOrder3) {
        @SuppressWarnings({"unchecked"})
        List<AttributeOrder<O>> attributeOrders = Arrays.asList(attributeOrder1, attributeOrder2, attributeOrder3);
        return new OrderByOption<O>(attributeOrders);
    }
    
    /**
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.query.option.AttributeOrder[])} - see that method
     * for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> OrderByOption<O> orderBy(AttributeOrder<O> attributeOrder1, AttributeOrder<O> attributeOrder2,
                                             AttributeOrder<O> attributeOrder3, AttributeOrder<O> attributeOrder4) {
        @SuppressWarnings({"unchecked"})
        List<AttributeOrder<O>> attributeOrders = Arrays.asList(attributeOrder1, attributeOrder2, attributeOrder3,
                attributeOrder4);
        return new OrderByOption<O>(attributeOrders);
    }
    
    /**
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.query.option.AttributeOrder[])} - see that method
     * for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> OrderByOption<O> orderBy(AttributeOrder<O> attributeOrder1, AttributeOrder<O> attributeOrder2,
                                             AttributeOrder<O> attributeOrder3, AttributeOrder<O> attributeOrder4,
                                             AttributeOrder<O> attributeOrder5) {
        @SuppressWarnings({"unchecked"})
        List<AttributeOrder<O>> attributeOrders = Arrays.asList(attributeOrder1, attributeOrder2, attributeOrder3,
                attributeOrder4, attributeOrder5);
        return new OrderByOption<O>(attributeOrders);
    }

    /**
     * Converts a {@link Query} to a {@link Predicate} which can evaluate if the the query matches any given object.
     * The predicate will determine this by invoking {@link Query#matches(Object, QueryOptions)}, supplying null for
     * the query options.
     * <p/>
     * Note that while most queries do not utilize query options and thus will be compatible with this method,
     * it's possible that some queries might require query options. For those cases, create the predicate via
     * the counterpart method {@link #predicate(Query, QueryOptions)} instead.
     *
     * @param query The query to be converted to a predicate
     * @return A predicate which can evaluate if the the query matches any given object
     */
    public static <O> Predicate<O> predicate(Query<O> query) {
        return predicate(query, null);
    }

    /**
     * Converts a {@link Query} to a {@link Predicate} which can evaluate if the the query matches any given object.
     * The predicate will determine this by invoking {@link Query#matches(Object, QueryOptions)}.
     *
     * @param query The query to be converted to a predicate
     * @param queryOptions The query options to supply to the {@link Query#matches(Object, QueryOptions)} method
     * @return A predicate which can evaluate if the the query matches any given object
     */
    public static <O> Predicate<O> predicate(Query<O> query, QueryOptions queryOptions) {
        return object -> query.matches(object, queryOptions);
    }

    // ***************************************************************************************************************

    /**
     * Overloaded variant of {@link #queryOptions(Object...)}  - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static QueryOptions queryOptions(Object queryOption) {
        return queryOptions(Collections.singleton(queryOption));
    }

    /**
     * Overloaded variant of {@link #queryOptions(Object...)}  - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static QueryOptions queryOptions(Object queryOption1, Object queryOption2) {
        @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
        List<Object> queryOptions = Arrays.asList(queryOption1, queryOption2);
        return queryOptions(queryOptions);
    }
}
