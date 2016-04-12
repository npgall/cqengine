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
package com.googlecode.cqengine.persistence.composite;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.sqlite.ConnectionManager;
import com.googlecode.cqengine.index.sqlite.RequestScopeConnectionManager;
import com.googlecode.cqengine.persistence.ExternalPersistence;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A Persistence object which wraps two or more backing Persistence objects.
 * <p/>
 * The collection itself will be persisted to the primary persistence object supplied to the constructor.
 *
 * @author niall.gallagher
 */
public class CompositePersistence<O> implements Persistence<O> {

    final Persistence<O> primaryPersistence;
    final Persistence<O> secondaryPersistence;
    final List<? extends Persistence<O>> additionalPersistences;

    // Cache of the Persistence object associated with each index.
    // Tuned for very little concurrency because this is caching static config.
    final ConcurrentMap<Index<O>, Persistence<O>> indexPersistences = new ConcurrentHashMap<Index<O>, Persistence<O>>(1, 1.0F, 1);

    /**
     * Creates a CompositePersistence wrapping two or more backing persistences.
     * <b>The collection itself will be persisted to the primary persistence.</b>
     *
     * @param primaryPersistence The first backing persistence to wrap, and to use to persist the collection itself.
     * @param secondaryPersistence The second backing persistence to wrap.
     * @param additionalPersistences Zero or more additional backing persistences to wrap.
     * @throws NullPointerException If any argument is null.
     * @throws IllegalArgumentException If any of the Persistence objects are not on the same primary key.
     */
    public CompositePersistence(Persistence<O> primaryPersistence, Persistence<O> secondaryPersistence, List<? extends Persistence<O>> additionalPersistences) {
        validatePersistenceArguments(primaryPersistence, secondaryPersistence, additionalPersistences);
        this.primaryPersistence = primaryPersistence;
        this.secondaryPersistence = secondaryPersistence;
        this.additionalPersistences = additionalPersistences;
    }


    @Override
    public boolean supportsIndex(Index<O> index) {
        Persistence<O> persistence = getPersistenceForIndexOrNullWithCaching(index);
        return persistence != null;
    }

    public Persistence<O> getPersistenceForIndex(Index<O> index) {
        Persistence<O> persistence = getPersistenceForIndexOrNullWithCaching(index);
        if (persistence == null) {
            throw new IllegalStateException("No persistence is configured for index: " + index);
        }
        return persistence;
    }

    @Override
    public ObjectStore<O> createObjectStore() {
        return primaryPersistence.createObjectStore();
    }

    Persistence<O> getPersistenceForIndexOrNullWithCaching(Index<O> index) {
        Persistence<O> persistence = indexPersistences.get(index);
        if (persistence == null) {
            persistence = getPersistenceForIndexOrNull(index);
            if (persistence != null) {
                Persistence<O> existing = indexPersistences.putIfAbsent(index, persistence);
                if (existing != null) {
                    persistence = existing;
                }
            }
        }
        return persistence;
    }

    Persistence<O> getPersistenceForIndexOrNull(Index<O> index) {
        if (primaryPersistence.supportsIndex(index)) {
            return primaryPersistence;
        }
        if (secondaryPersistence.supportsIndex(index)) {
            return secondaryPersistence;
        }
        for (Persistence<O> additionalPersistence : additionalPersistences) {
            if (additionalPersistence.supportsIndex(index)) {
                return additionalPersistence;
            }
        }
        return null;
    }

    public Persistence<O> getPrimaryPersistence() {
        return primaryPersistence;
    }

    public Persistence<O> getSecondaryPersistence() {
        return secondaryPersistence;
    }

    public List<? extends Persistence<O>> getAdditionalPersistences() {
        return additionalPersistences;
    }

    /**
     * Validates that all of the given Persistence objects are non-null, and validates that all Persistence objects
     * which are instances of {@link ExternalPersistence} have the same {@link ExternalPersistence#getPrimaryKeyAttribute()}.
     *
     * @param primaryPersistence A Persistence object to be validated
     * @param secondaryPersistence A Persistence object to be validated
     * @param additionalPersistences Zero or more Persistence objects to be validated
     */
    static <O> void validatePersistenceArguments(Persistence<O> primaryPersistence, Persistence<O> secondaryPersistence, List<? extends Persistence<O>> additionalPersistences) {
        SimpleAttribute<O, ?> primaryKeyAttribute;
        primaryKeyAttribute = validatePersistenceArgument(primaryPersistence, null);
        primaryKeyAttribute = validatePersistenceArgument(secondaryPersistence, primaryKeyAttribute);
        for (Persistence<O> additionalPersistence : additionalPersistences) {
            validatePersistenceArgument(additionalPersistence, primaryKeyAttribute);
        }
    }

