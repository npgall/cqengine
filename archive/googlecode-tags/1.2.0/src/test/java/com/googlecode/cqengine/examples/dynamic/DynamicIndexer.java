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
package com.googlecode.cqengine.examples.dynamic;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.ReflectiveAttribute;
import com.googlecode.cqengine.index.navigable.NavigableIndex;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Methods to generate attributes dynamically for fields in a POJO, and to create IndexedCollections
 * configured dynamically to index these attributes.
 * <p/>
 * @author ngallagher
 * @since 2013-07-05 12:43
 */
public class DynamicIndexer {

    /**
     * Generates attributes dynamically for the fields declared in the given POJO class.
     * <p/>
     * Implementation is currently limited to generating attributes for Comparable fields (String, Integer etc.).
     *
     * @param pojoClass A POJO class
     * @param <O> Type of the POJO class
     * @return Attributes for fields in the POJO
     */
    public static <O> Map<String, Attribute<O, Comparable>> generateAttributesForPojo(Class<O> pojoClass) {
        Map<String, Attribute<O, Comparable>> generatedAttributes = new LinkedHashMap<String, Attribute<O, Comparable>>();
        for (Field field : pojoClass.getDeclaredFields()) {
            if (Comparable.class.isAssignableFrom(field.getType())) {
                @SuppressWarnings({"unchecked"})
                Class<Comparable> fieldType = (Class<Comparable>) field.getType();
                generatedAttributes.put(field.getName(), ReflectiveAttribute.forField(pojoClass, fieldType, field.getName()));
            }
        }
        return generatedAttributes;
    }

    /**
     * Creates an IndexedCollection and adds NavigableIndexes for the given attributes.
     *
     * @param attributes Attributes for which indexes should be added
     * @param <O> Type of objects stored in the collection
     * @return An IndexedCollection configured with indexes on the given attributes.
     */
    public static <O> IndexedCollection<O> newAutoIndexedCollection(Iterable<Attribute<O, Comparable>> attributes) {
        IndexedCollection<O> autoIndexedCollection = CQEngine.newInstance();
        for (Attribute<O, ? extends Comparable> attribute : attributes) {
            // Add a NavigableIndex...
            autoIndexedCollection.addIndex(NavigableIndex.onAttribute(attribute));
        }
        return autoIndexedCollection;
    }

    /**
     * Private constructor, not used.
     */
    DynamicIndexer() {
    }
}
