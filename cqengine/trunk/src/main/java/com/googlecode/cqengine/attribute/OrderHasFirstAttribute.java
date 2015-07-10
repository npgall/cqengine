package com.googlecode.cqengine.attribute;

import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.AttributeOrder;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Has;

/**
 * An attribute which can be used in a {@link QueryFactory#orderBy(AttributeOrder, AttributeOrder)} clause to control
 * the placement in results of objects which have and do not have values for a given delegate attribute.
 * <p/>
 * This attribute wraps a given delegate attribute, and allows results to be sorted based on whether a {@link Has} query
 * on the delegate attribute returns true or false.
 * <p/>
 * The default behaviour of CQEngine is as follows:
 * <ul>
 *     <li>
 *         When the sort order requested for a given query is {@link QueryFactory#ascending(Attribute)}, objects without
 *         values for the attribute will be returned first, followed by objects which have values for the attribute,
 *         sorted ascending by their values.
 *     </li>
 *     <li>
 *         When the sort order requested is {@link QueryFactory#descending(Attribute)}, objects with
 *         values for the attribute will be returned first sorted descending by their values, followed by objects
 *         without values for the attribute.
 *     </li>
 * </ul>
 * This attribute can alter that default behaviour:
 * <ul>
 *     <li>
 *         If the application instead requests results to be ordered first by this {@code OrderHasFirstAttribute}
 *         <i>ascending</i>, and <i>then</i> by the given attribute, then objects with values for the delegate attribute
 *         will always be returned before objects without; regardless of whether the delegate attribute is to be
 *         sorted ascending or descending.
 *     </li>
 *     <li>
 *         If the application instead requests results to be ordered first by this {@code OrderHasFirstAttribute}
 *         <i>descending</i>, and <i>then</i> by the given attribute, then objects without values for the delegate
 *         attribute will always be returned before objects with values; regardless of whether the delegate attribute
 *         is to be sorted ascending or descending.
 *     </li>
 * </ul>
 * 
 * @author niall.gallagher
 */
public class OrderHasFirstAttribute<O> extends SimpleAttribute<O, Integer> {

    final Attribute<O, ?> delegateAttribute;
    final Has<O, ?> hasQuery;

    public OrderHasFirstAttribute(Attribute<O, ?> delegateAttribute) {
        super(delegateAttribute.getObjectType(), Integer.class, "<OrderHasFirstAttribute: " + delegateAttribute.getAttributeName() + ">");
        this.delegateAttribute = delegateAttribute;
        this.hasQuery = QueryFactory.has(delegateAttribute);
    }

    public Attribute<O, ?> getDelegateAttribute() {
        return delegateAttribute;
    }

    @Override
    public Integer getValue(O object, QueryOptions queryOptions) {
        return hasQuery.matches(object, queryOptions) ? 0 : 1;
    }
}
