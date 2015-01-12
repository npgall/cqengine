package com.googlecode.cqengine.index.querytype;

import com.googlecode.cqengine.engine.IndexQueryEngine;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class QueryTypeIndexTest {

    // The query that is supported by the mocked QueryTypeIndex.
    static class QueryTypeQueryIndexQuery implements Query<String>{
        @Override
        public boolean matches(String object, QueryOptions queryOptions) {
            return true;
        }
    }

    @Mock
    QueryTypeIndex<String> queryTypeIndex;

    @Test
    public void testRetrieve() throws Exception {

        MockitoAnnotations.initMocks(this);

        Set<Class<? extends Query>> supportedQueries = new HashSet<Class<? extends Query>>();
        supportedQueries.add(QueryTypeQueryIndexQuery.class);

        when(queryTypeIndex.getSupportedQueryTypes()).thenReturn(supportedQueries);
        when(queryTypeIndex.supportsQuery(any(QueryTypeQueryIndexQuery.class))).thenReturn(true);

        IndexQueryEngine<String> indexQueryEngine = new IndexQueryEngine<String>();
        indexQueryEngine.addIndex(queryTypeIndex);

        QueryTypeQueryIndexQuery queryTypeQueryIndexQuery = new QueryTypeQueryIndexQuery();
        QueryOptions queryOptions = new QueryOptions();
        indexQueryEngine.retrieve(queryTypeQueryIndexQuery, queryOptions);

        verify(queryTypeIndex, times(1)).retrieve(queryTypeQueryIndexQuery, queryOptions);

    }
}