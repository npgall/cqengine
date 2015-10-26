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
 * A static factory for creating {@link Quantizer}s for {@link Long} attributes.
 * <p/>
 * See {@link #withCompressionFactor(int)} for details.
 * 
 * @author Niall Gallagher
 */
public class LongQuantizer {

    static class CompressingQuantizer implements Quantizer<Long> {

        private final int compressionFactor;

        public CompressingQuantizer(int compressionFactor) {
            if (compressionFactor < 2) {
                throw new IllegalArgumentException("Invalid compression factor, must be >= 2: " + compressionFactor);
            }
            this.compressionFactor = compressionFactor;
        }

        @Override
        public Long getQuantizedValue(Long attributeValue) {
            return (attributeValue / compressionFactor) * compressionFactor;
        }
    }

    /**
     * Returns a {@link Quantizer} which converts the input value to the nearest multiple of the compression
     * factor, in the direction towards zero.
     * <p/>
     * <b>Examples (compression factor 5):</b><br/>
     * <ul>
     *     <li>Input value 0 -> 0</li>
     *     <li>Input value 4 -> 0</li>
     *     <li>Input value 5 -> 5</li>
     *     <li>Input value 9 -> 5</li>
     *     <li>Input value -4 -> 0</li>
     *     <li>Input value -5 -> -5</li>
     *     <li>Input value -9 -> -5</li>
     * </ul>
     *
     * @param compressionFactor The number of adjacent mathematical integers (>= 2) to coalesce to a single key
     * @return A {@link Quantizer} which converts the input value to the nearest multiple of the compression
     * factor, in the direction towards zero
     */
    public static Quantizer<Long> withCompressionFactor(int compressionFactor) {
        return new CompressingQuantizer(compressionFactor);
    }

    /**
     * Private constructor, not used.
     */
    LongQuantizer() {
    }
}
