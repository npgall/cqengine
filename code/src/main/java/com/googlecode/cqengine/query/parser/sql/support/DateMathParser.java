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

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A variant of {@link com.googlecode.cqengine.query.parser.cqn.support.DateMathParser} which can be used with
 * the SQL dialect.
 *
 * @author npgall
 */
public class DateMathParser extends com.googlecode.cqengine.query.parser.cqn.support.DateMathParser {

    public DateMathParser() {
    }

    public DateMathParser(Date now) {
        super(now);
    }

    public DateMathParser(TimeZone timeZone, Locale locale) {
        super(timeZone, locale);
    }

    public DateMathParser(TimeZone timeZone, Locale locale, Date now) {
        super(timeZone, locale, now);
    }

    @Override
    protected String stripQuotes(String stringValue) {
        return StringParser.stripQuotes(stringValue);
    }
}
