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
package com.googlecode.cqengine.resultset.common;

/**
 * @author Niall Gallagher
 */
public class NoSuchObjectException extends RuntimeException {

    private static final long serialVersionUID = 7269599398244063814L;

    public NoSuchObjectException() {
    }

    public NoSuchObjectException(String s) {
        super(s);
    }
}
