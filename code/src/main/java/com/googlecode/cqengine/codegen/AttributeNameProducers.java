/**
 * Copyright 2012-2019 Niall Gallagher
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
package com.googlecode.cqengine.codegen;

import com.googlecode.cqengine.codegen.MemberFilters.GetterPrefix;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Provides some functions for use alongside {@link MemberFilters} which produce the name of an attribute from
 * the name of the member, sometimes transforming the name to a more human-readable form.
 */
public class AttributeNameProducers {

    /**
     * Returns the name of the member verbatim.
     */
    public static Function<Member, String> USE_MEMBER_NAMES_VERBATIM = Member::getName;

    /**
     * If the member is a method, whose name starts with a getter prefix, converts the name of the getter method
     * to a more human readable form, by stripping the getter prefix and converting the first character to lowercase.
     * Otherwise, returns the name of the member verbatim.
     * <p>
     * Examples: ["getFoo" -> "foo"], ["isFoo" -> "foo"], ["hasFoo" -> "foo"]
     */
    public static Function<Member, String> USE_HUMAN_READABLE_NAMES_FOR_GETTERS = AttributeNameProducers::getterToHumanReadableName;


    private static String getterToHumanReadableName(Member member) {
        String memberName = member.getName();
        if (member instanceof Method) {
            for (GetterPrefix prefix : GetterPrefix.values()) {
                if (memberName.startsWith(prefix.name()) && memberName.length() > prefix.name().length()) {
                    StringBuilder sb = new StringBuilder(memberName);
                    // Strip the getter prefix...
                    sb.delete(0, prefix.name().length());
                    // Convert the first character to lowercase..
                    sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
                    return sb.toString();
                }
            }
        }
        return memberName;
    }
}
