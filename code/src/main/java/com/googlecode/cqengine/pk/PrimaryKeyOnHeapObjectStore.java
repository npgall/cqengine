package com.googlecode.cqengine.pk;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.persistence.support.CollectionWrappingObjectStore;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * ObjectStore backed by index
 */
public class PrimaryKeyOnHeapObjectStore<O, A extends Comparable<A>> extends CollectionWrappingObjectStore<O> {
    private final ConcurrentMap<A, O> pkMap;

    public PrimaryKeyOnHeapObjectStore(SimpleAttribute<O, A> pkAttr) {
        super(new SetFromMap(new ConcurrentHashMap<A, O>(), pkAttr));
        pkMap = ((SetFromMap<O, A>) getBackingCollection()).getMap();
    }

    public PrimaryKeyOnHeapObjectStore(int initialCapacity, float loadFactor, int concurrencyLevel, SimpleAttribute<O, A> pkAttr) {
        super(new SetFromMap<O,A>(new ConcurrentHashMap<A, O>(initialCapacity, loadFactor, concurrencyLevel), pkAttr));
        pkMap = ((SetFromMap<O, A>) getBackingCollection()).getMap();
    }

    public ConcurrentMap<A, O> getPkMap() {
        return pkMap;
    }

    private static class SetFromMap<O, A extends Comparable<A>> extends AbstractSet<O>
            implements Set<O>, Serializable {
        // The backing map
        private final ConcurrentMap<A, O> map;
        // Its values view
        private transient Collection<O> values;
        // Attribute to read a key
        private final SimpleAttribute<O, A> attr;

        SetFromMap(ConcurrentMap<A, O> map, SimpleAttribute<O, A> attr) {
            if (!map.isEmpty())
                throw new IllegalArgumentException("Map must be empty");
            this.map = map;
            // ValuesView in ConcurrentHashMap is unique and final!
            // store the reference for faster access
            values = map.values();
            this.attr = attr;
        }

        public void clear() {
            map.clear();
        }

        public int size() {
            return map.size();
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public boolean contains(Object o) {
            return map.containsKey(attr.getValue((O)o, null));
        }

        public boolean remove(Object o) {
            return map.remove(attr.getValue((O)o, null)) != null;
        }

        public boolean add(O o) {
            return map.put(attr.getValue((O)o, null), o) == null;
        }

        public Iterator<O> iterator() {
            return values.iterator();
        }

        public Object[] toArray() {
            return values.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return values.toArray(a);
        }

        public String toString() {
            return values.toString();
        }

        public int hashCode() {
            return map.keySet().hashCode();
        }

        public boolean equals(Object o) {
            return o == this;
        }

        public boolean containsAll(Collection<?> c) {
            for (Object o : c) {
                A value = attr.getValue((O) o, null);
                if(map.get(value) == null) return false;
            }
            return true;
        }

        public boolean removeAll(Collection<?> c) {
            boolean changed = false;
            for (Object o : c) {
                A value = attr.getValue((O) o, null);
                if(map.remove(value) != null) changed = true;
            }
            return changed;
        }

        // retainAll and addAll is are inherited implementation

        // Override default methods in Collection
        @Override
        public void forEach(Consumer<? super O> action) {
            values.forEach(action);
        }

        @Override
        public boolean removeIf(Predicate<? super O> filter) {
            return values.removeIf(filter);
        }

        @Override
        public Spliterator<O> spliterator() {
            return values.spliterator();
        }

        @Override
        public Stream<O> stream() {
            return values.stream();
        }

        @Override
        public Stream<O> parallelStream() {
            return values.parallelStream();
        }

        ConcurrentMap<A, O> getMap(){
            return map;
        }

        private static final long serialVersionUID = 2454657854757543879L;

        private void readObject(java.io.ObjectInputStream stream)
                throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            values = map.values();
        }

    }
}
