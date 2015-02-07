package com.googlecode.cqengine.index.common;

import com.googlecode.cqengine.resultset.iterator.UnmodifiableIterator;

import java.util.*;

/**
 * A read-only view of a NavigableSet.
 * <p/>
 * This is provided for compatibility with Java 6 which lacks a {@code Collections#unmodifiableNavigableSet()} method.
 */
public class UnmodifiableNavigableSet<E> implements NavigableSet<E> {

    private final NavigableSet<E> delegate;

    public UnmodifiableNavigableSet(NavigableSet<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public E lower(E e) {
        return delegate.lower(e);
    }

    @Override
    public E floor(E e) {
        return delegate.floor(e);
    }

    @Override
    public E ceiling(E e) {
        return delegate.ceiling(e);
    }

    @Override
    public E higher(E e) {
        return delegate.higher(e);
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    private transient UnmodifiableNavigableSet<E> descendingSet;

    @Override
    public NavigableSet<E> descendingSet() {
        UnmodifiableNavigableSet<E> result = descendingSet;
        if (result == null) {
            result = descendingSet = new UnmodifiableNavigableSet<E>(
                    delegate.descendingSet());
            result.descendingSet = this;
        }
        return result;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new UnmodifiableIterator<E>() {
            Iterator<E> i = delegate.descendingIterator();
            @Override
            public boolean hasNext() {
                return i.hasNext();
            }
            @Override
            public E next() {
                return i.next();
            }
        };
    }

    @Override
    public NavigableSet<E> subSet(
            E fromElement,
            boolean fromInclusive,
            E toElement,
            boolean toInclusive) {
        return new UnmodifiableNavigableSet<E>(delegate.subSet(
                fromElement,
                fromInclusive,
                toElement,
                toInclusive));
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return new UnmodifiableNavigableSet<E>(delegate.headSet(toElement, inclusive));
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return new UnmodifiableNavigableSet<E>(
                delegate.tailSet(fromElement, inclusive));
    }

    @Override
    public Iterator<E> iterator() {
        return new UnmodifiableIterator<E>() {
            Iterator<E> i = delegate.iterator();
            @Override
            public boolean hasNext() {
                return i.hasNext();
            }
            @Override
            public E next() {
                return i.next();
            }
        };
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return Collections.unmodifiableSortedSet(delegate.subSet(fromElement, toElement));
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return Collections.unmodifiableSortedSet(delegate.headSet(toElement));
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return Collections.unmodifiableSortedSet(delegate.tailSet(fromElement));
    }

    @Override
    public Comparator<? super E> comparator() {
        return delegate.comparator();
    }

    @Override
    public E first() {
        return delegate.first();
    }

    @Override
    public E last() {
        return delegate.first();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}