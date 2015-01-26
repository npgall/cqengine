package com.googlecode.cqengine.engine;

import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.testutil.Car;
import org.junit.Test;

import java.util.Collections;

public class IndexQueryEngineTest {

    @Test(expected = IllegalStateException.class)
    public void testAddIndex_ArgumentValidation() throws Exception {
        IndexQueryEngine<Car> queryEngine = new IndexQueryEngine<Car>();
        queryEngine.init(Collections.<Car>emptySet(), QueryOptions.noQueryOptions());

        queryEngine.addIndex(null);
    }
}