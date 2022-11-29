package com.googlecode.cqengine.examples.ordering;

import com.googlecode.concurrenttrees.common.PrettyPrinter;
import com.googlecode.concurrenttrees.radix.node.NodeFactory;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.indexOrdering.*;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.option.ConcurrentRadixTreeLongestPrefixMatch;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static com.googlecode.cqengine.query.option.EngineThresholds.INDEX_ORDERING_SELECTIVITY;

public class LongestPrefixMatchExampleCodeRun {


    private final NodeFactory nodeFactory = new DefaultCharArrayNodeFactory();

    protected NodeFactory getNodeFactory() {
        return nodeFactory;
    }


    @Test
    public void testNewDesignedSolutionOneColumnDefinedAsLongest() {

        // Indexed collection
        IndexedCollection<LongestPrefixMatchOneColumnExampleCode> longestPrefixMatchExampleCodes = new ConcurrentIndexedCollection<>();

        //get the IndexOrderingConcurrentTreeHolder from the cqengine
        IConcurrentInvertedRadixTreesHolder singletonConcurrentTreeHolder = new ConcurrentInvertedRadixTreesHolder();

        // some indexes
        //longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(LongestPrefixMatchExampleCode.A_NUMBER));
        longestPrefixMatchExampleCodes.addIndex(HashIndex.onAttribute(LongestPrefixMatchOneColumnExampleCode.bVALID_FROM));
        longestPrefixMatchExampleCodes.addIndex(HashIndex.onAttribute(LongestPrefixMatchOneColumnExampleCode.cVALID_TO));
        // add cars objects to collection-
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchOneColumnExampleCode("89761", new Long(4), new Long(7), "A1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchOneColumnExampleCode("67894", new Long(4), new Long(7), "A1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchOneColumnExampleCode("32221", new Long(11), new Long(17), "A1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchOneColumnExampleCode("32", new Long(9), new Long(16), "C1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchOneColumnExampleCode("32", new Long(9), new Long(18), "D1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchOneColumnExampleCode("32", new Long(7), new Long(15), "D1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchOneColumnExampleCode("328", new Long(9), new Long(10), "E1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchOneColumnExampleCode("4561", new Long(8), new Long(15), "F1"));

        // run some queries
        System.out.println("LongestPrefixMatchExample Table");

        // Query time case 1: between valid from and valid to
        Query<LongestPrefixMatchOneColumnExampleCode> queryTime_case1 = and(lessThanOrEqualTo(LongestPrefixMatchOneColumnExampleCode.bVALID_FROM, new Long(10)), greaterThanOrEqualTo(LongestPrefixMatchOneColumnExampleCode.cVALID_TO, new Long(10)));
        // two cases combined
//        Query<NationalShortCode> query_Time = or(queryTime_case1, queryTime1_case2);
        Query<LongestPrefixMatchOneColumnExampleCode> query_Time = queryTime_case1;

        longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(LongestPrefixMatchOneColumnExampleCode.a_NUMBER));

        List<String> lookups = new ArrayList<>();

        lookups.add("MONATIOANLs");

        List<Attribute<LongestPrefixMatchOneColumnExampleCode, String>> attributeList = new ArrayList<>();

        attributeList.add(LongestPrefixMatchOneColumnExampleCode.a_NUMBER);


        List<String> attributesValues = new ArrayList<>();

        attributesValues.add("322211");


        QueryOptions queryOptions = QueryFactory.queryOptions(orderBy(descending(LongestPrefixMatchOneColumnExampleCode.a_NUMBER)),
                applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)),
                applyAttrObjectOptions(attrObjectOption(ConcurrentRadixTreeLongestPrefixMatch.CONCURRENT_RADIX_TREE_LONGEST_PREFIX_MATCH_BY_ATTRIBUTES, attributeList),
                        attrObjectOption(ConcurrentRadixTreeLongestPrefixMatch.CONCURRENT_RADIX_TREE_LONGEST_PREFIX_MATCH_BY_VALUES, attributesValues)));

        //creation of the concurrent radix tree

        ConcurrentInvertedRadixTree tree1 = new ConcurrentInvertedRadixTree(nodeFactory);

        IndexedConcurrentInvertedRadixTree indexedConcurrentInvertedRadixTree = new IndexedConcurrentInvertedRadixTree(tree1);

        List<IndexedConcurrentInvertedRadixTree> trees = new ArrayList<>();

        trees.add(indexedConcurrentInvertedRadixTree);


        longestPrefixMatchExampleCodes.forEach(element -> {
            tree1.put(element.getANumber(), indexedConcurrentInvertedRadixTree.getIndex());
            Integer index = indexedConcurrentInvertedRadixTree.getIndex();
            index++;
            indexedConcurrentInvertedRadixTree.setIndex(index);
        });


        AtomicInteger indexForPrint = new AtomicInteger(0);
        attributeList.forEach(element -> {
            System.out.println(String.format("Pretty print for tree of Field %s: ", attributeList.get(indexForPrint.get()).getAttributeName()));

            String actual = PrettyPrinter.prettyPrint(trees.get(indexForPrint.get()).getConcurrentInvertedRadixTree());

            System.out.println(actual);

            indexForPrint.getAndIncrement();
        });

        QueryOptions finalQueryOptions = queryOptions;
        AtomicInteger index = new AtomicInteger(0);
        attributeList.forEach(e -> {
            singletonConcurrentTreeHolder.addConcurrentInvertedRadixTree(attributeList.get(index.get()).getAttributeName(), trees.get(index.get()));
            index.getAndIncrement();
        });


        longestPrefixMatchExampleCodes.setConcurrentInvertedRadixTree(singletonConcurrentTreeHolder);
        System.out.println("finalResult:");




        ResultSet<LongestPrefixMatchOneColumnExampleCode> longestPrefixMatchExampleCodeResultSet = longestPrefixMatchExampleCodes.retrieve(query_Time, queryOptions);

        longestPrefixMatchExampleCodeResultSet.forEach(System.out::println);


    }


    @Test
    public void testNewDesignedSolutionTwoColumnsDefinedAsLongest() {

        // Indexed collection
        IndexedCollection<LongestPrefixMatchTwoColumnsExampleCode> longestPrefixMatchExampleCodes = new ConcurrentIndexedCollection<>();

        //get the IndexOrderingConcurrentTreeHolder from the cqengine
        IConcurrentInvertedRadixTreesHolder singletonConcurrentTreeHolder = new ConcurrentInvertedRadixTreesHolder();

        // some indexes
        //longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(LongestPrefixMatchExampleCode.A_NUMBER));
        longestPrefixMatchExampleCodes.addIndex(HashIndex.onAttribute(LongestPrefixMatchTwoColumnsExampleCode.bVALID_FROM));
        longestPrefixMatchExampleCodes.addIndex(HashIndex.onAttribute(LongestPrefixMatchTwoColumnsExampleCode.cVALID_TO));

        LongestPrefixMatchTwoColumnsExampleCode expectedLongest = new LongestPrefixMatchTwoColumnsExampleCode("3222", "4567", new Long(9), new Long(16), "C1");
        // add cars objects to collection-
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchTwoColumnsExampleCode("89761", "9876", new Long(4), new Long(7), "A1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchTwoColumnsExampleCode("67894", "45678", new Long(4), new Long(7), "A1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchTwoColumnsExampleCode("32221", "45671", new Long(11), new Long(17), "A1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchTwoColumnsExampleCode("3222", "4567", new Long(12), new Long(16), "C1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchTwoColumnsExampleCode("3222", "4567", new Long(9), new Long(18), "D1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchTwoColumnsExampleCode("3222", "4567", new Long(7), new Long(15), "D1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchTwoColumnsExampleCode("328", "49", new Long(9), new Long(10), "E1"));
        longestPrefixMatchExampleCodes.add(new LongestPrefixMatchTwoColumnsExampleCode("4561", "45671", new Long(8), new Long(15), "F1"));

        // run some queries
        System.out.println("LongestPrefixMatchExample Table");

        // Query time case 1: between valid from and valid to
        Query<LongestPrefixMatchTwoColumnsExampleCode> queryTime_case1 = and(lessThanOrEqualTo(LongestPrefixMatchTwoColumnsExampleCode.bVALID_FROM, new Long(10)), greaterThanOrEqualTo(LongestPrefixMatchTwoColumnsExampleCode.cVALID_TO, new Long(10)));
        // two cases combined
//        Query<NationalShortCode> query_Time = or(queryTime_case1, queryTime1_case2);
        Query<LongestPrefixMatchTwoColumnsExampleCode> query_Time = queryTime_case1;

        //longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(forObjectsMissing(LongestPrefixMatchTwoColumnsExampleCode.a_NUMBER)));

        longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(LongestPrefixMatchTwoColumnsExampleCode.a_NUMBER));


        //longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(forObjectsMissing(LongestPrefixMatchTwoColumnsExampleCode.b_NUMBER)));

        longestPrefixMatchExampleCodes.addIndex(NavigableIndex.onAttribute(LongestPrefixMatchTwoColumnsExampleCode.b_NUMBER));


        List<Attribute<LongestPrefixMatchTwoColumnsExampleCode, String>> attributeList = new ArrayList<>();

        attributeList.add(LongestPrefixMatchTwoColumnsExampleCode.a_NUMBER);

        attributeList.add(LongestPrefixMatchTwoColumnsExampleCode.b_NUMBER);


        List<String> attributesValues = new ArrayList<>();

        attributesValues.add("322211");

        attributesValues.add("45671");


        QueryOptions queryOptions = QueryFactory.queryOptions(orderBy(descending(LongestPrefixMatchTwoColumnsExampleCode.a_NUMBER)
                        , descending(LongestPrefixMatchTwoColumnsExampleCode.b_NUMBER)),
                applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0)),
                applyAttrObjectOptions(attrObjectOption(ConcurrentRadixTreeLongestPrefixMatch.CONCURRENT_RADIX_TREE_LONGEST_PREFIX_MATCH_BY_ATTRIBUTES, attributeList),
                        attrObjectOption(ConcurrentRadixTreeLongestPrefixMatch.CONCURRENT_RADIX_TREE_LONGEST_PREFIX_MATCH_BY_VALUES, attributesValues)));

        //creation of the concurrent radix tree

        ConcurrentInvertedRadixTree tree1 = new ConcurrentInvertedRadixTree(nodeFactory);


        ConcurrentInvertedRadixTree tree2 = new ConcurrentInvertedRadixTree(nodeFactory);


        IndexedConcurrentInvertedRadixTree indexedConcurrentInvertedRadixTree1 = new IndexedConcurrentInvertedRadixTree(tree1);

        IndexedConcurrentInvertedRadixTree indexedConcurrentInvertedRadixTree2 = new IndexedConcurrentInvertedRadixTree(tree2);

        List<IndexedConcurrentInvertedRadixTree> trees = new ArrayList<>();

        trees.add(indexedConcurrentInvertedRadixTree1);

        trees.add(indexedConcurrentInvertedRadixTree2);

        AtomicInteger indexForTree = new AtomicInteger(0);
        longestPrefixMatchExampleCodes.forEach(element -> {
            Integer indexForA  = trees.get(0).getIndex();
            tree1.put(element.getANumber(), indexForA);
            indexForA++;
            trees.get(0).setIndex(indexForA);
            Integer indexForB  = trees.get(1).getIndex();
            tree2.put(element.getBNumber(), indexForB);
            indexForB++;
            trees.get(1).setIndex(indexForB);
        });


        AtomicInteger indexForPrint = new AtomicInteger(0);
        attributeList.forEach(element -> {
            System.out.println(String.format("Pretty print for tree of Field %s: ", attributeList.get(indexForPrint.get()).getAttributeName()));

            String actual = PrettyPrinter.prettyPrint(trees.get(indexForPrint.get()).getConcurrentInvertedRadixTree());

            System.out.println(actual);

            indexForPrint.getAndIncrement();
        });

        QueryOptions finalQueryOptions = queryOptions;
        AtomicInteger index = new AtomicInteger(0);
        attributeList.forEach(e -> {
            singletonConcurrentTreeHolder.addConcurrentInvertedRadixTree(attributeList.get(index.get()).getAttributeName(), trees.get(index.get()));
            index.getAndIncrement();
        });


        longestPrefixMatchExampleCodes.setConcurrentInvertedRadixTree(singletonConcurrentTreeHolder);


        System.out.println("finalResult:");

        ResultSet<LongestPrefixMatchTwoColumnsExampleCode> longestPrefixMatchExampleCodeResultSet = longestPrefixMatchExampleCodes.retrieve(query_Time, queryOptions);


        longestPrefixMatchExampleCodeResultSet.forEach(System.out::println);


    }
}