    /**
     * Helper method for {@link #validatePersistenceArguments(Persistence, Persistence, List)}. See documentation of
     * that method for details.
     */
    static <O> SimpleAttribute<O, ?> validatePersistenceArgument(Persistence<O> persistence, SimpleAttribute<O, ?> primaryKeyAttribute) {
        if (persistence == null) {
            throw new NullPointerException("The Persistence argument cannot be null.");
        }
        if (!(persistence instanceof ExternalPersistence)) {
            return primaryKeyAttribute;
        }
        ExternalPersistence<O, ?> externalPersistence = (ExternalPersistence<O, ?>)persistence;
        if (primaryKeyAttribute == null) {
            primaryKeyAttribute = externalPersistence.getPrimaryKeyAttribute();
        }
        else if (!primaryKeyAttribute.equals(externalPersistence.getPrimaryKeyAttribute())) {
            throw new IllegalArgumentException("All ExternalPersistence implementations must be on the same primary key.");
        }
        return primaryKeyAttribute;
    }

    /**
     * Creates a new {@link RequestScopeConnectionManager} and adds it to the given query options with key
     * {@link ConnectionManager}, if an only if no object with that key is already in the query options.
     * This allows the application to supply its own implementation of {@link ConnectionManager} to override the default
     * if necessary.
     *
     * @param queryOptions The query options supplied with the request into CQEngine.
     */
    @Override
    public void openRequestScopeResources(QueryOptions queryOptions) {
        if (queryOptions.get(ConnectionManager.class) == null) {
            queryOptions.put(ConnectionManager.class, new RequestScopeConnectionManager(this));
        }
    }

    /**
     * Closes a {@link RequestScopeConnectionManager} if it is present in the given query options with key
     * {@link ConnectionManager}.
     *
     * @param queryOptions The query options supplied with the request into CQEngine.
     */
    @Override
    public void closeRequestScopeResources(QueryOptions queryOptions) {
        ConnectionManager connectionManager = queryOptions.get(ConnectionManager.class);
        if (connectionManager instanceof RequestScopeConnectionManager) {
            ((RequestScopeConnectionManager) connectionManager).close();
            queryOptions.remove(ConnectionManager.class);
        }
    }

    /**
     * Creates a CompositePersistence wrapping two or more backing persistences.
     * <b>The collection itself will be persisted to the primary persistence.</b>
     *
     * @param primaryPersistence The first backing persistence to wrap, and to use to persist the collection itself.
     * @param secondaryPersistence The second backing persistence to wrap.
     * @param additionalPersistences Zero or more additional backing persistences to wrap.
     * @throws NullPointerException If any argument is null.
     * @throws IllegalArgumentException If any of the Persistence objects are not on the same primary key.
     */
    public static <O> CompositePersistence<O> of(Persistence<O> primaryPersistence, Persistence<O> secondaryPersistence, List<? extends Persistence<O>> additionalPersistences) {
        return new CompositePersistence<O>(primaryPersistence, secondaryPersistence, additionalPersistences);
    }

    /**
     * Creates a CompositePersistence wrapping two backing persistences.
     * <b>The collection itself will be persisted to the primary persistence.</b>
     *
     * @param primaryPersistence The first backing persistence to wrap, and to use to persist the collection itself.
     * @param secondaryPersistence The second backing persistence to wrap.
     * @throws NullPointerException If any argument is null.
     * @throws IllegalArgumentException If any of the Persistence objects are not on the same primary key.
     */
    public static <O> CompositePersistence<O> of(Persistence<O> primaryPersistence, Persistence<O> secondaryPersistence) {
        return new CompositePersistence<O>(primaryPersistence, secondaryPersistence, Collections.<Persistence<O>>emptyList());
    }
}
