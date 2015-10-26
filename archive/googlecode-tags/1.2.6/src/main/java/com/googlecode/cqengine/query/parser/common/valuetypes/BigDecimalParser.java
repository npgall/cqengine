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
package com.googlecode.cqengine.query.parser.common.valuetypes;

import com.googlecode.cqengine.query.parser.common.AttributeValueParser;

import java.math.BigDecimal;

/**
 * @author Niall Gallagher
 */
public class BigDecimalParser extends AttributeValueParser<BigDecimal> {

    public BigDecimalParser() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal parseToAttributeType(String stringValue) {
        return new BigDecimal(stringValue);
    }
}
