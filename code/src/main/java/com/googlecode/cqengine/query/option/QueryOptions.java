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

import com.googlecode.cqengine.engine.CollectionQueryEngine;
import com.googlecode.cqengine.engine.QueryEngine;
import com.googlecode.cqengine.index.sqlite.ConnectionManager;
import com.googlecode.cqengine.index.support.CloseableRequestResources;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.query.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates optional key-value parameters supplied by the application to the query engine, attributes
 * and indexes.
 * <p/>
 * These parameters can request specific behaviour from the query engine such as specifying transaction
 * isolation levels.
 * <p/>
 * Query options also allow the application to pass arbitrary or request-scope objects to custom Attributes or Indexes.
 */
public class QueryOptions {

    protected Persistence persistenceOption = null;
    protected ConnectionManager connectionManagerOption = null;
    protected QueryEngine queryEngineOption = null;
    protected DeduplicationOption deduplicationOption = null;
    protected IsolationOption isolationOption = null;
    protected FlagsEnabled flagsEnabledOption = null;
    protected FlagsDisabled flagsDisabledOption = null;
    protected Thresholds thresholdsOption = null;
    protected CloseableRequestResources closeableRequestResourcesOption = null;
    protected OrderByOption orderByOption = null;
    protected Query rootQueryOption = null;
    protected Map<Object, Object> extraOptions = null;

    public Persistence getPersistence() {
        return persistenceOption;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManagerOption;
    }

    public QueryEngine getQueryEngine() {
        return queryEngineOption;
    }

    public DeduplicationOption getDeduplicationOption() {
        return deduplicationOption;
    }

    public IsolationOption getIsolationOption() {
        return isolationOption;
    }

    public FlagsEnabled getFlagsEnabled() {
        return flagsEnabledOption;
    }

    public FlagsDisabled getFlagsDisabled() {
        return flagsDisabledOption;
    }

    public Thresholds getThresholds() {
        return thresholdsOption;
    }

    public CloseableRequestResources getCloseableRequestResources() {
        return closeableRequestResourcesOption;
    }

    public OrderByOption getOrderByOption() {
        return orderByOption;
    }

    public Query getRootQuery() {
        return rootQueryOption;
    }

    public <T> T get(Class<T> optionType) {
        @SuppressWarnings("unchecked")
        T optionValue = (T) get((Object)optionType);
        return optionValue;
    }

    public Object get(Object key) {
        if (Persistence.class.equals(key)) {
            return this.persistenceOption;
        }
        else if (ConnectionManager.class.equals(key)) {
            return this.connectionManagerOption;
        }
        else if (QueryEngine.class.equals(key)) {
            return this.queryEngineOption;
        }
        else if (DeduplicationOption.class.equals(key)) {
            return this.deduplicationOption;
        }
        else if (IsolationOption.class.equals(key)) {
            return this.isolationOption;
        }
        else if (FlagsEnabled.class.equals(key)) {
            return this.flagsEnabledOption;
        }
        else if (FlagsDisabled.class.equals(key)) {
            return this.flagsDisabledOption;
        }
        else if (Thresholds.class.equals(key)) {
            return this.thresholdsOption;
        }
        else if (CloseableRequestResources.class.equals(key)) {
            return this.closeableRequestResourcesOption;
        }
        else if (OrderByOption.class.equals(key)) {
            return this.orderByOption;
        }
        else if (CollectionQueryEngine.ROOT_QUERY.equals(key)) {
            return this.rootQueryOption;
        }
        else {
            return extraOptions == null ? null : extraOptions.get(key);
        }
    }

    public void put(Object key, Object value) {
        if (Persistence.class.equals(key)) {
            this.persistenceOption = (Persistence) value;
        }
        else if (ConnectionManager.class.equals(key)) {
            this.connectionManagerOption = (ConnectionManager) value;
        }
        else if (QueryEngine.class.equals(key)) {
            this.queryEngineOption = (QueryEngine) value;
        }
        else if (DeduplicationOption.class.equals(key)) {
            this.deduplicationOption = (DeduplicationOption) value;
        }
        else if (IsolationOption.class.equals(key)) {
            this.isolationOption = (IsolationOption) value;
        }
        else if (FlagsEnabled.class.equals(key)) {
            this.flagsEnabledOption = (FlagsEnabled) value;
        }
        else if (FlagsDisabled.class.equals(key)) {
            this.flagsDisabledOption = (FlagsDisabled) value;
        }
        else if (Thresholds.class.equals(key)) {
            this.thresholdsOption = (Thresholds) value;
        }
        else if (CloseableRequestResources.class.equals(key)) {
            this.closeableRequestResourcesOption = (CloseableRequestResources) value;
        }
        else if (OrderByOption.class.equals(key)) {
            this.orderByOption = (OrderByOption) value;
        }
        else if (CollectionQueryEngine.ROOT_QUERY.equals(key)) {
            this.rootQueryOption = (Query) value;
        }
        else {
            if (extraOptions == null) {
                extraOptions = createExtraOptionsMap();
            }
            extraOptions.put(key, value);
        }
    }

