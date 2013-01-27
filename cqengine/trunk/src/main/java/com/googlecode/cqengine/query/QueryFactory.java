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
package com.googlecode.cqengine.query;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.DeduplicationOption;
import com.googlecode.cqengine.query.option.DeduplicationStrategy;
import com.googlecode.cqengine.query.option.OrderByOption;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.query.simple.*;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.Not;
import com.googlecode.cqengine.query.logical.Or;

import java.util.*;

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
     * A shorthand way to create an {@link Or} query comprised of several {@link Equal} queries.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValues The potential values for the {@link Equal} queries to be asserted by the {@link Or} query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link Or} query comprised of several {@link Equal} queries
     */
    public static <O, A> Or<O> in(Attribute<O, A> attribute, A... attributeValues) {
        return in(attribute, Arrays.asList(attributeValues));
    }

    /**
     * A shorthand way to create an {@link Or} query comprised of several {@link Equal} queries.
     * <p/>
     * Note that <b><u>this can result in more efficient queries</u></b> than several {@link Equal} queries "OR"ed together
     * using other means.
     * <p/>
     * If the given attribute is a {@link SimpleAttribute}, this method will set a hint in the query to
     * indicate that results for the child queries will inherently be disjoint and so will not require deduplication.
     *
     * @param attribute The attribute to which the query refers
     * @param attributeValues The potential values for the {@link Equal} queries to be asserted by the {@link Or} query
     * @param <A> The type of the attribute
     * @param <O> The type of the object containing the attribute
     * @return An {@link Or} query comprised of several {@link Equal} queries
     */
    public static <O, A> Or<O> in(Attribute<O, A> attribute, Collection<A> attributeValues) {
        List<Query<O>> equalStatements = new ArrayList<Query<O>>(attributeValues.size());
        for (A attributeValue : attributeValues) {
            Equal<O, A> equalStatement = equal(attribute, attributeValue);
            equalStatements.add(equalStatement);
        }
        return new Or<O>(equalStatements, attribute instanceof SimpleAttribute);
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
     * Creates an {@link Has} query which asserts that an attribute has a value (is not null).
     * <p/>
     * Asserts that an attribute has a value (is not null).
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
     * @param queries The child queries to be connected via a logical AND
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link And} query, representing a logical AND on child queries
     */
    public static <O> And<O> and(Query<O>... queries) {
        return new And<O>(Arrays.asList(queries));
    }

    /**
     * Creates an {@link And} query, representing a logical AND on child queries, which when evaluated yields the
     * <u>set intersection</u> of the result sets from child queries.
     *
     * @param queries The child queries to be connected via a logical AND
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link And} query, representing a logical AND on child queries
     */
    public static <O> And<O> and(Collection<Query<O>> queries) {
        return new And<O>(queries);
    }

    /**
     * Creates an {@link Or} query, representing a logical OR on child queries, which when evaluated yields the
     * <u>set union</u> of the result sets from child queries.
     *
     * @param queries The child queries to be connected via a logical OR
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link Or} query, representing a logical OR on child queries
     */
    public static <O> Or<O> or(Query<O>... queries) {
        return new Or<O>(Arrays.asList(queries));
    }

    /**
     * Creates an {@link Or} query, representing a logical OR on child queries, which when evaluated yields the
     * <u>set union</u> of the result sets from child queries.
     *
     * @param queries The child queries to be connected via a logical OR
     * @param <O> The type of the object containing attributes to which child queries refer
     * @return An {@link Or} query, representing a logical OR on child queries
     */
    public static <O> Or<O> or(Collection<Query<O>> queries) {
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
        Attribute<O, Boolean> exists = new SimpleAttribute<O, Boolean>("existsInForeignCollection") {
            public Boolean getValue(O localObject) {
                for (A localValue : localKeyAttribute.getValues(localObject)) {
                    boolean contained = foreignCollection.retrieve(equal(foreignKeyAttribute, localValue)).isNotEmpty();
                    if (contained) {
                        return true;
                    }
                }
                return false;
            }
        };
        return equal(exists, true);
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
        Attribute<O, Boolean> exists = new SimpleAttribute<O, Boolean>((Class<O>)localKeyAttribute.getObjectType(), Boolean.class, "existsInForeignCollection_with_restriction") {
            public Boolean getValue(O localObject) {
                for (A localValue : localKeyAttribute.getValues(localObject)) {
                    boolean contained = foreignCollection.retrieve(
                            and(equal(foreignKeyAttribute, localValue), foreignRestrictions)).isNotEmpty();
                    if (contained) {
                        return true;
                    }
                }
                return false;
            }
        };
        return equal(exists, true);
    }

    /**
     * Creates an {@link OrderByOption} query option, encapsulating the given list of attributes, which when supplied to
     * the query engine requests it to sort in ascending order result objects based on values in their fields referenced
     * by these attributes.
     *
     * @param attributes The list of attributes by which objects should be sorted
     * @param <O> The type of the object containing the attributes
     * @return An {@link OrderByOption} query option, requests results to be returned in ascending sort order
     */
    public static <O> QueryOption<O> orderBy(Attribute<O, ? extends Comparable>... attributes) {
        return new OrderByOption<O>(Arrays.asList(attributes), false);
    }

    /**
     * Creates an {@link OrderByOption} query option, encapsulating the given list of attributes, which when supplied to
     * the query engine requests it to sort in ascending order result objects based on values in their fields referenced
     * by these attributes.
     *
     * @param attributes The list of attributes by which objects should be sorted
     * @param <O> The type of the object containing the attributes
     * @return An {@link OrderByOption} query option, requests results to be returned in ascending sort order
     */
    public static <O> QueryOption<O> orderBy(List<Attribute<O, ? extends Comparable>> attributes) {
        return new OrderByOption<O>(attributes, false);
    }

    /**
     * Creates an {@link OrderByOption} query option, encapsulating the given list of attributes, which when supplied to
     * the query engine requests it to sort in descending order result objects based on values in their fields
     * referenced by these attributes.
     *
     * @param attributes The list of attributes by which objects should be sorted
     * @param <O> The type of the object containing the attributes
     * @return An {@link OrderByOption} query option, requests results to be returned in descending sort order
     */
    public static <O> QueryOption<O> orderByDescending(Attribute<O, ? extends Comparable>... attributes) {
        return new OrderByOption<O>(Arrays.asList(attributes), true);
    }

    /**
     * Creates an {@link OrderByOption} query option, encapsulating the given list of attributes, which when supplied to
     * the query engine requests it to sort in descending order result objects based on values in their fields
     * referenced by these attributes.
     *
     * @param attributes The list of attributes by which objects should be sorted
     * @param <O> The type of the object containing the attributes
     * @return An {@link OrderByOption} query option, requests results to be returned in descending sort order
     */
    public static <O> QueryOption<O> orderByDescending(List<Attribute<O, ? extends Comparable>> attributes) {
        return new OrderByOption<O>(attributes, true);
    }

    /**
     * Creates a {@link DeduplicationOption} query option, encapsulating a given {@link DeduplicationStrategy}, which
     * when supplied to the query engine requests it to eliminate duplicates objects from the results returned using
     * the strategy indicated.
     *
     * @param deduplicationStrategy The deduplication strategy the query engine should use
     * @param <O> The type of the object containing the attributes
     * @return A {@link DeduplicationOption} query option, requests duplicate objects to be eliminated from results
     */
    public static <O> QueryOption<O> deduplicate(DeduplicationStrategy deduplicationStrategy) {
        return new DeduplicationOption<O>(deduplicationStrategy);
    }

    /**
     * A convenience method to group several {@link QueryOption} objects together in a single map. This map can
     * then be supplied directly to the
     * {@link com.googlecode.cqengine.engine.QueryEngine#retrieve(Query, Map)}  method.
     *
     * @param queryOptions The {@link QueryOption} objects to wrap in the map
     * @param <O> The type of the objects referenced by the query
     * @return A list of {@link QueryOption} objects
     */
    public static <O> Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions(QueryOption<O>... queryOptions) {
        return queryOptions(Arrays.asList(queryOptions));
    }

    /**
     * A convenience method to group several {@link QueryOption} objects together in a single map. This map can
     * then be supplied directly to the
     * {@link com.googlecode.cqengine.engine.QueryEngine#retrieve(Query, Map)}  method.
     *
     * @param queryOptions The {@link QueryOption} objects to wrap in the map
     * @param <O> The type of the objects referenced by the query
     * @return A list of {@link QueryOption} objects
     */
    public static <O> Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions(Collection<QueryOption<O>> queryOptions) {
        Map<Class<? extends QueryOption>, QueryOption<O>> map = new HashMap<Class<? extends QueryOption>, QueryOption<O>>(queryOptions.size());
        for (QueryOption<O> queryOption : queryOptions) {
            map.put(queryOption.getClass(), queryOption);
        }
        return map;
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
     * Overloaded variant of {@link #and(Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> And<O> and(Query<O> query1) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Collections.singleton(query1);
        return new And<O>(queries);
    }

    /**
     * Overloaded variant of {@link #and(Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> And<O> and(Query<O> query1, Query<O> query2) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2);
        return new And<O>(queries);
    }

    /**
     * Overloaded variant of {@link #and(Query[])} - see that method for details.
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
     * Overloaded variant of {@link #and(Query[])} - see that method for details.
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
     * Overloaded variant of {@link #and(Query[])} - see that method for details.
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
     * Overloaded variant of {@link #or(Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> Or<O> or(Query<O> query1) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Collections.singleton(query1);
        return new Or<O>(queries);
    }

    /**
     * Overloaded variant of {@link #or(Query[])} - see that method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> Or<O> or(Query<O> query1, Query<O> query2) {
        @SuppressWarnings({"unchecked"})
        Collection<Query<O>> queries = Arrays.asList(query1, query2);
        return new Or<O>(queries);
    }

    /**
     * Overloaded variant of {@link #or(Query[])} - see that method for details.
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
     * Overloaded variant of {@link #or(Query[])} - see that method for details.
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
     * Overloaded variant of {@link #or(Query[])} - see that method for details.
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
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.attribute.Attribute[])} - see that method for
     * details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderBy(Attribute<O, ? extends Comparable> attribute) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Collections.<Attribute<O, ? extends Comparable>>singletonList(attribute);
        return new OrderByOption<O>(attributes, false);
    }
    
    /**
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.attribute.Attribute[])} - see that method for
     * details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderBy(Attribute<O, ? extends Comparable> attribute1, Attribute<O, ? extends Comparable> attribute2) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Arrays.asList(attribute1, attribute2);
        return new OrderByOption<O>(attributes, false);
    }
    
    /**
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.attribute.Attribute[])} - see that method for
     * details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderBy(Attribute<O, ? extends Comparable> attribute1, Attribute<O, ? extends Comparable> attribute2, Attribute<O, ? extends Comparable> attribute3) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Arrays.asList(attribute1, attribute2, attribute3);
        return new OrderByOption<O>(attributes, false);
    }
    
    /**
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.attribute.Attribute[])} - see that method for
     * details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderBy(Attribute<O, ? extends Comparable> attribute1, Attribute<O, ? extends Comparable> attribute2, Attribute<O, ? extends Comparable> attribute3, Attribute<O, ? extends Comparable> attribute4) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Arrays.asList(attribute1, attribute2, attribute3, attribute4);
        return new OrderByOption<O>(attributes, false);
    }
    
    /**
     * Overloaded variant of {@link #orderBy(com.googlecode.cqengine.attribute.Attribute[])} - see that method for
     * details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderBy(Attribute<O, ? extends Comparable> attribute1, Attribute<O, ? extends Comparable> attribute2, Attribute<O, ? extends Comparable> attribute3, Attribute<O, ? extends Comparable> attribute4, Attribute<O, ? extends Comparable> attribute5) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Arrays.asList(attribute1, attribute2, attribute3, attribute4, attribute5);
        return new OrderByOption<O>(attributes, false);
    }

    // ***************************************************************************************************************

    /**
     * Overloaded variant of {@link #orderByDescending(com.googlecode.cqengine.attribute.Attribute[])}  - see that
     * method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderByDescending(Attribute<O, ? extends Comparable> attribute) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Collections.<Attribute<O, ? extends Comparable>>singletonList(attribute);
        return new OrderByOption<O>(attributes, true);
    }

    /**
     * Overloaded variant of {@link #orderByDescending(com.googlecode.cqengine.attribute.Attribute[])}  - see that
     * method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderByDescending(Attribute<O, ? extends Comparable> attribute1, Attribute<O, ? extends Comparable> attribute2) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Arrays.asList(attribute1, attribute2);
        return new OrderByOption<O>(attributes, true);
    }

    /**
     * Overloaded variant of {@link #orderByDescending(com.googlecode.cqengine.attribute.Attribute[])}  - see that
     * method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderByDescending(Attribute<O, ? extends Comparable> attribute1, Attribute<O, ? extends Comparable> attribute2, Attribute<O, ? extends Comparable> attribute3) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Arrays.asList(attribute1, attribute2, attribute3);
        return new OrderByOption<O>(attributes, true);
    }

    /**
     * Overloaded variant of {@link #orderByDescending(com.googlecode.cqengine.attribute.Attribute[])}  - see that
     * method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderByDescending(Attribute<O, ? extends Comparable> attribute1, Attribute<O, ? extends Comparable> attribute2, Attribute<O, ? extends Comparable> attribute3, Attribute<O, ? extends Comparable> attribute4) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Arrays.asList(attribute1, attribute2, attribute3, attribute4);
        return new OrderByOption<O>(attributes, true);
    }

    /**
     * Overloaded variant of {@link #orderByDescending(com.googlecode.cqengine.attribute.Attribute[])}  - see that
     * method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> QueryOption<O> orderByDescending(Attribute<O, ? extends Comparable> attribute1, Attribute<O, ? extends Comparable> attribute2, Attribute<O, ? extends Comparable> attribute3, Attribute<O, ? extends Comparable> attribute4, Attribute<O, ? extends Comparable> attribute5) {
        @SuppressWarnings({"unchecked"})
        List<Attribute<O, ? extends Comparable>> attributes = Arrays.asList(attribute1, attribute2, attribute3, attribute4, attribute5);
        return new OrderByOption<O>(attributes, true);
    }

    // ***************************************************************************************************************

    /**
     * Overloaded variant of {@link #queryOptions(com.googlecode.cqengine.query.option.QueryOption[])}  - see that
     * method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions(QueryOption<O> queryOption) {
        return queryOptions(Collections.singleton(queryOption));
    }

    /**
     * Overloaded variant of {@link #queryOptions(com.googlecode.cqengine.query.option.QueryOption[])}  - see that
     * method for details.
     * <p/>
     * Note: This method is unnecessary as of Java 7, and is provided only for backward compatibility with Java 6 and
     * earlier, to eliminate generic array creation warnings output by the compiler in those versions.
     */
    @SuppressWarnings({"JavaDoc"})
    public static <O> Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions(QueryOption<O> queryOption1, QueryOption<O> queryOption2) {
        @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
        List<QueryOption<O>> queryOptions = Arrays.asList(queryOption1, queryOption2);
        return queryOptions(queryOptions);
    }
}
