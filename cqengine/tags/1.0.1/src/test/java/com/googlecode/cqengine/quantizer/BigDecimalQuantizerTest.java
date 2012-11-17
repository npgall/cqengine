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
package com.googlecode.cqengine.quantizer;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author Niall Gallagher
 */
public class BigDecimalQuantizerTest {

    @Test
    public void testWithCompressionFactor_5() throws Exception {
        Quantizer<BigDecimal> quantizer = BigDecimalQuantizer.withCompressionFactor(5);
        // Note: comparing using toString, as double comparison with epsilon would not distinguish 0.0 from -0.0...
        Assert.assertEquals("0", quantizer.getQuantizedValue(BigDecimal.valueOf(0.0)).toString());
        Assert.assertEquals("0", quantizer.getQuantizedValue(BigDecimal.valueOf(4.2)).toString());
        Assert.assertEquals("5", quantizer.getQuantizedValue(BigDecimal.valueOf(5.0)).toString());
        Assert.assertEquals("5", quantizer.getQuantizedValue(BigDecimal.valueOf(9.9)).toString());
        Assert.assertEquals("10", quantizer.getQuantizedValue(BigDecimal.valueOf(11.3)).toString());
        Assert.assertEquals("0", quantizer.getQuantizedValue(BigDecimal.valueOf(-0.0)).toString());
        Assert.assertEquals("0", quantizer.getQuantizedValue(BigDecimal.valueOf(-4.2)).toString());
        Assert.assertEquals("-5", quantizer.getQuantizedValue(BigDecimal.valueOf(-5.0)).toString());
        Assert.assertEquals("-5", quantizer.getQuantizedValue(BigDecimal.valueOf(-9.9)).toString());
        Assert.assertEquals("-10", quantizer.getQuantizedValue(BigDecimal.valueOf(-11.3)).toString());
    }

    @Test
    public void testWithCompressionFactor_1() throws Exception {
        Quantizer<BigDecimal> quantizer = BigDecimalQuantizer.withCompressionFactor(1);
        // Note: comparing using toString, as double comparison with epsilon would not distinguish 0.0 from -0.0...
        Assert.assertEquals("0", quantizer.getQuantizedValue(BigDecimal.valueOf(0.0)).toString());
        Assert.assertEquals("4", quantizer.getQuantizedValue(BigDecimal.valueOf(4.2)).toString());
        Assert.assertEquals("5", quantizer.getQuantizedValue(BigDecimal.valueOf(5.0)).toString());
        Assert.assertEquals("9", quantizer.getQuantizedValue(BigDecimal.valueOf(9.9)).toString());
        Assert.assertEquals("11", quantizer.getQuantizedValue(BigDecimal.valueOf(11.3)).toString());
        Assert.assertEquals("0", quantizer.getQuantizedValue(BigDecimal.valueOf(-0.0)).toString());
        Assert.assertEquals("-4", quantizer.getQuantizedValue(BigDecimal.valueOf(-4.2)).toString());
        Assert.assertEquals("-5", quantizer.getQuantizedValue(BigDecimal.valueOf(-5.0)).toString());
        Assert.assertEquals("-9", quantizer.getQuantizedValue(BigDecimal.valueOf(-9.9)).toString());
        Assert.assertEquals("-11", quantizer.getQuantizedValue(BigDecimal.valueOf(-11.3)).toString());
    }
}
