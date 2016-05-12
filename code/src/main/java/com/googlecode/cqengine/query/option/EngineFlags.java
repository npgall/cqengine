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
package com.googlecode.cqengine.query.option;

import com.googlecode.cqengine.query.QueryFactory;

/**
 * An enum of some flags which can be set into {@link QueryOptions} to request certain behaviours from the query engine.
 * This enum is not an exhaustive set of possible flags which can be set. For example custom indexes are free to define
 * their own flags. This enum is just a central collection of flags used directly by the query engine.
 * <p/>
 * Example usage using {@link QueryFactory}:<br/>
 * <code>QueryOptions queryOptions =
 * queryOptions(enableFlags(EngineFlags.SOME_FLAG, EngineFlags.OTHER_FLAG), disableFlags(EngineFlags.ANOTHER_FLAG))
 * </code>
 *
 * @author niall.gallagher
 */
public enum EngineFlags {

    /**
     * A performance tuning flag which may be useful in applications in which objects are stored off-heap, remotely, or
     * on disk, or generally where it is more expensive to retrieve values from CQEngine attributes, than it is to
     * probe indexes on those attributes, when evaluating a query.
     * <p/>
     * <b>Example</b><br/>
     * Consider the query: <code>and(equal(Car.MANUFACTURER, "Ford"), equal(Car.COLOR, Color.BLUE))</code><br/>
     * <ol>
     *     <li>Let's say there are indexes on both the MANUFACTURER and COLOR attributes.</li>
     *     <li>CQEngine will query those indexes to determine which is the smallest candidate set:
     *     are there fewer Ford cars in the collection, or are there fewer BLUE cars in the collection? Let's say
     *     there are fewer Ford cars in the collection.</li>
     *     <li>CQEngine will then use the index on MANUFACTURER, to jump to the set of Ford cars, and then it will
     *     need to determine if each of those Ford cars is also BLUE.</li>
     *     <li>This flag determines CQEngine's strategy at this point:
     *     <ul>
     *         <li>If this flag is <i>disabled</i> (or is not set) then CQEngine will retrieve the color of the car
     *         being evaluated from the COLOR attribute, and it will compare that value with the color in the query.
     *         If they match, the candidate object is considered to match the overall query.</li>
     *         <li>If this flag is <i>enabled</i> then CQEngine will instead try to access an index on the COLOR
     *         attribute, and it will ask that index if it contains a BLUE car which refers to this object.
     *         If the index reports that it contains such an object, then the candidate object is considered to match
     *         the overall query.</li>
     *     </ul>
     *     </li>
     * </ol>
     */
    PREFER_INDEX_MERGE_STRATEGY,

    /**
     * A performance tuning flag for when the index ordering strategy is used, which configures the strategy to
     * maximize ordering speed, at the expense of allowing a slightly inexact ordering of objects which have multiple
     * values for the attribute by which they are being ordered.
     * <p/>
     * For example: if object 1 has values ["a"] and object 2 has values ["a", "b"] then technically object 1 should
     * always be returned before object 2, when results are to be returned in ascending order. The relative ordering
     * of these objects should also be reversed, when results are to be returned in descending order. That is, to be
     * consistent with the other ordering strategies in CQEngine.
     * <p/>
     * However this technically-correct and deterministic ordering, does not come for free, and requires that the
     * strategy re-sort the objects in each bucket encountered.
     * <p/>
     * If this flag is enabled, the index ordering strategy will avoid re-sorting the buckets in the index used for
     * ordering. This will improve retrieval speed, at the expense of allowing the relative ordering of objects having
     * one attribute value in common, and having other differing attribute values, to be slightly inexact.
     */
    INDEX_ORDERING_ALLOW_FAST_ORDERING_OF_MULTI_VALUED_ATTRIBUTES
}
