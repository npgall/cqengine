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
package com.googlecode.cqengine.query.simple;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.Query;

import static com.googlecode.cqengine.query.QueryFactory.*;

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

    public ExistsIn(IndexedCollection<F> foreignCollection, Attribute<O, A> localKeyAttribute, Attribute<F, A> foreignKeyAttribute, Query<F> foreignRestrictions) {
        super(localKeyAttribute);
        this.foreignCollection = foreignCollection;
        this.localKeyAttribute = localKeyAttribute;
        this.foreignKeyAttribute = foreignKeyAttribute;
        this.foreignRestrictions = foreignRestrictions;
    }

    public ExistsIn(IndexedCollection<F> foreignCollection, Attribute<O, A> localKeyAttribute, Attribute<F, A> foreignKeyAttribute) {
        super(localKeyAttribute);
        this.foreignCollection = foreignCollection;
        this.localKeyAttribute = localKeyAttribute;
        this.foreignKeyAttribute = foreignKeyAttribute;
        this.foreignRestrictions = null;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object) {
        A localValue = attribute.getValue(object);
        return foreignRestrictions == null
                ? foreignCollection.retrieve(equal(foreignKeyAttribute, localValue)).isNotEmpty()
                : foreignCollection.retrieve(and(equal(foreignKeyAttribute, localValue), foreignRestrictions)).isNotEmpty();
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object) {
        if (foreignRestrictions == null) {
            for (A localValue : attribute.getValues(object)) {
                boolean contained = foreignCollection.retrieve(equal(foreignKeyAttribute, localValue)).isNotEmpty();
                if (contained) {
                    return true;
                }
            }
            return false;
        }
        else {
            for (A localValue : attribute.getValues(object)) {
                boolean contained = foreignCollection.retrieve(and(equal(foreignKeyAttribute, localValue), foreignRestrictions)).isNotEmpty();
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
                ", " + localKeyAttribute.getObjectType().getSimpleName() + "." + localKeyAttribute.getAttributeName() +
                ", " + foreignKeyAttribute.getObjectType().getSimpleName() + "." + foreignKeyAttribute.getAttributeName() +
                ")";
        }
        else {
            return "existsIn(" +
                "IndexedCollection<" + foreignKeyAttribute.getObjectType().getSimpleName() + ">" +
                ", " + localKeyAttribute.getObjectType().getSimpleName() + "." + localKeyAttribute.getAttributeName() +
                ", " + foreignKeyAttribute.getObjectType().getSimpleName() + "." + foreignKeyAttribute.getAttributeName() +
                ", " + foreignRestrictions +
                ")";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExistsIn existsIn = (ExistsIn) o;

        if (!foreignCollection.equals(existsIn.foreignCollection)) return false;
        if (!foreignKeyAttribute.equals(existsIn.foreignKeyAttribute)) return false;
        if (foreignRestrictions != null ? !foreignRestrictions.equals(existsIn.foreignRestrictions) : existsIn.foreignRestrictions != null)
            return false;
        if (!localKeyAttribute.equals(existsIn.localKeyAttribute)) return false;

        return true;
    }

    @Override
    protected int calcHashCode() {
        int result = foreignCollection.hashCode();
        result = 31 * result + localKeyAttribute.hashCode();
        result = 31 * result + foreignKeyAttribute.hashCode();
        result = 31 * result + (foreignRestrictions != null ? foreignRestrictions.hashCode() : 0);
        return result;
    }
}
