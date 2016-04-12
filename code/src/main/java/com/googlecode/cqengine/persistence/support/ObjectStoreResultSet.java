package com.googlecode.cqengine.persistence.support;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

import java.util.Iterator;

/**
 * Created by npgall on 01/03/2016.
 */
public class ObjectStoreResultSet<O> extends ResultSet<O> {

    final ObjectStore<O> objectStore;
    final Query<O> query;
    final QueryOptions queryOptions;
    final int retrievalCost;

    public ObjectStoreResultSet(ObjectStore<O> objectStore, Query<O> query, QueryOptions queryOptions, int retrievalCost) {
        this.objectStore = objectStore;
        this.query = query;
        this.queryOptions = queryOptions;
        this.retrievalCost = retrievalCost;
    }

    @Override
    public Iterator<O> iterator() {
        return objectStore.iterator(queryOptions);
    }

    @Override
    public boolean contains(O object) {
        return objectStore.contains(object, queryOptions);
    }

    @Override
    public boolean matches(O object) {
        return query.matches(object, queryOptions);
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
        return retrievalCost;
    }

    @Override
    public int getMergeCost() {
        return size();
    }

    @Override
    public int size() {
        return objectStore.size(queryOptions);
    }

    @Override
    public void close() {
        // No op.
    }
}
