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
package com.googlecode.cqengine.query.parser.sql.support;

import com.googlecode.cqengine.query.parser.common.ValueParser;

/**
 * @author Niall Gallagher
 */
public class StringParser extends ValueParser<String> {

    @Override
    public String parse(Class<? extends String> valueType, String stringValue) {
        return stripQuotes(stringValue);
    }

    public static String stripQuotes(String stringValue) {
        int length = stringValue.length();
        // Strip leading and trailing single quotes...
        if (length >= 2 && stringValue.charAt(0) == '\'' && stringValue.charAt(length - 1) == '\'') {
            stringValue = stringValue.substring(1, length - 1);
        }
        // Convert double single quotes (which is how a single quote is escaped), to one single quote...
        stringValue = stringValue.replace("''", "'");
        return stringValue;
    }
}
