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
package com.googlecode.cqengine.index.unique;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.common.AbstractAttributeIndex;
import com.googlecode.cqengine.index.common.Factory;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOption;
import com.googlecode.cqengine.query.simple.Equal;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

/**
 * An index backed by a {@link ConcurrentHashMap}, which can be more efficient than {@link HashIndex} when used with
 * (and only with) attributes which uniquely identify objects (primary key-type attributes).
 * <p/>
 * This type of index does not store a set of objects matching each attribute value, but instead stores only a
 * single object for each value. This results in faster query performance, and often lower memory usage, but has some
 * trade-offs.
 * <p/>
 * This index will throw an exception if a duplicate object is detected for an existing attribute value. That condition
 * means however that inconsistencies might already have arisen between this and other indexes as a result of the
 * application's misuse of this index.
 * <p/>
 * <b>Trade-offs: {@code UniqueIndex} versus {@code HashIndex}</b>
 * <ul>
 *     <li>
 *         {@code UniqueIndex} will always use less memory than a <i>non-quantized</i> {@code HashIndex}
 *     </li>
 *     <li>
 *         {@code UniqueIndex} will not necessarily use less memory than a <i>quantized</i> {@code HashIndex}, i.e.
 *         configured with a {@link com.googlecode.cqengine.quantizer.Quantizer}
 *     </li>
 *     <li>
 *         In all cases, {@code UniqueIndex} will answer queries faster than a {@code HashIndex}
 *     </li>
 *     <li>
 *         It is important that {@code UniqueIndex} only be used with attributes which uniquely identify objects
 *     </li>
 * </ul>
 * <p/>
 * Supports query types:
 * <ul>
 *     <li>
 *         {@link Equal}
 *     </li>
 * </ul>
 * 
 * @author Kinz Liu
 * @author Niall Gallagher
 */
public class UniqueIndex<A,O> extends AbstractAttributeIndex<A,O> {

	protected static final int INDEX_RETRIEVAL_COST = 25;
	
	protected final Factory<ConcurrentMap<A,O>> indexMapFactory;
	
	protected final ConcurrentMap<A,O> indexMap;

    /**
     * Package-private constructor, used by static factory methods. Creates a new UniqueIndex initialized to index the
     * supplied attribute.
     *
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param attribute The attribute on which the index will be built
     */
	protected UniqueIndex(Factory<ConcurrentMap<A,O>> indexMapFactory, Attribute<O, A> attribute)	{
		super(attribute, new HashSet<Class<? extends Query>>() {{
            add(Equal.class);
        }});
		this.indexMapFactory = indexMapFactory;
		this.indexMap = indexMapFactory.create();
	}

    /**
     * {@inheritDoc}
     * <p/>
     * This index is mutable.
     *
     * @return true
     */
	@Override
	public boolean isMutable() {
		return true;
	}
	
	@Override
	public ResultSet<O> retrieve(Query<O> query, Map<Class<? extends QueryOption>, QueryOption<O>> queryOptions) {
		Class<?> queryClass = query.getClass();
        if (queryClass.equals(Equal.class)) 
        {
        	final ConcurrentMap<A, O> indexMap = this.indexMap;
            final Equal<O, A> equal = (Equal<O, A>) query;
            final O obj = indexMap.get(equal.getValue());
            
            return new ResultSet<O>() {
            	@Override
                public Iterator<O> iterator() {
                    return new UnmodifiableIterator<O>() {
                    	boolean hasNext = (obj != null);
                    	
						@Override
						public boolean hasNext() {
							return this.hasNext;
						}

						@Override
						public O next() {
							this.hasNext=false;
							return obj;
						}
                    };
                }
            	
                @Override
                public boolean contains(O object) {
                	return (object != null && obj != null && object.equals(obj));
                }
                
                @Override
                public int size() {
                	return obj == null ? 0 : 1;
                		
                }
                
                @Override
                public int getRetrievalCost() {
                    return INDEX_RETRIEVAL_COST;
                }
                
                @Override
                public int getMergeCost() {
                	return obj == null ? 0 : 1;
                }
            };
        }
		throw new IllegalArgumentException("Unsupported query: " + query);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObjectsAdded(Collection<O> objects) {
        ConcurrentMap<A, O> indexMap = this.indexMap;
        for (O object : objects) {
            Iterable<A> attributeValues = getAttribute().getValues(object);
            for (A attributeValue : attributeValues) {
                O existingValue = indexMap.put(attributeValue, object);
                if (existingValue != null && !existingValue.equals(object)) {
                    throw new UniqueConstraintViolatedException(
                            "The application has attempted to add a duplicate object to the UniqueIndex on attribute '"
                                + attribute.getAttributeName() +
                                "', potentially causing inconsistencies between indexes. " +
                                "UniqueIndex should not be used with attributes which do not uniquely identify objects. " +
                                "Problematic attribute value: '" + attributeValue + "', " +
                                "problematic duplicate object: " + object);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObjectsRemoved(Collection<O> objects) {
        ConcurrentMap<A, O> indexMap = this.indexMap;
        for (O object : objects) {
            Iterable<A> attributeValues = getAttribute().getValues(object);
            for (A attributeValue : attributeValues) {
                indexMap.remove(attributeValue);
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

    public static class UniqueConstraintViolatedException extends RuntimeException {
        public UniqueConstraintViolatedException(String message) {
            super(message);
        }
    }
	
    /**
     * Creates an index map using default settings.
     */
    public static class DefaultIndexMapFactory<A, O> implements Factory<ConcurrentMap<A, O>> {
        @Override
        public ConcurrentMap<A, O> create() {
            return new ConcurrentHashMap<A, O>();
        }
    }

    // ---------- Static factory methods to create UniqueIndexes ----------

    /**
     * Creates a new {@link UniqueIndex} on the specified attribute.
     * <p/>
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link UniqueIndex} on this attribute
     */
    public static <A, O> UniqueIndex<A, O> onAttribute(Attribute<O, A> attribute) {
        return onAttribute(new DefaultIndexMapFactory<A, O>(), attribute);
    }

    /**
     * Creates a new {@link UniqueIndex} on the specified attribute.
     * <p/>
     * @param indexMapFactory A factory used to create the main map-based data structure used by the index
     * @param attribute The attribute on which the index will be built
     * @param <O> The type of the object containing the attribute
     * @return A {@link UniqueIndex} on this attribute
     */
    public static <A, O> UniqueIndex<A, O> onAttribute(Factory<ConcurrentMap<A, O>> indexMapFactory, Attribute<O, A> attribute) {
        return new UniqueIndex<A, O>(indexMapFactory, attribute);
    }
}
