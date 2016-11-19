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

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@SuppressWarnings("deprecation")
public class GeneratedAttributeSupportTest {

    @Test
    public void testAsList_PrimitiveArray() throws Exception {
        int[] input = new int[] {1, 2, 3, 4, 5};
        List<Integer> list = GeneratedAttributeSupport.valueOf(input);
        Assert.assertEquals(5, list.size());
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(Integer.valueOf(input[i]), list.get(i));
        }

        list.set(2, 7);
        Assert.assertEquals(Integer.valueOf(7), list.get(2));
        Assert.assertEquals(7, input[2]);
    }

    @Test
    public void testAsList_ObjectArray() throws Exception {
        Integer[] input = new Integer[] {1, 2, 3, 4, 5};
        List<Integer> list = GeneratedAttributeSupport.valueOf(input);
        Assert.assertEquals(5, list.size());
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(input[i], list.get(i));
        }

        list.set(2, 7);
        Assert.assertEquals(Integer.valueOf(7), list.get(2));
        Assert.assertEquals(Integer.valueOf(7), input[2]);
    }

    @Test
    public void testValueOfPrimitiveMethods() {
        assertEquals(Byte.valueOf((byte)5), GeneratedAttributeSupport.valueOf((byte)5));
        assertEquals(Short.valueOf((short)5), GeneratedAttributeSupport.valueOf((short)5));
        assertEquals(Integer.valueOf(5), GeneratedAttributeSupport.valueOf(5));
        assertEquals(Long.valueOf(5L), GeneratedAttributeSupport.valueOf(5L));
        assertEquals(Float.valueOf(5.0F), GeneratedAttributeSupport.valueOf(5.0F));
        assertEquals(Double.valueOf(5.0), GeneratedAttributeSupport.valueOf(5.0));
        assertEquals(Boolean.TRUE, GeneratedAttributeSupport.valueOf(true));
        assertEquals(Character.valueOf('c'), GeneratedAttributeSupport.valueOf('c'));
    }

    @Test
    public void testValueOfPrimitiveArrayMethods() {
        assertEquals(asList((byte)5, (byte)6), GeneratedAttributeSupport.valueOf(new byte[]{(byte)5, (byte)6}));
        assertEquals(asList((short) 5, (short) 6), GeneratedAttributeSupport.valueOf(new short[]{(short)5, (short)6}));
        assertEquals(asList(5, 6), GeneratedAttributeSupport.valueOf(new int[]{5, 6}));
        assertEquals(asList(5L, 6L), GeneratedAttributeSupport.valueOf(new long[]{5L, 6L}));
        assertEquals(asList(5.0F, 6.0F), GeneratedAttributeSupport.valueOf(new float[]{5.0F, 6.0F}));
        assertEquals(asList(5.0, 6.0), GeneratedAttributeSupport.valueOf(new double[]{5.0, 6.0}));
        assertEquals(asList(true, false), GeneratedAttributeSupport.valueOf(new boolean[]{true, false}));
        assertEquals(asList('c', 'd'), GeneratedAttributeSupport.valueOf(new char[]{'c', 'd'}));
    }

    @Test
    public void testValueOfObjectArray() {
        assertEquals(asList("a", "b"), GeneratedAttributeSupport.valueOf(new String[]{"a", "b"}));
    }

    @Test
    public void testValueOfList() {
        ArrayList<String> input = new ArrayList<String>(asList("a", "b"));
        assertSame(input, GeneratedAttributeSupport.valueOf(input));
    }

    @Test
    public void testValueOfObject() {
        Object input = new Object();
        assertSame(input, GeneratedAttributeSupport.valueOf(input));
    }

}