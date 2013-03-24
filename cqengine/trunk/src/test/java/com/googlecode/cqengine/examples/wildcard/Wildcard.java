package com.googlecode.cqengine.examples.wildcard;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SelfAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.filter.FilteringResultSet;

import java.util.Arrays;


import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * Demonstrates how to perform wildcard queries in CQEngine as of CQEngine ~1.0.3.
 * This will be further simplified in later versions.
 * <p/>
 * These queries can be accelerated by adding both {@link RadixTreeIndex} and {@link ReversedRadixTreeIndex}.
 *
 * @author Niall Gallagher
 */
public class Wildcard {

    public static void main(String[] args) {
        IndexedCollection<String> collection = CQEngine.copyFrom(Arrays.asList("TEAM", "TEST", "TOAST", "T", "TT"));
        collection.addIndex(RadixTreeIndex.onAttribute(SELF));
        collection.addIndex(ReversedRadixTreeIndex.onAttribute(SELF));

        for (String match : retrieveWildcardMatches(collection, "T", "T")) {
            System.out.println(match); // TOAST, TEST, TT
        }
    }

    public static ResultSet<String> retrieveWildcardMatches(IndexedCollection<String> collection, final String prefix, final String suffix) {
        ResultSet<String> candidates = collection.retrieve(and(startsWith(SELF, prefix), endsWith(SELF, suffix)));

        return new FilteringResultSet<String>(candidates) {
            @Override
            public boolean isValid(String candidate) {
                return candidate.length() >= prefix.length() + suffix.length();
            }
        };
    }

    static final Attribute<String, String> SELF = new SelfAttribute<String>();
}
