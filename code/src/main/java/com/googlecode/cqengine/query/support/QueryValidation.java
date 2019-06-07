/**
 * Copyright 2012-2019 Niall Gallagher
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
package com.googlecode.cqengine.query.support;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Contains static helper methods for validating query arguments.
 */
public class QueryValidation {


    public static <T> T checkQueryValueNotNull(T value) {
        return Objects.requireNonNull(value, "The value supplied to a query cannot be null. To check for null values, you should instead use a has() query to check if an attribute value exists, or use a not(has()) query to check if an attribute value does not exist.");
    }

    public static <T> Class<T> checkObjectTypeNotNull(Class<T> objectType) {
        return requireNonNull(objectType, "The objectType supplied to a query cannot be null");
    }
}
