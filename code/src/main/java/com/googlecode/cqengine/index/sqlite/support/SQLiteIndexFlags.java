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
     * A flag which if enabled causes the SQLiteIndex to temporarily drop the index on a table prior to adding objects,
     * then to restore the index after objects have been added.
     * <p/>
     * This should not be used if other concurrent operations might also be ongoing on the collection. It is intended
     * for use when the collection is first being populated or similar, such as at application startup.
     */
    public static String BULK_IMPORT = "BULK_IMPORT";
}
