package com.googlecode.cqengine.codegen;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * Provides some general purpose {@link MemberFilter}s which can be used with {@link AttributeSourceGenerator} or
 * {@link AttributeBytecodeGenerator}.
 */
public class MemberFilters {

    /**
     * A filter which matches all members (both fields and methods).
     */
    public static final MemberFilter ALL_MEMBERS = new MemberFilter() {
        @Override
        public boolean accept(Member member) {
            return true;
        }
    };

    /**
     * A filter which matches all fields.
     */
    public static final MemberFilter FIELDS_ONLY = new MemberFilter() {
        @Override
        public boolean accept(Member member) {
            return member instanceof Field;
        }
    };

    /**
     * A filter which matches all methods.
     */
    public static final MemberFilter METHODS_ONLY = new MemberFilter() {
        @Override
        public boolean accept(Member member) {
            return member instanceof Method;
        }
    };

    /**
     * A filter which matches all methods which start with "get", "is" and "has" and where the following character
     * is in uppercase.
     */
    public static final MemberFilter GETTER_METHODS_ONLY = new MemberFilter() {
        @Override
        public boolean accept(Member member) {
            return member instanceof Method
                    && (hasGetterPrefix(member.getName(), "get")
                    || hasGetterPrefix(member.getName(), "is")
                    || hasGetterPrefix(member.getName(), "has"));
        }
    };

    static boolean hasGetterPrefix(String memberName, String prefix) {
        int prefixLength = prefix.length();
        return memberName.length() > prefixLength
                && memberName.startsWith(prefix)
                && Character.isUpperCase(memberName.charAt(prefixLength));
    }

    /**
     * Private constructor, not used.
     */
    MemberFilters() {
    }
}
