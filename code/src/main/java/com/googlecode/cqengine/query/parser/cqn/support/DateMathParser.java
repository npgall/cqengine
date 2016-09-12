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
package com.googlecode.cqengine.query.parser.cqn.support;

import com.googlecode.cqengine.query.parser.common.ValueParser;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Parses date math expressions into {@link java.util.Date} objects, using {@link ApacheSolrDataMathParser}.
 * <p/>
 * Examples:
 * <pre>
 *   /HOUR
 *      ... Round to the start of the current hour
 *   /DAY
 *      ... Round to the start of the current day
 *   +2YEARS
 *      ... Exactly two years in the future from now
 *   -1DAY
 *      ... Exactly 1 day prior to now
 *   /DAY+6MONTHS+3DAYS
 *      ... 6 months and 3 days in the future from the start of
 *          the current day
 *   +6MONTHS+3DAYS/DAY
 *      ... 6 months and 3 days in the future from now, rounded
 *          down to nearest day
 * </pre>
 * See {@link ApacheSolrDataMathParser} for more details.
 *
 * @author niall.gallagher
 */
public class DateMathParser extends ValueParser<Date> {

    final TimeZone timeZone;
    final Locale locale;
    final Date now;

    public DateMathParser() {
        this(ApacheSolrDataMathParser.DEFAULT_MATH_TZ, ApacheSolrDataMathParser.DEFAULT_MATH_LOCALE, null);
    }

    public DateMathParser(Date now) {
        this(ApacheSolrDataMathParser.DEFAULT_MATH_TZ, ApacheSolrDataMathParser.DEFAULT_MATH_LOCALE, now);

    }

    public DateMathParser(TimeZone timeZone, Locale locale) {
        this(timeZone, locale, null);
    }

    public DateMathParser(TimeZone timeZone, Locale locale, Date now) {
        this.timeZone = timeZone;
        this.locale = locale;
        this.now = now;
    }

    @Override
    protected Date parse(Class<? extends Date> valueType, String stringValue) {
        try {
            ApacheSolrDataMathParser solrParser = new ApacheSolrDataMathParser();
            if (now != null) {
                solrParser.setNow(now);
            }
            return solrParser.parseMath(stripQuotes(stringValue));
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to parse date math expression: " + stringValue, e);
        }
    }

    protected String stripQuotes(String stringValue) {
        return StringParser.stripQuotes(stringValue);
    }
}
