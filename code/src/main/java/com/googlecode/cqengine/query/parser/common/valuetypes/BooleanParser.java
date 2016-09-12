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
package com.googlecode.cqengine.query.parser.common.valuetypes;

import com.googlecode.cqengine.query.parser.common.ValueParser;

/**
 * @author Niall Gallagher
 */
public class BooleanParser extends ValueParser<Boolean> {

    static final String TRUE_STR = Boolean.TRUE.toString();
    static final String FALSE_STR = Boolean.FALSE.toString();

    @Override
    public Boolean parse(Class<? extends Boolean> valueType, String stringValue) {
        if (TRUE_STR.equalsIgnoreCase(stringValue)) {
            return true;
        }
        else if (FALSE_STR.equalsIgnoreCase(stringValue)) {
            return false;
        }
        else {
            throw new IllegalStateException("Could not parse value as boolean: " + stringValue);
        }
    }
}
