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
package com.googlecode.cqengine.entity;

import com.googlecode.cqengine.testutil.Car;

import java.util.List;
import java.util.Map;

import static com.googlecode.cqengine.query.QueryFactory.primaryKeyedMapEntity;

/**
 * Validates general functionality using PrimaryKeyedMapEntity as collection element - indexes, query engine, ordering
 * results.
 *
 * @author Niall Gallagher
 */
public class PrimaryKeyedMapEntityTest extends MapEntityTest {

    @Override
    public void testMapFunctionality() {
        super.testMapFunctionality();
    }

    protected Map buildNewCar(int carId, String manufacturer, String model, Car.Color color, int doors, double price, List<String> features) {
        return primaryKeyedMapEntity(createMap(carId, manufacturer, model, color, doors, price, features), "CAR_ID");
    }
}
