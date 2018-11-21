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
package com.googlecode.cqengine.index.sqlite.support;

import com.googlecode.cqengine.index.sqlite.SQLiteIndex;

/**
 * Flags which can be set in query options (via {@link com.googlecode.cqengine.query.QueryFactory}) which allow the
 * behaviour of {@link SQLiteIndex} to be adjusted.
 *
 * @author niall.gallagher
 */
public class SQLiteIndexFlags {


    /**
     * <p> A 2-values flag that enables externally managed bulk import and specifies it's status.
     */
    public enum BulkImportExternallyManged {
        /**
         * <p> Indicates that the batch is not the last and that the SQLiteIndex shouldn't reinstate the index on the table
         */
        NOT_LAST,

        /**
         * <p> Indicates that the batch is the last batch and that the SQLiteIndex needs to reinstate the index on the table
         */
        LAST;
    };

}
