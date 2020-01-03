package com.googlecode.cqengine.query;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.query.comparative.LongestPrefix;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * A special type of query which can only be evaluated by comparing objects stored in the collection with each other.
 * <p>
 * It is not possible to determine if a certain object matches a comparative query in isolation; for example by calling
 * {@link #matches(Object, QueryOptions)}. Therefore if that method is called on queries of this type, it will throw
 * an exception. Comparative queries are instead evaluated via the method {@link #getMatches(ObjectSet, QueryOptions)}.
 * <p>
 * Examples of comparative queries include {@link QueryFactory#min(Attribute)}, {@link QueryFactory#max(Attribute)}.
 * It is only possible to determine the object(s) which have the minimum or maximum values of an attribute by examining
 * all such attribute values throughout the collection (this may however be accelerated by an index). In this case,
 * the objects are compared via the {@link Comparable} interface. The object(s) whose attribute values in the collection
 * sort lower or higher (respectively) are the results of those queries.
 * <p>
 * Note that comparative queries are not always implemented via the {@link Comparable} interface. For example, the
 * {@link LongestPrefix} query examines the lengths of matched prefixes, rather
 * than the relative ordering of objects in the collection as determined by {@link Comparable}. Therefore, this
 * interface does not require that the generic type A extend Comparable per-se.
 *
 * @param <O> Type of objects in the collection
 * @param <A> Type of the attribute being examined by this query
 */
public interface ComparativeQuery<O, A> extends Query<O> {

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException always
     */
    @Override
    default boolean matches(O object, QueryOptions queryOptions) {
        throw new UnsupportedOperationException("This method is not supported on comparative queries");
    }

    /**
     * Evaluates the comparative query, returning the objects in the collection which match the query.
     * @param objectsInCollection All objects in the collection
     * @param queryOptions Optional query options to use
     * @return An iterator which provides the subset of objects in the collection which match the query
     */
    Iterable<O> getMatches(ObjectSet<O> objectsInCollection, QueryOptions queryOptions);

    /**
     * Returns the attribute to which the query relates.
     * @return the attribute to which the query relates
     */
    Attribute<O, A> getAttribute();
}
