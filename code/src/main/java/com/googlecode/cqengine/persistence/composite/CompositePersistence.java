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
import com.googlecode.cqengine.persistence.Persistence;

import java.sql.Connection;
import java.util.*;

/**
 * A Persistence object which wraps two or more backing Persistence objects.
 * <p/>
 * Delegates index-based persistence operations to the backing Persistence object which indicates it supports the given
 * index via {@link Persistence#supportsIndex(Index)}.
 * <p/>
 * For aggregate persistence operation {@link Persistence#expand(long)}, divides the bytes to expand evenly
 * between persistence objects. See that method's JavaDoc for details.
 * <p/>
 * For aggregate persistence operation {@link Persistence#compact()}, simply compacts all persistence objects.
 *
 * @author niall.gallagher
 */
public class CompositePersistence<O, A extends Comparable<A>> implements Persistence<O, A> {

    final Persistence<O, A> primaryPersistence;
    final Persistence<O, A> secondaryPersistence;
    final List<? extends Persistence<O, A>> additionalPersistences;
    final SimpleAttribute<O, A> primaryKeyAttribute;

    /**
     * Creates a CompositePersistence wrapping two or more backing peristences.
     * @param primaryPersistence The first backing persistence to wrap.
     * @param secondaryPersistence The second backing persistence to wrap.
     * @param additionalPersistences Zero or more additional backing persistences to wrap.
     * @throws NullPointerException If any argument is null.
     * @throws IllegalArgumentException If any of the Persistence objects are not on the same primary key.
     */
    public CompositePersistence(Persistence<O, A> primaryPersistence, Persistence<O, A> secondaryPersistence, List<? extends Persistence<O, A>> additionalPersistences) {
        validateBackingPersistences(primaryPersistence, secondaryPersistence, additionalPersistences);
        this.primaryPersistence = primaryPersistence;
        this.secondaryPersistence = secondaryPersistence;
        this.additionalPersistences = additionalPersistences;
        this.primaryKeyAttribute = primaryPersistence.getPrimaryKeyAttribute();
    }

    @Override
    public SimpleAttribute<O, A> getPrimaryKeyAttribute() {
        return primaryKeyAttribute;
    }

    /**
     * @return The sum of {@link Persistence#getBytesUsed()} reported by the wrapped Persistence objects.
     */
    @Override
    public long getBytesUsed() {
        long totalBytesUsed = primaryPersistence.getBytesUsed();
        totalBytesUsed += secondaryPersistence.getBytesUsed();
        for (Persistence<O, A> additionalPersistence : additionalPersistences) {
            totalBytesUsed += additionalPersistence.getBytesUsed();
        }
        return totalBytesUsed;
    }

    /**
     * Calls {@link Persistence#compact()} on all wrapped Persistence objects.
     */
    @Override
    public void compact() {
        primaryPersistence.compact();
        secondaryPersistence.compact();
        for (Persistence<O, A> additionalPersistence : additionalPersistences) {
            additionalPersistence.compact();
        }
    }

    /**
     * Divides the given {@code totalBytesToExpand} by the number of wrapped persistence objects to determine
     * {@code bytesToExpandPerPersistence}, and calls {@link Persistence#expand(long)} on each wrapped Persistence
     * object supplying that value.
     * <p/>
     * If there are any remainder bytes which did not divide evenly, expands the primary Persistence object by the
     * remainder bytes as well.
     */
    @Override
    public void expand(long totalBytesToExpand) {
        final int numPersistences = 2 + additionalPersistences.size();
        final long bytesToExpandPerPersistence = totalBytesToExpand / numPersistences;
        final long remainderBytes = totalBytesToExpand % numPersistences;
        primaryPersistence.expand(bytesToExpandPerPersistence + remainderBytes);
        secondaryPersistence.expand(bytesToExpandPerPersistence);
        for (Persistence<O, A> additionalPersistence : additionalPersistences) {
            additionalPersistence.expand(bytesToExpandPerPersistence);
        }
    }

    @Override
    public Connection getConnection(Index<?> index) {
        Persistence<O, A> persistence = getPersistenceForIndex(index, primaryPersistence, secondaryPersistence, additionalPersistences);
        return persistence.getConnection(index);
    }

    @Override
    public boolean supportsIndex(Index<?> index) {
        Persistence<O, A> persistence = getPersistenceForIndexOrNull(index, primaryPersistence, secondaryPersistence, additionalPersistences);
        return persistence != null;
    }

    @Override
    public boolean isApplyUpdateForIndexEnabled(Index<?> index) {
        Persistence<O, A> persistence = getPersistenceForIndex(index, primaryPersistence, secondaryPersistence, additionalPersistences);
        return persistence.isApplyUpdateForIndexEnabled(index);
    }

    @Override
    public Set<O> create() {
        return primaryPersistence.create();
    }

    static <O, A extends Comparable<A>> Persistence<O, A> getPersistenceForIndex(Index<?> index, Persistence<O, A> primaryPersistence, Persistence<O, A> secondaryPersistence, List<? extends Persistence<O, A>> additionalPersistences) {
        Persistence<O, A> persistence = getPersistenceForIndexOrNull(index, primaryPersistence, secondaryPersistence, additionalPersistences);
        if (persistence == null) {
            throw new IllegalStateException("No persistence is configured for index: " + index);
        }
        return persistence;
    }

    static <O, A extends Comparable<A>> Persistence<O, A> getPersistenceForIndexOrNull(Index<?> index, Persistence<O, A> primaryPersistence, Persistence<O, A> secondaryPersistence, List<? extends Persistence<O, A>> additionalPersistences) {
        if (primaryPersistence.supportsIndex(index)) {
            return primaryPersistence;
        }
        if (secondaryPersistence.supportsIndex(index)) {
            return secondaryPersistence;
        }
        for (Persistence<O, A> additionalPersistence : additionalPersistences) {
            if (additionalPersistence.supportsIndex(index)) {
                return additionalPersistence;
            }
        }
        return null;
    }

    static <O, A extends Comparable<A>> void validateBackingPersistences(Persistence<O, A> primaryPersistence, Persistence<O, A> secondaryPersistence, List<? extends Persistence<O, A>> additionalPersistences) {
        final SimpleAttribute<O, A> primaryKeyAttribute = primaryPersistence.getPrimaryKeyAttribute();
        if (!primaryKeyAttribute.equals(secondaryPersistence.getPrimaryKeyAttribute())) {
            throw new IllegalArgumentException("All persistence implementations must be on the same primary key.");
        }
        for (Persistence<O, A> additionalPersistence : additionalPersistences) {
            if (!primaryKeyAttribute.equals(additionalPersistence.getPrimaryKeyAttribute())) {
                throw new IllegalArgumentException("All persistence implementations must be on the same primary key.");
            }
        }
    }
}
