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
package com.googlecode.cqengine.codegen.support;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.List;

/**
 * Methods called by generated attributes to perform common type conversions.
 * <p/>
 * Attributes will uniformly call {@code GeneratedAttributeSupport.valueOf()}, and during compilation the compiler will
 * take care of compiling the attribute to call the most specific {@code valueOf()} method for the type of the
 * value read by the attribute automatically.
 *
 * @author Niall Gallagher
 */
public class GeneratedAttributeSupport {

    // ====== Methods for converting primitive types to wrapper types... ======
    public static Byte valueOf(byte value) { return value; }
    public static Short valueOf(short value) { return value; }
    public static Integer valueOf(int value) { return value; }
    public static Long valueOf(long value) { return value; }
    public static Float valueOf(float value) { return value; }
    public static Double valueOf(double value) { return value; }
    public static Boolean valueOf(boolean value) { return value; }
    public static Character valueOf(char value) { return value; }

    // ====== Methods for converting primitive arrays to Lists of wrapper types... ======
    public static List<Byte> valueOf(byte[] value) { return wrapArray(value); }
    public static List<Short> valueOf(short[] value) { return wrapArray(value); }
    public static List<Integer> valueOf(int[] value) { return wrapArray(value); }
    public static List<Long> valueOf(long[] value) { return wrapArray(value); }
    public static List<Float> valueOf(float[] value) { return wrapArray(value); }
    public static List<Double> valueOf(double[] value) { return wrapArray(value); }
    public static List<Boolean> valueOf(boolean[] value) { return wrapArray(value); }
    public static List<Character> valueOf(char[] value) { return wrapArray(value); }

    // ====== Method for converting an object array to a List... ======
    public static <T> List<T> valueOf(T[] value) { return wrapArray(value); }

    // ====== A no-op method for preserving a List as-is... ======
    public static <T> List<T> valueOf(List<T> value) { return value; }

    // ====== A no-op method for preserving an object as-is... ======
    public static <T> T valueOf(T value) { return value; }


    /**
     * Returns a modifiable List-based view of an array, which may be an object array or a primitive array.
     * If a primitive array is supplied, the List implementation will take care of autoboxing automatically.
     * <p/>
     * This method does not copy the array: all operations read-through and write-through to the given array.
     *
     * @param array An array of any type (primitive or object)
     * @param <T> The element type in the list returned (wrapper types in the case of primitive arrays)
     * @return A modifiable List-based view of the array
     */
    static <T> List<T> wrapArray(final Object array) {
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                @SuppressWarnings("unchecked")
                T result = (T) Array.get(array, index);
                return result;
            }
            @Override
            public int size() {
                return Array.getLength(array);
            }
            @Override
            public T set(int index, T element) {
                T existing = get(index);
                Array.set(array, index, element);
                return existing;
            }
        };
    }

    /**
     * Private constructor, not used.
     */
    GeneratedAttributeSupport() {
    }
}
