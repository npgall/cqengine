package com.googlecode.cqengine.examples.ordering;

import com.googlecode.concurrenttrees.common.PrettyPrinter;
import com.googlecode.concurrenttrees.radix.node.NodeFactory;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.indexOrdering.IndexOrderingConcurrentTreeHolder;
import com.googlecode.cqengine.index.indexOrdering.LongestPrefixMatchExampleCode;
import com.googlecode.cqengine.index.indexOrdering.LookUpIdentifier;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.AttrStringOptions;
import com.googlecode.cqengine.query.option.ConcurrentRadixTreeLongestPrefixMatch;
import com.googlecode.cqengine.query.option.QueryOptions;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.option.EngineThresholds.INDEX_ORDERING_SELECTIVITY;

public class LongestPrefixMatchExampleCodeRun {


    private final NodeFactory nodeFactory = new DefaultCharArrayNodeFactory();

    protected NodeFactory getNodeFactory() {
        return nodeFactory;
    }


    @Test
    public void testNewDesignedSoluation() {


        final NodeFactory nodeFactory = new DefaultCharArrayNodeFactory();

        // Indexed collection
        IndexedCollection<LongestPrefixMatchExampleCode> longestPrefixMatchExampleCodes = new ConcurrentIndexedCollection<>();

        //get the IndexOrderingConcurrentTreeHolder from the cqengine
        IndexOrderingConcurrentTreeHolder singletonConcurrentTreeHolder = longestPrefixMatchExampleCodes.getSingletonConcurrentTreeHolder();

        // some indexes
        //longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(LongestPrefixMatchExampleCode.A_NUMBER));
        longestPrefixMatchExampleCodes.addIndex(HashIndex.onAttribute(LongestPrefixMatchExampleCode.bVALID_FROM));
        longestPrefixMatchExampleCodes.addIndex(HashIndex.onAttribute(LongestPrefixMatchExampleCode.cVALID_TO));

        // add cars objects to collection-
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchExampleCode("32221", new Long(4), new Long(7), "A1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchExampleCode("3222", new Long(9), new Long(16), "C1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchExampleCode("322", new Long(17), new Long(21), "D1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchExampleCode("32", new Long(9), new Long(10), "E1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchExampleCode("4561", new Long(8), new Long(15), "F1"));

        // run some queries
        System.out.println("LongestPrefixMatchExample Table");
        // longest match
        Query<LongestPrefixMatchExampleCode> query_ANumber_case1 = longestPrefix(LongestPrefixMatchExampleCode.a_NUMBER, "322211");

        // Query time case 1: between valid from and valid to
        Query<LongestPrefixMatchExampleCode> queryTime_case1 = and(lessThanOrEqualTo(LongestPrefixMatchExampleCode.bVALID_FROM, new Long(10)), greaterThanOrEqualTo(LongestPrefixMatchExampleCode.cVALID_TO, new Long(10)));
        // two cases combined
//        Query<NationalShortCode> query_Time = or(queryTime_case1, queryTime1_case2);
        Query<LongestPrefixMatchExampleCode> query_Time = queryTime_case1;

        longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(forObjectsMissing(LongestPrefixMatchExampleCode.a_NUMBER)));

        longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(LongestPrefixMatchExampleCode.a_NUMBER));


        QueryOptions queryOptions = queryOptions();


        queryOptions = QueryFactory.queryOptions(orderBy(descending(missingLast(LongestPrefixMatchExampleCode.a_NUMBER))), applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)), query_ANumber_case1, applyAttrStringOptions(attrStringOption(ConcurrentRadixTreeLongestPrefixMatch.CONCURRENT_RADIX_TREE_LONGEST_PREFIX_MATCH_BY_LOOKUP, "MONATIOANLs")));

        //creation of the concurrent radix tree

        ConcurrentInvertedRadixTree tree = new ConcurrentInvertedRadixTree(nodeFactory);

        AtomicInteger index = new AtomicInteger(0);
        longestPrefixMatchExampleCodes.forEach(element -> {
            tree.put(element.getANumber(), index.getAndIncrement());
        });


        String actual = PrettyPrinter.prettyPrint(tree);

        System.out.println(actual);

        singletonConcurrentTreeHolder.addConcurrentInvertedRadixTree(new LookUpIdentifier(queryOptions.get(AttrStringOptions.class).getAttrStringOption(ConcurrentRadixTreeLongestPrefixMatch.CONCURRENT_RADIX_TREE_LONGEST_PREFIX_MATCH_BY_LOOKUP), LongestPrefixMatchExampleCode.a_NUMBER.getAttributeName()), tree);


        System.out.println("finalResult:");

        longestPrefixMatchExampleCodes.retrieve(query_Time, queryOptions).forEach(System.out::println);

//        List<LongestPrefixMatchExampleCode> finalResult = StreamSupport.stream(resultData.spliterator(), false).collect(Collectors.toList());
//
//        finalResult.forEach(System.out::println);


//        System.out.println(Iterables.toString(tree.getKeysPrefixing("322211")));
//
//
//        Iterable<CharSequence> keysPrefixing = tree.getKeysPrefixing("322211");
//
//        Iterator<CharSequence> iterator = keysPrefixing.iterator();
//
//        Set<CharSequence> charSequences = new HashSet<>();
//
//
//
//        while(iterator.hasNext()){
//            charSequences.add(iterator.next());
//        }
//
//
//        Query<LongestPrefixMatchExampleCode> in_Query = new In(LongestPrefixMatchExampleCode.A_NUMBER, false,charSequences);
//
//
//        System.out.println("In Query");
//        Query<LongestPrefixMatchExampleCode> finalQuery1 = and(query_Time,in_Query);
//
//        ResultSet<LongestPrefixMatchExampleCode> finalResult1 = longestPrefixMatchExampleCodes.retrieve(finalQuery1,queryOptions);
//
//        finalResult1.forEach(System.out::println);
//


    }
}