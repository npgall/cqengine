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

/**
 * A static factory for creating {@link Quantizer}s for {@link Float} attributes.
 * <p/>
 * See {@link #withCompressionFactor(int)} for details.
 * 
 * @author Niall Gallagher
 */
public class FloatQuantizer {

    /**
     * Private constructor, not used.
     */
    FloatQuantizer() {
    }

    static class TruncatingAndCompressingQuantizer implements Quantizer<Float> {
        private final int compressionFactor;

        public TruncatingAndCompressingQuantizer(int compressionFactor) {
            if (compressionFactor < 2) {
                throw new IllegalArgumentException("Invalid compression factor, must be >= 2: " + compressionFactor);
            }
            this.compressionFactor = compressionFactor;
        }

        @Override
        public Float getQuantizedValue(Float attributeValue) {
            return (float) ((attributeValue.longValue() / compressionFactor) * compressionFactor);
        }
    }

    static class TruncatingQuantizer implements Quantizer<Float> {

        @Override
        public Float getQuantizedValue(Float attributeValue) {
            return (float) (long) attributeValue.doubleValue();
        }
    }


    /**
     * Returns a {@link Quantizer} which converts the input value to the nearest multiple of the compression
     * factor, in the direction towards zero.
     * <p/>
     * <b>Examples (compression factor 5):</b><br/>
     * <ul>
     *     <li>Input value 0.0 -> 0</li>
     *     <li>Input value 4.2 -> 0</li>
     *     <li>Input value 5.0 -> 5</li>
     *     <li>Input value 9.9 -> 5</li>
     *     <li>Input value -0.0 -> 0</li>
     *     <li>Input value -4.2 -> 0</li>
     *     <li>Input value -5.0 -> -5</li>
     *     <li>Input value -9.9 -> -5</li>
     * </ul>
     * 
     * @param compressionFactor The number of adjacent mathematical integers to coalesce to a single key. <b>Supply a
     * factor < 2 to disable compression</b> and simply truncate everything after the decimal point
     * @return A {@link Quantizer} which converts the input value to the closest multiple of the compression
     * factor, in the direction towards zero
     */
    public static Quantizer<Float> withCompressionFactor(int compressionFactor) {
        return compressionFactor < 2 ? new TruncatingQuantizer() : new TruncatingAndCompressingQuantizer(compressionFactor);
    }
}
