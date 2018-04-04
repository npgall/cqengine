package com.googlecode.cqengine.index.levenshtein;

import com.github.liblevenshtein.collection.dictionary.SortedDawg;
import com.github.liblevenshtein.transducer.Algorithm;
import com.github.liblevenshtein.transducer.Candidate;
import com.github.liblevenshtein.transducer.ITransducer;
import com.github.liblevenshtein.transducer.factory.TransducerBuilder;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.AbstractAttributeIndex;
import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.LevenshteinDistance;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author <a href="mailto:ruslan.sennov@gmail.com">Ruslan Sennov</a>
 */
public class LevenshteinDistanceIndex<O> extends AbstractAttributeIndex<String, O> {

    private final TransducerFactory transducerFactory;
    private ITransducer<Candidate> transducer;
    private Map<String, Set<O>> terms;

    /**
     * Private constructor, used by static factory methods.
     *
     * @param attribute The attribute on which the index will be built
     */
    private LevenshteinDistanceIndex(Attribute<O, String> attribute, Algorithm transducerAlgorithm) {
        super(attribute, Collections.singleton(LevenshteinDistance.class));
        this.transducerFactory = new TransducerFactory(transducerAlgorithm);
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public boolean isQuantized() {
        return false;
    }

    @Override
    public ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions) {
        Class<?> queryClass = query.getClass();
        if (LevenshteinDistance.class.equals(queryClass)) {
            LevenshteinDistance<O> lev = (LevenshteinDistance<O>) query;
            Set<O> set = new LinkedHashSet<>();
            transducer.transduce(lev.getValue(), lev.getMaxDistance()).forEach(candidate -> {
                set.addAll(terms.get(candidate.term()));
            });
            return new ResultSet<O>() {
                @Override
                public Iterator<O> iterator() {
                    return set.iterator();
                }

                @Override
                public boolean contains(O object) {
                    return set.contains(object);
                }

                @Override
                public boolean matches(O object) {
                    return set.contains(object);
                }

                @Override
                public Query<O> getQuery() {
                    return query;
                }

                @Override
                public QueryOptions getQueryOptions() {
                    return queryOptions;
                }

                @Override
                public int getRetrievalCost() {
                    return 10;
                }

                @Override
                public int getMergeCost() {
                    return 10;
                }

                @Override
                public int size() {
                    return set.size();
                }

                @Override
                public void close() {
                    set.clear();
                }
            };
        } else {
            throw new IllegalArgumentException("Unsupported query: " + query);
        }
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    @Override
    public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        // this index is immutable, will never be here
        throw new IllegalStateException();
    }

    @Override
    public boolean removeAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
        // this index is immutable, will never be here
        throw new IllegalStateException();
    }

    @Override
    public void clear(QueryOptions queryOptions) {
    }

    @Override
    public void init(ObjectStore<O> objectStore, QueryOptions queryOptions) {
        try (CloseableIterator<O> it = objectStore.iterator(queryOptions)) {
            terms = new HashMap<>();
            it.forEachRemaining(o -> {
                attribute.getValues(o, queryOptions).forEach(term -> {
                    Set<O> objects = terms.computeIfAbsent(term, new Function<String, Set<O>>() {
                        @Override
                        public Set<O> apply(String s) {
                            return new HashSet<>();
                        }
                    });
                    objects.add(o);
                });
            });
        }
        SortedDawg dict = new SortedDawg();
        List<String> list = new ArrayList<>(terms.keySet());
        Collections.sort(list);
        dict.addAll(list);
        transducer = transducerFactory.buildTransducer(dict);
    }

    public static <O> LevenshteinDistanceIndex<O> onAttribute(Attribute<O, String> attribute) {
        return new LevenshteinDistanceIndex<>(attribute, Algorithm.STANDARD);
    }

    public static <O> LevenshteinDistanceIndex<O> withSpellingCorrectionOnAttribute(Attribute<O, String> attribute) {
        return new LevenshteinDistanceIndex<>(attribute, Algorithm.TRANSPOSITION);
    }

    public static <O> LevenshteinDistanceIndex<O> withOCRCorrectionOnAttribute(Attribute<O, String> attribute) {
        return new LevenshteinDistanceIndex<>(attribute, Algorithm.MERGE_AND_SPLIT);
    }
}

class TransducerFactory {
    private final Algorithm transducerAlgorithm;

    TransducerFactory(Algorithm transducerAlgorithm) {
        this.transducerAlgorithm = transducerAlgorithm;
    }

    ITransducer<Candidate> buildTransducer(SortedDawg dictionary) {
        return new TransducerBuilder()
                .dictionary(dictionary)
                .algorithm(transducerAlgorithm)
                .includeDistance(true)
                .build();
    }
}