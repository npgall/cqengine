/**
 * Copyright 2012 Niall Gallagher
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
package com.googlecode.cqengine.examples.codegen;

import com.googlecode.cqengine.codegen.AttributesGenerator;

/**
 * Demonstrates how to auto-generate source code for CQEngine attributes which access fields in a given class, which
 * can then be copy-pasted into the target class.
 *
 * @author Niall Gallagher
 */
public class GenerateAttributesForCopyPaste {

    public static void main(String[] args) {
        System.out.println(AttributesGenerator.generateAttributesForPastingIntoTargetClass(Car.class));
    }
}
