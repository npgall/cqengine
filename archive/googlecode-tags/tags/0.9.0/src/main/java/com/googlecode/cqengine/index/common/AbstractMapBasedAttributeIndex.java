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
package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * An abstract implementation of an index backed by a {@link java.util.concurrent.ConcurrentMap}, where the exact map
 * implementation is provided by subclasses.
 * <p/>
 * This class implements the methods to actually build the index and update it when objects are added or removed.
 * Subclasses will implement methods to retrieve from the index, using logic appropriate to the particular
 * implementation.
 * <p/>
 * This class also provides some static utility methods useful to map-based implementations.
 *
 * @author Niall Gallagher
 */
public abstract class AbstractMapBasedAttributeIndex<A, O> extends AbstractAttributeIndex<A, O> {

    public AbstractMapBasedAttributeIndex(Attribute<O, A> attribute, Set<Class<? extends Query>> supportedQueries) {
        super(attribute, supportedQueries);
    }

    /**
     * Returns a new subclass-specific implementation of a ResultSet which will be a value in the map-based index, to
     * hold a set of objects matching a particular value of an attribute.
     *
     * @return A new subclass-specific implementation of a ResultSet which will be a value in the map-based index, to
     * hold a set of objects matching a particular value of an attribute
     */
    public abstract StoredResultSet<O> createValueSet();

    /**
     * Returns the instance of a particular implementation of a {@link java.util.concurrent.ConcurrentMap} maintained
     * by the subclass, which comprises the main data structure of this index.
     *
     * @return A new instance of a subclass-specific implementation of a {@link java.util.concurrent.ConcurrentMap},
     * which comprises the main data structure of this index
     */
    public abstract ConcurrentMap<A, StoredResultSet<O>> getIndexMap();

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObjectsAdded(Collection<O> objects) {
        ConcurrentMap<A, StoredResultSet<O>> indexMap = getIndexMap();
        for (O object : objects) {
            Iterable<A> attributeValues = getAttribute().getValues(object);
            for (A attributeValue : attributeValues) {

                // Replace attributeValue with quantized value if applicable...
                attributeValue = getQuantizedValue(attributeValue);

                // Look up StoredResultSet for the value...
                StoredResultSet<O> valueSet = indexMap.get(attributeValue);
                if (valueSet == null) {
                    // No StoredResultSet, create and add one...
                    valueSet = createValueSet();
                    StoredResultSet<O> existingValueSet = indexMap.putIfAbsent(attributeValue, valueSet);
                    if (existingValueSet != null) {
                        // Another thread won race to add new value set, use that one...
                        valueSet = existingValueSet;
                    }
                }
                // Add the object to the StoredResultSet for this value...
                valueSet.add(object);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObjectsRemoved(Collection<O> objects) {
        ConcurrentMap<A, StoredResultSet<O>> indexMap = getIndexMap();
        for (O object : objects) {
            Iterable<A> attributeValues = getAttribute().getValues(object);
            for (A attributeValue : attributeValues) {

                // Replace attributeValue with quantized value if applicable...
                attributeValue = getQuantizedValue(attributeValue);

                StoredResultSet<O> valueSet = indexMap.get(attributeValue);
                if (valueSet == null) {
                    continue;
                }
                valueSet.remove(object);
                if (valueSet.isEmpty()) {
                    indexMap.remove(attributeValue);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Set<O> collection) {
        notifyObjectsAdded(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObjectsCleared() {
        getIndexMap().clear();
    }


    // ---------- Hook methods which can be overridden by indexes using a Quantizer ----------

    /**
     * A no-op method which may be overridden by subclasses which use a
     * {@link com.googlecode.cqengine.quantizer.Quantizer}.
     * <p/>
     * <b>This default implementation simply returns the given attribute value unmodified.</b>
     *
     * @param attributeValue A value returned by an attribute
     * @return A quantized version of the attribute value
     */
    protected A getQuantizedValue(A attributeValue) {
        return attributeValue;
    }
}
