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
import com.googlecode.cqengine.query.option.*;
import com.googlecode.cqengine.query.simple.*;
import com.googlecode.cqengine.query.logical.And;
import com.googlecode.cqengine.query.logical.Not;
import com.googlecode.cqengine.query.logical.Or;
import com.googlecode.cqengine.query.option.AttributeOrder;

import java.util.*;
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
