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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static java.util.Objects.requireNonNull;

/**
 * Asserts than an object exists in a foreign collection, based on an attribute in the local collection being equal
 * to an attribute in the foreign collection, and optionally with restrictions applied to objects in the foreign
 * collection. This supports the equivalent of SQL EXISTS-type queries and JOINs between IndexedCollections.

 * @author ngallagher
 * @since 2013-08-27 20:05
 */
public class ExistsIn<O, F, A> extends SimpleQuery<O, A> {

final IndexedCollection<F> foreignCollection;
    final Attribute<O, A> localKeyAttribute;
    final Attribute<F, A> foreignKeyAttribute;
    final Query<F> foreignRestrictions;

    public ExistsIn(IndexedCollection<F> foreignCollection, Attribute<O, A> localKeyAttribute, Attribute<F, A> foreignKeyAttribute) {
        this(foreignCollection, localKeyAttribute, foreignKeyAttribute, null);
    }

    public ExistsIn(IndexedCollection<F> foreignCollection, Attribute<O, A> localKeyAttribute, Attribute<F, A> foreignKeyAttribute, Query<F> foreignRestrictions) {
        super(requireNonNull(localKeyAttribute, "The localKeyAttribute cannot be null"));
        this.foreignCollection = requireNonNull(foreignCollection, "The foreignCollection cannot be null");
        this.localKeyAttribute = requireNonNull(localKeyAttribute, "The localKeyAttribute cannot be null");
        this.foreignKeyAttribute = requireNonNull(foreignKeyAttribute, "The foreignKeyAttribute cannot be null");
        this.foreignRestrictions = foreignRestrictions; // ..this may be null
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
        A localValue = attribute.getValue(object, queryOptions);
        return foreignRestrictions == null
                ? foreignCollectionContains(foreignCollection, equal(foreignKeyAttribute, localValue))
                : foreignCollectionContains(foreignCollection, and(equal(foreignKeyAttribute, localValue), foreignRestrictions));
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
        if (foreignRestrictions == null) {
            for (A localValue : attribute.getValues(object, queryOptions)) {
                boolean contained = foreignCollectionContains(foreignCollection, equal(foreignKeyAttribute, localValue));
                if (contained) {
                    return true;
                }
            }
            return false;
        }
        else {
            for (A localValue : attribute.getValues(object, queryOptions)) {
                boolean contained = foreignCollectionContains(foreignCollection, and(equal(foreignKeyAttribute, localValue), foreignRestrictions));
                if (contained) {
                    return true;
                }
            }
            return false;
        }

    }


    @Override
    public String toString() {
        if (foreignRestrictions == null) {
            return "existsIn(" +
                "IndexedCollection<" + foreignKeyAttribute.getObjectType().getSimpleName() + ">" +
                ", " + asLiteral(localKeyAttribute.getAttributeName()) +
                ", " + asLiteral(foreignKeyAttribute.getAttributeName()) +
                ")";
        }
        else {
            return "existsIn(" +
                "IndexedCollection<" + foreignKeyAttribute.getObjectType().getSimpleName() + ">" +
                ", " + asLiteral(localKeyAttribute.getAttributeName()) +
                ", " + asLiteral(foreignKeyAttribute.getAttributeName()) +
                ", " + foreignRestrictions +
                ")";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExistsIn)) return false;

        ExistsIn existsIn = (ExistsIn) o;

        if (!foreignKeyAttribute.equals(existsIn.foreignKeyAttribute)) return false;
        if (foreignRestrictions != null ? !foreignRestrictions.equals(existsIn.foreignRestrictions) : existsIn.foreignRestrictions != null)
            return false;
        if (!localKeyAttribute.equals(existsIn.localKeyAttribute)) return false;

        // Evaluate equals() on the foreignCollection last, to avoid performance hit if possible...
        if (!foreignCollection.equals(existsIn.foreignCollection)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        // Use identityHashCode() to avoid expensive hashCode computation in case the foreignCollection is large...
        int result = System.identityHashCode(foreignCollection);
        result = 31 * result + localKeyAttribute.hashCode();
        result = 31 * result + foreignKeyAttribute.hashCode();
        result = 31 * result + (foreignRestrictions != null ? foreignRestrictions.hashCode() : 0);
        return result;
    }

    /**
     * Checks if the given foreign collection contains objects which match the given query.
     * @param foreignCollection The foreign collection to check
     * @param query The query to check
     * @return True if the foreign collection contains one or more objects which match the query, otherwise false
     */
    static <F> boolean foreignCollectionContains(IndexedCollection<F> foreignCollection, Query<F> query) {
        ResultSet<F> resultSet = foreignCollection.retrieve(query);
        try {
            return resultSet.isNotEmpty();
        }
        finally {
            resultSet.close();
        }
    }
}
