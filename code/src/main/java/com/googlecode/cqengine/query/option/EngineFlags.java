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
     * A performance tuning flag which may be useful in applications in which it is more expensive to retrieve values
     * from attributes, than it is to probe indexes on those attributes, when evaluating a query.
     * <p/>
     * This may be useful where the collection which attributes access is stored off-heap, or where attributes read
     * from external data sources. Essentially this is useful where the latency to access CQEngine indexes, will
     * typically be lower than the latency to retrieve values from attributes.
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
    PREFER_INDEX_MERGE_STRATEGY
}
