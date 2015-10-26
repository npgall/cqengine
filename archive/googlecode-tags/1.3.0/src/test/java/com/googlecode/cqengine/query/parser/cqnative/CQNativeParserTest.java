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
package com.googlecode.cqengine.query.parser.cqnative;

import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

/**
 * @author Niall Gallagher
 */
public class CQNativeParserTest {

    @Test
    public void testParseNativeQueryStructure() {
        CQNativeParser<Car> parser = new CQNativeParser<Car>(Car.class);
        CQNativeParser.QueryStructure structure = parser.parseNativeQueryStructure("or(equal(Car.DOORS, 5), equal(Car.DOORS, 4), equal(Car.DOORS, 3))");
        System.out.println(structure.queryType);
        for (String s : structure.queryArguments) {
            System.out.println("[" + s + "]");
        }

    }
}
