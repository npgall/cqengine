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
package com.googlecode.cqengine.attribute;

import com.googlecode.concurrenttrees.common.Iterables;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.cqengine.query.QueryFactory.mapAttribute;
import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;

/**
 * Created by npgall on 23/05/2016.
 */
public class SimpleNullableMapAttributeTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testSimpleNullableMapAttribute() {
        Map map = new HashMap();
        map.put("foo", 1);
        map.put("bar", 2.5);
        map.put(1.5F, "baz");

        Attribute<Map, Integer> FOO = mapAttribute("foo", Integer.class);
        Attribute<Map, Double> BAR = mapAttribute("bar", Double.class);
        Attribute<Map, String> ONE_POINT_FIVE = mapAttribute(1.5F, String.class);
        Attribute<Map, String> NON_EXISTENT = mapAttribute("foobar", String.class);


        Assert.assertEquals("[1]", Iterables.toString(FOO.getValues(map, noQueryOptions())));
        Assert.assertEquals("[2.5]", Iterables.toString(BAR.getValues(map, noQueryOptions())));
        Assert.assertEquals("[baz]", Iterables.toString(ONE_POINT_FIVE.getValues(map, noQueryOptions())));
        Assert.assertEquals("[]", Iterables.toString(NON_EXISTENT.getValues(map, noQueryOptions())));
    }


}