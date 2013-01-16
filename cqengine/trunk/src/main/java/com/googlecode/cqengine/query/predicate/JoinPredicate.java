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
package com.googlecode.cqengine.query.predicate;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.simple.SimpleQuery;

import java.util.List;

/**
 * Asserts that an attribute of a local object is contained in the values obtained from a corresponding attribute
 * of the same type in a collection of foreign objects (a ResultSet or collection of other potentially foreign objects).
 * <p/>
 * This supports the SQL equivalent of "SELECT * FROM Foo WHERE Foo.a IN (SELECT Bar.b FROM Bar WHERE ...)"; in which
 * Foo is the type of the local object, Foo.a is the attribute in the local object, and Bar.b is the attribute in
 * the foreign objects.
 * <p/>
 * <b>This class is a work in progress and is subject to change.</b>
 *
 * @author Niall Gallagher
 */
public class JoinPredicate<O, F, A> extends SimpleQuery<O, A> {

    final Iterable<F> foreignObjects;
    final Attribute<O, A> localAttribute;
    final Attribute<F, A> foreignAttribute;

    public JoinPredicate(Iterable<F> foreignObjects, Attribute<O, A> localAttribute, Attribute<F, A> foreignAttribute) {
        super(localAttribute);
        this.foreignObjects = foreignObjects;
        this.localAttribute = localAttribute;
        this.foreignAttribute = foreignAttribute;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> localAttribute, O object) {
        for (F foreignObject : foreignObjects) {
            A localAttributeValue = localAttribute.getValue(object);
            List<A> foreignAttributeValues = foreignAttribute.getValues(foreignObject);
            if (foreignAttributeValues.contains(localAttributeValue)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, A> localAttribute, O object) {
        for (F foreignObject : foreignObjects) {
            List<A> localAttributeValues = localAttribute.getValues(object);
            List<A> foreignAttributeValues = foreignAttribute.getValues(foreignObject);
            for (A localAttributeValue : localAttributeValues) {
                if (foreignAttributeValues.contains(localAttributeValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <O, F, A> JoinPredicate<O, F, A> create(Iterable<F> foreignObjects, Attribute<O, A> localAttribute, Attribute<F, A> foreignAttribute) {
        return new JoinPredicate<O, F, A>(foreignObjects, localAttribute, foreignAttribute);
    }
}