    public void remove(Object key) {
        if (Persistence.class.equals(key)) {
            this.persistenceOption = null;
        }
        else if (ConnectionManager.class.equals(key)) {
            this.connectionManagerOption = null;
        }
        else if (QueryEngine.class.equals(key)) {
            this.queryEngineOption = null;
        }
        else if (DeduplicationOption.class.equals(key)) {
            this.deduplicationOption = null;
        }
        else if (IsolationOption.class.equals(key)) {
            this.isolationOption = null;
        }
        else if (FlagsEnabled.class.equals(key)) {
            this.flagsEnabledOption = null;
        }
        else if (FlagsDisabled.class.equals(key)) {
            this.flagsDisabledOption = null;
        }
        else if (Thresholds.class.equals(key)) {
            this.thresholdsOption = null;
        }
        else if (CloseableRequestResources.class.equals(key)) {
            this.closeableRequestResourcesOption = null;
        }
        else if (OrderByOption.class.equals(key)) {
            this.orderByOption = null;
        }
        else if (CollectionQueryEngine.ROOT_QUERY.equals(key)) {
            this.rootQueryOption = null;
        }
        else {
            if (extraOptions != null) {
                extraOptions.remove(key);
            }
        }
    }

    protected HashMap<Object, Object> createExtraOptionsMap() {
        return new HashMap<Object, Object>();
    }

    @Override
    public String toString() {
        Map<Object, Object> allOptions = new HashMap<Object, Object>();
        if (persistenceOption != null) allOptions.put(Persistence.class, persistenceOption);
        if (connectionManagerOption != null) allOptions.put(ConnectionManager.class, connectionManagerOption);
        if (queryEngineOption != null) allOptions.put(QueryEngine.class, queryEngineOption);
        if (deduplicationOption != null) allOptions.put(DeduplicationOption.class, deduplicationOption);
        if (isolationOption != null) allOptions.put(IsolationOption.class, isolationOption);
        if (flagsEnabledOption != null) allOptions.put(FlagsEnabled.class, flagsEnabledOption);
        if (flagsDisabledOption != null) allOptions.put(FlagsDisabled.class, flagsDisabledOption);
        if (thresholdsOption != null) allOptions.put(Thresholds.class, thresholdsOption);
        if (closeableRequestResourcesOption != null) allOptions.put(CloseableRequestResources.class, closeableRequestResourcesOption);
        if (orderByOption != null) allOptions.put(OrderByOption.class, orderByOption);
        if (rootQueryOption != null) allOptions.put(CollectionQueryEngine.ROOT_QUERY, rootQueryOption);
        if (extraOptions != null) allOptions.putAll(extraOptions);

        return "queryOptions(" + allOptions + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryOptions)) return false;

        QueryOptions that = (QueryOptions) o;

        if (persistenceOption != null ? !persistenceOption.equals(that.persistenceOption) : that.persistenceOption != null)
            return false;
        if (connectionManagerOption != null ? !connectionManagerOption.equals(that.connectionManagerOption) : that.connectionManagerOption != null)
            return false;
        if (queryEngineOption != null ? !queryEngineOption.equals(that.queryEngineOption) : that.queryEngineOption != null)
            return false;
        if (deduplicationOption != null ? !deduplicationOption.equals(that.deduplicationOption) : that.deduplicationOption != null)
            return false;
        if (isolationOption != null ? !isolationOption.equals(that.isolationOption) : that.isolationOption != null)
            return false;
        if (flagsEnabledOption != null ? !flagsEnabledOption.equals(that.flagsEnabledOption) : that.flagsEnabledOption != null)
            return false;
        if (flagsDisabledOption != null ? !flagsDisabledOption.equals(that.flagsDisabledOption) : that.flagsDisabledOption != null)
            return false;
        if (thresholdsOption != null ? !thresholdsOption.equals(that.thresholdsOption) : that.thresholdsOption != null)
            return false;
        if (closeableRequestResourcesOption != null ? !closeableRequestResourcesOption.equals(that.closeableRequestResourcesOption) : that.closeableRequestResourcesOption != null)
            return false;
        if (orderByOption != null ? !orderByOption.equals(that.orderByOption) : that.orderByOption != null)
            return false;
        if (rootQueryOption != null ? !rootQueryOption.equals(that.rootQueryOption) : that.rootQueryOption != null)
            return false;
        return extraOptions != null ? extraOptions.equals(that.extraOptions) : that.extraOptions == null;

    }

    @Override
    public int hashCode() {
        int result = persistenceOption != null ? persistenceOption.hashCode() : 0;
        result = 31 * result + (connectionManagerOption != null ? connectionManagerOption.hashCode() : 0);
        result = 31 * result + (queryEngineOption != null ? queryEngineOption.hashCode() : 0);
        result = 31 * result + (deduplicationOption != null ? deduplicationOption.hashCode() : 0);
        result = 31 * result + (isolationOption != null ? isolationOption.hashCode() : 0);
        result = 31 * result + (flagsEnabledOption != null ? flagsEnabledOption.hashCode() : 0);
        result = 31 * result + (flagsDisabledOption != null ? flagsDisabledOption.hashCode() : 0);
        result = 31 * result + (thresholdsOption != null ? thresholdsOption.hashCode() : 0);
        result = 31 * result + (closeableRequestResourcesOption != null ? closeableRequestResourcesOption.hashCode() : 0);
        result = 31 * result + (orderByOption != null ? orderByOption.hashCode() : 0);
        result = 31 * result + (rootQueryOption != null ? rootQueryOption.hashCode() : 0);
        result = 31 * result + (extraOptions != null ? extraOptions.hashCode() : 0);
        return result;
    }
}
