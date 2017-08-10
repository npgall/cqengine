package com.googlecode.cqengine.codegen;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * Provides some general purpose {@link MemberFilter}s which can be used with {@link AttributeSourceGenerator} or
 * {@link AttributeBytecodeGenerator}.
 */
public class MemberFilters {

    public static final MemberFilter ALL_MEMBERS = new MemberFilter() {
        @Override
        public boolean accept(Member member) {
            return true;
        }
    };

    public static final MemberFilter FIELDS_ONLY = new MemberFilter() {
        @Override
        public boolean accept(Member member) {
            return member instanceof Field;
        }
    };

    public static final MemberFilter METHODS_ONLY = new MemberFilter() {
        @Override
        public boolean accept(Member member) {
            return member instanceof Method;
        }
    };

    public static final MemberFilter GETTER_METHODS_ONLY = new MemberFilter() {
        @Override
        public boolean accept(Member member) {
            return member instanceof Method
                    && (member.getName().startsWith("get")
                    || member.getName().startsWith("is")
                    || member.getName().startsWith("has"));
        }
    };

    /**
     * Private constructor, not used.
     */
    MemberFilters() {
    }
}
