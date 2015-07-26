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


/**
 * If set as a query option causes CQEngine to log its internal decisions during query evaluation to the given sink.
 * The given sink could be System.out in the simplest case.
 * This can be used for debugging, or for testing CQEngine itself.
 *
 * @author niall.gallagher
 */
public class QueryLog {

    final Appendable sink;
    final String lineSeparator;

    public QueryLog(Appendable sink) {
        this(sink, System.getProperty("line.separator"));
    }

    public QueryLog(Appendable sink, String lineSeparator) {
        this.sink = sink;
        this.lineSeparator = lineSeparator;
    }

    public Appendable getSink() {
        return sink;
    }

    public void log(String message) {
        try {
            sink.append(message);
            if (lineSeparator != null) {
                sink.append(lineSeparator);
            }
        }
        catch (Exception e) {
            throw new IllegalStateException("Exception appending to query log", e);
        }
    }
}
