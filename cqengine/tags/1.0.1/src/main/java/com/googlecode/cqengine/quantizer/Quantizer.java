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
 * A Quantizer converts a fine-grained or <i>continuous</i> value to a <i>discrete</i> or coarse-grained value.
 * <p/>
 * This can be used to control the <i>granularity</i> of indexes: the tradeoff between storage space required by
 * indexes, and the query processing time in looking up objects. It can also be used to convert <i>continuous</i>
 * values to <i>discrete</i> values, given the inherent challenges in indexing continuous values.
 * <p/>
 * <b>Example uses:</b><br/>
 * <ul>
 *     <li>
 *         Index adjacent integers to fewer coarse-grained keys with a <i>compression factor</i>.
 *         <p/>
 *         Store objects having integer attributes with adjacent integer values to the same key in indexes, to reduce
 *         the overall number of keys in the index.
 *         <p/>
 *         For example, objects with an integer "price" attribute: store objects with price 0-4 against the same key,
 *         objects with price 5-9 against the the next key etc., potentially reducing the number of keys
 *         in the index by a factor of five. The <u><i>compression factor</i></u> (5 in this example) can be varied.
 *         <p/>
 *         See: {@link IntegerQuantizer}, {@link LongQuantizer}, {@link BigIntegerQuantizer}
 *     </li>
 *     <li>
 *         Index attributes with <i>continuous</i> values, by quantizing to <i>discrete</i> values, and optionally
 *         apply compression.
 *         <p/>
 *         For example, objects with "price" stored with arbitrary precision ({@link Float}, {@link Double},
 *         {@link java.math.BigDecimal} etc.). If one object has price 5.00, and another has price 5.0000001,
 *         these objects would by default be stored against different keys, leading to an arbitrarily large number
 *         of keys for potentially small ranges in price.
 *         <p/>
 *         See: {@link FloatQuantizer}, {@link DoubleQuantizer}, {@link BigDecimalQuantizer}
 *     </li>
 * </ul>
 * When an index is configured with a {@link Quantizer} and added to a collection, the index will use the function
 * implemented by the {@link Quantizer} to generate keys against which it will store objects in the index.
 * <p/>
 * Subsequently, when an index receives a query, it will use the same quantizer to determine from <i>sought</i> values
 * in the query the relevant keys against which it can find matching objects in the index. Given that the set of objects
 * stored against a <i>quantized key</i> will be larger than those actually sought in the query, the index will then
 * filter objects in the retrieved set on-the-fly to those actually matching the query.
 * <p/>
 * As such quantization allows the size of indexes to be reduced but trades it for additional CPU overhead in filtering
 * retrieved objects.
 *
 * @author Niall Gallagher
 */
public interface Quantizer<A> {

    A getQuantizedValue(A attributeValue);
}
