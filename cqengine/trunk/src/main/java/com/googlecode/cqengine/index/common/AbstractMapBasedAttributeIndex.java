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
import com.googlecode.cqengine.resultset.stored.StoredResultSet;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * An abstract implementation of an index backed by a {@link java.util.concurrent.ConcurrentMap}, where the exact map
 * implementation is provided by a factory supplied to the constructor.
 * <p/>
 * This class implements the methods to actually build the index and update it when objects are added or removed.
 * Subclasses will implement methods to retrieve from the index, using logic appropriate to the particular
 * implementation.
 * <p/>
 * This class also provides some static utility methods useful to map-based implementations.
 *
 * @author Niall Gallagher
 */
public abstract class AbstractMapBasedAttributeIndex<A, O, MapType extends ConcurrentMap<A, StoredResultSet<O>>> extends AbstractAttributeIndex<A, O> {

    protected final Factory<MapType> indexMapFactory;
    protected final Factory<StoredResultSet<O>> valueSetFactory;

    protected final MapType indexMap;

    /**
     * Protected constructor, called by subclasses.
     *
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param valueSetFactory A factory used to create sets to store values in the index
     * @param attribute The attribute on which the index will be built
     * @param supportedQueries The set of {@link Query} types which the subclass implementation supports
     */
    protected AbstractMapBasedAttributeIndex(Factory<MapType> indexMapFactory, Factory<StoredResultSet<O>> valueSetFactory, Attribute<O, A> attribute, Set<Class<? extends Query>> supportedQueries) {
        super(attribute, supportedQueries);
        this.indexMapFactory = indexMapFactory;
        this.valueSetFactory = valueSetFactory;
        this.indexMap = indexMapFactory.create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObjectsAdded(Collection<O> objects) {
        ConcurrentMap<A, StoredResultSet<O>> indexMap = this.indexMap;
        for (O object : objects) {
            Iterable<A> attributeValues = getAttribute().getValues(object);
            for (A attributeValue : attributeValues) {

                // Replace attributeValue with quantized value if applicable...
                attributeValue = getQuantizedValue(attributeValue);

                // Look up StoredResultSet for the value...
                StoredResultSet<O> valueSet = indexMap.get(attributeValue);
                if (valueSet == null) {
                    // No StoredResultSet, create and add one...
                    valueSet = valueSetFactory.create();
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
        ConcurrentMap<A, StoredResultSet<O>> indexMap = this.indexMap;
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
        this.indexMap.clear();
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
